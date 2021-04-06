package com.demo.androidapp.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.room.Room;

import com.demo.androidapp.MyApplication;
import com.demo.androidapp.api.Api;
import com.demo.androidapp.dao.AppDatabase;
import com.demo.androidapp.dao.ChatRecordDao;
import com.demo.androidapp.model.entity.ChatRecord;

import java.util.List;

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
