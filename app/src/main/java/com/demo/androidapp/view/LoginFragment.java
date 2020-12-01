package com.demo.androidapp.view;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.demo.androidapp.MyApplication;
import com.demo.androidapp.R;
import com.demo.androidapp.databinding.LoginFragmentBinding;
import com.demo.androidapp.model.Auth;
import com.demo.androidapp.model.common.ReturnData;
import com.demo.androidapp.view.commom.MethodCommon;
import com.demo.androidapp.view.commom.MyTextWatcher;
import com.demo.androidapp.viewmodel.LoginViewModel;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;

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
//        binding = LoginFragmentBinding.inflate(inflater);
        binding.textInputLayoutPwd.setEndIconDrawable(R.drawable.pwdn);
        binding.textInputLayoutPwd.setEndIconCheckable(false);
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

        MethodCommon methodCommon = new MethodCommon();
        methodCommon.setEndIconOnClickListener(binding.textInputLayoutPwd);

//        binding.textInputLayoutPwd.setEndIconOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(binding.textInputLayoutPwd.isEndIconCheckable()){
//                    //设置文本框末尾的图标
//                    binding.textInputLayoutPwd.setEndIconDrawable(R.drawable.pwdn);
//                    binding.textInputLayoutPwd.setEndIconCheckable(false);
//                    //隐藏密码
//                    binding.textInputLayoutPwd.getEditText().setTransformationMethod(PasswordTransformationMethod.getInstance());
//                }else {
//                    Log.d("imageView","显示密码");
//                    //设置文本框末尾的图标
//                    binding.textInputLayoutPwd.setEndIconDrawable(R.drawable.pwd);
//                    binding.textInputLayoutPwd.setEndIconCheckable(true);
//                    //显示密码
//                    binding.textInputLayoutPwd.getEditText().setTransformationMethod(null);
//                }
//                //设置光标移至最后
//                binding.textInputLayoutPwd.getEditText().setSelection(binding.textInputLayoutPwd.getEditText().getText().length());
//            }
//        });

        //对页面所有TextInputEditText进行监听，所有的TextInputEditText不为空登录按钮才有效
        List<TextInputEditText> textInputEditTexts = new ArrayList<>();
        textInputEditTexts.add(binding.loginUserName);
        textInputEditTexts.add(binding.loginPassword);
        MyTextWatcher myTextWatcher = new MyTextWatcher(textInputEditTexts,binding.loginButton);
        myTextWatcher.addEditTextsChangeListener();
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
        loginViewModel.getReturnLiveData().observe(this,new Observer<ReturnData>() {
            @Override
            public void onChanged(ReturnData returnData) {
                Log.d("imageView", "ReturnData------onchange()" + returnData.getCode() + returnData.getContent());
                if (returnData.getCode() == 200) {
                    Toast.makeText(getActivity(), "登录成功", Toast.LENGTH_SHORT).show();

                    //上传数据，更新MyApplication中的数据
                    Auth auth = loginViewModel.getAuthLiveData().getValue();
                    if (auth != null) {
                        MyApplication.getApplication().saveData(requireContext(),
                                                                auth.getUserName(),
                                                                auth.getPassword());
                    }

                    Log.d("imageView", String.valueOf(returnData.getCode()));

                    //跳转主页
                    loginViewModel.jumpToHomeFragment(getView());
                } else {
                    Toast.makeText(getActivity(), "用户名或密码错误", Toast.LENGTH_SHORT).show();
                    Log.d("imageView", "用户名或密码错误");
                }
                //loginViewModel.getReturnLiveData().setValue(null);
            }
        });
        Log.d("imageView","onAttach");
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
