package com.demo.androidapp.view;

import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.annotation.SuppressLint;
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
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.Toast;

import com.demo.androidapp.R;
import com.demo.androidapp.databinding.ClockFragmentBinding;
import com.demo.androidapp.model.common.RCodeEnum;
import com.demo.androidapp.model.common.ReturnData;
import com.demo.androidapp.model.entity.Bill;
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
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        clockFragmentBinding = DataBindingUtil.inflate(inflater,R.layout.clock_fragment,container,false);
        clockViewModel = new ViewModelProvider(this).get(ClockViewModel.class);
        View view = clockFragmentBinding.getRoot();
        Log.d("imageView", "onCreateView: " + view.getClass().getSimpleName());
        fragmentManager = requireActivity().getSupportFragmentManager();
        navHostFragment = (NavHostFragment)fragmentManager.findFragmentById(R.id.fragment);
        assert navHostFragment != null;
        controller = navHostFragment.getNavController();
        appBarConfiguration = new AppBarConfiguration.Builder(controller.getGraph()).build();
        NavigationUI.setupWithNavController(clockFragmentBinding.clockFragmentToolBar,controller,appBarConfiguration);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("imageView", "view onClick: ");
            }
        });
        return view;
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
                }
            }
        });
        //getAllClocksLiveDataInDB();
        setListener();
    }

    private void getAllClocksLiveDataInDB() {
        clocksLiveData = clockViewModel.getAllClocksLiveDataInDB();
        clocksLiveData.observe(getViewLifecycleOwner(), new Observer<List<Clock>>() {
            @Override
            public void onChanged(List<Clock> clocks) {
                if (clocks == null) {
                    return;
                }
                clockItemAdapter.setClocks(clocks);
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.clockfragment_bar,menu);
        SearchView searchView = (SearchView) menu.findItem(R.id.clockSearch).getActionView();
        searchView.setMaxWidth(500);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Log.d("imageView", "onQueryTextChange:");
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Log.d("imageView", "onQueryTextChange:");
                clocksLiveData.removeObservers(getViewLifecycleOwner());
                clocksLiveData = clockViewModel.getClocksLiveDataByPattern(newText.trim());
                clocksLiveData.observe(getViewLifecycleOwner(), new Observer<List<Clock>>() {
                    @Override
                    public void onChanged(List<Clock> clocks) {
                        Log.d("imageView", "onChanged: 查找返回" + clocks.size());
                        clockItemAdapter.setClocks(clocks);
                    }
                });
                return true;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    public void setListener() {
        Log.d("imageView", "setListener: ");
        clockFragmentBinding.clockCancelImageButton.setOnClickListener(this);
        clockFragmentBinding.clockDeleteImageButton.setOnClickListener(this);
        clockFragmentBinding.clockAllSelectImageButton.setOnClickListener(this);
        clockFragmentBinding.clockMyFloatingActionButton.setOnClickListener(this);
        clockItemAdapter.setItemLongOnClickListener(new ClockItemAdapter.ItemLongOnClickListener() {
            @Override
            public void itemLongOnClick() {
                Log.d("imageView", "setListener: 长按");
                clockItemAdapter.setCheckBoxIsShow();
                clockFragmentBinding.clockItemLongClickEditWindow.setVisibility(View.VISIBLE);
            }
        });
        clockItemAdapter.setItemOnClickListener(new ClockItemAdapter.ItemOnClickListener() {
            @Override
            public void itemOnClick(int position) {
                Log.d("imageView", "setListener: " + clockViewModel.getReturnLiveData().getValue().getData().get(position).toString());
                AddClockDialog addClockDialog = new AddClockDialog(clockViewModel.getReturnLiveData().getValue().getData().get(position));
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

        //导航栏Menu菜单监听事件
        clockFragmentBinding.clockFragmentToolBar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.clockSearch : {
                        break;
                    }
                    case R.id.clockAdd:{
                        AddClockDialog addClockDialog = new AddClockDialog();
                        addClockDialog.setEnterClicked(new AddClockDialog.EnterListener() {
                            @RequiresApi(api = Build.VERSION_CODES.O)
                            @Override
                            public void enterBtnOnClicked() {
                                clockViewModel.addClocksInDB(addClockDialog.getClock());
                                clocksLiveData = clockViewModel.getAllClocksLiveDataInDB();
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

    private void addClock(Clock clock) {
        clockViewModel.addClockToServer(clock).observe(getViewLifecycleOwner(), new Observer<ReturnData<Clock>>() {
            @Override
            public void onChanged(ReturnData<Clock> clockReturnData) {
                if (clockReturnData.getCode() == RCodeEnum.OK.getCode()) {
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

    private void upDateClock(Clock clock,int position) {
        clockViewModel.upDateClocksInServer(clock).observe(getViewLifecycleOwner(), new Observer<ReturnData<Object>>() {
            @Override
            public void onChanged(ReturnData<Object> clockReturnData) {
                if (clockReturnData.getCode() == RCodeEnum.OK.getCode()) {
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
}