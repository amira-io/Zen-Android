package io.thera.zen.json;

/**
 * ZenHTTP: a friendly JSON requests shortcut for ZenHTTP
 *
 * Created by Giovanni Barillari on 27/08/14.
 * Copyright © 2013-2014. Thera Technologies.
 */

import org.apache.http.entity.StringEntity;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import io.thera.zen.http.ZenHTTP;


public class ZenJson {
    private static JSONObject buildJsonObjectFromMap(Map params) throws JSONException {
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

    private static StringEntity buildString(Object obj) {
        try {
            return new StringEntity(obj.toString(), "UTF8");
        } catch (Exception e) {
            return null;
        }
    }

    public static void get(String url, Object caller, String onSuccessMethod, String onErrorMethod) {
        get(url, caller, onSuccessMethod, onErrorMethod, 0, false);
    }

    public static void get(String url, Object caller, String onSuccessMethod, String onErrorMethod,
                           int cache_time) {
        get(url, caller, onSuccessMethod, onErrorMethod, cache_time, false);
    }

    public static void get(String url, Object caller, String onSuccessMethod, String onErrorMethod,
                           int cache_time, boolean refresh_cache) {
        get(url, caller, onSuccessMethod, onErrorMethod, null, cache_time, refresh_cache);
    }

    public static void get(String url, Object caller, String onSuccessMethod, String onErrorMethod,
                           Map<String, String> headers) {
        get(url, caller, onSuccessMethod, onErrorMethod, headers, 0, false);
    }

    public static void get(String url, Object caller, String onSuccessMethod, String onErrorMethod,
                           Map<String, String> headers, int cache_time) {
        get(url, caller, onSuccessMethod, onErrorMethod, headers, cache_time, false);
    }

    public static void get(String url, Object caller, String onSuccessMethod, String onErrorMethod,
                           Map<String, String> headers, int cache_time, boolean refresh_cache) {
        ZenHTTP.getJson(url, caller, onSuccessMethod, onErrorMethod, headers, cache_time, refresh_cache);
    }

    public static void getwsync(String url, Object caller, String onSuccessMethod, String onErrorMethod,
                           Map<String, String> headers) {
        getwsync(url, caller, onSuccessMethod, onErrorMethod, headers, 0, false);
    }

    public static void getwsync(String url, Object caller, String onSuccessMethod, String onErrorMethod,
                           Map<String, String> headers, int cache_time, boolean refresh_cache) {
        ZenHTTP.getJson(url, caller, onSuccessMethod, onErrorMethod, headers, cache_time, refresh_cache, false);
    }

    public static void post(String url, Object caller, String onSuccessMethod, String onErrorMethod,
                            Map<String, Object> params) {
        post(url, caller, onSuccessMethod, onErrorMethod, params, null);
    }

    public static void post(String url, Object caller, String onSuccessMethod, String onErrorMethod, JSONObject body) {
        post(url, caller, onSuccessMethod, onErrorMethod, body, null);
    }

    public static void post(String url, Object caller, String onSuccessMethod, String onErrorMethod,
                            Map<String, Object> params, Map<String, String> headers) {
        JSONObject jbody;
        try {
            jbody = buildJsonObjectFromMap(params);
        } catch (Exception e) {
            jbody = null;
        }
        ZenHTTP.postJson(url, caller, onSuccessMethod, onErrorMethod, buildString(jbody), headers);
    }

    public static void post(String url, Object caller, String onSuccessMethod, String onErrorMethod,
                            JSONObject body, Map<String, String> headers) {
        ZenHTTP.postJson(url, caller, onSuccessMethod, onErrorMethod, buildString(body), headers);
    }

}
