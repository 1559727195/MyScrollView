package com.masskysraum.myscrollview;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ScrollView;

public class MyScrollView extends ScrollView {


    private final String TAG = this.getClass().getSimpleName();

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

//           if(getScrollY() <= 0) {
            if (getScrollY() <= 0) {
                myScrollViewListener.onMyScrollTop(this, currentX, currentY, oldx, oldy);
                Log.i(TAG, "到达了顶部");
            }


//====================================================
            View view = (View) getChildAt(getChildCount() - 1);//获取ScrollView最后一个控件
//            int diff=(view.getBottom() - (getHeight() + getScrollY()));
            int diff = view.getBottom() - (getHeight() + getScrollY());

//            if(diff==0){
            if (diff == 0) {
                myScrollViewListener.onMyScrollBottom(this, currentX, currentY, oldx, oldy);
                Log.i(TAG, "到达了底部");
            }


            if (myScrollViewListener != null) {
                myScrollViewListener.onMyScrollChanged(this, currentX, currentY, oldx, oldy);
                if (!scrollerTaskRunning) {
                    startScrollerTask(this, currentX, currentY, oldx, oldy);
                }
            }
        }
    }


    private Runnable scrollerTask;

    private int initialPosition;

    private int newCheck = 50;

    private boolean scrollerTaskRunning = false;

    private void startScrollerTask(final MyScrollView scrollView, final int currentX, final int currentY, final int oldx, final int oldy) {
        if (!scrollerTaskRunning) {
            myScrollViewListener.onMyScrollStart(this, currentX, currentY, oldx, oldy);
            Log.i(TAG, "滚动開始");
        }
        scrollerTaskRunning = true;
        if (scrollerTask == null) {
            scrollerTask = new Runnable() {
                public void run() {
                    int newPosition = getScrollY();
                    if (initialPosition - newPosition == 0) {
                        if (myScrollViewListener != null) {
                            scrollerTaskRunning = false;
                            myScrollViewListener.onMyScrollStop(scrollView, currentX, currentY, oldx, oldy);
                            Log.i(TAG, "滚动结束");
                        }
                    } else {
                        startScrollerTask(scrollView, currentX, currentY, oldx, oldy);
                    }
                }
            };
        }


        initialPosition = getScrollY();

        postDelayed(scrollerTask, newCheck);

    }


    private MyScrollViewListener myScrollViewListener;


    public void setMyScrollViewListener(MyScrollViewListener myScrollViewListener) {
        this.myScrollViewListener = myScrollViewListener;
    }


}
