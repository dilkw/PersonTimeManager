package com.demo.androidapp.view;

import androidx.annotation.RequiresApi;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import android.content.res.ColorStateList;
import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.demo.androidapp.MyApplication;
import com.demo.androidapp.R;
import com.demo.androidapp.databinding.HomeFragmentBinding;
import com.demo.androidapp.model.Auth;
import com.demo.androidapp.util.DataSP;
import com.demo.androidapp.viewmodel.HomeViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class HomeFragment extends Fragment {

    private HomeViewModel mViewModel;

    private Auth auth;

    private HomeFragmentBinding homeFragmentBinding;

    private NavController navController;

    private FloatingActionButton floatingActionButton;

    private View.OnTouchListener onTouchListener;

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        Log.d("imageView", "onCreateView0: ");
        homeFragmentBinding = DataBindingUtil.inflate(inflater,R.layout.home_fragment,container,false);
        return homeFragmentBinding.getRoot();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        DataSP dataSP = new DataSP(getContext());
        Log.d("imageView", "onCreateView1: ");
        if(MyApplication.getApplication().getUSER_NAME().equals("userName")){
            Log.d("imageView", "onCreateView2: ");
            NavController navController = Navigation.findNavController(requireView());
            navController.navigate(R.id.action_homeFragment_to_loginFragment);
        }

        homeFragmentBinding.myFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("imageView", "onClick: 点击事件");
            }
        });

//        homeFragmentBinding.myFloatingActionButton.setImageTintMode();
//        navController = Navigation.findNavController(requireView());
//        NavigationUI.setupWithNavController(homeFragmentBinding.bottomNavigationView,navController);
        //Log.d("MyView",auth.toString());
    }

    @Override
    public void onResume() {
        super.onResume();
        if (auth == null)
        MyApplication.getApplication().getUSER_NAME();
    }
}
