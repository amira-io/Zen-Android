package io.thera.zen.geo;

import android.content.Context;
import android.location.LocationManager;
import android.support.v4.app.FragmentActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import io.thera.zen.core.ZenLog;
import io.thera.zen.json.ZenJsonManager;

public class ZenGeoUtil {

	static String callback;
    static Object caller;
    static String format;

	public static synchronized void setLocationManager(FragmentActivity a) {
		
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

    public static void positionToAddress(double lat, double lon, String callback_name, Object caller_name, String format_name) {

        ZenLog.l("sono in positiontoaddress");
        callback = callback_name;
        caller = caller_name;
        format = format_name;

        String url  = "http://maps.googleapis.com/maps/api/geocode/json?latlng="+lat+","+lon+"&sensor=true";

        ZenJsonManager.parseJson(url, "getAddress", ZenGeoUtil.class);
    }

    public static void findPosition(String address, String callback_name, Object caller_name, String format_name) {
        callback = callback_name;
        caller = caller_name;
        format = format_name;

        //replacing
        String formatted_address = address.replace(" ", "+");
        ZenLog.l("TEST inside findposition " + address + " - " + formatted_address);
        String url  = "http://maps.googleapis.com/maps/api/geocode/json?address="+formatted_address+"&sensor=true";

        ZenJsonManager.parseJson(url, "getAddressesList", ZenGeoUtil.class, false);

    }

    public static void getAddress(String json) {
        try {
            String country = "", region = "", province ="", locality = "";

            JSONObject o = new JSONObject(json);
            JSONArray a = o.getJSONArray("results");
            JSONObject object =  a.getJSONObject(0);
            JSONArray comp = object.getJSONArray("address_components");
            for (int i=0; i< comp.length(); i++) {
                JSONObject j = comp.getJSONObject(i);
                if (j.getJSONArray("types").getString(0).equals("country")) {
                    country = j.getString("long_name");
                }
                if (j.getJSONArray("types").getString(0).equals("administrative_area_level_1")) {
                    region = j.getString("long_name");
                }
                if (j.getJSONArray("types").getString(0).equals("administrative_area_level_2")) {
                    province = j.getString("long_name");
                }
                if (j.getJSONArray("types").getString(0).equals("locality")) {
                    locality = j.getString("long_name");
                }
            }
            Class[] params;
            Object[] values;

            if (format.equals("string")) {
                params = new Class[4];
                params[0] = String.class;
                params[1] = String.class;
                params[2] = String.class;
                params[3] = String.class;

                values = new Object[4];
                values[0] = country;
                values[1] = region;
                values[2] = province;
                values[3] = locality;
            }
            else {
                JSONObject val = new JSONObject();
                val.put("country", country);
                val.put("region", region);
                val.put("province", province);
                val.put("locality", locality);

                params = new Class[1];
                params[0] = JSONObject.class;
                values = new Object[1];
                values[0] = val;
            }

            if (caller instanceof Class) {
                ((Class) caller).getMethod(callback, params).invoke(caller,values);
            }
            else {
                caller.getClass().getMethod(callback, params).invoke(caller, values);
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void getAddressesList(String json) {
        try {
            ZenLog.l("TEST inside getaddresses list " + json);
            List<String> results = new ArrayList<String>();
            JSONArray json_results = new JSONArray();
            String country="", region="", province="", locality="";

            JSONObject o = new JSONObject(json);
            JSONArray a = o.getJSONArray("results");
            for (int i=0; i<a.length(); i++) {
                JSONObject object =  a.getJSONObject(i);
                String formatted = object.getString("formatted_address");
                JSONArray comp = object.getJSONArray("address_components");
                for (int k=0; k< comp.length(); k++) {
                    JSONObject j = comp.getJSONObject(k);
                    if (j.getJSONArray("types").getString(0).equals("country")) {
                        country = j.getString("long_name");
                    }
                    if (j.getJSONArray("types").getString(0).equals("administrative_area_level_1")) {
                        region = j.getString("long_name");
                    }
                    if (j.getJSONArray("types").getString(0).equals("administrative_area_level_2")) {
                        province = j.getString("long_name");
                    }
                    if (j.getJSONArray("types").getString(0).equals("locality")) {
                        locality = j.getString("long_name");
                    }
                }
                if (format.equals("json")) {
                    JSONObject val = new JSONObject();
                    val.put("country", country);
                    val.put("region", region);
                    val.put("province", province);
                    val.put("locality", locality);
                    val.put("formatted", formatted);
                    json_results.put(i, val);
                } else {
                    String res = country+":"+region+":"+province+":"+locality+":"+formatted;
                    results.add(res);
                }

            }

            Class[] params;
            Object[] values;

            if (format.equals("string")) {
                params = new Class[1];
                params[0] = List.class;

                values = new Object[1];
                values[0] = results;
            }
            else {
                ZenLog.l("TEST about to send jsonarray" + json_results);
                params = new Class[1];
                params[0] = JSONArray.class;
                values = new Object[1];
                values[0] = json_results;
            }

            if (caller instanceof Class) {
                ((Class) caller).getMethod(callback, params).invoke(caller,values);
            }
            else {
                caller.getClass().getMethod(callback, params).invoke(caller, values);
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
	
	
	
}
