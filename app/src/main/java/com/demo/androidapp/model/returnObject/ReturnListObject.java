package com.demo.androidapp.model.returnObject;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

public class ReturnListObject<T> implements Serializable {

    //private T[] items;

    private List<T> items;

    private int total;

//    public ReturnListObject(T[] items, int total) {
//        this.items = items;
//        this.total = total;
//    }
    public ReturnListObject(List<T> items, int total) {
        this.items = items;
        this.total = total;
    }

//    public T[] getItems() {
//        return items;
//    }

    public List<T> getItems() {
        return items;
    }

//    public void setItems(T[] items) {
//        this.items = items;
//    }

    public void setItems(List<T> items) {
        this.items = items;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

//    @Override
//    public String toString() {
//        return "ReturnListObject{" +
//                "ts=" + Arrays.toString(items) +
//                ", total=" + total +
//                '}';
//    }

    @Override
    public String toString() {
        return "ReturnListObject{" +
                "ts=" + items.toString() +
                ", total=" + total +
                '}';
    }
}
