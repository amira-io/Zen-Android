package io.thera.zen.geo;

import android.app.Activity;
import android.content.Context;
import android.location.LocationManager;

import org.json.JSONArray;
import org.json.JSONObject;

import io.thera.zen.core.ZenLog;
import io.thera.zen.json.ZenJsonManager;

public class ZenGeoUtil {

	static String callback;
    static Object caller;

	public static synchronized void setLocationManager(Activity a) {
		
		//locationManager = (LocationManager) a.getSystemService(Context.LOCATION_SERVICE);
		
	}
	
	public static boolean isGpsEnabled (LocationManager locationManager) {
		
		return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
	}
	
	public static boolean isNetworkEnabled(LocationManager locationManager) {
		
		return locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
		
	}
	
	public static boolean isPassiveEnabled (LocationManager locationManager) {
		
		return locationManager.isProviderEnabled(LocationManager.PASSIVE_PROVIDER);
	}

    public static void positionToAddress(double lat, double lon, String callback_name, Object caller_name) {

        ZenLog.l("sono in positiontoaddress");
        callback = callback_name;
        caller = caller_name;

        String url          = "http://maps.googleapis.com/maps/api/geocode/json?latlng="+lat+","+lon+"&sensor=true";

        ZenJsonManager.parseJson(url, "getAddress", ZenGeoUtil.class);

    }

    public static void getAddress(String json) {
        try {
            System.out.println("value of string: " + json);

            JSONObject o = new JSONObject(json);
            JSONArray a = o.getJSONArray("results");
            String value = a.getJSONObject(0).getString("formatted_address");

            Class[] params = new Class[1];
            params[0] = String.class;
            Object[] values = new Object[1];
            values[0] = value;
            System.out.println("value of string: " + value);
            if (caller instanceof Class) {
                ((Class) caller).getMethod(callback, params).invoke(caller,values);
            }
            else {
                caller.getClass().getMethod(callback, params).invoke(caller, values);
            }
            //t.setText(a.getJSONObject(0).getString("formatted_address"));

        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
	
	
	
}
