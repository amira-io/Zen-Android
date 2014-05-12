package io.thera.zen.layout.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.view.ContextThemeWrapper;

import com.google.android.gms.internal.ac;

import java.util.HashMap;
import java.util.Map;

import io.thera.zen.core.ZenAppManager;
import io.thera.zen.core.ZenLog;

/**
 * Created by marcostagni on 05/12/13.
 */
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
        _type = 2;
    }

    public void show() {
        show(ZenAppManager.getActivity());
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
                                .setPositiveButton(_defaults.get("ok"), new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        ok();
                                    }
                                })
                                .setNegativeButton(_defaults.get("cancel"), new DialogInterface.OnClickListener() {
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
                    case 2 : {
                        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(a, android.R.style.Theme_Holo_Light_Dialog));
                        builder.setTitle(title);
                        builder.setMessage(message);
                        builder.create().show();
                        break;
                    }
                }
            }
        });
    }

    public abstract void ok();
    public abstract void cancel();

}
