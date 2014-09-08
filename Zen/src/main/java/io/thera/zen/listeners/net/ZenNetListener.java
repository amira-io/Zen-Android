package io.thera.zen.listeners.net;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

import io.thera.zen.core.ZenAppManager;

/**
 * Created by marcostagni on 10/01/14.
 */
public class ZenNetListener extends BroadcastReceiver {

    private static int TYPE_WIFI = 1;
    private static int TYPE_MOBILE = 2;
    private static int TYPE_NOT_CONNECTED = 0;

    public static int getConnectivityStatus(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (null != activeNetwork) {
            if(activeNetwork.getType() == ConnectivityManager.TYPE_WIFI)
                return TYPE_WIFI;

            if(activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE)
                return TYPE_MOBILE;
        }
        return TYPE_NOT_CONNECTED;
    }

    public static String getConnectivityStatusString(Context context) {
        int conn = getConnectivityStatus(context);
        String status = null;
        if (conn == TYPE_WIFI) {
            status = "WIFI";
        } else if (conn == TYPE_MOBILE) {
            status = "MOBILE";
        } else if (conn == TYPE_NOT_CONNECTED) {
            status = "no";
        }
        return status;
    }

    @Override
    public void onReceive(final Context context, final Intent intent) {

        String status = getConnectivityStatusString(context);
        if (status.equals("no")) {
            ZenAppManager.setConnected(false);
        }
    }
}
