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
	/*
	public ZenGeoManager ( String callback, Object caller) {
		try {
            this.callback   = callback;
            this.caller     =
   			//this.locationManager    = (LocationManager) lm;
			//this.context            = a.getApplicationContext();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}*/

    public static synchronized void getPosition (String callback_name , Object caller_name) {

        if (!isListenerSet) {

            isListenerSet      = true;
            callback           = callback_name;
            caller             = caller_name;

            locationListener   = new ZenLocationListener(callback,caller);



            Criteria c          =   new Criteria();
            long minTime        =   1000;
            float minDistance   =   1;
            locationManager    = (LocationManager) ZenAppManager.getActivity().getSystemService(Context.LOCATION_SERVICE);
            locationManager.requestLocationUpdates(minTime, minDistance, c, locationListener , null);
        }

    }


    public static synchronized void stopListenPosition () {

            isListenerSet = false;
            locationManager.removeUpdates(locationListener);

    }
	/*
	public synchronized void setListenerStatus(boolean status) {
		if (status!=isListenerSet) {
			if (status) {
				Criteria c          =   new Criteria();
				long minTime        =   1000;
				float minDistance   =   1;
				locationManager.requestLocationUpdates(minTime, minDistance, c, this, null);
			}
			else {
				locationManager.removeUpdates(this);
			}	
			isListenerSet = status;
		}
		else {
			//se status e il flag sono uguali, abbiamo gi� fatto il setup
			//dovrei mostrare un messaggio di errore.
		}
	}*/
	


}
