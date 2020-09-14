package com.demo.androidapp.view.myView.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.demo.androidapp.R;

import java.util.ArrayList;
import java.util.List;

public class MyAdapter extends ArrayAdapter<MyAdapter.MyViewHolder> {

    private List<String> itemStr = new ArrayList<>();

    public MyAdapter(@NonNull Context context, int resource) {
        super(context, resource);
        itemStr.add("从相册中选择");
        itemStr.add("拍照");
        itemStr.add("默认头像");
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        @SuppressLint("ViewHolder") View view = LayoutInflater.from(getContext()).inflate(R.layout.mylistview_item,parent,false);
        MyViewHolder myViewHolder = new MyViewHolder(view);
        myViewHolder.textView.setText(itemStr.get(position));
        return view;
    }



    static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView textView;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.item_textView);
        }
    }
}
