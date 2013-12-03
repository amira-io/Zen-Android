package io.thera.zen.json;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

import java.lang.reflect.*;

public class ZenTask extends AsyncTask<String, Void, String> {
	
	private String type;
	private TextView container;
    private String method;
	private List<String> permittedTypes;
    private Object caller;

	public ZenTask(String type, String method, Object caller) {
		this.type = type;
		if (ZenJsonUtil.isTesting) {
			//container.setText("Selected type : " + this.type );
		}
        this.caller = caller;
		this.method = method;
		this.permittedTypes = new ArrayList<String>();
		permittedTypes.add("json");
		permittedTypes.add("xml");
	}
	@Override
	protected String doInBackground(String... params) {
		// TODO Auto-generated method stub
		try {
			if (permittedTypes.contains(this.type)) {
				switch (permittedTypes.indexOf(this.type)){
					
					case 0: {
						return this.getJson(params[0]);
					}
					case 1: {
						return"";
					}
				}
			}
			else {
				return null;
			}
		}
		catch (IOException ioe) {
			this.container.setText("IOException : " + ioe.getMessage());
			return null;
		}
		catch (Exception e) {
			this.container.setText("Exception : " + e.getMessage());
			return null;
		}
		return null;
	}
	
	@Override 
	protected void onPostExecute (String result) {
		if (permittedTypes.contains(this.type)) {
			switch (permittedTypes.indexOf(this.type)){
				
				case 0: {
					//return this.getJson(params[0], this.container);
					try {


                        Class[] params = new Class[1];
                        params[0] = String.class;
                        Object[] values = new Object[1];
                        values[0] = result;
                        this.caller.getClass().getMethod(this.method, params).invoke(this.caller, values);


					  //JSONArray jsonArray = new JSONArray(result);
				      //Log.i(ZenJsonManager.class.getName(),"Number of entries " + jsonArray.length());
				      //JSONArray o = (JSONArray) jsonArray.get(0);
				      //JSONObject obj = o.getJSONObject(1);
				      //container.append(obj.getString("formatted_address"));
					  //JSONObject obj = new JSONObject(result);
					  //JSONArray array = obj.getJSONArray("data");
					  //process(array);
					  //JSONObject o = array.getJSONObject(0);
					  //container.setText(o.getString("formatted_address"));
					}
					catch (Exception e) {
						//container.setText(e.printStackTrace());
						//container.setText(e.getLocalizedMessage());
						return;
					}
				}
				case 1: {
					return;
				}
			}
		}
	
	}
	
	//TEMP
	public void process ( JSONArray array ) {
		
		float[] accel = new float[array.length()];
		
		for (int i =0 ; i < array.length() ;  i++ ) {
			
			try {
				JSONObject o = array.getJSONObject(i);
				String time = o.getString("timestamp");
				time = time.substring(19);
				int index = Integer.parseInt(time);
				
				String j = o.getString("accel");
				
				String[] temp = j.split(":");
				
				accel[index] = Float.parseFloat(temp[2]);
				
				System.out.println(accel[index]);
				
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	}
	
	/*
	 *  PERMITTED TYPES METHODS.
	 */
	public String getJson ( String url ) throws ClientProtocolException, IOException {
	  StringBuilder builder = new StringBuilder();
	  HttpClient client = new DefaultHttpClient();
	  HttpGet httpGet = new HttpGet(url);
	  HttpResponse response = client.execute(httpGet);
	  StatusLine statusLine = response.getStatusLine();
	  int statusCode = statusLine.getStatusCode();
	  if (statusCode == 200) {
	    HttpEntity entity = response.getEntity();
	    InputStream content = entity.getContent();
	    BufferedReader reader = new BufferedReader(new InputStreamReader(content));
	    String line;
	    while ((line = reader.readLine()) != null) {
	      builder.append(line);
	    }
	  } else {
	    Log.e(ZenJsonManager.class.toString(), "Failed to download file");
	  }
	  
	  return builder.toString();
	}

}
