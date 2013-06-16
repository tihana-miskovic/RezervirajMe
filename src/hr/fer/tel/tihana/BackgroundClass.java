package hr.fer.tel.tihana;

import hr.fer.tel.tihana.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class BackgroundClass extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.backgroundclass);
		Thread timer = new Thread() {

			public void run() {
				try {
					sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				} finally {
					Intent openStartingPoint = new Intent(
							"android.intent.action.LOGIN");
					startActivity(openStartingPoint);
				}

			}
		};
		timer.start();
	}

	@Override
	protected void onPause() {

		super.onPause();
		finish();
	}
}
