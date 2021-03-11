package com.demo.androidapp.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.demo.androidapp.model.entity.AlertOfTask;
import com.demo.androidapp.model.entity.Bill;
import com.demo.androidapp.model.entity.CategoryOfTask;
import com.demo.androidapp.model.entity.Clock;
import com.demo.androidapp.model.entity.Friend;
import com.demo.androidapp.model.entity.Task;
import com.demo.androidapp.model.entity.User;
import com.demo.androidapp.util.Converters;

@Database(entities = {User.class, Task.class, CategoryOfTask.class,
        AlertOfTask.class,Clock.class, Bill.class,Friend.class}, version = 1)
//@TypeConverters({Converters.class})
public abstract class AppDatabase extends RoomDatabase {
    public abstract UserDao userDao();
    public abstract TaskDao taskDao();
    public abstract CategoryOfTaskDao categoryOfTaskDao();
    public abstract AlertOfTaskDao alertOfTaskDao();
    public abstract ClockDao clockDao();
    public abstract BillDao billDao();
    public abstract FriendDao friendDao();
}
