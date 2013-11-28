package io.thera.zen.geo;

import android.app.Activity;
import android.content.Context;
import android.location.LocationManager;

public class ZenGeoUtil {

	
	public static synchronized void setLocationManager(Activity a) {
		
		//locationManager = (LocationManager) a.getSystemService(Context.LOCATION_SERVICE);
		
	}
	
	public static boolean isGpsEnabled (LocationManager locationManager) {
		
		return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
	}
	
	public static boolean isNetworkEnabled(LocationManager locationManager) {
		
		return locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
		
	}
	
	public static boolean isPassiveEnabled (LocationManager locationManager) {
		
		return locationManager.isProviderEnabled(LocationManager.PASSIVE_PROVIDER);
	}
	
	
	
}
