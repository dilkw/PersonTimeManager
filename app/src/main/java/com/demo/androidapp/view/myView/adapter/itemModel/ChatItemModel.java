package com.demo.androidapp.view.myView.adapter.itemModel;

public class ChatItemModel {

    private int type;

    private Object data;

    public ChatItemModel(int type, Object data) {
        this.type = type;
        this.data = data;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "ChatItemModel{" +
                "type=" + type +
                ", data=" + data.toString() +
                '}';
    }
}
