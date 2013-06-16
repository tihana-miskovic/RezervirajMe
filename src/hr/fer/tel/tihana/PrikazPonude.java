package hr.fer.tel.tihana;

import hr.fer.tel.tihana.Json;
import hr.fer.tel.tihana.JsonImenaCentara;
import hr.fer.tel.tihana.PrikazPonude;
import hr.fer.tel.tihana.PrikazSatnice;
import hr.fer.tel.tihana.R;
import hr.fer.tel.tihana.SearchData;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class PrikazPonude extends Activity implements OnItemClickListener {

	public final static String url_ponude = "hr.fer.tel.tihana.url_ponude";
	public final static String id_ponude = "hr.fer.tel.tihana.id_ponude";

	private String upitPonuda = "http://novo.rezervirajme.hr/mobile/popis_rez.php?";
	String akcija;
	List<String> ponudaCentara = new ArrayList<String>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.searchresult);

		if (SearchData.getSport().equals("n")
				|| SearchData.getVrijeme().equals("n"))
			akcija = "popis1";
		else
			akcija = "popis";

		String mjesto = Uri.encode(SearchData.mjesto);
		String urlUpit = upitPonuda + "akcija=" + akcija + "&mjesto=" + mjesto
				+ "&sport=" + SearchData.getSport() + "&vrijeme="
				+ SearchData.getVrijeme() + "&objekt="
				+ SearchData.getTipObjekta();
		System.out.println(urlUpit);

		String odgovor = (String) Json.GetJSONResponse(urlUpit);

		ponudaCentara = JsonImenaCentara.GetPopisPonude(odgovor);

		if (ponudaCentara.isEmpty()) {
			Toast.makeText(getApplicationContext(),
					"Nema rezultata za zadane parametre", Toast.LENGTH_LONG)
					.show();
		} else {
			GridView gridV = (GridView) findViewById(R.id.gridSearch);
			ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
					android.R.layout.simple_list_item_1, ponudaCentara);
			gridV.setAdapter(adapter);
			gridV.setOnItemClickListener(this);
		}
	}

	public void onItemClick(AdapterView<?> arg0, View v, int position, long arg3) {
		Intent i = new Intent(PrikazPonude.this, PrikazSatnice.class);
		String idPonude = "", imeCentra = "";
		imeCentra = ponudaCentara.get(position);
		String t;
		for (int x = 0; x < JsonImenaCentara.listaPovezivanja.size(); x++) {
			t = JsonImenaCentara.listaPovezivanja.get(x);
			if (t.indexOf(imeCentra) != -1) {
				idPonude = t.substring(imeCentra.length());
				break;
			}
		}

		String link = "http://novo.rezervirajme.hr/mobile/reservation_fill.php?offer=";
		link = link + idPonude;
		i.putExtra(id_ponude, idPonude);
		i.putExtra(url_ponude, link);
		System.out.println(idPonude);
		startActivity(i);

	}
}
