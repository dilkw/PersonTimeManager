package com.demo.androidapp.view.myView;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;

import com.demo.androidapp.R;
import com.demo.androidapp.databinding.DatetimepickerBinding;
import com.demo.androidapp.util.DateTimeUtil;

import java.sql.Date;
import java.util.Calendar;

public class DateTimePickerDialog extends DialogFragment {

    private DatetimepickerBinding datetimepickerBinding;

    private EnterListener enterListener;

    private Date selectedDate;

    private int createYear, createMonthOfYear, createDayOfMonth;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @SuppressLint("InflateParams")
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Calendar calendar = Calendar.getInstance();
        createYear = calendar.get(Calendar.YEAR);
        createMonthOfYear = calendar.get(Calendar.MONTH);
        createDayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        //View view = LayoutInflater.from(this.context).inflate(R.layout.datetimepicker,null);
        datetimepickerBinding = DataBindingUtil.inflate(LayoutInflater.from(requireContext()),R.layout.datetimepicker,null,false);
        setListener();
        datetimepickerBinding.timePicker.setIs24HourView(true);
        datetimepickerBinding.timePickerEnterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enterListener.enterBtnOnClicked();
                getDialog().dismiss();
            }
        });
        builder.setView(datetimepickerBinding.getRoot());
        return builder.create();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void setListener() {
        datetimepickerBinding.datePicker.setOnDateChangedListener(new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                if (year >= createDayOfMonth
                    && monthOfYear >= createDayOfMonth
                    && dayOfMonth >= createDayOfMonth) {
                    datetimepickerBinding.timePickerEnterBtn.setEnabled(true);
                }else {
                    datetimepickerBinding.timePickerEnterBtn.setEnabled(false);
                }
            }
        });

        datetimepickerBinding.dateTimePickerCloseImgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().dismiss();
            }
        });
    }

    public interface EnterListener{
        void enterBtnOnClicked();
    }

    public void setEnterClicked(EnterListener enterClicked) {
        enterListener = enterClicked;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public String getSelectTimeString() {
        int year,moth,day,hour,minute;
        year = datetimepickerBinding.datePicker.getYear();
        moth = datetimepickerBinding.datePicker.getMonth();
        day = datetimepickerBinding.datePicker.getDayOfMonth();
        hour = datetimepickerBinding.timePicker.getHour();
        minute = datetimepickerBinding.timePicker.getMinute();
        DateTimeUtil dateTimeUtil = new DateTimeUtil();
        return dateTimeUtil.intToStrDateTime(year,moth,day,hour,minute);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public String getSelectedDate() {
        int year,moth,day,hour,minute;
        year = datetimepickerBinding.datePicker.getYear();
        moth = datetimepickerBinding.datePicker.getMonth();
        day = datetimepickerBinding.datePicker.getDayOfMonth();
        hour = datetimepickerBinding.timePicker.getHour();
        minute = datetimepickerBinding.timePicker.getMinute();
        DateTimeUtil dateTimeUtil = new DateTimeUtil();
        return dateTimeUtil.intToStrDateTime(year,moth,day,hour,minute);
    }

}
