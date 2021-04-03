package com.demo.androidapp.viewmodel;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.demo.androidapp.MyApplication;
import com.demo.androidapp.model.common.RCodeEnum;
import com.demo.androidapp.model.common.ReturnData;
import com.demo.androidapp.model.entity.ChatRecord;
import com.demo.androidapp.model.entity.Clock;
import com.demo.androidapp.model.entity.Task;
import com.demo.androidapp.model.entity.User;
import com.demo.androidapp.repository.AuthRepository;
import com.demo.androidapp.repository.ChatRecordRepository;
import com.demo.androidapp.repository.ClockRepository;
import com.demo.androidapp.repository.FriendRepository;
import com.demo.androidapp.repository.TaskRepository;

import java.io.File;
import java.util.List;


public class ChatViewModel extends AndroidViewModel {

    private ChatRecordRepository chatRecordRepository;

    private TaskRepository taskRepository;
    private ClockRepository clockRepository;


    public ChatViewModel(@NonNull Application application) {
        super(application);
        chatRecordRepository = new ChatRecordRepository(application);
        taskRepository = new TaskRepository(application);
        clockRepository = new ClockRepository(application);
    }

    //在本地数据库获取聊天记录
    public LiveData<List<ChatRecord>> getAllChatRecordLiveDataByUidAndFId(String fuid) {
        String uid = MyApplication.getApplication().getUser().getUid();
        return chatRecordRepository.getAllChatRecordLiveDataByUidAndFId(uid,fuid);
    }

    //将好友分享的任务保存到自己服务器的任务列表中
    public LiveData<ReturnData<Task>> saveShareTaskToServer(Task task) {
        return taskRepository.addTasksToServer(task);
    }

    //将好友分享的任务保存到自己本地数据库的任务列表中
    public void saveShareTaskToDB(Task task) {
        taskRepository.addTasksToDB(task);
    }

    //将好友分享的任务保存到自己服务器的任务列表中
    public LiveData<ReturnData<Clock>> saveShareClockToServer(Clock clock) {
        return clockRepository.addClockToServer(clock);
    }

    //将好友分享的任务保存到自己本地数据库的任务列表中
    public void saveShareClockToDB(Clock clock) {
        clockRepository.addClocksToDB(clock);
    }

}