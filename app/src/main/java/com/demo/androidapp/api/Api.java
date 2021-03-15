package com.demo.androidapp.api;

import androidx.lifecycle.LiveData;

import com.demo.androidapp.model.FriendListItem;
import com.demo.androidapp.model.ResetPwdModel;
import com.demo.androidapp.model.entity.Bill;
import com.demo.androidapp.model.entity.Clock;
import com.demo.androidapp.model.entity.Friend;
import com.demo.androidapp.model.entity.Task;
import com.demo.androidapp.model.commitObject.RegisterCommit;
import com.demo.androidapp.model.common.ReturnData;
import com.demo.androidapp.model.entity.User;
import com.demo.androidapp.model.returnObject.LoginAndRegisterReturn;
import com.demo.androidapp.model.returnObject.ReturnListObject;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.HTTP;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface Api {

    //登录
    @FormUrlEncoded
    @POST("user/login")
    LiveData<ReturnData<User>> signIn(@Field("name") String name,
                                                    @Field("password") String password);

    //登录
    @FormUrlEncoded
    @POST("user/login")
    LiveData<ReturnData<User>> signInLiveData(@Field("name") String name,
                                                    @Field("password") String password);

    //注册
    @POST("user/signup")
    LiveData<ReturnData<User>> signUp(@Body RegisterCommit registerCommit);

    //激活帐号获取验证码（返回字符串）https://sodacoco.com/api/v1/user/forget
    @FormUrlEncoded
    @POST("user/active/code")
    LiveData<ReturnData<Object>> getActiveCodes(@Field("email") String email);

    //重置密码获取验证码（返回字符串）
    @FormUrlEncoded
    @POST("user/forget")
    LiveData<ReturnData<Object>> getResetPwdCode(@Field("email") String email);

    //重置密码
    @POST("user/reset")
    LiveData<ReturnData<Object>> resetPwd(@Body ResetPwdModel resetPwdModel);

    //注册完成后激活帐号
    @FormUrlEncoded
    @POST("user/active")
    LiveData<ReturnData<Object>> active(@Field("email") String email,@Field("code") String code);

    //注销
    @POST("user/logout")
    LiveData<ReturnData<Object>> signOut();

    //获取某个用户的所有任务列表
    @GET("todo/list")
    Call<ReturnData<ReturnListObject<Task>>> getTaskList();

    // 获取用户个人信息
    @GET("user/me")
    LiveData<ReturnData<User>> getUserInfo();

    // 修改个人信息名字（昵称）
    @FormUrlEncoded
    @PUT("user/editName")
    LiveData<ReturnData<Object>> userInfoEditName(@Field("name") String newName);

    // 用户获取注销用户验证码
    @FormUrlEncoded
    @POST("user/cancellation/code")
    LiveData<ReturnData<Object>> getCancellationCode(@Field("email") String email);

    // 用户注销
    @FormUrlEncoded
    @POST("user/cancellation")
    LiveData<ReturnData<Object>> cancellation(@Field("email") String email,@Field("code") String code);

    // 用户获取更换邮箱验证码
    @FormUrlEncoded
    @POST("user/resetEmail/code")
    LiveData<ReturnData<Object>> getResetEmailCode(@Field("email") String email);

    // 修改个人信息邮箱
    @FormUrlEncoded
    @PUT("user/editEmail")
    LiveData<ReturnData<Object>> resetEmail(@Field("email") String email
                                                    ,@Field("new_email") String newEmail
                                                    ,@Field("code") String code);


    //获取任务列表
    @GET("todo/list")
    Call<ReturnData<ReturnListObject<Task>>> getTaskListLiveData();

    //获取某个任务信息
    @GET("todo/info/{taskId}")
    Call<ReturnData<Task>> getTaskInfoById(@Path("taskId") Long taskId);

    //添加任务
    @POST("todo/add")
    LiveData<ReturnData<Task>> addTask(@Body Task task);

    //删除任务
    @HTTP(method = "DELETE", path = "todo/{ids}", hasBody = false)
    LiveData<ReturnData<Object>> deleteTasksByIds(@Path("ids") String taskIds);

    //修改任务
    @PUT("todo/{id}")
    LiveData<ReturnData<Object>> updateTask(@Path("id")long taskId, @Body Task task);



    //获取时钟列表
    @GET("clock/list")
    Call<ReturnData<ReturnListObject<Clock>>> getAllClocks();

    //获取某个时钟信息
    @GET("clock/info/{id}")
    LiveData<ReturnData<Clock>> getClockInfoByClockId(@Path("id")long clockId);

    //添加时钟
    @POST("clock/add")
    LiveData<ReturnData<Clock>> addClock(@Body Clock clock);

    //更新时钟
    @PUT("clock/{clockId}")
    LiveData<ReturnData<Object>> upDateClock(@Body Clock clock);

    //删除时钟
    @HTTP(method = "DELETE", path = "clock/{ids}", hasBody = false)
    LiveData<ReturnData<Object>> deleteClock(@Path("ids") String clockIds);

    //修改时钟
    @PUT("clock/{id}")
    LiveData<ReturnData<Object>> updateClock(@Path("id")long clockId, @Body Clock clock);




    //获取账单
    @GET("bill/list")
    Call<ReturnData<ReturnListObject<Bill>>> getAllBills();

    //添加账单
    @POST("bill/add")
    LiveData<ReturnData<Bill>> addBill(@Body Bill bill);

    //删除账单
    @HTTP(method = "DELETE", path = "bill/{ids}", hasBody = false)
    LiveData<ReturnData<Object>> deleteBillsByIds(@Path("ids") String billIds);

    //修改账单
    @PUT("bill/{id}")
    LiveData<ReturnData<Object>> updateBill(@Path("id")long billId, @Body Bill bill);


    //获取好友
    @GET("friend/list")
    Call<ReturnData<ReturnListObject<Friend>>> getAllFriends();

    //获取好友信息
    @GET("friend/info/{id}")
    LiveData<ReturnData<FriendListItem>> getFriendInfoByUid(@Path("id")String fuid);

    //添加好友
    @FormUrlEncoded
    @POST("friend/add")
    LiveData<ReturnData<Friend>> addFriend(@Field("email") String friendEmail);

    //删除好友
    @HTTP(method = "DELETE", path = "friend/{ids}", hasBody = false)
    LiveData<ReturnData<Object>> deleteFriendsByIds(@Path("ids") String friendIds);

}
