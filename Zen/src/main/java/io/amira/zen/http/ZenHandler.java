/*
 * ZenFramework for Android
 *
 * :copyright: (c) 2013-2016 by Marco Stagni, Giovanni Barillari
 * :license: GPLv3, see LICENSE for more details.
 */

package io.amira.zen.http;

import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.Header;

import java.io.File;

import io.amira.zen.core.ZenApplication;

public abstract class ZenHandler extends AsyncHttpResponseHandler {
    Object caller;
    String successMethod;
    String errorMethod;

    public ZenHandler(Object caller, String onSuccessMethod, String onErrorMethod) {
        this.caller = caller;
        successMethod = onSuccessMethod;
        errorMethod = onErrorMethod;
    }

    @Override
    public void onSuccess(int i, Header[] headers, byte[] bytes) {

    }

    @Override
    public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {

    }
}
