package com.demo.androidapp.view.myView;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.databinding.BindingAdapter;
import androidx.databinding.InverseBindingAdapter;
import androidx.databinding.InverseBindingListener;

import com.demo.androidapp.MyApplication;
import com.demo.androidapp.R;
import com.demo.androidapp.model.entity.CategoryOfTask;
import com.demo.androidapp.view.myView.adapter.MySpinnerAdapter;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

public class MySpinner extends LinearLayout {

    private MySpinnerAdapter mySpinnerAdapter;

    private TextView mySpinnerTextView;

    private ImageButton mySpinnerImageButton;

    private CategoryOfTask categoryOfTask;

    private PopupWindow myPopupWindow;

    private Context context;

    private String category;

    public void setMySpinnerAdapter(MySpinnerAdapter mySpinnerAdapter) {
        this.mySpinnerAdapter = mySpinnerAdapter;
        setItemOnClickListener();
        intPopupWindow();
    }

    public String getCategory() {
        Log.d("imageView", "getCategory: 数据改变");
        return category;
    }

    public void setCategory(String category) {
        Log.d("imageView", "setCategory: 数据改变");
        this.category = category;
        mySpinnerTextView.setText(category);
    }

    @BindingAdapter(value = "app:category")
    public static void setCategory(MySpinner v, String category) {
        Log.d("imageView", "v.setCategory: 数据改变");
        v.setCategory(category);
    }

    @InverseBindingAdapter(attribute = "app:category", event = "categoryAttrChanged")
    public static String getCategory(MySpinner v) {
        Log.d("imageView", "v.getCategory: 数据改变");
        return v.getCategory();
    }

    private InverseBindingListener inverseBindingListener;

    @BindingAdapter(value = {"categoryAttrChanged"}, requireAll = false)
    public static void categoryAttrChanged(MySpinner v, InverseBindingListener listener) {
        Log.d("imageView", "categoryAttrChanged: 数据改变");
        v.inverseBindingListener = listener;
    }

    public CategoryOfTask getSelectCategoryOfTask() {
        return categoryOfTask;
    }

    public MySpinner(Context context) {
        super(context);
        this.context = context;
        initView(context);
        Log.d("imageView", "MySpinner: 1");
    }

    public MySpinner(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        Log.d("imageView", "MySpinner: 2");
        this.context = context;
        initView(context);
    }

    public MySpinner(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        Log.d("imageView", "MySpinner: 3");
        this.context = context;
        initView(context);
    }

    @SuppressLint("ResourceType")
    void initView(Context context){
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.my_category_spinner,null);
        if (view == null) {
            Log.d("imageView", "initView: spinner为空");
        }
        mySpinnerTextView = view.findViewById(R.id.mySpinnerTextView);
        mySpinnerTextView.setText(getCategory());
        mySpinnerImageButton = view.findViewById(R.id.mySpinnerImageButton);
        setClickListener();
        LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);
        setVerticalGravity(layoutParams.gravity);
        addView(view,layoutParams);
    }

    public void setClickListener() {
        if (mySpinnerImageButton == null) {
            Log.d("imageView", "setClickListener: imageButton为空");
            return;
        }
        mySpinnerImageButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (myPopupWindow.isShowing()) {
                    Log.d("imageView", "onClick: imageButton点击----收起");
                    myPopupWindow.dismiss();
                    return;
                }
                myPopupWindow.showAsDropDown(MySpinner.this,20,20);
                Log.d("imageView", "onClick: imageButton点击-----展开");
            }
        });
    }

    public void setItemOnClickListener() {
        if (mySpinnerAdapter == null)return;
        mySpinnerAdapter.setMyItemOnClickListener(new MySpinnerAdapter.MyItemOnClickListener() {
            @Override
            public void itemOnClick(int position) {
                categoryOfTask = (CategoryOfTask)mySpinnerAdapter.getItem(position);
                String str = categoryOfTask.getCategoryName();
                setCategory(MySpinner.this,str);
                inverseBindingListener.onChange();
            }

            @Override
            public void itemDeleteClick(int position) {
                CategoryOfTask clickCategoryOfTask = (CategoryOfTask)mySpinnerAdapter.getItem(position);
                mySpinnerAdapter.removeCategory(position);
                if (categoryOfTask != null
                        && clickCategoryOfTask.getId().equals(categoryOfTask.getId())) {
                    categoryOfTask = null;
                    mySpinnerTextView.setText(getCategory());
                }

            }
        });
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private void intPopupWindow() {
        myPopupWindow = new PopupWindow(context);
        View contentView = LayoutInflater.from(context).inflate(R.layout.myspinner_popupdownview,null);
        ListView listView = contentView.findViewById(R.id.mySpinnerPopupWindowListView);
        TextView textView = contentView.findViewById(R.id.mySpinnerPopupWindowTextView);
        Button addCategoryButton = contentView.findViewById(R.id.mySpinnerPopupWindowButton);
        textView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mySpinnerTextView.setText(textView.getText());
                myPopupWindow.dismiss();
            }
        });

        //下拉框中新建按钮，点击弹出AlertDialog对话框
        addCategoryButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                View addCategoryView = LayoutInflater.from(context).inflate(R.layout.add_category_dialoglayout,null);
                TextInputEditText textInputEditText = addCategoryView.findViewById(R.id.addCategoryDialogTextInputEditText);
                MaterialButton cancelButton = addCategoryView.findViewById(R.id.addCategoryCancelButton);
                MaterialButton enterButton = addCategoryView.findViewById(R.id.addCategoryEnterButton);
                AlertDialog alertDialog = new AlertDialog.Builder(context)
                        .setView(addCategoryView)
                        .create();
                alertDialog.show();
                textInputEditText.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        Log.d("imageView", "onTextChanged:  " + s.length());
                        enterButton.setEnabled(s.length() > 0);
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });
                enterButton.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        categoryOfTask = null;
                        categoryOfTask = new CategoryOfTask(MyApplication.getApplication().getUID(),
                                                                            textInputEditText.getText().toString().trim());
                        mySpinnerTextView.setText(categoryOfTask.getCategoryName());
                        mySpinnerAdapter.addCategory(categoryOfTask);
                        Log.d("imageView", "onClick: 添加分类成功");
                        alertDialog.dismiss();
                        myPopupWindow.dismiss();
                    }
                });
                cancelButton.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                });
            }
        });
        Log.d("imageView", "MyPopupWindow:数量：" + mySpinnerAdapter.getCount());
        listView.setAdapter(mySpinnerAdapter);
        myPopupWindow.setContentView(contentView);
        myPopupWindow.setOutsideTouchable(true);
        myPopupWindow.setWidth(LayoutParams.WRAP_CONTENT);
        myPopupWindow.setHeight(LayoutParams.WRAP_CONTENT);
        myPopupWindow.setOutsideTouchable(false);
        myPopupWindow.setFocusable(true);
        myPopupWindow.setBackgroundDrawable(getResources().getDrawable(R.color.backgroundColor));
    }
}
