package hr.fer.tel.tihana;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

public class Json {

    static String GetJSONResponse(String url){
    	
    	try {
	    	HttpClient client = new DefaultHttpClient();
	    	HttpGet httpget = new HttpGet(url);
	    	
	    	httpget.addHeader("accept", "application/json");
	    	HttpResponse response = client.execute(httpget);
	    	HttpEntity entity = response.getEntity();
    	
    	
			return EntityUtils.toString(entity);
		} catch (ParseException e) {
			e.printStackTrace();
			return "Parse Exception occured.";
		} catch (IOException e) {
			e.printStackTrace();
			return "IOException occured.";
		}
    }
    
}

