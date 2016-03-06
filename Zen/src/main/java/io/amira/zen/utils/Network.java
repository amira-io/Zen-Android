/*
 * ZenFramework for Android
 *
 * :copyright: (c) 2013-2016 by Marco Stagni, Giovanni Barillari
 * :license: GPLv3, see LICENSE for more details.
 */

package io.amira.zen.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import io.amira.zen.core.ZenApplication;

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
