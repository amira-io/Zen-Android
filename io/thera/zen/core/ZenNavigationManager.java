package io.thera.zen.core;

import java.lang.reflect.InvocationTargetException;
import java.util.Stack;

import io.thera.zen.layout.drawer.ZenFragment;
import io.thera.zen.layout.drawer.ZenFragmentManager;

/**
 * Created by marcostagni on 04/12/13.
 */
public class ZenNavigationManager  {

    private static Stack<Object> navigationStack = new Stack();

    private static Object current;


    public static synchronized void push ( Object object) {
        try {
            ZenLog.l("PUSH" + (String) object.getClass().getMethod("getTitle",null).invoke(object,null));
            ZenLog.l("BEFORE PUSH" + navigationStack.size());
            navigationStack.push(object);
            ZenLog.l("AFTER PUSH" + navigationStack.size());
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
                Object a  = navigationStack.pop();
                ZenLog.l("POP" + (String) a.getClass().getMethod("getTitle",null).invoke(a,null));

                ZenLog.l("STACK POP" + navigationStack);


                Object o  = navigationStack.pop();
                ZenLog.l("POP" + (String) o.getClass().getMethod("getTitle",null).invoke(o,null));

                ZenLog.l("STACK POP" + navigationStack);


                if (!(o==null)) {
                    ZenLog.l("NOTEMPTY");
                    if (o.getClass().getSuperclass().getCanonicalName().equals("io.thera.zen.layout.drawer.ZenFragment")) {
                        ZenLog.l("ISFRAGMENT");
                        String title = (String) o.getClass().getMethod("getTitle",null).invoke(o,null);
                        ZenLog.l("FRAGMENT TITLE: " + title);
                        ZenFragmentManager.setZenFragment(title, ZenAppManager.getActivity());
                    }
                    if (o.getClass().getSuperclass().getCanonicalName().equals("io.thera.zen.layout.elements.ZenActivity")) {
                        ZenLog.l("ISACTIVITY");
                        Class[] params = new Class[1];
                        params[0] = Class.class;

                        Object[] values = new Object[1];
                        values[0] = o;
                        ZenAppManager.getActivity().getClass().getMethod("goTo",params).invoke(ZenAppManager.getActivity(),values);

                    }
                }
            }
            else {
                ZenLog.l("sono vuoto cazzo");
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
