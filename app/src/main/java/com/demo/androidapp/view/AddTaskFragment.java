package com.demo.androidapp.view;

import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.LiveData;
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
import android.widget.CompoundButton;
import android.widget.Toast;

import com.demo.androidapp.MainActivity;
import com.demo.androidapp.R;
import com.demo.androidapp.databinding.AddTaskFragmentBinding;
import com.demo.androidapp.model.common.RCodeEnum;
import com.demo.androidapp.model.common.ReturnData;
import com.demo.androidapp.model.entity.AlertOfTask;
import com.demo.androidapp.model.entity.CategoryOfTask;
import com.demo.androidapp.model.entity.Task;
import com.demo.androidapp.util.DateTimeUtil;
import com.demo.androidapp.view.myView.DateTimePickerDialog;
import com.demo.androidapp.view.myView.adapter.MySpinnerAdapter;
import com.demo.androidapp.viewmodel.AddTaskViewModel;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class AddTaskFragment extends Fragment implements View.OnClickListener {

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
            task = new Task();
            LocalDateTime localDateTime = LocalDateTime.now();
            task.setCreated_at(dateTimeUtil.localDateTimeToSecLong(localDateTime));
        }else {
            isAddTask = false;
        }
        addTaskViewModel.taskMutableLiveData.setValue(task);
        init();
        setClickListener();
    }

    //初始化界面
    private void init() {
        addTaskFragmentBinding.setAddTaskViewModel(addTaskViewModel);
        MySpinnerAdapter mySpinnerAdapter = new MySpinnerAdapter(requireActivity().getApplicationContext(), new ArrayList<>(), addTaskViewModel);
        addTaskFragmentBinding.mySpinner.setMySpinnerAdapter(mySpinnerAdapter);
        LiveData<List<CategoryOfTask>> categoryLiveData = addTaskViewModel.getCategory();
        categoryLiveData.observe(getViewLifecycleOwner(), new Observer<List<CategoryOfTask>>() {
            @Override
            public void onChanged(List<CategoryOfTask> categoryOfTasks) {
                Log.d("imageView", "category onChanged: " + categoryOfTasks.size());
                mySpinnerAdapter.setCategoryOfTaskListAndNotify(categoryOfTasks);
            }
        });
        addTaskViewModel.taskMutableLiveData.observe(getViewLifecycleOwner(), new Observer<Task>() {
            @Override
            public void onChanged(Task task) {
                Log.d("imageView", "onChanged: taskMutableLiveData变化" + task.getCreated_at());
            }
        });
        addTaskViewModel.alertOfTaskMutableLiveData.observe(getViewLifecycleOwner(), new Observer<AlertOfTask>() {
            @Override
            public void onChanged(AlertOfTask alertOfTask) {
                addTaskFragmentBinding.addTaskAlertTimeTextView.setText(alertOfTask.getAlertTime());
            }
        });
    }

    //按钮设置监听事件
    private void setClickListener() {
        addTaskFragmentBinding.addTaskSelectEndTimeImgButton.setOnClickListener(this);
        addTaskFragmentBinding.addTaskAlertTimeClearImgBtn.setOnClickListener(this);
        addTaskFragmentBinding.stateToggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @SuppressLint("UseCompatLoadingForDrawables")
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Log.d("imageView", "onChanged:数据变化task ");
                if (isChecked) {
                    addTaskFragmentBinding.stateToggleButton.setBackground(getResources().getDrawable(R.color.colorTextTrue));
                    //addTaskFragmentBinding.stateToggleButton.setBackgroundColor(getResources().getColor(R.color.colorTextTrue));
                }else {
                    addTaskFragmentBinding.stateToggleButton.setBackground(getResources().getDrawable(R.color.backgroundColor));
                    //addTaskFragmentBinding.stateToggleButton.setBackgroundColor(getResources().getColor(R.color.backgroundColor));
                }
            }
        });
        //导航栏Menu菜单监听事件
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
                                String date = dateTimePickerDialog.getSelectTimeString();
                                addTaskViewModel.taskMutableLiveData.getValue().setAlert(true);
                                addTaskFragmentBinding.alertTimeLinearLayout.setVisibility(View.VISIBLE);
                                addTaskViewModel.alertOfTaskMutableLiveData.getValue().setAlertTime(date);
                                addTaskFragmentBinding.addTaskAlertTimeTextView.setText(date);
                            }
                        });
                        dateTimePickerDialog.show(fragmentManager,"alertTimeSetDialog");
                    }
                }
                return false;
            }
        });

    }

    //重写点击监听方法
    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.addTaskSelectEndTimeImgButton: {
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
                break;
            }
            case R.id.addTaskAlertTimeClearImgBtn: {
                Log.d("imageView", "删除提醒时间:");
                addTaskViewModel.taskMutableLiveData.getValue().setAlert(false);
                addTaskFragmentBinding.alertTimeLinearLayout.setVisibility(View.GONE);
                addTaskViewModel.alertOfTaskMutableLiveData.getValue().setAlertTime("");
                addTaskFragmentBinding.addTaskAlertTimeTextView.setText("");
                break;
            }
        }
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
        addTaskViewModel.addTask().observe(getViewLifecycleOwner(), new Observer<ReturnData<Task>>() {
            @Override
            public void onChanged(ReturnData<Task> objectReturnData) {
                if (objectReturnData.getCode() == RCodeEnum.OK.getCode()) {
                    Toast.makeText(getContext(),"添加任务成功",Toast.LENGTH_SHORT).show();
                    Log.d("imageView", "onChanged: " + objectReturnData.getData().toString());
                    NavController navController = Navigation.findNavController(getView());
                    navController.navigateUp();
                }else {
                    if (objectReturnData.getCode() == RCodeEnum.ERROR.getCode()){

                    }
                    Toast.makeText(getContext(),"添加任务失败",Toast.LENGTH_SHORT).show();
                    Log.d("imageView",objectReturnData.getCode() + objectReturnData.getMsg());
                }
            }
        });
    }
}