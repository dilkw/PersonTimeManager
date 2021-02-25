package com.demo.androidapp.view.commom;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;

import com.demo.androidapp.MyApplication;
import com.demo.androidapp.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class MyEmailEditTextWatcher implements TextWatcher {

    private MaterialButton materialButton;
    private TextInputEditText textInputEditText;
    private String emailStr = MyApplication.getMyApplicationContext().getResources().getString(R.string.emailCheckStr);

    public MyEmailEditTextWatcher(TextInputEditText textInputEditText) {
        this.textInputEditText = textInputEditText;
    }

    public MyEmailEditTextWatcher(TextInputEditText textInputEditText,MaterialButton materialButton) {
        this.textInputEditText = textInputEditText;
        this.materialButton = materialButton;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        Log.d("imageView", "onTextChanged: textChanged");
        if (!s.toString().matches(emailStr) && s.length() > 0) {
            ((TextInputLayout)(textInputEditText.getParent().getParent())).setError("邮箱格式错误");
            if (materialButton != null)materialButton.setEnabled(false);
        }else {
            ((TextInputLayout)(textInputEditText.getParent().getParent())).setErrorEnabled(false);
            if (materialButton != null)materialButton.setEnabled(true);
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}
