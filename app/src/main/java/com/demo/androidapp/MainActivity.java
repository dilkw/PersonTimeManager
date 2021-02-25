package com.demo.androidapp;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.Toast;

import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private Date date1,date2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        activityMainBinding.loginOutBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                FragmentManager fragmentManager = getFragmentManager();
//                int count = fragmentManager.getBackStackEntryCount();
//                Log.d("imageView", "onClick:fragment数量 " + count);
//                for (int i = 0; i < count; ++i) {
//                    fragmentManager.popBackStack();
//                }
//                MyApplication.getApplication().signOut();
//                Log.d("imageView", "onClick:userName" + MyApplication.getApplication().getUSER_NAME());
//                MainActivity.this.finish();
//                Intent intent = new Intent(); //生成Intent对象
//                intent.setClass(MainActivity.this, MainActivity.class);
//                startActivity(intent);  //
//            }
//        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if (date1 != null) {
                date2 = new Date();
                if((date2.getTime()-date1.getTime()) < 1000) {
                    finish();
                    System.exit(0);
                    //onDestroy();
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

    //自定义返回键监听方法
    public void my_onBackPressed(boolean isExit) {
        if(isExit) {
            finish();
            onDestroy();
        }else {
            super.onBackPressed();
        }
    }

}
