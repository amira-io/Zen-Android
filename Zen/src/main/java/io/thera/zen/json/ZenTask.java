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
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

import java.lang.reflect.*;

import io.thera.zen.core.ZenLog;

public class ZenTask extends AsyncTask<Object, Void, String> {
	
	private String type;
	private TextView container;
    private String method;
    private String onError;
	private List<String> permittedTypes;
    private Object caller;
    private boolean error = false;

	public ZenTask(String type, String method, Object caller, String onError) {
		this.type = type;
		if (ZenJsonUtil.isTesting) {
			//container.setText("Selected type : " + this.type );
		}
        this.caller = caller;
		this.method = method;
        this.onError = onError;
		this.permittedTypes = new ArrayList<String>();
		permittedTypes.add("json");
		permittedTypes.add("xml");
	}
	@Override
	protected String doInBackground(Object... params) {
		// TODO Auto-generated method stub
		try {
			if (permittedTypes.contains(this.type)) {
				switch (permittedTypes.indexOf(this.type)){

					case 0: {
                        String url = (String) params[0];
                        Map<String, String> headers = (Map<String, String>) params[1];
                        String mode = (String) params[2];
                        if (mode.equals("get")) {
						    return this.getJson(url, headers);
                        } else {
                            Map<String, String> r_params = (Map<String, String>) params[3];
                            return this.postJson(url, r_params, headers);
                        }
					}
					case 1: {
						return "";
					}
                    default: {
                        return null;
                    }
				}
			}
			else {
				return null;
			}
        }
		catch (Exception e) {
			//this.container.setText("Exception : " + e.getMessage());
            e.printStackTrace();
            error = true;
			return null;
		}
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
                        if (this.caller instanceof Class) {
                            //ZenLog.l("PROVO A CHIAMARE"+((Class) this.caller).getCanonicalName());
                            if (this.error) {
                                ((Class) this.caller).getMethod(this.onError).invoke(this.caller);
                            } else {
                                ((Class) this.caller).getMethod(this.method, params).invoke(this.caller,values);
                            }
                        }
                        else {
                            //ZenLog.l("NON CLASSE PROVO A CHIAMARE"+this.caller.getClass().getCanonicalName());
                            if (this.error) {
                                this.caller.getClass().getMethod(this.onError).invoke(this.onError);
                            } else {
                                this.caller.getClass().getMethod(this.method, params).invoke(this.caller, values);
                            }
                        }

					}
					catch (Exception e) {
						//container.setText(e.printStackTrace());
						//container.setText(e.getLocalizedMessage());
                        e.printStackTrace();
					}
				}

			}
		}
	
    }
	
	/*
	 *  PERMITTED TYPES METHODS.
	 */
	public String getJson ( String url, Map<String, String> req_headers ) throws ClientProtocolException, IOException {
        StringBuilder builder = new StringBuilder();
        HttpClient client = new DefaultHttpClient();
        HttpGet httpget = new HttpGet(url);
        if (req_headers != null) {
            Set<String> keys = req_headers.keySet();
            for (String k : keys) {
                httpget.setHeader(k, req_headers.get(k));
            }
        }
        HttpResponse response = client.execute(httpget);
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

    public String postJson ( String url, Map<String, String> params, Map<String, String> req_headers ) throws ClientProtocolException, IOException {
        ZenLog.l(url);
        StringBuilder builder = new StringBuilder();
        HttpClient client = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(url);
        try {
            //convert parameters into JSON object
            JSONObject holder = getJsonObjectFromMap(params);
            //passes the results to a string builder/entity
            StringEntity se = new StringEntity(holder.toString());
            ZenLog.l(holder.toString());
            //sets the post request as the resulting string
            httppost.setEntity(se);
        } catch (Exception e) {
            ZenLog.l(params.toString());
            e.printStackTrace();
            ZenLog.l("Bad post params");
            return "";
        }
        if (req_headers != null) {
            Set<String> keys = req_headers.keySet();
            for (String k : keys) {
                httppost.setHeader(k, req_headers.get(k));
            }
        }
        httppost.setHeader("Accept", "application/json");
        httppost.setHeader("Content-type", "application/json");
        HttpResponse response = client.execute(httppost);
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
            ZenLog.l(statusCode);
            Log.e(ZenJsonManager.class.toString(), "Failed to download file");
        }

        return builder.toString();
    }

    private static JSONObject getJsonObjectFromMap(Map params) throws JSONException {

        //all the passed parameters from the post request
        //iterator used to loop through all the parameters
        //passed in the post request
        Iterator iter = params.entrySet().iterator();

        //Stores JSON
        JSONObject holder = new JSONObject();

        //While there is another entry
        while (iter.hasNext())
        {
            //gets an entry in the params
            Map.Entry pairs = (Map.Entry)iter.next();

            holder.put((String) pairs.getKey(), (String) pairs.getValue());

        }
        return holder;
    }

}
