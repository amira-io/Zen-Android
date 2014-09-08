package io.thera.zen.layout.elements;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.EditText;

import io.thera.zen.core.ZenResManager;

/**
 * Created by marcostagni on 25/04/14.
 */
public class ZenEditText extends EditText {

    @Override
    public boolean isInEditMode() {
        return true;
    }

    public ZenEditText(Context context) {
        super(context);
        this.init();

    }

    public ZenEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.init();

    }

    public ZenEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.init();

    }

    private void init() {
        CharSequence fontTag = this.getContentDescription();
        String fontString = "";
        if (fontTag != null) {
            fontString = fontTag.toString();
        }
        setTypeface(ZenResManager.getTypeface(fontString, getContext()) ,1);
    }
}
