package com.demo.androidapp.repository;

import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.room.Room;

import com.demo.androidapp.MyApplication;
import com.demo.androidapp.api.Api;
import com.demo.androidapp.db.AppDatabase;
import com.demo.androidapp.db.ChatRecordDao;
import com.demo.androidapp.db.TaskDao;
import com.demo.androidapp.model.common.ReturnData;
import com.demo.androidapp.model.entity.ChatRecord;
import com.demo.androidapp.model.entity.Task;
import com.demo.androidapp.model.returnObject.ReturnListObject;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class ChatRecordRepository {

    private Api api;

    private ChatRecordDao chatRecordDao;

    private ChatRecordRepository chatRecordRepository;

    public ChatRecordRepository(Application application) {
        this.api = MyApplication.getApi();
        this.chatRecordDao = Room
                .databaseBuilder(application.getApplicationContext(), AppDatabase.class, "task")
                .build()
                .chatRecordDao();
    }


    public LiveData<List<ChatRecord>> getAllChatRecordLiveDataByUidAndFId(String uid,String friendUid) {
        return chatRecordDao.getAllChatRecordLiveDataByUidAndFId(uid,friendUid);
    }
}
