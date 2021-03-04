package com.demo.androidapp.view;

import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.demo.androidapp.MainActivity;
import com.demo.androidapp.R;
import com.demo.androidapp.databinding.AddTaskFragmentBinding;
import com.demo.androidapp.model.common.RCodeEnum;
import com.demo.androidapp.model.common.ReturnData;
import com.demo.androidapp.model.entity.CategoryOfTask;
import com.demo.androidapp.model.entity.Task;
import com.demo.androidapp.util.DateTimeUtil;
import com.demo.androidapp.view.myView.DateTimePickerDialog;
import com.demo.androidapp.view.myView.adapter.MySpinnerAdapter;
import com.demo.androidapp.viewmodel.AddTaskViewModel;
import com.google.gson.internal.$Gson$Preconditions;

import java.sql.Date;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class AddTaskFragment extends Fragment{

    private AddTaskViewModel addTaskViewModel;

    private AddTaskFragmentBinding addTaskFragmentBinding;

    private NavController controller;

    private DateTimePickerDialog dateTimePickerDialog;

    public static AddTaskFragment newInstance() {
        return new AddTaskFragment();
    }

    private FragmentManager fragmentManager;

    private DateTimeUtil dateTimeUtil;

    private boolean isAddTask = true;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        addTaskFragmentBinding = DataBindingUtil.inflate(inflater,R.layout.add_task_fragment,container,false);
        fragmentManager = requireActivity().getSupportFragmentManager();
        NavHostFragment navHostFragment = (NavHostFragment) fragmentManager.findFragmentById(R.id.fragment);
        assert navHostFragment != null;
        controller = navHostFragment.getNavController();
        NavigationUI.setupWithNavController(addTaskFragmentBinding.addTaskFragmentToolBar,controller);
        return addTaskFragmentBinding.getRoot();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        dateTimeUtil = new DateTimeUtil();
        Task task = (Task)((MainActivity)requireActivity()).getDataFromMapByKey("task");
        addTaskViewModel = new ViewModelProvider(this).get(AddTaskViewModel.class);
        if (task == null) {
            isAddTask = true;
            Log.d("imageView", "onActivityCreated: map为空");
            task = new Task();
            LocalDateTime localDateTime = LocalDateTime.now();
            task.setCreated_at(dateTimeUtil.localDateTimeToSecLong(localDateTime));
            Log.d("imageView", "onActivityCreated: Created_at" + dateTimeUtil.localDateTimeToStrYMDHM(localDateTime));
        }else {
            Log.d("imageView", "onActivityCreated: task" + task.toString());
            isAddTask = false;
        }
        addTaskViewModel.taskMutableLiveData.setValue(task);
        addTaskFragmentBinding.setAddTaskViewModel(addTaskViewModel);
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
        addTaskViewModel.taskMutableLiveData.observe(getViewLifecycleOwner(), new Observer<Task>() {
            @Override
            public void onChanged(Task task) {
                Log.d("imageView", "onChanged: taskMutableLiveData变化" + task.getCreated_at());
            }
        });
        setClickListener();

    }
    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
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
                            addTaskFragmentBinding.endTimeEditText.setText(dateTimePickerDialog.getSelectTimeString());
                        }
                    });
                }
                dateTimePickerDialog.show(fragmentManager,"dialog");
            }
        });

        addTaskFragmentBinding.addTaskFragmentToolBar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.addTaskSaveBtn : {
                        if (isAddTask) {
                            addTask();
                        }else {
                            saveTask();
                        }
                        break;
                    }
                    case R.id.addTaskAddAlertTimeBtn:{
                        DateTimePickerDialog dateTimePickerDialog = new DateTimePickerDialog();
                        dateTimePickerDialog.setEnterClicked(new DateTimePickerDialog.EnterListener() {
                            @RequiresApi(api = Build.VERSION_CODES.O)
                            @Override
                            public void enterBtnOnClicked() {
                                String date = dateTimePickerDialog.getSelectedDate();
                                addTaskViewModel.alertOfTaskMutableLiveData.getValue().setAlertTime(date);
                            }
                        });
                        dateTimePickerDialog.show(fragmentManager,"alertTimeSetDialog");
                    }
                }
                return false;
            }
        });
    }

    //编辑完成后保存任务
    private void saveTask() {
        Log.d("imageView", "saveTask: 保存任务");
        addTaskViewModel.updateTaskInServer().observe(getViewLifecycleOwner(), new Observer<ReturnData<Object>>() {
            @Override
            public void onChanged(ReturnData<Object> objectReturnData) {
                if (objectReturnData.getCode() == RCodeEnum.OK.getCode()) {
                    Toast.makeText(getContext(),"保存成功",Toast.LENGTH_SHORT).show();
                    NavController navController = Navigation.findNavController(getView());
                    navController.navigateUp();
                }else {
                    Toast.makeText(getContext(),"保存任务失败",Toast.LENGTH_SHORT).show();
                    Log.d("imageView",objectReturnData.getCode() + objectReturnData.getMsg());
                }
            }
        });
    }

    //添加任务
    private void addTask() {
        Log.d("imageView", "saveTask: 添加任务");
        addTaskViewModel.addTask().observe(getViewLifecycleOwner(), new Observer<ReturnData<Object>>() {
            @Override
            public void onChanged(ReturnData<Object> objectReturnData) {
                if (objectReturnData.getCode() == RCodeEnum.OK.getCode()) {
                    Toast.makeText(getContext(),"添加任务成功",Toast.LENGTH_SHORT).show();
                    NavController navController = Navigation.findNavController(getView());
                    navController.navigateUp();
                }else {
                    Toast.makeText(getContext(),"添加任务失败",Toast.LENGTH_SHORT).show();
                    Log.d("imageView",objectReturnData.getCode() + objectReturnData.getMsg());
                }
            }
        });
    }
}