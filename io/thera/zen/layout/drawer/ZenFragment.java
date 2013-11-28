package io.thera.zen.layout.drawer;

/**
 * Created by marcostagni on 26/11/13.
 *
 * Copyright © 2013. Thera Technologies.
 */

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Fragment;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import io.thera.zen.core.ZenAppManager;
import io.thera.zen.core.ZenLog;

//import com.marcostagni.androidtestapp.R;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public abstract class ZenFragment extends Fragment {


    public static final String ARG_PLANET_NUMBER = "myFragment String";

    private String title;

    private Activity currentActivity;

    private int layoutId;

    public ZenFragment () {

		/*
		 * EMPTY CONSTRUCTOR
		 */

    }

    public void setVariables ( Activity a , String title, Integer layoutId) {
        this.currentActivity 	= a;
        this.title 				= title;
        this.layoutId 			= layoutId;
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
        View rootView; //= new View(currentActivity);
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

	    	JsonManager.parseJson(tmpUrl, jsonText);
	    }
	    if (this.title.equals("5")) {
	    	rootView = inflater.inflate(R.layout.json_example ,container, false);
	    	TextView jsonText = (TextView) rootView.findViewById(R.id.jsonText);
	    	//String tmpUrl = "http://maps.googleapis.com/maps/api/geocode/json?latlng=44,16&sensor=true";
	    	GeoManager geo = new GeoManager(currentActivity.getSystemService(Context.LOCATION_SERVICE), currentActivity);
	    	geo.setListenerStatus(true);
	    	//JsonManager.parseJson(tmpUrl, jsonText);
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
        getElements();
        setElements();
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


    public abstract void setElements();

	/*
	 * HANDLER METHODS.
	 */

    public abstract void handleTouch();



}
