package com.demo.androidapp.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

public class ActiveViewModel extends AndroidViewModel {

    public MutableLiveData<String> codesLiveData;

    public ActiveViewModel(@NonNull Application application) {
        super(application);
        this.codesLiveData = new MutableLiveData<String>();
        codesLiveData.setValue("");
    }

    public void setCodesLiveData(MutableLiveData<String> codesLiveData) {
        this.codesLiveData = codesLiveData;
    }

    public MutableLiveData<String> getCodesLiveData() {
        return codesLiveData;
    }
}