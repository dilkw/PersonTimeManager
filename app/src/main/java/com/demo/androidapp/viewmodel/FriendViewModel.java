package com.demo.androidapp.viewmodel;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.demo.androidapp.model.common.RCodeEnum;
import com.demo.androidapp.model.common.ReturnData;
import com.demo.androidapp.model.entity.Bill;
import com.demo.androidapp.model.entity.Friend;
import com.demo.androidapp.repository.FriendRepository;

import java.util.Arrays;
import java.util.List;

public class FriendViewModel extends AndroidViewModel {

    private FriendRepository friendRepository;

    public FriendViewModel(@NonNull Application application) {
        super(application);
        Log.d("imageView", "HomeViewModel:-=-=-=-=-= " + application.getClass().getName());
        friendRepository = new FriendRepository(application);
    }

    //获取clockRepository中的returnLiveData
    public MutableLiveData<ReturnData<List<Friend>>> getReturnLiveData() {
        return friendRepository.getReturnDataLiveData();
    }

    //云端获取Friends
    public void getAllFriendsInServer() {
        friendRepository.getAllFriendsByUidInServer();
    }

    //数据库获取Friends
    public LiveData<List<Friend>> getAllFriendsInDB() {
        return friendRepository.getAllFriendsByUidInDB();
    }

    //数据库获取Friends
    public LiveData<List<Friend>> getAllFriendsInDBByFName(String fName) {
        return friendRepository.getAllFriendsInDBByUidAndFName(fName);
    }


    //根据用户id从本地数据库清空该用户的时钟列表
    public void deleteClocksByUidInDB(List<Friend> friends) {
        //在数据库中没有数据时尝试从无服务器中获取
        if (friends == null || friends.size() == 0) {
            return;
        }
        Friend[] friendArray = new Friend[friends.size()];
        friends.toArray(friendArray);
        friendRepository.deleteFriendsByUidInDB(friendArray);
    }

    //根据好友id从服务器中删除好友（多个删除）
    public LiveData<ReturnData<Object>> deleteFriendsByIdsInServer(List<Friend> friends) {
        Log.d("imageView", "deleteTasksByUidInServer: 删除任务");
        if (friends == null || friends.size() == 0) {
            return new MutableLiveData<>(new ReturnData<>(RCodeEnum.DATA_ERROR));
        }
        long[] friendIds = new long[friends.size()];
        for (int i =0 ; i < friends.size() ; i++) {
            friendIds[i] = friends.get(i).getId();
        }
        return friendRepository.deleteFriendsByIdsInServer(Arrays.toString(friendIds));
    }

    //数据库添加好友
    public void addFriendsInDB(Friend... friends) {
        //在数据库中没有数据时尝试从无服务器中获取
        if (friends.length == 0) return;
        friendRepository.addFriendsToDB(friends);
    }

    //服务器添加好友
    public LiveData<ReturnData<Friend>> addFriendInServer(String friendEmail) {
        if (friendEmail == null || friendEmail.equals("")) {
            return new MutableLiveData<>(new ReturnData<>(RCodeEnum.DATA_ERROR));
        }
        return friendRepository.addFriendToServer(friendEmail);
    }

    //数据库更新好友
    public void upDateFriendsInDB(Friend... friends) {
        //在数据库中没有数据时尝试从无服务器中获取
        if (friends.length == 0) return;
        friendRepository.updateFriendsInDB(friends);
    }

}