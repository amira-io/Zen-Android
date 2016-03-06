/*
 * ZenFramework for Android
 *
 * :copyright: (c) 2013-2016 by Marco Stagni, Giovanni Barillari
 * :license: GPLv3, see LICENSE for more details.
 */

package io.amira.zen.http;

import com.loopj.android.http.FileAsyncHttpResponseHandler;

import org.apache.http.Header;

import java.io.File;

import io.amira.zen.core.ZenApplication;

public class ZenFileHandler extends FileAsyncHttpResponseHandler {
    Object caller;
    String successMethod;
    String errorMethod;
    String filename;

    public ZenFileHandler(Object caller, String onSuccessMethod, String onErrorMethod, String filename) {
        super(ZenApplication.context());
        _init(caller, onSuccessMethod, onErrorMethod, filename);
    }

    public ZenFileHandler(Object caller, String onSuccessMethod, String onErrorMethod, String filename, File file) {
        super(file);
        _init(caller, onSuccessMethod, onErrorMethod, filename);
    }

    public void _init(Object caller, String onSuccessMethod, String onErrorMethod, String filename) {
        this.caller = caller;
        successMethod = onSuccessMethod;
        errorMethod = onErrorMethod;
        this.filename = filename;
    }

    private void _callSuccess(File file) {
        Class[] params = new Class[2];
        params[0] = File.class;
        params[1] = String.class;
        Object[] values = new Object[2];
        values[0] = file;
        values[1] = filename;
        try {
            if (this.caller instanceof Class) {
                ((Class) this.caller).getMethod(this.successMethod, params).invoke(this.caller, values);
            } else {
                this.caller.getClass().getMethod(this.successMethod, params).invoke(this.caller, values);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void _callError() {
        try {
            if (this.caller instanceof Class) {
                ((Class) this.caller).getMethod(this.errorMethod).invoke(this.caller);
            } else {
                this.caller.getClass().getMethod(this.errorMethod).invoke(this.caller);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onFailure(int statusCode, Header[] headers, Throwable throwable, File file) {
        _callError();
    }

    @Override
    public void onSuccess(int statusCode, Header[] headers, File file) {
        if (statusCode == 200) {
            _callSuccess(file);
        } else {
            _callError();
        }
    }
}
