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

   static String[] DrawerMenuTitles;
   static String[] DrawerMenuLayouts;

   static String homeButtonAnimation;

   public static String getHomeButtonAnimation() {

       return homeButtonAnimation;

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
           homeButtonAnimation = (String) settings.getField("homeButtonAnimation").get(settings);

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

       }
       catch (Exception e) {
         e.printStackTrace();
       }


    }
}
