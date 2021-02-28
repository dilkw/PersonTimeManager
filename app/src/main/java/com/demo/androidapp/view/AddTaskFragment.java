package com.demo.androidapp.view;

import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.demo.androidapp.R;
import com.demo.androidapp.databinding.AddTaskFragmentBinding;
import com.demo.androidapp.model.entity.CategoryOfTask;
import com.demo.androidapp.view.myView.DateTimePickerDialog;
import com.demo.androidapp.view.myView.adapter.MySpinnerAdapter;
import com.demo.androidapp.viewmodel.AddTaskViewModel;

import java.util.ArrayList;
import java.util.List;

public class AddTaskFragment extends Fragment{

    private AddTaskViewModel addTaskViewModel;

    private AddTaskFragmentBinding addTaskFragmentBinding;

    private NavHostFragment navHostFragment;

    private NavController controller;

    private DateTimePickerDialog dateTimePickerDialog;

    public static AddTaskFragment newInstance() {
        return new AddTaskFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        addTaskFragmentBinding = DataBindingUtil.inflate(inflater,R.layout.add_task_fragment,container,false);
        View view = addTaskFragmentBinding.getRoot();
        navHostFragment = (NavHostFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.fragment);
        controller = navHostFragment.getNavController();
        NavigationUI.setupWithNavController(addTaskFragmentBinding.addTaskFragmentToolBar,controller);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        addTaskViewModel = new ViewModelProvider(this).get(AddTaskViewModel.class);
        addTaskFragmentBinding.setAddTaskViewModel(addTaskViewModel);
        setClickListener();
        List<CategoryOfTask> categoryOfTasks = null;
        categoryOfTasks = addTaskViewModel.getCategory().getValue();
        if (categoryOfTasks == null) {
            categoryOfTasks = new ArrayList<CategoryOfTask>();
            Log.d("imageView", "onActivityCreated: category为空" + categoryOfTasks.size());
        }
        MySpinnerAdapter mySpinnerAdapter = new MySpinnerAdapter(requireActivity().getApplicationContext(), categoryOfTasks, addTaskViewModel);
        addTaskFragmentBinding.mySpinner.setMySpinnerAdapter(mySpinnerAdapter);
        addTaskViewModel.getCategory().observe(getViewLifecycleOwner(), new Observer<List<CategoryOfTask>>() {
            @Override
            public void onChanged(List<CategoryOfTask> categoryOfTasks) {
                Log.d("imageView", "onChanged: " + categoryOfTasks.size());
                mySpinnerAdapter.setCategoryOfTaskList(categoryOfTasks);
                mySpinnerAdapter.notifyDataSetChanged();
            }
        });

    }

    private void setClickListener() {

        //为选择时间的imgBtn按钮添加点击事件
        //弹出时间选择对话框并返回时间字符串
        addTaskFragmentBinding.addTaskSelectEndTimeImgButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dateTimePickerDialog == null) {
                    dateTimePickerDialog = new DateTimePickerDialog();
                    dateTimePickerDialog.setEnterClicked(new DateTimePickerDialog.EnterListener() {
                        @RequiresApi(api = Build.VERSION_CODES.O)
                        @Override
                        public void enterBtnOnClicked() {
                            addTaskFragmentBinding.endTimeEditText.setText(dateTimePickerDialog.getSelectTime());
                        }
                    });
                }
                dateTimePickerDialog.show(getActivity().getSupportFragmentManager(),"dialog");
            }
        });

        addTaskFragmentBinding.addTaskFragmentToolBar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.addTaskSaveBtn :

                        controller.navigateUp();
                }
                return false;
            }
        });
    }

    private void saveTask() {
        addTaskViewModel.taskMutableLiveData.getValue();
    }
}