package com.demo.androidapp.viewmodel;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.demo.androidapp.model.common.RCodeEnum;
import com.demo.androidapp.model.common.ReturnData;
import com.demo.androidapp.model.entity.Bill;
import com.demo.androidapp.model.entity.Clock;
import com.demo.androidapp.repository.BillRepository;
import com.demo.androidapp.repository.ClockRepository;

import java.util.List;

public class BillViewModel extends AndroidViewModel {

    private BillRepository billRepository;

    public BillViewModel(@NonNull Application application) {
        super(application);
        Log.d("imageView", "HomeViewModel:-=-=-=-=-= " + application.getClass().getName());
        billRepository = new BillRepository(application);
    }

    //获取clockRepository中的returnLiveData
    public MutableLiveData<ReturnData<List<Bill>>> getReturnLiveData() {
        return billRepository.getBillLiveData();
    }

    //云端获取bill
    public void getAllBillsInServer() {
        billRepository.getBillLiveDataInServer();
    }

    //本地数据库获取bill
    public LiveData<List<Bill>> getBillLiveDataInDB() {
        return billRepository.getBillLiveDataInDB();
    }

    //根据账单内容获取clockRepository中的returnLiveData
    public LiveData<List<Bill>> getBillsLiveDataByContent(String s) {
        return billRepository.getBillsLiveDataByContent(s);
    }

    //根据用户id从本地数据库清空该用户的时钟列表
    public void deleteClocksByUidInDB(List<Bill> bills) {
        //在数据库中没有数据时尝试从无服务器中获取
        if (bills == null || bills.size() == 0) {
            return;
        }
        Bill[] billArray = new Bill[bills.size()];
        bills.toArray(billArray);
        billRepository.deleteBillsByUidInDB(billArray);
    }

    //添加时钟
    public void addBillsInDB(Bill... bills) {
        //在数据库中没有数据时尝试从无服务器中获取
        if (bills.length == 0) return;
        billRepository.addBillsToDB(bills);
    }

    //更新时钟
    public void upDateBillsInDB(Bill... bills) {
        //在数据库中没有数据时尝试从无服务器中获取
        if (bills.length == 0) return;
        billRepository.updateBills(bills);
    }

    //更新时钟
    public LiveData<ReturnData<Object>> upDateBillInServer(Bill bill) {
        //在数据库中没有数据时尝试从无服务器中获取
        if (bill == null) return new MutableLiveData<>(new ReturnData<>(RCodeEnum.DATA_ERROR));
        return billRepository.upDateBillInServer(bill);
    }

}