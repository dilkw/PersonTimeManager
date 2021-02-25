package com.demo.androidapp.view.myView.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.demo.androidapp.R;
import com.demo.androidapp.databinding.ItemTaskBinding;
import com.demo.androidapp.model.entity.Task;

import java.util.List;

public class TasksItemAdapter extends RecyclerView.Adapter<TasksItemAdapter.MyViewHolder> {

    private List<Task> tasks;

    private ItemTaskBinding itemTaskBinding;

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }

    public TasksItemAdapter(List<Task> tasks) {
        Log.d("imageView", "TasksItemAdapter: 数据长度：" + tasks.size());
        this.tasks = tasks;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_task,parent,false);
//        itemTaskBinding = DataBindingUtil.getBinding(view);
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        itemTaskBinding = DataBindingUtil.inflate(layoutInflater,R.layout.item_task,parent,false);
        return new MyViewHolder(itemTaskBinding.getRoot());
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Task task = tasks.get(position);
        if (itemTaskBinding != null) {
            itemTaskBinding.itemTaskCreateTimeText.append(task.getCreated_at().toString());
            itemTaskBinding.itemTaskEndTimeText.append(task.getTime().toString());
            itemTaskBinding.itemTaskStateText.append("" + task.getState());
            itemTaskBinding.itemTaskTaskText.append(task.getTask());
        }else {
            Log.d("imageView", "onBindViewHolder: itemTaskBinding为空");
        }
    }

    @Override
    public int getItemCount() {
        return tasks == null ? 0 : tasks.size();
        //return 0;
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
