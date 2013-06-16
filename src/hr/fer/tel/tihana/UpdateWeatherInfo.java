package hr.fer.tel.tihana;

import hr.fer.tel.tihana.SearchData;
import java.net.MalformedURLException;

public class UpdateWeatherInfo {
	public String updateWeatherInfo(WeatherForecastCondition aWFIS)
			throws MalformedURLException {

		int tempMin = aWFIS.getTempMinCelsius();
		int tempMax = aWFIS.getTempMaxCelsius();
		String condition = aWFIS.getCondition();

		String stanje = "" + tempMin + "C - " + tempMax + "C " + condition;
		System.out.println(tempMin + " " + tempMax + " " + condition);

		if ((condition.equals("Partly Sunny") || condition.equals("Sunny")
				|| condition.equals("Clear")
				|| condition.equals("Mostly Sunny")
				|| condition.equals("Windy") || condition.equals("Cloudy")
				|| condition.equals("Fog") || condition.equals("Dust") || condition
					.equals("Partly Cloudy")) && (tempMin > 15 || tempMax > 19))
			SearchData.setTipObjekta("n");
		else
			SearchData.setTipObjekta("balon");

		return stanje;
	}

	public void updateWeatherInfo(WeatherCurrentCondition aWCIS)
			throws MalformedURLException {

		String conditionCurr = aWCIS.getCondition();
		int tempCelsius = aWCIS.getTempCelcius();
		System.out.println(tempCelsius + conditionCurr);

	}

}
