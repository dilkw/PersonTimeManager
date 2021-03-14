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

public class DateTimePickerDialog extends DialogFragment {

    private DatetimepickerBinding datetimepickerBinding;

    private EnterListener enterListener;

    private Date selectedDate;

    private boolean btnIsEnable = false;

    private int createYear, createMonthOfYear, createDayOfMonth,createHour, createMinute;

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
        LocalDateTime localDateTime = LocalDateTime.now();
        createYear = localDateTime.getYear();;
        createMonthOfYear = localDateTime.getMonthValue();
        createDayOfMonth = localDateTime.getDayOfMonth();
        createHour = localDateTime.getHour();
        createMinute = localDateTime.getMinute();
        Log.d("imageView", "onDateChanged: 创建" + createMonthOfYear + "-" + createDayOfMonth );
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        datetimepickerBinding = DataBindingUtil.inflate(LayoutInflater.from(requireContext()),R.layout.datetimepicker,null,false);
        setListener();
        datetimepickerBinding.timePicker.setIs24HourView(true);
        datetimepickerBinding.datePicker.setMinDate(DateTimeUtil.localDateTimeToLong(localDateTime));
        datetimepickerBinding.timePicker.setHour(createHour);
        datetimepickerBinding.timePicker.setMinute(createMinute);
        datetimepickerBinding.timePickerEnterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enterListener.enterBtnOnClicked(getSelectTimeString());
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
                Log.d("imageView", "onDateChanged: " + monthOfYear + "-" + dayOfMonth );
                btnIsEnable = (year > createYear || monthOfYear + 1 > createDayOfMonth || dayOfMonth > createDayOfMonth);
                datetimepickerBinding.timePickerEnterBtn.setEnabled(btnIsEnable);
            }
        });

        datetimepickerBinding.timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                Log.d("imageView", "onDateChanged: " + hourOfDay + "-" + minute );
                datetimepickerBinding.timePickerEnterBtn.setEnabled(btnIsEnable || (hourOfDay > createHour || (hourOfDay == createHour && (minute >= createMinute))));
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
        void enterBtnOnClicked(String dateTimeStr);
    }

    public void setEnterClicked(EnterListener enterClicked) {
        enterListener = enterClicked;
    }

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

}
