package com.demo.androidapp.view;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.demo.androidapp.MyApplication;
import com.demo.androidapp.R;
import com.demo.androidapp.databinding.RegisterFragmentBinding;
import com.demo.androidapp.model.common.ReturnData;
import com.demo.androidapp.view.commom.MethodCommon;
import com.demo.androidapp.view.commom.MyTextWatcher;
import com.demo.androidapp.viewmodel.RegisterViewModel;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;

public class RegisterFragment extends Fragment {

    private RegisterViewModel registerViewModel;

    private RegisterFragmentBinding registerBinding;

    public static RegisterFragment newInstance() {
        return new RegisterFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        registerBinding = DataBindingUtil.inflate(inflater,R.layout.register_fragment,container,false);
//        registerBinding = RegisterFragmentBinding.inflate(inflater);
        registerBinding.registerPwd.setEndIconDrawable(R.drawable.pwdn);
        registerBinding.registerPwd.setEndIconCheckable(false);
        registerBinding.registerPwdConfirm.setEndIconDrawable(R.drawable.pwdn);
        registerBinding.registerPwdConfirm.setEndIconCheckable(false);
        return registerBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        registerViewModel = new ViewModelProvider(this).get(RegisterViewModel.class);
        registerBinding.setRegisterViewModel(registerViewModel);
        registerViewModel.getReturnData().observe(getViewLifecycleOwner(), new Observer<ReturnData>() {
            @Override
            public void onChanged(ReturnData returnData) {
                Log.d("imageView","ReturnData------onchange()" + returnData.getCode() + returnData.getContent());
                if(returnData.getCode() == 200) {
                    MyApplication.getApplication().loadData(requireContext());
                    //跳转激活页面
                    //NavController navController = Navigation.findNavController(getView());
                    //navController.navigate(R.id.action_registerFragment_to_activeFragment);
                    registerViewModel.jumpToActiveFragment(getView());
                }else {
                    Toast.makeText(getActivity(),"用户名已被注册",Toast.LENGTH_SHORT).show();
                    Log.d("imageView","注册失败");
                }
            }
        });

        MethodCommon methodCommon = new MethodCommon();
        methodCommon.setEndIconOnClickListener(registerBinding.registerPwd);
        methodCommon.setEndIconOnClickListener(registerBinding.registerPwdConfirm);


        //对页面所有TextInputEditText进行监听，所有的TextInputEditText不为空登录按钮才有效
        List<TextInputEditText> textInputEditTexts = new ArrayList<>();
        textInputEditTexts.add(registerBinding.registerUserName);
        textInputEditTexts.add(registerBinding.registerEmail);
        textInputEditTexts.add(registerBinding.registerPassword);
        textInputEditTexts.add(registerBinding.registerPasswordConfirm);
        MyTextWatcher myTextWatcher = new MyTextWatcher(textInputEditTexts,registerBinding.registerButton);
        myTextWatcher.addEditTextsChangeListener();
//        registerBinding.registerPassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View v, boolean hasFocus) {
//                if(registerBinding.registerPassword.isInEditMode()) {
//                    registerBinding.registerPwd.setHelperTextEnabled(true);
//                }else {
//                    registerBinding.registerPwd.setHelperTextEnabled(false);
//                }
//            }
//
//        });


    }

}
