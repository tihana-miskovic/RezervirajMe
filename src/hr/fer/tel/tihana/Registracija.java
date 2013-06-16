package hr.fer.tel.tihana;

import hr.fer.tel.tihana.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Registracija extends Activity implements View.OnClickListener {

	EditText username, password, ime, prezime, email, telefon;
	Button bRegister;
	String registerUrl = "http://www.novo.rezervirajme.hr/mobile/registracija.php?";

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.registracija);
		bRegister = (Button) findViewById(R.id.bRegister);
		bRegister.setOnClickListener(this);
		username = (EditText) findViewById(R.id.edUsernameR);
		password = (EditText) findViewById(R.id.edPasswordR);
		ime = (EditText) findViewById(R.id.edImeR);
		prezime = (EditText) findViewById(R.id.edPrezimeR);
		email = (EditText) findViewById(R.id.edEmail);
		telefon = (EditText) findViewById(R.id.edTel);

	}

	public void onClick(View arg0) {

		if (username.getText().toString().equals("")
				|| password.getText().toString().equals("")
				|| ime.getText().toString().equals("")
				|| prezime.getText().toString().equals("")
				|| email.getText().toString().equals("")
				|| telefon.getText().toString().equals("")) {

			Toast.makeText(
					getApplicationContext(),
					"Neuspješna registracija. Molimo ispunite sva polja. Hvala!",
					Toast.LENGTH_LONG).show();
			Intent vrati = new Intent(Registracija.this, Login.class);
			startActivity(vrati);
		}

		String usernameString = "username=" + username.getText().toString()
				+ "&";
		String passwordString = "password=" + password.getText().toString()
				+ "&";
		String imeString = "first_name=" + ime.getText().toString() + "&";
		String emailString = "email=" + email.getText().toString().equals("")
				+ "&";
		String telefonString = "tel=" + telefon.getText().toString().equals("")
				+ "&";
		String prezimeString = "last_name=" + prezime.getText().toString();
		// prezime ide na kraju slaganja urlRegistracija
		String urlRegistracija = registerUrl + usernameString + passwordString
				+ imeString
				// + emailString +telefonString
				+ prezimeString;
		String urlProvjera = "http://www.novo.rezervirajme.hr/mobile/registracija_provjera.php?"
				+ usernameString
				+ passwordString
				+ imeString
				+ emailString
				+ telefonString + prezimeString;
		System.out.println(urlRegistracija);
		System.out.println(urlProvjera);
		System.out.println("******");
		if (Json.GetJSONResponse(urlProvjera).equals("notok")) {
			Toast.makeText(getApplicationContext(), "Korisnicko ime zauzeto!",
					Toast.LENGTH_LONG).show();
		} else {
			Json.GetJSONResponse(urlRegistracija);

			if (Json.GetJSONResponse(urlProvjera).equals("notok")) {
				Toast.makeText(getApplicationContext(),
						"Hvala na registraciji!", Toast.LENGTH_LONG).show();
			} else {

				Toast.makeText(getApplicationContext(),
						"Molimo ponovite registraciju!", Toast.LENGTH_LONG)
						.show();

			}
		}

		Intent i = new Intent(Registracija.this, Login.class);
		startActivity(i);

	}

	@Override
	protected void onPause() {

		super.onPause();
		finish();
	}

}
