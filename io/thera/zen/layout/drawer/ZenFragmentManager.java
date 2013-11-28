package io.thera.zen.layout.drawer;

/**
 * Created by marcostagni on 26/11/13.
 *
 * Copyright © 2013. Thera Technologies.
 */

import android.app.Activity;
import android.app.FragmentManager;
import android.os.Build;
import android.support.v4.widget.DrawerLayout;
import android.widget.ListView;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import io.thera.zen.core.ZenAppManager;
import io.thera.zen.core.ZenLog;
import io.thera.zen.core.ZenResManager;

public class ZenFragmentManager {

    static Map<String,Object> availableFragments = new HashMap<String,Object>();


    public static void setZenFragment (String title, int position , Activity activity, DrawerLayout drawer , ListView drawerList) {


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {

            ZenLog.l("your api version is ok");
			/*
			 * API LEVEL GREATER OR EQUAL TO HONEYCOMB.
			 */

            if (availableFragments.containsKey(title)) {
				/*
				 * IF WE HAVE ALREADY CREATED AN ATLFRAGMENT,
				 * THEN IT'S INSIDE AVAILABLEFRAGMENTS.
				 */
                try {
                    long p = System.nanoTime();
                    ZenLog.l("old fragment");
                    int content_frame_id = ZenResManager.getResourceId("content_frame");
                    FragmentManager fragmentManager = activity.getFragmentManager();
                    fragmentManager.beginTransaction().replace(content_frame_id, (android.app.Fragment) availableFragments.get(title)).commit();
                    long d = System.nanoTime();
                    ZenAppManager.moveDrawer(true);
                    ZenLog.l("TIME to recover old "+(d-p));
                }
                catch (NullPointerException e ){
                    e.printStackTrace();
                }
            }
            else {
				/*
				 * CREATE A NEW FRAGMENT
				 *
				 * THERE MUST BE A CLASS NAMED:
				 *
				 * titleController.java
				 *
				 * WHICH EXTENDS ATLFragment
				 * if such class is not available,
				 * ClassNotFoundException is launched.
				 *
				 */
                try {
                    //String toCallClass = title.replace("_", "");
                    ZenLog.l("create new fragment");
                    long p = System.nanoTime();
                    String toCallClass = ZenAppManager.getLayoutsString().get(title);
                    toCallClass = Character.toUpperCase(toCallClass.charAt(0)) + toCallClass.substring(1);
                    Class toCall = Class.forName("io.thera.Controllers."+toCallClass+"Controller");
                    ZenLog.l("io.thera.Controllers."+toCallClass+"Controller");
                    try {
                        Object controller = toCall.newInstance();
                        if (controller.getClass().getSuperclass().getCanonicalName().equals("io.thera.atl.layout.ATLFragment")) {
							/*
							 * QUESTO VUOL DIRE CHE ABBIAMO CARICATO UNA CLASSE CHE HA COME SUPERCLASSE ATLFRAGMENT
							 */
                            Class[] paramTypes 	= new Class[3]; //prima era new class[2]
                            paramTypes[0] 		= Activity.class;
                            paramTypes[1] 		= String.class;

                            paramTypes[2] 		= Integer.class;

                            //paramTypes[3] 		= DrawerLayout.class;
                            //paramTypes[4] 		= ListView.class;

                            Integer layoutId = ZenAppManager.getLayoutIds()[position];

                            toCall.getMethod("setVariables", paramTypes).invoke(controller, createParameters(activity, title, layoutId ));

                            int content_frame_id = ZenResManager.getResourceId("content_frame");
                            FragmentManager fragmentManager = activity.getFragmentManager();
                            fragmentManager.beginTransaction().replace(content_frame_id, (android.app.Fragment) controller ).commit();
                            availableFragments.put(title, controller);
                            long d = System.nanoTime();
                            ZenLog.l("TIME to create new fragment " + (d-p));

                        }
                    } catch (InstantiationException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    } catch (NoSuchMethodException e) {

                        e.printStackTrace();
                    } catch (InvocationTargetException e) {

                        e.printStackTrace();
                    } catch(NullPointerException e) {
                        e.printStackTrace();
                    }
                } catch (ClassNotFoundException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                //ATLFragment atlf = new ATLFragment();
                //atlf.setCurrentActivity(activity);
                //Bundle args = new Bundle();

                //args.putString("title", title);


                //atlf.setArguments(args);

                //int content_frame_id = getResourceId("content_frame");
                //FragmentManager fragmentManager = activity.getFragmentManager();
                //fragmentManager.beginTransaction()
                //               .replace(content_frame_id, atlf)
                //               .commit();
			    /*
			     * PUSHING NEW FRAGMENT INSIDE LIST
			     */

            }

        }
        else {
			/*
			 * LOWER API.
			 */
        }

		/*
		 * FINALLY WE CLOSE THE DRAWER.
		 */
        //ATLAppManager.closeDrawer();
    }

	/*
	 * METHOD FOR GETTING PARAMETERS FOR JAVA REFLECTION
	 */

    public static Object[] createParameters( Activity a , String title , int position) {
        Object[] parameters = new Object[3];
        parameters[0] = a;
        parameters[1] = title;
        parameters[2] = position;

        return parameters;
    }


}