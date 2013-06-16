package hr.fer.tel.tihana;

import hr.fer.tel.tihana.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.view.*;

public class Login extends Activity implements View.OnClickListener {

	String loginUrl = "http://www.novo.rezervirajme.hr/mobile/login_json.php?";
	Button bLogin, bReg;
	EditText username, password;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		bLogin = (Button) findViewById(R.id.bLogin);
		bLogin.setOnClickListener(this);
		bReg = (Button) findViewById(R.id.bReg);
		bReg.setOnClickListener(this);
		username = (EditText) findViewById(R.id.edUsername);
		password = (EditText) findViewById(R.id.edPassword);

	}

	public void onClick(View arg0) {

		switch (arg0.getId()) {
		case R.id.bReg:
			Intent otvoriRegistraciju = new Intent(Login.this,
					Registracija.class);
			startActivity(otvoriRegistraciju);
			break;

		case R.id.bLogin:
			LoginData.setUsername(username.getText().toString());
			LoginData.setPassword(password.getText().toString());
			loginUrl = loginUrl + "username=" + LoginData.getUsername() + "&"
					+ "password=" + LoginData.getPassword();
			String response = (String) Json.GetJSONResponse(loginUrl);
			
			int idu=JsonImenaCentara.GetLoginId(response);
			
			if (idu!=-1) {
				
				LoginData.setIdUser(idu);
				Intent i = new Intent(Login.this, RezervirajMeActivity.class);
				startActivity(i);
			} else {
				Toast.makeText(getApplicationContext(), "Krivo korisnicko ime ili lozinka!",
	   					Toast.LENGTH_LONG).show();
				Intent ifalse = new Intent(Login.this, Login.class);
				startActivity(ifalse);
			}
			
			
			break;
		}

	}

	@Override
	protected void onPause() {

		super.onPause();
		finish();
	}

}
