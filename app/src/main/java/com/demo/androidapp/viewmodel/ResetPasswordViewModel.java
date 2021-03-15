package com.demo.androidapp.viewmodel;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.demo.androidapp.model.ResetPwdModel;
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

    public LiveData<ResetPwdModel> resetPwdMutableLiveData;

    public ResetPasswordViewModel(@NonNull Application application){
        super(application);
        authRepository = new AuthRepository(application);
        resetPwdMutableLiveData = new MutableLiveData<ResetPwdModel>(new ResetPwdModel());
    }

    //重置密码获取验证码
    public LiveData<ReturnData<Object>> resetPwdGetCode() {
        String email = resetPwdMutableLiveData.getValue().getEmail();
        if (email.isEmpty()) {
            Log.d("imageView", "resetPwdGetCode: 邮箱为空");
            return new MutableLiveData<>(new ReturnData<>(RCodeEnum.DATA_ERROR));
        }
        return authRepository.resetPwdGetCode(email);
    }



    //重置密码数据提交
    public LiveData<ReturnData<Object>> resetPwdCommit() {
        if (resetPwdMutableLiveData.getValue() == null) {
            Log.d("imageView", "resetPwdGetCode: 邮箱为空");
            return new MutableLiveData<>(new ReturnData<>(RCodeEnum.DATA_ERROR));
        }
        return authRepository.resetPwd(resetPwdMutableLiveData.getValue());
    }

}