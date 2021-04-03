package com.demo.androidapp.view.myView.adapter;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.demo.androidapp.MyApplication;
import com.demo.androidapp.R;
import com.demo.androidapp.model.common.ItemTypeEnum;
import com.demo.androidapp.model.entity.ChatRecord;
import com.demo.androidapp.model.entity.Clock;
import com.demo.androidapp.model.entity.Task;
import com.demo.androidapp.util.DateTimeUtil;
import com.demo.androidapp.view.myView.adapter.itemModel.ChatItemModel;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.ArrayList;
import java.util.List;

public class ChatItemAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private boolean isShow = false;

    private boolean allChecked = false;

    private List<ChatRecord> chatRecords;

    private List<ChatItemModel> chatItemModels;

    private ItemLongOnClickListener itemLongOnClickListener;

    private TaskItemOnClickListener taskItemOnClickListener;

    private ClockItemOnClickListener clockItemOnClickListener;

    private String fImgUrl;

    private Bitmap fBitmap;
    private Bitmap uBitmap;

    public void setChatItemModels(List<ChatItemModel> chatItemModels) {
        this.chatItemModels.clear();
        if (chatItemModels == null || chatItemModels.size() == 0)return ;
        this.chatItemModels.addAll(chatItemModels);
        notifyDataSetChanged();
    }

    public void addChatItemModel(ChatItemModel chatItemModel) {
        Log.d("imageView", "addChatItemModel: " + chatItemModel.toString());
        this.chatItemModels.add(chatItemModel);
        notifyDataSetChanged();
        //notifyItemRangeInserted(chatItemModels.size()-1,1);
    }

    public void setItemLongOnClickListener(ItemLongOnClickListener itemLongOnClickListener) {
        this.itemLongOnClickListener = itemLongOnClickListener;
    }
    public void setClockItemOnClickListener(ClockItemOnClickListener clockItemOnClickListener) {
        this.clockItemOnClickListener = clockItemOnClickListener;
    }
    public void setTaskItemOnClickListener(TaskItemOnClickListener taskItemOnClickListener) {
        this.taskItemOnClickListener = taskItemOnClickListener;
    }

    public ChatItemAdapter(List<ChatItemModel> chatItemModels,String fImgUrl) {
        if (chatItemModels == null){
            this.chatItemModels = new ArrayList<>();
        }else {
            Log.d("imageView", "ChatItemAdapter: 数据长度：" + chatItemModels.size());
            this.chatItemModels = chatItemModels;
        }
        this.fImgUrl = fImgUrl;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        Log.d("imageView", "onCreateViewHolder: " + viewType);
        View view;
        switch (viewType) {
            case 1: {
                view = layoutInflater.inflate(R.layout.chat_text_item_left,parent,false);
                return new MyTextViewHolder(view);
            }
            case 2: {
                view = layoutInflater.inflate(R.layout.chat_text_item_right,parent,false);
                return new MyTextViewHolder(view);
            }
            case 3: {
                view = layoutInflater.inflate(R.layout.chat_task_item_left,parent,false);
                return new MyTaskViewHolder(view);
            }
            case 4: {
                view = layoutInflater.inflate(R.layout.chat_task_item_right,parent,false);
                return new MyTaskViewHolder(view);
            }
            case 5: {
                view = layoutInflater.inflate(R.layout.chat_clock_item_left,parent,false);
                return new MyClockViewHolder(view);
            }
            case 6: {
                view = layoutInflater.inflate(R.layout.chat_clock_item_right,parent,false);
                return new MyClockViewHolder(view);
            }
            default:{
                break;
            }
        }
        return null;
    }

    @Override
    public int getItemViewType(int position) {
        Log.d("imageView", "getItemViewType: " + position);
        return chatItemModels == null ? super.getItemViewType(position) : chatItemModels.get(position).getType();
    }

    @SuppressLint("SetTextI18n")
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ChatItemModel chatItemModel = chatItemModels.get(position);
        int type = chatItemModel.getType();
        if (type == 1 || type == 2) {
            Glide.with(MyApplication.getMyApplicationContext()).load(MyApplication.getApplication().getUser().getImg_url() + DateTimeUtil.getRandom()).into(((MyTextViewHolder) holder).chatItemImageView);
            ((MyTextViewHolder)holder).msgTextView.setText((String)chatItemModel.getData());
        }
        if (type == 3 || type == 4) {
            Task task = (Task)chatItemModel.getData();
            Glide.with(MyApplication.getMyApplicationContext()).load(MyApplication.getApplication().getUser().getImg_url() + DateTimeUtil.getRandom()).into(((MyTaskViewHolder) holder).chatItemImageView);
            ((MyTaskViewHolder)holder).chatTaskItemTaskTextView.setText(task.getTask());
            ((MyTaskViewHolder)holder).itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (taskItemOnClickListener == null)return;
                    taskItemOnClickListener.itemOnClick(position,task,type);
                }
            });
        }
        if (type == 5 || type == 6) {
            Clock clock = (Clock) chatItemModel.getData();
            Glide.with(MyApplication.getMyApplicationContext()).load(MyApplication.getApplication().getUser().getImg_url() + DateTimeUtil.getRandom()).into(((MyClockViewHolder) holder).chatItemImageView);
            ((MyClockViewHolder)holder).chatClockItemClockTaskTextView.setText(clock.getTask());
            ((MyClockViewHolder)holder).itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (clockItemOnClickListener == null)return;
                    clockItemOnClickListener.itemOnClick(position,clock,type);
                }
            });
        }
    }
    @Override
    public int getItemCount() {
        return chatItemModels == null ? 0 : chatItemModels.size();
    }

    //文本消息Item
    public static class MyTextViewHolder extends RecyclerView.ViewHolder {

        public View itemView;
        public ShapeableImageView chatItemImageView;
        public TextView msgTextView;

        public MyTextViewHolder(@NonNull View itemView) {
            super(itemView);
            this.itemView = itemView;
            this.chatItemImageView = itemView.findViewById(R.id.chatItemImageView);
            this.msgTextView = itemView.findViewById(R.id.chatItemMsgTextView);
        }
    }

    //任务消息Item
    public static class MyTaskViewHolder extends RecyclerView.ViewHolder {

        public View itemView;
        public ShapeableImageView chatItemImageView;
        public TextView chatTaskItemTaskTextView;

        public MyTaskViewHolder(@NonNull View itemView) {
            super(itemView);
            this.itemView = itemView;
            this.chatItemImageView = itemView.findViewById(R.id.chatItemImageView);
            this.chatTaskItemTaskTextView = itemView.findViewById(R.id.chatTaskItemTaskTextView);
        }
    }

    //时钟消息Item
    public static class MyClockViewHolder extends RecyclerView.ViewHolder {

        public View itemView;
        public ShapeableImageView chatItemImageView;
        public TextView chatClockItemClockTaskTextView;

        public MyClockViewHolder(@NonNull View itemView) {
            super(itemView);
            this.itemView = itemView;
            this.chatItemImageView = itemView.findViewById(R.id.chatItemImageView);
            this.chatClockItemClockTaskTextView = itemView.findViewById(R.id.chatClockItemClockTaskTextView);
        }
    }

    //item长按接口
    public interface ItemLongOnClickListener {
        void itemLongOnClick();
    }

    //任务item点击接口
    public interface TaskItemOnClickListener {
        void itemOnClick(int position,Task task,int type);
    }

    //时钟item点击接口
    public interface ClockItemOnClickListener {
        void itemOnClick(int position,Clock clock,int type);
    }
}
