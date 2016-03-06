/*
 * ZenFramework for Android
 *
 * :copyright: (c) 2013-2016 by Marco Stagni, Giovanni Barillari
 * :license: GPLv3, see LICENSE for more details.
 */

package io.amira.zen.geo;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;

import java.lang.reflect.InvocationTargetException;

public class ZenLocationListener implements LocationListener {

    String callback;
    Object caller;

    public ZenLocationListener(String callback, Object caller) {
        this.callback   =   callback;
        this.caller     =   caller;

    }
    @Override
    public void onLocationChanged(Location location) {
        Class[] params = new Class[1];
        params[0] = Location.class;

        Object[] values = new Object[1];
        values[0] = location;
        try {
            if (this.caller instanceof Class) {
                ((Class) this.caller).getMethod(this.callback, params).invoke(this.caller,values);
            }
            else {
                this.caller.getClass().getMethod(this.callback, params).invoke(this.caller, values);
            }
        } catch (IllegalAccessException e) {
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
