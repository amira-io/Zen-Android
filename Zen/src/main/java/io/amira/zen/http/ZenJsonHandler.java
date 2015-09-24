package io.amira.zen.http;

/**
 * Handler for HTTP json requests
 * Implements caching features using ZenCache.
 *
 * Created by Giovanni Barillari on 27/08/14.
 * Copyright © 2013-2014. Thera Technologies.
 */

import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import java.security.MessageDigest;

import io.amira.zen.core.ZenApplication;


public class ZenJsonHandler extends JsonHttpResponseHandler {
    Object caller;
    String successMethod;
    String errorMethod;
    String url;
    int cache;
    private String _cache_key;
    private Object _cached;
    private Object _cached_class;

    //: basic constructor
    public ZenJsonHandler(Object caller, String onSuccessMethod, String onErrorMethod) {
        super();
        this.caller = caller;
        successMethod = onSuccessMethod;
        errorMethod = onErrorMethod;
        cache = 0;
    }

    //: caching constructor
    public ZenJsonHandler(Object caller, String onSuccessMethod, String onErrorMethod, String url, int cache_time) {
        super();
        this.caller = caller;
        successMethod = onSuccessMethod;
        errorMethod = onErrorMethod;
        this.url = url;
        cache = cache_time;
    }

    //: invoker methods
    private void _callSuccess(Class[] params, Object[] values) {
        try {
            if (this.caller instanceof Class) {
                ((Class) this.caller).getMethod(this.successMethod, params).invoke(this.caller, values);
            } else {
                this.caller.getClass().getMethod(this.successMethod, params).invoke(this.caller, values);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void _callSuccess(JSONObject json) {
        Class[] params = new Class[1];
        params[0] = JSONObject.class;
        Object[] values = new Object[1];
        values[0] = json;
        _callSuccess(params, values);
    }
    private void _callSuccess(JSONArray json) {
        Class[] params = new Class[1];
        params[0] = JSONArray.class;
        Object[] values = new Object[1];
        values[0] = json;
        _callSuccess(params, values);
    }
    private void _callError(Class[] params, Object[] values) {
        try {
            if (this.caller instanceof Class) {
                ((Class) this.caller).getMethod(this.errorMethod, params).invoke(this.caller, values);
            } else {
                this.caller.getClass().getMethod(this.errorMethod, params).invoke(this.caller, values);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void _callError(JSONObject json) {
        Class[] params = new Class[1];
        params[0] = JSONObject.class;
        Object[] values = new Object[1];
        values[0] = json;
        _callError(params, values);
    }
    private void _callError(JSONArray json) {
        Class[] params = new Class[1];
        params[0] = JSONArray.class;
        Object[] values = new Object[1];
        values[0] = json;
        _callError(params, values);
    }
    private void _callError() {
        try {
            if (this.caller instanceof Class) {
                ((Class) this.caller).getMethod(this.errorMethod).invoke(this.caller);
            } else {
                this.caller.getClass().getMethod(this.errorMethod).invoke(this.caller);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //: generates a unique key for caching
    private String cacheKey() {
        if (_cache_key == null) {
            try {
                MessageDigest md = MessageDigest.getInstance("SHA-256");
                md.update(url.getBytes());
                byte[] mdbytes = md.digest();
                StringBuffer hexString = new StringBuffer();
                for (int i = 0; i < mdbytes.length; i++) {
                    hexString.append(Integer.toHexString(0xFF & mdbytes[i]));
                }
                _cache_key = "_zen:" + hexString.toString();
            } catch (Exception e) {
                e.printStackTrace();
                _cache_key = null;
            }
        }
        return _cache_key;
    }

    //: caching utilities
    public boolean hasCache() {
        String key = cacheKey();
        _cached = ZenApplication.cache()._get(key);
        _cached_class = ZenApplication.cache()._get(key+"_class");
        if (_cached != null) {
            return true;
        }
        return false;
    }
    public void getFromCache() {
        if (_cached == null) {
            String key = cacheKey();
            _cached = ZenApplication.cache()._get(key);
            _cached_class = ZenApplication.cache()._get(key+"_class");
        }
        if (_cached_class.toString().equals("JObj")) {
            try {
                onSuccess(200, null, new JSONObject(_cached.toString()));
            } catch (Exception e) {
                e.printStackTrace();
                onFailure(200, null, "Cache error", e);
            }
        } else {
            try {
                onSuccess(200, null, new JSONArray(_cached.toString()));
            } catch (Exception e) {
                e.printStackTrace();
                onFailure(200, null, "Cache error", e);
            }
        }
    }
    private void storeCache(Object data, Class data_class) {
        if (cache != 0 && _cached == null) {
            ZenApplication.cache()._store(cacheKey(), data.toString(), cache);
            String c;
            if (data_class == JSONObject.class) {
                c = "JObj";
            } else {
                c = "JArr";
            }
            ZenApplication.cache()._store(cacheKey()+"_class", c, cache);
        }
    }

    @Override
    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
        storeCache(response, JSONObject.class);
        _callSuccess(response);
    }

    @Override
    public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
        storeCache(response, JSONArray.class);
        _callSuccess(response);
    }

    @Override
    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
        _callError(errorResponse);
    }

    @Override
    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
        _callError(errorResponse);
    }

    @Override
    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
        _callError();
    }
}
