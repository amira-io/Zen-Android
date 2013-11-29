package io.thera.zen.core;

/**
 * Created by marcostagni on 29/11/13.
 */

import java.util.*;

public class ZenSettingsManager {

   /*
        dev MUST provide a valid settings file
        inside app.settings package.

        example:
            file name
            app.settings.Settings.java
    */

   static Class settings;

   static String[]                  DrawerMenuTitles;
   static String[]                  DrawerMenuLayouts;
   static Map<String , Integer>     DrawerLayoutMap;
   static Map<String , String>      DrawerLayoutString;
   static Integer[]                 DrawerLayoutIds;


    public static String[] getDrawerMenuTitles() {
        return DrawerMenuTitles;
    }

    public static void setDrawerMenuTitles(String[] drawerMenuTitles) {
        DrawerMenuTitles = drawerMenuTitles;
    }

    public static String[] getDrawerMenuLayouts() {
        return DrawerMenuLayouts;
    }

    public static void setDrawerMenuLayouts(String[] drawerMenuLayouts) {
        DrawerMenuLayouts = drawerMenuLayouts;
    }

    public static Map<String, Integer> getDrawerLayoutMap() {
        return DrawerLayoutMap;
    }

    public static void setDrawerLayoutMap(Map<String, Integer> drawerLayoutMap) {
        DrawerLayoutMap = drawerLayoutMap;
    }

    public static Map<String, String> getDrawerLayoutString() {
        return DrawerLayoutString;
    }

    public static void setDrawerLayoutString(Map<String, String> drawerLayoutString) {
        DrawerLayoutString = drawerLayoutString;
    }

    public static Integer[] getDrawerLayoutIds() {
        return DrawerLayoutIds;
    }

    public static void setDrawerLayoutIds(Integer[] drawerLayoutIds) {
        DrawerLayoutIds = drawerLayoutIds;
    }

    static String drawerButtonAnimation;

   static Integer layoutType;

   public static Integer getLayoutType() {

       return layoutType;

   }

   public static String getDrawerButtonAnimation() {

       return drawerButtonAnimation;

   }


   public static synchronized void start() {
       /*
            GETTING GENERAL VARIABLES
        */
       try {
           settings = Class.forName("app.settings.Settings");
           /*
                GET HOME BUTTON ANIMATION IF AVAILABLE
           */
           drawerButtonAnimation = (String) settings.getField("DRAWER_BUTTON_ANIMATION").get(settings);

           layoutType = (Integer) settings.getField("LAYOUT_TYPE").get(settings);

       } catch (ClassNotFoundException e) {
           e.printStackTrace();

       } catch (NoSuchFieldException e) {
           e.printStackTrace();

       } catch (IllegalAccessException e) {
           e.printStackTrace();
       }





   }

   public static synchronized void parseDrawerMenuLayout(String[] titles, String[] layouts, Map<String,Integer> layoutMap, Map<String, String> layoutString ,  Integer[] ids) {

       try {

          // DrawerMenuTitles    = (String[]) settings.getField("MENU_TITLES").get(settings);
          // DrawerMenuLayouts   = (String[]) settings.getField("MENU_LAYOUT").get(settings);

           titles = (String[]) settings.getField("MENU_TITLES").get(settings);
           layouts = (String[]) settings.getField("MENU_LAYOUT").get(settings);

           if (titles.length == layouts.length) {

               ids = new Integer[titles.length];

               for (int i = 0; i < titles.length; i++) {

                   Integer  id     = ZenResManager.getLayoutId(layouts[i]);
                   ids[i]    = id;
                   layoutMap.put(titles[i], id);
                   layoutString.put(titles[i], layouts[i]);

               }

           }

           DrawerMenuLayouts    = layouts;
           DrawerMenuTitles     = titles;
           DrawerLayoutMap      = layoutMap;
           DrawerLayoutString   = layoutString;
           DrawerLayoutIds      = ids;

       }
       catch (Exception e) {
         e.printStackTrace();
       }


    }
}
