package io.thera.zen.layout.elements;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.content.*;
import android.os.Handler;

import io.thera.zen.core.ZenAppManager;
import io.thera.zen.core.ZenLog;
import io.thera.zen.core.ZenNavigationManager;
import io.thera.zen.core.ZenSettingsManager;
import io.thera.zen.layout.drawer.ZenFragmentManager;

/**
 * Created by marcostagni on 03/12/13.
 */
public abstract class ZenActivity extends FragmentActivity{

    protected boolean skipStart = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (!skipStart) {
            if (savedInstanceState != null) {
                ZenLog.l("RESTORING ACTIVITY");
                String fragmentToLoad = savedInstanceState.getString("fragment");
                boolean loadFirst = !(fragmentToLoad.equals(ZenSettingsManager.getFirstView()));
                ZenAppManager.start(this, loadFirst, false);
                ZenFragmentManager.setZenFragment(fragmentToLoad, false);
            }
            else {
                ZenLog.l("NEW ACTIVITY");
                ZenAppManager.start(this);
            }
        }

        loadHelpers();

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("fragment", ZenFragmentManager.getCurrent());
    }

    @Override
    public  void onBackPressed() {
        ZenNavigationManager.back();
    }

    public abstract void getElements();

    public abstract void buildElements();

    public void loadHelpers() {
        getElements();
        buildElements();
    }

    public void goTo(Class activity, boolean addToStack) {
        final Class a = activity;

        Intent i = new Intent(ZenActivity.this, activity);
        startActivity(i);

        // close this activity
        if (addToStack) {
            ZenNavigationManager.push(this.getClass().getCanonicalName(), this.getClass().getSuperclass().getCanonicalName());
        }
        finish();
    }

    public void goTo ( Class activity , boolean addToStack , boolean mustFinish ) {
        final Class a = activity;

        Intent i = new Intent(ZenActivity.this, activity);
        startActivity(i);

        // close this activity
        if (addToStack) {
            ZenNavigationManager.push(this.getClass().getCanonicalName(), this.getClass().getSuperclass().getCanonicalName());
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
                ZenLog.l("GOTO " + this.getClass().getName());
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
                ZenLog.l("GOTO " + this.getClass().getName());
                ZenNavigationManager.push(cName, this.getClass().getSuperclass().getCanonicalName());

                finish();
            }
        }, timeout);
    }
}
