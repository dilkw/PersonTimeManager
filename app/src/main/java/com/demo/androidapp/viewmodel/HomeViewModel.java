package com.demo.androidapp.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.demo.androidapp.model.entity.Task;
import com.demo.androidapp.model.common.ReturnData;
import com.demo.androidapp.repository.TaskRepository;

import java.util.ArrayList;
import java.util.List;

public class HomeViewModel extends AndroidViewModel {

    public MutableLiveData<List<Task>> homeLiveData;

    private TaskRepository taskRepository;

    public HomeViewModel(@NonNull Application application) {
        super(application);
        homeLiveData = new MutableLiveData<List<Task>>(new ArrayList<Task>());
        taskRepository = new TaskRepository(application);
    }
    // TODO: Implement the ViewModel

    public MutableLiveData<ReturnData> getReturnLiveData() {
        return taskRepository.getReturnDataLiveData();
    }

    public void getAllTaskByUid(String uid) {
        taskRepository.getAllTaskByUidInService(uid);
    }

}
