package com.demo.androidapp.api;

import android.os.Build;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.lifecycle.LiveData;

import com.demo.androidapp.model.common.ReturnData;

import org.jetbrains.annotations.NotNull;

import java.lang.annotation.Annotation;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import retrofit2.CallAdapter;
import retrofit2.Retrofit;

public class LiveDataCallAdapterFactory extends CallAdapter.Factory {

    private static final String TAG = LiveDataCallAdapterFactory.class.getSimpleName();

    @RequiresApi(api = Build.VERSION_CODES.P)
    @Nullable
    @Override
    public CallAdapter<?, ?> get(@NotNull Type returnType, @NotNull Annotation @NotNull [] annotations, @NotNull Retrofit retrofit) {
        if (getRawType(returnType) != LiveData.class) {
            return null;
        }
        //获取第一个泛型类型
        Type observableType = getParameterUpperBound(0, (ParameterizedType) returnType);
        Class<?> rawType = getRawType(observableType);
        Log.d(TAG, "rawType = " + rawType.getSimpleName());
        boolean isApiResponse = true;
        if (rawType != ReturnData.class) {
            //不是返回ApiResponse类型的返回值
            Log.d(TAG, "rawType = 类型错误");
            isApiResponse = false;
        }
        if (observableType instanceof ParameterizedType) {
            Log.d(TAG, "observableType = " + ((ParameterizedType) observableType).getRawType().getTypeName());
            //throw new IllegalArgumentException("resource must be parameterized");
        }
        return new LiveDataCallAdapter<>(observableType, isApiResponse);
    }
}

