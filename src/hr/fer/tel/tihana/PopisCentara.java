package hr.fer.tel.tihana;

import hr.fer.tel.tihana.R;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.AdapterView.OnItemClickListener;

public class PopisCentara extends Activity implements OnItemClickListener {

	public final static String url_centra = "hr.fer.tel.tihana.url_centra";
	List<String> centersArray;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.popiscentara);

		String url = "http://www.novo.rezervirajme.hr/mobile/list_centers.php";
		String response = (String) Json.GetJSONResponse(url);
		centersArray = JsonImenaCentara.GetCentersList(response);

		GridView gv = (GridView) findViewById(R.id.grid);
		ArrayAdapter<String> aa = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, centersArray);
		gv.setAdapter(aa);
		gv.setOnItemClickListener(this);

	}

	public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
		Intent i = new Intent(PopisCentara.this, PrikazCentra.class);
		String idCentra = "", imeCentra = "";
		imeCentra = centersArray.get(position);
		String t;
		for (int x = 0; x < JsonImenaCentara.listaPovezivanja.size(); x++) {
			t = JsonImenaCentara.listaPovezivanja.get(x);
			if (t.indexOf(imeCentra) != -1) {
				idCentra = t.substring(imeCentra.length());
				break;
			}
		}

		String link = "http://novo.rezervirajme.hr/mobile/center_info.php?id=";
		link = link + idCentra;

		i.putExtra(url_centra, link);
		startActivity(i);

	}
}
