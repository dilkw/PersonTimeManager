package com.demo.androidapp.view;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.annotation.SuppressLint;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.demo.androidapp.MyApplication;
import com.demo.androidapp.R;
import com.demo.androidapp.databinding.RegisterFragmentBinding;
import com.demo.androidapp.model.common.RCodeEnum;
import com.demo.androidapp.model.common.ReturnData;
import com.demo.androidapp.view.commom.MethodCommon;
import com.demo.androidapp.view.commom.MyTextWatcher;
import com.demo.androidapp.viewmodel.RegisterViewModel;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class RegisterFragment extends Fragment {

    private RegisterViewModel registerViewModel;

    private RegisterFragmentBinding registerBinding;

    private List<TextInputEditText> textInputEditTexts = new ArrayList<>();

    private int isActive = 0;

    private boolean isNull = true;

    private boolean isNotOk = true;

    private int userNameTextEditId = R.id.registerUserName;
    private int passwordConfirmTextEditId = R.id.registerPasswordConfirm;

    public static RegisterFragment newInstance() {
        return new RegisterFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        registerBinding = DataBindingUtil.inflate(inflater,R.layout.register_fragment,container,false);
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
                Log.d("imageView","ReturnData------onchange()" + returnData.getCode() + returnData.getData());
                if(returnData.getCode() == RCodeEnum.OK.getCode()) {
                    //跳转激活页面
                    Log.d("imageView","注册成功" + returnData.getCode());
                    Bundle bundle = new Bundle();
                    bundle.putString("email",registerViewModel.getRegisterCommitLiveData().getValue().getEmail());
                    NavController navController = Navigation.findNavController(registerBinding.registerButton);
                    navController.navigate(R.id.action_registerFragment_to_activeFragment);
                }else {
                    Toast.makeText(getActivity(),returnData.getMsg(),Toast.LENGTH_SHORT).show();
                    Log.d("imageView","注册失败" + returnData.getCode());
                }
            }
        });

//        MethodCommon methodCommon = new MethodCommon();
//        methodCommon.setEndIconOnClickListener(registerBinding.textInputLayoutPassword);
//        methodCommon.setEndIconOnClickListener(registerBinding.textInputLayoutPwdConfirm);

        //对页面所有TextInputEditText进行监听，所有的TextInputEditText不为空登录按钮才有效
        textInputEditTexts.add(registerBinding.registerUserName);
        textInputEditTexts.add(registerBinding.registerEmail);
        textInputEditTexts.add(registerBinding.registerPassword);
        textInputEditTexts.add(registerBinding.registerPasswordConfirm);
        addTextWatcher();
    }

    private void addTextWatcher() {

        registerBinding.textInputLayoutEmail.setErrorIconDrawable(null);
        registerBinding.textInputLayoutPassword.setErrorIconDrawable(null);
        registerBinding.textInputLayoutPwdConfirm.setErrorIconDrawable(null);

        registerBinding.registerUserName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                afterTextChange();
            }
        });

        registerBinding.registerEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.d("imageView", "onTextChanged: 不为空" + s.length());
                if (!s.toString().matches(getResources().getString(R.string.emailCheckStr)) && s.length() > 0) {
                    registerBinding.textInputLayoutEmail.setError("邮箱格式错误");
                }else {
                    isNotOk = !(s.length() > 0);
                    registerBinding.textInputLayoutEmail.setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                afterTextChange();
            }
        });

        registerBinding.registerPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.d("imageView", "onTextChanged: 不为空" + s);
                if ((s.length() < 6 || s.length() > 18) && s.length() > 0) {
                    registerBinding.textInputLayoutPassword.setError("密码格式错误");
                }else {
                    isNotOk = !(s.length() > 0);
                    if (!isNotOk &&
                            (registerBinding.registerPasswordConfirm.getText()!=null && registerBinding.registerPasswordConfirm.getText().length() > 0)) {
                        registerBinding.textInputLayoutPwdConfirm.setError("两次密码不一样");
                    }else {
                        registerBinding.textInputLayoutPwdConfirm.setErrorEnabled(false);
                    }
                    registerBinding.textInputLayoutPassword.setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                afterTextChange();
            }
        });

        registerBinding.registerPasswordConfirm.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.d("imageView", "onTextChanged: 不为空" + s);
                if ( !s.toString().equals(Objects.requireNonNull(registerBinding.registerPassword.getText()).toString())
                    && s.length() > 0) {
                    registerBinding.textInputLayoutPwdConfirm.setError("两次密码不一样");
                }else {
                    isNotOk = !(s.length() > 0);
                    registerBinding.textInputLayoutPwdConfirm.setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                afterTextChange();
            }
        });
    }

    private void afterTextChange() {
        if (isNotOk) {
            registerBinding.registerButton.setEnabled(false);
            return;
        }
        for (TextInputEditText textInputEditText : textInputEditTexts) {
            if (textInputEditText.getText() == null ||
                    textInputEditText.getText().length() == 0) {
                isNotOk = true;
                break;
            }else {
                isNotOk = false;
            }
        }
        registerBinding.registerButton.setEnabled(!isNotOk);
    }
}
