package io.thera.zen.listeners.click;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import io.thera.zen.core.ZenAppManager;

public class ZenClickListener implements OnItemClickListener {


    private int 	view_id;
    private View 	view;
    private String 	className;
    private String 	methodName;
    private Method 	callback;
    private Object 	caller;
    private String 	type; //so we know which type of event is handled.

    public ZenClickListener (View v, String methodName) {
        System.out.println("Initializing ATLItemClickListener");


        this.view 		= v;
        this.methodName = methodName;
        this.view_id 	= v.getId();
        this.className 	= ZenAppManager.getCurrentPosition().getClass().getCanonicalName();
        this.caller	 	= ZenAppManager.getCurrentPosition();

        Class[] paramTypes = new Class[3];
        paramTypes[0] = View.class;
        paramTypes[1] = Integer.class;
        paramTypes[2] = Long.class;
        //paramTypes[1] = MotionEvent.class;

        try {
            System.out.println("Trying to load "+ className);
            this.callback = Class.forName(className).getMethod(methodName, paramTypes);
        } catch (NoSuchMethodException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public ZenClickListener() {
        //empty constructor
    }

    private Object[] createParameters(View v, int position, long id) {

        Object[] parameters = new Object[3];
        parameters[0] = v;
        parameters[1] = position;
        parameters[2] = id;
        return parameters;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View v, int position,
                            long id) {
        // TODO Auto-generated method stub
        // se l'evento  avvenuto sulla view a cui facciamo riferimento.
        if (v.getId() == view_id) {
            try {
                this.callback.invoke(caller, createParameters(v, position,id));
            } catch (IllegalArgumentException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            catch (NullPointerException e) {
                e.printStackTrace();
            }
        }
    }




}
