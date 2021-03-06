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
import com.demo.androidapp.model.entity.Bill;
import com.demo.androidapp.model.entity.Clock;
import com.demo.androidapp.util.DateTimeUtil;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class BillItemAdapter extends RecyclerView.Adapter<BillItemAdapter.MyViewHolder> {

    private boolean isShow = false;

    private boolean allChecked = false;

    private List<Bill> bills;

    private List<Bill> editModelSelectedBills;

    private ItemLongOnClickListener itemLongOnClickListener;

    private ItemOnClickListener itemOnClickListener;

    public List<Bill> getEditModelSelectedBills() {
        return editModelSelectedBills;
    }

    private DateTimeUtil dateTimeUtil;

    //长按Item时弹出编辑菜单，取消按钮（删除所选择的）
    public void cancelBill() {
        isShow = false;
        allChecked = false;
        notifyDataSetChanged();
    }
    //长按Item时弹出编辑菜单，删除按钮（删除所选择的）
    public List<Bill> deleteSelectedBills() {
        allChecked = false;
        if (editModelSelectedBills.size() == 0)return null;
        bills.removeAll(Objects.requireNonNull(editModelSelectedBills));
        notifyDataSetChanged();
        return editModelSelectedBills;
    }
    //长按Item时弹出编辑菜单，全选按钮
    public void selectedAllBills() {
        allChecked = true;
        notifyDataSetChanged();
    }

    public void setItemLongOnClickListener(ItemLongOnClickListener itemLongOnClickListener) {
        this.itemLongOnClickListener = itemLongOnClickListener;
    }
    public void setItemOnClickListener(ItemOnClickListener itemOnClickListener) {
        this.itemOnClickListener = itemOnClickListener;
    }

    public void setBills(List<Bill> bills) {
        this.bills.clear();
        this.bills = bills;
        notifyDataSetChanged();
    }

    public void setCheckBoxIsShow() {
        isShow = true;
        notifyDataSetChanged();
    }

    public BillItemAdapter(List<Bill> bills) {
        Log.d("imageView", "TasksItemAdapter: 数据长度：" + bills.size());
        this.bills = bills;
        editModelSelectedBills = new ArrayList<>();
        dateTimeUtil = new DateTimeUtil();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.bill_item,parent,false);
        return new MyViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Bill bill = bills.get(position);
        holder.billContentTextView.setText(bill.getContent());
        holder.billCreateTimeTextView.setText(dateTimeUtil.longToStrYMDHM(bill.getCreatedTime()));
        holder.billItemMoneyTextView.setText((bill.isCategory() ? "-" : "+") + bill.getMoney());
        holder.billItemCheckBox.setVisibility(isShow ? View.VISIBLE : View.GONE);
        holder.billItemCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    editModelSelectedBills.add(bills.get(position));
                }else {
                    editModelSelectedBills.remove(bills.get(position));
                }
            }
        });
        if (isShow) {
            holder.billItemCheckBox.setChecked(allChecked);
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
        return bills == null ? 0 : bills.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        public View itemView;
        public TextView billContentTextView;
        public TextView billCreateTimeTextView;
        public TextView billItemMoneyTextView;
        public CheckBox billItemCheckBox;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            this.itemView = itemView;
            this.billContentTextView = itemView.findViewById(R.id.billItemContentTextView);
            this.billCreateTimeTextView = itemView.findViewById(R.id.billItemCreateTimeTextView);
            this.billItemMoneyTextView = itemView.findViewById(R.id.billItemMoneyTextView);
            this.billItemCheckBox = itemView.findViewById(R.id.billItemCheckBox);
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
