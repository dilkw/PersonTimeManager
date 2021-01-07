package com.demo.androidapp.api;

import com.demo.androidapp.model.commitObject.RegisterCommit;
import com.demo.androidapp.model.returnObject.LoginReturn;
import com.demo.androidapp.model.returnObject.RegisterReturn;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface Api {

    //登录
    @FormUrlEncoded
    @POST("user/login")
    Call<LoginReturn> signIn(@Field("name") String name,
                            @Field("password") String password);

    //注册
    @POST("user/signup")
    Call<RegisterReturn> signUp(@Body RegisterCommit registerCommit);

    //获取验证码（返回字符串）
    @POST("user/active/code")
    Call<ResponseBody> getActiveCodes(@Field("email") String email);

    //注册完成后激活帐号
    @FormUrlEncoded
    @POST("user/active")
    Call<ResponseBody> active(@Field("email") String email,
                              @Field("code") String code);

    //注销
    @POST("user/logout")
    Call<ResponseBody> signOut();

    @GET("/todo")
    Call<ResponseBody> todo();

}
