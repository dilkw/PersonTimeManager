package com.demo.androidapp.view;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.demo.androidapp.MyApplication;
import com.demo.androidapp.R;
import com.demo.androidapp.databinding.LoginFragmentBinding;
import com.demo.androidapp.model.common.RCodeEnum;
import com.demo.androidapp.model.common.ReturnData;
import com.demo.androidapp.model.returnObject.LoginAndRegisterReturn;
import com.demo.androidapp.view.commom.MethodCommon;
import com.demo.androidapp.view.commom.MyTextWatcher;
import com.demo.androidapp.viewmodel.LoginViewModel;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class LoginFragment extends Fragment {

    private LoginViewModel loginViewModel;

    private LoginFragmentBinding binding;

//    public static LoginFragment newInstance() {
//        return new LoginFragment();
//    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        Log.d("imageView","onCreateView");
        binding = DataBindingUtil.inflate(inflater,R.layout.login_fragment,container,false);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d("imageView","onActivityCreated");
        binding.setLoginViewModel(loginViewModel);
        binding.myImageView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        binding.loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginViewModel.signInLiveData().observe(getViewLifecycleOwner(), new Observer<ReturnData<LoginAndRegisterReturn>>() {
                    @Override
                    public void onChanged(ReturnData<LoginAndRegisterReturn> loginAndRegisterReturnReturnData) {
                        if (loginAndRegisterReturnReturnData.getCode() == RCodeEnum.OK.getCode()) {
                            Toast.makeText(getActivity(), "登录成功", Toast.LENGTH_SHORT).show();
                            //上传数据，更新MyApplication中的数据
                            LoginAndRegisterReturn loginAndRegisterReturn = (LoginAndRegisterReturn)loginAndRegisterReturnReturnData.getData();
                            Log.d("imageView", "" + loginAndRegisterReturn.toString());
                            MyApplication.getApplication().signIn(loginAndRegisterReturn.getName(),
                                    loginViewModel.getAuthLiveData().getValue().getPassword(),
                                    loginAndRegisterReturn.getUid());
                            //跳转主页
                            loginViewModel.jumpToHomeFragment(getView());
                        } else {
                            Toast.makeText(getActivity(), loginAndRegisterReturnReturnData.getMsg(), Toast.LENGTH_SHORT).show();
                            Log.d("imageView", "用户名或密码错误");
                        }
                    }
                });
            }
        });

        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (Objects.requireNonNull(binding.loginUserName.getText()).length() > 0
                        && Objects.requireNonNull(binding.loginPassword.getText()).length() > 0) {
                    binding.loginButton.setEnabled(true);
                }else {
                    binding.loginButton.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };
        binding.loginUserName.addTextChangedListener(textWatcher);
        binding.loginPassword.addTextChangedListener(textWatcher);
    }

    //fragment实现物理返回按键监听事件思路：
    //通过在Activity中重写返回键监听事件函数
    //在fragment中调用
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (loginViewModel == null) {
            Log.d("imageView","重新创建");
            loginViewModel = new ViewModelProvider(this).get(LoginViewModel.class);
        }

//        loginViewModel.getReturnLiveData().observe(this,new Observer<ReturnData<LoginAndRegisterReturn>>() {
//            @Override
//            public void onChanged(ReturnData returnData) {
//                if (returnData.getCode() == RCodeEnum.OK.getCode()) {
//                    Toast.makeText(getActivity(), "登录成功", Toast.LENGTH_SHORT).show();
//                    //上传数据，更新MyApplication中的数据
//                    LoginAndRegisterReturn loginAndRegisterReturn = (LoginAndRegisterReturn)loginViewModel.getReturnLiveData().getValue().getData();
//                    MyApplication.getApplication().signIn(loginAndRegisterReturn.getName(),
//                                                            loginViewModel.getAuthLiveData().getValue().getPassword(),
//                                                            loginAndRegisterReturn.getUid());
//                    //跳转主页
//                    loginViewModel.jumpToHomeFragment(getView());
//                } else {
//                    Toast.makeText(getActivity(), returnData.getMsg(), Toast.LENGTH_SHORT).show();
//                    Log.d("imageView", "用户名或密码错误");
//                }
//            }
//        });
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("imageView","onCreate");
//        loginViewModel = new ViewModelProvider(this).getClass()
//        returnDataLiveData = loginViewModel.getReturnLiveData();
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d("imageView","onStart");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("imageView","onResume");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d("imageView","onPause");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d("imageView","onStop");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d("imageView","onDestroyView");
//        returnDataLiveData.removeObservers(getViewLifecycleOwner());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("imageView","onDestroy");
}

    @Override
    public void onDetach() {
        Log.d("imageView","onDetach");
        super.onDetach();
    }
}
