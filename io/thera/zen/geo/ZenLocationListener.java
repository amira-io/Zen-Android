package io.thera.zen.geo;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;

import java.lang.reflect.InvocationTargetException;

import io.thera.zen.core.ZenLog;

/**
 * Created by marcostagni on 03/12/13.
 */
public class ZenLocationListener implements LocationListener {

    String callback;
    Object caller;

    public ZenLocationListener(String callback, Object caller) {
        this.callback   =   callback;
        this.caller     =   caller;
        ZenLog.l(callback + " - " + caller.getClass().getCanonicalName());

    }
    @Override
    public void onLocationChanged(Location location) {
        Class[] params = new Class[1];
        params[0] = Location.class;

        Object[] values = new Object[1];
        values[0] = location;
        try {
            ZenLog.l(this.callback + " - " + this.caller.getClass().getCanonicalName());
            if (this.caller instanceof Class) {
                ZenLog.l("PROVO A CHIAMARE"+((Class) this.caller).getCanonicalName());
                ((Class) this.caller).getMethod(this.callback, params).invoke(this.caller,values);
            }
            else {
                ZenLog.l("NON CLASSE PROVO A CHIAMARE"+this.caller.getClass().getCanonicalName());

                this.caller.getClass().getMethod(this.callback, params).invoke(this.caller, values);
            }        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }


    @Override
    public void onProviderEnabled(String provider) {

    }


    @Override
    public void onProviderDisabled(String provider) {

    }
}
