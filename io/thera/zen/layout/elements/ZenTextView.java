package io.thera.zen.layout.elements;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

import io.thera.zen.core.*;

/**
 * Created by marcostagni on 28/11/13.
 *
 */
public class ZenTextView extends TextView {

    public ZenTextView(Context context) {
        super(context);
        ZenLog.l(this.getId());
        this.init();
    }

    public ZenTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        ZenLog.l(this.getId());
        this.init();

    }

    public ZenTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        ZenLog.l(this.getId());
        this.init();

    }

    public void init() {
        String fontName = ZenSettingsManager.getFont(this.getId());
        ZenLog.l(fontName);
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/"+fontName);
        setTypeface(tf ,1);
    }
}
