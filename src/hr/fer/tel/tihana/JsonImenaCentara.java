package hr.fer.tel.tihana;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class JsonImenaCentara {

	static List<String> listaPovezivanja;
	static List<String> listaInfo = new ArrayList<String>();

	static String prvi = "";
	static String drugi = "";

	public static List<String> GetCentersList(String json_string) {
		List<JSONObject> centers_json = new ArrayList<JSONObject>();
		List<String> centers = new ArrayList<String>();
		listaPovezivanja = new ArrayList<String>();

		try {
			JSONArray json_array = new JSONArray(json_string);
			for (int i = 0; i < json_array.length(); ++i) {
				centers_json.add(new JSONObject(json_array.getString(i)));
				System.out.println(centers_json);

			}

			for (JSONObject json_obj : centers_json) {
				centers.add(json_obj.getString("name"));
				listaPovezivanja.add(json_obj.getString("name")
						+ json_obj.getString("id"));
			}

		} catch (JSONException e) {
			e.printStackTrace();
		}

		return centers;
	}

	public static List<String> GetPopisPonude(String json_string) {
		List<JSONObject> centers_json1 = new ArrayList<JSONObject>();
		List<String> centers1 = new ArrayList<String>();
		listaPovezivanja = new ArrayList<String>();
		String temp = "";
				
		try {
			JSONArray json_array1 = new JSONArray(json_string);
			for (int i = 0; i < json_array1.length(); ++i) {
				centers_json1.add(new JSONObject(json_array1.getString(i)));

			}

			for (JSONObject json_obj : centers_json1) {
				temp = "";
				temp = temp + json_obj.getString("name");
				temp = temp + " - " + json_obj.getString("type");

				for (int x = 0; x < listaInfo.size(); x+=2) {
					if (listaInfo.get(x).toString().equals(json_obj.getString("name"))){
						centers1.add(temp+ "      " +listaInfo.get(x+1));
						listaPovezivanja.add(temp + "      " +listaInfo.get(x+1) + json_obj.getString("id"));
						break;
					}
				}
			}

		} catch (JSONException e) {
			e.printStackTrace();
		}
		System.out.println(centers1);
		return centers1;
	}

	public static String GetAddress(String json_string) {
		String adresa = null;
	    try {
			JSONObject jsonObject = new JSONObject(json_string);
		    JSONArray array = jsonObject.getJSONArray("results");
		    adresa = array.getJSONObject(0).getString("formatted_address").toString();
		} catch (JSONException e) {
			e.printStackTrace();
		}  
	    return adresa;
	}
	
	public static String GetDistance(String json_string, String name) {
		String distance = null;
		JSONObject n = null;
		JSONObject m = null;
				
	    try {
			JSONObject jsonObject = new JSONObject(json_string);
		    JSONArray array = jsonObject.getJSONArray("rows");
		    
		    for (int i = 0; i < array.length(); i++) {
				n = array.getJSONObject(i);
			}
		    
		    JSONArray sub = n.getJSONArray("elements");
		    
		    for (int j = 0; j < sub.length(); j++) {
				m = sub.getJSONObject(j);
			}
		    
		    distance = m.getJSONObject("distance").getString("text");	
		    listaInfo.add(name);
		    listaInfo.add(distance);
		    
		} catch (JSONException e) {
			e.printStackTrace();
		}  
	    return distance;
	}

	
	public static List<String> GetInfo(String json_string) {
		List<JSONObject> centers = new ArrayList<JSONObject>();
		List<String> lista = new ArrayList<String>();
		try {
			JSONArray json_array1 = new JSONArray(json_string);
			for (int i = 0; i < json_array1.length(); ++i) {
				centers.add(new JSONObject(json_array1.getString(i)));
			}
			
			for (JSONObject json_obj : centers) {
				lista.add(json_obj.getString("id"));
				lista.add(json_obj.getString("name"));
				lista.add(json_obj.getString("address"));
			}

		} catch (JSONException e) {
			e.printStackTrace();
		}

		return lista;
	}

	public static PodaciCentra InfoCentra(String json_string)
			throws JSONException {
		List<JSONObject> centers_json = new ArrayList<JSONObject>();

		PodaciCentra x = new PodaciCentra();
		try {
			JSONArray json_array1 = new JSONArray(json_string);
			for (int i = 0; i < json_array1.length(); ++i) {
				centers_json.add(new JSONObject(json_array1.getString(i)));

			}

			for (JSONObject json_obj : centers_json) {
				x.adresaC = (json_obj.getString("address")).toString();
				x.opisC = (json_obj.getString("description")).toString();
				x.emailC = (json_obj.getString("email")).toString();
				x.imeC = (json_obj.getString("1")).toString();
				x.cijenaC = (json_obj.getString("price")).toString();
				x.kvartC = (json_obj.getString("6")).toString();
				x.gradC = (json_obj.getString("7")).toString();
			}

		} catch (JSONException e) {
			e.printStackTrace();
		}

		return x;
	}

	public static List<String> GetReservationList(String json_string) {
		List<JSONObject> centers_json = new ArrayList<JSONObject>();
		List<String> listaR = new ArrayList<String>();
		try {
			JSONArray json_array = new JSONArray(json_string);
			for (int i = 0; i < json_array.length(); ++i) {
				centers_json.add(new JSONObject(json_array.getString(i)));

			}

			for (JSONObject json_obj : centers_json) {
				listaR.add(json_obj.getString("id"));
				listaR.add("Ime centra: " + json_obj.getString("1"));
				listaR.add("Vrsta sporta: " + json_obj.getString("name"));
				String tempDatum = "", tempVrijeme = "";
				tempDatum = json_obj.getString("start_time").substring(0, 10);
				listaR.add("Datum: " + tempDatum);
				tempVrijeme = json_obj.getString("start_time")
						.substring(11, 16)
						+ " - "
						+ json_obj.getString("end_time").substring(11, 16);
				listaR.add(tempVrijeme);

			}

		} catch (JSONException e) {
			e.printStackTrace();
		}

		return listaR;
	}

	public static List<String> GetReservationView(String json_string) {
		List<JSONObject> centers_json = new ArrayList<JSONObject>();
		List<String> listaR2 = new ArrayList<String>();
		try {
			JSONArray json_array = new JSONArray(json_string);
			for (int i = 0; i < json_array.length(); ++i) {
				centers_json.add(new JSONObject(json_array.getString(i)));
			}

			int a = 1;
			for (JSONObject json_obj : centers_json) {

				String tempDatum = "", tempVrijeme = "";
				tempDatum = json_obj.getString("start_time").substring(0, 10);
				tempVrijeme = json_obj.getString("start_time")
						.substring(11, 16)
						+ "-"
						+ json_obj.getString("end_time").substring(11, 16);

				listaR2.add("" + a + ". " + json_obj.getString("1") + "\n"
						+ "    " + json_obj.getString("name") + "\n" + "    "
						+ tempDatum + " " + tempVrijeme);
				listaR2.add("*otkaži rezervaciju iznad*");
				a++;
			}

		} catch (JSONException e) {
			e.printStackTrace();
		}

		return listaR2;
	}

	public static List<PodaciRezervacije> GetReservationFill(String json_string) {
		List<JSONObject> centers_json1 = new ArrayList<JSONObject>();
		List<PodaciRezervacije> rezervacije = new ArrayList<PodaciRezervacije>();
		PodaciRezervacije pojedinacnaR;

		try {
			JSONArray json_array1 = new JSONArray(json_string);
			for (int i = 0; i < json_array1.length(); ++i) {
				centers_json1.add(new JSONObject(json_array1.getString(i)));

			}

			for (JSONObject json_obj : centers_json1) {
				pojedinacnaR = new PodaciRezervacije();
				pojedinacnaR.setStart_time(json_obj.getString("start_time"));
				pojedinacnaR.setEnd_time(json_obj.getString("end_time"));
				pojedinacnaR.setField_index(json_obj.getString("field_index"));
				pojedinacnaR.setCount(json_obj.getString("count"));

				rezervacije.add(pojedinacnaR);

			}

		} catch (JSONException e) {
			e.printStackTrace();
		}

		return rezervacije;
	}

	public static List<String> GetRadnoVrijeme(String json_string) {
		List<JSONObject> centers_json = new ArrayList<JSONObject>();
		List<String> radnoVrijeme = new ArrayList<String>();

		try {
			JSONArray json_array = new JSONArray(json_string);
			for (int i = 0; i < json_array.length(); ++i) {
				centers_json.add(new JSONObject(json_array.getString(i)));

			}

			for (JSONObject json_obj : centers_json) {
				radnoVrijeme.add(json_obj.getString("0"));
				radnoVrijeme.add(json_obj.getString("1"));

			}

		} catch (JSONException e) {
			e.printStackTrace();
		}

		return radnoVrijeme;
	}

	public static int GetLoginId (String json_string) {
		List<JSONObject> centers_json = new ArrayList<JSONObject>();
		int id=-1;
		

		try {
			JSONArray json_array = new JSONArray(json_string);
			for (int i = 0; i < json_array.length(); ++i) {
				centers_json.add(new JSONObject(json_array.getString(i)));

			}

			for (JSONObject json_obj : centers_json) {
				id= Integer.parseInt(json_obj.getString("id"));
				
			}

		} catch (JSONException e) {
			e.printStackTrace();
		}

		return id;
	}
}
