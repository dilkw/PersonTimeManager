package com.demo.androidapp.view.commom;

import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;

import com.demo.androidapp.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.List;

public class MethodCommon {

    //textInputLayout密码框设置密码隐藏与显示
    public void setEndIconOnClickListener(TextInputLayout textInputLayout) {
        textInputLayout.setEndIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (textInputLayout.isEndIconCheckable()) {
                    //设置文本框末尾的图标
                    textInputLayout.setEndIconDrawable(R.drawable.pwdn);
                    textInputLayout.setEndIconCheckable(false);
                    //隐藏密码
                    textInputLayout.getEditText().setTransformationMethod(PasswordTransformationMethod.getInstance());
                } else {
                    Log.d("imageView", "显示密码");
                    //设置文本框末尾的图标
                    textInputLayout.setEndIconDrawable(R.drawable.pwd);
                    textInputLayout.setEndIconCheckable(true);
                    //显示密码
                    textInputLayout.getEditText().setTransformationMethod(null);
                }
                //设置光标移至最后
                textInputLayout.getEditText().setSelection(textInputLayout.getEditText().getText().length());
            }
        });
    }

//    public void rmEndIconOnClickListener(TextInputLayout textInputLayout) {
//        textInputLayout.removeOnEndIconChangedListener(textInputLayout.getEnd);
//    }
}
