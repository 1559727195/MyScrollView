package com.masskysraum.myscrollview;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;

public class CustomView extends FrameLayout {
    public CustomView(Context context) {
        super(context);
    }

    public CustomView(Context context,AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomView(Context context, AttributeSet attrs, int defStyleAttr) {//---
        super(context, attrs, defStyleAttr);
    }
    //构造方法省略...


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(measureWidth(widthMeasureSpec),measureWidth(heightMeasureSpec));
    }


    private int measureWidth(int widthMeasureSpec) {
        int width = 0;
        /**
         * 1、从MeasureSpec对象中提出出具体的测量模式和大小
         */
        int specMode = MeasureSpec.getMode(widthMeasureSpec);
        int specSize = MeasureSpec.getSize(widthMeasureSpec);
        /**
         * 2、通过判断测量模式，给出不同的测量值
         */
        if (specMode == MeasureSpec.EXACTLY) {   // match_parent , accurate
            width = specSize;
        } else {
            width = 200;    //给一个默认的大小
            if (specMode == MeasureSpec.AT_MOST) {  // wrap_content
                width = Math.min(width,specSize); //注意取两者之间小的值
            }
        }
        return width;
    }


}
