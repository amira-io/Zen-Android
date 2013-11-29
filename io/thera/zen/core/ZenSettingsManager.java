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



   public static synchronized void parseDrawerMenuLayout(String[] titles, String[] layouts, Map<String,Integer> layoutMap, Map<String, String> layoutString ,  Integer[] ids) {

       try {
           settings = Class.forName("app.settings.Settings");

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
