package com.demo.androidapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.content.Context;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.demo.androidapp.view.ActiveFragment;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.zip.Inflater;

public class MainActivity extends AppCompatActivity {

    private Date date1,date2;

    NavController controller;

    private List<FragmentTouchListener> fragmentTouchListenerList = new ArrayList<>();

    @Override
    public boolean onSupportNavigateUp() {
        final InputMethodManager inputMethodManager = (InputMethodManager)this.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(findViewById(R.id.fragment).getWindowToken(),0);
        NavController controller = Navigation.findNavController(this,R.id.fragment);
        return controller.navigateUp();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        controller = Navigation.findNavController(this,R.id.fragment);
        NavigationUI.setupActionBarWithNavController(this,controller);
//        NavigationUI.setupWithNavController();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            Toast.makeText(this,"双击返回键退出程序",Toast.LENGTH_SHORT).show();
            if (date1 != null) {
                date2 = new Date();
                if((date2.getTime()-date1.getTime()) < 1000) {
                    finish();
                    System.exit(0);
                    //onDestroy();
                }
                else {
                    date1 = date2;
                    date2 = null;
                }
            }
            else {
                date1 = new Date();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);

    }

    //自定义返回键监听方法
    public void my_onBackPressed(boolean isExit) {
        if(isExit) {
            finish();
            onDestroy();
        }else {
            super.onBackPressed();
        }
    }

//    @Override
//    public boolean onTouchEvent(MotionEvent event) {
//        return super.onTouchEvent(event);
//    }

//    @Override
//    public boolean dispatchTouchEvent(MotionEvent ev) {
//        for (FragmentTouchListener listener : fragmentTouchListenerList) {
//            listener.fragmentTouchEvent(ev);
//        }
//        return super.dispatchTouchEvent(ev);
//    }

    public interface FragmentTouchListener{
        boolean fragmentTouchEvent(MotionEvent event);
    }

    public void addFragmentTouchListener(FragmentTouchListener fragmentTouchListener) {
        fragmentTouchListenerList.add(fragmentTouchListener);
    }

    public void rmFragmentTouchListener(FragmentTouchListener fragmentTouchListener) {
        fragmentTouchListenerList.remove(fragmentTouchListener);
    }
}
