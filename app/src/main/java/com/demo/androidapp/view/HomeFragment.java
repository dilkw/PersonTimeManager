package com.demo.androidapp.view;

import androidx.annotation.RequiresApi;
import androidx.databinding.DataBindingUtil;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.annotation.SuppressLint;
import android.content.Intent;
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

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.demo.androidapp.MainActivity;
import com.demo.androidapp.MyApplication;
import com.demo.androidapp.R;
import com.demo.androidapp.databinding.HomeFragmentBinding;
import com.demo.androidapp.model.entity.Task;
import com.demo.androidapp.model.common.RCodeEnum;
import com.demo.androidapp.model.common.ReturnData;
import com.demo.androidapp.util.DataSP;
import com.demo.androidapp.view.myView.adapter.TasksItemAdapter;
import com.demo.androidapp.viewmodel.HomeViewModel;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class HomeFragment extends Fragment implements View.OnClickListener {

    private HomeViewModel homeViewModel;

    private HomeFragmentBinding homeFragmentBinding;

    private List<Task> taskList = new ArrayList<>();

    TasksItemAdapter tasksItemAdapter;

    NavHostFragment navHostFragment;

    NavController controller;

    AppBarConfiguration appBarConfiguration;

    private FragmentManager fragmentManager;

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        Log.d("imageView", "onCreateView0: ");
        homeFragmentBinding = DataBindingUtil.inflate(inflater,R.layout.home_fragment,container,false);
        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        View view = homeFragmentBinding.getRoot();
        Log.d("imageView", "onCreateView: " + view.getClass().getSimpleName());
        DrawerLayout drawerLayout = view.findViewById(R.id.drawer_layout);
        NavigationView navView = view.findViewById(R.id.nav_view);
        fragmentManager = requireActivity().getSupportFragmentManager();
        navHostFragment = (NavHostFragment)fragmentManager.findFragmentById(R.id.fragment);
        assert navHostFragment != null;
        controller = navHostFragment.getNavController();
        appBarConfiguration = new AppBarConfiguration.Builder(controller.getGraph()).setDrawerLayout(drawerLayout).build();
        NavigationUI.setupWithNavController(homeFragmentBinding.homeFragmentToolBar,controller,appBarConfiguration);
        return view;

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        DataSP dataSP = new DataSP(getContext());
        Log.d("imageView", "onCreateView1: ");
        boolean isLogin = false;
        isLogin = getArguments() != null && getArguments().getBoolean("isLogin");
        Log.d("imageView","pppppppp" + MyApplication.getApplication().getCOOKIE());
        if((MyApplication.getApplication().getUSER_NAME()).equals("userName")){
            Log.d("imageView", "onCreateView2: ");
            controller.navigate(R.id.action_homeFragment_to_loginFragment);
        }else {
            initData(this,isLogin);
            setListener();
        }
    }

    private void initData(LifecycleOwner lifecycleOwner,boolean isLogin) {
        Log.d("imageView",MyApplication.getApplication().getUID() + "++++++++++");
        //        if (isLogin) {
        //            homeViewModel.getAllTaskByUidInServer();
        //            assert getArguments() != null;
        //            getArguments().remove("isLogin");
        //        } else {
        //            homeViewModel.getAllTaskByUidInDB();
        //        }
        homeViewModel.getAllTaskByUidInServer();
        tasksItemAdapter = new TasksItemAdapter((List<Task>)(new ArrayList<Task>()));
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, RecyclerView.VERTICAL);
        homeFragmentBinding.recyclerView.setLayoutManager(staggeredGridLayoutManager);
        homeFragmentBinding.recyclerView.setAdapter(tasksItemAdapter);
        homeViewModel.getReturnLiveData().observe(lifecycleOwner, new Observer<ReturnData<List<Task>>>() {
            @Override
            public void onChanged(ReturnData<List<Task>> returnData) {
                RCodeEnum rCodeEnum = RCodeEnum.returnRCodeEnumByCode(returnData.getCode());
                List<Task> tasks = returnData.getData();
                switch (rCodeEnum) {
                    case OK:{
                        Log.d("imageView", "onChanged: 成功");
                        break;
                    }
                    case DB_OK:{
                        if (tasks == null || tasks.size() == 0) {
                            Log.d("imageView", "onChanged: 本地数据库为空");
                            homeViewModel.getAllTaskByUidInServer();
                            break;
                        }
                        Log.d("imageView", "onChanged: 本地数据库不为空");
                        tasksItemAdapter.setTasks(tasks);
                        tasksItemAdapter.notifyDataSetChanged();
                        break;
                    }
                    case GET_TASKS_SERVER_OK:{
                        Log.d("imageView", "onChanged: 网络请求成功");
                        tasksItemAdapter.setTasks(tasks);
                        tasksItemAdapter.notifyDataSetChanged();
                        break;
                    }
                    case ERROR_AUTH:{
                        Log.d("imageView", "onChanged: token_error");
                    }
                    case ERROR:{
                        Toast.makeText(getContext(), "请检查您的网络链接",Toast.LENGTH_LONG).show();
                        Log.d("imageView", "onChanged: token_error");
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
        tasksItemAdapter.setItemLongOnClickListener(new TasksItemAdapter.ItemLongOnClickListener() {
            @Override
            public void itemLongOnClick() {
                tasksItemAdapter.setCheckBoxIsShow();
                homeFragmentBinding.itemLongClickEditWindow.setVisibility(View.VISIBLE);
            }
        });
        tasksItemAdapter.setItemOnClickListener(new TasksItemAdapter.ItemOnClickListener() {
            @Override
            public void itemOnClick(int position) {
                //将点击的task对象放到MainActivity中的map中
                ((MainActivity)requireActivity()).putDataInToMap("task", Objects.requireNonNull(((Objects.requireNonNull(homeViewModel.getReturnLiveData().getValue())).getData())).get(position));
                NavController navController = Navigation.findNavController(requireView());
                navController.navigate(R.id.action_homeFragment_to_addTaskFragment);
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
                homeViewModel.deleteTasksByUidInServer(tasksItemAdapter.deleteSelectedTask());
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
            case R.id.turnToClockFragmentBtn:{
                //跳转时钟界面
                controller.navigate(R.id.action_homeFragment_to_clockFragment);
                break;
            }
            case R.id.turnToBillFragmentBtn:{
                //跳转账单界面
                controller.navigate(R.id.action_homeFragment_to_billFragment);
                break;
            }
            case R.id.loginOutBtn: {
                MyApplication.getApplication().signOut();
                requireActivity().finish();
                Intent intent = new Intent(); //生成Intent对象
                intent.setClass(getActivity(), MainActivity.class);
                startActivity(intent);
            }
        }
    }
}
