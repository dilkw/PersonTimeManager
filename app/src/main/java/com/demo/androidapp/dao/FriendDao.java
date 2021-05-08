package com.demo.androidapp.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.demo.androidapp.model.entity.Friend;

import java.util.List;

@Dao
public interface FriendDao {

    //根据uid查询出所有任务
    @Query("SELECT * FROM friend where uid = :uid")
    public LiveData<List<Friend>> getAllFriendLiveDataByUid(String uid);

    //根据uid查询出所有任务
    @Query("SELECT * FROM friend where uid = :uid and fname like :fName")
    public LiveData<List<Friend>> getAllFriendsInDBByUidAndFName(String uid,String fName);

    //根据uid查询出所有任务
    @Query("SELECT * FROM friend")
    public List<Friend> getAllFriendListByUid();

    //根据uid添加单个任务
    @Insert
    public void addFriend(Friend friend);

    //根据uid添加多个任务
    @Insert
    public Long[] addFriends(Friend... friends);

    //根据任务内容进行模糊查询出所有相关任务
    @Query("SELECT * FROM friend WHERE fname like :pattern")
    public List<Friend> getAllFriendByName(String pattern);

    @Update
    void updateAllFriendFromServers(Friend...friends);

    @Delete
    void deleteFriend(Friend...friends);

    @Query("DELETE FROM friend WHERE uid = :uid ")
    void deleteAllFriendsByUid(String uid);

    @Query("DELETE FROM friend")
    void deleteAllFriendsByUid();

}
