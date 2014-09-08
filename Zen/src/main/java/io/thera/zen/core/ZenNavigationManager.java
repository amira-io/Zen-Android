package io.thera.zen.core;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;
import java.util.*;

import io.thera.zen.layout.drawer.ZenFragment;
import io.thera.zen.layout.drawer.ZenFragmentManager;

/**
 * Created by marcostagni on 04/12/13.
 */
public class ZenNavigationManager  {

    /*
        THIS WILL HELP US HANDLING PARAMETERS BETWEEN VIEWS.

        List<Object> parameters = new ArrayList<Object>();

        AFTER GETTING PARAMETERS, THESE ARE DESTROYED.
     */

    //static Map<String,Map<Class,Object>> parameters = new HashMap<String, Map<Class, Object>>();

    private static List<Object> parameters = new ArrayList<Object>();

    private static List<Object> parametersLast = new ArrayList<Object>();

    public static synchronized  void setParameters( List<Object> params) {
        parameters = params;
    }

    public static boolean isBack  = false;

    public static synchronized boolean isBack() {
        return isBack;
    }

    public static synchronized List<Object> getParameters () {
        List<Object> copy =  new ArrayList<Object>(parameters);
        parametersLast = new ArrayList<Object>(parameters);
        //copy = parameters;
        parameters.clear();

        if (copy.isEmpty()) {
            return null;
        }

        return copy;
    }

    public static synchronized ArrayList<String> currentParameters() {
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
    private static Stack<String[]> navigationStack = new Stack<String[]>();

    private static String previous = ZenSettingsManager.getFirstView(); //to store previous position


    public static synchronized void push (String title, String cls) {
        try {
            //ZenLog.l("PUSH" + (String) object.getClass().getMethod("getTitle",null).invoke(object,null));
            //ZenLog.l("BEFORE PUSH" + navigationStack.size());
            ZenLog.l("PUSH" + title);

            if (navigationStack.isEmpty()) {
                //previous = (String) object.getClass().getMethod("getTitle",null).invoke(object,null);
                previous = title;
                String[] s = {title, cls};
                ZenLog.l(s);
                navigationStack.push(s);
                return;
            }


            if ((!( title.equals(previous)))) {

                /**
                 *  L'OGGETTO VIENE INSERITO NELLO STACK SOLO SE NON è UGUALE A QUELLO PRECEDENTE.
                 *  ESEMPIO: POTREI AVER CLICCATO COME UN EBETE SULLA STESSA VOCE DEL MENU
                 */
                //String toPut = (String) object.getClass().getMethod("getTitle",null).invoke(object,null);
                if (title.equals(ZenSettingsManager.getFirstView())) {
                    navigationStack.removeAllElements();
                }
                //previous = (String) object.getClass().getMethod("getTitle",null).invoke(object,null);
                previous = title;
                String[] s = {title, cls};
                ZenLog.l(s);
                navigationStack.push(s);

            }
            //ZenLog.l("AFTER PUSH" + navigationStack.size());
        }
        catch (Exception e) {
            e.printStackTrace();
        }


    }

    public static synchronized void back() {

        try {
            //if (!(navigationStack.empty())) {
            if (navigationStack.size()>=2) {
                ZenLog.l("STACK POP" + navigationStack);
                //Object a  = navigationStack.pop();
                //ZenLog.l("POP" + (String) a.getClass().getMethod("getTitle",null).invoke(a,null));
                String[] a = navigationStack.pop();

                ZenLog.l("STACK POP" + navigationStack);


                //Object o  = navigationStack.pop();
                //ZenLog.l("POP" + (String) o.getClass().getMethod("getTitle",null).invoke(o,null));
                String[] o = navigationStack.pop();
                ZenLog.l(o);

                ZenLog.l("STACK POP" + navigationStack);


                if (!(o==null)) {
                    ZenLog.l("NOTEMPTY");
                    /*
                    if (o.getClass().getSuperclass().getCanonicalName().equals("io.thera.zen.layout.drawer.ZenFragment")) {
                        ZenLog.l("ISFRAGMENT");
                        String title = (String) o.getClass().getMethod("getTitle",null).invoke(o,null);
                        ZenLog.l("FRAGMENT TITLE: " + title);
                        isBack = true;
                        ZenFragmentManager.setZenFragment(title, ZenAppManager.getActivity(),false);
                        isBack = false;
                    }
                    if (o.getClass().getSuperclass().getCanonicalName().equals("io.thera.zen.layout.drawer.ZenDetailFragment")) {
                        ZenLog.l("ISFRAGMENT");
                        String title = (String) o.getClass().getMethod("getTitle",null).invoke(o,null);
                        ZenLog.l("FRAGMENT TITLE: " + title);
                        isBack = true;
                        ZenFragmentManager.setZenFragment(title, ZenAppManager.getActivity(),true);
                        isBack = false;
                    }
                    */
                    //if (o.getClass().getSuperclass().getCanonicalName().equals("io.thera.zen.layout.elements.ZenActivity")) {
                    if (o[1].equals("io.thera.zen.layout.elements.ZenActivity")) {
                        ZenLog.l("ISACTIVITY");
                        Class[] params = new Class[1];
                        params[0] = Class.class;

                        Object[] values = new Object[1];
                        values[0] = o[0];
                        ZenAppManager.getActivity().getClass().getMethod("goTo",params).invoke(ZenAppManager.getActivity(),values);
                    }
                    else {
                        ZenLog.l("ISFRAGMENT");
                        //String title = (String) o.getClass().getMethod("getTitle",null).invoke(o,null);
                        ZenLog.l("FRAGMENT TITLE: " + o[0]);
                        isBack = true;
                        ZenFragmentManager.setZenFragment(o[0]);
                        isBack = false;
                    }
                }
            }

            else {

                ZenAppManager.getActivity().finish();

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
