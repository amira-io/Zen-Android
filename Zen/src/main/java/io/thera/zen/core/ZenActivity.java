package io.thera.zen.core;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.content.*;
import android.os.Handler;

import java.util.HashMap;
import java.util.Map;

import io.thera.zen.layout.ZenViewProxy;

/**
 * Created by marcostagni on 03/12/13.
 * Revisited by gi0baro on 14/05/14.
 */

public abstract class ZenActivity extends FragmentActivity{

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
