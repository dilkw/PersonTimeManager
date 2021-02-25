package com.demo.androidapp.view;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.demo.androidapp.R;
import com.demo.androidapp.databinding.ResetPasswordFragmentBinding;
import com.demo.androidapp.model.common.RCodeEnum;
import com.demo.androidapp.model.common.ReturnData;
import com.demo.androidapp.view.commom.MyEmailEditTextWatcher;
import com.demo.androidapp.view.commom.MyPwdConfirmEditTextWatcher;
import com.demo.androidapp.view.commom.MyPwdEditTextWatcher;
import com.demo.androidapp.view.commom.MyTextWatcher;
import com.demo.androidapp.viewmodel.ResetPasswordViewModel;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ResetPasswordFragment extends Fragment {

    private ResetPasswordViewModel mViewModel;

    private ResetPasswordFragmentBinding resetPwdBinding;

    private CountDownTimer countDownTimer;

    public static ResetPasswordFragment newInstance() {
        return new ResetPasswordFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        resetPwdBinding = DataBindingUtil.inflate(inflater,R.layout.reset_password_fragment,container,false);
        return resetPwdBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(ResetPasswordViewModel.class);
        mViewModel.getRepositoryMutableLiveData().observe(getViewLifecycleOwner(), new Observer<ReturnData>() {
            @Override
            public void onChanged(ReturnData returnData) {
                if (returnData.getCode() == RCodeEnum.OK.getCode()) {
                    startCountDownTimer();
                    resetPwdBinding.resetPwdGetCodeBtn.setEnabled(false);
                    resetPwdBinding.resetPwTextInputEditTextEmail.setEnabled(false);
                }else {
                    Toast.makeText(getActivity(),returnData.getMsg(),Toast.LENGTH_SHORT).show();
                }
            }
        });

        //为输入框添加监听事件，所有输入框不为空，重置按钮方可点击
        addMyTextWatcher();

        resetPwdBinding.resetPwdButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewModel.resetPwdGetCode(Objects.requireNonNull(resetPwdBinding.resetPwTextInputEditTextEmail.getText()).toString());
            }
        });

        resetPwdBinding.resetPwdGetCodeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewModel.resetPwdGetCode(resetPwdBinding.resetPwTextInputEditTextEmail.getText().toString());
            }
        });
    }

    public void startCountDownTimer() {
        if (countDownTimer == null) {
            countDownTimer = new CountDownTimer(60000, 1000) {
                @SuppressLint("SetTextI18n")
                @Override
                public void onTick(long millisUntilFinished) {
                    resetPwdBinding.resetPwdGetCodeBtn.setText((millisUntilFinished / 1000) + "s\n获取验证码");
                }

                @Override
                public void onFinish() {
                    resetPwdBinding.resetPwdGetCodeBtn.setEnabled(true);
                    resetPwdBinding.resetPwTextInputEditTextEmail.setEnabled(true);
                    resetPwdBinding.resetPwdGetCodeBtn.setText("获取验证码");
                }

            };
        }
        countDownTimer.start();
    }

    public void addMyTextWatcher() {
        List<TextInputEditText> textInputEditTexts = new ArrayList<>();
        textInputEditTexts.add(resetPwdBinding.resetPwTextInputEditTextEmail);
        textInputEditTexts.add(resetPwdBinding.resetPwTextInputEditTextPw);
        textInputEditTexts.add(resetPwdBinding.resetPwTextInputEditTextPwConfirm);
        textInputEditTexts.add(resetPwdBinding.resetPwTextInputEditTextCode);
        MyTextWatcher myTextWatcher = new MyTextWatcher();
        myTextWatcher.addTextWatcher(textInputEditTexts,resetPwdBinding.resetPwdButton);

        MyPwdEditTextWatcher myPwdEditTextWatcher = new MyPwdEditTextWatcher(resetPwdBinding.resetPwTextInputEditTextPw,
                                                                                resetPwdBinding.resetPwTextInputEditTextPwConfirm,
                                                                                resetPwdBinding.resetPwdButton);
        MyPwdConfirmEditTextWatcher myPwdConfirmEditTextWatcher = new MyPwdConfirmEditTextWatcher(resetPwdBinding.resetPwTextInputEditTextPw,
                                                                                            resetPwdBinding.resetPwTextInputEditTextPwConfirm);


        MyEmailEditTextWatcher myEmailEditTextWatcher = new MyEmailEditTextWatcher(resetPwdBinding.resetPwTextInputEditTextEmail,
                                                                                        resetPwdBinding.resetPwdGetCodeBtn);


        resetPwdBinding.resetPwTextInputEditTextEmail.addTextChangedListener(myEmailEditTextWatcher);
        resetPwdBinding.resetPwTextInputEditTextPw.addTextChangedListener(myPwdEditTextWatcher);
        resetPwdBinding.resetPwTextInputEditTextPwConfirm.addTextChangedListener(myPwdConfirmEditTextWatcher);
    }
}