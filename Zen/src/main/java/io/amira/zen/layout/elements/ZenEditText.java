/*
 * ZenFramework for Android
 *
 * :copyright: (c) 2013-2016 by Marco Stagni, Giovanni Barillari
 * :license: GPLv3, see LICENSE for more details.
 */

package io.amira.zen.layout.elements;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.EditText;

import io.amira.zen.core.ZenResManager;

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

    public void init() {
        CharSequence fontTag = this.getContentDescription();
        String fontString = "";
        if (fontTag != null) {
            fontString = fontTag.toString();
        }
        setTypeface(ZenResManager.getTypeface(fontString, getContext()) ,1);
    }
}
