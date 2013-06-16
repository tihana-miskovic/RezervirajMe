package hr.fer.tel.tihana;

public class WeatherCurrentCondition {
	private String dayofWeek = null;
	private Integer tempCelcius = null;
	private String condition = null;

	public String getDayofWeek() {
		return this.dayofWeek;
	}

	public void setDayofWeek(String dayofWeek) {
		this.dayofWeek = dayofWeek;
	}

	public Integer getTempCelcius() {
		return this.tempCelcius;
	}

	public void setTempCelcius(Integer temp) {
		this.tempCelcius = temp;
	}

	public String getCondition() {
		return this.condition;
	}

	public void setCondition(String condition) {
		this.condition = condition;
	}
}
