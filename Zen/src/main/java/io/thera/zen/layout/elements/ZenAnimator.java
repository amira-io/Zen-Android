package io.thera.zen.layout.elements;

//import android.animation.ObjectAnimator;
import android.view.View;
import android.view.animation.TranslateAnimation;
import com.nineoldandroids.animation.ObjectAnimator;


/**
 * Created by giovanni on 21/03/14.
 */
public abstract class ZenAnimator {

    protected static ObjectAnimator objanim;
    protected static TranslateAnimation tanim;
    protected static View mView;

    public static void set(View view, float fromX, float toX, float fromY, float toY, int duration) {
        set(view, fromX, toX, fromY, toY, duration, true);
    }

    public static void set(View view, float fromX, float toX, float fromY, float toY, int duration, boolean autostart) {
        mView = view;

        objanim = ObjectAnimator.ofFloat(view , "y" , fromY, toY);
        objanim.setDuration(duration);

        if (autostart) {
            start();
        }

    }

    public static void start() {
        objanim.start();

    }

}
