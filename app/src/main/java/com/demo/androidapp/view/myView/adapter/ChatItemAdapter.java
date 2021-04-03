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
import com.demo.androidapp.model.entity.Task;
import com.demo.androidapp.util.DateTimeUtil;
import com.demo.androidapp.view.myView.adapter.itemModel.ChatItemModel;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.List;

public class ChatItemAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private boolean isShow = false;

    private boolean allChecked = false;

    private List<ChatRecord> chatRecords;

    private List<ChatItemModel> chatItemModelList;

    private ItemLongOnClickListener itemLongOnClickListener;

    private ItemOnClickListener itemOnClickListener;

    private String fImgUrl;

    private Bitmap fBitmap;
    private Bitmap uBitmap;

    public void setChatRecords(List<ChatRecord> chatRecords) {
        this.chatRecords.clear();
        if (chatRecords == null || chatRecords.size() == 0)return ;
        this.chatRecords.addAll(chatRecords);
        notifyDataSetChanged();
    }

    public void addChatRecords(ChatRecord chatRecord) {
        Log.d("imageView", "addChatRecords: ");
        this.chatRecords.add(chatRecord);
        notifyItemRangeInserted(chatRecords.size()-1,1);
        //notifyDataSetChanged();
    }

    public void setItemLongOnClickListener(ItemLongOnClickListener itemLongOnClickListener) {
        this.itemLongOnClickListener = itemLongOnClickListener;
    }
    public void setItemOnClickListener(ItemOnClickListener itemOnClickListener) {
        this.itemOnClickListener = itemOnClickListener;
    }

    public ChatItemAdapter(List<ChatRecord> chatRecords,String fImgUrl) {
        Log.d("imageView", "ChatItemAdapter: 数据长度：" + chatRecords.size());
        this.chatRecords = chatRecords;
        this.fImgUrl = fImgUrl;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.chat_text_item_left,parent,false);
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
        return new MyTextViewHolder(view);
    }

    @Override
    public int getItemViewType(int position) {
        return chatItemModelList.get(position).getType();
    }

    @SuppressLint("SetTextI18n")
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ChatRecord chatRecord = chatRecords.get(position);
        ChatItemModel chatItemModel = chatItemModelList.get(position);
        int type = chatItemModelList.get(position).getType();
        if (type == 1 || type == 2) {
            Glide.with(MyApplication.getMyApplicationContext()).load(MyApplication.getApplication().getUser().getImg_url() + DateTimeUtil.getRandom()).into(((MyTextViewHolder) holder).imageView);
            ((MyTextViewHolder)holder).msgTextView.setText((String)chatItemModel.getData());
        }
        if (type == 3 || type == 4) {
            Glide.with(MyApplication.getMyApplicationContext()).load(MyApplication.getApplication().getUser().getImg_url() + DateTimeUtil.getRandom()).into(((MyTextViewHolder) holder).imageView);
            ((MyTaskViewHolder)holder).chatTaskItemTaskTextView.setText(((Task)chatItemModel.getData()).getTask());
        }
        if (type == 5 || type == 6) {
            Glide.with(MyApplication.getMyApplicationContext()).load(MyApplication.getApplication().getUser().getImg_url() + DateTimeUtil.getRandom()).into(((MyTextViewHolder) holder).imageView);
            ((MyClockViewHolder)holder).chatClockItemClockTaskTextView.setText(chatRecord.getTask());
        }
    }
    @Override
    public int getItemCount() {
        return chatRecords == null ? 0 : chatRecords.size();
    }

    //文本消息Item
    public static class MyTextViewHolder extends RecyclerView.ViewHolder {

        public View itemView;
        public ShapeableImageView imageView;
        public TextView msgTextView;

        public MyTextViewHolder(@NonNull View itemView) {
            super(itemView);
            this.itemView = itemView;
            this.imageView = itemView.findViewById(R.id.chatItemImageView);
            this.msgTextView = itemView.findViewById(R.id.chatItemMsgTextView);
        }
    }

    //任务消息Item
    public static class MyTaskViewHolder extends RecyclerView.ViewHolder {

        public View itemView;
        public ShapeableImageView imageView;
        public TextView chatTaskItemTaskTextView;

        public MyTaskViewHolder(@NonNull View itemView) {
            super(itemView);
            this.itemView = itemView;
            this.imageView = itemView.findViewById(R.id.chatItemImageView);
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

    //item点击接口
    public interface ItemOnClickListener {
        void itemOnClick(int position,long id,String fuid);
    }
}
