package com.demo.androidapp.view.myView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

import com.demo.androidapp.R;

public class ClipImgView extends View {

    private static final String TAG =  "clipImgView_tag";
    private Paint mPaint1;
    private Paint mPaint2;
    private Path mPath;

    private float pathWidth;
    private float pathHeight;

    Bitmap originalBitmap = getDrawingCache();  //原图
    Bitmap clipBitmap;      //裁剪后图片

    public void setOriginalBitmapByUrl(String url) {
        if (url == null) return;
        this.originalBitmap = BitmapFactory.decodeFile(url);
        if (originalBitmap == null) {
            Log.d("imageView", "setOriginalBitmapByUrl: 图片为空");
        }
        Log.d("imageView", "setOriginalBitmapByUrl: 图片不为空");
        invalidate();
    }
    public void setOriginalBitmap(Bitmap originalBitmap) {
        this.originalBitmap = originalBitmap;
        if (this.originalBitmap == null) {
            Log.d("imageView", "setOriginalBitmap: 图片为空");
        }
        Log.d("imageView", "setOriginalBitmap: 图片不为空");
        invalidate();
    }

    public ClipImgView(Context context) {
        this(context,null);
    }

    public ClipImgView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public ClipImgView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mPath = new Path();
        if (mPaint1 == null) {
            mPaint2 = new Paint();
            mPaint1 = new Paint();
            mPaint1.setColor(Color.parseColor("#E91E63"));
            mPaint1.setStrokeWidth(2);
        }
        //获取自定义属性。
        @SuppressLint({"Recycle", "CustomViewStyleable"})
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.clipImgView);
        String src = ta.getString(R.styleable.clipImgView_clipImgSrc);
        if (src == null) {
            clipBitmap = BitmapFactory.decodeResource(getResources(),R.drawable.head);
        }else {
            clipBitmap = BitmapFactory.decodeFile(src);
        }
        ta.recycle();
    }

    @SuppressLint("DrawAllocation")
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        pathWidth = 300;
        pathHeight = 300;
        setMeasuredDimension(width,height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (originalBitmap != null) {
            Log.d("imageView", "onDraw: originalBitmap不为空");
            canvas.drawBitmap(originalBitmap, 0, 0, mPaint2);
            mPath.addCircle(pathWidth, pathHeight, pathWidth - 10, Path.Direction.CW);
            canvas.clipPath(mPath);
        }else {
            Log.d("imageView", "onDraw: originalBitmap为空");
        }
    }

    private void clipImg(Canvas canvas) {
        canvas.save();
        canvas.drawBitmap(clipBitmap,0,0,mPaint2);
        canvas.restore();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN : {
                Log.d(TAG, "onTouchEvent: ACTION_DOWN");

                break;
            }
            case MotionEvent.ACTION_MOVE : {
                Log.d(TAG, "onTouchEvent: ACTION_MOVE");
                pathWidth = event.getX();
                pathHeight = event.getY();
                invalidate();
                break;
            }
            case MotionEvent.ACTION_UP : {
                Log.d(TAG, "onTouchEvent: ACTION_UP");
                break;
            }
        }
        return super.onTouchEvent(event);
    }
}
