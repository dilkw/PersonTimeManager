package com.demo.androidapp.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.demo.androidapp.model.entity.Clock;
import com.demo.androidapp.model.entity.Task;

import java.util.List;

@Dao
public interface ClockDao {

    //根据uid查询出所有时钟
    @Query("SELECT * FROM clock WHERE uid = :uid")
    public LiveData<List<Clock>> getAllClockLiveDataByUid(String uid);

    //根据uid查询出所有时钟
    @Query("SELECT * FROM clock")
    public List<Clock> getAllClockListByUid();

    //根据uid添加单个时钟
    @Insert
    public void addClock(Clock clock);

    //根据uid添加多个时钟
    @Insert
    public Long[] addClocks(Clock... clocks);

    //根据时钟内容进行模糊查询出所有相关时钟
    @Query("SELECT * FROM clock WHERE task like :pattern")
    public LiveData<List<Clock>> getAllClockByByPattern(String pattern);

    @Update
    void updateAllClockFromServers(Clock... clocks);

    @Delete
    void deleteClock(Clock... clocks);

}
