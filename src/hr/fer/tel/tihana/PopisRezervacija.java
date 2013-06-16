package hr.fer.tel.tihana;

import hr.fer.tel.tihana.R;

import java.util.Calendar;
import java.util.List;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;

import android.widget.GridView;
import android.widget.Toast;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;

import android.widget.TextView;

public class PopisRezervacija extends Activity implements OnItemClickListener {

	String upitZaBrisanje = "http://www.novo.rezervirajme.hr/mobile/brisanje_rezerv.php?id=";
	String linkPopisRezervacija = "http://www.novo.rezervirajme.hr/mobile/list_reservations.php?id=";
	private int mYear, mMonth, mDay, mHour, mMinute;
	List<String> popisR;
	List<String> popisR2;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.searchresult);
		linkPopisRezervacija = linkPopisRezervacija + LoginData.idUser;
		String odgovor = (String) Json.GetJSONResponse(linkPopisRezervacija);
		String odgovor2 = (String) Json.GetJSONResponse(linkPopisRezervacija);

		popisR = JsonImenaCentara.GetReservationList(odgovor);
		popisR2 = JsonImenaCentara.GetReservationView(odgovor2);

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
		int sistemVrijeme = mHour;

		int y = 0;
		for (int z = 0; z < popisR.size(); z++) {

			String datumTemp = popisR.get(z + 3);
			String vrijemeTemp = popisR.get(z + 4);
			datumTemp = datumTemp.substring(7);
			int vrijemeEndTemp = Integer.parseInt(vrijemeTemp.substring(8, 10));

			if ((sistemDatum.compareTo(datumTemp) == 0 && sistemVrijeme > vrijemeEndTemp)
					|| (sistemDatum.compareTo(datumTemp) > 0)) {
				popisR.remove(z + 4);
				popisR.remove(z + 3);
				popisR.remove(z + 2);
				popisR.remove(z + 1);
				popisR.remove(z);

				popisR2.remove(y + 1);
				popisR2.remove(y);

				if (popisR.size() == 0)
					break;
				z = -1;
				y = 0;
				System.out.println("Z= " + z);
				continue;
			}
			z = z + 4;
			y = y + 2;
		}

		if (popisR.isEmpty()) {
			Toast.makeText(getApplicationContext(),
					"Nemate zabilježenih rezervacija", Toast.LENGTH_LONG)
					.show();
		} else {
			GridView gridPopis = (GridView) findViewById(R.id.gridSearch);
			ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
					android.R.layout.simple_list_item_1, popisR2);
			gridPopis.setAdapter(adapter);
			gridPopis.setOnItemClickListener(this);
		}

	}

	public void onItemClick(AdapterView<?> arg0, View arg1, int position,
			long id) {
		TextView y = (TextView) arg1;
		String x = y.getText().toString();

		if (x.indexOf("rezervaciju iznad") != -1) {
			int idRezervacije;
			int pos2 = 0;

			for (int pos1 = 1; pos1 <= position; pos1 = pos1 + 2) {
				if (pos1 == position) {

					idRezervacije = Integer.parseInt(popisR.get(pos2));
					upitZaBrisanje = upitZaBrisanje + idRezervacije;

					AlertDialog.Builder builder = new AlertDialog.Builder(this);
					builder.setTitle("Potvrda brisanja...")
							.setMessage(
									"Želite li otkazati odabranu rezervaciju?")
							.setCancelable(false)
							.setPositiveButton("Da",
									new DialogInterface.OnClickListener() {
										public void onClick(
												DialogInterface dialog, int id) {
											Json.GetJSONResponse(upitZaBrisanje);
											Toast.makeText(
													getApplicationContext(),
													"Rezervacija otkazana",
													Toast.LENGTH_LONG).show();
											Intent i = new Intent(
													PopisRezervacija.this,
													RezervirajMeActivity.class);
											startActivity(i);
										}
									})
							.setNegativeButton("Ne",
									new DialogInterface.OnClickListener() {
										public void onClick(
												DialogInterface dialog, int id) {
											dialog.cancel();
										}
									});
					builder.show();
				}
				pos2 = pos2 + 5;
			}
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
		// TODO Auto-generated method stub
		super.onPause();
		finish();
	}

}
