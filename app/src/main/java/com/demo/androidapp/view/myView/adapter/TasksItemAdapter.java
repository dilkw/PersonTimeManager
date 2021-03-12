package com.demo.androidapp.view.myView.adapter;

import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.demo.androidapp.R;
import com.demo.androidapp.model.entity.Task;
import com.demo.androidapp.util.DateTimeUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class TasksItemAdapter extends RecyclerView.Adapter<TasksItemAdapter.MyViewHolder> {

    private boolean isShow = false;

    private boolean allChecked = false;

    private List<Task> tasks;

    private List<Task> editModelSelectedTasks;

    private ItemLongOnClickListener itemLongOnClickListener;

    private ItemOnClickListener itemOnClickListener;

    public List<Task> getEditModelSelectedTasks() {
        return editModelSelectedTasks;
    }

    //长按Item时弹出编辑菜单，取消按钮（删除所选择的）
    public void cancelTask() {
        isShow = false;
        allChecked = false;
        notifyDataSetChanged();
    }
    //长按Item时弹出编辑菜单，删除按钮（删除所选择的）
    public void deleteSelectedTask() {
        allChecked = false;
        if (editModelSelectedTasks.size() == 0)return;
        tasks.removeAll(Objects.requireNonNull(editModelSelectedTasks));
        notifyDataSetChanged();
    }
    //长按Item时弹出编辑菜单，全选按钮
    public void selectedAllTasks() {
        allChecked = true;
        notifyDataSetChanged();
    }

    public void setItemLongOnClickListener(ItemLongOnClickListener itemLongOnClickListener) {
        this.itemLongOnClickListener = itemLongOnClickListener;
    }
    public void setItemOnClickListener(ItemOnClickListener itemOnClickListener) {
        this.itemOnClickListener = itemOnClickListener;
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }

    public void setCheckBoxIsShow() {
        isShow = true;
        notifyDataSetChanged();
    }

    public TasksItemAdapter(List<Task> tasks) {
        Log.d("imageView", "TasksItemAdapter: 数据长度：" + tasks.size());
        this.tasks = tasks;
        editModelSelectedTasks = new ArrayList<>();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.task_item,parent,false);
        return new MyViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Task task = tasks.get(position);
        holder.createTimeTextView.setText(DateTimeUtil.longToStrYMDHM(task.getCreated_at()));
        holder.endTimeTextView.setText(DateTimeUtil.longToStrYMDHM(task.getEnd_time()));
        Log.d("imageView", "onBindViewHolder: endTime " + holder.endTimeTextView.getText());
        holder.stateTextView.setText(task.isState() ? "完成" : "未完成");
        holder.taskTextView.setText(task.getTask());
        holder.checkBox.setVisibility(isShow ? View.VISIBLE : View.GONE);
        if (isShow) {
            holder.checkBox.setChecked(allChecked);
            holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    Log.d("imageView", "onCheckedChanged: checkBox点击");
                    if (isChecked) {
                        editModelSelectedTasks.add(tasks.get(position));
                    }else {
                        editModelSelectedTasks.remove(tasks.get(position));
                    }
                }
            });
        }

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (itemLongOnClickListener == null)return false;
                if (editModelSelectedTasks == null) {
                    editModelSelectedTasks = new ArrayList<>();
                }
                itemLongOnClickListener.itemLongOnClick();
                return false;
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (itemOnClickListener == null)return;
                itemOnClickListener.itemOnClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return tasks == null ? 0 : tasks.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        public View itemView;
        public TextView createTimeTextView;
        public TextView endTimeTextView;
        public TextView stateTextView;
        public TextView taskTextView;
        public CheckBox checkBox;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            this.itemView = itemView;
            this.createTimeTextView = itemView.findViewById(R.id.itemTaskCreateTimeText);
            this.endTimeTextView = itemView.findViewById(R.id.itemTaskEndTimeText);
            this.stateTextView = itemView.findViewById(R.id.itemTaskStateText);
            this.taskTextView = itemView.findViewById(R.id.itemTask_taskText);
            this.checkBox = itemView.findViewById(R.id.itemTask_checkBox);
        }
    }

    public interface ItemLongOnClickListener {
        void itemLongOnClick();
    }

    public interface ItemOnClickListener {
        void itemOnClick(int position);
    }
}
