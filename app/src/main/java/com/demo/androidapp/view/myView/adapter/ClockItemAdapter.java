package com.demo.androidapp.view.myView.adapter;

import android.annotation.SuppressLint;
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
import com.demo.androidapp.model.entity.Clock;
import com.demo.androidapp.util.DateTimeUtil;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ClockItemAdapter extends RecyclerView.Adapter<ClockItemAdapter.MyViewHolder> {

    private boolean isShow = false;

    private boolean allChecked = false;

    private List<Clock> clocks;

    private List<Clock> editModelSelectedClocks;

    private ItemLongOnClickListener itemLongOnClickListener;

    private ItemOnClickListener itemOnClickListener;

    public List<Clock> getEditModelSelectedTasks() {
        return editModelSelectedClocks;
    }

    private DateTimeUtil dateTimeUtil;

    //长按Item时弹出编辑菜单，取消按钮（删除所选择的）
    public void cancelTask() {
        isShow = false;
        allChecked = false;
        notifyDataSetChanged();
    }
    //长按Item时弹出编辑菜单，删除按钮（删除所选择的）
    public List<Clock> deleteSelectedClocks() {
        allChecked = false;
        if (editModelSelectedClocks.size() == 0)return null;
        clocks.removeAll(Objects.requireNonNull(editModelSelectedClocks));
        notifyDataSetChanged();
        return editModelSelectedClocks;
    }
    //长按Item时弹出编辑菜单，全选按钮
    public void selectedAllClocks() {
        allChecked = true;
        notifyDataSetChanged();
    }

    public void setItemLongOnClickListener(ItemLongOnClickListener itemLongOnClickListener) {
        this.itemLongOnClickListener = itemLongOnClickListener;
    }
    public void setItemOnClickListener(ItemOnClickListener itemOnClickListener) {
        this.itemOnClickListener = itemOnClickListener;
    }

    public void setClocks(List<Clock> clocks) {
        this.clocks.clear();
        this.clocks = clocks;
        notifyDataSetChanged();
    }

    public void setCheckBoxIsShow() {
        isShow = true;
        notifyDataSetChanged();
    }

    public ClockItemAdapter(List<Clock> clocks) {
        Log.d("imageView", "TasksItemAdapter: 数据长度：" + clocks.size());
        this.clocks = clocks;
        editModelSelectedClocks = new ArrayList<>();
        dateTimeUtil = new DateTimeUtil();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.clock_item,parent,false);
        return new MyViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Clock clock = clocks.get(position);
        holder.clockTaskTextView.setText(clock.getTask());
        holder.clockMinuteTextView.setText(clock.getClock_minuet() + "分钟");
        holder.checkBox.setVisibility(isShow ? View.VISIBLE : View.GONE);
        holder.clockAlertTimeTextView.setText(dateTimeUtil.longToStrYMDHM(clock.getAlert_Time()));
        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    editModelSelectedClocks.add(clocks.get(position));
                }else {
                    editModelSelectedClocks.remove(clocks.get(position));
                }
            }
        });
        if (isShow) {
            holder.checkBox.setChecked(allChecked);
        }

        holder.clockTaskTextView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (itemLongOnClickListener == null)return false;
                if (editModelSelectedClocks == null) {
                    editModelSelectedClocks = new ArrayList<>();
                }
                itemLongOnClickListener.itemLongOnClick();
                return false;
            }
        });

        holder.clockTaskTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (itemOnClickListener == null) return;
                itemOnClickListener.itemOnClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return clocks == null ? 0 : clocks.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        public View itemView;
        public TextView clockTaskTextView;
        public TextView clockMinuteTextView;
        public MaterialButton clockStartButton;
        public CheckBox checkBox;
        public TextView clockAlertTimeTextView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            this.itemView = itemView;
            this.clockTaskTextView = itemView.findViewById(R.id.clockTaskTextView);
            this.clockMinuteTextView = itemView.findViewById(R.id.clockMinuteTextView);
            this.clockStartButton = itemView.findViewById(R.id.clockStartButton);
            this.checkBox = itemView.findViewById(R.id.clockItemCheckBox);
            this.clockAlertTimeTextView = itemView.findViewById(R.id.clockAlertTimeTextView);
        }
    }

    public interface ItemLongOnClickListener {
        void itemLongOnClick();
    }

    public interface ItemOnClickListener {
        void itemOnClick(int position);
    }
}
