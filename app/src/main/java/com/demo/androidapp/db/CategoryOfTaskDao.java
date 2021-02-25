package com.demo.androidapp.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.demo.androidapp.model.entity.CategoryOfTask;

import java.util.List;

@Dao
public interface CategoryOfTaskDao {

    @Query(value = "SELECT * FROM category")
    public LiveData<List<CategoryOfTask>>getAllCategory();

    @Query(value = "SELECT * FROM category WHERE uid = :uid")
    public LiveData<List<CategoryOfTask>>getAllCategoryByUid(String uid);

    @Insert
    public void addCategory(CategoryOfTask categoryOfTask);

    @Delete
    public void deleteCategory(CategoryOfTask categoryOfTask);

    @Insert
    List<Long> insertCategory(CategoryOfTask... CategoryOfTasks);

    @Insert
    Long insertCategory(CategoryOfTask categoryOfTask);

    @Update
    void updateCategory(CategoryOfTask... CategoryOfTasks);

    @Delete
    void deleteCategory(CategoryOfTask... CategoryOfTasks);

}
