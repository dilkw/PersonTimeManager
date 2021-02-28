package com.demo.androidapp.viewmodel;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.demo.androidapp.model.common.ReturnData;
import com.demo.androidapp.model.returnObject.LoginAndRegisterReturn;
import com.demo.androidapp.repository.AuthRepository;

import java.util.Objects;

public class ActiveViewModel extends AndroidViewModel {

    public MutableLiveData<String> codesLiveData;

    public AuthRepository authRepository;

    private String email;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public ActiveViewModel(@NonNull Application application) {
        super(application);
        this.codesLiveData = new MutableLiveData<String>();
        codesLiveData.setValue("");
        authRepository = new AuthRepository();
    }

    public void setCodesLiveData(MutableLiveData<String> codesLiveData) {
        this.codesLiveData = codesLiveData;
    }

    public MutableLiveData<String> getCodesLiveData() {
        return codesLiveData;
    }

    public MutableLiveData<ReturnData<LoginAndRegisterReturn>> getReturnLiveData() {
        return authRepository.getReturnDataLiveData();
    }

    /**
     * email:注册页面填写的邮箱
     * codes:接收到的帐号激活码
     */
    public void sendCodes() {
        if (email != null && Objects.requireNonNull(codesLiveData.getValue()).length() == 6) {
            authRepository.active(this.email,codesLiveData.getValue());
        }
    }

    /**
     * 重新获取验证码
     * email:帐号关联的邮箱
     */
    public void getActiveCodes() {
        if (email != null) {
            Log.d("imageView", "getActiveCodes: email不为空");
            authRepository.getActiveCode(this.email);
            return;
        }
        Log.d("imageView", "getActiveCodes: email为空");
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        Log.d("imageView", "ActiveViewModel------onCleared: ");
    }
}