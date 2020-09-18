package com.demo.androidapp.view.myView;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.res.Resources;
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

import androidx.annotation.DrawableRes;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.demo.androidapp.R;

public class MyImageView extends View {

    private Paint mPaint;
    private Bitmap bitmap;

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
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
        super(context);
        Log.d("imageView","MyImageView(Context context)");
        bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.head);
    }

    //如果直接写在xml文件中会调用二个参数的构造函数被调用。
    public MyImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        Log.d("imageView","MyImageView(Context context, @Nullable AttributeSet attrs)");
        bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.head);
    }

    public MyImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
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
        int width = dp2pix();
        setMeasuredDimension(width,width);
    }

    //将dp值转化成px像素
    private int dp2pix() {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 120, Resources.getSystem().getDisplayMetrics());
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
        Shader shader = new BitmapShader(bitmap,Shader.TileMode.CLAMP,Shader.TileMode.CLAMP);
        mPaint.setShader(shader);
        canvas.drawCircle(cx,cx,cx,mPaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("选择照片");

        }
        return super.onTouchEvent(event);
    }
}
