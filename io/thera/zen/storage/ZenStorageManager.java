package io.thera.zen.storage;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import io.thera.zen.core.ZenAppManager;
import io.thera.zen.core.ZenLog;
import io.thera.zen.core.ZenSettingsManager;

/**
 * Created by marcostagni on 04/12/13.
 */
public class ZenStorageManager {


    public static synchronized void write (String key, Object value) {
        Context c = ZenAppManager.getActivity().getApplicationContext();
        SharedPreferences pref =  c.getSharedPreferences(ZenSettingsManager.getStorageFile() , Context.MODE_PRIVATE);

        SharedPreferences.Editor prefEditor = pref.edit();
        String tosave = value.getClass().getCanonicalName() + "," + value.toString();
        ZenLog.l("SAVED STRING : " + tosave);
        prefEditor.putString(key, tosave);
        //fottiti
        prefEditor.commit();
    }

    public static synchronized Object read (String key) {

        Context c = ZenAppManager.getActivity().getApplicationContext();
        SharedPreferences pref =  c.getSharedPreferences(ZenSettingsManager.getStorageFile() , Context.MODE_PRIVATE);

        try {
            String res = pref.getString(key,"default");

            String classtype = res.split(",")[0];
            String content   = res.split(",")[1];

            Object o = Class.forName(classtype).cast(content);
            ZenLog.l("READ STRING " + o.toString() + " - CLASS TYPE" + classtype);
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
