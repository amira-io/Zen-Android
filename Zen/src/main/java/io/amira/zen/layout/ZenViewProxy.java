/*
 * ZenFramework for Android
 *
 * :copyright: (c) 2013-2016 by Marco Stagni, Giovanni Barillari
 * :license: GPLv3, see LICENSE for more details.
 */

package io.amira.zen.layout;

import android.support.v4.app.FragmentActivity;
import android.view.View;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.amira.zen.core.ZenApplication;

public class ZenViewProxy {
    private int view_id;
    private FragmentActivity activity;
    private View root;
    private View view;
    private Class view_class;
    private Map<Class, Class> class_convert;

    public ZenViewProxy(int id, View rootView) {
        view_id = id;
        root = rootView;
        view = null;
    }

    public ZenViewProxy(int id, FragmentActivity a) {
        view_id = id;
        activity = a;
        root = null;
        view = null;
    }

    private void _fill_classes() {
        class_convert = new HashMap<Class, Class>();
        class_convert.put(Integer.class, Integer.TYPE);
        class_convert.put(Float.class, Float.TYPE);
        class_convert.put(Double.class, Double.TYPE);
        class_convert.put(Boolean.class, Boolean.TYPE);
    }

    private void _load_view() {
        if (view != null) {
            return;
        }
        if (root != null) {
            view = root.findViewById(view_id);
        } else {
            view = activity.findViewById(view_id);
        }
        try {
            view_class = view.getClass();
        } catch (Exception e) {
            view_class = null;
        }
        _fill_classes();
    }

    private Object _invoke(String method, Object[] args, Class[] types) {
        Object rv = null;
        Method[] allMethods = view_class.getMethods();
        List<Method> foundMethods = new ArrayList<Method>();
        Method theMethod = null;
        // we cycle class methods to find compatibles ones
        for (Method m : allMethods) {
            // if name is different, skip
            if (!m.getName().equals(method)) {
                continue;
            }
            Class[] mTypes = m.getParameterTypes();
            // if the numbers of args is different skip
            if (mTypes.length != args.length) {
                continue;
            }
            // if the args types are the same, we've done
            if (mTypes == types) {
                theMethod = m;
                break;
            }
            // we try to map args with compatibles ones
            boolean compatible = true;
            for (int i=0; i<mTypes.length; i++) {
                if (!mTypes[i].isAssignableFrom(types[i])) {
                    compatible = false;
                    break;
                }
            }
            // if we mapped args successfully, add to compatibles methods
            if (compatible) {
                foundMethods.add(m);
            }
        }
        if (theMethod != null) {
            // we have the method with same args types, invoke it
            try {
                rv = theMethod.invoke(view_class.cast(view), args);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            // we first try to invoke method with current types, maybe we are lucky
            try {
                rv = view_class.getMethod(method, types).invoke(view_class.cast(view), args);
            } catch (Exception e) {
                // if we came here, we try to change our args types with the method required ones
                boolean succeded = false;
                for (Method m : foundMethods) {
                    try {
                        rv = m.invoke(view_class.cast(view), args);
                        succeded = true;
                        break;
                    } catch (Exception e2) {
                        // continue the cycle to next method
                    }
                }
                if (!succeded) {
                    ZenApplication.log.e(method+" invoke failed for view "+view_id+" of type "+view_class.getName());
                }
            }
        }
        return rv;
    }

    public Object go(String method, Object... args) {
        _load_view();
        if (!exists()) return null;
        Class[] args_types = new Class[args.length];
        int c = 0;
        for (Object arg: args) {
            Class arg_type = arg.getClass();
            if (class_convert.containsKey(arg_type)) {
                arg_type = class_convert.get(arg_type);
            }
            args_types[c] = arg_type;
            c++;
        }
        return _invoke(method, args, args_types);
    }

    public boolean exists() {
        _load_view();
        return !(view_class == null);
    }

    public Object ret(String method, Object... args) {
        return null;
    }
}
