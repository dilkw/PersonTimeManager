package com.demo.androidapp.view;

import androidx.annotation.RequiresApi;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.demo.androidapp.MyApplication;
import com.demo.androidapp.R;
import com.demo.androidapp.databinding.ActiveFragmentBinding;
import com.demo.androidapp.model.common.RCodeEnum;
import com.demo.androidapp.model.common.ReturnData;
import com.demo.androidapp.model.entity.User;
import com.demo.androidapp.view.myView.IdentifyCodeView;
import com.demo.androidapp.viewmodel.ActiveViewModel;

import java.util.Objects;

public class ActiveFragment extends Fragment implements IdentifyCodeView.CodesChangedListener, View.OnClickListener {

    private ActiveViewModel activeViewModel;

    public static ActiveFragment newInstance() {
        return new ActiveFragment();
    }

    private ActiveFragmentBinding activeFragmentBinding;

    private FragmentManager fragmentManager;

    private NavHostFragment navHostFragment;

    private NavController controller;

    private AppBarConfiguration appBarConfiguration;

    private CountDownTimer countDownTimer;      //
    private CountDownTimer countDownTimer2;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        activeFragmentBinding = DataBindingUtil.inflate(inflater,R.layout.active_fragment,container,false);
        activeFragmentBinding.setLifecycleOwner(this);
        activeFragmentBinding.getRoot().setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (activeFragmentBinding.identifyCodeView.isFocused()) {
                    //binding.identifyCodeView.hideSoftInputOutOfVonClick();
                    activeFragmentBinding.identifyCodeView.clearFocus();
                }
                return true;
            }
        });
        fragmentManager = requireActivity().getSupportFragmentManager();
        navHostFragment = (NavHostFragment)fragmentManager.findFragmentById(R.id.fragment);
        assert navHostFragment != null;
        controller = navHostFragment.getNavController();
        appBarConfiguration = new AppBarConfiguration.Builder(controller.getGraph()).build();
        NavigationUI.setupWithNavController(activeFragmentBinding.activeFragmentToolBar,controller,appBarConfiguration);
        return activeFragmentBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        activeViewModel = new ViewModelProvider(this).get(ActiveViewModel.class);
        if (getArguments() != null) {
            activeViewModel.setEmail(getArguments().getString("email"));
            Log.d("imageView", "onActivityCreated: " + getArguments().getString("email"));
        }

        activeFragmentBinding.setActiveViewModel(activeViewModel);
        activeFragmentBinding.setView(activeFragmentBinding.identifyCodeView);
        activeFragmentBinding.identifyCodeView.addCodesChangeListener(codes -> {
            Log.d("imageView", "activityFragment:textChange" + codes);
            if (codes.length() == 6) {
                activeFragmentBinding.activeButton.setEnabled(true);
                Log.d("imageView", "activityFragment:textChange" + "按键有效");
            }else {
                activeFragmentBinding.activeButton.setEnabled(false);
            }
        });
        activeFragmentBinding.identifyCodeView.setOnLongClickListener();
        activeFragmentBinding.identifyCodeView.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                activeFragmentBinding.identifyCodeView.hideSoftInputOutOfVonClick();
            }
        });
        setOnClickListener();

        if (activeViewModel.getEmail() == null || activeViewModel.getEmail().equals("")) {
            NotFromRegisterFragment();
        }else {
            fromRegisterFragment();
        }

        activeViewModel.getCodesLiveData().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                Log.d("imageView", "onChanged: mViewModel观察者" + s);
            }
        });

        activeViewModel.getReturnLiveData().observe(getViewLifecycleOwner(), new Observer<ReturnData>() {
            @Override
            public void onChanged(ReturnData returnData) {
                if (returnData.getCode() == RCodeEnum.OK.getCode()) {
                    if (returnData.getData() == null) {
                        Toast.makeText(requireActivity(),"验证码已发送至您的邮箱",Toast.LENGTH_SHORT).show();
                        return;
                    }
                    controller.navigate(R.id.login_fragment);
                } else {
                    Toast.makeText(requireActivity(),returnData.getMsg() + "或者帐号已被注册并激活了",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    //从注册页面跳转过来激活帐号（带有email）
    private void fromRegisterFragment() {
        Log.d("imageView", "从注册页跳转");
        activeFragmentBinding.getCodeTipsTextView.setVisibility(View.VISIBLE);
        countDownTimer = new CountDownTimer(60000,1000) {
            @SuppressLint("SetTextI18n")
            @Override
            public void onTick(long millisUntilFinished) {
                Log.d("imageView", "倒计时：");
                activeFragmentBinding.getCodeTipsTextView.setClickable(false);
                activeFragmentBinding.getCodeTipsTextView.setTextColor(getResources().getColor(R.color.colorTextFalse));
                activeFragmentBinding.getCodeTipsTextView.setText((millisUntilFinished / 1000) + "s获取验证码");
            }

            @Override
            public void onFinish() {
                activeFragmentBinding.getCodeTipsTextView.setText("获取验证码");
                activeFragmentBinding.getCodeTipsTextView.setTextColor(getResources().getColor(R.color.colorTextTrue));
                activeFragmentBinding.getCodeTipsTextView.setClickable(true);
            }
        };
        countDownTimer.start();
        //重新获取验证码（访问获取验证码接口）
    }


    //不是从注册页面跳转过来激活帐号（没带有email）
    private void NotFromRegisterFragment() {
        Log.d("imageView", "非注册页跳转");
        activeFragmentBinding.activeGetCodeBtn.setVisibility(View.VISIBLE);
        activeFragmentBinding.activeTextInputLayoutEmail.setVisibility(View.VISIBLE);

        activeFragmentBinding.activeTextInputEditEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().matches(getResources().getString(R.string.emailCheckStr)) && s.length() > 0) {
                    activeFragmentBinding.activeTextInputLayoutEmail.setError("邮箱格式错误");
                }else {
                    activeFragmentBinding.activeGetCodeBtn.setEnabled(s.length() > 0);
                    activeFragmentBinding.activeTextInputLayoutEmail.setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        //按钮
        countDownTimer2 = new CountDownTimer(60000,1000) {
            @SuppressLint("SetTextI18n")
            @Override
            public void onTick(long millisUntilFinished) {
                Log.d("imageView", "倒计时：");
                activeFragmentBinding.activeGetCodeBtn.setEnabled(false);
                activeFragmentBinding.activeGetCodeBtn.setText((millisUntilFinished / 1000) + "s获取验证码");
            }

            @Override
            public void onFinish() {
                activeFragmentBinding.activeGetCodeBtn.setText("获取验证码");
                activeFragmentBinding.activeGetCodeBtn.setEnabled(true);
            }
        };
        activeViewModel.getReturnLiveData().observe(requireActivity(), new Observer<ReturnData>() {
            @Override
            public void onChanged(ReturnData returnData) {
                if (returnData.getCode() == RCodeEnum.OK.getCode()) {
                    Log.d("imageView", "getActiveCodes: 获取验证码成功");
                    countDownTimer2.start();
                    activeFragmentBinding.codeHasBeenSentText.setVisibility(View.VISIBLE);
                }else {
                    Log.d("imageView", "getActiveCodes: 获取验证码失败");
                    Toast.makeText(getActivity(),returnData.getMsg(),Toast.LENGTH_SHORT).show();
                    activeFragmentBinding.activeGetCodeBtn.setEnabled(false);
                    activeFragmentBinding.activeTextInputLayoutEmail.setError(returnData.getMsg());
                }
            }
        });
//        countDownTimer2.start();
    }

    @Override
    public void textChanged(String codes) {
        if (codes.length() == 6) {
            activeFragmentBinding.activeButton.setEnabled(true);
        }
    }

    private void setOnClickListener() {
        activeFragmentBinding.activeGetCodeBtn.setOnClickListener(this);
        activeFragmentBinding.getCodeTipsTextView.setOnClickListener(this);
        activeFragmentBinding.activeButton.setOnClickListener(this);
    }

    @SuppressLint({"NonConstantResourceId", "ResourceAsColor"})
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.activeGetCodeBtn: {
                activeViewModel.setEmail(Objects.requireNonNull(activeFragmentBinding.activeTextInputEditEmail.getText()).toString());
                getActiveCode();
                countDownTimer2.start();
                Log.d("imageView", "getActiveCodes: 获取验证码");
                break;
            }
            case R.id.getCodeTipsTextView: {
                getActiveCode();
                countDownTimer.start();
                break;
            }
            case R.id.activeButton: {
                String code = activeFragmentBinding.identifyCodeView.getCodes();
                activeViewModel.active(code).observe(getViewLifecycleOwner(), new Observer<ReturnData<Object>>() {
                    @Override
                    public void onChanged(ReturnData<Object> objectReturnData) {
                        if (objectReturnData.getCode() == RCodeEnum.OK.getCode()) {
                            Toast.makeText(getContext(), "激活成功", Toast.LENGTH_SHORT).show();
                            controller.navigate(R.id.action_activeFragment_to_loginFragment);
                        } else {
                            Toast.makeText(getContext(), objectReturnData.getMsg(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }
    }

    private void getActiveCode() {
        activeViewModel.getActiveCodes().observe(getViewLifecycleOwner(), new Observer<ReturnData<Object>>() {
            @Override
            public void onChanged(ReturnData<Object> objectReturnData) {
                if (objectReturnData.getCode() == RCodeEnum.OK.getCode()) {
                    Toast.makeText(getContext(), "获取验证码成功", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), objectReturnData.getMsg(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onStop() {
        super.onStop();
        if (countDownTimer != null) {
            countDownTimer.onFinish();
        }
        if (countDownTimer2 != null) {
            countDownTimer2.onFinish();
        }
    }
}