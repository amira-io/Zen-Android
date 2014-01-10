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

    @Override
    public boolean isInEditMode() {
        return true;
    }


    public ZenTextView(Context context) {
        super(context);
        //ZenLog.l(this.getId());
        this.init();

    }

    public ZenTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        //ZenLog.l(this.getId());
        this.init();

    }

    public ZenTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        //ZenLog.l(this.getId());
        this.init();

    }

    public void init() {
        CharSequence fontTag = this.getContentDescription();
        String fontString = "";
        if (fontTag != null) {
            fontString = fontTag.toString();
        }
        String fontName = ZenSettingsManager.getFont(fontString);
        ZenLog.l(fontName);
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/"+fontName);
        setTypeface(tf ,1);
    }
}
