package com.demo.androidapp.repository;

import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.room.Room;

import com.demo.androidapp.db.AppDatabase;
import com.demo.androidapp.db.CategoryOfTaskDao;
import com.demo.androidapp.model.entity.CategoryOfTask;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class CategoryRepository {

    private CategoryOfTaskDao categoryOfTaskDao;

    private LiveData<List<CategoryOfTask>> liveData;

    private MutableLiveData<List<CategoryOfTask>> mutableLiveData;

    public CategoryRepository(Application application,String uid){
        categoryOfTaskDao = Room
                .databaseBuilder(application.getApplicationContext(), AppDatabase.class, "task")
                .build()
                .categoryOfTaskDao();
        Log.d("imageView", "CategoryRepository: uid::: " + uid);
        liveData = categoryOfTaskDao.getAllCategoryByUid(uid);
    }

    public LiveData<List<CategoryOfTask>> getAllCategoryLiveData() {
        return liveData;
    }

    //添加类别
    public Long addCategory(CategoryOfTask... categoryOfTasks) throws ExecutionException, InterruptedException {
        return (new AddCategory(categoryOfTaskDao).execute(categoryOfTasks)).get();
    }

    //更新类别
    public void updateCategory(CategoryOfTask... categoryOfTasks) {
        new UpdateCategory(categoryOfTaskDao).execute(categoryOfTasks);
    }

    //删除类别
    public void deleteCategory(CategoryOfTask... categoryOfTasks) {
        new DeleteCategory(categoryOfTaskDao).execute(categoryOfTasks);
    }

    //添加类别
    static class AddCategory extends AsyncTask<CategoryOfTask,Void, Long>{

        CategoryOfTaskDao categoryOfTaskDao;

        public AddCategory(CategoryOfTaskDao categoryOfTaskDao){

            this.categoryOfTaskDao = categoryOfTaskDao;
        }

        @Override
        protected Long doInBackground(CategoryOfTask... categoryOfTasks) {
            Long l = categoryOfTaskDao.insertCategory(categoryOfTasks).get(0);
            Log.d("imageView", "doInBackground: id: " + l);
            return l;
        }
    }

    //更新类别
    static class UpdateCategory extends AsyncTask<CategoryOfTask,Void,Void>{

        CategoryOfTaskDao categoryOfTaskDao;

        public UpdateCategory(CategoryOfTaskDao categoryOfTaskDao){
            this.categoryOfTaskDao = categoryOfTaskDao;
        }

        @Override
        protected Void doInBackground(CategoryOfTask... categoryOfTasks) {
            categoryOfTaskDao.updateCategory(categoryOfTasks);
            return null;
        }
    }

    //删除类别
    static class DeleteCategory extends AsyncTask<CategoryOfTask,Void,Void>{

        CategoryOfTaskDao categoryOfTaskDao;

        public DeleteCategory(CategoryOfTaskDao categoryOfTaskDao){
            this.categoryOfTaskDao = categoryOfTaskDao;
        }

        @Override
        protected Void doInBackground(CategoryOfTask... categoryOfTasks) {
            categoryOfTaskDao.deleteCategory(categoryOfTasks);
            return null;
        }
    }
}
