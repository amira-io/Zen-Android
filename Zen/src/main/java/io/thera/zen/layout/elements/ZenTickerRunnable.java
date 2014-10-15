package io.thera.zen.layout.elements;

/**
 * Created by marcostagni on 14/05/14.
 */
public abstract class ZenTickerRunnable implements Runnable {
    public Object value, to;

    public void set(Object value, Object to) {
        //ZenLog.l("TEST TICKER calling set value");
        this.value = value;
        this.to = to;
    }
    public Object get() {
        //ZenLog.l("TEST TICKER calling get");
        return value;
    }

    public void run() {
        //ZenLog.l("TEST TICKER calling run");
        if (((Integer) this.value).intValue() == ((Integer) this.to).intValue()) {
            onComplete();
        }
        tick();
    }

    public abstract void tick();
    public abstract void onComplete();
}
