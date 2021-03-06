package com.coreictconsultancy.fourtune.ui;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
/**
 * Created by Martin Mundia 07/02/2021.
 */
public class MyEditText extends androidx.appcompat.widget.AppCompatEditText {

    public MyEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public MyEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MyEditText(Context context) {
        super(context);
        init();
    }

    private void init() {
        if (!isInEditMode()) {
            Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "Lato-Regular.ttf");
            setTypeface(tf);
        }
    }

}