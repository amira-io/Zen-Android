package io.thera.zen.geo;

import android.location.LocationManager;
import android.support.v4.app.FragmentActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import io.thera.zen.core.ZenApplication;
import io.thera.zen.json.ZenJson;

public class ZenGeoUtil {

    static String callback;
    static Object caller;
    static String format;

    public static void positionToAddress(double lat, double lon, String callback_name, Object caller_name, String format_name) {
        callback = callback_name;
        caller = caller_name;
        format = format_name;

        String url  = "http://maps.googleapis.com/maps/api/geocode/json?latlng="+lat+","+lon+"&sensor=true";

        ZenJson.get(url, ZenGeoUtil.class, "getAddress", "getAddress");
    }

    public static void findPosition(String address, String callback_name, Object caller_name, String format_name) {
        findPosition(address, callback_name, caller_name, format_name, null);
    }

    public static void findPosition(String address, String callback_name, Object caller_name, String format_name, Map<String, String> filters) {
        callback = callback_name;
        caller = caller_name;
        format = format_name;

        //: convert string format to url compatible
        String formatted_address = address.replace(" ", "+");

        //: add components filtering (if required)
        String filter_string = "";
        if (filters != null) {
            filter_string = "&components=";
            for (String key : filters.keySet()) {
                String value = filters.get(key);
                filter_string += key+":"+value+"%7C";
            }
            filter_string = filter_string.substring(0, filter_string.length()-3);
        }

        String url  = "http://maps.googleapis.com/maps/api/geocode/json?address="+formatted_address+filter_string+"&sensor=true";
        ZenJson.get(url, ZenGeoUtil.class, "getAddressesList", "getAddressesList");
    }

    public static void getAddress(JSONObject json) {
        try {
            String country = "", region = "", province ="", locality = "";

            JSONArray a = json.getJSONArray("results");
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

    public static void getAddressesList(JSONObject json) {
        try {
            ZenApplication.log("TEST inside getaddresses list " + json);
            List<String> results = new ArrayList<String>();
            JSONArray json_results = new JSONArray();
            String lat="", lon="";
            String country="<missing>", region="<missing>", province="<missing>",
                    municipality="<missing>", locality="<missing>", locality_fallback="";

            JSONArray a = json.getJSONArray("results");

            List<String> skip_types = new ArrayList<String>(){{add("locality"); add("neighborhood");}};
            //: cycle among results
            for (int i=0; i<a.length(); i++) {
                boolean skip = false;
                JSONObject object =  a.getJSONObject(i);
                String formatted = object.getString("formatted_address");
                JSONObject geometry = object.getJSONObject("geometry");
                lat = geometry.getJSONObject("location").get("lat").toString();
                lon = geometry.getJSONObject("location").get("lng").toString();
                JSONArray comp = object.getJSONArray("address_components");
                //: cycle among components of the result to collect address data
                for (int k=0; k< comp.length(); k++) {
                    JSONObject j = comp.getJSONObject(k);
                    JSONArray types = j.getJSONArray("types");
                    //: if google doesn't provide any type, skip the component
                    if (types.length() == 0) {
                        continue;
                    }
                    String type = types.getString(0);
                    //: if the first component isn't a locality or a 'neighborhood', we should skip it,
                    //  as it could be a route or something else
                    if (k == 0 && !skip_types.contains(type)) {
                        skip = true;
                        break;
                    }
                    //: store location details
                    if (type.equals("country")) {
                        country = j.getString("long_name");
                    }
                    if (type.equals("administrative_area_level_1")) {
                        region = j.getString("long_name");
                    }
                    if (type.equals("administrative_area_level_2")) {
                        province = j.getString("long_name");
                    }
                    if (type.equals("administrative_area_level_3")) {
                        municipality = j.getString("long_name");
                    }
                    if (type.equals("locality")) {
                        locality = j.getString("long_name");
                    }
                    if (type.equals("neighborhood")) {
                        locality_fallback = j.getString("long_name");
                    }
                }
                if (skip) {
                    continue;
                }
                if (locality.equals("<missing>")) {
                    locality = locality_fallback;
                }
                if (format.equals("json")) {
                    JSONObject val = new JSONObject();
                    val.put("country", country);
                    val.put("region", region);
                    val.put("province", province);
                    val.put("municipality", municipality);
                    val.put("locality", locality);
                    val.put("formatted", formatted);
                    val.put("lat", lat);
                    val.put("lon", lon);
                    json_results.put(val);
                } else {
                    String res = country+":"+region+":"+province+":"+municipality+":"+locality+":"+formatted+":"+lat+":"+lon;
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
                ZenApplication.log("TEST about to send jsonarray" + json_results);
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
