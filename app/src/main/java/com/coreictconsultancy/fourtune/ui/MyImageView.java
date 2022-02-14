package com.coreictconsultancy.fourtune.ui;


import android.content.Context;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatImageView;


public class MyImageView extends AppCompatImageView {
    public MyImageView(Context paramContext) {
        super(paramContext);
    }

    public MyImageView(Context paramContext, AttributeSet paramAttributeSet) {
        super(paramContext, paramAttributeSet);
    }

    public MyImageView(Context paramContext, AttributeSet paramAttributeSet, int paramInt) {
        super(paramContext, paramAttributeSet, paramInt);
    }

    protected void onMeasure(int paramInt1, int paramInt2) {
        super.onMeasure(paramInt1, paramInt2);
        paramInt1 = getMeasuredWidth();
        double d = getMeasuredWidth();
        Double.isNaN(d);
        //setMeasuredDimension(paramInt1, (int)(d * 1.5D));
        setMeasuredDimension(paramInt1, (int)(d * 1.2D));
    }
}
