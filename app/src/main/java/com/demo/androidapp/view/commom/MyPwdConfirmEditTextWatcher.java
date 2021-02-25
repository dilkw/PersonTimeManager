package com.demo.androidapp.view.commom;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class MyPwdConfirmEditTextWatcher implements TextWatcher {

    private TextInputEditText pwdEditText;          //密码输入框
    private TextInputEditText pwdConfirmEditText;   //确认密码输入框

    public MyPwdConfirmEditTextWatcher(TextInputEditText pwdEditText, TextInputEditText pwdConfirmEditText) {
        this.pwdEditText = pwdEditText;
        this.pwdConfirmEditText = pwdConfirmEditText;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        Log.d("imageView", "onTextChanged: MyPwdTextWatcher--onTextChanged");
        if (s.length() > 0 && pwdEditText.getText().length() > 0 && !s.toString().equals(pwdEditText.getText().toString())) {
            ((TextInputLayout)pwdConfirmEditText.getParent().getParent()).setError("两次密码不一样");
            ((TextInputLayout)pwdConfirmEditText.getParent().getParent()).setErrorIconDrawable(null);
        }else {
            ((TextInputLayout) pwdConfirmEditText.getParent().getParent()).setErrorEnabled(false);
        }

    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}
