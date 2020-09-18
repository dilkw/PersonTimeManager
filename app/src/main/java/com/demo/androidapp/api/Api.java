package com.demo.androidapp.api;

import com.demo.androidapp.model.Auth;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface Api {

    @POST("/login")
    Call<Auth> login();
}
