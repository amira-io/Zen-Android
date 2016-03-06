/*
 * ZenFramework for Android
 *
 * :copyright: (c) 2013-2016 by Marco Stagni, Giovanni Barillari
 * :license: GPLv3, see LICENSE for more details.
 */

package io.amira.zen.utils;

import io.amira.zen.core.ZenApplication;

public class Utilities {

    private final static Integer lock = 1;

    public static void RunOnUIThread(Runnable runnable) {
        synchronized (lock) {
            ZenApplication.applicationHandler.post(runnable);
        }
    }
}
