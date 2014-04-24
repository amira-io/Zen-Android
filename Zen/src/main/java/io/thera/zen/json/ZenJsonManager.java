package io.thera.zen.json;

import android.widget.Toast;

import java.util.Map;

import io.thera.zen.core.ZenAppManager;

public class ZenJsonManager {
    //"http://twitter.com/statuses/user_timeline/vogella.json"

    // simple get function
    public static void parseJson (String url , String m, Object caller) {
        parseJson(url, m, caller, null, "get", null);
    }

    // simple get plus connection check param
    public static void parseJson (String url , String m, Object caller, boolean connCheck) {
        parseJson(url, m, caller, null, "get", null, connCheck);
    }

    // simple get plus onError callback (with connection check on)
    public static void parseJson (String url , String m, Object caller, String onError) {
        parseJson(url, m, caller, null, "get", null, onError);
    }

    // get with headers
    public static void parseJson (String url , String m, Object caller, Map<String, String> reqheaders) {
        parseJson(url, m, caller, reqheaders, "get", null);
    }

    // get with headers and connection check param
    public static void parseJson (String url , String m, Object caller, Map<String, String> reqheaders, boolean connCheck) {
        parseJson(url, m, caller, reqheaders, "get", null, connCheck);
    }

    // get with headers and onError callback (connection check on)
    public static void parseJson (String url , String m, Object caller, Map<String, String> reqheaders, String onError) {
        parseJson(url, m, caller, reqheaders, "get", null, true, onError);
    }

    // post with headers
    public static void parseJson (String url, String m, Object caller, Map<String, String> reqheaders, String mode, Map<String, String> params) {
        parseJson(url, m, caller, reqheaders, mode, params, true);
    }

    // to use a onError callback (with connection check)
    public static void parseJson (String url, String m, Object caller, Map<String, String> reqheaders, String mode, Map<String, String> params, String onError) {
        parseJson(url, m, caller, reqheaders, mode, params, true, onError);
    }

    // to avoid connection check
    public static void parseJson (String url, String m, Object caller, Map<String, String> reqheaders, String mode, Map<String, String> params, boolean connCheck) {
        parseJson(url, m, caller, reqheaders, mode, params, connCheck, null);
    }

    // complete method
    public static void parseJson (String url, String m, Object caller, Map<String, String> reqheaders, String mode, Map<String, String> params, boolean connCheck, String onError) {
        getJsonFromUrl(url , m , caller, reqheaders, mode, params, connCheck, onError);
    }

    private static void getJsonFromUrl(String url , String method , Object caller, Map<String, String> headers, String mode, Map<String, String> params, boolean connCheck, String onError) {

        try {
            if (connCheck) {
                if (ZenAppManager.isConnected()) {
                    /*
                     * CREATE A NEW TASK AND PERFORM CONNECTION
                     */
                    new ZenTask("json", method, caller, onError).execute(url, headers, mode, params);
                }
                else {
                    /*
                     *  YOU ARE NOT CONNECTED. ERROR MESSAGE DISPLAYED.
                     */
                    //container.setText("Non sono riuscito a stabilire una connessione");

                    if (onError != null) {
                        if (caller instanceof Class) {
                            ((Class) caller).getMethod(onError).invoke(caller);
                        }
                        else {
                            caller.getClass().getMethod(method).invoke(caller);
                        }
                    } else {
                        ZenAppManager.getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(ZenAppManager.getActivity().getApplicationContext(),
                                        "Non sei connesso a internet.",
                                        1000).show();
                            }
                        });
                    }
                }
            } else {
                new ZenTask("json", method, caller, onError).execute(url, headers, mode, params);
            }

        } catch (Exception e) {

            if (onError != null) {
                try {
                    if (caller instanceof Class) {
                        ((Class) caller).getMethod(onError).invoke(caller);
                    }
                    else {
                        caller.getClass().getMethod(method).invoke(caller);
                    }
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
            }
        }
    }


}
