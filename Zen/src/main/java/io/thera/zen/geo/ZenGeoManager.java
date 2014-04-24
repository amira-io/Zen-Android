package io.thera.zen.geo;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;
import android.annotation.TargetApi;
import android.app.*;
import android.content.Context;

import io.thera.zen.core.ZenAppManager;
import io.thera.zen.core.ZenLog;


@TargetApi(Build.VERSION_CODES.GINGERBREAD)
public class ZenGeoManager{



    private double latitude;
    private double longitude;
    private double bearing;
    private double speed;
    private double altitude;

    private static LocationManager locationManager;
    private Context context;

    private static String callback;
    private static Object caller;

    private static boolean isListenerSet = false;


    private static ZenLocationListener locationListener;

    public static synchronized void getPosition(String callback_name , Object caller_name) {

        if (!isListenerSet) {

            isListenerSet      = true;
            callback           = callback_name;
            caller             = caller_name;

            locationListener   = new ZenLocationListener("_stopListenPosition",ZenGeoManager.class);

            Criteria c          =   new Criteria();
            c.setPowerRequirement(Criteria.POWER_LOW);
            c.setAccuracy(Criteria.ACCURACY_FINE);

            locationManager    = (LocationManager) ZenAppManager.getActivity().getSystemService(Context.LOCATION_SERVICE);
            locationManager.requestSingleUpdate(c, locationListener , null);
        }

    }


    public static synchronized void _stopListenPosition(Location location) {

        locationManager.removeUpdates(locationListener);

        //abbiamo ottenuto la posizione
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
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }

        isListenerSet = false;

    }

}
