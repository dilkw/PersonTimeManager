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

import com.bumptech.glide.Glide;
import com.demo.androidapp.MainActivity;
import com.demo.androidapp.MyApplication;
import com.demo.androidapp.R;
import com.demo.androidapp.databinding.HomeFragmentBinding;
import com.demo.androidapp.model.common.StateEnum;
import com.demo.androidapp.model.entity.Bill;
import com.demo.androidapp.model.entity.Task;
import com.demo.androidapp.model.common.RCodeEnum;
import com.demo.androidapp.model.common.ReturnData;
import com.demo.androidapp.model.entity.User;
import com.demo.androidapp.model.returnObject.ReturnListObject;
import com.demo.androidapp.util.DataSP;
import com.demo.androidapp.util.DateTimeUtil;
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
        boolean isLogin = false;
        isLogin = getArguments() != null && getArguments().getBoolean("isLogin");
        if(MyApplication.getApplication().getUser() == null){
            Log.d("imageView", "跳转登录页面: ");
            controller.navigate(R.id.action_homeFragment_to_loginFragment);
        }else {
            initData(this,isLogin);
            setListener();
        }
    }

    private void initData(LifecycleOwner lifecycleOwner,boolean isLogin) {
        Log.d("imageView",MyApplication.getApplication().getUser().getUid() + "++++++++++");
        User user = MyApplication.getApplication().getUser();
        homeFragmentBinding.userNameTextView.setText(user.getName());
        Glide.with(getContext()).load(user.getImg_url() + DateTimeUtil.getRandom()).into(homeFragmentBinding.drawerLayoutUserImage);
        tasksItemAdapter = new TasksItemAdapter((List<Task>)(new ArrayList<Task>()));
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, RecyclerView.VERTICAL);
        homeFragmentBinding.recyclerView.setLayoutManager(staggeredGridLayoutManager);
        homeFragmentBinding.recyclerView.setAdapter(tasksItemAdapter);
        homeViewModel.getAllTaskByUidInServer().observe(getViewLifecycleOwner(), new Observer<ReturnData<ReturnListObject<Task>>>() {
            @Override
            public void onChanged(ReturnData<ReturnListObject<Task>> returnListObjectReturnData) {
                RCodeEnum rCodeEnum = RCodeEnum.returnRCodeEnumByCode(returnListObjectReturnData.getCode());
                switch (rCodeEnum) {
                    case OK: {
                        Log.d("imageView", "onChanged: 成功");
                        taskList = returnListObjectReturnData.getData().getItems();
                        tasksItemAdapter.setTasks(taskList);
                        tasksItemAdapter.notifyDataSetChanged();
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
            public void itemOnClick(int position) {
                //将点击的task对象放到MainActivity中的map中
                Log.d("imageView", "itemOnClick: " + position);
                ((MainActivity)requireActivity()).putDataInToMap("task", taskList.get(position));
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
                MyApplication.getApplication().signOut();
                requireActivity().finish();
                Intent intent = new Intent(); //生成Intent对象
                intent.setClass(getActivity(), MainActivity.class);
                startActivity(intent);
                break;
            }
            default:{
                break;
            }
        }
    }
}
