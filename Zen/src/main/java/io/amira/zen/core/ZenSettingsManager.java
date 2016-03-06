/*
 * ZenFramework for Android
 *
 * :copyright: (c) 2013-2016 by Marco Stagni, Giovanni Barillari
 * :license: GPLv3, see LICENSE for more details.
 */

package io.amira.zen.core;

import java.lang.reflect.Field;
import java.util.*;

import io.amira.zen.utils.ZenMenuItem;

public class ZenSettingsManager {

    /*
        dev MUST provide a valid settings file
        inside app.settings package.

        example:
            file name
            app.settings.Settings.java
    */

    private Class settings;
    private Class settings_menu;
    private List<String> settings_list;

    private boolean debug;
    private String firstView;
    private Map<String, String> layoutsMap;
    private List<String> languages;
    private String languages_default;
    private Map<String,String> fonts;

    private String drawer_buttonAnimation;

    private ZenMenuItem[] drawer_menu;
    private boolean drawer_menuExpandable;
    private boolean drawer_menuExpOnlyOne;
    private boolean drawer_menuExpParentsParams;
    private int drawer_menuType;
    private String drawer_menuLayout;
    private List<String> drawer_menu_elements;
    private HashMap<String, String> drawer_menu_layouts;
    private HashMap<String, List<String>> drawer_menu_children;
    private HashMap<String, List<String>> drawer_menu_params;

    public ZenSettingsManager() {
        try {
            settings = Class.forName("app.settings.Settings");

            Field[] fields = settings.getFields();
            settings_list = new ArrayList<String>();
            for (Field f : fields) {
                settings_list.add(f.getName());
            }

            debug = (Boolean) _getSetting("DEBUG", false);

            firstView = (String) _getSetting("FIRST_VIEW");

            layoutsMap = (HashMap<String,String>) _getSetting("LAYOUTS");

            languages = (ArrayList<String>) _getSetting("LANGUAGES");
            languages_default = (String) _getSetting("LANGUAGE_DEFAULT", "en");

            fonts = (HashMap<String,String>) _getSetting("FONTS");

            drawer_menuExpandable = false;
            drawer_buttonAnimation = (String) _getSetting("DRAWER_BUTTON_ANIMATION", "default");

            // init drawer arrays/maps to avoid nullpointers
            //elements
            drawer_menu_elements = new ArrayList<String>();
            //layouts
            drawer_menu_layouts = new HashMap<String, String>();
            //children
            drawer_menu_children = new HashMap<String, List<String>>();
            //params
            drawer_menu_params = new HashMap<String, List<String>>();

            _loadMenuConfig();

        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    protected void _loadMenuConfig() {
        try {
            settings_menu = Class.forName("app.settings.Menu");
            settings_menu.getMethod("set").invoke(null);

            Field[] fields = settings_menu.getFields();
            for (Field f : fields) {
                settings_list.add(f.getName());
            }

            //ZenLog.l(settings_list);

            List<ZenMenuItem> Menu = (List<ZenMenuItem>) _getMenuSetting("MENU");
            if (Menu != null) {
                ZenApplication.log("MENU IS NOT NULL");
                drawer_menu = Menu.toArray(new ZenMenuItem[Menu.size()]);

                for (int i=0; i < drawer_menu.length; i++) {
                    if (drawer_menu[i].children.size() > 0) {
                        drawer_menuExpandable = true;
                        break;
                    }
                }
            }

            String menu_type = (String) _getMenuSetting("MENU_TYPE", "content");
            if (menu_type.equals("content")) {
                drawer_menuType = 1;
            }
            else {
                drawer_menuType = 0;
            }

            drawer_menuLayout = (String) _getMenuSetting("MENU_LAYOUT");

            if (drawer_menu != null) {
                //String[] groups = (String[]) settings.getField("GROUPS").get(settings);
                for (int i = 0; i < drawer_menu.length ; i++ ) {

                    List<String> lista = new ArrayList<String>();
                    List<String> params = new ArrayList<String>();
                    drawer_menu_elements.add(drawer_menu[i].title);

                    if (drawer_menu[i].children.size() > 0) {
                        ZenMenuItem[] children = drawer_menu[i].children.toArray(new ZenMenuItem[drawer_menu[i].children.size()]);

                        for (int j=0; j<children.length; j++) {
                            drawer_menu_layouts.put(children[j].title, children[j].layout);
                            lista.add(children[j].title);
                            params.add(children[j].param);
                        }
                    }
                    else {
                        drawer_menu_layouts.put(drawer_menu[i].title, drawer_menu[i].layout);
                    }

                    //ZenLog.l(drawer_menu[i].title);
                    ZenApplication.log(lista.size());

                    drawer_menu_children.put(drawer_menu[i].title, lista);
                    drawer_menu_params.put(drawer_menu[i].title, params);
                }
            }

            if (drawer_menuExpandable) {
                drawer_menuExpOnlyOne = (Boolean) _getMenuSetting("ONLY_ONE_IS_OPEN", true);
            }

            drawer_menuExpParentsParams = (Boolean) _getMenuSetting("MENU_PARENTS_AS_PARAMS", false);

        } catch (Exception e) {
            ZenApplication.log.i("Menu settings not defined in app.");
        }
    }

    protected Object _getSetting(String name) {
        return _getSetting(name, null);
    }

    protected Object _getSetting(String name, Object def) {
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

    protected Object _getMenuSetting(String name) {
        return _getMenuSetting(name, null);
    }

    protected Object _getMenuSetting(String name, Object def) {
        Object o;

        if (settings_list.contains(name)) {
            try {
                o = settings_menu.getField(name).get(settings_menu);
            } catch (Exception e) {
                e.printStackTrace();
                o = null;
            }
        } else {
            ZenApplication.log.i("Setting "+name+" is not in app config, fall back to default value: "+def);
            o = def;
        }

        return o;
    }

    public boolean isDebug() { return debug; }

    public String getFirstView() {
        return firstView;
    }

    public HashMap<String, String> getDrawer_menu_layouts() {
        return drawer_menu_layouts;
    }

    public List<String> getDrawer_menu_elements() {
        return drawer_menu_elements;
    }

    public HashMap<String, List<String>> getDrawer_menu_children() {
        return drawer_menu_children;
    }

    public HashMap<String, List<String>> getDrawer_menu_params() {
        return drawer_menu_params;
    }

    public int getDrawer_menuType() {
        return drawer_menuType;
    }

    public String getDrawer_menuLayout() {
        return drawer_menuLayout;
    }

    public boolean isDrawer_menuExpandable() {
        return drawer_menuExpandable;
    }

    public String getDrawer_buttonAnimation() {
        return drawer_buttonAnimation;
    }

    public boolean isDrawer_menuExpOnlyOne() {
        return drawer_menuExpOnlyOne;
    }

    public boolean isDrawer_menuExpParentsParams() {
        return drawer_menuExpParentsParams;
    }

    public String getFont (String font) {
        String fontName;

        fontName = fonts.get(font);
        if (fontName == null) {
            fontName = fonts.get("default");
        }

        return fontName;
    }

    public String getStorageFile () {
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

    public String getLang() {
        String s = Locale.getDefault().getLanguage();
        if (languages.contains(s)) {
            return s;
        }
        return languages_default;
    }

}
