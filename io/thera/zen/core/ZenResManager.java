package io.thera.zen.core;

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

}
