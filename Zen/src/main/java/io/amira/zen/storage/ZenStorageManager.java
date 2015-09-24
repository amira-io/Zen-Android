package io.amira.zen.storage;

import android.content.Context;
import android.content.SharedPreferences;

import io.amira.zen.core.ZenApplication;

/**
 * Created by marcostagni on 04/12/13.
 */
public class ZenStorageManager {


    public static synchronized void write (String key, Object value) {
        Context c = ZenApplication.context();
        SharedPreferences pref =  c.getSharedPreferences(ZenApplication.config().getStorageFile() , Context.MODE_PRIVATE);

        SharedPreferences.Editor prefEditor = pref.edit();
        String tosave = value.getClass().getCanonicalName() + "," + value.toString();
        ZenApplication.log("SAVED STRING : " + tosave);
        prefEditor.putString(key, tosave);
        //fottiti
        prefEditor.commit();
    }

    public static synchronized Object read (String key) {

        Context c = ZenApplication.context();
        SharedPreferences pref =  c.getSharedPreferences(ZenApplication.config().getStorageFile() , Context.MODE_PRIVATE);

        try {
            String res = pref.getString(key,"default");

            String classtype = res.split(",")[0];
            String content   = res.split(",")[1];

            Object o = Class.forName(classtype).cast(content);
            ZenApplication.log("READ STRING " + o.toString() + " - CLASS TYPE" + classtype);
            return o;
        } /*catch (InstantiationException e) {
            e.printStackTrace();
            return null;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return null;
        } */catch (ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }

    }
}
