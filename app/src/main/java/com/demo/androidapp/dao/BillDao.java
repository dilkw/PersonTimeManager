package com.demo.androidapp.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.demo.androidapp.model.entity.Bill;

import java.util.List;

@Dao
public interface BillDao {

    //根据uid查询出所有任务
    @Query("SELECT * FROM bill WHERE uid = :uid")
    public LiveData<List<Bill>> getAllBillLiveDataByUid(String uid);

    //根据uid查询出所有任务
    @Query("SELECT * FROM bill WHERE uid = :uid")
    public List<Bill> getAllBillListByUid(String uid);

    //根据uid添加单个任务
    @Insert
    public void addBill(Bill bill);

    //根据uid添加多个任务
    @Insert
    public Long[] addBills(Bill... bills);

    //根据任务内容进行模糊查询出所有相关任务
    @Query("SELECT * FROM bill WHERE uid = :uid and content like :pattern")
    public LiveData<List<Bill>> getAllBillByContentByPattern(String pattern,String uid);

    //根据任务内容进行模糊查询出所有相关任务
    @Query("SELECT * FROM bill WHERE category = :category")
    public LiveData<List<Bill>> getAllBillByCategory(boolean category);

    @Update
    void updateAllBillFromServers(Bill...bills);

    @Delete
    void deleteBill(Bill...bills);

    @Delete
    void deleteBill(List<Bill> billLists);

    @Query("DELETE FROM bill WHERE uid = :uid ")
    void deleteAllBillsByUid(String uid);

}
