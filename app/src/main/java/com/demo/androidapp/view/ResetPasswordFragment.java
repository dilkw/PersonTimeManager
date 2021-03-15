package com.demo.androidapp.view;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.demo.androidapp.R;
import com.demo.androidapp.databinding.ResetPasswordFragmentBinding;
import com.demo.androidapp.model.common.RCodeEnum;
import com.demo.androidapp.model.common.ReturnData;
import com.demo.androidapp.model.entity.User;
import com.demo.androidapp.view.commom.MyEmailEditTextWatcher;
import com.demo.androidapp.view.commom.MyPwdConfirmEditTextWatcher;
import com.demo.androidapp.view.commom.MyPwdEditTextWatcher;
import com.demo.androidapp.view.commom.MyTextWatcher;
import com.demo.androidapp.viewmodel.ResetPasswordViewModel;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ResetPasswordFragment extends Fragment implements View.OnClickListener {

    private ResetPasswordViewModel resetPasswordViewModel;

    private ResetPasswordFragmentBinding resetPwdBinding;

    private CountDownTimer countDownTimer;

    private FragmentManager fragmentManager;

    private NavHostFragment navHostFragment;

    private NavController controller;

    private AppBarConfiguration appBarConfiguration;

    public static ResetPasswordFragment newInstance() {
        return new ResetPasswordFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        resetPwdBinding = DataBindingUtil.inflate(inflater,R.layout.reset_password_fragment,container,false);
        resetPwdBinding.setLifecycleOwner(this);
        fragmentManager = requireActivity().getSupportFragmentManager();
        navHostFragment = (NavHostFragment)fragmentManager.findFragmentById(R.id.fragment);
        assert navHostFragment != null;
        controller = navHostFragment.getNavController();
        appBarConfiguration = new AppBarConfiguration.Builder(controller.getGraph()).build();
        NavigationUI.setupWithNavController(resetPwdBinding.resetPwdFragmentToolBar,controller,appBarConfiguration);
        return resetPwdBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        resetPasswordViewModel = new ViewModelProvider(this).get(ResetPasswordViewModel.class);
        resetPwdBinding.setResetPasswordViewModel(resetPasswordViewModel);
        //为输入框添加监听事件，所有输入框不为空，重置按钮方可点击
        addMyTextWatcher();
        setOnClickListener();
    }

    //启动倒计时
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

    //添加文本编辑器监听事件
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


    //设置点击监听
    private void setOnClickListener() {
        resetPwdBinding.resetPwdButton.setOnClickListener(this);
        resetPwdBinding.resetPwdGetCodeBtn.setOnClickListener(this);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //重置密码按钮
            case R.id.resetPwdButton: {
                Log.d("imageView", "onClick: 重置按钮点击");
                resetPasswordViewModel.resetPwdCommit().observe(getViewLifecycleOwner(), new Observer<ReturnData<Object>>() {
                    @Override
                    public void onChanged(ReturnData<Object> userReturnReturnData) {
                        if (userReturnReturnData.getCode() == RCodeEnum.OK.getCode()) {
                            Toast.makeText(getContext(),"重置密码成功",Toast.LENGTH_SHORT).show();
                            controller.navigateUp();
                        }else {
                            Toast.makeText(getContext(),"重置密码成功" + userReturnReturnData.getMsg(),Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                break;
            }
            //获取验证码按钮
            case R.id.resetPwdGetCodeBtn: {
                Log.d("imageView", "onClick: 获取验证码按钮点击");
                resetPasswordViewModel.resetPwdGetCode().observe(getViewLifecycleOwner(), new Observer<ReturnData<Object>>() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onChanged(ReturnData<Object> userReturnReturnData) {
                        if (userReturnReturnData.getCode() == RCodeEnum.OK.getCode()) {
                            startCountDownTimer();
                            resetPwdBinding.resetPwdGetCodeBtn.setEnabled(false);
                            resetPwdBinding.resetPwdCodeSendTip.setText("验证码已成功发送到" + resetPasswordViewModel.resetPwdMutableLiveData.getValue().getEmail() + "邮箱，请注意查收！");
                            Toast.makeText(getContext(),"获取验证码成功",Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(getContext(),"获取验证码失败" + userReturnReturnData.getMsg(),Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                break;
            }
            default:{
                break;
            }
        }
    }
}