package com.demo.androidapp.viewmodel;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.demo.androidapp.model.FriendListItem;
import com.demo.androidapp.model.common.RCodeEnum;
import com.demo.androidapp.model.common.ReturnData;
import com.demo.androidapp.model.entity.Friend;
import com.demo.androidapp.model.entity.User;
import com.demo.androidapp.repository.AuthRepository;
import com.demo.androidapp.repository.FriendRepository;

import java.util.List;


public class FriendInfoViewModel extends AndroidViewModel {

    private FriendRepository friendRepository;

    public LiveData<ReturnData<FriendListItem>> friendListReturnLiveData;

    public String deleteFriendStr = "删除好友";

    public String addFriendStr = "添加好友";

    public FriendInfoViewModel(@NonNull Application application) {
        super(application);
        Log.d("imageView", "UserInfoViewModel:-=-=-=-=-= " + application.getClass().getName());
        friendRepository = new FriendRepository(application);
        friendListReturnLiveData = new MutableLiveData<>();
    }

    //服务器获取用户信息
    public LiveData<ReturnData<FriendListItem>> getFriendInfoByUid(String fuid) {
        return this.friendListReturnLiveData = friendRepository.getFriendInfoByUid(fuid);
    }

    //删除好友
    public LiveData<ReturnData<Object>> deleteFriend(long id) {
        Log.d("imageView", "deleteFriend: " + id);
        if (friendListReturnLiveData == null || id == 0) return new MutableLiveData<>(new ReturnData<>(RCodeEnum.DATA_ERROR));
        return friendRepository.deleteFriendsByIdsInServer("[" + id + "]");
    }

    //添加好友
    public LiveData<ReturnData<Friend>> addFriend() {
        Log.d("imageView", "addFriend: ");
        if (friendListReturnLiveData == null) return new MutableLiveData<>(new ReturnData<>(RCodeEnum.DATA_ERROR));
        return friendRepository.addFriendToServer(friendListReturnLiveData.getValue().getData().getUser().getEmail());
    }

    //获取returnLiveData
    public LiveData<ReturnData<FriendListItem>> getUserInfoLiveData() {
        return friendListReturnLiveData;
    }

}