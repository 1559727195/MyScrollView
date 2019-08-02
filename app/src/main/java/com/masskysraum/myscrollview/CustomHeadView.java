package com.masskysraum.myscrollview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

public class CustomHeadView extends View {

    private Bitmap DBitmap;

    private Bitmap SBitmap;

    private Paint mPaint;

    private PorterDuffXfermode xfermode;

    public CustomHeadView(Context context) {
        this(context, null);
    }

    public CustomHeadView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomHeadView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init(){
        mPaint = new Paint();
        // 这个图片是正常的头像
        DBitmap = BitmapFactory.decodeResource(getResources(),R.drawable.head_d,null);
        // 这个图片是中间一个圆，四个角透明的图片
        SBitmap = BitmapFactory.decodeResource(getResources(),R.drawable.head_s,null);

        //xfermode = new PorterDuffXfermode(PorterDuff.Mode.SRC_IN);
        xfermode = new PorterDuffXfermode(PorterDuff.Mode.SRC_IN);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int width = getWidth();
        int height = getHeight();
        int layerId = canvas.saveLayer(0, 0, width,height,null, Canvas.ALL_SAVE_FLAG);

        canvas.drawBitmap(SBitmap,0,0,mPaint);
        mPaint.setXfermode(xfermode);
        canvas.drawBitmap(DBitmap,0,0,mPaint);

        mPaint.setXfermode(null);
        canvas.restoreToCount(layerId);
    }
}
