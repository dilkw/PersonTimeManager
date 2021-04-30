package com.demo.androidapp.view;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.demo.androidapp.MainActivity;
import com.demo.androidapp.R;
import com.demo.androidapp.databinding.ShareFragmentBinding;
import com.demo.androidapp.model.entity.Clock;
import com.demo.androidapp.model.entity.Task;
import com.demo.androidapp.view.myView.adapter.ClockItemAdapter;
import com.demo.androidapp.view.myView.adapter.TasksItemAdapter;
import com.demo.androidapp.viewmodel.ShareViewModel;

import java.util.ArrayList;
import java.util.List;

public class ShareFragment extends Fragment implements View.OnClickListener {

    private ShareFragmentBinding shareFragmentBinding;

    private ShareViewModel shareViewModel;

    private FragmentManager fragmentManager;

    private NavHostFragment navHostFragment;

    private NavController controller;

    private ClockItemAdapter clockItemAdapter;

    private TasksItemAdapter tasksItemAdapter;

    private LiveData<List<Clock>> clocksLiveData;

    private LiveData<List<Task>> tasksLiveData;

    private String type = "";

    public static ShareFragment newInstance() {
        return new ShareFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        shareFragmentBinding = DataBindingUtil.inflate(inflater,R.layout.share_fragment,container,false);
        fragmentManager = requireActivity().getSupportFragmentManager();
        navHostFragment = (NavHostFragment)fragmentManager.findFragmentById(R.id.fragment);
        assert navHostFragment != null;
        controller = navHostFragment.getNavController();
        return shareFragmentBinding.getRoot();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        shareViewModel = new ViewModelProvider(this).get(ShareViewModel.class);
        if (getArguments() != null) {
            type = getArguments().getString("type");
        }
        if (type.equals("clock")) {
            Log.d("imageView", "onActivityCreated: 分享时钟");
            clockItemAdapter = new ClockItemAdapter((List<Clock>) (new ArrayList<Clock>()));
            shareFragmentBinding.shareRecyclerView.setLayoutManager(new LinearLayoutManager(requireActivity()));
            shareViewModel.getAllClocksLiveDataInDB().observe(getViewLifecycleOwner(), new Observer<List<Clock>>() {
                @Override
                public void onChanged(List<Clock> clocks) {
                    clockItemAdapter.setClocks(clocks);
                    clockItemAdapter.notifyDataSetChanged();
                }
            });
            clockItemAdapter.setCheckBoxIsShow();
            shareFragmentBinding.shareRecyclerView.setAdapter(clockItemAdapter);
        }else {
            Log.d("imageView", "onActivityCreated: 分享任务");
            tasksItemAdapter = new TasksItemAdapter((List<Task>) (new ArrayList<Task>()));
            StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, RecyclerView.VERTICAL);
            shareFragmentBinding.shareRecyclerView.setLayoutManager(staggeredGridLayoutManager);
            shareViewModel.getAllTaskLiveDataByUidInDB().observe(getViewLifecycleOwner(), new Observer<List<Task>>() {
                @Override
                public void onChanged(List<Task> tasks) {
                    Log.d("imageView", "onChanged: " + tasks.size());
                    tasksItemAdapter.setTasks(tasks);
                    tasksItemAdapter.notifyDataSetChanged();
                }
            });
            tasksItemAdapter.setCheckBoxIsShow();
            shareFragmentBinding.shareRecyclerView.setAdapter(tasksItemAdapter);
        }
        setListener();
    }

    public void setListener() {
        Log.d("imageView", "setListener: ");
        shareFragmentBinding.shareFragmentSearchView.setQueryHint("搜索");
        //shareFragmentBinding.shareFragmentSearchView.onActionViewExpanded();
        shareFragmentBinding.shareFragmentSearchView.clearFocus();
        //搜索框监听事件
        //shareFragmentBinding.shareFragmentSearchView.setIconified(false);
        //查询监听
        shareFragmentBinding.shareFragmentSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Log.d("imageView", "onQueryTextChange:");
                if (type.equals("clock")) {
                    if (clocksLiveData != null && clocksLiveData.hasObservers()) {
                        clocksLiveData.removeObservers(getViewLifecycleOwner());
                    }
                    clocksLiveData = shareViewModel.getClocksLiveDataByPatternInDB(newText);
                    clocksLiveData.observe(getViewLifecycleOwner(), new Observer<List<Clock>>() {
                        @Override
                        public void onChanged(List<Clock> clocks) {
                            clockItemAdapter.setClocks(clocks);
                            clockItemAdapter.notifyDataSetChanged();
                        }
                    });
                }else if (type.equals("task")){
                    if (tasksLiveData != null && tasksLiveData.hasObservers()) {
                        tasksLiveData.removeObservers(getViewLifecycleOwner());
                    }
                    tasksLiveData = shareViewModel.getTasksLiveDataByPatternInDB(newText);
                    tasksLiveData.observe(getViewLifecycleOwner(), new Observer<List<Task>>() {
                        @Override
                        public void onChanged(List<Task> tasks) {
                            tasksItemAdapter.setTasks(tasks);
                            tasksItemAdapter.notifyDataSetChanged();
                        }
                    });
                }
                return true;
            }
        });
        shareFragmentBinding.shareFragmentSearchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                if (type.equals("clock")) {
                    if (clocksLiveData != null && clocksLiveData.hasObservers()) {
                        clocksLiveData.removeObservers(getViewLifecycleOwner());
                    }
                    clocksLiveData = shareViewModel.getAllClocksLiveDataInDB();
                    clocksLiveData.observe(getViewLifecycleOwner(), new Observer<List<Clock>>() {
                        @Override
                        public void onChanged(List<Clock> clocks) {
                            clockItemAdapter.setClocks(clocks);
                            clockItemAdapter.addSelectClockInStart(clockItemAdapter.getEditModelSelectedClocks());
                            clockItemAdapter.notifyDataSetChanged();
                        }
                    });
                }else if (type.equals("task")){
                    if (tasksLiveData != null && tasksLiveData.hasObservers()) {
                        tasksLiveData.removeObservers(getViewLifecycleOwner());
                    }
                    tasksLiveData = shareViewModel.getAllTaskLiveDataByUidInDB();
                    tasksLiveData.observe(getViewLifecycleOwner(), new Observer<List<Task>>() {
                        @Override
                        public void onChanged(List<Task> tasks) {
                            tasksItemAdapter.setTasks(tasks);
                            tasksItemAdapter.notifyDataSetChanged();
                        }
                    });
                }
                return false;
            }
        });

        shareFragmentBinding.shareFragmentCancelBtn.setOnClickListener(this);
        shareFragmentBinding.shareFragmentEnterBtn.setOnClickListener(this);
    }

    @SuppressLint({"NonConstantResourceId", "ResourceAsColor"})
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.shareFragmentCancelBtn: {
                Log.d("imageView", "取消分享");
                if (type.equals("clock")) {
                    clockItemAdapter.cancelTask();
                }else if (type.equals("task")) {
                    tasksItemAdapter.cancelTask();
                }
                controller.navigateUp();
                break;
            }
            case R.id.shareFragmentEnterBtn: {
                Log.d("imageView", "确定分享");
                if (type.equals("clock")) {
                    List<Clock> clocks = clockItemAdapter.getEditModelSelectedClocks();
                    ((MainActivity)requireActivity()).putDataInToMap("clocks",clocks);
                    ((MainActivity)requireActivity()).putDataInToMap("shareType","clock");
                }else if (type.equals("task")) {
                    List<Task> tasks = tasksItemAdapter.getEditModelSelectedTasks();
                    ((MainActivity)requireActivity()).putDataInToMap("tasks",tasks);
                    ((MainActivity)requireActivity()).putDataInToMap("shareType","task");
                }
                controller.navigateUp();
                break;
            }
            default:{
                break;
            }
        }
    }
}