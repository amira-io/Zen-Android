/*
 * ZenFramework for Android
 *
 * :copyright: (c) 2013-2016 by Marco Stagni, Giovanni Barillari
 * :license: GPLv3, see LICENSE for more details.
 */

package io.amira.zen.utils;

import java.util.ArrayList;
import java.util.List;

public class ZenMenuItem {
    public String title;
    public String layout;
    public String param;
    public List<ZenMenuItem> children;

    public ZenMenuItem(String title, String layout, String param, List<ZenMenuItem> children) {
        this.title = title;
        this.layout = layout;
        this.param = param;
        this.children = children;
    }

    public ZenMenuItem(String title, String layout, String param) {
        this(title, layout, param, new ArrayList<ZenMenuItem>());
    }

    public boolean addChild(ZenMenuItem item) {
        return this.children.add(item);
    }
}
