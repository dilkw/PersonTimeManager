package com.demo.androidapp.viewmodel;

import android.annotation.SuppressLint;
import android.app.Application;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.demo.androidapp.MyApplication;
import com.demo.androidapp.R;
import com.demo.androidapp.model.Auth;
import com.demo.androidapp.model.common.RCodeEnum;
import com.demo.androidapp.model.common.ReturnData;
import com.demo.androidapp.model.entity.User;
import com.demo.androidapp.model.returnObject.LoginAndRegisterReturn;
import com.demo.androidapp.repository.AuthRepository;
import com.google.android.material.textfield.TextInputLayout;

public class LoginViewModel extends AndroidViewModel {

    public final int registerFragmentId = R.id.action_loginFragment_to_registerFragment;

    private MutableLiveData<User> userLiveData;

    private AuthRepository authRepository;

    public LoginViewModel(@NonNull Application application) {
        super(application);
        Log.d("imageView", "LoginViewModel:-=-=-=-=-= " + application.getClass().getName());
        this.userLiveData = new MutableLiveData<User>();
        this.userLiveData.setValue(new User());
        this.authRepository = new AuthRepository(application);
    }
    // TODO: Implement the ViewModel


    public MutableLiveData<ReturnData<LoginAndRegisterReturn>> getReturnLiveData() {
        return authRepository.getReturnDataLiveData();
    }

    public MutableLiveData<User> getUserLiveData() {
        return userLiveData;
    }

    //登录方法
    public LiveData<ReturnData<User>> login() {
        Log.d("imageView","ViewModel-----login");
        StringBuilder token = new StringBuilder();
        Log.d("imageView",userLiveData.getValue().getName() + userLiveData.getValue().getPassword());
        return this.authRepository.login(userLiveData.getValue().getName(),userLiveData.getValue().getPassword());
    }

    //跳转注册页面方法
    public void jumpToRegisterFragment(View view) {
        Log.d("imageView","跳转注册页面");
        NavController navController = Navigation.findNavController(view);
        navController.navigate(registerFragmentId);
    }

    //登陆成功跳转主页面方法
    public void jumpToHomeFragment(View view) {
        Log.d("imageView","跳转主页面");
        Bundle bundle = new Bundle();
        bundle.putBoolean("isLogin",true);
        NavController navController = Navigation.findNavController(view);
        navController.navigate(R.id.action_loginFragment_to_homeFragment,bundle);        //返回上一级
    }

    //忘记密码跳转重置密码页面方法
    public void jumpToRetrievePasswordFragment(View view) {
        Log.d("imageView","跳转重置密码页面");
        NavController navController = Navigation.findNavController(view);
        navController.navigate(R.id.retrievePasswordFragment);
    }


    @Override
    protected void onCleared() {
        super.onCleared();
        Log.d("imageView", "LoginViewModel------onCleared: ");
    }

}
