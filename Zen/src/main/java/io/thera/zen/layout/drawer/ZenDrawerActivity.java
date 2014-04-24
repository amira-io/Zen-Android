package io.thera.zen.layout.drawer;


/**
 * Created by marcostagni on 28/11/13.
 */

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.thera.zen.R;
import io.thera.zen.core.*;
import io.thera.zen.layout.elements.*;
import io.thera.zen.listeners.drawer.*;

import android.content.pm.ActivityInfo;
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

import io.thera.zen.layout.slider.ZenSlidingMenu;

public class ZenDrawerActivity extends ZenActivity {//implements OnGestureListener, OnTouchListener {

    private SeekBar 		bar;
    private GestureDetector gDetector;
    private Button	    	drawerButton;
    private Button          backButton;

    private ImageView       drawerButtonImage;


    private TextView		title;

    private String[]        drawerListViewItems;
    private ListView        drawerListView;

    private RelativeLayout    drawerLayout;
    //private ActionBarDrawerToggle actionBarDrawerToggle;

    //to handle extendable menu.
    ArrayAdapter<String>        listAdapter;
    ExpandableListAdapter       explistAdapter;
    private ListView            nListView;
    ExpandableListView          expListView;
    List<String>                listDataHeader;
    Map<String, List<String>>   listDataChild;

    Map<String, List<String>>   listDataParams;

    private ZenSlidingMenu sMenu;

    /*
     * ACTIVITY METHODS.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (!skipStart) {
            setUp();
        }

        getElements();
        buildElements();
    }

    @Override
    public void getElements() {

    }

    @Override
    public void buildElements() {

    }

    @Override
    public void loadHelpers() {

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

        //load view
        //setContentView(ZenResManager.getLayoutId(ZenSettingsManager.getDrawerLayout()));
        setContentView(ZenResManager.getLayoutId("activity_main"));

        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();

        listDataHeader = ZenSettingsManager.getMenuElements();
        listDataChild = ZenSettingsManager.getMenuChildren();
        listDataParams = ZenSettingsManager.getMenuParams();

        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, ZenResManager.getLayoutId("activity_title_bar"));

        this.drawerButton			= (Button) findViewById(ZenResManager.getResourceId("ic_menu_overlay"));
        this.drawerButtonImage      = (ImageView) findViewById(ZenResManager.getResourceId("ic_menu"));
        //this.drawerLayout 			= (DrawerLayout) findViewById(ZenResManager.getResourceId("drawer_layout"));
        try {
            this.backButton = (Button) findViewById(ZenResManager.getResourceId("ic_back_overlay"));
        } catch (Exception e) {
            this.backButton = null;
        }


        /*
         * END OF ELEMENT RETRIEVING.
         */

        // load menu styling prefs and fallback on default if missing
        int widthId = ZenResManager.getDimenId("menu_width");
        if (widthId == 0) {
            widthId = R.dimen.menu_width;
        }
        int shadowId = ZenResManager.getDimenId("menu_shadow");
        if (shadowId == 0) {
            shadowId = R.dimen.menu_shadow;
        }
        // load menu
        sMenu = new ZenSlidingMenu(this, ZenSettingsManager.getMenuType());
        sMenu.setShadowWidthRes(shadowId);
        sMenu.setShadowDrawable(R.drawable.shadow);
        //sMenu.setBehindOffsetRes(offsetId);
        sMenu.setBehindWidthRes(widthId);
        sMenu.setFadeDegree(0.35f);
        sMenu.setMenu(ZenResManager.getLayoutId(ZenSettingsManager.getMenuLayout()));
        if (ZenSettingsManager.hasExpandableMenu()) {
            expListView = (ExpandableListView) findViewById(ZenResManager.getResourceId("menu_elements"));
            explistAdapter = new ZenExpandableListAdapter(this, listDataHeader, listDataChild);
            expListView.setAdapter(explistAdapter);
            expListView.setCacheColorHint(0);
        }
        else {
            nListView = (ListView) findViewById(ZenResManager.getResourceId("left_drawer"));
            listAdapter = new ArrayAdapter<String>(this, ZenResManager.getLayoutId("drawer_listview_item"), ZenResManager.getResourceId("group_item"), listDataHeader);
            nListView.setAdapter(listAdapter);
        }

        this.drawerLayout = (RelativeLayout) findViewById(ZenResManager.getResourceId("menu_l_layout"));

    }

    public void addListeners () {

        final Animation drawerButtonAnimation;
        if ( ZenSettingsManager.getDrawerButtonAnimation() != "default") {
            drawerButtonAnimation = AnimationUtils.loadAnimation(this, ZenResManager.getAnimId(ZenSettingsManager.getDrawerButtonAnimation()));
        }
        else {
            drawerButtonAnimation = null;
        }

        //drawerLayout.setDrawerListener(new ZenDrawerListener());

        drawerButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (drawerButtonAnimation != null) {
                            drawerButtonImage.startAnimation(drawerButtonAnimation);
                        }
                        sMenu.toggle();
                    }
                }
        );


        if (backButton != null) {
            backButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ZenLog.l("BACK BUTTON PRESSED");
                    ZenNavigationManager.back();
                }
            });
        }


        /*
        backButton.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                ZenLog.l("BACK BUTTON PRESSED");
                ZenNavigationManager.back();
                return true;
            }
        });*/

        if (!ZenSettingsManager.hasExpandableMenu()) {



            nListView.setOnItemClickListener(new OnItemClickListener() {

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
                    String tit = listDataHeader.get(position).toLowerCase();
                    ZenFragmentManager.setZenFragment(tit);
                    //updateLayout(((TextView) view).getText());
                    sMenu.toggle();
                }

            });

            //gDetector = new GestureDetector(context, this);
        }
        else {
            /*
                   settings for extendable menu.
             */

            expListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
                @Override
                public void onGroupExpand(int groupPosition) {
                    /*if (listDataChild.get(listDataHeader.get(groupPosition)).size() == 0) {
                        //vuol dire che stiamo cliccando un elemento del menu con zero figli.
                        String layout = ZenSettingsManager.getExpandableMenuLayoutsMap().get(listDataHeader.get(groupPosition));
                        //ZenLog.l("single layout" + layout);
                        ZenFragmentManager.setZenFragment(layout, ZenAppManager.getActivity() , false);
                        closeDrawer();
                        return;
                    }*/
                    if (ZenSettingsManager.onlyOneIsOpen()) {
                        for (int i = 0; i < listDataHeader.size() ; i++) {
                            if (i!=groupPosition) {
                                expListView.collapseGroup(i);
                            }
                        }
                    }
                }
            });

            expListView.setGroupIndicator(null);

            expListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

                @Override
                public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                    //Toast.makeText(
                    //        getApplicationContext(),
                    //        listDataHeader.get(groupPosition)
                    //                + " : "
                    //                + listDataChild.get(
                    //                listDataHeader.get(groupPosition)).get(
                    //                childPosition), Toast.LENGTH_SHORT)
                    //        .show();

                    //TEMP:
                    // (baro) WHY need layout? ZenAppManager should find it by title..
                    String tit = listDataChild.get( listDataHeader.get(groupPosition)).get(childPosition);
                    //String lay = ZenAppManager.getLayouts().get(tit).replace(" " ,"_");

                    //String lay = ZenSettingsManager.getMenuLayouts().get(tit).replace(" ", "_");
                    //
                    List<Object> params = new ArrayList<Object>();
                    if (ZenSettingsManager.menuParentsAsParams()) {
                        params.add(listDataHeader.get(groupPosition).toLowerCase());
                    }
                    params.add(listDataParams.get(listDataHeader.get(groupPosition)).get(childPosition));
                    ZenNavigationManager.setParameters(params);

                    //ZenFragmentManager.setZenFragment(listDataChild.get( listDataHeader.get(groupPosition)).get(childPosition), ZenAppManager.getActivity(),false);
                    ZenFragmentManager.setZenFragment(tit);

                    sMenu.toggle();

                    return true;
                }
            });

            expListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {

                @Override
                public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                    if (expListView.getExpandableListAdapter().getChildrenCount(groupPosition)==0) {
                        final String toCall = ((String) expListView.getExpandableListAdapter().getGroup(groupPosition));
                        ZenLog.l("GROUP ZERO"+ toCall);
                        ZenFragmentManager.setZenFragment(toCall);
                        sMenu.toggle();
                    }
                    else {
                        if (expListView.isGroupExpanded(groupPosition)) {
                            expListView.collapseGroup(groupPosition);
                        }
                        else {
                            expListView.expandGroup(groupPosition);
                        }
                    }
                    return true;
                }
            });
        }

    }

}


