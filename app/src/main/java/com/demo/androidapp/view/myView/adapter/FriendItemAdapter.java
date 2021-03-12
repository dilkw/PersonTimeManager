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
import com.demo.androidapp.model.entity.Friend;
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

    public List<Friend> getEditModelSelectedBills() {
        return editModelSelectedFriends;
    }

    //长按Item时弹出编辑菜单，取消按钮（删除所选择的）
    public void cancelFriend() {
        isShow = false;
        allChecked = false;
        notifyDataSetChanged();
    }
    //长按Item时弹出编辑菜单，删除按钮（删除所选择的）
    public void deleteSelectedFriends() {
        allChecked = false;
        if (editModelSelectedFriends.size() == 0)return ;
        friends.removeAll(Objects.requireNonNull(editModelSelectedFriends));
        notifyDataSetChanged();
    }
    //长按Item时弹出编辑菜单，全选按钮
    public void selectedAllFriends() {
        allChecked = true;
        notifyDataSetChanged();
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

    public void setFriends(List<Friend> friends) {
        this.friends.clear();
        if (friends == null || friends.size() == 0)return ;
        this.friends.addAll(friends);
        notifyDataSetChanged();
    }

    public void setCheckBoxIsShow() {
        isShow = true;
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
        holder.friendItemFNameTextView.setText(friend.getFriend_name());
        holder.friendItemImageView.setImageResource(R.drawable.background);
        holder.itemFriendCheckBox.setVisibility(isShow ? View.VISIBLE : View.GONE);
        holder.itemFriendCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    editModelSelectedFriends.add(friends.get(position));
                }else {
                    editModelSelectedFriends.remove(friends.get(position));
                }
            }
        });
        if (isShow) {
            holder.itemFriendCheckBox.setChecked(allChecked);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (itemOnClickListener == null) return;
                itemOnClickListener.itemOnClick(position);
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
        public CheckBox itemFriendCheckBox;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            this.itemView = itemView;
            this.friendItemFNameTextView = itemView.findViewById(R.id.friendItemFNameTextView);
            this.friendItemImageView = itemView.findViewById(R.id.friendItemImageView);
            this.itemFriendCheckBox = itemView.findViewById(R.id.itemFriend_checkBox);
        }
    }

    //item长按接口
    public interface ItemLongOnClickListener {
        void itemLongOnClick();
    }

    //item点击接口
    public interface ItemOnClickListener {
        void itemOnClick(int position);
    }
}
