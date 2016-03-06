/*
 * ZenFramework for Android
 *
 * :copyright: (c) 2013-2016 by Marco Stagni, Giovanni Barillari
 * :license: GPLv3, see LICENSE for more details.
 */

package io.amira.zen.layout.drawer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.amira.zen.R;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.*;
import android.widget.AdapterView.OnItemClickListener;

import io.amira.zen.core.ZenActivityMain;
import io.amira.zen.core.ZenApplication;
import io.amira.zen.core.ZenResManager;
import io.amira.zen.layout.slider.ZenSlidingMenu;

public class ZenDrawerActivity extends ZenActivityMain {


    //to handle extendable menu.
    ArrayAdapter<String>        listAdapter;
    ExpandableListAdapter       explistAdapter;
    List<String>                listDataHeader;
    Map<String, List<String>>   listDataChild;

    Map<String, List<String>>   listDataParams;

    private ZenSlidingMenu sMenu;

    @Override
    public void _preHelpers(Bundle savedInstanceState) {
        super._preHelpers(savedInstanceState);

        setUp();
    }

    //: set up contents
    public void setUp () {

        setUpElements();
        addListeners();
    }

    public void setUpElements () {

        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);

        setContentView(ZenResManager.getLayoutId("activity_main"));

        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();

        listDataHeader = ZenApplication.config().getDrawer_menu_elements();
        listDataChild = ZenApplication.config().getDrawer_menu_children();
        listDataParams = ZenApplication.config().getDrawer_menu_params();

        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, ZenResManager.getLayoutId("activity_title_bar"));

        //: load menu styling prefs and fallback on default if missing
        int widthId = ZenResManager.getDimenId("menu_width");
        if (widthId == 0) {
            widthId = R.dimen.menu_width;
        }
        int shadowId = ZenResManager.getDimenId("menu_shadow");
        if (shadowId == 0) {
            shadowId = R.dimen.menu_shadow;
        }

        //: setting up sliding menu
        sMenu = new ZenSlidingMenu(this, ZenApplication.config().getDrawer_menuType());
        sMenu.setShadowWidthRes(shadowId);
        sMenu.setShadowDrawable(R.drawable.shadow);
        sMenu.setBehindWidthRes(widthId);
        sMenu.setFadeDegree(0.35f);
        sMenu.setMenu(ZenResManager.getLayoutId(ZenApplication.config().getDrawer_menuLayout()));
        if (ZenApplication.config().isDrawer_menuExpandable()) {
            explistAdapter = new ZenExpandableListAdapter(this, listDataHeader, listDataChild);
            res("menu_elements").go("setAdapter",explistAdapter);
            res("menu_elements").go("setCacheColorHint",0);
        }
        else {
            listAdapter = new ArrayAdapter<String>(this, ZenResManager.getLayoutId("drawer_listview_item"), ZenResManager.getResourceId("group_item"), listDataHeader);
            res("left_drawer").go("setAdapter",listAdapter);
        }

        //this.drawerLayout = (RelativeLayout) findViewById(ZenResManager.getResourceId("menu_l_layout"));

    }

    public void addListeners () {

        final Animation drawerButtonAnimation;
        if (!ZenApplication.config().getDrawer_buttonAnimation().equals("default")) {
            drawerButtonAnimation = AnimationUtils.loadAnimation(this, ZenResManager.getAnimId(ZenApplication.config().getDrawer_buttonAnimation()));
        }
        else {
            drawerButtonAnimation = null;
        }

        res("ic_menu_overlay").go("setOnClickListener", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (drawerButtonAnimation != null) {
                        res("ic_menu").go("startAnimation",drawerButtonAnimation);
                    }
                    sMenu.toggle();
                }
            }
        );

        if (res("ic_back_overlay").exists()) {
            res("ic_back_overlay").go("setOnClickListener", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ZenApplication.navigation().back();
                }
            });
        }

        //: setting up non expandable menu
        if (!ZenApplication.config().isDrawer_menuExpandable()) {
            res("left_drawer").go("setOnItemClickListener", new OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String tit = listDataHeader.get(position).toLowerCase();
                    ZenApplication.fragments().load(tit);
                    sMenu.toggle();
                }

            });
        }
        //: setting up expandable menu
        else {
            res("menu_elements").go("setOnGroupExpandListener", new ExpandableListView.OnGroupExpandListener() {
                @Override
                public void onGroupExpand(int groupPosition) {
                    if (ZenApplication.config().isDrawer_menuExpOnlyOne()) {
                        for (int i = 0; i < listDataHeader.size() ; i++) {
                            if (i!=groupPosition) {
                                res("menu_elements").go("collapseGroup",i);
                            }
                        }
                    }
                }
            });

            res("menu_elements").go("setGroupIndicator", null);

            //: listners on expandable list childs
            res("menu_elements").go("setOnChildClickListener", new ExpandableListView.OnChildClickListener() {

                @Override
                public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {

                    String tit = listDataChild.get( listDataHeader.get(groupPosition)).get(childPosition);

                    List<Object> params = new ArrayList<Object>();
                    if (ZenApplication.config().isDrawer_menuExpParentsParams()) {
                        params.add(listDataHeader.get(groupPosition).toLowerCase());
                    }
                    params.add(listDataParams.get(listDataHeader.get(groupPosition)).get(childPosition));
                    ZenApplication.navigation().setParameters(params);
                    ZenApplication.fragments().load(tit);

                    sMenu.toggle();

                    return true;
                }
            });

            //: listeners on expandable list groups
            res("menu_elements").go("setOnGroupClickListener", new ExpandableListView.OnGroupClickListener() {

                @Override
                public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                    ExpandableListAdapter expListAdapter = (ExpandableListAdapter) res("menu_elements").go("getExpandableListAdapter");
                    if (expListAdapter.getChildrenCount(groupPosition)==0) {
                        final String toCall = ((String) expListAdapter.getGroup(groupPosition));
                        ZenApplication.fragments().load(toCall);
                        sMenu.toggle();
                    }
                    else {
                        if ((Boolean)res("menu_elements").go("isGroupExpanded",groupPosition)) {
                            res("menu_elements").go("collapseGroup", groupPosition);
                        }
                        else {
                            res("menu_elements").go("expandGroup",groupPosition);
                        }
                    }
                    return true;
                }
            });
        }

    }

}


