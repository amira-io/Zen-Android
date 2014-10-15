package io.thera.zen.core;

/**
 * Created by marcostagni on 26/11/13.
 * Revisited by gi0baro on 14/05/14.
 *
 * Copyright © 2014. Thera Technologies.
 */

import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.Fragment;

import android.support.v4.app.FragmentTransaction;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;


public class ZenFragmentManager {

    private Map<String,Object> availableFragments;
    private String lastFragment;

    public ZenFragmentManager() {
        availableFragments = new HashMap<String,Object>();
    }

    public String getCurrent() {
        return lastFragment;
    }

    public Object getCurrentInstance() { return availableFragments.get(lastFragment); }

    public void load(String title) {
        load(title, true);
    }

    public void load(String title, boolean loadView) {

        //FragmentActivity activity = ZenAppManager.getActivity();
        FragmentActivity activity = ZenApplication.getAppActivity();

        ZenApplication.log("SETZENFRAGMENT " + title + " - " + activity.getClass().getCanonicalName());


        ZenApplication.log("your api version is ok :  GRAZIE A STO CAZZO.");
        /*
         * API LEVEL GREATER OR EQUAL TO HONEYCOMB.
         */

        ZenApplication.log("AVAILABLEFRAGMENTS ARE: "+availableFragments);
        if (availableFragments.containsKey(title)) {
            /*
             * IF WE HAVE ALREADY CREATED AN ATLFRAGMENT,
             * THEN IT'S INSIDE AVAILABLEFRAGMENTS.
             */
            try {
                    long p = System.nanoTime();
                    ZenApplication.log("Fragment "+title+" instance available, using it.");
                    int content_frame_id = ZenResManager.getResourceId("content_frame");

                    FragmentManager fragmentManager =  activity.getSupportFragmentManager();

                    //ZenNavigationManager.push(availableFragments.get(title));
                    ZenApplication.navigation().push(title, availableFragments.get(title).getClass().getSuperclass().getCanonicalName());
                    lastFragment = title;
                    //TEST

                    FragmentTransaction transaction = fragmentManager.beginTransaction();
                    //ANIMATIONS

                    if (ZenApplication.navigation().isBack()){
                        transaction.setCustomAnimations(ZenResManager.getAnimId("back_enter"),ZenResManager.getAnimId("back_exit"));
                        //transaction.setCustomAnimations(ZenResManager.getAnimId("back_enter"), ZenResManager.getAnimId("back_exit"), ZenResManager.getAnimId("back_pop_enter") , ZenResManager.getAnimId("back_pop_exit"));

                    }
                    else {
                        transaction.setCustomAnimations(ZenResManager.getAnimId("enter"),ZenResManager.getAnimId("exit"));
                        //transaction.setCustomAnimations(ZenResManager.getAnimId("enter"), ZenResManager.getAnimId("exit"), ZenResManager.getAnimId("pop_enter") , ZenResManager.getAnimId("pop_exit"));
                    }

                    transaction.replace(content_frame_id, (Fragment) availableFragments.get(title)).commitAllowingStateLoss();
                    long d = System.nanoTime();
                    //ZenAppManager.moveDrawer(true);
                    ZenApplication.log("TIME to recover old "+(d-p));


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
                ZenApplication.log("create new fragment");
                long p = System.nanoTime();

                String layoutName;
                String toCallClass;
                Integer layoutId;

                ZenApplication.log("TIT: "+title);

                //layoutName = ZenAppManager.getLayouts().get(title);
                layoutName = ZenApplication.config().getDrawer_menu_layouts().get(title);
                // if layoutName is null fall back to title (because is the layout actually)
                if (layoutName == null) {
                    ZenApplication.log("ZENFRAGMAN: title is null");
                    layoutName = title;
                }
                ZenApplication.log("LAY NAME: "+layoutName);

                //boolean isDetail = ZenAppManager.getDetailLayouts().containsKey(layoutName);
                //ZenApplication.log("ISDETAIL: "+isDetail);

                String[] tlist;
                tlist = layoutName.split("_");
                ZenApplication.log(tlist.length);

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
                ZenApplication.log("CNAME: "+cName);


                //if (!isDetail) {
                    //toCallClass = ZenAppManager.getLayouts().get(title);
                toCallClass = cName;
                //layoutId = ZenAppManager.getLayouts().get(title);
                layoutId = ZenResManager.getLayoutId(layoutName);
                ZenApplication.log("SET NOT DETAIL LAYOUTID " + layoutId);
                //}
                /*
                else {
                        //toCallClass = ZenAppManager.getDetailLayouts().get(title);
                        //toCallClass = title + "Detail";
                        toCallClass = cName + "Detail";
                        ZenApplication.log("LAYOUTCLASS " + toCallClass);
                        layoutId    = ZenResManager.getLayoutId(ZenAppManager.getDetailLayouts().get(layoutName));
                        ZenApplication.log("SET DETAIL LAYOUTID " + layoutId);
                }*/

                //ZenApplication.log("LAYOUTID " + layoutId);

                //TENTATIVO
                //int pos = ZenAppManager.getLayouts().get(title); //prima non esisteva.
                //TENTATIVO

                toCallClass = Character.toUpperCase(toCallClass.charAt(0)) + toCallClass.substring(1);
                Class toCall = Class.forName("app.Controllers."+toCallClass+"Controller");
                ZenApplication.log("app.Controllers."+toCallClass+"Controller");
                try {



                    availableFragments.put(title, toCall.newInstance());
                    lastFragment = title;
                    Object controller = availableFragments.get(title);

                    String superclass = "io.thera.zen.layout.drawer.ZenFragment";
                    if (ZenFragment.class.isAssignableFrom(controller.getClass())) {
                    //if (controller.getClass().getSuperclass().getCanonicalName().equals(superclass)) {
                        //
                        // QUESTO VUOL DIRE CHE ABBIAMO CARICATO UNA CLASSE CHE HA COME SUPERCLASSE ATLFRAGMENT
                        //
                        Class[] paramTypes 	= new Class[2]; //prima era new class[2]
                        paramTypes[0] 		= String.class;
                        paramTypes[1] 		= Integer.class;

                        //paramTypes[3] 		= DrawerLayout.class;
                        //paramTypes[4] 		= ListView.class;

                        //TENTATIVO
                        //Integer layoutId = ZenAppManager.getLayoutIds()[pos]; // prima era position
                        //= ZenAppManager.getLayouts().get(title);
                        //TENTATIVO
                        ZenApplication.log("TRYING LAYOUTID " + layoutId);

                        toCall.getMethod("setVariables", paramTypes).invoke(controller, createParameters(title, layoutId ));

                        int content_frame_id = ZenResManager.getResourceId("content_frame");
                        FragmentManager fragmentManager = activity.getSupportFragmentManager();

                        //TEST
                        //ZenNavigationManager.push(controller);
                        ZenApplication.navigation().push(title, controller.getClass().getSuperclass().getCanonicalName());
                        //TEST

                        if (loadView) {

                            FragmentTransaction transaction = fragmentManager.beginTransaction();
                            //ANIMATION

                            if (ZenApplication.navigation().isBack()){

                                transaction.setCustomAnimations(ZenResManager.getAnimId("back_enter"),ZenResManager.getAnimId("back_exit"));
                                //transaction.setCustomAnimations(ZenResManager.getAnimId("back_enter"), ZenResManager.getAnimId("back_exit"), ZenResManager.getAnimId("back_pop_enter") , ZenResManager.getAnimId("back_pop_exit"));

                            }
                            else {
                                transaction.setCustomAnimations(ZenResManager.getAnimId("enter"),ZenResManager.getAnimId("exit"));

                                //transaction.setCustomAnimations(ZenResManager.getAnimId("enter"), ZenResManager.getAnimId("exit"), ZenResManager.getAnimId("pop_enter") , ZenResManager.getAnimId("pop_exit"));
                            }

                            transaction.replace(content_frame_id, (Fragment) controller ).commitAllowingStateLoss();

                        }
                        ZenApplication.log("SAVING " + title + " - " + controller.getClass().getCanonicalName());

                        long d = System.nanoTime();
                        ZenApplication.log("TIME to create new fragment " + (d-p));

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

    public Object[] createParameters(String title , int position) {
        Object[] parameters = new Object[2];
        parameters[0] = title;
        parameters[1] = position;

        return parameters;
    }


}
