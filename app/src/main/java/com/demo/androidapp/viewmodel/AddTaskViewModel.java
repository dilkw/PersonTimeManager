package com.demo.androidapp.viewmodel;

import android.annotation.SuppressLint;
import android.app.Application;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.demo.androidapp.MyApplication;
import com.demo.androidapp.model.entity.CategoryOfTask;
import com.demo.androidapp.model.entity.Task;
import com.demo.androidapp.repository.CategoryRepository;
import com.demo.androidapp.util.DateTimeUtil;

import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class AddTaskViewModel extends AndroidViewModel {

    private CategoryRepository categoryRepository;
    
    public MutableLiveData<Task> taskMutableLiveData;

    private DateTimeUtil dateTimeUtil;

    public AddTaskViewModel(@NonNull Application application) {
        super(application);
        java.util.Date date1 = new java.util.Date();
        Task task = new Task();
        dateTimeUtil = new DateTimeUtil();
        task.setCreated_at(new Date(date1.getTime()));
        taskMutableLiveData = new MutableLiveData<>();
        taskMutableLiveData.setValue(task);
        categoryRepository = new CategoryRepository(application, MyApplication.getApplication().getUID());
    }

    //对任务的创建时间的setter和getter方法
    public String getDateString(){
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return format.format(taskMutableLiveData.getValue().getCreated_at());
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void setDateString(String str){
        DateTimeUtil dateTimeUtil = new DateTimeUtil();
        taskMutableLiveData.getValue().setCreated_at(dateTimeUtil.strToDateYMDHM(str));
    }

    //对任务的结束时间的setter和getter方法
    @RequiresApi(api = Build.VERSION_CODES.O)
    public String getTimeStr() {
        if (taskMutableLiveData.getValue().getTime() == null){
            return "";
        }
        return dateTimeUtil.dateToStrYMDHM(taskMutableLiveData.getValue().getTime());
    }
    public void setTimeStr(String timeStr) {
        taskMutableLiveData.getValue().setTime(dateTimeUtil.strToDateYMDHM(timeStr));
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

}