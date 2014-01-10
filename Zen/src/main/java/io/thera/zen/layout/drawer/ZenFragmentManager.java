package io.thera.zen.layout.drawer;

/**
 * Created by marcostagni on 26/11/13.
 *
 * Copyright © 2013. Thera Technologies.
 */

import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.Fragment;

import android.os.Build;
import android.support.v4.app.FragmentTransaction;
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

    static String lastFragment;

    public static synchronized String getCurrent() {
        return lastFragment;
    }


    public static void setZenFragment (String title) {

        FragmentActivity activity = ZenAppManager.getActivity();

        ZenLog.l("SETZENFRAGMENT " + title + " - " + activity.getClass().getCanonicalName());


        ZenLog.l("your api version is ok :  GRAZIE A STO CAZZO.");
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

                    FragmentManager fragmentManager =  activity.getSupportFragmentManager();

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
                    lastFragment = title;
                    //TEST

                    FragmentTransaction transaction = fragmentManager.beginTransaction();

                    if (ZenNavigationManager.isBack()){

                        transaction.setCustomAnimations(ZenResManager.getAnimId("back_enter"), ZenResManager.getAnimId("back_exit"), ZenResManager.getAnimId("back_pop_enter") , ZenResManager.getAnimId("back_pop_exit"));

                    }
                    else {
                        transaction.setCustomAnimations(ZenResManager.getAnimId("enter"), ZenResManager.getAnimId("exit"), ZenResManager.getAnimId("pop_enter") , ZenResManager.getAnimId("pop_exit"));
                    }

                    transaction.replace(content_frame_id, (Fragment) availableFragments.get(title)).commit();
                    long d = System.nanoTime();
                    //ZenAppManager.moveDrawer(true);
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
                ZenLog.l("create new fragment");
                long p = System.nanoTime();

                String layoutName;
                String toCallClass;
                Integer layoutId;

                ZenLog.l("TIT: "+title);

                layoutName = ZenAppManager.getLayouts().get(title);
                // if layoutName is null fall back to title (because is the layout actually)
                if (layoutName == null) {
                    ZenLog.l("ZENFRAGMAN: title is null");
                    layoutName = title;
                }
                ZenLog.l("LAY NAME: "+layoutName);

                boolean isDetail = ZenAppManager.getDetailLayouts().containsKey(layoutName);
                ZenLog.l("ISDETAIL: "+isDetail);

                String[] tlist;
                tlist = layoutName.split("_");
                ZenLog.l(tlist.length);

                String cName = layoutName;
                if (tlist.length > 1) {
                    StringBuilder sBuilder = new StringBuilder();
                    sBuilder.append(tlist[0]);
                    for (int i=1; i<tlist.length; i++) {
                        String t = Character.toUpperCase(tlist[i].charAt(0)) + tlist[i].substring(1);
                        //tlist[i] = t;
                        sBuilder.append(t);
                    }
                    cName = sBuilder.toString();
                }
                //else {
                //    cName = layoutName;
                //}
                ZenLog.l("CNAME: "+cName);

                if (!isDetail) {
                    //toCallClass = ZenAppManager.getLayouts().get(title);
                    toCallClass = cName;
                    //layoutId = ZenAppManager.getLayouts().get(title);
                    layoutId = ZenResManager.getLayoutId(layoutName);
                    ZenLog.l("SET NOT DETAIL LAYOUTID " + layoutId);
                }
                else {
                        //toCallClass = ZenAppManager.getDetailLayouts().get(title);
                        //toCallClass = title + "Detail";
                        toCallClass = cName + "Detail";
                        ZenLog.l("LAYOUTCLASS " + toCallClass);
                        layoutId    = ZenResManager.getLayoutId(ZenAppManager.getDetailLayouts().get(layoutName));
                        ZenLog.l("SET DETAIL LAYOUTID " + layoutId);
                }

                //ZenLog.l("LAYOUTID " + layoutId);

                //TENTATIVO
                //int pos = ZenAppManager.getLayouts().get(title); //prima non esisteva.
                //TENTATIVO

                toCallClass = Character.toUpperCase(toCallClass.charAt(0)) + toCallClass.substring(1);
                Class toCall = Class.forName("app.Controllers."+toCallClass+"Controller");
                ZenLog.l("app.Controllers."+toCallClass+"Controller");
                try {



                    availableFragments.put(title, toCall.newInstance());
                    lastFragment = title;
                    Object controller = availableFragments.get(title);

                    String superclass = "io.thera.zen.layout.drawer.ZenFragment";

                    if (controller.getClass().getSuperclass().getCanonicalName().equals(superclass)) {
                        //
                        // QUESTO VUOL DIRE CHE ABBIAMO CARICATO UNA CLASSE CHE HA COME SUPERCLASSE ATLFRAGMENT
                        //
                        Class[] paramTypes 	= new Class[3]; //prima era new class[2]
                        paramTypes[0] 		= FragmentActivity.class;
                        paramTypes[1] 		= String.class;

                        paramTypes[2] 		= Integer.class;

                        //paramTypes[3] 		= DrawerLayout.class;
                        //paramTypes[4] 		= ListView.class;

                        //TENTATIVO
                        //Integer layoutId = ZenAppManager.getLayoutIds()[pos]; // prima era position
                        //= ZenAppManager.getLayouts().get(title);
                        //TENTATIVO
                        ZenLog.l("TRYING LAYOUTID " + layoutId);

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
                        FragmentManager fragmentManager = activity.getSupportFragmentManager();

                        //TEST
                        ZenNavigationManager.push(controller);
                        //TEST

                        FragmentTransaction transaction = fragmentManager.beginTransaction();

                        if (ZenNavigationManager.isBack()){

                            transaction.setCustomAnimations(ZenResManager.getAnimId("back_enter"), ZenResManager.getAnimId("back_exit"), ZenResManager.getAnimId("back_pop_enter") , ZenResManager.getAnimId("back_pop_exit"));

                        }
                        else {
                            transaction.setCustomAnimations(ZenResManager.getAnimId("enter"), ZenResManager.getAnimId("exit"), ZenResManager.getAnimId("pop_enter") , ZenResManager.getAnimId("pop_exit"));
                        }
                        transaction.replace(content_frame_id, (Fragment) controller ).commit();
                        ZenLog.l("SAVING " + title + " - " + controller.getClass().getCanonicalName());

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

		/*
		 * FINALLY WE CLOSE THE DRAWER.
		 */
        //ATLAppManager.closeDrawer();
    }

	/*
	 * METHOD FOR GETTING PARAMETERS FOR JAVA REFLECTION
	 */

    public static Object[] createParameters( FragmentActivity a , String title , int position) {
        Object[] parameters = new Object[3];
        parameters[0] = a;
        parameters[1] = title;
        parameters[2] = position;

        return parameters;
    }


}
