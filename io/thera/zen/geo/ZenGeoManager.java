package io.thera.zen.geo;

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

@TargetApi(Build.VERSION_CODES.GINGERBREAD)
public class ZenGeoManager implements LocationListener{

	private double latitude;
	private double longitude;
	private double bearing;
	private double speed;
	private double altitude;
	
	private LocationManager locationManager;
	private Context context;
		
	private static boolean isListenerSet = false;
	
	
	public ZenGeoManager(Object lm, Activity a) {
		try {
			this.locationManager  = (LocationManager) lm;
			this.context = a.getApplicationContext();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public synchronized void setListenerStatus(boolean status) {
		if (status!=isListenerSet) {
			if (status) {
				Criteria c = new Criteria();
				long minTime = 1000;
				float minDistance =1;
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
	}
	
	@Override
	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub
		latitude = location.getLatitude();
		longitude = location.getLongitude();
		bearing = location.getBearing();
		speed = location.getSpeed();
		altitude = location.getAltitude();
		CharSequence c = latitude + "-" + longitude;
		Toast.makeText(context , c , Toast.LENGTH_LONG).show();

	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
		
	}

}
