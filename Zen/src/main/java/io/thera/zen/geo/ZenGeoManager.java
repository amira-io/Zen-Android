package io.thera.zen.geo;

import java.lang.reflect.InvocationTargetException;

import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.annotation.TargetApi;
import android.content.Context;

import io.thera.zen.core.ZenApplication;


@TargetApi(Build.VERSION_CODES.GINGERBREAD)
public class ZenGeoManager{

    private static LocationManager locationManager;

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
            locationManager    = (LocationManager) ZenApplication.context().getSystemService(Context.LOCATION_SERVICE);

            Criteria c          =   new Criteria();
            c.setPowerRequirement(Criteria.POWER_LOW);
            c.setAccuracy(Criteria.ACCURACY_FINE);

            //: verify provider exists
            String provider = locationManager.getBestProvider(c , true);
            if (provider == null) {
                ZenApplication.log("No location provider found!");
                c.setAccuracy(Criteria.ACCURACY_COARSE);
                provider = locationManager.getBestProvider(c, true);
                if (provider == null) {
                    c.setPowerRequirement(Criteria.POWER_HIGH);
                    provider = locationManager.getBestProvider(c, true);
                    if (provider == null) {
                        _stopListenPosition(null);
                        return;
                    }
                }
            }

            locationManager.requestSingleUpdate(c, locationListener , null);
        }

    }


    public static synchronized void _stopListenPosition(Location location) {
        try {
            locationManager.removeUpdates(locationListener);
        } catch (Exception e) {
            //: this is not a deal, no need to catch
        }

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
