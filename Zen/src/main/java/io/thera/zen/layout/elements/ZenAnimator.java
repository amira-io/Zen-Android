package io.thera.zen.layout.elements;

//import android.animation.ObjectAnimator;
import android.graphics.Matrix;
import android.os.Build;
import android.text.format.Time;
import android.view.View;
import android.view.ViewGroup;
//import android.view.ViewPropertyAnimator;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.view.animation.TranslateAnimation;
import java.util.Timer;
import com.nineoldandroids.animation.ObjectAnimator;

import io.thera.zen.core.ZenLog;

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

        //if (Build.VERSION.SDK_INT >= 11) {
            objanim = ObjectAnimator.ofFloat(view , "y" , fromY, toY);
            objanim.setDuration(duration);
        //} else {
            //animate(mView).setDuration(duration).translationYBy(toY);
            /*
            final float fToY = toY;
            final float fFromY = fromY;
            final float rFromY;
            final float rToY;

            if (fromY > toY) {
                rFromY = toY;
                rToY = toY-fromY;
            } else {
                rFromY = fromY;
                rToY = toY;
            }

            tanim = new TranslateAnimation(0, 0, rFromY, rToY);
            tanim.setFillEnabled(true);
            tanim.setDuration(duration);
            tanim.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                    //mView.setDrawingCacheEnabled(true);
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    //mView.setDrawingCacheEnabled(false);
                    ZenLog.l("height " + mView.getMeasuredHeight());
                    //((ViewGroup.MarginLayoutParams)mView.getLayoutParams()).setMargins(0, (int) fToY, 0, 0);
                    mView.layout(0, (int) fToY, mView.getMeasuredWidth(), (int) (fToY + mView.getMeasuredHeight()));
                    //tanim.cancel();
                    //mView.getParent().requestLayout();

                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            */
        //}

        if (autostart) {
            start();
        }

    }

    public static void start() {
        //if (Build.VERSION.SDK_INT >= 11) {
            objanim.start();
        //} else {
            /*mView.startAnimation(tanim);*/
        //}
    }

}