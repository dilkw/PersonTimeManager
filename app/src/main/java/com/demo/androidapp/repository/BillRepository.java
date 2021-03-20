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
import com.demo.androidapp.model.common.RCodeEnum;
import com.demo.androidapp.model.common.ReturnData;
import com.demo.androidapp.model.entity.Bill;
import com.demo.androidapp.model.entity.Clock;
import com.demo.androidapp.model.entity.Task;
import com.demo.androidapp.model.returnObject.ReturnListObject;

import java.util.List;
import java.util.concurrent.ExecutionException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
    public void getBillLiveDataInServer() {
        api.getAllBills().enqueue(new Callback<ReturnData<ReturnListObject<Bill>>>() {
            @Override
            public void onResponse(Call<ReturnData<ReturnListObject<Bill>>> call, Response<ReturnData<ReturnListObject<Bill>>> response) {
                returnDataLiveData.postValue(new ReturnData<List<Bill>>(response.body().getCode(),response.body().getMsg(),response.body().getData().getItems()));
                if (response.body().getData().getTotal() > 0) {
                    Bill[] bills = new Bill[response.body().getData().getTotal()];
                    response.body().getData().getItems().toArray(bills);
                    deleteALLBillsAndAdd(bills);
                }
            }

            @Override
            public void onFailure(Call<ReturnData<ReturnListObject<Bill>>> call, Throwable t) {
                returnDataLiveData.postValue(new ReturnData<List<Bill>>(RCodeEnum.ERROR.getCode(),RCodeEnum.ERROR.getMessage(),null));
            }
        });
    }

    //从服务器中获取数据
    public LiveData<List<Bill>> getBillLiveDataInDB() {
        return billDao.getAllBillLiveDataByUid(MyApplication.getUser().getUid());
    }

    public MutableLiveData<ReturnData<List<Bill>>> getBillLiveData() {
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
        String uid = MyApplication.getUser().getUid();
        new GetAllBillByUid(billDao,returnDataLiveData).execute(uid);
    }

    //在本地数据库中删除所有数据并更新数据
    public void deleteALLBillsAndAdd(Bill... bills) {
        String uid = MyApplication.getUser().getUid();
        Log.d("imageView", "getAllTaskByUidInDB: 数据库删除数据");
        new DeleteALLBillsAndAdd(billDao,this).execute(bills);
    }

    //在本地数据库中删除所有数据并更新数据
    public void deleteBillsByUidInDB(Bill... bills) {
        String uid = MyApplication.getUser().getUid();
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
    public static class DeleteALLBillsAndAdd extends AsyncTask<Bill,Void,Void> {

        BillDao billDao;

        BillRepository billRepository;

        DeleteALLBillsAndAdd(BillDao billDao,BillRepository billRepository) {
            this.billDao = billDao;
            this.billRepository = billRepository;
        }

        Bill[] bills;

        @Override
        protected Void doInBackground(Bill... bills) {
            this.bills = bills;
            billDao.deleteAllBillsByUid(MyApplication.getUser().getUid());
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
