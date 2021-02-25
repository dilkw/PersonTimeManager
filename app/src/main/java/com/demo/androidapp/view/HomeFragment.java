package com.demo.androidapp.view;

import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.app.FragmentManager;
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

import com.demo.androidapp.MainActivity;
import com.demo.androidapp.MyApplication;
import com.demo.androidapp.R;
import com.demo.androidapp.databinding.HomeFragmentBinding;
import com.demo.androidapp.model.Auth;
import com.demo.androidapp.model.entity.Task;
import com.demo.androidapp.model.common.RCodeEnum;
import com.demo.androidapp.model.common.ReturnData;
import com.demo.androidapp.model.returnObject.ReturnListObject;
import com.demo.androidapp.util.DataSP;
import com.demo.androidapp.view.myView.adapter.TasksItemAdapter;
import com.demo.androidapp.viewmodel.HomeViewModel;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;

    private HomeFragmentBinding homeFragmentBinding;

    private List<Task> taskList = new ArrayList<>();

    TasksItemAdapter tasksItemAdapter;

    NavHostFragment navHostFragment;

    NavController controller;

    AppBarConfiguration appBarConfiguration;

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
        DrawerLayout drawerLayout = view.findViewById(R.id.drawer_layout);
        NavigationView navView = view.findViewById(R.id.nav_view);
        navHostFragment = (NavHostFragment)getActivity().getSupportFragmentManager().findFragmentById(R.id.fragment);
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

        homeFragmentBinding.loginOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyApplication.getApplication().signOut();
                Log.d("imageView", "onClick:userName" + MyApplication.getApplication().getUSER_NAME());
                getActivity().finish();
                Intent intent = new Intent(); //生成Intent对象
                intent.setClass(getActivity(), MainActivity.class);
                startActivity(intent);
            }
        });

        homeFragmentBinding.myFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("imageView", "onClick: 点击事件");
                NavController navController = Navigation.findNavController(homeFragmentBinding.myFloatingActionButton);
                navController.navigate(R.id.action_homeFragment_to_addTaskFragment);
            }
        });
        Log.d("imageView","pppppppp" + MyApplication.getApplication().getCOOKIE());
        if((MyApplication.getApplication().getUSER_NAME()).equals("userName")){
            Log.d("imageView", "onCreateView2: ");
            controller.navigate(R.id.action_homeFragment_to_loginFragment);
        }else {
            initData(this);
        }
    }

    private void initData(LifecycleOwner lifecycleOwner) {
        Log.d("imageView",MyApplication.getApplication().getUID() + "++++++++++");
        homeViewModel.getAllTaskByUid(MyApplication.getApplication().getUID());
        homeViewModel.getReturnLiveData().observe(lifecycleOwner, new Observer<ReturnData>() {
            @Override
            public void onChanged(ReturnData returnData) {
                if (returnData == null) {
                    Log.d("imageView", "onChanged:returnData为空" );
                    return;
                }
                Log.d("imageView", "onChanged:returnData.toString" + returnData.toString());
                if (returnData.getCode() == RCodeEnum.OK.getCode()) {
                    Log.d("imageView", "onChanged: 获取任务清单成功==============");
                    homeViewModel.homeLiveData.setValue(((ReturnListObject<Task>)returnData.getData()).getItems());
                }else {
                    Log.d("imageView", "onChanged: 获取任务清单失败==============");
                }
            }
        });
        TasksItemAdapter tasksItemAdapter = new TasksItemAdapter(homeViewModel.homeLiveData.getValue());
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, RecyclerView.VERTICAL);
        homeFragmentBinding.recyclerView.setLayoutManager(staggeredGridLayoutManager);
        homeFragmentBinding.recyclerView.setAdapter(tasksItemAdapter);

        homeViewModel.homeLiveData.observe(lifecycleOwner, new Observer<List<Task>>() {
            @Override
            public void onChanged(List<Task> tasks) {
                Log.d("imageView", "onChanged: 数据变化");
                if (tasks.size() == 0) return;
                tasksItemAdapter.setTasks(tasks);
                tasksItemAdapter.notifyDataSetChanged();
            }
        });

    }
}
