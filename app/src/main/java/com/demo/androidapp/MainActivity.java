package com.demo.androidapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.ActivityChooserView;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.NavHostController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.annotation.SuppressLint;
import android.app.FragmentManager;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import java.util.Date;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private Date date1,date2;

    private NavController controller;

    private AppBarConfiguration appBarConfiguration;

    @Override
    public boolean onSupportNavigateUp() {
        Log.d("imageView", "onSupportNavigateUp: 侧滑布局");
        final InputMethodManager inputMethodManager = (InputMethodManager)this.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(findViewById(R.id.fragment).getWindowToken(),0);
        return NavigationUI.navigateUp(controller, appBarConfiguration) || super.onSupportNavigateUp();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        NavHostFragment navHostFragment = (NavHostFragment)getSupportFragmentManager().findFragmentById(R.id.fragment);
        assert navHostFragment != null;
        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        controller = navHostFragment.getNavController();
        appBarConfiguration = new AppBarConfiguration.Builder(controller.getGraph()).setDrawerLayout(drawerLayout).build();
        NavigationView navView = findViewById(R.id.nav_view);
        NavigationUI.setupActionBarWithNavController(this,controller,appBarConfiguration);
        NavigationUI.setupWithNavController(navView,controller);
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
