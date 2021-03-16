package com.demo.androidapp.view.myView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.demo.androidapp.util.DateTimeUtil;

public class ClockCountDown extends View {

    private Paint mPaint1;

    private Paint mPaint2;

    private Paint mPaint3;

    private float progress = 0;

    private double multiple = 0;        //角度倍数

    private int circleX = 0, circleY = 0;

    private long second = 0;

    public float getProgress() {
        return progress;
    }

    public void setProgress(float progress) {
        this.progress = progress;
        invalidate();
    }

    public long getSecond() {
        return second;
    }

    public void setSecond(long second) {
        this.second = second;
    }

    public void setMultiple(double multiple) {
        this.multiple = multiple;
    }

    public ClockCountDown(Context context) {
        this(context,null);
    }

    public ClockCountDown(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public ClockCountDown(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        if (mPaint2 == null) {
            mPaint2 = new Paint();
            mPaint2.setColor(Color.parseColor("#E91E63"));
            mPaint2.setStyle(Paint.Style.STROKE);
            mPaint2.setStrokeCap(Paint.Cap.ROUND);
            mPaint2.setAntiAlias(true);
            mPaint2.setStrokeWidth(20);
            mPaint2.setTextSize(32);
            mPaint2.setTextAlign(Paint.Align.CENTER);
        }
        if (mPaint1 == null) {
            mPaint1 = new Paint();
            mPaint1.setColor(Color.parseColor("#E91E63"));
            mPaint1.setStyle(Paint.Style.STROKE);
            mPaint1.setStrokeCap(Paint.Cap.ROUND);
            mPaint1.setAntiAlias(true);
            mPaint1.setStrokeWidth(5);
            mPaint1.setTextAlign(Paint.Align.CENTER);
        }
        if (mPaint3 == null) {
            mPaint3 = new Paint();
            mPaint3.setColor(Color.parseColor("#E91E63"));
            mPaint3.setStyle(Paint.Style.FILL);
            mPaint3.setStrokeCap(Paint.Cap.ROUND);
            mPaint3.setTextSize(70);
            mPaint3.setAntiAlias(true);
            mPaint3.setStrokeWidth(2);
            mPaint3.setTextAlign(Paint.Align.CENTER);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        circleX = MeasureSpec.getSize(widthMeasureSpec) /  2;
        circleY = MeasureSpec.getSize(heightMeasureSpec) / 2;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        @SuppressLint("DrawAllocation") RectF rectF = new RectF(20,20,getWidth()-20,getHeight()-20);
        @SuppressLint("DrawAllocation") RectF rectFPath1 = new RectF(10,10,getWidth()-10,getHeight()-10);
        @SuppressLint("DrawAllocation") RectF rectFPath2 = new RectF(30,30,getWidth()-30,getHeight()-30);
        canvas.drawArc(rectFPath1,0,360,false,mPaint1);
        canvas.drawArc(rectFPath2,0,360,false,mPaint1);
        canvas.drawArc(rectF,-90,(float) (progress * multiple),false,mPaint2);
        canvas.drawText(DateTimeUtil.secondToHMS((int)progress),getWidth()/2,getWidth()/2,mPaint3);
        Log.d("imageView", "onDraw: progress" + progress);
        Log.d("imageView", "onDraw: multiple" + multiple);
        Log.d("imageView", "onDraw progress * multiple: " + progress * multiple);
    }
}
