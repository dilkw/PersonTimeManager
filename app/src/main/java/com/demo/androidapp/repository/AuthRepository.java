package com.demo.androidapp.repository;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.demo.androidapp.MyApplication;
import com.demo.androidapp.api.Api;
import com.demo.androidapp.model.commitObject.RegisterCommit;
import com.demo.androidapp.model.common.RCodeEnum;
import com.demo.androidapp.model.common.ReturnData;
import com.demo.androidapp.model.entity.User;
import com.demo.androidapp.model.returnObject.LoginAndRegisterReturn;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Field;

public class AuthRepository {

    private Api api;

    private MutableLiveData<ReturnData<LoginAndRegisterReturn>> returnDataLiveData = new MutableLiveData<>();

    private AuthRepository authRepository;

    public AuthRepository(Application application) {
        this.api = MyApplication.getApi();
        if (api == null) {
            Log.d("imageView", "AuthRepository: api为空==========");
        }
    }

    public AuthRepository getInstance() {
        if (authRepository == null) {
            return null;
        }
        return this;
    }


    //登录
    public LiveData<ReturnData<User>> login(String userName, String password) {
        Log.d("imageView","authRepository层：repository----login");
        Log.d("imageView","authRepository层：登录信息" + "用户名：" + userName + "密码：" + password);
        return api.signIn(userName,password);
    }
    public LiveData<ReturnData<User>> signInLiveData(String userName, String password) {
        return api.signInLiveData(userName,password);
    }

    //注册
    public LiveData<ReturnData<User>> register(RegisterCommit registerCommit) {
        Log.d("imageView","authRepository层：repository----register");
        if (registerCommit == null) {
            Log.d("imageView","authRepository层注册信息为空");
            return new MutableLiveData<>(new ReturnData<>(RCodeEnum.DATA_ERROR));
        }
        Log.d("imageView","authRepository层：注册信息：" + registerCommit.toString());
        return api.signUp(registerCommit);
    }


    //帐号激活获取验证码
    public LiveData<ReturnData<Object>> getActiveCode(String email) {
        return api.getActiveCodes(email);
    }

    //重置密码获取验证码
    public void resetPwdGetCode(String email) {
        api.getResetPwdCode(email).enqueue(new Callback<ReturnData<LoginAndRegisterReturn>>() {
            @Override
            public void onResponse(Call<ReturnData<LoginAndRegisterReturn>> call, Response<ReturnData<LoginAndRegisterReturn>> response) {
                Log.d("imageView","authRepository层：重置密码获取验证码成功" );
                returnDataLiveData.postValue((ReturnData<LoginAndRegisterReturn>)response.body());
            }

            @Override
            public void onFailure(Call<ReturnData<LoginAndRegisterReturn>> call, Throwable t) {
                Log.d("imageView","authRepository层：重置密码获取验证码失败");
                t.printStackTrace();
                returnDataLiveData.postValue(new ReturnData<>(RCodeEnum.ERROR));
            }
        });
    }

    public MutableLiveData<ReturnData<LoginAndRegisterReturn>> getReturnDataLiveData() {
        return returnDataLiveData;
    }

    //获取用户信息
    public LiveData<ReturnData<User>> getUserInfoLiveData() {
        return api.getUserInfo();
    }

    //更新用户名字
    public LiveData<ReturnData<Object>> userInfoEditName(String userName) {
        return api.userInfoEditName(userName);
    }

    //注销用户信息
    public LiveData<ReturnData<Object>> cancellation(String email,String code) {
        return api.cancellation(email,code);
    }

    public LiveData<ReturnData<Object>> getCancellationCode(String email) {
        return api.getCancellationCode(email);
    }


    //验证码激活新注册帐号
    /**
     * email:注册页面填写的邮箱
     * codes:接收到的帐号激活码
     */
    public LiveData<ReturnData<Object>> active(String email,String codes) {
        return api.active(email,codes);
    }

    public LiveData<ReturnData<Object>> resetEmail(String email,String newEmail,String code) {
        return api.resetEmail(email,newEmail,code);
    }
}
