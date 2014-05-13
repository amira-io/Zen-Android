package io.thera.zen.layout.elements;

//import android.animation.*;
import com.nineoldandroids.animation.*;

import io.thera.zen.core.ZenLog;

/**
 * Created by marcostagni on 13/05/14.
 */
public class ZenValueAnimator  {

    static TickerRunnable _runnable;
    static Ticker t;

    public static abstract class TickerRunnable implements Runnable {
        public Object value, to;

        public void set(Object value, Object to) {
            ZenLog.l("TEST TICKER calling set value");
            this.value = value;
            this.to = to;
        }
        public Object get() {
            ZenLog.l("TEST TICKER calling get");
            return value;
        }

        public void run() {
            ZenLog.l("TEST TICKER calling run");
            if (((Integer) this.value).intValue() == ((Integer) this.to).intValue()) {
                onComplete();
            }
            tick();
        }

        public abstract void tick();
        public abstract void onComplete();
    }

    public static class Ticker {
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

        public void go(TickerRunnable runnable) {
            ZenLog.l("TEST TICKER calling go");
            _runnable = runnable;
            ValueAnimator va;
            if (type.equals("int")) {
                va = ValueAnimator.ofInt((Integer) from, (Integer) to);
            } else {
                va = ValueAnimator.ofFloat((Float) from, (Float) to);
            }
            va.setDuration(duration);
            va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    try {
                        ZenLog.l("TEST TICKER calling set");
                        _runnable.set(animation.getAnimatedValue(), to);
                        _runnable.run();
                    } catch (Exception e) {
                        ZenLog.l("TEST TICKER exception");
                        ZenLog.l("EXCEPTION occurred in value animator listener");
                        e.printStackTrace();
                    }
                }
            });
            va.start();
        }

    }

    public static Ticker set(int from, int to, int duration) {
        ZenLog.l("TEST TICKER calling set");
        return new Ticker(from, to, duration);
    }

    public static Ticker set(float from, float to, int duration) {
        return new Ticker(from, to, duration);
    }


}
