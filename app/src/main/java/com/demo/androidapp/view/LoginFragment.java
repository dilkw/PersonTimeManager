package com.demo.androidapp.view;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import android.annotation.SuppressLint;
import android.content.res.ColorStateList;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.demo.androidapp.R;
import com.demo.androidapp.databinding.LoginFragmentBinding;
import com.demo.androidapp.view.myView.MyImageView;
import com.demo.androidapp.viewmodel.LoginViewModel;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.textfield.TextInputLayout;

public class LoginFragment extends Fragment {

    private LoginViewModel loginViewModel;

    private MyImageView myImageView;

    private TextInputLayout textInputLayout;

    private TextView textView;

    private LoginFragmentBinding binding;

    public static LoginFragment newInstance() {
        return new LoginFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater,R.layout.login_fragment,container,false);
//        binding = LoginFragmentBinding.inflate(inflater);
        loginViewModel = new ViewModelProvider(this).get(LoginViewModel.class);
        binding.textInputLayoutPwd.setEndIconDrawable(R.drawable.pwdn);
        binding.textInputLayoutPwd.setEndIconCheckable(false);
        binding.setLoginViewModel(loginViewModel);
        return binding.getRoot();
    }

    @SuppressLint("ResourceType")
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        binding.myImageView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(getContext());
//                bottomSheetDialog.set
            }
        });

        binding.textInputLayoutPwd.setEndIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(binding.textInputLayoutPwd.isEndIconCheckable()){
                    //设置文本框末尾的图标
                    binding.textInputLayoutPwd.setEndIconDrawable(R.drawable.pwdn);
                    binding.textInputLayoutPwd.setEndIconCheckable(false);
                    //隐藏密码
                    binding.textInputLayoutPwd.getEditText().setTransformationMethod(PasswordTransformationMethod.getInstance());
                }else {
                    Log.d("imageView","显示密码");
                    //设置文本框末尾的图标
                    binding.textInputLayoutPwd.setEndIconDrawable(R.drawable.pwd);
                    binding.textInputLayoutPwd.setEndIconCheckable(true);
                    //显示密码
                    binding.textInputLayoutPwd.getEditText().setTransformationMethod(null);
                }
                //设置光标移至最后
                binding.textInputLayoutPwd.getEditText().setSelection(binding.textInputLayoutPwd.getEditText().getText().length());
            }
        });
    }

}
