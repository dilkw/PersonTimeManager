package com.demo.androidapp.view;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.demo.androidapp.R;
import com.demo.androidapp.databinding.UserinfoFragmentBinding;
import com.demo.androidapp.model.entity.User;
import com.demo.androidapp.viewmodel.UserInfoViewModel;

public class UserInfoFragment extends Fragment implements View.OnClickListener {

    private UserInfoViewModel userInfoViewModel;

    private UserinfoFragmentBinding userinfoFragmentBinding;

    private FragmentManager fragmentManager;

    private NavHostFragment navHostFragment;

    private NavController controller;

    private LiveData<User> userInfoLiveData;

    private AppBarConfiguration appBarConfiguration;

    public static UserInfoFragment newInstance() {
        return new UserInfoFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        userinfoFragmentBinding = DataBindingUtil.inflate(inflater,R.layout.userinfo_fragment,container,false);
        userInfoViewModel = new ViewModelProvider(this).get(UserInfoViewModel.class);
        View view = userinfoFragmentBinding.getRoot();
        Log.d("imageView", "onCreateView: userInfo_fragment " + view.getClass().getSimpleName());
        fragmentManager = requireActivity().getSupportFragmentManager();
        navHostFragment = (NavHostFragment)fragmentManager.findFragmentById(R.id.fragment);
        assert navHostFragment != null;
        controller = navHostFragment.getNavController();
        appBarConfiguration = new AppBarConfiguration.Builder(controller.getGraph()).build();
        NavigationUI.setupWithNavController(userinfoFragmentBinding.userInfoFragmentToolBar,controller,appBarConfiguration);
        return view;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d("imageView", "onCreateView: userInfo_fragment ");
        userInfoViewModel.getUserInfoLiveData();
    }

    public void setListener() {
        userinfoFragmentBinding.userInfoImageView.setOnClickListener(this);
        userinfoFragmentBinding.userInfoEditNameButton.setOnClickListener(this);
        userinfoFragmentBinding.userInfoResetEmailButton.setOnClickListener(this);
        userinfoFragmentBinding.userInfoCancellationButton.setOnClickListener(this);
    }

    @SuppressLint({"NonConstantResourceId", "ResourceAsColor"})
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.userInfoImageView: {
                Log.d("imageView", "onClick: 用户头像点击");
                break;
            }
            case R.id.userInfoEditNameButton: {
                Log.d("imageView", "onClick: 更改昵称按钮");
                break;
            }
            case R.id.userInfoResetEmailButton: {
                Log.d("imageView", "onClick: 更改邮箱按钮");
                break;
            }
            case R.id.userInfoCancellationButton: {
                Log.d("imageView", "onClick: 注销帐号按钮");
                break;
            }
            default:{
                Log.d("imageView", "onClick: ");
                break;
            }
        }
    }
}