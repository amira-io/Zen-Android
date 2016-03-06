/*
 * ZenFramework for Android
 *
 * :copyright: (c) 2013-2016 by Marco Stagni, Giovanni Barillari
 * :license: GPLv3, see LICENSE for more details.
 */

package io.amira.zen.utils;

import java.util.ArrayList;

public class ZenMenu extends ArrayList<ZenMenuItem> {

    public boolean addEl(String title, String layout, String param) {
        return super.add(new ZenMenuItem(title, layout, param));
    }

    public boolean addEl(String title, String layout) {
        return this.addEl(title, layout, "");
    }

    public boolean addEl(String title) {
        return this.addEl(title, "", "");
    }

    public boolean addCh(String title, String layout, String param) {
        ZenMenuItem element = super.get(super.size()-1);
        return element.addChild(new ZenMenuItem(title, layout, param));
    }

    public boolean addCh(String title, String layout) {
        return this.addCh(title, layout, "");
    }

    public boolean addCh(String title) {
        return this.addCh(title, "", "");
    }
}
