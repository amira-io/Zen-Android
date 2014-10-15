package io.thera.zen.core;

import android.os.Bundle;

import static java.lang.System.identityHashCode;

/**
 * Created by giovanni on 14/05/14.
 */
public class ZenActivityMain extends ZenActivity {

    @Override
    public boolean doAndroidStateRecover() {
        return false;
    }

    @Override
    public void _preHelpers(Bundle savedInstanceState) {
        // ensure instance in ZenApplication
        ZenApplication.registerActivity(this);

        if (savedInstanceState != null) {
            ZenApplication.log("RESTORING ACTIVITY");
            String fragmentToLoad = savedInstanceState.getString("fragment");
            ZenApplication.fragments().load(fragmentToLoad);
        }
        else {
            ZenApplication.log("NEW ACTIVITY");
            ZenApplication.fragments().load(ZenApplication.config().getFirstView());
        }
    }

    @Override
    protected void onDestroy() {
        ZenApplication.log(identityHashCode(this) + " ONDESTROY");
        ZenApplication.unregisterActivity();
        super.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString("fragment", ZenApplication.fragments().getCurrent());
        super.onSaveInstanceState(outState);
    }

    @Override
    public void getElements() {

    }

    @Override
    public void buildElements() {

    }
}
