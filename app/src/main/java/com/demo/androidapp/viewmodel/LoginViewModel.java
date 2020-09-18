package com.demo.androidapp.viewmodel;

import android.annotation.SuppressLint;
import android.app.Application;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.demo.androidapp.R;
import com.demo.androidapp.model.Auth;
import com.google.android.material.textfield.TextInputLayout;

public class LoginViewModel extends AndroidViewModel {

    public final int resId = R.id.action_loginFragment_to_registerFragment;

    private LiveData<Auth> auth;

    public LoginViewModel(@NonNull Application application) {
        super(application);
    }
    // TODO: Implement the ViewModel


    public LiveData<Auth> getAuth() {
        return auth;
    }

    //跳转方法
    public void skipping(View view) {
        Log.d("imageView","跳转");
        NavController navController = Navigation.findNavController(view);
        navController.navigate(resId);
    }

    public String login() {
        StringBuilder token = new StringBuilder();
        return token.toString();
    }
}
