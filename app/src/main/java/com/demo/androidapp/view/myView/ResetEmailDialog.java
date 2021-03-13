package com.demo.androidapp.view.myView;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.Observer;

import com.demo.androidapp.MyApplication;
import com.demo.androidapp.R;
import com.demo.androidapp.api.Api;
import com.demo.androidapp.databinding.ResetemailDialogBinding;
import com.demo.androidapp.model.common.RCodeEnum;
import com.demo.androidapp.model.common.ReturnData;

import java.util.Objects;

public class ResetEmailDialog extends DialogFragment implements View.OnClickListener {

    private ResetemailDialogBinding resetemailDialogBinding;

    private EnterListener enterListener;

    private String email;

    private boolean enterBtnClickable = false;

    private Api api;

    private CountDownTimer countDownTimer;

    public ResetEmailDialog(String email) {
        this.email = email;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        Log.d("imageView", "onActivityCreated: ");
        setListener();
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        Log.d("imageView", "onCreate: ");
        super.onCreate(savedInstanceState);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @SuppressLint({"InflateParams", "SetTextI18n"})
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Log.d("imageView", "onCreateDialog: ");
        resetemailDialogBinding = DataBindingUtil.inflate(LayoutInflater.from(requireContext()),R.layout.resetemail_dialog,null,false);
        View contentView = resetemailDialogBinding.getRoot();
        addTextChangeWatcher();
        AlertDialog alertDialog = new AlertDialog
                .Builder(requireContext())
                .setView(contentView)
                .setCancelable(false)
                .create();
        return alertDialog;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d("imageView", "onCreateView: ");
        return super.onCreateView(inflater, container, savedInstanceState);

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void setListener() {
        resetemailDialogBinding.resetEmailEnterBtn.setOnClickListener(this);
        resetemailDialogBinding.resetEmailDialogCloseImgBtn.setOnClickListener(this);
        resetemailDialogBinding.resetEmailGetCodeButton.setOnClickListener(this);
    }

    private void addTextChangeWatcher() {
        TextWatcher textWatcher =  new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Editable e1  = resetemailDialogBinding.resetEmailDialogNewEmailTextInputEditText.getText();
                Editable e2  = resetemailDialogBinding.resetEmailDialogCodeInputEditText.getText();
                resetemailDialogBinding.resetEmailEnterBtn.setEnabled(e1 != null && e2 != null && !e1.toString().equals("") && !e2.toString().equals(""));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };
        resetemailDialogBinding.resetEmailDialogNewEmailTextInputEditText.addTextChangedListener(textWatcher);
        resetemailDialogBinding.resetEmailDialogCodeInputEditText.addTextChangedListener(textWatcher);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.resetEmailEnterBtn: {
                enterResetEmailDialog();
                break;
            }
            case R.id.resetEmailDialogCloseImgBtn: {
                dismiss();
                break;
            }
            case R.id.resetEmailGetCodeButton: {
                if (api == null) {
                    api = MyApplication.getApi();
                }
                getCodeNetWork();
                break;
            }
        }
    }

    public interface EnterListener{
        void enterBtnOnClicked(String newEmail,String code);
    }

    public void setEnterClicked(EnterListener enterClicked) {
        enterListener = enterClicked;
    }

    private void enterResetEmailDialog() {
        String msg = "是否确定更换邮箱";
        AlertDialog alertDialog = new AlertDialog.Builder(getContext())
                .setTitle("提示")
                .setMessage(msg)
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String newEmail = resetemailDialogBinding.resetEmailDialogNewEmailTextInputEditText.getText().toString().trim();
                        String code = resetemailDialogBinding.resetEmailDialogCodeInputEditText.getText().toString().trim();
                        enterListener.enterBtnOnClicked(newEmail,code);
                        dismiss();
                    }
                })
                .create();
        alertDialog.show();
    }

    private void setCountDownTimer() {
        if (countDownTimer == null) {
            countDownTimer = new CountDownTimer(60000, 1000) {
                @SuppressLint("SetTextI18n")
                @Override
                public void onTick(long millisUntilFinished) {
                    Log.d("imageView", "倒计时：");
                    resetemailDialogBinding.resetEmailGetCodeButton.setClickable(false);
                    resetemailDialogBinding.resetEmailGetCodeButton.setTextColor(requireActivity().getResources().getColor(R.color.colorTextFalse));
                    resetemailDialogBinding.resetEmailGetCodeButton.setText((millisUntilFinished / 1000) + "s获取验证码");
                }

                @Override
                public void onFinish() {
                    resetemailDialogBinding.resetEmailGetCodeButton.setText("获取验证码");
                    resetemailDialogBinding.resetEmailGetCodeButton.setTextColor(requireActivity().getResources().getColor(R.color.colorTextTrue));
                    resetemailDialogBinding.resetEmailGetCodeButton.setClickable(true);
                }
            };
        }
        countDownTimer.start();
    }

    //请求验证码
    private void getCodeNetWork() {
        api.getResetEmailCode(email).observe(this, new Observer<ReturnData<Object>>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onChanged(ReturnData<Object> objectReturnData) {
                if (objectReturnData.getCode() == RCodeEnum.OK.getCode()) {
                    Toast.makeText(getContext(),"验证码已发送",Toast.LENGTH_SHORT).show();
                    setCountDownTimer();
                    resetemailDialogBinding.resetEmailCodeSendTip.setVisibility(View.VISIBLE);
                    resetemailDialogBinding.resetEmailCodeSendTip.setText("验证码已发送至" + email + "邮箱");
                }else {
                    Toast.makeText(getContext(),"验证码获取失败" + objectReturnData.getMsg(),Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void dismiss() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        super.dismiss();
    }
}
