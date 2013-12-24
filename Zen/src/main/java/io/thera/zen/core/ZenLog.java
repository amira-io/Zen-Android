package io.thera.zen.core;

/**
 * Created by marcostagni on 26/11/13.
 *
 * Copyright © 2013. Thera Technologies.
 */
public class ZenLog {

    /*
        TESTING FLAG
     */

    private static boolean isTesting = true;

    public static synchronized void setTesting (boolean value) {
        isTesting = value;
    }

    public static void l (Object message) {
        if (isTesting) {
            System.out.println(message);
        }
    }
}
