package com.masskysraum.myscrollview;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.Scroller;

import java.time.chrono.HijrahEra;

public class SlidingPanel extends RelativeLayout {
    private Context context;
    private FrameLayout leftMenu;
    private FrameLayout middleMenu;
    private FrameLayout rightMenu;
    private FrameLayout middleMask;
    private Scroller mScroller;
    public final int LEFT_ID = 0xaabbcc;
    public final int MIDEELE_ID = 0xaaccbb;
    public final int RIGHT_ID = 0xccbbaa;

    private boolean isSlideCompete;
    private boolean isHorizontalScroll;

    private Point point = new Point();
    private static final int SLIDE_SLOP = 20;

    public SlidingPanel(Context context) {
        super(context);
        initView(context);
    }

    public SlidingPanel(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    private void initView(Context context) {

        this.context = context;
        mScroller = new Scroller(context, new DecelerateInterpolator());
        leftMenu = new FrameLayout(context);//左抽屉
        middleMenu = new FrameLayout(context);//中间抽屉
        rightMenu = new FrameLayout(context);//右边菜单
        middleMask = new FrameLayout(context);
        leftMenu.setBackgroundColor(Color.RED);
        middleMenu.setBackgroundColor(Color.GREEN);
        rightMenu.setBackgroundColor(Color.RED);
        middleMask.setBackgroundColor(0x88000000);

        addView(leftMenu);
        addView(middleMenu);
        addView(rightMenu);
        addView(middleMask);

        middleMask.setAlpha(0);
    }

    public float onMiddleMask() {
        return middleMask.getAlpha();
    }

    /**
     * *invalidate->computeScroll->scrollTo->invalidate->draw->Scroller->invalidate
     * *getScrollX():当前视图相对于屏幕原点在x轴上的偏移量
     * *getScrollY():当前视图相对于屏幕原点在y轴上的偏移量
     *
     * @param x
     * @param y
     */
    @Override
    public void scrollTo(int x, int y) {
        super.scrollTo(x, y);
        onMiddleMask();
        // Log.e("getScrollX","getScrollX="+getScrollX());//可以是负值
        int curX = Math.abs(getScrollX());
        float scale = curX / (float) leftMenu.getMeasuredWidth();
        middleMask.setAlpha(scale);
    }

    /**
     * onMeasure->setMeasuredDimension
     *
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        middleMenu.measure(widthMeasureSpec, heightMeasureSpec);
        middleMask.measure(widthMeasureSpec, heightMeasureSpec);
        int realWidth = MeasureSpec.getSize(widthMeasureSpec);
        int tempWidthMeasure = MeasureSpec.makeMeasureSpec((int) (realWidth * 0.8f), MeasureSpec.EXACTLY);
        leftMenu.measure(tempWidthMeasure, heightMeasureSpec);
        rightMenu.measure(tempWidthMeasure, heightMeasureSpec);
    }

    /**
     * @param changed
     * @param l
     * @param t
     * @param r
     * @param b
     */
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {//0,0,720,1118
        super.onLayout(changed, l, t, r, b);
        middleMenu.layout(l, t, r, b);
        middleMask.layout(l, t, r, b);

        int left_menu_left = leftMenu.getMeasuredWidth();
        leftMenu.layout(l - left_menu_left, t, r, b);


        rightMenu.layout(
                l + middleMenu.getMeasuredWidth(),
                t,
                l + middleMenu.getMeasuredWidth()
                        + rightMenu.getMeasuredWidth(), b
        );
    }


    /*   *getScrollX():当前视图相对于屏幕原点在x轴上的偏移量
     *   *getX():触摸点相对于其所在视图原点在x轴上的偏移量
     *   *getX() + getScrollX()-> 相当于View上一个触摸点相对于屏幕原点在x轴上的偏移量，
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (!isSlideCompete) {
            handleSlideEvent(ev);
            return true;
        }
        if (isHorizontalScroll) {
            switch (ev.getActionMasked()) {
                case MotionEvent.ACTION_MOVE:
                    int curScrollX = getScrollX();
                    int dis_x = (int) (ev.getX() - point.x);
                    //滑动方向和滚动滚动条方向相反,因此dis_x必须取负值
                    int expectX = -dis_x + curScrollX;

                    if (dis_x > 0) {
                        Log.e("I", "Right-Slide,Left-Scroll");//向右滑动，向左滚动
                    } else {
                        Log.e("I", "Left-Slide,Right-Scroll");
                    }

                    Log.e("I", "ScrollX=" + curScrollX + " , X=" + ev.getX() + " , dis_x=" + dis_x);
                    //规定expectX的变化范围
                    Log.e("I", "leftMenu.getMeasuredWidth()=" + leftMenu.getMeasuredWidth()
                            + " , rightMenu.getMeasuredWidth()=" + rightMenu.getMeasuredWidth());

                    int finalX = Math.max(-leftMenu.getMeasuredWidth(), Math.min(expectX, rightMenu.getMeasuredWidth()));
                    //这句话让滑动最多滑动leftMenu.getMeasuredWidth()大小的距离；防止出现白屏；

                    scrollTo(finalX, 0);
                    point.x = (int) ev.getX();//更新，保证滑动平滑
                    break;

                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                    curScrollX = getScrollX();
                    int half_left = leftMenu.getMeasuredWidth() >> 1;
                    Log.e("I", "leftMenu.getMeasuredWidth() >> 1=" +half_left + "curScrollX = " + curScrollX);


                    if (Math.abs(curScrollX) > half_left) {
                        if (curScrollX < 0) {//滑满leftMenu.getMeasuredWidth()
                            mScroller.startScroll(curScrollX, 0,
                                    -leftMenu.getMeasuredWidth() - curScrollX, 0,
                                    200);
                        } else {
                            mScroller.startScroll(curScrollX, 0,
                                    leftMenu.getMeasuredWidth() - curScrollX, 0,
                                    200);
                        }
                    } else {
                        mScroller.startScroll(curScrollX, 0, -curScrollX, 0, 200);
                    }
                    invalidate();
                    isHorizontalScroll = false;
                    isSlideCompete = false;
                    break;
            }
        } else {
            switch (ev.getActionMasked()) {
                case MotionEvent.ACTION_UP:
                    isHorizontalScroll = false;
                    isSlideCompete = false;
                    break;
                default:
                    break;
            }
        }

        return super.dispatchTouchEvent(ev);
    }

    /**
     *     * 通过invalidate操纵,此方法通过draw方法调用
     *     *invalidate->computeScroll->scrollTo->invalidate->draw->Scroller->invalidate
     *     *scrollTo(tempX, 0)->tempX > 0 向右滑动，
     */
    @Override
    public void computeScroll() {
        super.computeScroll();
        if (!mScroller.computeScrollOffset()) {
            //计算currX，currY,并检测是否已完成“滚动”
            return;
        }
        int tempX = mScroller.getCurrX();
        scrollTo(tempX, 0); //会重复调用invalidate
    }


    private void handleSlideEvent(MotionEvent ev) {
        switch (ev.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                point.x = (int) ev.getX();
                point.y = (int) ev.getY();
                super.dispatchTouchEvent(ev);
                break;

            case MotionEvent.ACTION_MOVE:
                int dX = Math.abs((int) ev.getX() - point.x);
                int dY = Math.abs((int) ev.getY() - point.y);
                if (dX >= SLIDE_SLOP && dX > dY) { // 左右滑动
                    isHorizontalScroll = true;
                    isSlideCompete = true;
                    point.x = (int) ev.getX();
                    point.y = (int) ev.getY();
                } else if (dY >= SLIDE_SLOP && dY > dX) { // 上下滑动
                    isHorizontalScroll = false;
                    isSlideCompete = true;
                    point.x = (int) ev.getX();
                    point.y = (int) ev.getY();
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_OUTSIDE:
            case MotionEvent.ACTION_CANCEL:
                super.dispatchTouchEvent(ev);
                isHorizontalScroll = false;
                isSlideCompete = false;
                break;
        }
    }

}
