package io.thera.zen.layout.drawer;


/**
 * Created by marcostagni on 28/11/13.
 */

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import io.thera.zen.core.*;
import io.thera.zen.layout.elements.*;
import io.thera.zen.listeners.drawer.*;
import android.os.Bundle;
import android.content.res.Configuration;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.*;
import android.widget.AdapterView.OnItemClickListener;
import android.support.v4.widget.*;

public class ZenDrawerActivity extends ZenActivity {//implements OnGestureListener, OnTouchListener {



    private SeekBar 		bar;
    private GestureDetector gDetector;
    private ImageView 		drawerButton;
    private ImageView       backButton;

    private TextView		title;

    private String[] drawerListViewItems;
    private ListView drawerListView;

    private DrawerLayout drawerLayout;
    //private ActionBarDrawerToggle actionBarDrawerToggle;

    //to handle extendable menu.
    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;

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

        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);

		/*
		 * RETRIEVING LAYOUT ELEMENTS.
		 */
        //this.context 				= this.getApplicationContext();




        if (ZenSettingsManager.hasExpandableMenu()) {
            //adding general view.
            setContentView(ZenResManager.getLayoutId(ZenSettingsManager.getExpandableMenuLayout())); //must be changed if we have a different menu
            prepareListData();
            listAdapter = new ZenExpandableListAdapter(this, listDataHeader, listDataChild);
            expListView = (ExpandableListView) findViewById(ZenResManager.getResourceId("left_drawer"));
            expListView.setAdapter(listAdapter);

        }
        else {
            //adding general view.
            setContentView(ZenResManager.getLayoutId(ZenSettingsManager.getNotExpandableMenuLayout())); //must be changed if we have a different menu
            this.drawerListViewItems 	= ZenSettingsManager.getDrawerMenuTitles();//this.getResources().getStringArray(ZenResManager.getArrayId("items"));
            this.drawerListView 		= (ListView) findViewById(ZenResManager.getResourceId("left_drawer"));
            this.drawerListView.setAdapter(new ArrayAdapter<String>(this, ZenResManager.getLayoutId("drawer_listview_item"), drawerListViewItems));
        }
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, ZenResManager.getLayoutId("activity_title_bar"));

        this.drawerButton			= (ImageView) findViewById(ZenResManager.getResourceId("ic_menu"));
        this.drawerLayout 			= (DrawerLayout) findViewById(ZenResManager.getResourceId("drawer_layout"));
        this.backButton             = (ImageView) findViewById(ZenResManager.getResourceId("ic_back"));


        /*
         * END OF ELEMENT RETRIEVING.
         */

    }

    /*
    * Preparing the list data
    */
    private void prepareListData() {
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();

        // Adding child data
        listDataHeader.add("Top 250");
        listDataHeader.add("Now Showing");
        listDataHeader.add("Coming Soon..");

        // Adding child data
        List<String> top250 = new ArrayList<String>();
        top250.add("The Shawshank Redemption");
        top250.add("The Godfather");
        top250.add("The Godfather: Part II");
        top250.add("Pulp Fiction");
        top250.add("The Good, the Bad and the Ugly");
        top250.add("The Dark Knight");
        top250.add("12 Angry Men");

        List<String> nowShowing = new ArrayList<String>();
        nowShowing.add("The Conjuring");
        nowShowing.add("Despicable Me 2");
        nowShowing.add("Turbo");
        nowShowing.add("Grown Ups 2");
        nowShowing.add("Red 2");
        nowShowing.add("The Wolverine");

        List<String> comingSoon = new ArrayList<String>();
        comingSoon.add("2 Guns");
        comingSoon.add("The Smurfs 2");
        comingSoon.add("The Spectacular Now");
        comingSoon.add("The Canyons");
        comingSoon.add("Europa Report");

        listDataChild.put(listDataHeader.get(0), top250); // Header, Child data
        listDataChild.put(listDataHeader.get(1), nowShowing);
        listDataChild.put(listDataHeader.get(2), comingSoon);
    }


    public void addListeners () {

        final Animation drawerButtonAnimation;
        if ( ZenSettingsManager.getDrawerButtonAnimation() != "default") {
            drawerButtonAnimation = AnimationUtils.loadAnimation(this, ZenResManager.getAnimId(ZenSettingsManager.getDrawerButtonAnimation()));
        }
        else {
            drawerButtonAnimation = null;
        }

        drawerLayout.setDrawerListener(new ZenDrawerListener());

        drawerButton.setOnTouchListener(
                new OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        if (!ZenSettingsManager.hasExpandableMenu()) {
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
                        else {
                            if (drawerButtonAnimation != null) {
                                drawerButton.startAnimation(drawerButtonAnimation);
                            }
                            if (drawerLayout.isDrawerOpen(expListView)) {
                                drawerLayout.closeDrawer(expListView);
                                return true;
                            }
                            else {
                                drawerLayout.openDrawer(expListView);
                                return true;
                            }
                        }

                    }
                }
        );

        backButton.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                ZenNavigationManager.back();
                return true;
            }
        });

        if (!ZenSettingsManager.hasExpandableMenu()) {



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
                    ZenFragmentManager.setZenFragment((String) ((TextView) view).getText(), ZenAppManager.getActivity(),false);
                    long dopo = System.nanoTime();
                    ZenLog.l("TIME to launch ATLFragmentmangager"+(dopo-prima));
                    //updateLayout(((TextView) view).getText());
                }

            });

            //gDetector = new GestureDetector(context, this);
        }
        else {
            /*
                   settings for extendable menu.
             */
        }

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


