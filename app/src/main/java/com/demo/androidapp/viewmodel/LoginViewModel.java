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
import com.demo.androidapp.model.returnObject.LoginAndRegisterReturn;
import com.demo.androidapp.repository.AuthRepository;
import com.google.android.material.textfield.TextInputLayout;

public class LoginViewModel extends AndroidViewModel {

    public final int registerFragmentId = R.id.action_loginFragment_to_registerFragment;

    private MutableLiveData<Auth> authLiveData;

    private AuthRepository authRepository;

    public LoginViewModel(@NonNull Application application) {
        super(application);
        this.authLiveData = new MutableLiveData<Auth>();
        this.authLiveData.setValue(new Auth());
        this.authRepository = new AuthRepository(application);
    }
    // TODO: Implement the ViewModel


    public MutableLiveData<ReturnData<LoginAndRegisterReturn>> getReturnLiveData() {
        return authRepository.getReturnDataLiveData();
    }

    public MutableLiveData<Auth> getAuthLiveData() {
        return authLiveData;
    }

    //登录方法
    public void login() {
        Log.d("imageView","ViewModel-----login");
        StringBuilder token = new StringBuilder();
        Log.d("imageView",authLiveData.getValue().getUserName() + authLiveData.getValue().getPassword());
        this.authRepository.login(authLiveData.getValue().getUserName(),authLiveData.getValue().getPassword());
    }

    public LiveData<ReturnData<LoginAndRegisterReturn>> signInLiveData() {
        return authRepository.signInLiveData(authLiveData.getValue().getUserName(),authLiveData.getValue().getPassword());
    }

    //重写点击监听事件的方法
    public View.OnClickListener loginClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Log.d("imageView","ViewModel-----login");
            StringBuilder token = new StringBuilder();
            Log.d("imageView",authLiveData.getValue().getUserName() + authLiveData.getValue().getPassword());
            authRepository.login(authLiveData.getValue().getUserName(),authLiveData.getValue().getPassword());
        }
    };

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
