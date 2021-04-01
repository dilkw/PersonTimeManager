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
import com.demo.androidapp.model.entity.User;
import com.demo.androidapp.repository.AuthRepository;
import com.demo.androidapp.repository.ChatRecordRepository;
import com.demo.androidapp.repository.FriendRepository;

import java.io.File;
import java.util.List;


public class ChatViewModel extends AndroidViewModel {

    private ChatRecordRepository chatRecordRepository;

    public ChatViewModel(@NonNull Application application) {
        super(application);
        chatRecordRepository = new ChatRecordRepository(application);
    }

    //在本地数据库获取聊天记录
    public LiveData<List<ChatRecord>> getAllChatRecordLiveDataByUidAndFId(String fuid) {
        String uid = MyApplication.getApplication().getUser().getUid();
        return chatRecordRepository.getAllChatRecordLiveDataByUidAndFId(uid,fuid);
    }

}