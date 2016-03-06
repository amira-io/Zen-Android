/*
 * ZenFramework for Android
 *
 * :copyright: (c) 2013-2016 by Marco Stagni, Giovanni Barillari
 * :license: GPLv3, see LICENSE for more details.
 */

package io.amira.zen.geo;

import java.util.Stack;

import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.content.Context;

import io.amira.zen.core.ZenApplication;


public class ZenGeoManager{
    private static final int DEF_VALIDMINS = 5;

    private static LocationManager locationManager;
    private static ZenLocationListener locationListener;
    private static Criteria criteria;
    private static String provider;

    private static Stack<Object[]> callStack;

    private static synchronized void _initManager() {
        if (locationManager != null) {
            return;
        }
        locationManager = (LocationManager) ZenApplication.context().getSystemService(Context.LOCATION_SERVICE);
        locationListener   = new ZenLocationListener("_stopListenPosition", ZenGeoManager.class);
        criteria = new Criteria();
        _findBestProvider();
        callStack = new Stack<Object[]>();
    }

    private static void _findBestProvider() {
        //: set best criteria
        criteria.setPowerRequirement(Criteria.POWER_LOW);
        criteria.setAccuracy(Criteria.ACCURACY_FINE);

        //: verify provider exists, lower criteria accuracy
        provider = locationManager.getBestProvider(criteria , true);
        if (provider == null) {
            criteria.setAccuracy(Criteria.ACCURACY_COARSE);
            provider = locationManager.getBestProvider(criteria, true);
            if (provider == null) {
                criteria.setPowerRequirement(Criteria.POWER_HIGH);
                provider = locationManager.getBestProvider(criteria, true);
            }
        }
    }

    public static void getPosition(String callback, Object caller) {
        getPosition(callback, caller, DEF_VALIDMINS);
    }

    public static void getPosition(String callback , Object caller, int last_valid_minutes) {
        int timelimit = last_valid_minutes * 1000 * 60;

        //: init static elements
        _initManager();

        //: store caller data in stack
        callStack.push(new Object[]{caller, callback});

        //: if location is not available on phone, avoid trying to get it
        if (provider == null) {
            _stopListenPosition(null);
            return;
        }

        //: first we read latest available position
        Location lastPos = null;
        if (timelimit != 0) {
            lastPos = locationManager.getLastKnownLocation(provider);
            if (lastPos != null) {
                long dt = System.currentTimeMillis() - lastPos.getTime();
                if (dt > timelimit) {
                    lastPos = null;
                }
            }
        }
        if (lastPos != null) {
            _stopListenPosition(lastPos);
            return;
        }

        //: if last position unavailable, request a new one
        locationManager.requestSingleUpdate(criteria, locationListener, null);
    }


    public static void _stopListenPosition(Location location) {
        try {
            locationManager.removeUpdates(locationListener);
        } catch (Exception e) {
            //: this is not a big deal, no need to catch it
        }

        //: get caller data
        Object[] callData = callStack.pop();
        Object caller = callData[0];
        String callback = callData[1].toString();

        //: return position to caller
        Class[] params = new Class[1];
        params[0] = Location.class;
        Object[] values = new Object[1];
        values[0] = location;
        try {
            if (caller instanceof Class) {
                ((Class)caller).getMethod(callback, params).invoke(caller,values);
            }
            else {
                caller.getClass().getMethod(callback, params).invoke(caller, values);
            }
        } catch (Exception e) {
            ZenApplication.log.e(e);
        }
    }

}
