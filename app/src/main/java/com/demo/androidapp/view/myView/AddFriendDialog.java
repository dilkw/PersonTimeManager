package com.demo.androidapp.view.myView;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;

import com.demo.androidapp.R;
import com.demo.androidapp.databinding.AddbillDialogBinding;
import com.demo.androidapp.databinding.AddfriendDialogBinding;
import com.demo.androidapp.model.entity.Bill;
import com.demo.androidapp.model.entity.Friend;
import com.demo.androidapp.util.DateTimeUtil;

import java.sql.Date;
import java.time.LocalDateTime;

public class AddFriendDialog extends DialogFragment implements View.OnClickListener {

    private AddfriendDialogBinding addfriendDialogBinding;

    private EnterListener enterListener;

    private Date selectedDate;

    private boolean isAdd = false;

    private Friend friend;


    public AddFriendDialog(){
        friend = new Friend();
    }

    public AddFriendDialog(Friend friend) {
        this.isAdd = false;
        this.friend = friend;
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
        addfriendDialogBinding = DataBindingUtil.inflate(LayoutInflater.from(requireContext()),R.layout.addfriend_dialog,null,false);
        View contentView = addfriendDialogBinding.getRoot();
        String dialogTitle = "添加账单";
        if (!isAdd) {
            dialogTitle = "编辑账单";
            addfriendDialogBinding.addFriendDialogContentTextInputEditText.setText("friend.getContent()");
        }
        addfriendDialogBinding.addFriendTitleTextView.setText(dialogTitle);
        setListener();
        AlertDialog alertDialog = new AlertDialog
                .Builder(requireContext())
                .setView(contentView)
                .create();
        return alertDialog;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void setListener() {
        addfriendDialogBinding.addFriendDialogCloseImgBtn.setOnClickListener(this);
        addfriendDialogBinding.addFriendEnterBtn.setOnClickListener(this);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.addFriendEnterBtn: {
                if (addfriendDialogBinding.addFriendDialogContentTextInputEditText.getText()==null ||
                        addfriendDialogBinding.addFriendDialogContentTextInputEditText.getText().toString().equals("")) {
                    Toast.makeText(getContext(),"id不能为空",Toast.LENGTH_SHORT).show();
                    return;
                }
                enterListener.enterBtnOnClicked();
                dismiss();
                break;
            }
            case R.id.addFriendDialogCloseImgBtn: {
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
    public String getFriendEmail() {
        String friendEmail = addfriendDialogBinding.addFriendDialogContentTextInputEditText.getText().toString();
        return friendEmail;
    }

}
