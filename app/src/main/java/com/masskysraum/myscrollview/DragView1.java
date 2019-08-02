package com.masskysraum.myscrollview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class DragView1 extends View {
    private final Paint mDeafultPaint;
    String TAG = "Gcs";

    Bitmap mBitmap;         // 图片
    RectF mBitmapRectF;     // 图片所在区域
    Matrix mBitmapMatrix;   // 控制图片的 matrix

    boolean canDrag = false;
    PointF lastPoint = new PointF(0, 0);

    public DragView1(Context context) {
        this(context, null);
    }

    public DragView1(Context context, AttributeSet attrs) {
        super(context, attrs);
        mDeafultPaint = new Paint();
        mDeafultPaint.setAntiAlias(true);
        mDeafultPaint.setColor(Color.BLUE);


        // 调整图片大小
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.outWidth = 960/2;
        options.outHeight = 800/2;

        mBitmap = BitmapFactory.decodeResource(this.getResources(), R.drawable.head_d, options);
        mBitmapRectF = new RectF(0,0,mBitmap.getWidth(), mBitmap.getHeight());
        mBitmapMatrix = new Matrix();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                // 判断按下位置是否包含在图片区域内
                if (mBitmapRectF.contains(event.getX(),event.getY()))  {
                    canDrag = true;
                    lastPoint.set(event.getX(),event.getY());
                }
                break;
            case MotionEvent.ACTION_UP:
                canDrag = false;
            case MotionEvent.ACTION_MOVE:
                if (canDrag) {
                    // 移动图片
                    mBitmapMatrix.postTranslate(event.getX() - lastPoint.x,event.getY() - lastPoint.y);

                    // 更新上一次点位置
                    lastPoint.set(event.getX(),event.getY());

                    // 更新图片区域
//                    mBitmapRectF = new RectF(0, 0, mBitmap.getWidth(), mBitmap.getHeight());
//                    mBitmapMatrix.mapRect(mBitmapRectF);

                    mBitmapRectF = new RectF(0,0,mBitmap.getWidth(),mBitmap.getHeight());
                    mBitmapMatrix.mapRect(mBitmapRectF);
                    invalidate();
                }
                break;
        }
        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawBitmap(mBitmap, mBitmapMatrix, mDeafultPaint);
    }
}
