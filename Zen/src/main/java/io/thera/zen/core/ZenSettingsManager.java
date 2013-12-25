package io.thera.zen.core;

/**
 * Created by marcostagni on 29/11/13.
 */

import java.util.*;

import io.thera.zen.utils.ZenMenuItem;

public class ZenSettingsManager {

    /*
        dev MUST provide a valid settings file
        inside app.settings package.

        example:
            file name
            app.settings.Settings.java
    */

    /**
     *  SETTINGS FILE.
     */

    static Class settings;

    /**
     *  LAYOUT TYPE.
     */

    static Integer layoutType;

    public static Integer getLayoutType() {

        return layoutType;

    }


    /**
     *  DRAWER LAYOUT SETTINGS.
     */

    static ZenMenuItem[] appMenu;

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

    public static String getDrawerButtonAnimation() {

       return drawerButtonAnimation;

    }



    /**
    *    DETAIL VIEWS.
    */

    static Map<String,String> detailMap;

    public static Map<String,String> getDetailMap() {
       return detailMap;
    }

    /**
     *  EXPANDABLE MENU
     */

    static boolean  hasExpandableMenu;

    public static Boolean hasExpandableMenu() {
        return hasExpandableMenu;
    }

    static boolean onlyOneIsOpen;

    public static Boolean onlyOneIsOpen() {
        return onlyOneIsOpen;
    }

    static String NotExpandableMenuLayout;

    static String ExpandableMenuLayout;


    public static String getExpandableMenuLayout() {
        return ExpandableMenuLayout;
    }

    public static String getNotExpandableMenuLayout() {
        return NotExpandableMenuLayout;
    }

    static List<String> expandableMenuGroups;

    public static List<String> getExpandableMenuGroups () {
        return expandableMenuGroups;
    }

    static Map<String,List<String>> expandableMenuMap;

    public static Map<String,List<String>> getExpandableMenuMap() {
         return expandableMenuMap;
    }

    static Map<String,String> expandableMenuLayoutsMap;

    public static Map<String,String> getExpandableMenuLayoutsMap() {
        return expandableMenuLayoutsMap;
    }

    /**
     *  FIRST VIEW
     */

    static String firstView;

    public static String getFirstView() {
     return firstView;
    }


    /**
     *  SETTINGS METHODS.
     */

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

           /*
                CHECK IF OUR APP HAS A COLLAPSIBLE MENU
            */

           List<ZenMenuItem> Menu = (List<ZenMenuItem>) settings.getField("MENU").get(settings);
           appMenu = Menu.toArray(new ZenMenuItem[Menu.size()]);

           for (int i=0; i < appMenu.length; i++) {
               if (appMenu[i].children.size() > 0) {
                   hasExpandableMenu = true;
                   break;
               }
           }

           //hasExpandableMenu = (Boolean) settings.getField("HAS_EXPANDABLE_MENU").get(settings);

           NotExpandableMenuLayout  = (String) settings.getField("NOT_EXPANDABLE_MENU_LAYOUT").get(settings);

           ExpandableMenuLayout     = (String) settings.getField("EXPANDABLE_MENU_LAYOUT").get(settings);

           System.err.println("\n\nValore di layout type: "+layoutType+"\n\n");

           if (hasExpandableMenu) {

               onlyOneIsOpen = (Boolean) settings.getField("ONLY_ONE_IS_OPEN").get(settings);

               expandableMenuGroups = new ArrayList<String>();
               expandableMenuLayoutsMap = new HashMap<String, String>();
               expandableMenuMap = new HashMap<String, List<String>>();

               //String[] groups = (String[]) settings.getField("GROUPS").get(settings);
               for (int i = 0; i < appMenu.length ; i++ ) {

                   List<String> lista = new ArrayList<String>();
                   expandableMenuGroups.add(appMenu[i].title);

                   if (appMenu[i].children.size() > 0) {
                       ZenMenuItem[] children = appMenu[i].children.toArray(new ZenMenuItem[appMenu[i].children.size()]);

                       for (int j=0; j<children.length; j++) {
                           expandableMenuLayoutsMap.put(children[j].title, children[j].layout);
                           lista.add(children[j].title);
                       }
                   }
                   else {
                       expandableMenuLayoutsMap.put(appMenu[i].title, appMenu[i].layout);
                   }

                   ZenLog.l(appMenu[i].title);
                   ZenLog.l(lista.size());

                   expandableMenuMap.put(appMenu[i].title, lista);

                   /*
                   String[] group_names     = (String[]) settings.getField(groups[i]+"_NAMES").get(settings);
                   String[] group_layouts   = (String[]) settings.getField(groups[i]+"_LAYOUTS").get(settings);

                   List<String> lista = new ArrayList<String>();
                   int gll = group_layouts.length;
                   int gnl = group_names.length;

                   if ((gll!=0) && (gnl!=0)) {
                       if (group_layouts.length == group_names.length) {
                           for (int j =0 ; j < group_names.length ; j++ ) {
                               expandableMenuLayoutsMap.put(group_names[j], group_layouts[j]);
                               lista.add(group_names[j]);
                           }

                       }
                   }
                   else {
                       String single_layout = (String) settings.getField(groups[i]+"_LAYOUT").get(settings);
                       expandableMenuLayoutsMap.put(groups[i],single_layout);
                   }

                   expandableMenuMap.put(groups[i],lista);

                   */

               }
           }

           /*
                GETTING VERY FIRST VIEW.

            */

           firstView = (String) settings.getField("FIRST_VIEW").get(settings);

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

           List<String> titles = new ArrayList<String>();
           List<String> layouts = new ArrayList<String>();

           for (int i = 0; i < appMenu.length ; i++ ) {

               titles.add(appMenu[i].title);
               layouts.add(appMenu[i].layout);

           }

           DrawerMenuTitles = titles.toArray(new String[titles.size()]);
           DrawerMenuLayouts = layouts.toArray(new String[layouts.size()]);

           //DrawerMenuTitles   = (String[]) settings.getField("MENU_TITLES").get(settings);
           //DrawerMenuLayouts  = (String[]) settings.getField("MENU_LAYOUTS").get(settings);

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

           detailMap = new HashMap<String,String>();
           detailMap = (HashMap<String,String>) settings.getField("DETAIL_LAYOUTS").get(settings);

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

    public static synchronized String getFont (int id) {

        String fontName;

        try {
             fontName = (String) settings.getField(id+"_font").get(settings);

        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return null;
        } catch (NoSuchFieldException e) {

            //e.printStackTrace();
            try {
            fontName = (String) settings.getField("DEFAULT_FONT").get(settings);
            }
            catch (IllegalAccessException ex) {
                e.printStackTrace();
                return null;

            } catch (NoSuchFieldException ex) {
                e.printStackTrace();
                return null;
            }
        }

        return fontName;
    }

    public static synchronized String getStorageFile () {
        String storageFile;
        try {
            storageFile = (String) settings.getField("STORAGE_FILE").get(settings);
            return storageFile;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return "io.thera.default.storage";
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
            return "io.thera.default.storage";
        }
    }
}
