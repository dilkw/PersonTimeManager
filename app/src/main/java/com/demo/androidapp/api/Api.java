package com.demo.androidapp.api;

import com.demo.androidapp.model.entity.Task;
import com.demo.androidapp.model.commitObject.RegisterCommit;
import com.demo.androidapp.model.common.ReturnData;
import com.demo.androidapp.model.returnObject.LoginAndRegisterReturn;
import com.demo.androidapp.model.returnObject.ReturnListObject;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface Api {

    //登录
    @FormUrlEncoded
    @POST("user/login")
    Call<ReturnData<LoginAndRegisterReturn>> signIn(@Field("name") String name,
                                                    @Field("password") String password);

    //注册
    @POST("user/signup")
    Call<ReturnData<LoginAndRegisterReturn>> signUp(@Body RegisterCommit registerCommit);

    //激活帐号获取验证码（返回字符串）https://sodacoco.com/api/v1/user/forget
    @FormUrlEncoded
    @POST("user/active/code")
    Call<ReturnData> getActiveCodes(@Field("email") String email);

    //重置密码获取验证码（返回字符串）
    @FormUrlEncoded
    @POST("user/forget")
    Call<ReturnData> getResetPwdCode(@Field("email") String email);

    //注册完成后激活帐号
    @FormUrlEncoded
    @POST("user/active")
    Call<ReturnData> active(@Field("email") String email,
                              @Field("code") String code);

    //注销
    @POST("user/logout")
    Call<ReturnData> signOut();

    @GET("/todo")
    Call<ReturnData> todo();

    //获取某个用户的所有任务列表
    @GET("todo/list")
    Call<ReturnData<ReturnListObject<Task>>> getTaskList();

    //获取某个任务信息
    @GET("todo/info/{taskId}")
    Call<ReturnData<Task>> getTaskInfoById(@Path("taskId") Long taskId);

    //添加任务
    @POST("todo/add")
    Call<ReturnData> addTask(Task task);

}
