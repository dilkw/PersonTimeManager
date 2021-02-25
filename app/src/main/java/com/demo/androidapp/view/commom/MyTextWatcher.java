package com.demo.androidapp.view.commom;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;

import com.demo.androidapp.MyApplication;
import com.demo.androidapp.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.List;
import java.util.Objects;

public class MyTextWatcher implements TextWatcher {

    private List<TextInputEditText> editTexts;

    private MaterialButton materialButton;

    public void addTextWatcher(List<TextInputEditText> editTexts, MaterialButton materialButton) {
        this.editTexts = editTexts;
        this.materialButton = materialButton;
        if (this.materialButton != null && this.editTexts.size() > 0) {
            for (TextInputEditText textInputEditText : editTexts) {
                textInputEditText.addTextChangedListener(this);
            }
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        Log.d("imageView", "beforeTextChanged: MyTextChanged : " + s + "    start :" + start + "   count: " + count + "   after: " + after);
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        Log.d("imageView", "onTextChanged: MyTextChanged: " + s + "    start :" + start + "   count: " + count);
    }
    
    @Override
    public void afterTextChanged(Editable s) {
        Log.d("imageView", "afterTextChanged: MyTextAfterChanged : " + s );
        for (TextInputEditText textInputEditText : editTexts) {
            if (textInputEditText.getText() == null
                    || textInputEditText.getText().length() == 0
                    || ((TextInputLayout)textInputEditText.getParent().getParent()).isErrorEnabled()) {
                materialButton.setEnabled(false);
                return;
            }
        }
        materialButton.setEnabled(true);
    }
}
