package com.masskysraum.myscrollview;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ScrollView;

public class MyScrollView extends ScrollView {


    private final String TAG = this.getClass().getSimpleName();
    private boolean scrollerTaskRunning;


    public MyScrollView(Context context) {
        super(context);
    }

    public MyScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    public MyScrollView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onScrollChanged(int currentX, int currentY, int oldx, int oldy) {
        super.onScrollChanged(currentX, currentY, oldx, oldy);
        if (myScrollViewListener != null) {
            myScrollViewListener.onMyScrollChanged(this, currentX, currentY, oldx, oldy);
            if (currentX - oldx > 0) {
                myScrollViewListener.onMyScrollDown(this, currentX, currentY, oldx, oldy);
                Log.i(TAG, "正在向下滚动");
            } else {
                myScrollViewListener.onMyScrollUp(this, currentX, currentY, oldx, oldy);
                Log.i(TAG, "正在向上滚动");
            }

//           if (getScrollY() <= 0) {
            if (getScrollY() <= 0) {
                myScrollViewListener.onMyScrollTop(this, currentX, currentY, oldx, oldy);
                Log.i(TAG, "到达了顶部");
            }


//====================================================
            View view = (View) getChildAt(getChildCount() - 1);//获取 ScrollView最后一个控件
//            int diff =(view.getBottom() - (getHeight() + getScrollY()));
            int diff = view.getBottom() - (getHeight() + getScrollY());

//            if (diff ==0){
            if (diff == 0) {
                myScrollViewListener.onMyScrollBottom(this, currentX, currentY, oldx, oldy);
                Log.i(TAG, "到达了底部");
            }


            if (myScrollViewListener != null) {
                myScrollViewListener.onMyScrollChanged(this, currentX, currentY, oldx, oldy);
                if (!scrollerTaskRunning) {
                    startScrollerTask(this,currentX,currentY, oldx, oldy);
                }
            }
        }
    }

    private MyScrollViewListener myScrollViewListener;


    public void setMyScrollViewListener(MyScrollViewListener myScrollViewListener) {
        this.myScrollViewListener = myScrollViewListener;
    }


}
