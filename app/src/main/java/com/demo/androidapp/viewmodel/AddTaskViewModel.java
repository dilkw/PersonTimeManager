package com.demo.androidapp.viewmodel;

import android.app.Application;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.demo.androidapp.MyApplication;
import com.demo.androidapp.R;
import com.demo.androidapp.model.common.ReturnData;
import com.demo.androidapp.model.entity.AlertOfTask;
import com.demo.androidapp.model.entity.CategoryOfTask;
import com.demo.androidapp.model.entity.Task;
import com.demo.androidapp.repository.CategoryRepository;
import com.demo.androidapp.repository.TaskRepository;
import com.demo.androidapp.util.DateTimeUtil;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

public class AddTaskViewModel extends AndroidViewModel {

    private final CategoryRepository categoryRepository;
    
    public MutableLiveData<Task> taskMutableLiveData;

    public MutableLiveData<AlertOfTask> alertOfTaskMutableLiveData;

    private final TaskRepository taskRepository;

    private final DateTimeUtil dateTimeUtil;

    @RequiresApi(api = Build.VERSION_CODES.O)
    public AddTaskViewModel(@NonNull Application application) {
        super(application);
        taskMutableLiveData = new MutableLiveData<>();
        alertOfTaskMutableLiveData = new MutableLiveData<>(new AlertOfTask());
        taskRepository = new TaskRepository(application);
        categoryRepository = new CategoryRepository(application, MyApplication.getApplication().getUID());
        dateTimeUtil = new DateTimeUtil();
    }

    //对任务的结束时间的setter和getter方法
    @RequiresApi(api = Build.VERSION_CODES.O)
    public String getEndTimeStr() {
        long endTime = taskMutableLiveData.getValue().getEnd_time();
        if (endTime == 0) {
            return "";
        }
        return dateTimeUtil.longToStrYMDHM(endTime);
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void setEndTimeStr(String timeStr) {
        Objects.requireNonNull(taskMutableLiveData.getValue()).setEnd_time(dateTimeUtil.strToLong(timeStr));
    }

    //对任务的结束时间的setter和getter方法
    @RequiresApi(api = Build.VERSION_CODES.O)
    public String getAlertTimeStr() {
        long alertTime = taskMutableLiveData.getValue().getAlert_time();
        if (alertTime == 0) {
            return "";
        }
        return dateTimeUtil.longToStrYMDHM(alertTime);
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void setAlertTimeStr(String timeStr) {
        Objects.requireNonNull(taskMutableLiveData.getValue()).setAlert_time(dateTimeUtil.strToLong(timeStr));
    }

    //对任务的创建时间的setter和getter方法
    @RequiresApi(api = Build.VERSION_CODES.O)
    public String getCreateTimeStr() {
        long createTime = taskMutableLiveData.getValue().getCreated_at();
        if (createTime == 0) {
            return "";
        }
        return dateTimeUtil.longToStrYMDHM(createTime);
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void setCreateTimeStr(String timeStr) {
        Objects.requireNonNull(taskMutableLiveData.getValue()).setCreated_at(dateTimeUtil.strToLong(timeStr));
    }

    //对任务的创建时间的setter和getter方法
    @RequiresApi(api = Build.VERSION_CODES.O)
    public boolean getAlert() {
        return taskMutableLiveData.getValue().isAlert();
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void setAlert(boolean alert) {
        Objects.requireNonNull(taskMutableLiveData.getValue()).setAlert(alert);
    }

    //获取任务分类
    public LiveData<List<CategoryOfTask>> getCategory() {
        return categoryRepository.getAllCategoryLiveData();
    }

    //添加任务分类
    public Long addCategory(CategoryOfTask... categoryOfTasks) throws ExecutionException, InterruptedException {
        return categoryRepository.addCategory(categoryOfTasks);
    }

    //更新任务分类
    public void updateCategory(CategoryOfTask... categoryOfTasks) {
        categoryRepository.updateCategory(categoryOfTasks);
    }

    //删除任务分类
    public void deleteCategory(CategoryOfTask... categoryOfTasks) {
        categoryRepository.deleteCategory(categoryOfTasks);
    }

    //添加任务
    public LiveData<ReturnData<Object>> addTask(){
        Log.d("imageView", "addTask: " + taskMutableLiveData.getValue().toString());
        return taskRepository.addTasksToServer(taskMutableLiveData.getValue());
    }

    //更新任务
    public LiveData<ReturnData<Object>> updateTaskInServer(){
        long id = taskMutableLiveData.getValue().getId();
        return taskRepository.updateTaskInServer(id,taskMutableLiveData.getValue());
    }

}