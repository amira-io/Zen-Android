package io.thera.zen.core;

import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by marcostagni on 26/11/13
 *
 * Copyright © 2013. Thera Technologies.
 */

public class ZenResManager {

	/*
	 * METHOD FOR GETTING RESOURCE ID
	 */

    public static synchronized int getResourceId(String resource) {
        try {
            //set di classi dichiarate da R (layout, id, array, ecc ecc)
            Class set[] = Class.forName(ZenAppManager.getResourceClass()).getDeclaredClasses();
            //cerco la classe id
            for (int i = 0; i < set.length ; i++) {

                if (set[i].getCanonicalName().endsWith("id")) {
                    //recupero il field della classe che corrisponde alla stringa in ingresso alla funzione
                    //viene ritornato come intero. La struttura della chiamata è colpa di Java Reflection.
                    return (Integer) set[i].getField(resource).get(Class.forName(ZenAppManager.getResourceClass()));
                }
            }

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return 0;

        } catch (NoSuchFieldException e) {
            e.printStackTrace();
            return 0;

        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return 0;
        }

        return 0;



    }



	/*
	 * METHOD FOR GETTING LAYOUT ID
	 */

    public static synchronized int getLayoutId(String layout) {
        try {
            Class set[] = Class.forName(ZenAppManager.getResourceClass()).getDeclaredClasses();

            for (int i = 0; i < set.length ; i++) {
                if (set[i].getCanonicalName().endsWith("layout")) {
                    return (Integer) set[i].getField(layout).get(Class.forName(ZenAppManager.getResourceClass()));
                }
            }

        } catch (ClassNotFoundException e) {
            //e.printStackTrace();
            ZenLog.l("ZenResManager.getLayoutId: ClassNotFound "+layout);
            return 0;

        } catch (NoSuchFieldException e) {
            //e.printStackTrace();
            ZenLog.l("ZenResManager.getLayoutId: NoSuchField "+layout);
            return 0;

        } catch (IllegalAccessException e) {
            //e.printStackTrace();
            ZenLog.l("ZenResManager.getLayoutId: IllegalAccess "+layout);
            return 0;
        }

        return 0;



    }

	/*
	 * METHOD FOR GETTING RESOURCE ARRAY
	 */

    public static synchronized Integer getArrayId (String array) {
        try {
            Class set[] = Class.forName(ZenAppManager.getResourceClass()).getDeclaredClasses();

            for (int i = 0; i < set.length ; i++) {
                if (set[i].getCanonicalName().endsWith("array")) {
                    return (Integer) set[i].getField(array).get(Class.forName(ZenAppManager.getResourceClass()));
                }
            }

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return null;

        } catch (NoSuchFieldException e) {
            e.printStackTrace();
            return null;

        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return null;
        }

        return null;



    }

    /**
     * METHOD FOR GETTING ANIMATION ID
     *
     * @param array
     * @return
     */
    public static synchronized Integer getAnimId (String array) {
        try {
            Class set[] = Class.forName(ZenAppManager.getResourceClass()).getDeclaredClasses();

            for (int i = 0; i < set.length ; i++) {
                if (set[i].getCanonicalName().endsWith("anim")) {
                    return (Integer) set[i].getField(array).get(Class.forName(ZenAppManager.getResourceClass()));
                }
            }

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return null;

        } catch (NoSuchFieldException e) {
            e.printStackTrace();
            return null;

        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return null;
        }

        return null;



    }

    public static synchronized Animation getAnimation( String name ) {

        try {
            Animation a;
            a = AnimationUtils.loadAnimation(ZenAppManager.getActivity().getApplicationContext(), getAnimId(name));
            return a;
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * METHOD FOR GETTING DRAWABLE ID
     * @param drawable
     * @return
     */

    public static synchronized Integer getDrawableId ( String drawable ) {
        try {
            Class set[] = Class.forName(ZenAppManager.getResourceClass()).getDeclaredClasses();

            for (int i = 0; i < set.length ; i++) {
                if (set[i].getCanonicalName().endsWith("drawable")) {
                    return (Integer) set[i].getField(drawable).get(Class.forName(ZenAppManager.getResourceClass()));
                }
            }

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return null;

        } catch (NoSuchFieldException e) {
            e.printStackTrace();
            return null;

        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return null;
        }

        return null;



    }

    /**
     * METHOD FOR GETTING DRAWABLE FROM ID.
     * @param name
     * @return
     */

    public static synchronized Drawable getDrawable ( String name ) {

        try {
            Drawable d =  ZenAppManager.getActivity().getApplicationContext().getResources().getDrawable(getDrawableId(name));
            return d;
        }
        catch(Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    public static synchronized int getDimenId(String resource) {
        try {
            //set di classi dichiarate da R (layout, id, array, ecc ecc)
            Class set[] = Class.forName(ZenAppManager.getResourceClass()).getDeclaredClasses();
            //cerco la classe id
            for (int i = 0; i < set.length ; i++) {

                if (set[i].getCanonicalName().endsWith("dimen")) {
                    //recupero il field della classe che corrisponde alla stringa in ingresso alla funzione
                    //viene ritornato come intero. La struttura della chiamata è colpa di Java Reflection.
                    return (Integer) set[i].getField(resource).get(Class.forName(ZenAppManager.getResourceClass()));
                }
            }

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return 0;

        } catch (NoSuchFieldException e) {
            e.printStackTrace();
            return 0;

        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return 0;
        }

        return 0;



    }

    static Map<String, Typeface> tfs = new HashMap<String, Typeface>();

    public static synchronized Typeface getTypeface(String font, Context c) {
        String fontName = ZenSettingsManager.getFont(font);
        //ZenLog.l(fontName);
        if (!tfs.containsKey(fontName)) {
            try {
                ZenLog.l("Creating typeface for font "+fontName);
                tfs.put(fontName, Typeface.createFromAsset(c.getAssets(), "fonts/"+fontName));
            }
            catch (Exception e) {
                ZenLog.l("Could not get typeface '" + fontName
                        + "' because " + e.getMessage());
                return null;
            }
        }
        return tfs.get(fontName);
    }

}
