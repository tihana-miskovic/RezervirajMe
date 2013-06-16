package hr.fer.tel.tihana;

import hr.fer.tel.tihana.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class PrikazSatnice extends Activity implements OnItemClickListener {

	private int mYear, mMonth, mDay, mHour, mMinute;

	String upit = "http://www.novo.rezervirajme.hr/mobile/upis_rezervacije.php?field_index=1&";
	String idPonude;
	List<String> nizZaSlanje;

	String upit2;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.searchresult);
		String link = getIntent().getStringExtra(PrikazPonude.url_ponude);

		idPonude = getIntent().getStringExtra(PrikazPonude.id_ponude);

		String response = (String) Json.GetJSONResponse(link);
		List<PodaciRezervacije> listaR = JsonImenaCentara
				.GetReservationFill(response);

		// pretraga radnog vremena
		link = "http://www.novo.rezervirajme.hr/mobile/radno_vrijeme.php?offer="
				+ idPonude;

		response = (String) Json.GetJSONResponse(link);
		List<String> radnoVrijemeJson = JsonImenaCentara
				.GetRadnoVrijeme(response);
		String otvorenOdTemp = radnoVrijemeJson.get(0);

		String otvorenOd = radnoVrijemeJson.get(0).substring(0, 2);
		String otvorenDo = radnoVrijemeJson.get(1).substring(0, 2);
		int otvorenOdRadnoVrijeme = Integer.parseInt(otvorenOd);

		// + 1 h za otvorenje u slucaju pocetka rada na nepuni sat
		if ((Integer.parseInt(otvorenOdTemp.substring(3, 5))) > 0) {
			otvorenOdRadnoVrijeme++;

		}
		setTimeDate();

		String mMonthS = "", mDayS = "";
		if (mMonth < 10)
			mMonthS = "" + "0" + mMonth;
		else
			mMonthS = "" + mMonth;
		if (mDay < 10)
			mDayS = "" + "0" + mDay;
		else
			mDayS = "" + mDay;
		String sistemDatum = "" + mYear + "-" + mMonthS + "-" + mDayS;

		String datumTemp = SearchData.getDatum();
		if (mHour >= otvorenOdRadnoVrijeme
				&& (sistemDatum.compareTo(datumTemp) == 0)) {
			otvorenOdRadnoVrijeme = (mHour + 1);
		}

		int otvorenDoRadnoVrijeme = Integer.parseInt(otvorenDo);
		int radnoVrijeme = otvorenDoRadnoVrijeme - otvorenOdRadnoVrijeme;

		if (radnoVrijeme < 0)
			radnoVrijeme = 0;

		String[][] nizIspisaVrijednostiRezervacija = new String[radnoVrijeme][2];
		int temp = otvorenOdRadnoVrijeme;
		String tempString;

		// punjenje matrice rezervacija s definiranim terminima; postavljanje
		// svih termina na slobodan
		for (int i = 0; i < radnoVrijeme; i++) {
			nizIspisaVrijednostiRezervacija[i][1] = "slobodno";
			tempString = "" + temp;
			if (temp < 10)
				tempString = "0" + tempString;
			nizIspisaVrijednostiRezervacija[i][0] = (tempString) + ":00";

			temp++;
			if (temp == 24)
				temp = 0;

		}

		for (int i = 0; i < radnoVrijeme; i++) {
			// System.out.println("Pocetak prve for petlje po radnom vremenu; prolazak broj: "+i);
			for (int k = 0; k < listaR.size(); k++) {
				if (((((listaR.get(k).getStart_time()).substring(11, 14))
						.indexOf((nizIspisaVrijednostiRezervacija[i][0])
								.substring(0, 2)) != -1) && ((listaR.get(k)
						.getStart_time()).indexOf(SearchData.getDatum())) != -1)
						|| ((((listaR.get(k).getEnd_time()).substring(11, 14))
								.indexOf((nizIspisaVrijednostiRezervacija[i][0])
										.substring(0, 2)) != -1)
								&& (((listaR.get(k).getEnd_time())
										.indexOf(SearchData.getDatum())) != -1) && Integer
								.parseInt(listaR.get(k).getEnd_time()
										.substring(14, 16)) > 0)) {

					nizIspisaVrijednostiRezervacija[i][1] = "";
					nizIspisaVrijednostiRezervacija[i][0] = "**********************";

					break;
				}
			}
		}

		nizZaSlanje = new ArrayList<String>();
		for (int z = 0; z < radnoVrijeme; z++) {
			nizZaSlanje.add(nizIspisaVrijednostiRezervacija[z][0] + " "
					+ nizIspisaVrijednostiRezervacija[z][1]);

		}

		GridView gridView = (GridView) findViewById(R.id.gridSearch);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, nizZaSlanje);
		gridView.setAdapter(adapter);
		gridView.setOnItemClickListener(this);

	}

	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		TextView vrijednost = (TextView) arg1;

		if ((vrijednost.getText().toString()).indexOf("********") != -1) {
			Toast.makeText(getApplicationContext(),
					"Termin je prošao ili je veæ rezerviran!",
					Toast.LENGTH_LONG).show();
		}
		// dodaj rezervaciju preko php skripte u bazu
		else {

			String user = "user=" + LoginData.getIdUser() + "&";
			String offer = "offer=" + idPonude + "&";
			int vrijemeKraja = Integer.parseInt(nizZaSlanje.get(arg2)
					.substring(0, 2)) + 1;
			String start_time = "start_time=" + SearchData.getDatum() + "%20"
					+ nizZaSlanje.get(arg2).substring(0, 5) + ":00&";
			String end_time = "end_time=" + SearchData.getDatum() + "%20"
					+ vrijemeKraja + ":00:00";
			String note = "note=Rezervacija%20korisnika";
			upit = upit + user + offer + start_time + end_time + "&" + note;
			System.out.println(upit);
			upit2 = "http://www.novo.rezervirajme.hr/mobile/provjera_rezervacije.php?"
					+ user + offer + start_time + end_time;

			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle("Potvrda rezervacije...")
					.setMessage("Želite li rezervirati odabrani termin?")
					.setCancelable(false)
					.setPositiveButton("Da",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									Json.GetJSONResponse(upit);
									System.out.println(upit2);
									if (Json.GetJSONResponse(upit2)
											.equals("ok")) {
										Toast.makeText(getApplicationContext(),
												"Termin je rezerviran!",
												Toast.LENGTH_LONG).show();
										Intent i = new Intent(
												PrikazSatnice.this,
												RezervirajMeActivity.class);
										startActivity(i);
									} else {
										Toast.makeText(
												getApplicationContext(),
												"Termin NIJE rezerviran! Pokušajte ponovno!",
												Toast.LENGTH_LONG).show();

									}

								}
							})
					.setNegativeButton("Ne",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									dialog.cancel();
								}
							});
			builder.show();

		}

	}

	private void setTimeDate() {

		final Calendar c = Calendar.getInstance();
		mYear = c.get(Calendar.YEAR);
		mMonth = c.get(Calendar.MONTH);
		mMonth = mMonth + 1;
		mDay = c.get(Calendar.DAY_OF_MONTH);
		mHour = c.get(Calendar.HOUR_OF_DAY);
		mMinute = c.get(Calendar.MINUTE);

	}

	@Override
	protected void onPause() {
		super.onPause();
		finish();
	}
}
