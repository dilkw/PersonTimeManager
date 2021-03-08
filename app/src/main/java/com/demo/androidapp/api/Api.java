package com.demo.androidapp.api;

import androidx.lifecycle.LiveData;
import androidx.room.Delete;
import androidx.room.Update;

import com.demo.androidapp.model.commitObject.UpdateTaskCommit;
import com.demo.androidapp.model.entity.Bill;
import com.demo.androidapp.model.entity.Clock;
import com.demo.androidapp.model.entity.Task;
import com.demo.androidapp.model.commitObject.RegisterCommit;
import com.demo.androidapp.model.common.ReturnData;
import com.demo.androidapp.model.returnObject.LoginAndRegisterReturn;
import com.demo.androidapp.model.returnObject.ReturnListObject;

import java.util.List;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.HTTP;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface Api {

    //登录
    @FormUrlEncoded
    @POST("user/login")
    Call<ReturnData<LoginAndRegisterReturn>> signIn(@Field("name") String name,
                                                    @Field("password") String password);

    //登录
    @FormUrlEncoded
    @POST("user/login")
    LiveData<ReturnData<LoginAndRegisterReturn>> signInLiveData(@Field("name") String name,
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
    Call<ReturnData<LoginAndRegisterReturn>> getResetPwdCode(@Field("email") String email);

    //注册完成后激活帐号
    @FormUrlEncoded
    @POST("user/active")
    Call<ReturnData> active(@Field("email") String email,
                              @Field("code") String code);

    //注销
    @POST("user/logout")
    LiveData<ReturnData<Object>> signOut();

    @GET("/todo")
    Call<ReturnData> todo();

    //获取某个用户的所有任务列表
    @GET("todo/list")
    Call<ReturnData<ReturnListObject<Task>>> getTaskList();

    //获取某个用户的所有任务列表
    @GET("todo/list")
    Call<ReturnData<ReturnListObject<Task>>> getTaskListLiveData();

    //获取某个任务信息
    @GET("todo/info/{taskId}")
    Call<ReturnData<Task>> getTaskInfoById(@Path("taskId") Long taskId);

    //添加任务
    @POST("todo/add")
    LiveData<ReturnData<Object>> addTask(@Body Task task);

    //删除任务
    @HTTP(method = "DELETE", path = "todo/{taskId}", hasBody = false)
    Call<ReturnData<Object>> deleteTask(@Path("taskId") long taskId);

    //修改任务
    @PUT("todo/{id}")
    LiveData<ReturnData<Object>> updateTask(@Path("id")long taskId, @Body Task task);



    //获取时钟
    @GET("clock/list")
    Call<ReturnData<ReturnListObject<Clock>>> getAllClocks();

    //更新时钟
    @PUT("clock/{clockId}")
    LiveData<ReturnData<Object>> upDateClock(@Body Clock clock);

    //删除时钟
    @HTTP(method = "DELETE", path = "clock/{clockId}", hasBody = false)
    LiveData<ReturnData<Object>> deleteClock(@Path("clockId") long clockId);

    //修改时钟
    @PUT("clock/{id}")
    LiveData<ReturnData<Object>> updateClock(@Path("id")long clockId, @Body Clock clock);




    //获取账单
    @GET("bill/list")
    Call<ReturnData<ReturnListObject<Bill>>> getAllBills();

    //更新账单
    @PUT("bill/{billId}")
    LiveData<ReturnData<Object>> upDateBill(@Body Bill bill);

    //删除账单
    @HTTP(method = "DELETE", path = "bill/{billId}", hasBody = false)
    LiveData<ReturnData<Object>> deleteBill(@Path("billId") long billId);

    //修改账单
    @PUT("bill/{id}")
    LiveData<ReturnData<Object>> updateBill(@Path("id")long billId, @Body Bill bill);


//    //获取账单
//    @GET("bill/list")
//    Call<ReturnData<ReturnListObject<Bill>>> getAllFriends();
//
//    //更新账单
//    @PUT("bill/{billId}")
//    LiveData<ReturnData<Object>> upDateBill(@Body Bill bill);
//
//    //删除账单
//    @HTTP(method = "DELETE", path = "bill/{taskId}", hasBody = false)
//    Call<ReturnData<Object>> deleteBill(@Path("taskId") long billId);
//
//    //修改账单
//    @PUT("bill/{id}")
//    LiveData<ReturnData<Object>> updateBill(@Path("id")long billId, @Body Bill bill);


}
