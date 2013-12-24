package io.thera.zen.json;

import android.content.*;
import android.app.*;
import android.net.*;

public class ZenJsonUtil {
	/*
	 *  DEVELOPMENT. SET FALSE ON PRODUCTION
	 */
	
	public static boolean isTesting = true;
	
	/*
	 * 	CHECK IF CONNECTION IS AVAILABLE
	 */
	public static boolean isConnected = false;
	
	public static synchronized void setIsConnected ( boolean value) {
		isConnected = value;
	}
	
	public static synchronized boolean getIsConnected () {
		return isConnected;
	}
}
