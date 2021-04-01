package com.demo.androidapp.view;

import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.Toast;

import com.demo.androidapp.MainActivity;
import com.demo.androidapp.R;
import com.demo.androidapp.databinding.ClockFragmentBinding;
import com.demo.androidapp.model.common.RCodeEnum;
import com.demo.androidapp.model.common.ReturnData;
import com.demo.androidapp.model.entity.Clock;
import com.demo.androidapp.view.myView.AddClockDialog;
import com.demo.androidapp.view.myView.adapter.ClockItemAdapter;
import com.demo.androidapp.viewmodel.ClockViewModel;

import java.util.ArrayList;
import java.util.List;

public class ClockFragment extends Fragment implements View.OnClickListener {

    private ClockViewModel clockViewModel;

    private ClockFragmentBinding clockFragmentBinding;

    private FragmentManager fragmentManager;

    private NavHostFragment navHostFragment;

    private NavController controller;

    private ClockItemAdapter clockItemAdapter;

    private LiveData<List<Clock>> clocksLiveData;

    private AppBarConfiguration appBarConfiguration;

    public static ClockFragment newInstance() {
        return new ClockFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        clockFragmentBinding = DataBindingUtil.inflate(inflater,R.layout.clock_fragment,container,false);
        clockViewModel = new ViewModelProvider(this).get(ClockViewModel.class);
        fragmentManager = requireActivity().getSupportFragmentManager();
        navHostFragment = (NavHostFragment)fragmentManager.findFragmentById(R.id.fragment);
        assert navHostFragment != null;
        controller = navHostFragment.getNavController();
        appBarConfiguration = new AppBarConfiguration.Builder(controller.getGraph()).build();
        NavigationUI.setupWithNavController(clockFragmentBinding.clockFragmentToolBar,controller,appBarConfiguration);
        return clockFragmentBinding.getRoot();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        clockItemAdapter = new ClockItemAdapter((List<Clock>)(new ArrayList<Clock>()));
        clockFragmentBinding.clockRecyclerView.setLayoutManager(new LinearLayoutManager(requireActivity()));
        clockFragmentBinding.clockRecyclerView.setAdapter(clockItemAdapter);
        clockViewModel.getAllClocksLiveDataInServer();
        clockViewModel.getReturnLiveData().observe(getViewLifecycleOwner(), new Observer<ReturnData<List<Clock>>>() {
            @Override
            public void onChanged(ReturnData<List<Clock>> listReturnData) {
                if (listReturnData.getCode() == RCodeEnum.OK.getCode()) {
                    clockItemAdapter.setClocks(listReturnData.getData());
                    clockItemAdapter.notifyDataSetChanged();
                    clockViewModel.deleteAllClocksAndAddInDB(listReturnData.getData());
                }
            }
        });
        setListener();
    }

    public void setListener() {
        Log.d("imageView", "setListener: ");
        clockFragmentBinding.clockCancelImageButton.setOnClickListener(this);
        clockFragmentBinding.clockDeleteImageButton.setOnClickListener(this);
        clockFragmentBinding.clockAllSelectImageButton.setOnClickListener(this);
        clockFragmentBinding.clockMyFloatingActionButton.setOnClickListener(this);
        //item长按时间
        clockItemAdapter.setItemLongOnClickListener(new ClockItemAdapter.ItemLongOnClickListener() {
            @Override
            public void itemLongOnClick() {
                Log.d("imageView", "setListener: 长按");
                clockItemAdapter.setCheckBoxIsShow();
                clockFragmentBinding.clockItemLongClickEditWindow.setVisibility(View.VISIBLE);
            }
        });
        //item点击时间
        clockItemAdapter.setItemOnClickListener(new ClockItemAdapter.ItemOnClickListener() {
            @Override
            public void itemOnClick(Clock clock, int position) {
                Log.d("imageView", "setListener: " + clock.toString());
                AddClockDialog addClockDialog = new AddClockDialog(clock);
                addClockDialog.setEnterClicked(new AddClockDialog.EnterListener() {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void enterBtnOnClicked() {
                        Clock clock = addClockDialog.getClock();
                        upDateClock(clock,position);
                    }
                });
                addClockDialog.show(fragmentManager,"editClockDialog");
            }
        });
        //item中开始按钮点击事件
        clockItemAdapter.setItemStartOnClickListener(new ClockItemAdapter.ItemStartOnClickListener() {
            @Override
            public void itemStartOnClick(int position, long clockId) {
                Bundle bundle = new Bundle();
                bundle.putLong("clockId",clockId);
                controller.navigate(R.id.action_clockFragment_to_clockStartFragment,bundle);
            }
        });
        //导航栏Menu菜单监听事件

        //导航栏Menu菜单，搜索框监听事件
        SearchView searchView = (SearchView) (clockFragmentBinding.clockFragmentToolBar.getMenu().findItem(R.id.clockSearch).getActionView());
        //导航栏查询监听
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Log.d("imageView", "onQueryTextChange:");
                if (clocksLiveData != null && clocksLiveData.hasObservers()) {
                    clocksLiveData.removeObservers(getViewLifecycleOwner());
                }
                clocksLiveData = clockViewModel.getClocksLiveDataByPatternInDB(newText);
                clocksLiveData.observe(getViewLifecycleOwner(), new Observer<List<Clock>>() {
                    @Override
                    public void onChanged(List<Clock> clocks) {
                        if (clocks == null || clocks.size() == 0) {
                            return;
                        }
                        Log.d("imageView", clocks.get(0).toString());
                        clockItemAdapter.setClocks(clocks);
                        clockItemAdapter.notifyDataSetChanged();
                    }
                });
                return true;
            }
        });
        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                if (clocksLiveData != null && clocksLiveData.hasObservers()) {
                    clocksLiveData.removeObservers(getViewLifecycleOwner());
                }
                clocksLiveData = clockViewModel.getAllClocksLiveDataInDB();
                clocksLiveData.observe(getViewLifecycleOwner(), new Observer<List<Clock>>() {
                    @Override
                    public void onChanged(List<Clock> clocks) {
                        clockItemAdapter.setClocks(clocks);
                        clockItemAdapter.notifyDataSetChanged();
                    }
                });
                return false;
            }
        });

        //对menu菜单设置监听
        clockFragmentBinding.clockFragmentToolBar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.clockSearch : {
                        Log.d("imageView", "clockSearch: 搜索");
                        break;
                    }
                    case R.id.clockAdd:{
                        Log.d("imageView", "clockAdd: 添加");
                        AddClockDialog addClockDialog = new AddClockDialog();
                        addClockDialog.setEnterClicked(new AddClockDialog.EnterListener() {
                            @RequiresApi(api = Build.VERSION_CODES.O)
                            @Override
                            public void enterBtnOnClicked() {
                                addClock(addClockDialog.getClock());
                            }
                        });
                        addClockDialog.show(fragmentManager,"addClockDialogByMenu");
                        break;
                    }
                }
                return false;
            }
        });
    }

    @SuppressLint({"NonConstantResourceId", "ResourceAsColor"})
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.clockCancelImageButton: {
                Log.d("imageView", "onClick: 取消按钮");
                clockItemAdapter.cancelTask();
                clockFragmentBinding.clockItemLongClickEditWindow.setVisibility(View.GONE);
                break;
            }
            case R.id.clockDeleteImageButton: {
                Log.d("imageView", "onClick: 删除按钮");
                clockViewModel.deleteClocksByClockIdsInServer(clockItemAdapter.getEditModelSelectedTasks()).observe(getViewLifecycleOwner(), new Observer<ReturnData<Object>>() {
                    @Override
                    public void onChanged(ReturnData<Object> objectReturnData) {
                        if (objectReturnData.getCode() == RCodeEnum.OK.getCode()) {
                            clockItemAdapter.deleteSelectedClocks();
                            Toast.makeText(getContext(),"删除成功",Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(getContext(),objectReturnData.getMsg(),Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                break;
            }
            case R.id.clockAllSelectImageButton: {
                Log.d("imageView", "onClick: 全选按钮");
                clockFragmentBinding.clockAllSelectImageButton.setBackgroundColor(R.color.colorTextTrue);
                clockItemAdapter.selectedAllClocks();
                break;
            }
            case R.id.clockMyFloatingActionButton: {
                Log.d("imageView", "onClick: 添加按钮");
                AddClockDialog addClockDialog = new AddClockDialog();
                addClockDialog.setEnterClicked(new AddClockDialog.EnterListener() {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void enterBtnOnClicked() {
                        Clock clock = addClockDialog.getClock();
                        Log.d("imageView", "enterBtnOnClicked: " + clock.toString());
                        addClock(clock);
                    }
                });
                addClockDialog.show(fragmentManager,"addClockDialogByFloatingActionButton");
                break;
            }
            default:{
                Log.d("imageView", "onClick: ");
                break;
            }
        }
    }

    //添加时钟
    private void addClock(Clock clock) {
        clockViewModel.addClockToServer(clock).observe(getViewLifecycleOwner(), new Observer<ReturnData<Clock>>() {
            @Override
            public void onChanged(ReturnData<Clock> clockReturnData) {
                if (clockReturnData.getCode() == RCodeEnum.OK.getCode()) {
                    if (clock.isAlert()) {
                        setAlertClockInOS(clock.getAlert_time(),clockReturnData.getData().getId(),clock.getTask());
                    }
                    Toast.makeText(getContext(),"添加时钟成功",Toast.LENGTH_SHORT).show();
                    Log.d("imageView", "onChanged: " + clockReturnData.getData().toString());
                    clockItemAdapter.addClock(clockReturnData.getData());
                    clockViewModel.addClocksInDB(clockReturnData.getData());
                }else {
                    Toast.makeText(getContext(),"添加时钟失败",Toast.LENGTH_SHORT).show();
                    Log.d("imageView",clockReturnData.getCode() + clockReturnData.getMsg());
                }
            }
        });
    }

    //更新时钟
    private void upDateClock(Clock clock,int position) {
        clockViewModel.upDateClocksInServer(clock).observe(getViewLifecycleOwner(), new Observer<ReturnData<Object>>() {
            @Override
            public void onChanged(ReturnData<Object> clockReturnData) {
                if (clockReturnData.getCode() == RCodeEnum.OK.getCode()) {
                    if (clock.isAlert()) {
                        setAlertClockInOS(clock.getAlert_time(),clock.getId(),clock.getTask());
                    }
                    Toast.makeText(getContext(),"更新时钟成功",Toast.LENGTH_SHORT).show();
                    clockItemAdapter.notifyItemChanged(position,clock);
                    clockViewModel.upDateClocksInDB(clock);
                }else {
                    Toast.makeText(getContext(),"更新时钟失败",Toast.LENGTH_SHORT).show();
                    Log.d("imageView",clockReturnData.getCode() + clockReturnData.getMsg());
                }
            }
        });
    }

    private void setAlertClockInOS(long alertTime,long clockId,String clockText) {
        Log.d("imageView", "setAlertClockInOS: " + alertTime);
        //获得AlarmManager实例对象
        AlarmManager alarmManager = (AlarmManager) requireActivity().getSystemService(Context.ALARM_SERVICE);
        //自定义广播
        Intent intent = new Intent("AlarmClock");
        intent.putExtra("name","clock");
        intent.putExtra("id",clockId);
        intent.putExtra("text",clockText);
        intent.setPackage("com.demo.androidapp");
        //PendingIntent发送广播意图
        PendingIntent pendingIntent = PendingIntent.getBroadcast(requireActivity(), (int)clockId, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        //一次性定时器
        alarmManager.set(AlarmManager.RTC_WAKEUP,alertTime * 1000,pendingIntent);
    }
}