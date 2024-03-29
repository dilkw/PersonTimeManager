package com.demo.androidapp.viewmodel;

import android.app.Application;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.demo.androidapp.R;
import com.demo.androidapp.model.commitObject.RegisterCommit;
import com.demo.androidapp.model.common.RCodeEnum;
import com.demo.androidapp.model.common.ReturnData;
import com.demo.androidapp.model.entity.User;
import com.demo.androidapp.model.returnObject.LoginAndRegisterReturn;
import com.demo.androidapp.repository.AuthRepository;

public class RegisterViewModel extends AndroidViewModel {

    public MutableLiveData<RegisterCommit> registerCommitLiveData;

    private AuthRepository authRepository;

    public MutableLiveData<RegisterCommit> getRegisterCommitLiveData() {
        return registerCommitLiveData;
    }

    public RegisterViewModel(@NonNull Application application) {
        super(application);
        registerCommitLiveData = new MutableLiveData<>();
        registerCommitLiveData.setValue(new RegisterCommit());
        authRepository = new AuthRepository(application);
        String imgUrl = application.getResources().getString(R.string.defaultImgUrl);
        registerCommitLiveData.getValue().setImg_url(imgUrl);
    }

    //注册方法
    public LiveData<ReturnData<User>> register() {
        if (registerCommitLiveData.getValue() == null) {
            Log.d("imageView","RegisterViewModel层，ViewModel为空！！！！！！");
            return new MutableLiveData<>(new ReturnData<>(RCodeEnum.DATA_ERROR));
        }
        Log.d("imageView","ViewModel-----register" + registerCommitLiveData.getValue().toString());
        return this.authRepository.register(registerCommitLiveData.getValue());
    }

    public MutableLiveData<ReturnData<LoginAndRegisterReturn>> getReturnData() {
        return authRepository.getReturnDataLiveData();
    }

    public void jumpToActiveFragment(View view) {
        //跳转激活页面
        NavController navController = Navigation.findNavController(view);
        Bundle bundle = new Bundle();
        bundle.putString("email",registerCommitLiveData.getValue().getEmail());
        navController.navigate(R.id.action_registerFragment_to_activeFragment,bundle);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        Log.d("imageView", "RegisterViewModel------onCleared: ");
    }
}
