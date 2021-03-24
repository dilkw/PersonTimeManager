package com.demo.androidapp.repository;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.demo.androidapp.MyApplication;
import com.demo.androidapp.api.Api;
import com.demo.androidapp.model.ResetPwdModel;
import com.demo.androidapp.model.commitObject.RegisterCommit;
import com.demo.androidapp.model.common.RCodeEnum;
import com.demo.androidapp.model.common.ReturnData;
import com.demo.androidapp.model.entity.User;
import com.demo.androidapp.model.returnObject.LoginAndRegisterReturn;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Field;
import retrofit2.http.Multipart;
import retrofit2.http.Part;

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

    public MutableLiveData<ReturnData<LoginAndRegisterReturn>> getReturnDataLiveData() {
        return returnDataLiveData;
    }

    //登录
    public LiveData<ReturnData<User>> login(String userName, String password) {
        Log.d("imageView","authRepository层：repository----login");
        Log.d("imageView","authRepository层：登录信息" + "用户名：" + userName + "密码：" + password);
        return api.signIn(userName,password);
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
    public LiveData<ReturnData<Object>> resetPwdGetCode(String email) {
        return api.getResetPwdCode(email);
    }

    //重置密码
    public LiveData<ReturnData<Object>> resetPwd(ResetPwdModel resetPwdModel) {
        return api.resetPwd(resetPwdModel);
    }

    //验证码激活新注册帐号
    /**
     * email:注册页面填写的邮箱
     * codes:接收到的帐号激活码
     */
    public LiveData<ReturnData<Object>> active(String email,String codes) {
        return api.active(email,codes);
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

    //获取注销用户验证码
    public LiveData<ReturnData<Object>> getCancellationCode(String email) {
        return api.getCancellationCode(email);
    }

    //重置邮箱
    public LiveData<ReturnData<Object>> resetEmail(String email,String newEmail,String code) {
        return api.resetEmail(email,newEmail,code);
    }

    // 用户上传头像获取七牛sdk Token
    public LiveData<ReturnData<Object>> getUploadImgToken(){
        return api.getUploadImgToken();
    }

    // 用户上传头像
    public LiveData<ReturnData<String>> uploadImg(File file) {
        Log.d("imageView", "uploadImg: " + file.getName() + file.length());
        String fileName = "img_" + MyApplication.getApplication().getUser().getUid();;
        // 为file建立RequestBody实例
        RequestBody requestFile = RequestBody.create(file,MediaType.parse("MULTIPART_FORM_DATA"));
        // MultipartBody.Part借助文件名完成最终的上传
        MultipartBody.Part part = MultipartBody.Part.createFormData("file", file.getName(), requestFile);
        return api.uploadImg(part);
    }
}
