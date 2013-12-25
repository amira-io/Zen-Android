package io.thera.zen.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by giovanni on 24/12/13.
 */
public class ZenMenuItem {
    public String title;
    public String layout;
    public String param;
    public List<ZenMenuItem> childs;

    public ZenMenuItem(String title, String layout, String param, List<ZenMenuItem> childs) {
        this.title = title;
        this.layout = layout;
        this.param = param;
        this.childs = childs;
    }

    public ZenMenuItem(String title, String layout, String param) {
        this(title, layout, param, new ArrayList<ZenMenuItem>());
    }

    public boolean addChild(ZenMenuItem item) {
        return this.childs.add(item);
    }
}
