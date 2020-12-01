package com.demo.androidapp.view.commom;

import android.text.Editable;
import android.text.TextWatcher;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.List;

public class MyTextWatcher implements TextWatcher {

    private List<TextInputEditText> editTexts;

    private MaterialButton materialButton;

    public MyTextWatcher(List<TextInputEditText> editTexts, MaterialButton materialButton) {
        this.editTexts = editTexts;
        this.materialButton = materialButton;
    }

    //添加监听器
    public void addEditTextsChangeListener() {
        for (TextInputEditText editText : editTexts) {
            editText.addTextChangedListener(this);
        }
    }

    public void rmEditTextsChangeListener() {
        for (TextInputEditText editText : editTexts) {
            editText.removeTextChangedListener(this);
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        boolean isNotNull = true;
        for (TextInputEditText editText : editTexts) {
            isNotNull = isNotNull && (!(editText.getText().toString().trim().isEmpty()));
        }
        this.materialButton.setEnabled(isNotNull);
    }
    
    @Override
    public void afterTextChanged(Editable s) {

    }
}
