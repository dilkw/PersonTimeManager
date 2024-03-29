package com.demo.androidapp.view.myView;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Shader;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.DrawableRes;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.bumptech.glide.Glide;
import com.demo.androidapp.R;

public class MyImageView extends View {

    private Paint mPaint;
    private Bitmap bitmap;

    private String myImgViewSrc;

    public String getMyImgViewSrc() {
        return myImgViewSrc;
    }

    public void setMyImgViewSrc(String myImgViewSrc) {
        this.myImgViewSrc = myImgViewSrc;
    }

    public void setBitmap(Bitmap bitmap) {
        if (bitmap == null) {
            Log.d("imageView", "setBitmap: 为空");
            return ;
        }
        Log.d("imageView", "setBitmap: 不为空");
        this.bitmap = bitmap;
        invalidate();
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setResId(@DrawableRes int resId) {
        Log.d("imageView","setResId");
        bitmap = BitmapFactory.decodeResource(getResources(),resId);
        invalidate();   //调用onDraw()方法
    }

    //利用代码直接new 布局时会调用一个参数的构造函数，
    public MyImageView(Context context) {
        this(context,null);
    }

    //如果直接写在xml文件中会调用二个参数的构造函数被调用。
    public MyImageView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    @SuppressLint("CheckResult")
    public MyImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        Log.d("imageView","MyImageView(Context context)");
        if (Glide.with(getContext()).load(getResources().getString(R.string.defaultImgUrl)).load(bitmap) == null) {
            bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.head);
        }
    }

    //四个参数的构造函数通常由我们自己主动调用.
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public MyImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    private void init() {
        mPaint = new Paint();
        Log.d("imageView","init()");
        bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.head);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        Log.d("imageView","onMeasure()");
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        //if (MeasureSpec.getMode(widthMeasureSpec) == MeasureSpec.AT_MOST)
        setMeasuredDimension(width,height);
    }

    //将dp值转化成px像素
    private int dp2pix(int px) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, px, Resources.getSystem().getDisplayMetrics());
    }

    //单位转换sp转px
    private int SpToPx(int sp) {
        return (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,sp,getResources().getDisplayMetrics());
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Log.d("imageView","onDraw()");
        int cx = getWidth() / 2;
        if(bitmap == null) {
            Log.d("imageView","图片为空");
            bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.head);
        }
        if(mPaint == null) {
            mPaint = new Paint();
        }
        @SuppressLint("DrawAllocation")
        Shader shader = new BitmapShader(bitmap,Shader.TileMode.CLAMP,Shader.TileMode.CLAMP);
        mPaint.setShader(shader);
        canvas.drawCircle(cx,cx,cx,mPaint);
    }
}
