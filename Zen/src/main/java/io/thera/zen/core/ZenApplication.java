package io.thera.zen.core;

import android.app.Application;
import android.content.Context;
import android.content.res.Resources;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;

import io.thera.zen.cache.ZenCache;

/**
 * Created by giovanni on 14/05/14.
 */
public class ZenApplication extends Application {

    public static volatile Context applicationContext = null;
    public static volatile Handler applicationHandler = null;
    private static volatile boolean applicationInited = false;
    private static volatile ZenSettingsManager applicationConfig = null;
    private static volatile ZenNavigationManager applicationNavigation = null;
    private static volatile ZenFragmentManager applicationFM = null;
    private static volatile ZenCache applicationCache = null;
    private static volatile FragmentActivity activity = null;
    public static volatile ZenLog log = null;

    @Override
    public void onCreate() {
        super.onCreate();

        applicationContext = getApplicationContext();
        applicationHandler = new Handler(applicationContext.getMainLooper());

    }

    public static void initApplication() {
        if (applicationInited) {
            return;
        }
        applicationInited = true;

        applicationConfig = new ZenSettingsManager();
        applicationFM = new ZenFragmentManager();
        applicationNavigation = new ZenNavigationManager(applicationConfig);

        applicationCache = new ZenCache();

        log = new ZenLog();
    }

    public static void registerActivity(FragmentActivity a) {
        activity = a;
    }

    public static void unregisterActivity() {
        activity = null;
    }

    public static FragmentActivity getAppActivity() {
        return activity;
    }

    public static Context context() {
        return applicationContext;
    }

    public static Resources resources() {
        return applicationContext.getResources();
    }

    public static ZenSettingsManager config() {
        return applicationConfig;
    }

    public static ZenNavigationManager navigation() {
        return applicationNavigation;
    }

    public static ZenFragmentManager fragments() {
        return applicationFM;
    }

    public static ZenCache cache() { return applicationCache; }

    public static String packageName() {
        return applicationContext.getPackageName();
    }

    public static void log(Object message) { log.d(message); }

}