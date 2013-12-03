package io.thera.zen.json;

import android.widget.TextView;
import java.lang.reflect.*;

import io.thera.zen.core.ZenAppManager;

public class ZenJsonManager {
	//"http://twitter.com/statuses/user_timeline/vogella.json"

	
	public static void parseJson (String url , String m, Object caller) {
			
		    try {

		    	System.out.println("sono parsejson");
				 getJsonFromUrl(url , m , caller);
		    } catch (Exception e) {
		      e.printStackTrace();
		    }
	}
	
	public static void getJsonFromUrl(String url , String method , Object caller) {
	    
	    try {
	    	if (ZenAppManager.isConnected()) {
	    		/*
	    		 * CREATE A NEW TASK AND PERFORM CONNECTION
	    		 */
	    		new ZenTask("json", method, caller).execute(url);
	    	}
	    	else {
	    		/*
	    		 *  YOU ARE NOT CONNECTED. ERROR MESSAGE DISPLAYED.
	    		 */
	    		//container.setText("Non sono riuscito a stabilire una connessione");
	    	}
	    } catch (Exception e) {
	    	//container.setText("Exception in ZenJsonManager : "+ e.getMessage());
	    	e.printStackTrace();
	    	return;
	    }
	  }
	
	
}
