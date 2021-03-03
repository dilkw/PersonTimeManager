package com.demo.androidapp.viewmodel;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.demo.androidapp.model.common.RCodeEnum;
import com.demo.androidapp.model.common.ReturnData;
import com.demo.androidapp.model.returnObject.LoginAndRegisterReturn;
import com.demo.androidapp.repository.AuthRepository;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ResetPasswordViewModel extends AndroidViewModel {
    // TODO: Implement the ViewModel

    private AuthRepository authRepository;

    private MutableLiveData resetPwdMutableLiveData;

    public ResetPasswordViewModel(@NonNull Application application){
        super(application);
        authRepository = new AuthRepository();
        resetPwdMutableLiveData = new MutableLiveData();
    }

    //重置密码获取验证码
    public void resetPwdGetCode(String email) {
        if (email.isEmpty()) {
            Log.d("imageView", "resetPwdGetCode: 邮箱为空");
            authRepository.getReturnDataLiveData().setValue(new ReturnData(201,"",null));
            return;
        }
        authRepository.resetPwdGetCode(email);
    }

    public MutableLiveData<ReturnData<LoginAndRegisterReturn>> getRepositoryMutableLiveData() {
        return authRepository.getReturnDataLiveData();
    }

    //重置密码数据提交
    public void resetPwdCommit(String email,String password,String passwordConfirm) {
        if (email.isEmpty()) {
            Log.d("imageView", "resetPwdGetCode: 邮箱为空");
            authRepository.getReturnDataLiveData().setValue(new ReturnData(201,"",null));
            return;
        }
        authRepository.resetPwdGetCode(email);
    }

}