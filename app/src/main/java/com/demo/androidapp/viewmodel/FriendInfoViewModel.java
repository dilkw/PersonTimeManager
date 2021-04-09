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
import com.demo.androidapp.model.entity.Friend;
import com.demo.androidapp.repository.FriendRepository;

import java.util.Objects;


public class FriendInfoViewModel extends AndroidViewModel {

    private FriendRepository friendRepository;

    public MutableLiveData<FindFriendInfo> findFriendInfoMutableLiveData;

    public String deleteFriendStr = "删除好友";

    public String addFriendStr = "添加好友";

    public FriendInfoViewModel(@NonNull Application application) {
        super(application);
        Log.d("imageView", "UserInfoViewModel:-=-=-=-=-= " + application.getClass().getName());
        friendRepository = new FriendRepository(application);
        findFriendInfoMutableLiveData = new MutableLiveData<>();
    }

    //服务器获取用户信息
    public LiveData<ReturnData<FindFriendInfo>> getFriendInfoByUid(String fuid) {
        return friendRepository.getFriendInfoByUid(fuid);
    }

    //删除好友
    public LiveData<ReturnData<Object>> deleteFriend() {
        String fUid = Objects.requireNonNull(findFriendInfoMutableLiveData.getValue()).getUser().getUid();
        if (fUid == null || fUid.equals("")) return new MutableLiveData<>(new ReturnData<>(RCodeEnum.DATA_ERROR));
        Log.d("imageView", "deleteFriend: " + fUid);
        return friendRepository.deleteFriendByFIdInServer(fUid);
    }

    //添加好友
    public LiveData<ReturnData<Friend>> addFriend() {
        if (findFriendInfoMutableLiveData.getValue() == null) return new MutableLiveData<>(new ReturnData<>(RCodeEnum.DATA_ERROR));
        Log.d("imageView", "addFriend: ");
        return friendRepository.addFriendToServer(findFriendInfoMutableLiveData.getValue().getUser().getUid());
    }

    //获取returnLiveData
    public LiveData<FindFriendInfo> getUserInfoLiveData() {
        return findFriendInfoMutableLiveData;
    }

}