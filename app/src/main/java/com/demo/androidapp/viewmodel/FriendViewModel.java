package com.demo.androidapp.viewmodel;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.demo.androidapp.model.FindFriendInfo;
import com.demo.androidapp.model.common.RCodeEnum;
import com.demo.androidapp.model.common.ReturnData;
import com.demo.androidapp.model.entity.Bill;
import com.demo.androidapp.model.entity.Friend;
import com.demo.androidapp.model.returnObject.ReturnListObject;
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

    //云端获取Friends
    public LiveData<ReturnData<ReturnListObject<Friend>>> getAllFriendsLiveDataInServer() {
        return friendRepository.getAllFriendsLiveDataByUidInServer();
    }

    //数据库获取Friends
    public LiveData<List<Friend>> getAllFriendsInDB() {
        return friendRepository.getAllFriendsByUidInDB();
    }

    //数据库获取Friends
    public LiveData<List<Friend>> getAllFriendsInDBByFName(String fName) {
        return friendRepository.getAllFriendsInDBByUidAndFName(fName);
    }


    //根据用户id从本地数据库清空删除好友
    public void deleteFriendByUidInDB(Friend friend) {
        //在数据库中没有数据时尝试从无服务器中获取
        if (friend == null) {
            return;
        }
        friendRepository.deleteFriendByUidInDB(friend);
    }

    //数据库添加好友
    public void addFriendsInDB(Friend... friends) {
        //在数据库中没有数据时尝试从无服务器中获取
        if (friends.length == 0) return;
        friendRepository.addFriendsToDB(friends);
    }

    //服务器添加好友
    public LiveData<ReturnData<Friend>> addFriendInServer(String fid) {
        if (fid == null || fid.equals("")) {
            return new MutableLiveData<>(new ReturnData<>(RCodeEnum.DATA_ERROR));
        }
        return friendRepository.addFriendToServer(fid);
    }

    //数据库更新好友
    public void upDateFriendsInDB(Friend... friends) {
        //在数据库中没有数据时尝试从无服务器中获取
        if (friends.length == 0) return;
        friendRepository.updateFriendsInDB(friends);
    }

    //数据库更新好友
    public LiveData<ReturnData<FindFriendInfo>> getFriendInfoInServerByFUid(String fUid) {
        //在数据库中没有数据时尝试从无服务器中获取
        if (fUid == null || fUid.equals("")) {
            return new MutableLiveData<>(new ReturnData<>(RCodeEnum.DATA_ERROR));
        }
        return friendRepository.getFriendInfoByUid(fUid);
    }

}