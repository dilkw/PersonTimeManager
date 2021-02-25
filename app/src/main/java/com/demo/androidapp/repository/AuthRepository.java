package com.demo.androidapp.repository;

import android.util.Log;
import androidx.lifecycle.MutableLiveData;

import com.demo.androidapp.MyApplication;
import com.demo.androidapp.api.Api;
import com.demo.androidapp.model.commitObject.RegisterCommit;
import com.demo.androidapp.model.common.RCodeEnum;
import com.demo.androidapp.model.common.ReturnData;
import com.demo.androidapp.model.returnObject.LoginAndRegisterReturn;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AuthRepository {

    private Api api;

    private MutableLiveData<ReturnData> returnDataLiveData = new MutableLiveData<>();

    private AuthRepository authRepository;

    public AuthRepository() {
        this.api = MyApplication.getApi();
    }

    public AuthRepository getInstance() {
        if (authRepository == null) {
            return new AuthRepository();
        }
        return this;
    }


    //登录
    public void login(String userName, String password) {
        Log.d("imageView","authRepository层：repository----login");
        Log.d("imageView","authRepository层：登录信息" + "用户名：" + userName + "密码：" + password);
        api.signIn(userName,password).enqueue(new Callback<ReturnData<LoginAndRegisterReturn>>() {
            @Override
            public void onResponse(Call<ReturnData<LoginAndRegisterReturn>> call, Response<ReturnData<LoginAndRegisterReturn>> response) {
                Log.d("imageView","authRepository层：登录成功" );
                Log.d("response", "onResponse:");
                if (response.body() == null) {
                    Log.d("response", "onResponse: null");
                    returnDataLiveData.postValue(new ReturnData(RCodeEnum.ERROR));
                    return;
                }
                Log.d("response", "onResponse: notNull=========" + response.body().getData());
                returnDataLiveData.postValue(response.body());
            }

            @Override
            public void onFailure(Call<ReturnData<LoginAndRegisterReturn>> call, Throwable t) {
                Log.d("imageView","authRepository层：登录失败" );
                t.printStackTrace();
                returnDataLiveData.postValue(new ReturnData(RCodeEnum.ERROR));
                //Log.d("imageView","authRepository层：登陆失败" + returnDataLiveData.getValue().getCode());
            }
        });
    }

    //注册
    public void register(RegisterCommit registerCommit) {
        Log.d("imageView","authRepository层：repository----register");
        if (registerCommit == null) {
            Log.d("imageView","authRepository层注册信息为空");
            return;
        }
        Log.d("imageView","authRepository层：注册信息：" + registerCommit.toString());
        api.signUp(registerCommit).enqueue(new Callback<ReturnData<LoginAndRegisterReturn>>() {
            @Override
            public void onResponse(Call<ReturnData<LoginAndRegisterReturn>> call, Response<ReturnData<LoginAndRegisterReturn>> response) {
                Log.d("imageView","authRepository层：注册成功" );
                //ReturnData returnData = new ReturnData(response);
                returnDataLiveData.postValue(response.body());
            }
            @Override
            public void onFailure(Call<ReturnData<LoginAndRegisterReturn>> call, Throwable t) {
                returnDataLiveData.postValue(new ReturnData(RCodeEnum.ERROR));
                Log.d("imageView","authRepository层：注册失败" + t.toString());
                t.printStackTrace();
            }
        });
    }


    //帐号激活获取验证码
    public void getActiveCode(String email) {
        Log.d("imageView","authRepository层：repository----getActiveCode");
        if (email == null) {
            Log.d("imageView","邮箱为空");
            return;
        }
        Log.d("imageView","authRepository层：注册信息：" + email);
        api.getActiveCodes(email).enqueue(new Callback<ReturnData>() {
            @Override
            public void onResponse(Call<ReturnData> call, Response<ReturnData> response) {
                Log.d("imageView","authRepository层：获取验证码成功" );
                returnDataLiveData.postValue(response.body());
            }

            @Override
            public void onFailure(Call<ReturnData> call, Throwable t) {
                Log.d("imageView","authRepository层：获取验证码失败");
                t.printStackTrace();
                ReturnData returnData = new ReturnData(RCodeEnum.ERROR);
                returnDataLiveData.postValue(returnData);
            }
        });
    }

    //重置密码获取验证码
    public void resetPwdGetCode(String email) {
        api.getResetPwdCode(email).enqueue(new Callback<ReturnData>() {
            @Override
            public void onResponse(Call<ReturnData> call, Response<ReturnData> response) {
                Log.d("imageView","authRepository层：重置密码获取验证码成功" );
                returnDataLiveData.postValue(response.body());
            }

            @Override
            public void onFailure(Call<ReturnData> call, Throwable t) {
                Log.d("imageView","authRepository层：重置密码获取验证码失败");
                t.printStackTrace();
                ReturnData returnData = new ReturnData(RCodeEnum.ERROR);
                returnDataLiveData.postValue(returnData);
            }
        });
    }

    public MutableLiveData<ReturnData> getReturnDataLiveData() {
        return returnDataLiveData;
    }


    //验证码激活新注册帐号
    /**
     * email:注册页面填写的邮箱
     * codes:接收到的帐号激活码
     */
    public void active(String email,String codes) {
        api.active(email,codes).enqueue(new Callback<ReturnData>() {
            @Override
            public void onResponse(Call<ReturnData> call, Response<ReturnData> response) {
                Log.d("imageView","authRepository层：激活账号成功" );
                //ReturnData returnData = new ReturnData(response);
                returnDataLiveData.postValue(response.body());
            }

            @Override
            public void onFailure(Call<ReturnData> call, Throwable t) {
                returnDataLiveData.postValue(new ReturnData(RCodeEnum.ERROR));
                Log.d("imageView","authRepository层：激活失败" + t.toString());
                t.printStackTrace();
            }
        });
    }
}
