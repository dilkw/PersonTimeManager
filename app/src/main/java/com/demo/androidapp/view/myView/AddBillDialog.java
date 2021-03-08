package com.demo.androidapp.view.myView;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;

import com.demo.androidapp.R;
import com.demo.androidapp.databinding.AddbillDialogBinding;
import com.demo.androidapp.model.entity.Bill;
import com.demo.androidapp.util.DateTimeUtil;

import java.sql.Date;
import java.time.LocalDateTime;

public class AddBillDialog extends DialogFragment implements View.OnClickListener {

    private AddbillDialogBinding addBillDialogBinding;

    private EnterListener enterListener;

    private Date selectedDate;

    private boolean isAdd = false;

    private Bill bill;


    public AddBillDialog(){
        bill = new Bill();
    }

    public AddBillDialog(Bill bill) {
        this.isAdd = false;
        this.bill = bill;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @SuppressLint({"InflateParams", "SetTextI18n"})
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        DateTimeUtil dateTimeUtil = new DateTimeUtil();
        addBillDialogBinding = DataBindingUtil.inflate(LayoutInflater.from(requireContext()),R.layout.addbill_dialog,null,false);
        View contentView = addBillDialogBinding.getRoot();
        String dialogTitle = "添加账单";
        if (!isAdd) {
            dialogTitle = "编辑账单";
            addBillDialogBinding.addBillDialogContentTextInputEditText.setText(bill.getContent());
            addBillDialogBinding.addBillDialogMoneyTextInputEditText.setText(bill.getAmount() + " 元");
            addBillDialogBinding.inComeToggleButton.setChecked(bill.isCategory());
        }
        addBillDialogBinding.addBillTitleTextView.setText(dialogTitle);
        setListener();
        AlertDialog alertDialog = new AlertDialog
                .Builder(requireContext())
                .setView(contentView)
                .create();
        return alertDialog;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void setListener() {
        addBillDialogBinding.addBillDialogCloseImgBtn.setOnClickListener(this);
        addBillDialogBinding.addBillEnterBtn.setOnClickListener(this);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.addBillEnterBtn: {
                enterListener.enterBtnOnClicked();
                dismiss();
                break;
            }
            case R.id.addBillDialogCloseImgBtn: {
                dismiss();
                break;
            }
        }
    }

    public interface EnterListener{
        void enterBtnOnClicked();
    }

    public void setEnterClicked(EnterListener enterClicked) {
        enterListener = enterClicked;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public Bill getBill() {
        DateTimeUtil dateTimeUtil = new DateTimeUtil();
        bill.setContent(addBillDialogBinding.addBillDialogContentTextInputEditText.getText().toString());
        bill.setAmount(Long.parseLong(addBillDialogBinding.addBillDialogMoneyTextInputEditText.getText().toString()));
        bill.setConsume_time(dateTimeUtil.localDateTimeToLong(LocalDateTime.now()) / 1000L);
        bill.setCategory(addBillDialogBinding.inComeToggleButton.isChecked());
        Log.d("imageView", "getClock: " + bill.toString());
        return bill;
    }

}
