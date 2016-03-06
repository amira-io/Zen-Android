/*
 * ZenFramework for Android
 *
 * :copyright: (c) 2013-2016 by Marco Stagni, Giovanni Barillari
 * :license: GPLv3, see LICENSE for more details.
 */

package io.amira.zen.core;

import android.os.Bundle;

import static java.lang.System.identityHashCode;

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
