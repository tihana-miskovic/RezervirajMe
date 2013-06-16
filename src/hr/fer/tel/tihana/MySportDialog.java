package hr.fer.tel.tihana;

import hr.fer.tel.tihana.R;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

public class MySportDialog extends Dialog implements OnCheckedChangeListener {

	private String sportName, sportSend="n";
	private ReadyListener readyListener;
	RadioGroup rgSport;

	public MySportDialog(Context context, String name,
			ReadyListener readyListener) {
		super(context);
		this.sportName = name;
		this.readyListener = readyListener;
	}

	public interface ReadyListener {
		public void ready(String name);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sportdialog);
		setTitle("Odaberite sport");
		Button bSave = (Button) findViewById(R.id.bSave);
		RadioGroup selectionList = (RadioGroup) findViewById(R.id.rgAnswers);
		selectionList.setOnCheckedChangeListener(this);
		bSave.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				readyListener.ready(sportName);
				MySportDialog.this.dismiss();
				SearchData.setSport(sportSend);
			}
		});
	}

	public void onCheckedChanged(RadioGroup arg0, int arg1) {
		switch (arg1) {
		case R.id.rbMN:
			sportName = "Mali nogomet";
			sportSend = "mali%20nogomet";
			break;
		case R.id.rbT:
			sportName = "Tenis";
			sportSend = "tenis";
			break;
		case R.id.rbP:
			sportName = "Plivanje";
			sportSend = "plivanje";
			break;
		case R.id.rbST:
			sportName = "Stolni tenis";
			sportSend = "stolni%20tenis";
			break;
		case R.id.rbPB:
			sportName = "Paintball";
			sportSend = "paintball";
			break;
		case R.id.rbAll:
			sportName = "Svi sportovi";
			sportSend = "n";

		}
	}
}
