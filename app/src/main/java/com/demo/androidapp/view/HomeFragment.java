package com.demo.androidapp.view;

import androidx.annotation.RequiresApi;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.PowerManager;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.demo.androidapp.MainActivity;
import com.demo.androidapp.MyApplication;
import com.demo.androidapp.R;
import com.demo.androidapp.databinding.HomeFragmentBinding;
import com.demo.androidapp.model.entity.Task;
import com.demo.androidapp.model.common.RCodeEnum;
import com.demo.androidapp.model.common.ReturnData;
import com.demo.androidapp.model.entity.User;
import com.demo.androidapp.model.returnObject.ReturnListObject;
import com.demo.androidapp.util.DateTimeUtil;
import com.demo.androidapp.view.myView.adapter.TasksItemAdapter;
import com.demo.androidapp.viewmodel.HomeViewModel;
import com.google.android.material.appbar.MaterialToolbar;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class HomeFragment extends Fragment implements View.OnClickListener , SwipeRefreshLayout.OnRefreshListener {

    private HomeViewModel homeViewModel;

    private HomeFragmentBinding homeFragmentBinding;

    private List<Task> taskList = new ArrayList<>();

    TasksItemAdapter tasksItemAdapter;

    NavHostFragment navHostFragment;

    NavController controller;

    AppBarConfiguration appBarConfiguration;

    private FragmentManager fragmentManager;

    private LiveData<List<Task>> taskLiveData;

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        Log.d("imageView", "onCreateView0: ");
        homeFragmentBinding = DataBindingUtil.inflate(inflater,R.layout.home_fragment,container,false);
        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        fragmentManager = requireActivity().getSupportFragmentManager();
        navHostFragment = (NavHostFragment)fragmentManager.findFragmentById(R.id.fragment);
        assert navHostFragment != null;
        controller = navHostFragment.getNavController();
        appBarConfiguration = new AppBarConfiguration.Builder(controller.getGraph()).setDrawerLayout(homeFragmentBinding.drawerLayout).build();
        NavigationUI.setupWithNavController(homeFragmentBinding.homeFragmentToolBar,controller,appBarConfiguration);
        return homeFragmentBinding.getRoot();

    }
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        boolean isLogin = false;
        isLogin = getArguments() != null && getArguments().getBoolean("isLogin");
        if(MyApplication.getApplication().getUser() == null){
            controller.navigate(R.id.action_homeFragment_to_loginFragment);
        }else {
            initData(this,isLogin);
            setListener();
            if (!isIgnoringBatteryOptimizations()) {
                requestIgnoreBatteryOptimizations();
            }
        }
    }

    @SuppressLint("ResourceType")
    private void initData(LifecycleOwner lifecycleOwner,boolean isLogin) {
        Log.d("imageView",MyApplication.getApplication().getUser().getUid() + "++++++++++");
        User user = MyApplication.getApplication().getUser();
        homeFragmentBinding.userNameTextView.setText(user.getName());
        Glide.with(requireContext()).load(user.getImg_url() + DateTimeUtil.getRandom()).into(homeFragmentBinding.drawerLayoutUserImage);
        tasksItemAdapter = new TasksItemAdapter((List<Task>)(new ArrayList<Task>()));
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, RecyclerView.VERTICAL);
        homeFragmentBinding.recyclerView.setLayoutManager(staggeredGridLayoutManager);
        homeFragmentBinding.recyclerView.setAdapter(tasksItemAdapter);
        homeViewModel.getAllTaskByUidInServer().observe(getViewLifecycleOwner(), new Observer<ReturnData<ReturnListObject<Task>>>() {
            @Override
            public void onChanged(ReturnData<ReturnListObject<Task>> returnListObjectReturnData) {
                if (returnListObjectReturnData == null)return;
                RCodeEnum rCodeEnum = RCodeEnum.returnRCodeEnumByCode(returnListObjectReturnData.getCode());
                switch (rCodeEnum) {
                    case OK: {
                        Log.d("imageView", "onChanged: 成功");
                        if (returnListObjectReturnData.getData() == null)break;
                        taskList = returnListObjectReturnData.getData().getItems();
                        tasksItemAdapter.setTasks(taskList);
                        tasksItemAdapter.notifyDataSetChanged();
                        homeViewModel.deleteAllTaskAndAddInDB(taskList);
                        break;
                    }
                    case ERROR_AUTH:{
                        Log.d("imageView", "onChanged: token_error");
                        break;
                    }
                    case SERVER_ERROR:{
                        Toast.makeText(getContext(), "请检查您的网络链接", Toast.LENGTH_LONG).show();
                        Log.d("imageView", "onChanged: token_error");
                        break;
                    }
                }
            }
        });

    }

    public void setListener() {
        homeFragmentBinding.loginOutBtn.setOnClickListener(this);
        homeFragmentBinding.cancelImageButton.setOnClickListener(this);
        homeFragmentBinding.deleteImageButton.setOnClickListener(this);
        homeFragmentBinding.allSelectImageButton.setOnClickListener(this);
        homeFragmentBinding.myFloatingActionButton.setOnClickListener(this);
        homeFragmentBinding.turnToClockFragmentBtn.setOnClickListener(this);
        homeFragmentBinding.turnToBillFragmentBtn.setOnClickListener(this);
        homeFragmentBinding.turnToFriendFragmentBtn.setOnClickListener(this);
        homeFragmentBinding.turnToUserInfoFragmentBtn.setOnClickListener(this);
        tasksItemAdapter.setItemLongOnClickListener(new TasksItemAdapter.ItemLongOnClickListener() {
            @Override
            public void itemLongOnClick() {
                tasksItemAdapter.setCheckBoxIsShow();
                homeFragmentBinding.itemLongClickEditWindow.setVisibility(View.VISIBLE);
            }
        });
        tasksItemAdapter.setItemOnClickListener(new TasksItemAdapter.ItemOnClickListener() {
            @Override
            public void itemOnClick(int position,Task task) {
                //将点击的task对象放到MainActivity中的map中
                Log.d("imageView", "itemOnClick: " + position);
                ((MainActivity)requireActivity()).putDataInToMap("task", task);
                NavController navController = Navigation.findNavController(requireView());
                navController.navigate(R.id.action_homeFragment_to_addTaskFragment);
            }
        });
        homeFragmentBinding.homeFragmentSwipeRefreshLayout.setOnRefreshListener(this);

        SearchView searchView = (SearchView) (homeFragmentBinding.homeFragmentToolBar.getMenu().findItem(R.id.taskSearch).getActionView());
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Log.d("imageView", "onQueryTextChange:");
                if (taskLiveData != null && taskLiveData.hasObservers()) {
                    taskLiveData.removeObservers(getViewLifecycleOwner());
                }
                taskLiveData = homeViewModel.getTasksLiveDataByPatternInDB(newText);
                taskLiveData.observe(getViewLifecycleOwner(), new Observer<List<Task>>() {
                    @Override
                    public void onChanged(List<Task> tasks) {
                        if (tasks == null || tasks.size() == 0) {
                            return;
                        }
                        Log.d("imageView", tasks.get(0).toString());
                        tasksItemAdapter.setTasks(tasks);
                        tasksItemAdapter.notifyDataSetChanged();
                    }
                });
                return true;
            }
        });
        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                if (taskLiveData != null && taskLiveData.hasObservers()) {
                    taskLiveData.removeObservers(getViewLifecycleOwner());
                }
                taskLiveData = homeViewModel.getAllTaskLiveDataByUidInDB();
                taskLiveData.observe(getViewLifecycleOwner(), new Observer<List<Task>>() {
                    @Override
                    public void onChanged(List<Task> tasks) {
                        tasksItemAdapter.setTasks(tasks);
                        tasksItemAdapter.notifyDataSetChanged();
                    }
                });
                return false;
            }
        });
        homeFragmentBinding.homeFragmentToolBar.setOnMenuItemClickListener(new MaterialToolbar.OnMenuItemClickListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
//                    case R.id.more: {
//                        Log.d("imageView", "onMenuItemClick: more");
//                        break;
//                    }
                    default:{
                        break;
                    }
                }
                return false;
            }
        });
    }

    @SuppressLint({"NonConstantResourceId", "ResourceAsColor"})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cancelImageButton: {
                tasksItemAdapter.cancelTask();
                homeFragmentBinding.itemLongClickEditWindow.setVisibility(View.GONE);
                break;
            }
            case R.id.deleteImageButton: {
                homeViewModel.deleteTaskByIdsInServer(tasksItemAdapter.getEditModelSelectedTasks()).observe(getViewLifecycleOwner(), new Observer<ReturnData<Object>>() {
                    @Override
                    public void onChanged(ReturnData<Object> objectReturnData) {
                        if (objectReturnData.getCode() == RCodeEnum.OK.getCode()) {
                            tasksItemAdapter.deleteSelectedTask();
                            Toast.makeText(getContext(),"删除成功",Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(getContext(),objectReturnData.getMsg(),Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                break;
            }
            case R.id.allSelectImageButton: {
                homeFragmentBinding.allSelectImageButton.setBackgroundColor(R.color.colorTextTrue);
                tasksItemAdapter.selectedAllTasks();
                break;
            }
            case R.id.myFloatingActionButton: {
                Log.d("imageView", "onClick: 添加按钮");
                //NavController navController = Navigation.findNavController(homeFragmentBinding.myFloatingActionButton);
                controller.navigate(R.id.action_homeFragment_to_addTaskFragment);
                break;
            }
            case R.id.turnToClockFragmentBtn: {
                //跳转时钟界面
                controller.navigate(R.id.action_homeFragment_to_clockFragment);
                break;
            }
            case R.id.turnToBillFragmentBtn: {
                //跳转账单界面
                controller.navigate(R.id.action_homeFragment_to_billFragment);
                break;
            }
            case R.id.turnToFriendFragmentBtn: {
                //跳转好友界面
                controller.navigate(R.id.action_homeFragment_to_friendFragment);
                break;
            }
            case R.id.turnToUserInfoFragmentBtn: {
                //跳转用户信息界面
                Bundle bundle = new Bundle();
                bundle.putBoolean("isMe",true);
                controller.navigate(R.id.action_homeFragment_to_userInfoFragment,bundle);
                break;
            }
            case R.id.loginOutBtn: {
                Log.d("imageView", "onClick: 退出登录按钮");
                MyApplication.getApi().signOut().observe(getViewLifecycleOwner(), new Observer<ReturnData<Object>>() {
                    @Override
                    public void onChanged(ReturnData<Object> objectReturnData) {
                        if (objectReturnData == null)return;
                        if (objectReturnData.getCode() == RCodeEnum.OK.getCode()) {
                            EMClient.getInstance().logout(true, new EMCallBack() {
                                @Override
                                public void onSuccess() {
                                    // TODO Auto-generated method stub

                                }
                                @Override
                                public void onProgress(int progress, String status) {
                                    // TODO Auto-generated method stub
                                }
                                @Override
                                public void onError(int code, String message) {
                                    // TODO Auto-generated method stub
                                }
                            });
                            MyApplication.getApplication().signOut();
                            requireActivity().finish();
                            Intent intent = new Intent(); //生成Intent对象
                            intent.setClass(getActivity(), MainActivity.class);
                            startActivity(intent);
                        }else {
                            Toast.makeText(getContext(),"退出登录失败",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                break;
            }
            default:{
                break;
            }
        }
    }

    //下拉刷新
    @Override
    public void onRefresh() {
        homeFragmentBinding.homeFragmentSwipeRefreshLayout.setRefreshing(true);
        homeViewModel.getAllTaskByUidInServer().observe(getViewLifecycleOwner(), new Observer<ReturnData<ReturnListObject<Task>>>() {
            @Override
            public void onChanged(ReturnData<ReturnListObject<Task>> returnListObjectReturnData) {
                if (returnListObjectReturnData.getCode() == RCodeEnum.OK.getCode()) {
                    if (tasksItemAdapter.getItemCount() != returnListObjectReturnData.getData().getTotal()) {
                        tasksItemAdapter.setTasks(returnListObjectReturnData.getData().getItems());
                        tasksItemAdapter.notifyDataSetChanged();
                    }
                    Toast.makeText(requireContext(),"刷新成功",Toast.LENGTH_SHORT).show();
                }
                homeFragmentBinding.homeFragmentSwipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private boolean isIgnoringBatteryOptimizations() {
        boolean isIgnoring = false;
        PowerManager powerManager = (PowerManager) requireActivity().getSystemService(Context.POWER_SERVICE);
        if (powerManager != null) {
            isIgnoring = powerManager.isIgnoringBatteryOptimizations(requireActivity().getPackageName());
        }
        return isIgnoring;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void requestIgnoreBatteryOptimizations() {
        try {
            @SuppressLint("BatteryLife") Intent intent = new Intent(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
            intent.setData(Uri.parse("package:" + requireActivity().getPackageName()));
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
