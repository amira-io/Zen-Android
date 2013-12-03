package io.thera.zen.layout.elements;

import android.app.Activity;
import android.os.Bundle;
import android.content.*;
import android.os.Handler;

/**
 * Created by marcostagni on 03/12/13.
 */
public abstract class ZenActivity extends Activity{

    /**
     * Called when the activity is starting.  This is where most initialization
     * should go: calling {@link #setContentView(int)} to inflate the
     * activity's UI, using {@link #findViewById} to programmatically interact
     * with widgets in the UI, calling
     * {@link #managedQuery(android.net.Uri, String[], String, String[], String)} to retrieve
     * cursors for data being displayed, etc.
     * <p/>
     * <p>You can call {@link #finish} from within this function, in
     * which case onDestroy() will be immediately called without any of the rest
     * of the activity lifecycle ({@link #onStart}, {@link #onResume},
     * {@link #onPause}, etc) executing.
     * <p/>
     * <p><em>Derived classes must call through to the super class's
     * implementation of this method.  If they do not, an exception will be
     * thrown.</em></p>
     *
     * @param savedInstanceState If the activity is being re-initialized after
     *                           previously being shut down then this Bundle contains the data it most
     *                           recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     * @see #onStart
     * @see #onSaveInstanceState
     * @see #onRestoreInstanceState
     * @see #onPostCreate
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getElements();
        buildElements();
    }

    public abstract void getElements();

    public abstract void buildElements();

    public void goTo (Class activity) {

        final Class a = activity;

        Intent i = new Intent(ZenActivity.this, activity);
        startActivity(i);

        // close this activity
        finish();

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
                finish();
            }
        }, timeout);
    }
}
