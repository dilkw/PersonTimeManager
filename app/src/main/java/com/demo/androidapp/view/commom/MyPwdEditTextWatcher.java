package com.demo.androidapp.view.commom;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class MyPwdEditTextWatcher implements TextWatcher {

    private TextInputEditText pwdEditText;          //密码输入框
    private TextInputEditText pwdConfirmEditText;   //确认密码输入框
    private MaterialButton materialButton;
    private String pwdCheckStrRegular;

    public void setPwdCheckStrRegular(String pwdCheckStrRegular) {
        this.pwdCheckStrRegular = pwdCheckStrRegular;
    }

    public MyPwdEditTextWatcher() {}

    public MyPwdEditTextWatcher(TextInputEditText pwdEditText, TextInputEditText pwdConfirmEditText, MaterialButton materialButton) {
        this.pwdEditText = pwdEditText;
        this.pwdConfirmEditText = pwdConfirmEditText;
        this.materialButton = materialButton;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        Log.d("imageView", "onTextChanged: MyPwdTextWatcher--onTextChanged");
        if (pwdCheckStrRegular != null) {
            if ( s.length() > 0 && s.toString().matches(pwdCheckStrRegular) ) {
                ((TextInputLayout)pwdEditText.getParent().getParent()).setError("密码格式错误");
            }else {
                ((TextInputLayout)pwdEditText.getParent().getParent()).setErrorEnabled(false);
            }
        }

        if (s.length() > 0 && pwdConfirmEditText != null){
                if( pwdConfirmEditText.getText().length() > 0
                        && !pwdConfirmEditText.getText().toString().equals(s.toString())) {
                    ((TextInputLayout) pwdConfirmEditText.getParent().getParent()).setError("密码格式错误");
                }else {
                    ((TextInputLayout) pwdConfirmEditText.getParent().getParent()).setErrorEnabled(false);
                }
        }else {
            if (pwdConfirmEditText != null) {
                ((TextInputLayout) pwdConfirmEditText.getParent().getParent()).setErrorEnabled(false);
            }
        }

    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}
