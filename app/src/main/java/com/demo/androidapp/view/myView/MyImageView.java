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
        this.bitmap = BitmapFactory.decodeResource(getResources(),resId);
    }

    public MyImageView(Context context) {
        super(context);
        this.bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.head);
    }

    public MyImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.head);
    }

    public MyImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public MyImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    private void init() {
        mPaint = new Paint();
        this.bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.head);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
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
        int cx = getWidth() / 2;
        if(this.bitmap == null) {
            Log.d("imageView","图片为空");
            setResId(R.drawable.head);
        }
        if(mPaint == null) {
            mPaint = new Paint();
        }
        Shader shader = new BitmapShader(this.bitmap,Shader.TileMode.REPEAT,Shader.TileMode.REPEAT);
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
