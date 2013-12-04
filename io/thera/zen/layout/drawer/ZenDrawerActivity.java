package io.thera.zen.layout.drawer;

import android.app.Activity;

/**
 * Created by marcostagni on 28/11/13.
 */

import java.security.InvalidParameterException;

import io.thera.zen.core.*;
import io.thera.zen.layout.drawer.*;
import io.thera.zen.layout.elements.*;
import io.thera.zen.listeners.touch.*;
import io.thera.zen.listeners.drawer.*;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.app.*;
import android.content.Context;
import android.content.res.Configuration;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.View.OnLayoutChangeListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.*;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.SeekBar.*;
import android.support.v4.app.*;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.*;
import android.app.Fragment;
import android.app.FragmentManager;

public class ZenDrawerActivity extends ZenActivity {//implements OnGestureListener, OnTouchListener {



    private SeekBar 		bar;
    private GestureDetector gDetector;
    private ImageView 		drawerButton;

    private TextView		title;

    private String[] drawerListViewItems;
    private ListView drawerListView;

    private DrawerLayout drawerLayout;
    //private ActionBarDrawerToggle actionBarDrawerToggle;



    /*
     * ACTIVITY METHODS.
     */
    @Override
    protected 	void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ZenAppManager.start(this);
        setUp();
    }

    @Override
    public void getElements() {

    }

    @Override
    public void buildElements() {

    }

    @Override
    public 		boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
		/*
		 * WE USE OUR CUSTOM MENU HANDLER.
		 */
        //getMenuInflater().inflate(R.menu.android_test_app, menu);
        return true;
    }

    @Override
    public 		void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        //actionBarDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public 		boolean onOptionsItemSelected(MenuItem item) {

        // call ActionBarDrawerToggle.onOptionsItemSelected(), if it returns true
        // then it has handled the app icon touch event
        //if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
        //    return true;
        //}
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected	void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        //actionBarDrawerToggle.syncState();
    }

    /*
     * END OF ACTIVITY METHODS.
     */

    public void setUp () {

		/*
		 * SETTING UP VIEW CONTENT.
		 */
        setUpElements();
        addListeners();
    }

    public void setUpElements () {

		/*
		 * RETRIEVING LAYOUT ELEMENTS.
		 */
        //this.context 				= this.getApplicationContext();

        this.drawerListViewItems 	= ZenSettingsManager.getDrawerMenuTitles();//this.getResources().getStringArray(ZenResManager.getArrayId("items"));
        this.drawerListView 		= (ListView) findViewById(ZenResManager.getResourceId("left_drawer"));
        this.drawerButton			= (ImageView) findViewById(ZenResManager.getResourceId("ic_menu"));
        this.drawerLayout 			= (DrawerLayout) findViewById(ZenResManager.getResourceId("drawer_layout"));
        this.drawerListView.setAdapter(new ArrayAdapter<String>(this, ZenResManager.getLayoutId("drawer_listview_item"), drawerListViewItems));


        /*
         * END OF ELEMENT RETRIEVING.
         */

    }

    public void handleMenuTouch(View v, MotionEvent e) {

        //Toast.makeText(AndroidTestApp.this, "touch handled", Toast.LENGTH_LONG).show();

        Animation menuAnimation = AnimationUtils.loadAnimation(this, ZenResManager.getAnimId("hyperspace_jump"));

        drawerButton.startAnimation(menuAnimation);
        if (drawerLayout.isDrawerOpen(drawerListView)) {
            drawerLayout.closeDrawer(drawerListView);
        }
        else {
            drawerLayout.openDrawer(drawerListView);
        }
        //ATLAppManager.moveDrawer();
    }

    public void addListeners () {

        final Animation drawerButtonAnimation;
        if ( ZenSettingsManager.getDrawerButtonAnimation() != "default") {
            drawerButtonAnimation = AnimationUtils.loadAnimation(this, ZenResManager.getAnimId(ZenSettingsManager.getDrawerButtonAnimation()));
        }
        else {
            drawerButtonAnimation = null;
        }

        drawerButton.setOnTouchListener(
                new OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {

                        if (drawerButtonAnimation != null) {
                            drawerButton.startAnimation(drawerButtonAnimation);
                        }
                        if (drawerLayout.isDrawerOpen(drawerListView)) {
                            drawerLayout.closeDrawer(drawerListView);
                            return true;
                        }
                        else {
                            drawerLayout.openDrawer(drawerListView);
                            return true;
                        }
                    }
                }
        );
        drawerLayout.setDrawerListener(new ZenDrawerListener());

        drawerListView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {

                //Toast.makeText(AndroidTestApp.this, ((TextView)view).getText(), Toast.LENGTH_LONG).show();
				/*
				 * CREATE A NEW FRAGMENT WITH OUR ATLFragmentManager.
				 *
				 * inside res/values/strings.xml
				 * there's a String array named fragmentNameList.
				 * Each value MUST be the exact id of the
				 * layout to load.
				 */

                //drawerLayout.closeDrawer(drawerListView);
                //drawerLayout.
                ZenLog.l("inside itemclicklistener");
                ZenLog.l(""+view.getId());
                long prima = System.nanoTime();
                ZenLog.l("POSITION "+ position + " - ID  " + id);
                ZenFragmentManager.setZenFragment((String) ((TextView) view).getText(), ZenAppManager.getActivity());
                long dopo = System.nanoTime();
                ZenLog.l("TIME to launch ATLFragmentmangager"+(dopo-prima));
                //updateLayout(((TextView) view).getText());
            }

        });

        //gDetector = new GestureDetector(context, this);


    }

	/*
	 * CLOSE DRAWER ACTIVITY
	 */

    public void closeDrawer() {
        drawerLayout.closeDrawer(drawerListView);
    }

    public void openDrawer() {
        drawerLayout.openDrawer(drawerListView);
    }

}


