package com.demo.androidapp.view.myView.adapter;

import android.annotation.SuppressLint;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
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

    private ItemStartOnClickListener itemStartOnClickListener;

    public List<Clock> getEditModelSelectedClocks() {
        return editModelSelectedClocks;
    }

    //从头部开始添加时钟
    public void addSelectClockInStart(List<Clock> clocks) {
        if (clocks == null || clocks.size() == 0)return;
        this.clocks.addAll(0,clocks);
    }

    //长按Item时弹出编辑菜单，取消按钮（删除所选择的）
    public void cancelTask() {
        isShow = false;
        allChecked = false;
        notifyDataSetChanged();
    }
    //长按Item时弹出编辑菜单，删除按钮（删除所选择的）
    public void deleteSelectedClocks() {
        allChecked = false;
        if (editModelSelectedClocks.size() == 0)return;
        clocks.removeAll(Objects.requireNonNull(editModelSelectedClocks));
        notifyDataSetChanged();
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

    public void setItemStartOnClickListener(ItemStartOnClickListener itemStartOnClickListener) {
        this.itemStartOnClickListener = itemStartOnClickListener;
    }

    public void setClocks(List<Clock> clocks) {
        if (clocks == null)return;
        this.clocks.clear();
        this.clocks = clocks;
    }

    public void addClock(Clock clock) {
        if (clock == null) {
            Log.d("imageView", "addClock: clock为空");
            return;
        }
        this.clocks.add(clock);
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
        Log.d("imageView", "onBindViewHolder: " + clock.getClock_minute() + "分钟");
        holder.clockMinuteTextView.setText(clock.getClock_minute() + "分钟");
        holder.checkBox.setVisibility(isShow ? View.VISIBLE : View.GONE);
        holder.clockAlertTimeTextView.setText(clock.isAlert() ? DateTimeUtil.longToStrYMDHM(clock.getAlert_time()) : "");
        holder.clockItemAlertImg.setVisibility(clock.isAlert() ? View.VISIBLE : View.GONE);
        holder.clockItemStateTextView.setText(clock.isState() ?
                "完成时间:" + DateTimeUtil.longToStrYMDHM(clock.getComplete_time()) : "未完成");
        if (clock.isState()) {
            holder.clockItemStateImageView.setVisibility(View.VISIBLE);
            holder.clockStartButton.setEnabled(false);
        }
        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    editModelSelectedClocks.add(clock);
                }else {
                    editModelSelectedClocks.remove(clock);
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
                itemOnClickListener.itemOnClick(clock,position);
            }
        });

        holder.clockStartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (itemOnClickListener == null)return;
                itemStartOnClickListener.itemStartOnClick(position,clock.getId());
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
        public ImageView clockItemAlertImg;
        public ImageView clockItemStateImageView;
        public TextView clockItemStateTextView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            this.itemView = itemView;
            this.clockTaskTextView = itemView.findViewById(R.id.clockTaskTextView);
            this.clockMinuteTextView = itemView.findViewById(R.id.clockMinuteTextView);
            this.clockStartButton = itemView.findViewById(R.id.clockStartButton);
            this.checkBox = itemView.findViewById(R.id.clockItemCheckBox);
            this.clockAlertTimeTextView = itemView.findViewById(R.id.clockAlertTimeTextView);
            this.clockItemAlertImg = itemView.findViewById(R.id.clockItemAlertImg);
            this.clockItemStateImageView = itemView.findViewById(R.id.clockItemStateImageView);
            this.clockItemStateTextView = itemView.findViewById(R.id.clockItemStateTextView);
        }
    }

    public interface ItemLongOnClickListener {
        void itemLongOnClick();
    }

    public interface ItemOnClickListener {
        void itemOnClick(Clock clock,int position);
    }

    public interface ItemStartOnClickListener {
        void itemStartOnClick(int position,long clockId);
    }
}
