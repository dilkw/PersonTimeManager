package com.demo.androidapp.model.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "category")
public class CategoryOfTask {

    @PrimaryKey(autoGenerate = true)
    private Long id;

    @ColumnInfo(name = "uid")
    private String uid;

    @ColumnInfo(name = "category_name")
    private String categoryName;

//    public CategoryOfTask(String uid, String categoryName) {
//        this.uid = uid;
//        this.categoryName = categoryName;
//    }

    public CategoryOfTask(String uid, String categoryName) {
        this.uid = uid;
        this.categoryName = categoryName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    @Override
    public String toString() {
        return "CategoryOfTask{" +
                "id=" + id +
                ", uid='" + uid + '\'' +
                ", categoryName='" + categoryName + '\'' +
                '}';
    }
}
