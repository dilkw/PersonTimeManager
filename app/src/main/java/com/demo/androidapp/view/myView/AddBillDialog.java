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
        isAdd = true;
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
        addBillDialogBinding = DataBindingUtil.inflate(LayoutInflater.from(requireContext()),R.layout.addbill_dialog,null,false);
        View contentView = addBillDialogBinding.getRoot();
        String dialogTitle = "添加账单";
        //判断是编辑账单还是添加账单
        if (!isAdd) {
            dialogTitle = "编辑账单";
            addBillDialogBinding.addBillEnterBtn.setText("保存");
            addBillDialogBinding.addBillDialogContentTextInputEditText.setText(bill.getContent());
            addBillDialogBinding.addBillDialogMoneyTextInputEditText.setText(bill.getAmount() + "");
            addBillDialogBinding.inComeToggleButton.setChecked(bill.isCategory());
            addBillDialogBinding.addBillDialogConsumeTimeTextView.setText(DateTimeUtil.longToStrYMDHM(this.bill.getConsume_time()));
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
        addBillDialogBinding.addBillDialogAddConsumeTimeImgBtn.setOnClickListener(this);
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
            case R.id.addBillDialogAddConsumeTimeImgBtn: {
                DateTimePickerDialog dateTimePickerDialog = new DateTimePickerDialog(false);
                dateTimePickerDialog.show(requireActivity().getSupportFragmentManager(),"addConsumeTimeDialog");
                dateTimePickerDialog.setEnterClicked(new DateTimePickerDialog.EnterListener() {
                    @Override
                    public void enterBtnOnClicked(String dateTimeStr) {
                        addBillDialogBinding.addBillDialogConsumeTimeTextView.setText(dateTimeStr);
                    }
                });
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
        bill.setContent(addBillDialogBinding.addBillDialogContentTextInputEditText.getText().toString());
        bill.setAmount(Float.parseFloat(addBillDialogBinding.addBillDialogMoneyTextInputEditText.getText().toString()));
        String dateTimeStr = addBillDialogBinding.addBillDialogConsumeTimeTextView.getText().toString();
        if (dateTimeStr == null || dateTimeStr.equals("")) {
            bill.setConsume_time(DateTimeUtil.localDateTimeToLong(LocalDateTime.now()) / 1000);
        } else {
            bill.setConsume_time(DateTimeUtil.strToLong(addBillDialogBinding.addBillDialogConsumeTimeTextView.getText().toString()));
        }
        bill.setCategory(addBillDialogBinding.inComeToggleButton.isChecked());
        Log.d("imageView", "getBill: " + bill.toString());
        return bill;
    }

}
