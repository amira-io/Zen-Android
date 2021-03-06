/*
 * ZenFramework for Android
 *
 * :copyright: (c) 2013-2016 by Marco Stagni, Giovanni Barillari
 * :license: GPLv3, see LICENSE for more details.
 */

package io.amira.zen.layout.elements;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.ContextThemeWrapper;

import java.util.HashMap;
import java.util.Map;

import io.amira.zen.core.ZenApplication;

public abstract class ZenDialog {

    String message;
    String button_OK = null, button_CANCEL = null;
    String title;
    int _type;

    static Map<String, String> _defaults = new HashMap<String, String>();
    static {
        _defaults.put("ok", "ok");
        _defaults.put("cancel", "cancel");
    }

    public ZenDialog(String title, String message, String ok , String cancel) {
        this.title = title;
        this.message = message;
        this.button_OK = ok;
        this.button_CANCEL = cancel;
        _type = 0;
    }

    public ZenDialog(String title, String message, String ok) {
        this.title = title;
        this.message = message;
        this.button_OK = ok;
        _type = 1;
    }

    public ZenDialog(String title, String message) {
        this.title = title;
        this.message = message;
        this.button_OK = _defaults.get("ok");
        this.button_CANCEL = _defaults.get("cancel");
        _type = 0;
    }

    public void show() {
        show(ZenApplication.getAppActivity());
    }

    public void show(Activity activity) {
        final Activity a = activity;
        a.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                switch (_type) {
                    case 0 : {
                        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper( a, android.R.style.Theme_Holo_Light_Dialog));
                        builder.setTitle(title);
                        builder.setMessage(message)
                                .setPositiveButton(button_OK, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        ok();
                                    }
                                })
                                .setNegativeButton(button_CANCEL, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        cancel();
                                    }
                                });
                        builder.create().show();
                        break;
                    }
                    case 1 : {
                        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(a, android.R.style.Theme_Holo_Light_Dialog));
                        builder.setTitle(title);
                        builder.setMessage(message)
                                .setPositiveButton(button_OK, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        ok();
                                    }
                                });
                        builder.create().show();
                        break;
                    }
                }
            }
        });
    }

    public void ok() {}
    public void cancel() {}

}
