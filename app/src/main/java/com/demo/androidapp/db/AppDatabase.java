package com.demo.androidapp.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.demo.androidapp.model.entity.AlertOfTask;
import com.demo.androidapp.model.entity.CategoryOfTask;
import com.demo.androidapp.model.entity.Task;
import com.demo.androidapp.model.entity.User;
import com.demo.androidapp.util.Converters;

@Database(entities = {User.class, Task.class, CategoryOfTask.class, AlertOfTask.class}, version = 1)
//@TypeConverters({Converters.class})
public abstract class AppDatabase extends RoomDatabase {
    public abstract UserDao userDao();
    public abstract TaskDao taskDao();
    public abstract CategoryOfTaskDao categoryOfTaskDao();
    public abstract AlertOfTaskDao alertOfTaskDao();
}
