package com.demo.androidapp.api;

import android.util.Log;

import androidx.lifecycle.LiveData;

import com.demo.androidapp.model.common.ReturnData;

import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Type;
import java.util.concurrent.atomic.AtomicBoolean;

import retrofit2.Call;
import retrofit2.CallAdapter;
import retrofit2.Callback;
import retrofit2.Response;

public class LiveDataCallAdapter<T> implements CallAdapter<T, LiveData<T>> {

    private final Type mResponseType;
    private final boolean isReturnData;

    LiveDataCallAdapter(Type mResponseType, boolean isReturnData) {
        this.mResponseType = mResponseType;
        this.isReturnData = isReturnData;
    }

    @NotNull
    @Override
    public Type responseType() {
        return mResponseType;
    }

    @NotNull
    @Override
    public LiveData<T> adapt(@NotNull final Call<T> call) {
        return new MyLiveData<>(call, isReturnData);
    }

    private static class MyLiveData<T> extends LiveData<T> {

        private final AtomicBoolean stared = new AtomicBoolean(false);
        private final Call<T> call;
        private final boolean isApiResponse;

        MyLiveData(Call<T> call, boolean isApiResponse) {
            this.call = call;
            this.isApiResponse = isApiResponse;
        }

        @Override
        protected void onActive() {
            super.onActive();
            //确保执行一次
            if (stared.compareAndSet(false, true)) {
                call.enqueue(new Callback<T>() {
                    @Override
                    public void onResponse(@NotNull Call<T> call, @NotNull Response<T> response) {
                        Log.d("imageView", "onResponse: ");
                        T body = response.body();
                        postValue(body);
                    }

                    @Override
                    public void onFailure(@NotNull Call<T> call, @NotNull Throwable t) {
                        Log.d("imageView", "onFailure:======= ");
                        if (isApiResponse) {
                            postValue((T) new ReturnData<>(201, t.getMessage(),null));
                        } else {
                            postValue(null);
                        }
                    }
                });
            }
        }
    }
}

