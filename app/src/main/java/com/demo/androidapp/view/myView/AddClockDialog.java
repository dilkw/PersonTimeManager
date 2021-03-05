package com.demo.androidapp.view.myView;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.MutableLiveData;

import com.demo.androidapp.R;
import com.demo.androidapp.databinding.AddclockDialogBinding;
import com.demo.androidapp.databinding.DatetimepickerBinding;
import com.demo.androidapp.model.entity.Clock;
import com.demo.androidapp.util.DateTimeUtil;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import java.sql.Date;
import java.time.LocalDateTime;

public class AddClockDialog extends DialogFragment implements View.OnClickListener {

    private AddclockDialogBinding addclockDialogBinding;

    private EnterListener enterListener;

    private Date selectedDate;

    private boolean isAdd = false;

    private Clock clock;


    public AddClockDialog(){
        clock = new Clock();
    }

    public AddClockDialog(Clock clock) {
        this.isAdd = false;
        this.clock = clock;
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
        addclockDialogBinding = DataBindingUtil.inflate(LayoutInflater.from(requireContext()),R.layout.addclock_dialog,null,false);
        View contentView = addclockDialogBinding.getRoot();
        if (!isAdd) {
            addclockDialogBinding.addClockDialogTaskTextInputEditText.setText(clock.getTask());
            addclockDialogBinding.addClockDialogMinuteTextInputEditText.setText("" + clock.getClockMinuet());

            addclockDialogBinding.addClockAlertTimeTextView.setText(dateTimeUtil.longToStrYMDHM(clock.getAlertTime()));
            if (clock.isAlert()) {
                addclockDialogBinding.addClockDialogAlertTimeLinearLayout.setVisibility(View.VISIBLE);
            }
            addclockDialogBinding.addClockDialogMinuteTextInputEditText.setText("" + clock.getClockMinuet());
        }
        setListener();
        AlertDialog alertDialog = new AlertDialog
                .Builder(requireContext())
                .setView(contentView)
                .create();
        return alertDialog;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void setListener() {
        addclockDialogBinding.addClockEnterBtn.setOnClickListener(this);
        addclockDialogBinding.addClockDialogCloseImgBtn.setOnClickListener(this);
        addclockDialogBinding.addClockDialogAddAlertTimeImgBtn.setOnClickListener(this);
        addclockDialogBinding.addClockAlertTimeClearImgBtn.setOnClickListener(this);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.addClockEnterBtn: {
                enterListener.enterBtnOnClicked();
                dismiss();
                break;
            }
            case R.id.addClockDialogCloseImgBtn: {
                dismiss();
                break;
            }
            case R.id.addClockDialogAddAlertTimeImgBtn: {//为时钟添加提醒时间按钮
                DateTimePickerDialog dateTimePickerDialog = new DateTimePickerDialog();
                dateTimePickerDialog.setEnterClicked(new DateTimePickerDialog.EnterListener() {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void enterBtnOnClicked() {
                        addclockDialogBinding.addClockAlertTimeTextView.setText(dateTimePickerDialog.getSelectTimeString());
                        clock.setAlertTime(dateTimePickerDialog.getSelectedDateToLong() / 1000);
                        clock.setAlert(true);
                        addclockDialogBinding.addClockDialogAlertTimeLinearLayout.setVisibility(View.VISIBLE);
                    }
                });
                dateTimePickerDialog.show(requireActivity().getSupportFragmentManager(),"alertTimePicker");
                break;
            }
            case R.id.addClockAlertTimeClearImgBtn: {
                addclockDialogBinding.addClockAlertTimeTextView.setText("");
                clock.setAlertTime(0);
                clock.setAlert(false);
                addclockDialogBinding.addClockDialogAlertTimeLinearLayout.setVisibility(View.GONE);
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
    public Clock getClock() {
        clock.setTask(addclockDialogBinding.addClockDialogTaskTextInputEditText.getText().toString());
        clock.setClockMinuet(Long.parseLong(addclockDialogBinding.addClockDialogMinuteTextInputEditText.getText().toString()));
        Log.d("imageView", "getClock: " + clock.toString());
        return clock;
    }

}
