package com.demo.androidapp.repository;

import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.room.Room;

import com.demo.androidapp.MyApplication;
import com.demo.androidapp.api.Api;
import com.demo.androidapp.db.AppDatabase;
import com.demo.androidapp.db.BillDao;
import com.demo.androidapp.db.ClockDao;
import com.demo.androidapp.model.common.ReturnData;
import com.demo.androidapp.model.entity.Bill;
import com.demo.androidapp.model.entity.Clock;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class BillRepository {

    private Api api;

    private BillDao billDao;

    private LiveData<List<Bill>> returnDataLiveData;

    private MutableLiveData<ReturnData> deleteReturnDataLiveData;

    private BillRepository billRepository;

    public BillRepository(Application application) {
        this.api = MyApplication.getApi();
        this.billDao = Room
                .databaseBuilder(application.getApplicationContext(), AppDatabase.class, "clock")
                .build()
                .billDao();
        returnDataLiveData = billDao.getAllBillLiveDataByUid(MyApplication.getApplication().getUID());
    }

    public LiveData<List<Bill>> getBillLiveData() {
        if (returnDataLiveData == null) {
            Log.d("imageView", "getReturnDataLiveData: returnDataLiveData为空");
        }
        return returnDataLiveData;
    }

    //根据账单内容查询
    public LiveData<List<Bill>> getBillsLiveDataByContent(String s) {
        return billDao.getAllBillByContentByPattern("%" + s + "%");
    }

    //根据账单内容查询
    public LiveData<List<Bill>> getBillsLiveDataByCategory(boolean category) {
        return billDao.getAllBillByCategory(category);
    }

    //根据uid在本地数据库中获取任务列表
    public void getAllBillByUidInDB() {
        String uid = MyApplication.getApplication().getUID();
        new GetAllBillByUid(billDao,returnDataLiveData).execute(uid);
    }

    //在本地数据库中删除多个数据
    public void deleteBillsByUidInDB(Bill... bills) {
        String uid = MyApplication.getApplication().getUID();
        Log.d("imageView", "getAllTaskByUidInDB: 数据库删除数据");
        new DeleteBills(billDao).execute(bills);
    }

    //插入本地数据库任务列表
    public Long[] addBillsToDB(Bill... bills) {
        Long[] longs = null;
        try {
            longs = new InsertBillList(billDao).execute(bills).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return longs;
    }

    //更新本地数据库任务列表
    public void updateBills(Bill... bills) {
        new UpdateBillList(billDao).execute(bills);
    }

    public static class InsertBillList extends AsyncTask<Bill,Void, Long[]> {

        BillDao billDao;

        InsertBillList(BillDao billDao) {
            this.billDao = billDao;
        }

        @Override
        protected Long[] doInBackground(Bill... bills) {
            return this.billDao.addBills(bills);
        }
    }

    public static class GetAllBillByUid extends AsyncTask<String,Void,Void> {

        BillDao billDao;

        private LiveData<List<Bill>> returnDataLiveData;

        GetAllBillByUid(BillDao billDao,LiveData<List<Bill>> returnDataLiveData) {
            this.billDao = billDao;
            this.returnDataLiveData = returnDataLiveData;
        }

        @Override
        protected Void doInBackground(String... strings) {
//            List<Task> tasks = taskDao.getAllTaskListByUid(strings[0]);
//            Log.d("imageView", "doInBackground: 本地数据库数据长度" + tasks.size());
//            returnDataLiveData.postValue(new ReturnData<List<Task>>(RCodeEnum.DB_OK,tasks));
            return null;
        }
    }

    public static class UpdateBillList extends AsyncTask<Bill,Void,Void> {

        BillDao billDao;

        UpdateBillList(BillDao billDao) {
            this.billDao = billDao;
        }

        @Override
        protected Void doInBackground(Bill... bills) {
            this.billDao.updateAllBillFromServers(bills);
            return null;
        }
    }

    public static class DeleteBills extends AsyncTask<Bill,Void,Void> {

        BillDao billDao;

        DeleteBills(BillDao billDao) {
            this.billDao = billDao;
        }

        @Override
        protected Void doInBackground(Bill... bills) {
            billDao.deleteBill(bills);
            return null;
        }
    }

}
