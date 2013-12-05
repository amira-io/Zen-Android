package io.thera.zen.layout.drawer;

/**
 * Created by marcostagni on 26/11/13.
 *
 * Copyright © 2013. Thera Technologies.
 */

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.HashMap;
import java.util.Map;

import io.thera.zen.core.ZenAppManager;
import io.thera.zen.core.ZenLog;
import io.thera.zen.core.ZenNavigationManager;


public abstract class ZenDetailFragment extends Fragment {


    public static final String ARG_PLANET_NUMBER = "myFragment String";

    /*
        NAVIGATION PARAMETERS.
     */

    //private static Map<String, Map<Class,Object>> parameters = new HashMap<String, Map<Class, Object>>();

    private  String title;

    public   String getTitle () {

        return title;

    }



    private FragmentActivity currentActivity;

    private int layoutId;

    private View rootView;

    public ZenDetailFragment () {

		/*
		 * EMPTY CONSTRUCTOR
		 */

    }

    public ZenDetailFragment ( String cazzo) {
        ZenLog.l(cazzo);
    }

    public void setVariables ( FragmentActivity a , String title, Integer layoutId) {

        ZenLog.l("SETTING VARIABLES " + title + " - " + ((Object) this).getClass().getCanonicalName())  ;
        this.currentActivity 	= a;
        this.title 				= title;
        this.layoutId 			= layoutId;
    }

    public Object findViewById(int id) {
        return rootView.findViewById(id);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
		/*
		 *  CHECKING METHOD ARGUMENTS
		 */
        //int i 					= getArguments().getInt(ARG_PLANET_NUMBER);
        //this.title 				= getArguments().getString("title");
        //this.currentActivity 	= (Activity) getArguments().get("current_activity");

	    /*
	     * 	RETRIEVING VIEW.
	     */
        //= new View(currentActivity);
        try{

            //String layoutName = ATLAppManager.getLayouts().get(title);
            //Integer layoutId = ATLAppManager.getLayouts().get(title);

            long p = System.nanoTime();
            //rootView = inflater.inflate(ATLResourceManager.getLayoutId(layoutName), container, false);
            //rootView = inflater.inflate(layoutId, container, false);
            rootView = inflater.inflate(layoutId, container, false);
            long d = System.nanoTime();
            ZenLog.l("TIME to inflate view "+(d-p));

        }
        catch (NullPointerException e) {
            e.printStackTrace();
            return null;
        }
	    /*
	    if (this.title.equals("4")) {
	    	rootView = inflater.inflate(R.layout.json_example ,container, false);
	    	TextView jsonText = (TextView) rootView.findViewById(R.id.jsonText);
	    	String tmpUrl = "http://maps.googleapis.com/maps/api/geocode/json?latlng=44,16&sensor=true";

	    	ZenJsonManager.parseJson(tmpUrl, jsonText);
	    }
	    if (this.title.equals("5")) {
	    	rootView = inflater.inflate(R.layout.json_example ,container, false);
	    	TextView jsonText = (TextView) rootView.findViewById(R.id.jsonText);
	    	//String tmpUrl = "http://maps.googleapis.com/maps/api/geocode/json?latlng=44,16&sensor=true";
	    	ZenGeoManager geo = new ZenGeoManager(currentActivity.getSystemService(Context.LOCATION_SERVICE), currentActivity);
	    	geo.setListenerStatus(true);
	    	//ZenJsonManager.parseJson(tmpUrl, jsonText);
	    }
	    else {
	    	rootView = inflater.inflate(R.layout.myfragment, container, false);
	    	  TextView t = (TextView) rootView.findViewById(R.id.fragment_text);
	          t.setText(this.title);
	          getActivity().setTitle(this.title);
	    }
       */
		/*
		 * 	SETTING UP VIEW ELEMENTS.
		 */
        // TextView t = (TextView) rootView.findViewById(R.id.fragment_text);
        // t.setText(this.title);
        // getActivity().setTitle(this.title);


        /*
         * 	RETURNING VIEW ELEMENT.
         *  SUBCLASS MUST PROVIDE IMPLEMENTATION
         *  TO GETELEMENTS AND SETUPELEMENTS,
         */
        ZenAppManager.moveDrawer(true);
        //parameters = ZenNavigationManager.getParameters();
        getElements();
        buildElements();
        return rootView;
    }



	/*
	 * THESE METHODS MUST BE IMPLEMENTED IN SUBCLASS.
	 */

    //basic setter and getter methods
	/*
	 *
	 */
    public abstract void getElements();


    public abstract void buildElements();

	/*
	 * HANDLER METHODS.
	 */

    public abstract void handleTouch();



}
