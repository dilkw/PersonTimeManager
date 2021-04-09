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
import com.demo.androidapp.model.entity.Friend;
import com.demo.androidapp.util.DateTimeUtil;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FriendItemAdapter extends RecyclerView.Adapter<FriendItemAdapter.MyViewHolder> {

    private boolean isShow = false;

    private boolean allChecked = false;

    private List<Friend> friends;

    private List<Friend> editModelSelectedFriends;

    private ItemLongOnClickListener itemLongOnClickListener;

    private ItemOnClickListener itemOnClickListener;

    private FInfoOnClickListener fInfoOnClickListener;

    public List<Friend> getEditModelSelectedBills() {
        return editModelSelectedFriends;
    }

    //添加账单
    public void addFriend(Friend friend) {
        this.friends.add(friend);
        notifyDataSetChanged();
    }

    public void setItemLongOnClickListener(ItemLongOnClickListener itemLongOnClickListener) {
        this.itemLongOnClickListener = itemLongOnClickListener;
    }
    public void setItemOnClickListener(ItemOnClickListener itemOnClickListener) {
        this.itemOnClickListener = itemOnClickListener;
    }

    public void setFInfoOnClickListener(FInfoOnClickListener fInfoOnClickListener) {
        this.fInfoOnClickListener = fInfoOnClickListener;
    }

    public void setFriends(List<Friend> friends) {
        this.friends.clear();
        if (friends == null || friends.size() == 0)return ;
        this.friends.addAll(friends);
        notifyDataSetChanged();
    }

    public FriendItemAdapter(List<Friend> friends) {
        Log.d("imageView", "TasksItemAdapter: 数据长度：" + friends.size());
        this.friends = friends;
        editModelSelectedFriends = new ArrayList<>();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.friend_item,parent,false);
        return new MyViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Friend friend = friends.get(position);
        Log.d("imageView", "onBindViewHolder: " + friend.toString());
        holder.friendItemFNameTextView.setText(friend.getFname());
        Glide.with(MyApplication.getMyApplicationContext()).load(friend.getFimgurl() + DateTimeUtil.getRandom()).into(holder.friendItemImageView);
        holder.friendItemImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fInfoOnClickListener == null) return;
                fInfoOnClickListener.fInfoOnClick(position,friend.getId(),friend.getFuid());
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (itemOnClickListener == null) return;
                itemOnClickListener.itemOnClick(position,friend.getFuid(),friend.getFname(),friend.getFimgurl());
            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (itemLongOnClickListener == null) return false;
                itemLongOnClickListener.itemLongOnClick();
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return friends == null ? 0 : friends.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        public View itemView;
        public TextView friendItemFNameTextView;
        public ShapeableImageView friendItemImageView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            this.itemView = itemView;
            this.friendItemFNameTextView = itemView.findViewById(R.id.friendItemFNameTextView);
            this.friendItemImageView = itemView.findViewById(R.id.friendItemImageView);
        }
    }

    //item长按接口
    public interface ItemLongOnClickListener {
        void itemLongOnClick();
    }

    //item点击接口
    public interface ItemOnClickListener {
        void itemOnClick(int position,String fUid,String fName,String fImgUrl);
    }

    //item点击接口
    public interface FInfoOnClickListener {
        void fInfoOnClick(int position,long id,String fUid);
    }
}
