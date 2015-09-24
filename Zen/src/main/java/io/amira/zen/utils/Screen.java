package io.amira.zen.utils;

import android.content.Context;
import android.content.res.Configuration;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import io.amira.zen.core.ZenApplication;

/**
 * Created by giovanni on 14/05/14.
 */
public class Screen {

    public static boolean isTablet() {
        if (android.os.Build.VERSION.SDK_INT >= 11) { // honeycomb
            boolean device_large = ((ZenApplication.resources().getConfiguration().screenLayout &
                    Configuration.SCREENLAYOUT_SIZE_MASK) ==
                    Configuration.SCREENLAYOUT_SIZE_XLARGE);
            ZenApplication.log("TABLET? " + ZenApplication.resources().getConfiguration().screenLayout + " - " + Configuration.SCREENLAYOUT_SIZE_MASK + " - " + Configuration.SCREENLAYOUT_SIZE_XLARGE + " - " + Configuration.SCREENLAYOUT_SIZE_LARGE + " - " + (ZenApplication.resources().getConfiguration().screenLayout &
                    Configuration.SCREENLAYOUT_SIZE_MASK) );
            if (device_large) {
                ZenApplication.log("TABLET? large screen");
                DisplayMetrics metrics = new DisplayMetrics();
                //Activity activity = (Activity) activityContext;
                ((WindowManager) ZenApplication.context().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getMetrics(metrics);

                if (metrics.densityDpi == DisplayMetrics.DENSITY_DEFAULT
                        || metrics.densityDpi == DisplayMetrics.DENSITY_HIGH
                        || metrics.densityDpi == DisplayMetrics.DENSITY_MEDIUM
                        || metrics.densityDpi == DisplayMetrics.DENSITY_TV
                        || metrics.densityDpi == DisplayMetrics.DENSITY_XHIGH) {

                    return true;
                }
            }
        }
        return false;
    }
}
