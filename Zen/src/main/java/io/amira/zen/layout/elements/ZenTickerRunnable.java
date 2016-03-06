/*
 * ZenFramework for Android
 *
 * :copyright: (c) 2013-2016 by Marco Stagni, Giovanni Barillari
 * :license: GPLv3, see LICENSE for more details.
 */

package io.amira.zen.layout.elements;

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
