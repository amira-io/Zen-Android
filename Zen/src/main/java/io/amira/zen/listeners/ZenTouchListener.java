/*
 * ZenFramework for Android
 *
 * :copyright: (c) 2013-2016 by Marco Stagni, Giovanni Barillari
 * :license: GPLv3, see LICENSE for more details.
 */

package io.amira.zen.listeners;

import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import java.lang.reflect.*;

import io.amira.zen.core.ZenApplication;

public class ZenTouchListener implements OnTouchListener {

    int 	view_id;
    View 	view;
    String 	className;
    String 	methodName;
    Method 	callback;
    Object 	caller;
    String 	type; //so we know which type of event is handled.

    public ZenTouchListener (View v, String methodName) {
        this.view 		= v;
        this.view_id 	= v.getId();
        this.caller	 	= ZenApplication.getAppActivity();

        Class[] paramTypes = new Class[2];
        paramTypes[0] = View.class;
        paramTypes[1] = MotionEvent.class;

        try {
            this.callback = Class.forName(caller.getClass().getCanonicalName()).getMethod(methodName, paramTypes);
        } catch (NoSuchMethodException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public ZenTouchListener() {
        //empty constructor
    }

    public Object[] createParameters(View v, MotionEvent event) {

        Object[] parameters = new Object[2];
        parameters[0] = v;
        parameters[1] = event;
        return parameters;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        // TODO Auto-generated method stub
        // se l'evento  avvenuto sulla view a cui facciamo riferimento.
        if (v.getId() == view_id) {
            try {
                this.callback.invoke(caller, createParameters(v, event));
                return true;
            } catch (IllegalArgumentException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return false;
            } catch (IllegalAccessException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return false;
            } catch (InvocationTargetException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return false;
            }
            catch (NullPointerException e) {
                e.printStackTrace();
                return false;
            }
        }
        return true;
    }

}
