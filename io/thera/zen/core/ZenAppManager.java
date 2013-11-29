package io.thera.zen.core;

/**
 * Created by marcostagni on 26/11/13.
 *
 * Copyright © 2013. Thera Technologies.
 */

import java.lang.reflect.InvocationTargetException;
import java.util.*;

import android.app.*;
import android.content.Context;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.Window;

public class ZenAppManager {

    static Object current;
    //used to store current activity/fragment.

    public static synchronized void setCurrentPosition(Object o) {
        current = o;
    }

    public static synchronized Object getCurrentPosition ( ) {
        return current;
    }

    Stack<Object> currentStack;

    static String[] layoutNames;
    static String[] layoutTitles;

    static Map<String,Integer> layouts = new HashMap<String,Integer>();
    static Map<String, String> layoutsString = new HashMap<String,String>();

    static Integer[] layoutIds;

    static int layoutIndex = 0;

    public static Integer[] getLayoutIds() {
        return layoutIds;
    }

    public static Map<String,Integer> getLayouts() {

        return layouts;

    }

    public static Map<String,String> getLayoutsString() {

        return layoutsString;

    }

	/**
	 * CONNECTION FLAG.
	 */

    public static boolean isConnected = false;

    public static synchronized boolean isConnected () {
        return isConnected;
    }

    public static synchronized void setConnected ( boolean flag ) {

        isConnected = flag;

    }

	/**
	 * GENERAL VARIABLES
	 */

    static String resourceClass;

    public static synchronized String getResourceClass() {

        return resourceClass;

    }

    static Activity activity;

    public static synchronized Activity getActivity() {

        return activity;

    }

    public static synchronized void setActivity( Activity a) {

        activity = a;
    }

	/**
	 * METHODS FOR HANDLING ACTIVITY.
	 */

    private static boolean drawerFlag = false;

    public static synchronized void revertDrawerFlag( ) {

        drawerFlag = (!drawerFlag);
    }

    public static synchronized void moveDrawer(boolean flag) {
        try {
            ZenLog.l("PRIMA " + drawerFlag);
            if (flag) {
                activity.getClass().getMethod("closeDrawer", null).invoke(activity, null);
            }
            else {
                activity.getClass().getMethod("openDrawer", null).invoke(activity, null);
            }
            ZenLog.l("DOPO "+drawerFlag);

        } catch (IllegalArgumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

	/**
	 * SETTING UP APP VARIABLE.
	 */

    public static synchronized boolean start (Activity a , int type) {

		/**
		 * SET CURRENT ACTIVITY
		 */

        setActivity(a);

		/**
		 * CHECKING FOR CONNECTION.
		 */
        ConnectivityManager connMgr = (ConnectivityManager) a.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        setConnected(networkInfo != null && networkInfo.isConnected());

	    /**
	     * BUILDING RESOURCE CLASS NAME
	     */

        String pack = a.getClass().getPackage().getName();
        resourceClass = pack + ".R";

	    /**
	     * DIFFERENT BEHAVIOUR FOR DIFFERENT LAYOUT.
	     */

        switch(type) {

            case 1: {
                //drawer layout
                setUpDrawer();
            }
            case 2: {

            }
            case 3: {

            }
        }
        return true;
    }

	/**
	 * METHOD FOR DRAWWERLAYOUT
	 */

    private static synchronized void setUpDrawer() {

		/**
		 * LOAD VIEW ARRAY FROM RESOURCES.
		 */

        layoutTitles = activity.getResources().getStringArray(ZenResManager.getArrayId("items"));
        layoutNames = activity.getResources().getStringArray(ZenResManager.getArrayId("layouts"));

        if (layoutTitles.length == layoutNames.length) {

            layoutIds = new Integer[layoutTitles.length];

            for (int i = 0; i < layoutTitles.length; i++) {

                Integer  id     = ZenResManager.getLayoutId(layoutNames[i]);
                layoutIds[i]    = id;
                layouts.put(layoutTitles[i], id);
                layoutsString.put(layoutTitles[i], layoutNames[i]);

            }

        }

        activity.requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        activity.setContentView(ZenResManager.getLayoutId("activity_android_test_app"));
        activity.getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, ZenResManager.getLayoutId("activity_title_bar"));
    }
}

