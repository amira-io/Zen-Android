/*
 * ZenFramework for Android
 *
 * :copyright: (c) 2013-2016 by Marco Stagni, Giovanni Barillari
 * :license: GPLv3, see LICENSE for more details.
 */

package io.amira.zen.core;

import android.util.Log;

public class ZenLog {

    static private String className;
    static private String methodName;
    static private int lineNumber;

    public ZenLog() {}

    private boolean isDebuggable() {
        //return ZenApplication.config().isDebug();
        return ZenApplication.debug;
    }

    private void getMethodNames(StackTraceElement[] sElements){
        className = sElements[2].getFileName();
        methodName = sElements[2].getMethodName();
        lineNumber = sElements[2].getLineNumber();
    }

    private static String createLog(Object log) {
        StringBuffer buffer = new StringBuffer();
        buffer.append("[");
        buffer.append(methodName);
        buffer.append(":");
        buffer.append(lineNumber);
        buffer.append("]");
        buffer.append(log.toString());
        return buffer.toString();
    }

    public void e(Object message){
        if (!isDebuggable())
            return;

        getMethodNames(new Throwable().getStackTrace());
        Log.e(className, createLog(message));
    }

    public void i(Object message){
        if (!isDebuggable())
            return;

        getMethodNames(new Throwable().getStackTrace());
        Log.i(className, createLog(message));
    }

    public void d(Object message){
        if (!isDebuggable())
            return;

        getMethodNames(new Throwable().getStackTrace());
        Log.d(className, createLog(message));
    }

    public void v(Object message){
        if (!isDebuggable())
            return;

        getMethodNames(new Throwable().getStackTrace());
        Log.v(className, createLog(message));
    }

    public void w(Object message){
        if (!isDebuggable())
            return;

        getMethodNames(new Throwable().getStackTrace());
        Log.w(className, createLog(message));
    }

}
