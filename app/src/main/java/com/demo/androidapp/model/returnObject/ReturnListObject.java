package com.demo.androidapp.model.returnObject;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

public class ReturnListObject<T> implements Serializable {

    private List<T> items;

    private int total;

    public ReturnListObject(List<T> items, int total) {
        this.items = items;
        this.total = total;
    }

    public List<T> getItems() {
        return items;
    }

    public void setItems(List<T> items) {
        this.items = items;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    @Override
    public String toString() {
        return "ReturnListObject{" +
                "ts=" + items.toString() +
                ", total=" + total +
                '}';
    }
}
