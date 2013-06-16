package hr.fer.tel.tihana;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class GoogleWeatherHandler extends DefaultHandler {

	private WeatherSet myWeatherSet = null;
	private boolean in_current_conditions = false;
	private boolean in_forecast_conditions = false;

	private boolean usingSITemperature = false; // false means Fahrenheit

	public WeatherSet getWeatherSet() {
		return this.myWeatherSet;
	}

	@Override
	public void startDocument() throws SAXException {
		this.myWeatherSet = new WeatherSet();
	}

	@Override
	public void endDocument() throws SAXException {
		// Nothing
	}

	@Override
	public void startElement(String namespaceURI, String localName,
			String qName, Attributes atts) throws SAXException {

		if (localName.equals("current_conditions")) {
			this.myWeatherSet.setWeatherCurrentCondition(new WeatherCurrentCondition());
			this.in_current_conditions = true;
		} else if (localName.equals("forecast_conditions")) {
			this.myWeatherSet.getWeatherForecastConditions().add(new WeatherForecastCondition());
			this.in_forecast_conditions = true;
		} else {
			String dataAttribute = atts.getValue("data");
			
			if (localName.equals("unit_system")) {
				if (dataAttribute.equals("SI"))
					this.usingSITemperature = true;
			}
			else if (localName.equals("day_of_week")) {
				if (this.in_current_conditions) {
					this.myWeatherSet.getWeatherCurrentCondition().setDayofWeek(dataAttribute);
				} else if (this.in_forecast_conditions) {
					this.myWeatherSet.getLastWeatherForecastCondition().setDayofWeek(dataAttribute);
				}
			} else if (localName.equals("condition")) {
				if (this.in_current_conditions) {
					this.myWeatherSet.getWeatherCurrentCondition().setCondition(dataAttribute);
				} else if (this.in_forecast_conditions) {
					this.myWeatherSet.getLastWeatherForecastCondition().setCondition(dataAttribute);
				}
			}
			else if (localName.equals("temp_c")) {
				this.myWeatherSet.getWeatherCurrentCondition().setTempCelcius(Integer.parseInt(dataAttribute));
			} else if (localName.equals("low")) {
				int temp = Integer.parseInt(dataAttribute);
				if (this.usingSITemperature) {
					this.myWeatherSet.getLastWeatherForecastCondition().setTempMinCelsius(temp);
				} else {
					this.myWeatherSet.getLastWeatherForecastCondition().setTempMinCelsius(fahrenheitToCelsius(temp));
				}
			} else if (localName.equals("high")) {
				int temp = Integer.parseInt(dataAttribute);
				if (this.usingSITemperature) {
					this.myWeatherSet.getLastWeatherForecastCondition()
							.setTempMaxCelsius(temp);
				} else {
					this.myWeatherSet.getLastWeatherForecastCondition()
							.setTempMaxCelsius(
									fahrenheitToCelsius(temp));
				}
			}
		}
	}

	@Override
	public void endElement(String namespaceURI, String localName, String qName)
			throws SAXException {
		if (localName.equals("current_conditions")) {
			this.in_current_conditions = false;
		} else if (localName.equals("forecast_conditions")) {
			this.in_forecast_conditions = false;
		}
	}

	public static int fahrenheitToCelsius(int tFahrenheit) {
		float m, n;
		m = ((5.0f / 9.0f) * (tFahrenheit - 32));
		n = m + 0.5f;
		if ((int)Math.max(m, n) > (int) m){
			return (int) n;
		} else {
			return (int) m;
		}
	}

}

