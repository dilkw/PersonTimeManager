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

import com.bumptech.glide.Glide;
import com.demo.androidapp.MyApplication;
import com.demo.androidapp.R;
import com.demo.androidapp.model.entity.ChatRecord;
import com.demo.androidapp.model.entity.Friend;
import com.demo.androidapp.util.DateTimeUtil;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ChatItemAdapter extends RecyclerView.Adapter<ChatItemAdapter.MyViewHolder> {

    private boolean isShow = false;

    private boolean allChecked = false;

    private List<ChatRecord> chatRecords;

    private ItemLongOnClickListener itemLongOnClickListener;

    private ItemOnClickListener itemOnClickListener;

    private String fImgUrl;

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
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.chat_item,parent,false);
        return new MyViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        ChatRecord chatRecord = chatRecords.get(position);
        if (chatRecord.getType().equals("发送")) {
            holder.friendImageView.setVisibility(View.GONE);
            holder.friendMsgTextView.setVisibility(View.GONE);
            holder.userImageView.setVisibility(View.VISIBLE);
            holder.userMsgTextView.setVisibility(View.VISIBLE);
            Glide.with(MyApplication.getMyApplicationContext()).load(MyApplication.getApplication().getUser().getImg_url() + DateTimeUtil.getRandom()).into(holder.userImageView);
            holder.userMsgTextView.setText(chatRecord.getTask());
        }else {
            holder.userImageView.setVisibility(View.GONE);
            holder.userMsgTextView.setVisibility(View.GONE);
            holder.friendImageView.setVisibility(View.VISIBLE);
            holder.friendMsgTextView.setVisibility(View.VISIBLE);
            Glide.with(MyApplication.getMyApplicationContext()).load(fImgUrl + DateTimeUtil.getRandom()).into(holder.friendImageView);
            holder.friendMsgTextView.setText(chatRecord.getTask());
        }
    }
    @Override
    public int getItemCount() {
        return chatRecords == null ? 0 : chatRecords.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        public View itemView;
        public ShapeableImageView friendImageView;
        public TextView friendMsgTextView;
        public ShapeableImageView userImageView;
        public TextView userMsgTextView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            this.itemView = itemView;
            this.friendImageView = itemView.findViewById(R.id.chatItemFriendImageView);
            this.friendMsgTextView = itemView.findViewById(R.id.chatItemFriendMsgTextView);
            this.userImageView = itemView.findViewById(R.id.chatItemUserImageView);
            this.userMsgTextView = itemView.findViewById(R.id.chatItemUserMsgTextView);
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
