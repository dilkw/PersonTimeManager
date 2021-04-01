package com.demo.androidapp.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.demo.androidapp.model.entity.ChatRecord;
import com.demo.androidapp.model.entity.Task;

import java.util.List;

@Dao
public interface ChatRecordDao {

    //根据uid查询出所有任务
    @Query("SELECT * FROM chat_record WHERE uid = :uid AND fuid = :friendUid")
    public LiveData<List<ChatRecord>> getAllChatRecordLiveDataByUidAndFId(String uid,String friendUid);

    //根据uid添加单个任务
    @Insert
    public void addChatRecord(ChatRecord chatRecord);

    //根据uid添加多个任务
    @Insert
    public Long[] addChatRecords(ChatRecord... chatRecords);

    //根据任务内容进行模糊查询出所有相关任务
    @Query("SELECT * FROM chat_record WHERE fuid = :friendUid and task like :pattern")
    public LiveData<List<ChatRecord>> getChatRecordsByTaskByPattern(String pattern,String friendUid);

    @Delete
    void deleteChatRecords(ChatRecord...chatRecords);

    @Query("DELETE FROM chat_record WHERE uid = :uid ")
    void deleteAllChatRecordsByUid(String uid);

}
