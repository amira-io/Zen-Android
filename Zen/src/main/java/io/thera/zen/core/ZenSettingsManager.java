package io.thera.zen.core;

/**
 * Created by marcostagni on 29/11/13.
 */

import java.lang.reflect.Field;
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
    static Class settings_menu;

    private static List<String> settings_list;

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

    //static String DrawerLayout;

    static int MenuType;

    public static int getMenuType() { return MenuType; }

    static String MenuLayout;

    static String NotExpandableMenuLayout;

    static String ExpandableMenuLayout;

    //public static String getDrawerLayout() { return DrawerLayout; }

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

    static List<String> appLanguages;
    static String appLanguageDefault;

    static Map<String,String> appFonts;



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

           Field[] fields = settings.getFields();
           settings_list = new ArrayList<String>();
           for (Field f : fields) {
               settings_list.add(f.getName());
           }

           drawerButtonAnimation = (String) _getSetting("DRAWER_BUTTON_ANIMATION", "default");
           layoutType = (Integer) _getSetting("LAYOUT_TYPE", 1);

           detailMap = (HashMap<String,String>) _getSetting("DETAIL_LAYOUTS");
           firstView = (String) _getSetting("FIRST_VIEW");

           appLanguages = (ArrayList<String>) _getSetting("LANGUAGES");
           appLanguageDefault = (String) _getSetting("LANGUAGE_DEFAULT", "en");

           appFonts = (HashMap<String,String>) _getSetting("FONTS");

           loadMenuConfig();

       } catch (Exception e) {
           e.printStackTrace();

       }

    }

    protected static void loadMenuConfig() {
        try {
            settings_menu = Class.forName("app.settings.Menu");
            settings_menu.getMethod("set").invoke(null);

            Field[] fields = settings_menu.getFields();
            for (Field f : fields) {
                settings_list.add(f.getName());
            }

            ZenLog.l(settings_list);

            List<ZenMenuItem> Menu = (List<ZenMenuItem>) _getMenuSetting("MENU");
            if (Menu != null) {
                ZenLog.l("MENU IS NOT NULL");
                appMenu = Menu.toArray(new ZenMenuItem[Menu.size()]);

                for (int i=0; i < appMenu.length; i++) {
                    if (appMenu[i].children.size() > 0) {
                        hasExpandableMenu = true;
                        break;
                    }
                }
            }

            String menu_type = (String) _getMenuSetting("MENU_TYPE", "content");
            if (menu_type.equals("content")) {
                MenuType = 1;
            }
            else {
                MenuType = 0;
            }

            MenuLayout = (String) _getMenuSetting("MENU_LAYOUT");
            //elements
            menuElements = new ArrayList<String>();
            //layouts
            menuLayouts = new HashMap<String, String>();
            //children
            menuChildren = new HashMap<String, List<String>>();
            //params
            menuParams = new HashMap<String, List<String>>();
            if (appMenu != null) {
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
            }

            if (hasExpandableMenu) {
                onlyOneIsOpen = (Boolean) _getMenuSetting("ONLY_ONE_IS_OPEN", true);
            }

            menuParentsAsParams = (Boolean) _getMenuSetting("MENU_PARENTS_AS_PARAMS", false);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static synchronized String getFont (String font) {

        String fontName;

        //fontName = (String) settings.getField(id+"_font").get(settings);
        /*if (appFonts == null) {

        }*/
        fontName = appFonts.get(font);
        if (fontName == null) {
            fontName = appFonts.get("default");
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

    public static synchronized String getLang() {
        String s = Locale.getDefault().getLanguage();
        if (appLanguages.contains(s)) {
            return s;
        }
        return appLanguageDefault;
    }


    private static Object _getSetting(String name) {
        return _getSetting(name, null);
    }

    private static Object _getSetting(String name, Object def) {
        Object o;

        if (settings_list.contains(name)) {
            try {
                o = settings.getField(name).get(settings);
            } catch (Exception e) {
                o = null;
            }
        } else {
            o = def;
        }

        return o;
    }

    private static Object _getMenuSetting(String name) {
        return _getMenuSetting(name, null);
    }

    private static Object _getMenuSetting(String name, Object def) {
        Object o;

        ZenLog.l("menuSetting name: "+name);
        if (settings_list.contains(name)) {
            try {
                o = settings_menu.getField(name).get(settings_menu);
                ZenLog.l("menuSetting value: "+o.toString());
            } catch (Exception e) {
                e.printStackTrace();
                o = null;
            }
        } else {
            ZenLog.l("SETTING NAME NOT IN LIST: "+name);
            o = def;
        }

        return o;
    }

}
