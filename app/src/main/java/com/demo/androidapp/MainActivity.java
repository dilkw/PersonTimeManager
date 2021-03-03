package com.demo.androidapp;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.Toast;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private Date date1,date2;

    private Map<String,Object> dataMap = new HashMap<String,Object>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
}
