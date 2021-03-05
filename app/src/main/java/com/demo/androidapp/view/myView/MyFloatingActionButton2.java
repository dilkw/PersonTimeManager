package com.demo.androidapp.view.myView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MyFloatingActionButton2 extends FloatingActionButton {

    //父级控件（布局）顶、底部Y坐标值
    private int parentTopY;
    private int parentBottomY;
    //父级控件（布局）左、右部X坐标值
    private int parentLeftX;
    private int parentRightX;

    private MyOnClickListener myOnClickListener;

    public void setMyOnClickListener(MyOnClickListener myOnClickListener) {
        this.myOnClickListener = myOnClickListener;
    }

    public MyFloatingActionButton2(@NonNull Context context) {
        this(context,null);
        Log.d("imageView", "构造1:");
    }

    public MyFloatingActionButton2(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
        Log.d("imageView", "构造2:");
    }

    public MyFloatingActionButton2(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        Log.d("imageView", "构造3:");
        //init();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        init();
        super.onDraw(canvas);
    }

    private void init(){
        //statusBarHeight = Resources.getSystem().getDimensionPixelSize(Resources.getSystem().getIdentifier("status_bar_height", "dimen", "android"));
        parentTopY = ((ViewGroup)getParent()).getTop();
        parentBottomY = ((ViewGroup)getParent()).getBottom();
        parentLeftX = ((ViewGroup)getParent()).getLeft();
        parentRightX = ((ViewGroup)getParent()).getRight();
    }

    private float actionDownX = 0;
    private float actionDownY = 0;
    private float downX = 0;
    private float downY = 0;
    private boolean isDrag = false;
    float moveX;
    float moveY;
    @SuppressLint({"ClickableViewAccessibility", "Recycle"})
    @Override
    public boolean onTouchEvent(@NonNull MotionEvent ev) {
        Log.d("imageView","状态栏高度：" + ((ViewGroup)getParent()).getBottom());
        Log.d("imageView", "onTouchEvent: 触摸事件");
        getParent().requestDisallowInterceptTouchEvent(true);
        Log.d("imageView","" + getPivotX());
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                Log.d("imageView","ACTION_DOWN");
                actionDownX = ev.getRawX();
                actionDownY = ev.getRawY();
                downX = ev.getRawX();
                downY = ev.getRawY();
                break;
            }
            case MotionEvent.ACTION_MOVE: {
                Log.d("imageView","ACTION_MOVE");
                isDrag = true;
                setPressed(false);
                moveX = getX() + (ev.getRawX() - actionDownX);
                moveY = getY() + (ev.getRawY() - actionDownY);
                moveX = moveX >= 0 ? moveX : 0;
                moveY = moveY >= 0 ? moveY : 0;
                moveX = moveX <= parentRightX - getWidth() ? moveX : parentRightX - getWidth();
                moveY = moveY <= parentBottomY -getHeight() ? moveY : parentBottomY -getHeight();
                setX(moveX);
                setY(moveY);
                actionDownX = ev.getRawX();
                actionDownY = ev.getRawY();
//                break;
                return true;
            }
            case MotionEvent.ACTION_UP:{
                //真机调试容错判断（允许误差范围为200（X和Y方向的移动距离））
                Log.d("imageView", "onTouchEvent: " + Math.abs(ev.getRawX() - downX) + "===" + Math.abs(ev.getRawX() - downY));
                if ((Math.abs(ev.getRawX() - downX)) < 200
                        &&(Math.abs(ev.getRawY() - downY)) < 200){
                    Log.d("imageView","点击");
                    isDrag = false;
                }
                Log.d("imageView","ACTION_UP");
                if (isDrag) {
                    Log.d("imageView","isDrag");
                    isDrag = false;
                    break;
                }
                else {
                    Log.d("imageView","not isDrag");
                    performClick();
                }
                return true;
            }
            case MotionEvent.ACTION_CANCEL:{
                Log.d("imageView", "onTouchEvent: case MotionEvent.ACTION_CANCEL:");
                break;
            }
        }
        return super.onTouchEvent(ev);
    }

    public interface MyOnClickListener{
        void onClick();
    }
}
