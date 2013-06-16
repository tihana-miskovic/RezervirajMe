package hr.fer.tel.tihana;

import hr.fer.tel.tihana.R;
import java.io.IOException;
import java.net.URL;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

public class RezervirajMeActivity extends Activity implements OnClickListener {

	Button bMyAcc, bCenterList, bAboutUs, bSport, bDate, bTime, bSearch;
	TextView tvSport, tvDate, tvTime, tvLocation, tvTemp;
	Dialog dialog;
	MySportDialog myDialog;
	LocationManager lm;
	Context con = this;

	private int mYear, mMonth, mDay, mHour, mMinute;
	static final boolean useCelsius = true;
	static final int DATE_DIALOG_ID = 0, TIME_DIALOG_ID = 1,
			SPORT_DIALOG_ID = 2;
	String name, towers, city = "";
	double lat = 0;
	double longi = 0;
	String adresaKorisnika = null;
	List<String> infoCentra = null;
	List<String> info = null;
	String distance = null;
	int i = 0, j = 0;

	private void resetSearchData() {
		SearchData.mjesto = city;
		SearchData.sport = "n";
		setTimeDate();
		String monthString, dayString;
		int a = mMonth + 1;
		if (a < 10)
			monthString = "0" + a;
		else
			monthString = "" + a;

		if (mDay < 10)
			dayString = "0" + mDay;
		else
			dayString = "" + mDay;

		SearchData.datum = "" + mYear + "-" + monthString + "-" + dayString;
		SearchData.vrijeme = "" + pad(mHour) + ":" + pad(mMinute);
		SearchData.tipObjekta = "n";
		SearchData.centarZaOpceInformacijeString = "";
		getForecast(1);

		tvTime.setText("");
		tvDate.setText(new StringBuilder().append(mDay).append(".")
				.append(mMonth + 1).append(".").append(mYear).append("."));
		tvSport.setText("Svi sportovi");
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mainscreen);
		System.setProperty("org.joda.time.DateTimeZone.Provider",
				"org.joda.time.tz.UTCProvider");

		setVariables();
		resetSearchData();
		setTimeDate();

		lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

		// ako se pokrece na telefonu sve zakomentirat
		/*
		 * city = "Zagreb"; SearchData.setMjesto(city); adresaKorisnika =
		 * "Unska 3 Zagreb"; tvLocation.setText(adresaKorisnika);
		 */

		lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 10000, 0,
				networkLocationListener);
		lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 100000, 0,
				gpsLocationListener);

		getForecast(1);

	}

	// metode za vremensku prognozu
	public void getForecast(int num) {
		URL url;
		String s = null;
		if (city.equals(""))
			SearchData.setTipObjekta("n");
		else {
			try {
				String requestURL = String.format(
						"http://www.google.com/ig/api?weather=%s",
						Uri.encode(city));
				url = new URL(requestURL.replace(" ", "%20"));

				SAXParserFactory spf = SAXParserFactory.newInstance();
				SAXParser sp = spf.newSAXParser();
				XMLReader xr = sp.getXMLReader();

				GoogleWeatherHandler gwh = new GoogleWeatherHandler();
				xr.setContentHandler(gwh);

				InputSource inStream = new InputSource(url.openStream());
				inStream.setEncoding("ISO-8859-1");
				xr.parse(inStream);

				WeatherSet ws = gwh.getWeatherSet();
				UpdateWeatherInfo uwi = new UpdateWeatherInfo();

				if (num == 1) {
					s = uwi.updateWeatherInfo(ws.getWeatherForecastConditions()
							.get(0));
					tvTemp.setText(s);
				} else if (num == 2) {
					s = uwi.updateWeatherInfo(ws.getWeatherForecastConditions()
							.get(1));
					tvTemp.setText(s);
				} else if (num == 3) {
					s = uwi.updateWeatherInfo(ws.getWeatherForecastConditions()
							.get(2));
					tvTemp.setText(s);
				} else if (num == 4) {
					s = uwi.updateWeatherInfo(ws.getWeatherForecastConditions()
							.get(3));
					tvTemp.setText(s);
				}
			} catch (Exception e) {
				Log.e("WeatherForecaster", "WeatherQueryError", e);
			}
		}
	}

	private void getAddress() {
		Geocoder gcd = new Geocoder(con, Locale.getDefault());
		List<Address> addresses = null;
		try {
			addresses = gcd.getFromLocation(lat, longi, 1);
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (addresses.size() > 0) {
			adresaKorisnika = addresses.get(0).getAddressLine(0).toString();
			city = addresses.get(0).getLocality();

			getForecast(1);
			adresaKorisnika = adresaKorisnika + " " + city;
			tvLocation.setText(adresaKorisnika);

			String upitInfo = String
					.format("http://novo.rezervirajme.hr/mobile/info_o_centru.php?mjesto=%s",
							Uri.encode(city));
			String odgovor1 = (String) Json.GetJSONResponse(upitInfo);
			infoCentra = JsonImenaCentara.GetInfo(odgovor1);

			for (int y = 2; y < infoCentra.size(); y += 3) {
				String pom = infoCentra.get(y).replace(" ", "+");
				String upitUdaljenost = String
						.format("https://maps.googleapis.com/maps/api/distancematrix/json?origins=%s&destinations=%s+%s&mode=driving&language=hr&sensor=true",
								Uri.encode(adresaKorisnika), Uri.encode(pom),
								Uri.encode(city));
				System.out.println(upitUdaljenost);
				String odgovor2 = (String) Json.GetJSONResponse(upitUdaljenost);
				distance = JsonImenaCentara.GetDistance(odgovor2, infoCentra
						.get(y - 1).toString());

			}
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		lm.removeUpdates(networkLocationListener);
		lm.removeUpdates(gpsLocationListener);
	}

	@Override
	protected void onResume() {
		super.onResume();
		resetSearchData();
	}

	@Override
	protected void onStop() {
		super.onStop();
		lm.removeUpdates(networkLocationListener);
		lm.removeUpdates(gpsLocationListener);
	}

	// metode za date i time dijaloge, stavlja trenutno vrijeme u dijalog
	private void setTimeDate() {
		// get vrijeme i datum
		final Calendar c = Calendar.getInstance();
		mYear = c.get(Calendar.YEAR);
		mMonth = c.get(Calendar.MONTH);
		mDay = c.get(Calendar.DAY_OF_MONTH);
		mHour = c.get(Calendar.HOUR_OF_DAY);
		mMinute = c.get(Calendar.MINUTE);

	}

	private void updateDisplayTime() {
		final Calendar c = Calendar.getInstance();
		int tYear = c.get(Calendar.YEAR);
		int tMonth = c.get(Calendar.MONTH);
		int tDay = c.get(Calendar.DAY_OF_MONTH);
		int tHour = c.get(Calendar.HOUR_OF_DAY);
		int tMinute = c.get(Calendar.MINUTE);

		if ((mHour < tHour && mYear == tYear && mMonth == tMonth && mDay == tDay)
				|| (mHour == tHour && mMinute < tMinute && mYear == tYear
						&& mMonth == tMonth && mDay == tDay)) {
			tvTime.setText("Krivo vrijeme");
			bSearch.setVisibility(View.INVISIBLE);

		} else if ((mYear < tYear) || (mYear == tYear && mMonth < tMonth)
				|| (mYear == tYear && mMonth == tMonth && mDay < tDay)) {
			tvTime.setText(new StringBuilder().append(pad(mHour)).append(":")
					.append(pad(mMinute)));
			bSearch.setVisibility(View.INVISIBLE);

		}

		else {
			tvTime.setText(new StringBuilder().append(pad(mHour)).append(":")
					.append(pad(mMinute)));
			bSearch.setVisibility(View.VISIBLE);
		}
	}

	private static String pad(int c) {
		if (c >= 10)
			return String.valueOf(c);
		else
			return "0" + String.valueOf(c);

	}

	private void updateDisplayDate() {
		final Calendar c = Calendar.getInstance();
		int tYear = c.get(Calendar.YEAR);
		int tMonth = c.get(Calendar.MONTH);
		int tDay = c.get(Calendar.DAY_OF_MONTH);
		int tHour = c.get(Calendar.HOUR_OF_DAY);
		int tMinute = c.get(Calendar.MINUTE);

		if ((mYear < tYear) || (mYear == tYear && mMonth < tMonth)
				|| (mYear == tYear && mMonth == tMonth && mDay < tDay)) {
			if (!tvTime.equals("")) {
				tvTime.setText(new StringBuilder().append(pad(mHour))
						.append(":").append(pad(mMinute)));
				SearchData.setVrijeme(tvTime.getText().toString());
			}
			tvDate.setText("Krivi datum");
			bSearch.setVisibility(View.INVISIBLE);

		} else if ((mYear == tYear && mMonth == tMonth && mDay == tDay && mHour < tHour)
				|| (mYear == tYear && mMonth == tMonth && mDay == tDay
						&& mHour == tHour && mMinute < tMinute)) {
			tvTime.setText("Krivo vrijeme");
			bSearch.setVisibility(View.INVISIBLE);
			tvDate.setText(new StringBuilder().append(mDay).append(".")
					.append(mMonth + 1).append(".").append(mYear).append("."));

			if (mMonth <= 8) {
				SearchData.setDatum((new StringBuilder().append(mYear)
						.append("-").append("0").append(mMonth + 1).append("-")
						.append(mDay)).toString());
			} else {
				SearchData.setDatum((new StringBuilder().append(mYear)
						.append("-").append(mMonth + 1).append("-")
						.append(mDay)).toString());
			}
			// trazi prognozu za današnji datum
			if ((mMonth + 1) == 11 || (mMonth + 1) == 12 || (mMonth + 1) == 1
					|| (mMonth + 1) == 2 || (mMonth + 1) == 3)
				SearchData.setTipObjekta("balon");
			else
				getForecast(1);

		} else {
			SearchData.vrijeme = "n";
			if (!tvTime.getText().toString().equals("")) {
				tvTime.setText(new StringBuilder().append(pad(mHour))
						.append(":").append(pad(mMinute)));
				SearchData.setVrijeme(tvTime.getText().toString());
			}

			tvDate.setText(new StringBuilder().append(mDay).append(".")
					.append(mMonth + 1).append(".").append(mYear).append("."));

			if (mMonth <= 8) {
				SearchData.setDatum((new StringBuilder().append(mYear)
						.append("-").append("0").append(mMonth + 1).append("-")
						.append(mDay)).toString());
			} else {
				SearchData.setDatum((new StringBuilder().append(mYear)
						.append("-").append(mMonth + 1).append("-")
						.append(mDay)).toString());
			}

			bSearch.setVisibility(View.VISIBLE);

			// trazi prognozu
			if ((mMonth + 1) == 11 || (mMonth + 1) == 12 || (mMonth + 1) == 1
					|| (mMonth + 1) == 2 || (mMonth + 1) == 3) {
				SearchData.setTipObjekta("balon");
			} else {
				LocalDate fromDate = new LocalDate();
				String date = mYear + "-" + (mMonth + 1) + "-" + mDay;
				DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy-MM-dd");
				DateTime lDate = fmt.parseDateTime(date);
				Days day = Days.daysBetween(fromDate.toDateTimeAtCurrentTime(),
						lDate);

				if (fromDate.getYear() == lDate.getYear()
						&& fromDate.getMonthOfYear() == lDate.getMonthOfYear()
						&& fromDate.getDayOfMonth() == lDate.getDayOfMonth()) {
					getForecast(1);
					if (tvTime.getText().toString().equals(""))
						SearchData.vrijeme = "" + pad(mHour) + ":"
								+ pad(mMinute);
				} else if (day.toString().equals("P0D")) {
					getForecast(2);
				} else if (day.toString().equals("P1D")) {
					getForecast(3);
				} else if (day.toString().equals("P2D")) {
					getForecast(4);
				} else {
					tvTemp.setText("");
					SearchData.setTipObjekta("n");
				}
			}
		}
	}

	private TimePickerDialog.OnTimeSetListener mTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
		public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
			if (minute == 0) {
				mMinute = 00;
				mHour = hourOfDay;
			} else {
				mMinute = 00;
				mHour = hourOfDay + 1;
			}

			tvTime.setVisibility(View.VISIBLE);
			updateDisplayTime();
			SearchData.setVrijeme(tvTime.getText().toString());
		}
	};

	private DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {
		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {
			mYear = year;
			mMonth = monthOfYear;
			mDay = dayOfMonth;
			updateDisplayDate();
		}
	};

	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case DATE_DIALOG_ID:
			return new DatePickerDialog(this, mDateSetListener, mYear, mMonth,
					mDay);
		case TIME_DIALOG_ID:
			return new TimePickerDialog(this, mTimeSetListener, mHour, mMinute,
					true);
		}
		return null;
	}

	// inicijalizacija varijabli
	private void setVariables() {

		bMyAcc = (Button) findViewById(R.id.bMyAcc);
		bCenterList = (Button) findViewById(R.id.bList);
		bAboutUs = (Button) findViewById(R.id.bAboutUs);
		bSport = (Button) findViewById(R.id.bSport);
		bDate = (Button) findViewById(R.id.bDate);
		bTime = (Button) findViewById(R.id.bTime);
		tvSport = (TextView) findViewById(R.id.tvSport);
		tvDate = (TextView) findViewById(R.id.tvDate);
		tvTime = (TextView) findViewById(R.id.tvTime);
		bSearch = (Button) findViewById(R.id.bSearch);
		tvLocation = (TextView) findViewById(R.id.tvLocation);
		tvTemp = (TextView) findViewById(R.id.tvTemp);

		bMyAcc.setOnClickListener(this);
		bSearch.setOnClickListener(this);
		bCenterList.setOnClickListener(this);
		bAboutUs.setOnClickListener(this);

		myDialog = new MySportDialog(this, "", new OnReadyListener());

		bDate.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				showDialog(DATE_DIALOG_ID);
			}
		});

		bTime.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				showDialog(TIME_DIALOG_ID);
			}
		});

		bSport.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				myDialog.show();
			}
		});

	}

	// onClick za tri gumba izbornika + Search
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.bList:
			Intent openListOfCenters = new Intent(
					"android.intent.action.POPISCENTARA");
			startActivity(openListOfCenters);
			break;
		case R.id.bAboutUs:
			Intent openAboutUs = new Intent("android.intent.action.ONAMA");
			startActivity(openAboutUs);
			break;
		case R.id.bMyAcc:
			Intent openMyAcc = new Intent(
					"android.intent.action.POPISREZERVACIJA");
			startActivity(openMyAcc);
			break;
		case R.id.bSearch:
			Intent openPrikazPonude = new Intent(
					"android.intent.action.PRIKAZPONUDE");
			startActivity(openPrikazPonude);
			break;
		}

	}

	// metoda za SportDialog
	private class OnReadyListener implements MySportDialog.ReadyListener {
		public void ready(String name) {
			tvSport.setText(name);
			bSearch.setVisibility(View.VISIBLE);

		}
	}

	private final LocationListener gpsLocationListener = new LocationListener() {
		// METODE ZA LOKACIJU
		public void onLocationChanged(Location location) {
			lm.removeUpdates(gpsLocationListener);
			lat = location.getLatitude();
			longi = location.getLongitude();
			getAddress();
			if (i == 0) {
				SearchData.setMjesto(city);
				i++;
			}
		}

		public void onProviderDisabled(String provider) {
			Toast.makeText(getApplicationContext(), "Gps Disabled",
					Toast.LENGTH_SHORT).show();
		}

		public void onProviderEnabled(String provider) {
			Toast.makeText(getApplicationContext(), "Gps Enabled",
					Toast.LENGTH_SHORT).show();
		}

		public void onStatusChanged(String provider, int status, Bundle extras) {
			// TODO Auto-generated method stub
		}

	};

	private final LocationListener networkLocationListener = new LocationListener() {

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
		}

		@Override
		public void onProviderEnabled(String provider) {
			Toast.makeText(getApplicationContext(), "Network Provider Enabled",
					Toast.LENGTH_SHORT).show();
		}

		@Override
		public void onProviderDisabled(String provider) {
			Toast.makeText(getApplicationContext(),
					"Network Provider Disabled", Toast.LENGTH_SHORT).show();
		}

		@Override
		public void onLocationChanged(Location location) {
			lat = location.getLatitude();
			longi = location.getLongitude();
			getAddress();
			if (i == 0) {
				SearchData.setMjesto(city);
				i++;
			}

		}
	};

}