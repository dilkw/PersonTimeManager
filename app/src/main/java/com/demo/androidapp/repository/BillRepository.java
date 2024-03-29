package com.demo.androidapp.repository;

import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.room.Room;

import com.demo.androidapp.MyApplication;
import com.demo.androidapp.api.Api;
import com.demo.androidapp.dao.AppDatabase;
import com.demo.androidapp.dao.BillDao;
import com.demo.androidapp.model.common.RCodeEnum;
import com.demo.androidapp.model.common.ReturnData;
import com.demo.androidapp.model.entity.Bill;
import com.demo.androidapp.model.returnObject.ReturnListObject;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class BillRepository {

    private Api api;

    private BillDao billDao;

    private MutableLiveData<ReturnData<List<Bill>>> returnDataLiveData;

    private MutableLiveData<ReturnData> deleteReturnDataLiveData;

    private BillRepository billRepository;

    public BillRepository(Application application) {
        this.api = MyApplication.getApi();
        this.billDao = Room
                .databaseBuilder(application.getApplicationContext(), AppDatabase.class, "clock")
                .build()
                .billDao();
        returnDataLiveData = new MutableLiveData<>();
        //returnDataLiveData = billDao.getAllBillLiveDataByUid(MyApplication.getApplication().getUID());
    }

    //从服务器中获取数据
    public LiveData<ReturnData<ReturnListObject<Bill>>> getBillLiveDataInServer() {
        return api.getAllBills();
    }

    //从服务器中获取数据
    public LiveData<List<Bill>> getAllBillLiveDataInDB() {
        return billDao.getAllBillLiveDataByUid(MyApplication.getApplication().getUser().getUid());
    }

    public MutableLiveData<ReturnData<List<Bill>>> getBillLiveData() {
        if (returnDataLiveData == null) {
            Log.d("imageView", "getReturnDataLiveData: returnDataLiveData为空");
        }
        return returnDataLiveData;
    }

    //根据账单内容查询
    public LiveData<List<Bill>> getBillsLiveDataByContent(String content) {
        String uid = MyApplication.getApplication().getUser().getUid();
        return billDao.getAllBillByContentByPattern("%" + content + "%",uid);
    }

    //根据账单内容查询
    public LiveData<List<Bill>> getBillsLiveDataByCategory(boolean category) {
        return billDao.getAllBillByCategory(category);
    }

    //根据uid在本地数据库中获取任务列表
    public void getAllBillByUidInDB() {
        String uid = MyApplication.getApplication().getUser().getUid();;
        new GetAllBillByUid(billDao,returnDataLiveData).execute(uid);
    }

    //在本地数据库中删除所有数据并更新数据
    public void deleteAllBillsAndAdd(Bill... bills) {
        Log.d("imageView", "getAllTaskByUidInDB: 数据库删除数据");
        new DeleteAllBillsAndAdd(billDao,this).execute(bills);
    }

    //在本地数据库中删除所有数据并更新数据
    public void deleteBillsByUidInDB(Bill... bills) {
        String uid = MyApplication.getApplication().getUser().getUid();;
        Log.d("imageView", "getAllTaskByUidInDB: 数据库删除数据");
        new DeleteBills(billDao).execute(bills);
    }

    //在服务器中删除数据参数为数组形式的字符串："[1,2]"
    public LiveData<ReturnData<Object>> deleteBillsByIdInServer(String billIds) {
        return api.deleteBillsByIds(billIds);
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

    //插入数据到服务器
    public LiveData<ReturnData<Bill>> addBillToServer(Bill bill) {
        return api.addBill(bill);
    }

    //更新服务器账单upDateBillsInServer
    public LiveData<ReturnData<Object>> upDateBillInServer(Bill bill) {
        Log.d("imageView", "repository-upDateBillInServer: " + bill.toString());
        return api.updateBill(bill.getId(),bill);
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

        private MutableLiveData<ReturnData<List<Bill>>> returnDataLiveData;

        GetAllBillByUid(BillDao billDao,MutableLiveData<ReturnData<List<Bill>>> returnDataLiveData) {
            this.billDao = billDao;
            this.returnDataLiveData = returnDataLiveData;
        }

        @Override
        protected Void doInBackground(String... strings) {
            List<Bill> bills = billDao.getAllBillListByUid(strings[0]);
            Log.d("imageView", "doInBackground: 本地数据库数据长度" + bills.size());
            returnDataLiveData.postValue(new ReturnData<List<Bill>>(RCodeEnum.DB_OK,bills));
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

    //删除并更新数据
    public static class DeleteAllBillsAndAdd extends AsyncTask<Bill,Void,Void> {

        BillDao billDao;

        BillRepository billRepository;

        DeleteAllBillsAndAdd(BillDao billDao, BillRepository billRepository) {
            this.billDao = billDao;
            this.billRepository = billRepository;
        }

        Bill[] bills;

        @Override
        protected Void doInBackground(Bill... bills) {
            this.bills = bills;
            billDao.deleteAllBillsByUid(MyApplication.getApplication().getUser().getUid());
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            billRepository.addBillsToDB(this.bills);
        }
    }

    //删除数据
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
