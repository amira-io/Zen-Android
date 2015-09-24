package io.amira.zen.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import io.amira.zen.core.ZenApplication;

/**
 * Created by giovanni on 14/05/14.
 */
public class Network {

    private static boolean connected = false;

    public static boolean isConnected () {

        ConnectivityManager connMgr = (ConnectivityManager) ZenApplication.context().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        setConnected(networkInfo != null && networkInfo.isConnected());

        return connected;
    }

    public static void setConnected ( boolean flag ) {

        connected = flag;

    }
}
