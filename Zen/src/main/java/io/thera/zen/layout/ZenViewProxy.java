package io.thera.zen.layout;

import android.view.View;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.thera.zen.core.ZenApplication;

/**
 * Created by giovanni on 15/10/14.
 */
public class ZenViewProxy {
    private int view_id;
    private View root;
    private View view;
    private Class view_class;
    private Map<Class, Class> class_convert;

    public ZenViewProxy(int id, View rootView) {
        view_id = id;
        root = rootView;
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
        view = root.findViewById(view_id);
        view_class = view.getClass();
        _fill_classes();
    }

    private void _invoke(String method, Object[] args, Class[] types) {
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
                theMethod.invoke(view_class.cast(view), args);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            // we first try to invoke method with current types, maybe we are lucky
            try {
                view_class.getMethod(method, types).invoke(view_class.cast(view), args);
            } catch (Exception e) {
                // if we came here, we try to change our args types with the method required ones
                boolean succeded = false;
                for (Method m : foundMethods) {
                    try {
                        m.invoke(view_class.cast(view), args);
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
    }

    public void go(String method, Object... args) {
        _load_view();
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
        _invoke(method, args, args_types);
    }

    public Object ret(String method, Object... args) {
        return null;
    }
}
