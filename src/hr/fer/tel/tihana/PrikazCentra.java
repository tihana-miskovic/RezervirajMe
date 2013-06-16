package hr.fer.tel.tihana;

import hr.fer.tel.tihana.R;
import org.json.JSONException;
import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

/**
 * @author Enes Zejnilovic
 * 
 */

public class PrikazCentra extends Activity {

	TextView tv1;
	TextView tv2;
	TextView tv3;
	TextView tv4;
	TextView tv5;
	TextView tv6;
	TextView tv7;
	TextView tv8;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.prikazcentra);


		String link = getIntent().getStringExtra(
				PopisCentara.url_centra);

		String response = (String) Json.GetJSONResponse(link);
		
		
			PodaciCentra x= new PodaciCentra();
			try {
				x= JsonImenaCentara.InfoCentra(response);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		
		
		tv1= (TextView) findViewById(R.id.tvIME1);
		tv1.setText(x.imeC);
		tv2= (TextView) findViewById(R.id.tv2);
		tv2.setText("Adresa: "+x.adresaC);
		tv3= (TextView) findViewById(R.id.tv3);
		tv3.setText("Kvart: "+x.kvartC);
		tv4= (TextView) findViewById(R.id.tv4);
		tv4.setText("Grad: "+x.gradC);
		tv5= (TextView) findViewById(R.id.tv5);
		tv5.setText("Email: "+x.emailC);
		tv6= (TextView) findViewById(R.id.textView1);
		tv6.setText("Cijena: "+x.cijenaC);
		tv7= (TextView) findViewById(R.id.textView2);
		tv7.setText("Kontakt telefon: "+x.brojTelefonaC);
		tv8= (TextView) findViewById(R.id.textView3);
		tv8.setText("Opis centra: "+x.opisC);
		
		
	
	}

}