package io.thera.zen.utils;

import io.thera.zen.core.ZenApplication;

/**
 * Created by giovanni on 14/05/14.
 */
public class Utilities {

    private final static Integer lock = 1;

    public static void RunOnUIThread(Runnable runnable) {
        synchronized (lock) {
            ZenApplication.applicationHandler.post(runnable);
        }
    }
}
