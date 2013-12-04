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
import io.thera.zen.core.ZenNavigationManager;
import io.thera.zen.core.ZenResManager;

public class ZenFragmentManager {

    static Map<String,Object> availableFragments = new HashMap<String,Object>();


    public static void setZenFragment (String title , Activity activity) {

        ZenLog.l("SETZENFRAGMENT " + title + " - " + activity.getClass().getCanonicalName());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {

            ZenLog.l("your api version is ok");
			/*
			 * API LEVEL GREATER OR EQUAL TO HONEYCOMB.
			 */

            ZenLog.l("AVAILABLEFRAGMENTS ARE: "+availableFragments);
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

                    //TEST
                    try {
                        ZenLog.l("ABOUT TO PUSH" + (String) availableFragments.get(title).getClass().getMethod("getTitle",null).invoke(availableFragments.get(title),null));
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    } catch (NoSuchMethodException e) {
                        e.printStackTrace();
                    }

                    ZenNavigationManager.push(availableFragments.get(title));
                    //TEST

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

                    //TENTATIVO
                    int pos = ZenAppManager.getLayouts().get(title); //prima non esisteva.
                    //TENTATIVO

                    toCallClass = Character.toUpperCase(toCallClass.charAt(0)) + toCallClass.substring(1);
                    Class toCall = Class.forName("app.Controllers."+toCallClass+"Controller");
                    ZenLog.l("app.Controllers."+toCallClass+"Controller");
                    try {
                        /*

                        Object controller = toCall.newInstance();
                        //Object controller = toCall.cast("ciaociaociaociaociaociaociaociaociao");
                        if (controller.getClass().getSuperclass().getCanonicalName().equals("io.thera.zen.layout.drawer.ZenFragment")) {
							//
							// QUESTO VUOL DIRE CHE ABBIAMO CARICATO UNA CLASSE CHE HA COME SUPERCLASSE ATLFRAGMENT
							//
                            Class[] paramTypes 	= new Class[3]; //prima era new class[2]
                            paramTypes[0] 		= Activity.class;
                            paramTypes[1] 		= String.class;

                            paramTypes[2] 		= Integer.class;

                            //paramTypes[3] 		= DrawerLayout.class;
                            //paramTypes[4] 		= ListView.class;

                            //TENTATIVO
                            //Integer layoutId = ZenAppManager.getLayoutIds()[pos]; // prima era position
                             Integer layoutId = ZenAppManager.getLayouts().get(title);
                            //TENTATIVO

                            toCall.getMethod("setVariables", paramTypes).invoke(controller, createParameters(activity, title, layoutId ));

                            try {
                                ZenLog.l("ALREADY SET" + (String) controller.getClass().getMethod("getTitle", null).invoke(controller, null));
                            } catch (IllegalAccessException e) {
                                e.printStackTrace();
                            } catch (InvocationTargetException e) {
                                e.printStackTrace();
                            } catch (NoSuchMethodException e) {
                                e.printStackTrace();
                            }

                            int content_frame_id = ZenResManager.getResourceId("content_frame");
                            FragmentManager fragmentManager = activity.getFragmentManager();

                            //TEST
                            ZenNavigationManager.push(controller);
                            //TEST

                            fragmentManager.beginTransaction().replace(content_frame_id, (android.app.Fragment) controller ).commit();
                            ZenLog.l("SAVING " + title + " - " + controller.getClass().getCanonicalName());
                            availableFragments.put(title, controller);
                            //TEST
                            try {
                                for (int i =0 ; i < availableFragments.keySet().size() ; i ++ ) {
                                   String key = (String) availableFragments.keySet().toArray()[i];
                                   ZenLog.l("MAP element " + (String) availableFragments.get(key).getClass().getMethod("getTitle", null).invoke(availableFragments.get(key), null));
                                }
                            } catch (IllegalAccessException eX) {
                                eX.printStackTrace();
                            } catch (InvocationTargetException eX) {
                                eX.printStackTrace();
                            } catch (NoSuchMethodException eX) {
                                eX.printStackTrace();
                            }
                            //TEST
                            long d = System.nanoTime();
                            ZenLog.l("TIME to create new fragment " + (d-p));

                        }
                        */


                        availableFragments.put(title, toCall.newInstance());

                        if (availableFragments.get(title).getClass().getSuperclass().getCanonicalName().equals("io.thera.zen.layout.drawer.ZenFragment")) {
                            //
                            // QUESTO VUOL DIRE CHE ABBIAMO CARICATO UNA CLASSE CHE HA COME SUPERCLASSE ATLFRAGMENT
                            //
                            Class[] paramTypes 	= new Class[3]; //prima era new class[2]
                            paramTypes[0] 		= Activity.class;
                            paramTypes[1] 		= String.class;

                            paramTypes[2] 		= Integer.class;

                            //paramTypes[3] 		= DrawerLayout.class;
                            //paramTypes[4] 		= ListView.class;

                            //TENTATIVO
                            //Integer layoutId = ZenAppManager.getLayoutIds()[pos]; // prima era position
                            Integer layoutId = ZenAppManager.getLayouts().get(title);
                            //TENTATIVO

                            toCall.getMethod("setVariables", paramTypes).invoke(availableFragments.get(title), createParameters(activity, title, layoutId ));

                            try {
                                ZenLog.l("ALREADY SET" + (String) availableFragments.get(title).getClass().getMethod("getTitle", null).invoke(availableFragments.get(title), null));
                            } catch (IllegalAccessException e) {
                                e.printStackTrace();
                            } catch (InvocationTargetException e) {
                                e.printStackTrace();
                            } catch (NoSuchMethodException e) {
                                e.printStackTrace();
                            }

                            int content_frame_id = ZenResManager.getResourceId("content_frame");
                            FragmentManager fragmentManager = activity.getFragmentManager();

                            //TEST
                            ZenNavigationManager.push(availableFragments.get(title));
                            //TEST

                            fragmentManager.beginTransaction().replace(content_frame_id, (android.app.Fragment) availableFragments.get(title) ).commit();
                            ZenLog.l("SAVING " + title + " - " + availableFragments.get(title).getClass().getCanonicalName());
                            //TEST
                            try {
                                for (int i =0 ; i < availableFragments.keySet().size() ; i ++ ) {
                                    String key = (String) availableFragments.keySet().toArray()[i];
                                    ZenLog.l("MAP element " + (String) availableFragments.get(key).getClass().getMethod("getTitle", null).invoke(availableFragments.get(key), null));
                                }
                            } catch (IllegalAccessException eX) {
                                eX.printStackTrace();
                            } catch (InvocationTargetException eX) {
                                eX.printStackTrace();
                            } catch (NoSuchMethodException eX) {
                                eX.printStackTrace();
                            }
                            //TEST
                            long d = System.nanoTime();
                            ZenLog.l("TIME to create new fragment " + (d-p));

                        }
                    }

                     catch (InstantiationException e) {
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