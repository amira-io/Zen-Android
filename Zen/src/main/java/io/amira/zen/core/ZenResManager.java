/*
 * ZenFramework for Android
 *
 * :copyright: (c) 2013-2016 by Marco Stagni, Giovanni Barillari
 * :license: GPLv3, see LICENSE for more details.
 */

package io.amira.zen.core;

import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import java.util.HashMap;
import java.util.Map;

public class ZenResManager {

    public static int getResourceId(String name) {
        return ZenApplication.resources().getIdentifier(name, "id", ZenApplication.packageName());
    }

    public static int getLayoutId(String name) {
        return ZenApplication.resources().getIdentifier(name, "layout", ZenApplication.packageName());
    }

    public static int getArrayId(String name) {
        return ZenApplication.resources().getIdentifier(name, "array", ZenApplication.packageName());
    }

    public static int getAnimId(String name) {
        return ZenApplication.resources().getIdentifier(name, "anim", ZenApplication.packageName());
    }

	public static Animation getAnimation(String name) {
        Animation a;
        try {
            a = AnimationUtils.loadAnimation(ZenApplication.context(), getAnimId(name));
        } catch (Exception e) {
            a = null;
        }
        return a;
    }

    public static int getDrawableId(String name) {
        return ZenApplication.resources().getIdentifier(name, "drawable", ZenApplication.packageName());
    }

    public static Drawable getDrawable(String name) {
        return ZenApplication.resources().getDrawable(getDrawableId(name));
    }

    public static int getStringId(String name) {
        return ZenApplication.resources().getIdentifier(name, "string", ZenApplication.packageName());
    }

    public static String getString(String name) {
        return ZenApplication.resources().getString(getStringId(name));
    }

    public static int getDimenId(String name) {
        return ZenApplication.resources().getIdentifier(name, "dimen", ZenApplication.packageName());
    }

    static Map<String, Typeface> tfs = new HashMap<String, Typeface>();

    public static Typeface getTypeface(String font, Context c) {
        String fontName = ZenApplication.config().getFont(font);
        //ZenApplication.log(fontName);
        if (!tfs.containsKey(fontName)) {
            try {
                ZenApplication.log("Creating typeface for font "+fontName);
                tfs.put(fontName, Typeface.createFromAsset(c.getAssets(), "fonts/"+fontName));
            }
            catch (Exception e) {
                ZenApplication.log("Could not get typeface '" + fontName
                        + "' because " + e.getMessage());
                return null;
            }
        }
        return tfs.get(fontName);
    }

}
