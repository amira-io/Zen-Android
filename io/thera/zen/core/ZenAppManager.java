package io.thera.zen.core;

/**
 * Created by marcostagni on 26/11/13.
 *
 * Copyright © 2013. Thera Technologies.
 */

import java.lang.reflect.InvocationTargetException;
import java.util.*;
import android.content.Context;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.Window;
import android.support.v4.app.FragmentActivity;

public class ZenAppManager {

    static Object current;
    //used to store current activity/fragment.

    public static synchronized void setCurrentPosition(Object o) {
        current = o;
    }

    public static synchronized Object getCurrentPosition ( ) {
        return current;
    }

    static Stack<Object> currentStack;

    static String[] layoutNames;
    static String[] layoutTitles;

    static Map<String,Integer> layouts = new HashMap<String,Integer>();
    static Map<String, String> layoutsString = new HashMap<String,String>();

    static Integer[] layoutIds;

    static Map<String,String> detailLayouts;

    static int layoutIndex = 0;

    public static Integer[] getLayoutIds() {
        return ZenSettingsManager.getDrawerLayoutIds();
    }

    public static Map<String,Integer> getLayouts() {

        return ZenSettingsManager.getDrawerLayoutMap();

    }

    public static Map<String,String> getLayoutsString() {

        return ZenSettingsManager.getDrawerLayoutString();

    }

    public static Map<String,String> getDetailLayouts() {
        return detailLayouts;
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

    static FragmentActivity activity;

    public static synchronized FragmentActivity getActivity() {

        return activity;

    }

    public static synchronized void setActivity( FragmentActivity a) {

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

    public static synchronized boolean start (FragmentActivity a) {
        /**
         *  GETTING INITIAL VALUES FROM SETTINGS FILE
         */

        ZenSettingsManager.start();

		/**
		 * SET CURRENT ACTIVITY
		 */

        setActivity(a);
        setCurrentPosition(a);
        currentStack = new Stack();
        currentStack.push(a);

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

        switch(ZenSettingsManager.getLayoutType()) {

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
	 * METHOD FOR DRAWERLAYOUT
	 */

    private static synchronized void setUpDrawer() {

		/**
		 * LOAD VIEW ARRAY FROM RESOURCES.
		 */
        System.out.println("sono prima di parsedrawer");
        ZenSettingsManager.parseDrawerMenuLayout();
        layoutTitles    = ZenSettingsManager.getDrawerMenuTitles();
        layoutNames     = ZenSettingsManager.getDrawerMenuLayouts();
        layouts         = ZenSettingsManager.getDrawerLayoutMap();
        layoutsString   = ZenSettingsManager.getDrawerLayoutString();
        layoutIds       = ZenSettingsManager.getDrawerLayoutIds();

        detailLayouts   = new HashMap<String,String>();
        detailLayouts   = ZenSettingsManager.getDetailMap();
        ZenLog.l("printing length");
        ZenLog.l(layoutTitles.length);

        /*
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
        */


    }
}

