package com.demo.androidapp.view.myView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MyFloatingActionButton extends FloatingActionButton {

    //父级控件（布局）顶、底部Y坐标值
    private int parentTopY;
    private int parentBottomY;
    //父级控件（布局）左、右部X坐标值
    private int parentLeftX;
    private int parentRightX;

    public MyFloatingActionButton(@NonNull Context context) {
        this(context,null);
    }

    public MyFloatingActionButton(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public MyFloatingActionButton(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
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
    private boolean isDrag = false;
    float moveX;
    float moveY;
    @SuppressLint("ClickableViewAccessibility")
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
                break;
            }
            case MotionEvent.ACTION_UP:{
                Log.d("imageView","ACTION_UP");
                if (isDrag) {
                    isDrag = false;
                    //真机调试容错判断（允许误差范围为200（X和Y方向的移动距离））
                    if ((Math.abs(moveX - actionDownX)) < 200
                        &&(Math.abs(moveY - actionDownY)) < 200){
                        break;
                    }
                    return true;
                }
                break;
            }
            case MotionEvent.ACTION_CANCEL:{
                Log.d("imageView", "onTouchEvent: case MotionEvent.ACTION_CANCEL:");
                break;
            }
        }
        return super.onTouchEvent(ev);
    }
}
