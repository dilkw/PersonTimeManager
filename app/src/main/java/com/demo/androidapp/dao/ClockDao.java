package com.demo.androidapp.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.demo.androidapp.model.entity.Clock;

import java.util.List;

@Dao
public interface ClockDao {

    //根据uid查询出所有时钟
    @Query("SELECT * FROM clock WHERE uid = :uid")
    public LiveData<List<Clock>> getAllClockLiveDataByUid(String uid);

    //根据uid查询出所有时钟
    @Query("SELECT * FROM clock")
    public List<Clock> getAllClockListByUid();

    //根据id查询出所有某一个时钟信息
    @Query("SELECT * FROM clock WHERE id = :clockId")
    public LiveData<Clock> getClockByClockId(long clockId);

    //根据uid添加单个时钟
    @Insert
    public void addClock(Clock clock);

    //根据uid添加多个时钟
    @Insert
    public Long[] addClocks(Clock... clocks);

    //根据时钟内容进行模糊查询出所有相关时钟
    @Query("SELECT * FROM clock WHERE uid = :uid and task like :pattern")
    public LiveData<List<Clock>> getAllClockByByPattern(String pattern,String uid);

    @Update
    void updateAllClockFromServers(Clock... clocks);

    @Delete
    void deleteClock(Clock... clocks);

    @Query("DELETE FROM clock WHERE uid = :uid ")
    void deleteAllClocksByUid(String uid);

}
