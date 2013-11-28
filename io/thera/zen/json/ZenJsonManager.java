package io.thera.zen.json;

import android.widget.TextView;

public class ZenJsonManager {
	//"http://twitter.com/statuses/user_timeline/vogella.json"
	
	public static void ciao() {
		
	}
	
	public static void parseJson (String url , TextView container , TextView second) {
			
		    try {
		    	System.out.println("sono parsejson");
				 getJsonFromUrl(url,container,second);
		    } catch (Exception e) {
		      e.printStackTrace();
		    }
	}
	
	public static void getJsonFromUrl(String url , TextView container,TextView second) {
	    
	    try {
	    	if (ZenJsonUtil.isConnected) {
	    		/*
	    		 * CREATE A NEW TASK AND PERFORM CONNECTION
	    		 */
	    		new ZenTask("json",container,second).execute(url);
	    	}
	    	else {
	    		/*
	    		 *  YOU ARE NOT CONNECTED. ERROR MESSAGE DISPLAYED.
	    		 */
	    		//container.setText("Non sono riuscito a stabilire una connessione");
	    	}
	    } catch (Exception e) {
	    	container.setText("Exception in ZenJsonManager : "+ e.getMessage());
	    	e.printStackTrace();
	    	return;
	    }
	  }
	
	
}
