package com.demo.androidapp.repository;

import android.util.Log;
import androidx.lifecycle.MutableLiveData;

import com.demo.androidapp.MyApplication;
import com.demo.androidapp.api.Api;
import com.demo.androidapp.model.commitObject.RegisterCommit;
import com.demo.androidapp.model.common.RCodeEnum;
import com.demo.androidapp.model.common.ReturnData;
import com.demo.androidapp.model.returnObject.LoginReturn;
import com.demo.androidapp.model.returnObject.RegisterReturn;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AuthRepository {

    private Api api;

    private MutableLiveData<ReturnData> returnDataLiveData = new MutableLiveData<>();

    public AuthRepository() {
        this.api = MyApplication.getApi();
//        this.returnDataLiveData.setValue(new ReturnData(RCodeEnum.ERROE));
    }

    public void login(String userName, String password) {
        Log.d("imageView","authRepository层：repository----login");
        Log.d("imageView","authRepository层：登录信息" + "用户名：" + userName + "密码：" + password);
        api.signIn(userName,password).enqueue(new Callback<LoginReturn>() {
            @Override
            public void onResponse(Call<LoginReturn> call, Response<LoginReturn> response) {
                Log.d("response", "onResponse:");
                Gson gson = new Gson();
                if (response.body() == null) {
                    Log.d("response", "onResponse: null");
                    returnDataLiveData.postValue(new ReturnData(RCodeEnum.ERROE));
                    return;
                }
                Log.d("response", "onResponse: notNull" + response.body().getCreated_at());
                //LoginReturn loginReturn = gson.fromJson(response.body().toString(),LoginReturn.class);
                LoginReturn loginReturn = response.body();
                ReturnData returnData = new ReturnData(loginReturn);
                returnDataLiveData.postValue(returnData);
            }

            @Override
            public void onFailure(Call<LoginReturn> call, Throwable t) {
                Log.d("response", "onFailure: ");
                t.printStackTrace();
                returnDataLiveData.postValue(new ReturnData(RCodeEnum.ERROE));
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
        api.signUp(registerCommit).enqueue(new Callback<RegisterReturn>() {
            @Override
            public void onResponse(Call<RegisterReturn> call, Response<RegisterReturn> response) {
                //Gson gson = new Gson();
                //RegisterReturn registerReturn = gson.fromJson(response.body().toString(),RegisterReturn.class);
                ReturnData returnData = new ReturnData(response);
                returnDataLiveData.postValue(returnData);
            }
            @Override
            public void onFailure(Call<RegisterReturn> call, Throwable t) {
                returnDataLiveData.postValue(new ReturnData(RCodeEnum.ERROE));
                Log.d("imageView","authRepository层：注册失败" + t.toString());
            }
        });
    }

    public MutableLiveData<ReturnData> getReturnDataLiveData() {
        return returnDataLiveData;
    }
}
