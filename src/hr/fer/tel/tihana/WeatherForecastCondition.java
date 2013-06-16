package hr.fer.tel.tihana;

public class WeatherForecastCondition {
	private String dayofWeek = null;
	private Integer tempMin = null;
	private Integer tempMax = null;
	private String condition = null;

	public WeatherForecastCondition() {

	}

	public String getDayofWeek() {
		return dayofWeek;
	}

	public void setDayofWeek(String dayofWeek) {
		this.dayofWeek = dayofWeek;
	}

	public Integer getTempMinCelsius() {
		return tempMin;
	}

	public void setTempMinCelsius(Integer tempMin) {
		this.tempMin = tempMin;
	}

	public Integer getTempMaxCelsius() {
		return tempMax;
	}

	public void setTempMaxCelsius(Integer tempMax) {
		this.tempMax = tempMax;
	}

	public String getCondition() {
		return condition;
	}

	public void setCondition(String condition) {
		this.condition = condition;
	}
}
