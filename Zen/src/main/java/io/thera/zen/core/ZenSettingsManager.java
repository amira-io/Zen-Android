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

    static List<String> menuElements;
    static HashMap<String, String> menuLayouts;
    static HashMap<String, List<String>> menuChildren;
    static HashMap<String, List<String>> menuParams;

    public static List<String> getMenuElements() { return menuElements; }

    public static HashMap<String, String> getMenuLayouts() { return menuLayouts; }

    public static HashMap<String, List<String>> getMenuChildren() { return menuChildren; }

    public static HashMap<String, List<String>> getMenuParams() { return menuParams; }

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

    static String DrawerLayout;

    static String MenuLayout;

    static String NotExpandableMenuLayout;

    static String ExpandableMenuLayout;

    public static String getDrawerLayout() { return DrawerLayout; }

    public static String getMenuLayout() { return MenuLayout; }

    static boolean menuParentsAsParams;

    public static Boolean menuParentsAsParams() {
        return menuParentsAsParams;
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

           DrawerLayout = (String) settings.getField("DRAWER_LAYOUT").get(settings);

           MenuLayout = (String) settings.getField("MENU_LAYOUT").get(settings);

           NotExpandableMenuLayout  = (String) settings.getField("NOT_EXPANDABLE_MENU_LAYOUT").get(settings);

           ExpandableMenuLayout     = (String) settings.getField("EXPANDABLE_MENU_LAYOUT").get(settings);

           //System.err.println("\n\nValore di layout type: "+layoutType+"\n\n");

           //elements
           menuElements = new ArrayList<String>();
           //layouts
           menuLayouts = new HashMap<String, String>();
           //children
           menuChildren = new HashMap<String, List<String>>();
           //params
           menuParams = new HashMap<String, List<String>>();

           //String[] groups = (String[]) settings.getField("GROUPS").get(settings);
           for (int i = 0; i < appMenu.length ; i++ ) {

               List<String> lista = new ArrayList<String>();
               List<String> params = new ArrayList<String>();
               menuElements.add(appMenu[i].title);

               if (appMenu[i].children.size() > 0) {
                   ZenMenuItem[] children = appMenu[i].children.toArray(new ZenMenuItem[appMenu[i].children.size()]);

                   for (int j=0; j<children.length; j++) {
                       menuLayouts.put(children[j].title, children[j].layout);
                       lista.add(children[j].title);
                       params.add(children[j].param);
                   }
               }
               else {
                   menuLayouts.put(appMenu[i].title, appMenu[i].layout);
               }

               ZenLog.l(appMenu[i].title);
               ZenLog.l(lista.size());

               menuChildren.put(appMenu[i].title, lista);
               menuParams.put(appMenu[i].title, params);
           }

           if (hasExpandableMenu) {

               onlyOneIsOpen = (Boolean) settings.getField("ONLY_ONE_IS_OPEN").get(settings);

           }

           detailMap = new HashMap<String,String>();
           detailMap = (HashMap<String,String>) settings.getField("DETAIL_LAYOUTS").get(settings);

           /*
                GETTING VERY FIRST VIEW.

            */

           firstView = (String) settings.getField("FIRST_VIEW").get(settings);

           menuParentsAsParams = (Boolean) settings.getField("MENU_PARENTS_AS_PARAMS").get(settings);

       } catch (ClassNotFoundException e) {
           e.printStackTrace();

       } catch (NoSuchFieldException e) {
           e.printStackTrace();

       } catch (IllegalAccessException e) {
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
