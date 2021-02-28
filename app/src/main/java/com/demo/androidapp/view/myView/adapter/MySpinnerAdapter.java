package com.demo.androidapp.view.myView.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.demo.androidapp.R;
import com.demo.androidapp.model.entity.CategoryOfTask;
import com.demo.androidapp.view.myView.MySpinner;
import com.demo.androidapp.viewmodel.AddTaskViewModel;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class MySpinnerAdapter extends BaseAdapter {

    private List<CategoryOfTask> categoryOfTaskList;

    private Context context;

    private AddTaskViewModel addTaskViewModel;

    private MyItemOnClickListener myItemOnClickListener;

    public void setCategoryOfTaskList(List<CategoryOfTask> categoryOfTaskList) {
        this.categoryOfTaskList.clear();
        this.categoryOfTaskList.addAll(categoryOfTaskList);
    }

    public MySpinnerAdapter(Context context, List<CategoryOfTask> categoryOfTaskList, AddTaskViewModel addTaskViewModel) {
        this.categoryOfTaskList = categoryOfTaskList;
        Log.d("imageView", "MySpinnerAdapter: " + this.categoryOfTaskList.toString());
        this.context = context;
        this.addTaskViewModel = addTaskViewModel;
    }

    public void addCategory(CategoryOfTask categoryOfTask) {
        categoryOfTaskList.add(categoryOfTask);
        try {
            Log.d("imageView", "addCategory: id");
            categoryOfTask.setId(addTaskViewModel.addCategory(categoryOfTask));
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Log.d("imageView", "addCategory: " + categoryOfTaskList.toString());
        Log.d("imageView", "addCategory: size:" +categoryOfTaskList.size() + "===== MySpinnerAdapter---" + categoryOfTask.toString());
        notifyDataSetChanged();
    }

    public void removeCategory(int position) {
        addTaskViewModel.deleteCategory(categoryOfTaskList.get(position));
        categoryOfTaskList.remove(position);
        this.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return categoryOfTaskList.size();
    }

    @Override
    public Object getItem(int position) {
        return categoryOfTaskList.get(position);
    }
    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint("ViewHolder")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Log.d("imageView", "getView: ");
        convertView = (LayoutInflater.from(this.context)).inflate(R.layout.category_item,null);
        ImageView imageView = convertView.findViewById(R.id.categoryItemImgView);
        TextView textView = convertView.findViewById(R.id.categoryItemTextView);
        ImageButton imageButton = convertView.findViewById(R.id.categoryItemImgButton);
        imageView.setImageResource(R.drawable.ic_category2);
        imageButton.setImageResource(R.drawable.category_item_delete_img);
        textView.setClickable(true);
        textView.setText(categoryOfTaskList.get(position).getCategoryName());
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (myItemOnClickListener == null) {
                    return;
                }
                myItemOnClickListener.itemOnClick(position);
            }
        });
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myItemOnClickListener.itemDeleteClick(position);
            }
        });
        return convertView;
    }

    public interface MyItemOnClickListener{
        void itemOnClick(int position);
        void itemDeleteClick(int position);
    }

    public void setMyItemOnClickListener(MyItemOnClickListener myItemOnClickListener) {
        this.myItemOnClickListener = myItemOnClickListener;
    }



}
