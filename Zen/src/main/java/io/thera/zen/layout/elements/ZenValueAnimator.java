package io.thera.zen.layout.elements;

import com.nineoldandroids.animation.*;

import io.thera.zen.core.ZenApplication;

/**
 * Created by marcostagni on 13/05/14.
 */

public class ZenValueAnimator  {

    static ZenTickerRunnable _runnable;
    static Ticker t;

    public class Ticker {
        Object from, to;
        int duration;
        public Object value;
        protected String type;

        public Ticker(int from, int to, int duration){
            this.from = from;
            this.to = to;
            this.duration = duration;
            this.type = "int";
        }

        public Ticker(float from, float to, int duration) {
            this.from = from;
            this.to = to;
            this.duration = duration;
            this.type = "float";
        }
//

        public void go(ZenTickerRunnable runnable) {
            ZenApplication.log("TEST TICKER calling go");
            _runnable = runnable;
            ValueAnimator va;
            if (type.equals("int")) {
                va = ValueAnimator.ofInt((Integer) from, (Integer) to);
            } else {
                va = ValueAnimator.ofFloat((Float) from, (Float) to);
            }
            va.setDuration(duration);
            //va.setInterpolator(new LinearInterpolator());
            va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    try {
                        _runnable.set(animation.getAnimatedValue(), to);
                        _runnable.run();
                    } catch (Exception e) {
                        ZenApplication.log("TEST TICKER exception");
                        ZenApplication.log("EXCEPTION occurred in value animator listener");
                        e.printStackTrace();
                    }
                }
            });
            va.start();
        }

    }

    public Ticker set(int from, int to, int duration) {
        ZenApplication.log("TEST TICKER calling set");
        return new Ticker(from, to, duration);
    }

    public Ticker set(float from, float to, int duration) {
        return new Ticker(from, to, duration);
    }


}
