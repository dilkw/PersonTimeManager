package com.demo.androidapp.view;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

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
import com.demo.androidapp.model.entity.User;
import com.demo.androidapp.viewmodel.LoginViewModel;

import java.util.Objects;

public class LoginFragment extends Fragment implements View.OnClickListener {

    private LoginViewModel loginViewModel;

    private LoginFragmentBinding binding;

    public static LoginFragment newInstance() {
        return new LoginFragment();
    }

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
        setListener();
        setTextChangeWatcher();
    }

    //输入框输入监听
    private void setTextChangeWatcher() {
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

    //按钮设置监听事件
    private void setListener() {
        binding.loginButton.setOnClickListener(this);
        binding.registerTextView.setOnClickListener(this);
        binding.forgetPwd.setOnClickListener(this);
        binding.activeAccount.setOnClickListener(this);
    }

    //重写点击出发事件
    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.loginButton: {
                loginViewModel.login().observe(getViewLifecycleOwner(), new Observer<ReturnData<User>>() {
                    @Override
                    public void onChanged(ReturnData<User> userReturnData) {
                        if (userReturnData.getCode() == RCodeEnum.OK.getCode()) {
                            Toast.makeText(getActivity(), "登录成功", Toast.LENGTH_SHORT).show();
                            //上传数据，更新MyApplication中的数据
                            User user = userReturnData.getData();
                            Log.d("imageView", "" + user.toString());
                            user.setPassword(loginViewModel.getUserLiveData().getValue().getPassword());
                            MyApplication.getApplication().signIn(user);
                            //跳转主页
                            loginViewModel.jumpToHomeFragment(getView());
                        } else {
                            Toast.makeText(getActivity(), userReturnData.getMsg(), Toast.LENGTH_SHORT).show();
                            Log.d("imageView", "用户名或密码错误");
                        }
                    }
                });
                break;
            }
            case R.id.registerTextView: {
                Log.d("imageView","跳转注册页面");
                NavController navController = Navigation.findNavController(getView());
                navController.navigate(R.id.action_loginFragment_to_registerFragment);
                break;
            }
            case R.id.forgetPwd: {
                Log.d("imageView","跳转重置密码页面");
                NavController navController = Navigation.findNavController(getView());
                navController.navigate(R.id.action_loginFragment_to_retrievePasswordFragment);
                break;
            }
            case R.id.activeAccount: {
                Log.d("imageView","跳转激活账号页面");
                NavController navController = Navigation.findNavController(getView());
                navController.navigate(R.id.action_loginFragment_to_activeFragment);
                break;
            }
        }
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
