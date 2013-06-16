package hr.fer.tel.tihana;

public class SearchData {

	public static String sport = "n";
	public static String mjesto = "";
	public static String vrijeme = "n";
	public static String datum = "";
	public static String tipObjekta = "n";
	public static String centarZaOpceInformacijeString = "";

	public static String getCentarZaOpceInformacijeString() {
		return centarZaOpceInformacijeString;
	}

	public static void setCentarZaOpceInformacijeString(
			String centarZaOpceInformacijeString) {
		SearchData.centarZaOpceInformacijeString = centarZaOpceInformacijeString;
	}

	public static String getSport() {
		return sport;
	}

	public static void setSport(String sport) {
		SearchData.sport = sport;
	}

	public static String getMjesto() {
		return mjesto;
	}

	public static void setMjesto(String mjesto) {
		SearchData.mjesto = mjesto;
	}

	public static String getVrijeme() {
		return vrijeme;
	}

	public static void setVrijeme(String vrijeme) {

		SearchData.vrijeme = vrijeme;
		SearchData.vrijeme += ":00";
	}

	public static String getDatum() {
		return datum;
	}

	public static void setDatum(String datum) {
		SearchData.datum = datum;
	}

	public static String getTipObjekta() {
		return tipObjekta;
	}

	public static void setTipObjekta(String tipObjekta) {
		SearchData.tipObjekta = tipObjekta;
	}

}
