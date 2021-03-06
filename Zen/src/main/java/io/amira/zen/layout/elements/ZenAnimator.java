/*
 * ZenFramework for Android
 *
 * :copyright: (c) 2013-2016 by Marco Stagni, Giovanni Barillari
 * :license: GPLv3, see LICENSE for more details.
 */

package io.amira.zen.layout.elements;

import android.animation.ObjectAnimator;
import android.view.View;
import android.view.animation.TranslateAnimation;
//import com.nineoldandroids.animation.ObjectAnimator;

public abstract class ZenAnimator {

    protected static ObjectAnimator objanim;
    protected static TranslateAnimation tanim;
    protected static View mView;

    public static void set(View view, float fromX, float toX, float fromY, float toY, int duration) {
        set(view, fromX, toX, fromY, toY, duration, true);
    }

    public static void set(View view, float fromX, float toX, float fromY, float toY, int duration, boolean autostart) {
        mView = view;

        if (fromX == toX) {
            objanim = ObjectAnimator.ofFloat(view , "y" , fromY, toY);
            objanim.setDuration(duration);
        } else if (fromY == toY) {
            objanim = ObjectAnimator.ofFloat(view, "x", fromX, toX);
            objanim.setDuration(duration);
        }

        if (autostart) {
            start();
        }

    }

    public static void start() {
        objanim.start();

    }

}
