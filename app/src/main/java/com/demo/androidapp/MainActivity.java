package com.demo.androidapp;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.app.AlarmManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.Toast;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private Date date1,date2;

    private Map<String,Object> dataMap = new HashMap<String,Object>();

    private FragmentManager fragmentManager;

    private NavHostFragment navHostFragment;

    private NavController controller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fragmentManager = getSupportFragmentManager();
        navHostFragment = (NavHostFragment)fragmentManager.findFragmentById(R.id.fragment);
        controller = navHostFragment.getNavController();
        //动态注册广播
//        IntentFilter intentFilter = new IntentFilter();
//        intentFilter.addAction("AlarmClock");
//        AlertReceiver alertReceiver = new AlertReceiver();
//        registerReceiver(alertReceiver, intentFilter);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if (date1 != null) {
                date2 = new Date();
                if((date2.getTime()-date1.getTime()) < 500) {
                    finish();
                    System.exit(0);
                }
                else {
                    date1 = null;
                    date2 = null;
                    return super.onKeyDown(keyCode, event);
                }
            }
            else {
                Toast.makeText(this,"双击返回键退出程序",Toast.LENGTH_SHORT).show();
                date1 = new Date();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);

    }

    //主页面(HomeFragment)定义返回键监听接口
    public interface HomeFragmentBackPressedListener{
        void homeFragmentBackPressedClickListener();
    }

    //从数据map中获取数据
    public Object getDataFromMapByKey(String key) {
        Object o = this.dataMap.get(key);
        this.dataMap.remove(key);
        return o;
    }

    //将数据存放到map
    public void putDataInToMap(String key,Object o) {
        this.dataMap.put(key,o);
    }

    @Override
    protected void onResume() {
        Intent intent = getIntent();
        String action = "";
        if (intent != null) {
            action = intent.getStringExtra("name");
            if (action != null) {
                switch (action) {
                    case "clock": {
                        Bundle bundle = new Bundle();
                        bundle.putLong("clockId",intent.getLongExtra("id",0));
                        controller.navigate(R.id.clockStartFragment,bundle);
                        break;
                    }
                    case "task": {
                        controller.navigate(R.id.add_task_fragment);
                        break;
                    }
                    default:{
                        break;
                    }
                }
            }
        }
        super.onResume();
    }
}
