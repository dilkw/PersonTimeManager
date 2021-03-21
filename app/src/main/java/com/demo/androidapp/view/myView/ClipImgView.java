package com.demo.androidapp.view.myView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Xfermode;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.demo.androidapp.R;

public class ClipImgView extends View {

    private static final String TAG =  "clipImgView_tag";
    private Paint mPaint1;
    private Paint mPaint2;
    private Paint mPaint3;
    private Path mPath;

    private float pathWidth;
    private float pathHeight;

    private float imgLeft = 0;
    private float imgTop = 0;

    private float imgLeftX = 0;
    private float imgTopY = 0;
    private float imgRightX = 0;
    private float imgBottomY = 0;

    private Bitmap originalBitmap = getDrawingCache();  //原图
    private Bitmap maskBitmap;      //裁剪后图片
    private Bitmap clipBitmap;      //裁剪后图片

    private Xfermode xfermode;
    private BlurMaskFilter blurMaskFilter;

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
        }else {
            Log.d("imageView", "setOriginalBitmap: 图片不为空");
            maskBitmap = Bitmap.createBitmap(this.originalBitmap.getWidth(), originalBitmap.getHeight(), Bitmap.Config.ARGB_8888);
            invalidate();
        }
    }

    public ClipImgView(Context context) {
        this(context,null);
    }

    public ClipImgView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public ClipImgView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.setFocusable(true);
        this.setClickable(true);
        this.setFocusableInTouchMode(true);
        init();
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
    private void init() {
        mPath = new Path();
        if (mPaint1 == null) {
            mPaint1 = new Paint(Paint.ANTI_ALIAS_FLAG);
            mPaint1.setColor(Color.parseColor("#E91E63"));
            mPaint1.setStrokeWidth(8);
            mPaint1.setStrokeCap(Paint.Cap.ROUND);
            mPaint1.setStyle(Paint.Style.FILL_AND_STROKE);
            mPaint1.setAntiAlias(true);
            xfermode = new PorterDuffXfermode(PorterDuff.Mode.CLEAR);
            blurMaskFilter = new BlurMaskFilter(300, BlurMaskFilter.Blur.OUTER);
        }
        if (mPaint3 == null) {
            mPaint3 = new Paint(Paint.ANTI_ALIAS_FLAG);
            mPaint3.setColor(Color.parseColor("#E91E63"));
            mPaint3.setStrokeWidth(8);
            mPaint3.setStyle(Paint.Style.FILL);
            mPaint3.setAlpha(100);
            mPaint3.setAntiAlias(true);
        }
        if (mPaint2 == null) {
            mPaint2 = new Paint();
            mPaint2.setShadowLayer(0.2f,0, 0, Color.parseColor("#E91E63"));
            mPaint2.setAntiAlias(true);
        }
    }

    @SuppressLint("DrawAllocation")
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        pathWidth = width / 2f;
        pathHeight = height / 2f;
        imgLeftX = -(width - pathWidth - 400);
        imgRightX = pathWidth - 400;
        imgTopY = -(height - pathHeight - 400);
        imgBottomY = pathHeight - 400;
        setMeasuredDimension(width,height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (originalBitmap != null) {
            canvas.drawBitmap(originalBitmap, imgLeft, imgTop, mPaint2);
            int saved = canvas.saveLayer(null, null, Canvas.ALL_SAVE_FLAG);
            canvas.drawRect(0,0,getWidth(),getHeight(),mPaint3);
            mPaint1.setXfermode(xfermode);
            canvas.drawCircle(getWidth() / 2f , getHeight() / 2f ,400,mPaint1);
            mPaint1.setXfermode(null);
            canvas.restoreToCount(saved);
        }
    }

    private void clipImg(Canvas canvas) {
        canvas.save();
        canvas.drawBitmap(clipBitmap,0,0,mPaint2);
        canvas.restore();
    }

    float downX = 0;
    float downY = 0;
    float moveX = 0;
    float moveY = 0;
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN : {
                Log.d(TAG, "onTouchEvent: ACTION_DOWN");
                downX = event.getX();
                downY = event.getY();;
                break;
            }
            case MotionEvent.ACTION_MOVE : {
                Log.d(TAG, "onTouchEvent: ACTION_MOVE");
                moveX = event.getX() - downX;
                moveY = event.getY() - downY;
                imgLeft += (imgLeft >= imgRightX && moveX > 0) || (imgLeft <= imgLeftX && moveX < 0) ?  0 : moveX;
                imgTop += (imgTop >= imgBottomY && moveY > 0) || (imgTop <= imgTopY && moveY < 0) ? 0 : moveY;
                downX = event.getX();
                downY = event.getY();
                Log.d(TAG, "onTouchEvent: ACTION_MOVE getX" + getX());
                Log.d(TAG, "onTouchEvent: ACTION_MOVE getY" + getY());
                invalidate();
                break;
            }
            case MotionEvent.ACTION_UP : {
                Log.d(TAG, "onTouchEvent: ACTION_UP");
                downX = 0;
                downY = 0;
                break;
            }
        }
        return super.onTouchEvent(event);
    }
}
