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

           if (drawerButtonAnimation == null) {
               /*
                    NO ANIMATION FOR MENU PROVIDED, SET TO DEFAULT.
                */

               drawerButtonAnimation = "default";
           }

           layoutType = (Integer) settings.getField("LAYOUT_TYPE").get(settings);

           System.err.println("\n\nValore di layout type: "+layoutType+"\n\n");

       } catch (ClassNotFoundException e) {
           e.printStackTrace();

       } catch (NoSuchFieldException e) {
           e.printStackTrace();

       } catch (IllegalAccessException e) {
           e.printStackTrace();
       }





   }

   public static synchronized void parseDrawerMenuLayout() {

       try {

          // DrawerMenuTitles    = (String[]) settings.getField("MENU_TITLES").get(settings);
          // DrawerMenuLayouts   = (String[]) settings.getField("MENU_LAYOUT").get(settings);

           DrawerMenuTitles   = (String[]) settings.getField("MENU_TITLES").get(settings);
           DrawerMenuLayouts  = (String[]) settings.getField("MENU_LAYOUTS").get(settings);

           DrawerLayoutString = new HashMap<String,String>();
           DrawerLayoutMap = new HashMap<String, Integer>();

           if (DrawerMenuTitles.length == DrawerMenuLayouts.length) {

               DrawerLayoutIds = new Integer[DrawerMenuTitles.length];

               for (int i = 0; i < DrawerMenuTitles.length; i++) {

                   Integer  id                  = ZenResManager.getLayoutId(DrawerMenuLayouts[i]);
                   DrawerLayoutIds[i]           = id;
                   DrawerLayoutMap.put(DrawerMenuTitles[i], id);
                   DrawerLayoutString.put(DrawerMenuTitles[i], DrawerMenuLayouts[i]);

               }

           }

           ZenLog.l("lunghezza drawermenutitles"+DrawerMenuTitles.length + "__ ");
           ZenLog.l("lunghezza drawerlayoutids"+DrawerLayoutIds.length + "__ ");
           ZenLog.l("lunghezza drawerlayoutstring"+DrawerLayoutString.size() + "__ ");
           ZenLog.l("lunghezza drawermenulayouts"+DrawerMenuLayouts.length + "__ ");


           //DrawerMenuLayouts    = layouts;
           //DrawerMenuTitles     = titles;
           //DrawerLayoutMap      = layoutMap;
           //DrawerLayoutString   = layoutString;
           //DrawerLayoutIds      = ids;

       }
       catch (Exception e) {
         e.printStackTrace();
       }


    }
}
