package com.demo.androidapp.view;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.demo.androidapp.MainActivity;
import com.demo.androidapp.R;
import com.demo.androidapp.databinding.ActiveFragmentBinding;
import com.demo.androidapp.model.common.RCodeEnum;
import com.demo.androidapp.model.common.ReturnData;
import com.demo.androidapp.view.myView.IdentifyCodeView;
import com.demo.androidapp.viewmodel.ActiveViewModel;

import java.util.Objects;

public class ActiveFragment extends Fragment implements IdentifyCodeView.CodesChangedListener {

    private ActiveViewModel mViewModel;

    public static ActiveFragment newInstance() {
        return new ActiveFragment();
    }

    private ActiveFragmentBinding binding;

    private CountDownTimer countDownTimer;      //
    private CountDownTimer countDownTimer2;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        //View view = inflater.inflate(R.layout.active_fragment, container, false);
        binding = DataBindingUtil.inflate(inflater,R.layout.active_fragment,container,false);
        binding.getRoot().setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (binding.identifyCodeView.isFocused()) {
                    //binding.identifyCodeView.hideSoftInputOutOfVonClick();
                    binding.identifyCodeView.clearFocus();
                }
                return true;
            }
        });
        //this.getActivity().getSupportFragmentManager().putFragment(null,"activeFragment",this);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(ActiveViewModel.class);
        if (getArguments() != null) {
            mViewModel.setEmail(getArguments().getString("email"));
            Log.d("imageView", "onActivityCreated: " + getArguments().getString("email"));
        }

        binding.setActiveViewModel(mViewModel);
        binding.setView(binding.identifyCodeView);
        binding.identifyCodeView.addCodesChangeListener(codes -> {
            Log.d("imageView", "activityFragment:textChange" + codes);
            if (codes.length() == 6) {
                binding.activeButton.setEnabled(true);
                Log.d("imageView", "activityFragment:textChange" + "按键有效");
            }else {
                binding.activeButton.setEnabled(false);
            }
        });
        binding.identifyCodeView.setOnLongClickListener();
        binding.identifyCodeView.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                binding.identifyCodeView.hideSoftInputOutOfVonClick();
            }
        });

        if (mViewModel.getEmail() == null) {
            NotFromRegisterFragment();
        }else {
            fromRegisterFragment();
        }

        mViewModel.getCodesLiveData().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                Log.d("imageView", "onChanged: mViewModel观察者" + s);
            }
        });

        mViewModel.getReturnLiveData().observe(getViewLifecycleOwner(), new Observer<ReturnData>() {
            @Override
            public void onChanged(ReturnData returnData) {
                if (returnData.getCode() == RCodeEnum.OK.getCode()) {
                    if (returnData.getData() == null) {
                        Toast.makeText(requireActivity(),"验证码已发送至您的邮箱",Toast.LENGTH_SHORT).show();
                        return;
                    }
                    NavController navController = Navigation.findNavController(binding.activeButton);
                    navController.navigate(R.id.login_fragment);
                } else {
                    Toast.makeText(requireActivity(),returnData.getMsg() + "或者帐号已被注册并激活了",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    //从注册页面跳转过来激活帐号（带有email）
    private void fromRegisterFragment() {
        binding.getCodeTipsTextView.setVisibility(View.VISIBLE);
        countDownTimer = new CountDownTimer(60000,1000) {
            @SuppressLint("SetTextI18n")
            @Override
            public void onTick(long millisUntilFinished) {
                Log.d("imageView", "倒计时：");
                binding.getCodeTipsTextView.setClickable(false);
                binding.getCodeTipsTextView.setTextColor(getResources().getColor(R.color.colorTextFalse));
                binding.getCodeTipsTextView.setText((millisUntilFinished / 1000) + "s获取验证码");
            }

            @Override
            public void onFinish() {
                binding.getCodeTipsTextView.setText("获取验证码");
                binding.getCodeTipsTextView.setTextColor(getResources().getColor(R.color.colorTextTrue));
                binding.getCodeTipsTextView.setClickable(true);
            }
        };
        countDownTimer.start();
        //重新获取验证码（访问获取验证码接口）
        binding.getCodeTipsTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewModel.getActiveCodes();
                countDownTimer.start();
            }
        });
    }


    //从注册页面跳转过来激活帐号（带有email）
    private void NotFromRegisterFragment() {
        binding.activeGetCodeBtn.setVisibility(View.VISIBLE);
        binding.activeTextInputLayoutEmail.setVisibility(View.VISIBLE);
        binding.activeGetCodeBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                mViewModel.setEmail(Objects.requireNonNull(binding.activeTextInputEditEmail.getText()).toString());
                mViewModel.getActiveCodes();
                Log.d("imageView", "getActiveCodes: 获取验证码");
            }
        });

        binding.activeTextInputEditEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().matches(getResources().getString(R.string.emailCheckStr)) && s.length() > 0) {
                    binding.activeTextInputLayoutEmail.setError("邮箱格式错误");
                }else {
                    binding.activeGetCodeBtn.setEnabled(s.length() > 0);
                    binding.activeTextInputLayoutEmail.setErrorEnabled(false);
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
                binding.activeGetCodeBtn.setEnabled(false);
                binding.activeGetCodeBtn.setText((millisUntilFinished / 1000) + "s获取验证码");
            }

            @Override
            public void onFinish() {
                binding.activeGetCodeBtn.setText("获取验证码");
                binding.activeGetCodeBtn.setEnabled(true);
            }
        };

        mViewModel.getReturnLiveData().observe(requireActivity(), new Observer<ReturnData>() {
            @Override
            public void onChanged(ReturnData returnData) {
                if (returnData.getCode() == RCodeEnum.OK.getCode()) {
                    Log.d("imageView", "getActiveCodes: 获取验证码成功");
                    countDownTimer2.start();
                    binding.codeHasBeenSentText.setVisibility(View.VISIBLE);
                }else {
                    Log.d("imageView", "getActiveCodes: 获取验证码失败");
                    Toast.makeText(getActivity(),returnData.getMsg(),Toast.LENGTH_SHORT).show();
                    binding.activeGetCodeBtn.setEnabled(false);
                    binding.activeTextInputLayoutEmail.setError(returnData.getMsg());
                }
            }
        });
    }

    @Override
    public void textChanged(String codes) {
        if (codes.length() == 6) {
            binding.activeButton.setEnabled(true);
        }
    }
}