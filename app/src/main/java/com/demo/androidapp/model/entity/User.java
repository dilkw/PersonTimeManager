package com.demo.androidapp.model.entity;


import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class User {

    //id
    @PrimaryKey(autoGenerate = true)
    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
