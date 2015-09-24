package io.amira.zen.http;

/**
 * ZenHTTP: a simple HTTP requests class
 *
 * Created by Giovanni Barillari on 27/08/14.
 * Copyright © 2013-2014. Thera Technologies.
 */

import android.os.Looper;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.SyncHttpClient;

import org.apache.http.Header;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicHeader;

import java.io.File;
import java.util.Iterator;
import java.util.Map;

import io.amira.zen.core.ZenApplication;
import io.amira.zen.utils.Network;


public class ZenHTTP {
    private static AsyncHttpClient syncClient = new SyncHttpClient();
    private static AsyncHttpClient asyncClient = new AsyncHttpClient();

    private static AsyncHttpClient client() {
        if (Looper.myLooper() == null) {
            return syncClient;
        }
        return asyncClient;
    }

    // do we need this?
    private static boolean _checkConnection() {
        return Network.isConnected();
    }

    //: convert headers from Map format to apache class
    private static Header[] _parseHeaders(Map<String, String> headers) {
        Header[] rv = new Header[headers.size()];
        Iterator iter = headers.entrySet().iterator();
        int i = 0;
        while (iter.hasNext()) {
            Map.Entry pairs = (Map.Entry)iter.next();
            rv[i] = new BasicHeader(pairs.getKey().toString(), pairs.getValue().toString());
            i++;
        }
        return rv;
    }

    //TODO: handle basic get requests
    public static void get(String url, Object caller, String onSuccessMethod, String onErrorMethod) {

    }

    //TODO: handle basic post requests
    public static void post(String url, Object caller, String onSuccessMethod, String onErrorMethod, StringEntity body) {

    }

    //: helper for getFile
    private static String _build_filename(String url) {
        String[] url_components = url.split("/");
        String filename = url_components[url_components.length-1];
        return filename.split("\\?")[0];
    }

    //: handle file download (using temp File descriptor)
    public static void getFile(String url, Object caller, String onSuccessMethod, String onErrorMethod) {
        client().get(url, new ZenFileHandler(caller, onSuccessMethod, onErrorMethod, _build_filename(url)));
    }

    //: handle file download (using user defined File descriptor)
    public static void getFile(String url, Object caller, String onSuccessMethod, String onErrorMethod, File file) {
        client().get(url, new ZenFileHandler(caller, onSuccessMethod, onErrorMethod, _build_filename(url), file));
    }

    //: handle json GET requests. Implements caching via ZenCache
    public static void getJson(String url, Object caller, String onSuccessMethod, String onErrorMethod, Map<String, String> headers, int cache, boolean refresh_cache) {
        getJson(url, caller, onSuccessMethod, onErrorMethod, headers, cache, refresh_cache, true);
    }

    public static void getJson(String url, Object caller, String onSuccessMethod, String onErrorMethod, Map<String, String> headers, int cache, boolean refresh_cache, boolean async) {
        ZenJsonHandler handler = new ZenJsonHandler(caller, onSuccessMethod, onErrorMethod, url, cache);
        boolean with_cache = (cache != 0);
        boolean run_req = true;
        if (with_cache) {
            if (!refresh_cache && handler.hasCache()) {
                run_req = false;
                handler.getFromCache();
            }
        }
        if (run_req) {
            AsyncHttpClient c;
            if (async) {
                c = client();
            } else {
                c = syncClient;
            }
            if (headers != null) {
                Header[] mheaders = _parseHeaders(headers);
                c.get(ZenApplication.context(), url, mheaders, null, handler);
            } else {
                c.get(url, handler);
            }
        }
    }

    //: handle json POST requests
    //  (caching is not implemented because is a POST: body will change on message sent)
    public static void postJson(String url, Object caller, String onSuccessMethod, String onErrorMethod, StringEntity body, Map<String, String> headers) {
        Header[] mheaders = null;
        if (headers != null) {
            mheaders = _parseHeaders(headers);
        }
        client().post(ZenApplication.context(), url, mheaders, body, "application/json",
                new ZenJsonHandler(caller, onSuccessMethod, onErrorMethod));
    }
}
