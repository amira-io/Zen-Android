/*
 * ZenFramework for Android
 *
 * :copyright: (c) 2013-2016 by Marco Stagni, Giovanni Barillari
 * :license: GPLv3, see LICENSE for more details.
 */

package io.amira.zen.core;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import java.util.HashMap;
import java.util.Map;

import io.amira.zen.layout.ZenViewProxy;

public abstract class ZenActivity extends AppCompatActivity{

    //: Zen support to resources (in views)
    private Map<String, Integer> _res;

    public boolean doAndroidStateRecover() {
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (savedInstanceState != null && !doAndroidStateRecover()) {
            if (savedInstanceState.getParcelable("android:support:fragments") != null) {
                savedInstanceState.putParcelable("android:support:fragments", null);
            }
        }
        super.onCreate(savedInstanceState);

        ZenApplication.initApplication();

        _res = new HashMap<String, Integer>();

        _preHelpers(savedInstanceState);
        _loadHelpers();
    }


    @Override
    public  void onBackPressed() {
        ZenApplication.navigation().back();
    }

    public abstract void getElements();

    public abstract void buildElements();

    public void _preHelpers(Bundle savedInstanceState) {

    }

    public void _loadHelpers() {
        getElements();
        buildElements();
    }

    //: provides access to resources
    public ZenViewProxy res(String id) {
        if (!_res.containsKey(id)) {
            int vid = ZenResManager.getResourceId(id);
            _res.put(id, vid);
        }
        return new ZenViewProxy(_res.get(id), this);
    }

    public void goTo(Class activity, boolean addToStack) {
        final Class a = activity;

        Intent i = new Intent(ZenActivity.this, activity);
        startActivity(i);

        // close this activity
        if (addToStack) {
            ZenApplication.navigation().push(this.getClass().getCanonicalName(), this.getClass().getSuperclass().getCanonicalName());
        }
        finish();
    }

    public void goTo ( Class activity , boolean addToStack , boolean mustFinish ) {
        final Class a = activity;

        Intent i = new Intent(ZenActivity.this, activity);
        startActivity(i);

        // close this activity
        if (addToStack) {
            ZenApplication.navigation().push(this.getClass().getCanonicalName(), this.getClass().getSuperclass().getCanonicalName());
        }
        if (mustFinish) {
            finish();
        }
    }

    public void goTo (Class activity) {

        this.goTo(activity, true);

    }

    public void goToOnTimeout(Class activity, int timeout) {

        final Class a = activity;
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                // This method will be executed once the timer is over
                // Start your app main activity
                Intent i = new Intent(ZenActivity.this, a);
                startActivity(i);

                // close this activity
                ZenApplication.log("GOTO " + a.getClass().getName());
                finish();
            }
        }, timeout);
    }

    public void goToOnTimeout(String cActivity, Class activity, int timeout) {
        final String cName = cActivity;
        final Class a = activity;
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                // This method will be executed once the timer is over
                // Start your app main activity
                Intent i = new Intent(ZenActivity.this, a);
                startActivity(i);

                // close this activity
                ZenApplication.log("GOTO " + a.getClass().getName());
                ZenApplication.navigation().push(cName, a.getClass().getSuperclass().getCanonicalName());

                finish();
            }
        }, timeout);
    }
}
