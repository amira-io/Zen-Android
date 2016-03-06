/*
 * ZenFramework for Android
 *
 * :copyright: (c) 2013-2016 by Marco Stagni, Giovanni Barillari
 * :license: GPLv3, see LICENSE for more details.
 */

package io.amira.zen.core;

import java.lang.reflect.InvocationTargetException;
import java.util.Stack;
import java.util.*;

public class ZenNavigationManager  {

    private List<Object> parameters;
    private List<Object> parametersLast;
    private Stack<String[]> navigationStack;
    private String first;
    private String previous;
    private boolean goingBack;


    public ZenNavigationManager(ZenSettingsManager settings) {
        parameters = new ArrayList<Object>();
        parametersLast = new ArrayList<Object>();
        navigationStack = new Stack<String[]>();
        first = settings.getFirstView();
        previous = settings.getFirstView();
        goingBack = false;
    }

    /*
        THIS WILL HELP US HANDLING PARAMETERS BETWEEN VIEWS.

        List<Object> parameters = new ArrayList<Object>();

        AFTER GETTING PARAMETERS, THESE ARE DESTROYED.
     */



    public void setParameters( List<Object> params) {
        parameters = params;
    }

    public boolean isBack() {
        return goingBack;
    }

    public List<Object> getParameters () {
        List<Object> copy =  new ArrayList<Object>(parameters);
        parametersLast = new ArrayList<Object>(parameters);
        //copy = parameters;
        parameters.clear();

        if (copy.isEmpty()) {
            return null;
        }

        return copy;
    }

    public ArrayList<String> currentParameters() {
        List<Object> p = parametersLast;
        ArrayList<String> s = new ArrayList<String>();
        for (int i=0; i<p.size(); i++) {
            s.add((String) p.get(i));
        }
        return s;
    }

    /*
           STACK FOR BACK NAVIGATION
     */

    //private static Stack<Object> navigationStack = new Stack();



    public void push (String title, String cls) {
        try {
            //ZenApplication.log("PUSH" + (String) object.getClass().getMethod("getTitle",null).invoke(object,null));
            //ZenApplication.log("BEFORE PUSH" + navigationStack.size());
            ZenApplication.log("PUSH" + title);

            if (navigationStack.isEmpty()) {
                //previous = (String) object.getClass().getMethod("getTitle",null).invoke(object,null);
                previous = title;
                String[] s = {title, cls};
                ZenApplication.log(s);
                navigationStack.push(s);
                return;
            }


            if ((!( title.equals(previous)))) {

                /**
                 *  L'OGGETTO VIENE INSERITO NELLO STACK SOLO SE NON è UGUALE A QUELLO PRECEDENTE.
                 *  ESEMPIO: POTREI AVER CLICCATO COME UN EBETE SULLA STESSA VOCE DEL MENU
                 */
                //String toPut = (String) object.getClass().getMethod("getTitle",null).invoke(object,null);
                if (title.equals(first)) {
                    navigationStack.removeAllElements();
                }
                //previous = (String) object.getClass().getMethod("getTitle",null).invoke(object,null);
                previous = title;
                String[] s = {title, cls};
                ZenApplication.log(s);
                navigationStack.push(s);

            }
            //ZenApplication.log("AFTER PUSH" + navigationStack.size());
        }
        catch (Exception e) {
            e.printStackTrace();
        }


    }

    public void back() {

        try {
            //if (!(navigationStack.empty())) {
            if (navigationStack.size()>=2) {
                ZenApplication.log("STACK POP" + navigationStack);
                //Object a  = navigationStack.pop();
                //ZenApplication.log("POP" + (String) a.getClass().getMethod("getTitle",null).invoke(a,null));
                String[] a = navigationStack.pop();

                ZenApplication.log("STACK POP" + navigationStack);


                //Object o  = navigationStack.pop();
                //ZenApplication.log("POP" + (String) o.getClass().getMethod("getTitle",null).invoke(o,null));
                String[] o = navigationStack.pop();
                ZenApplication.log(o);

                ZenApplication.log("STACK POP" + navigationStack);


                if (!(o==null)) {
                    ZenApplication.log("NOTEMPTY");
                    /*
                    if (o.getClass().getSuperclass().getCanonicalName().equals("io.amira.zen.layout.drawer.ZenFragment")) {
                        ZenApplication.log("ISFRAGMENT");
                        String title = (String) o.getClass().getMethod("getTitle",null).invoke(o,null);
                        ZenApplication.log("FRAGMENT TITLE: " + title);
                        isBack = true;
                        ZenFragmentManager.setZenFragment(title, ZenAppManager.getActivity(),false);
                        isBack = false;
                    }
                    if (o.getClass().getSuperclass().getCanonicalName().equals("io.amira.zen.layout.drawer.ZenDetailFragment")) {
                        ZenApplication.log("ISFRAGMENT");
                        String title = (String) o.getClass().getMethod("getTitle",null).invoke(o,null);
                        ZenApplication.log("FRAGMENT TITLE: " + title);
                        isBack = true;
                        ZenFragmentManager.setZenFragment(title, ZenAppManager.getActivity(),true);
                        isBack = false;
                    }
                    */
                    //if (o.getClass().getSuperclass().getCanonicalName().equals("io.amira.zen.layout.elements.ZenActivity")) {
                    if (o[1].equals("io.amira.zen.layout.elements.ZenActivity")) {
                        ZenApplication.log("ISACTIVITY");
                        Class[] params = new Class[1];
                        params[0] = Class.class;

                        Object[] values = new Object[1];
                        values[0] = o[0];
                        ZenApplication.getAppActivity().getClass().getMethod("goTo",params).invoke(ZenApplication.getAppActivity(),values);
                    }
                    else {
                        ZenApplication.log("ISFRAGMENT");
                        //String title = (String) o.getClass().getMethod("getTitle",null).invoke(o,null);
                        ZenApplication.log("FRAGMENT TITLE: " + o[0]);
                        goingBack = true;
                        ZenApplication.fragments().load(o[0]);
                        goingBack = false;
                    }
                }
            }

            else {

                ZenApplication.getAppActivity().finish();

            }

        }
        catch (NoSuchMethodException e ) {
            e.printStackTrace();
        }
        catch (IllegalAccessException e ) {
            e.printStackTrace();
        }
        catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}
