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
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;

import com.demo.androidapp.R;
import com.demo.androidapp.databinding.DatetimepickerBinding;
import com.demo.androidapp.util.DateTimeUtil;

import java.sql.Date;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Objects;

public class DateTimePickerDialog extends DialogFragment {

    private DatetimepickerBinding datetimepickerBinding;

    private EnterListener enterListener;

    private boolean btnIsEnable = false;

    private boolean hasMinDate = true;

    private LocalDateTime localDateTime;

    public DateTimePickerDialog() {
    }

    public DateTimePickerDialog(boolean hasMinDate) {
        this.hasMinDate = hasMinDate;
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
    @SuppressLint("InflateParams")
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        initView();
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setView(datetimepickerBinding.getRoot());
        return builder.create();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void initView() {
        localDateTime = LocalDateTime.now();
        datetimepickerBinding = DataBindingUtil.inflate(LayoutInflater.from(requireContext()),R.layout.datetimepicker,null,false);
        datetimepickerBinding.timePicker.setIs24HourView(true);
        datetimepickerBinding.timePicker.setHour(localDateTime.getHour());
        //判断是否有最小时间限制
        if (hasMinDate) {
            datetimepickerBinding.timePicker.setMinute(localDateTime.getMinute());
        }else {
            datetimepickerBinding.timePickerEnterBtn.setEnabled(true);
        }
        setListener();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void setListener() {

        //判断是否有最小时间限制
        if (hasMinDate) {

            //datePicker选择监听
            datetimepickerBinding.datePicker.setOnDateChangedListener(new DatePicker.OnDateChangedListener() {
                @Override
                public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    Log.d("imageView", "onDateChanged: " + year + "-" + monthOfYear + "-" + dayOfMonth);
                    datetimepickerBinding.timePickerEnterBtn.setEnabled(compareDate());
                }
            });

            //timePicker选择监听
            datetimepickerBinding.timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
                @Override
                public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                    Log.d("imageView", "onDateChanged: " + hourOfDay + "-" + minute);
                    datetimepickerBinding.timePickerEnterBtn.setEnabled(compareDate());
                }
            });

        }

        //弹窗关闭监听事件
        datetimepickerBinding.dateTimePickerCloseImgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Objects.requireNonNull(getDialog()).dismiss();
            }
        });

        //确定选择时间监听事件
        datetimepickerBinding.timePickerEnterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enterListener.enterBtnOnClicked(getSelectTimeString());
                Objects.requireNonNull(getDialog()).dismiss();
            }
        });
    }

    public interface EnterListener{
        void enterBtnOnClicked(String dateTimeStr);
    }

    public void setEnterClicked(EnterListener enterClicked) {
        enterListener = enterClicked;
    }

    //将选择的时间转成String类型
    @RequiresApi(api = Build.VERSION_CODES.O)
    public String getSelectTimeString() {
        int year,moth,day,hour,minute;
        year = datetimepickerBinding.datePicker.getYear();
        moth = datetimepickerBinding.datePicker.getMonth() + 1;
        day = datetimepickerBinding.datePicker.getDayOfMonth();
        hour = datetimepickerBinding.timePicker.getHour();
        minute = datetimepickerBinding.timePicker.getMinute();
        return DateTimeUtil.intToStrDateTime(year,moth,day,hour,minute);
    }

    //将选择的时间转成LocalDateTime类型
    @RequiresApi(api = Build.VERSION_CODES.O)
    public LocalDateTime getSelectedDate() {
        int year,moth,day,hour,minute;
        year = datetimepickerBinding.datePicker.getYear();
        moth = datetimepickerBinding.datePicker.getMonth() + 1;
        day = datetimepickerBinding.datePicker.getDayOfMonth();
        hour = datetimepickerBinding.timePicker.getHour();
        minute = datetimepickerBinding.timePicker.getMinute();
        return DateTimeUtil.intToLocalDateTime(year,moth,day,hour,minute);
    }

    //将选择的时间转成时间戳long类型
    @RequiresApi(api = Build.VERSION_CODES.O)
    public long getSelectedDateToLong() {
        int year,moth,day,hour,minute;
        year = datetimepickerBinding.datePicker.getYear();
        moth = datetimepickerBinding.datePicker.getMonth() + 1;
        day = datetimepickerBinding.datePicker.getDayOfMonth();
        hour = datetimepickerBinding.timePicker.getHour();
        minute = datetimepickerBinding.timePicker.getMinute();
        return DateTimeUtil.intToLong(year,moth,day,hour,minute);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private boolean compareDate() {
        int year,moth,day,hour,minute;
        year = datetimepickerBinding.datePicker.getYear();
        moth = datetimepickerBinding.datePicker.getMonth() + 1;
        day = datetimepickerBinding.datePicker.getDayOfMonth();
        hour = datetimepickerBinding.timePicker.getHour();
        minute = datetimepickerBinding.timePicker.getMinute();
        return localDateTime.compareTo(DateTimeUtil.intToLocalDateTime(year,moth,day,hour,minute)) <= 0;
    }

}
