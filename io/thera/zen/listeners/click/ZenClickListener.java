package io.thera.zen.listeners.click;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

public class ZenClickListener implements OnItemClickListener {


    int 	view_id;
    View 	view;
    String 	className;
    String 	methodName;
    Method 	callback;
    Object 	caller;
    String 	type; //so we know which type of event is handled.

    public ZenClickListener (View v, String methodName, String className, Object caller) {
        System.out.println("Initializing ATLItemClickListener");
        this.view 		= v;
        this.methodName = methodName;
        this.view_id 	= v.getId();
        this.className 	= className;
        this.caller	 	= caller;

        Class[] paramTypes = new Class[2];
        paramTypes[0] = View.class;
        paramTypes[1] = MotionEvent.class;

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

    public Object[] createParameters(View v, int position, long id) {

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
