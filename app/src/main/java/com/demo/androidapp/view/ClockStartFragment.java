package com.demo.androidapp.view;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.SearchView;
import android.widget.Toast;

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

import com.demo.androidapp.R;
import com.demo.androidapp.databinding.ClockFragmentBinding;
import com.demo.androidapp.databinding.ClockstartFragmentBinding;
import com.demo.androidapp.model.common.RCodeEnum;
import com.demo.androidapp.model.common.ReturnData;
import com.demo.androidapp.model.entity.Clock;
import com.demo.androidapp.view.myView.AddClockDialog;
import com.demo.androidapp.view.myView.adapter.ClockItemAdapter;
import com.demo.androidapp.viewmodel.ClockStartViewModel;
import com.demo.androidapp.viewmodel.ClockViewModel;

import java.util.ArrayList;
import java.util.List;

public class ClockStartFragment extends Fragment implements View.OnClickListener {

    private ClockStartViewModel clockStartViewModel;

    private ClockstartFragmentBinding clockstartFragmentBinding;

    private FragmentManager fragmentManager;

    private NavHostFragment navHostFragment;

    private NavController controller;

    private AppBarConfiguration appBarConfiguration;

    private ObjectAnimator objectAnimator;

    public static ClockStartFragment newInstance() {
        return new ClockStartFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        clockstartFragmentBinding = DataBindingUtil.inflate(inflater,R.layout.clockstart_fragment,container,false);
        clockstartFragmentBinding.setLifecycleOwner(this);
        clockStartViewModel = new ViewModelProvider(this).get(ClockStartViewModel.class);
        clockstartFragmentBinding.setClockStartViewModel(clockStartViewModel);
        fragmentManager = requireActivity().getSupportFragmentManager();
        navHostFragment = (NavHostFragment)fragmentManager.findFragmentById(R.id.fragment);
        assert navHostFragment != null;
        controller = navHostFragment.getNavController();
        appBarConfiguration = new AppBarConfiguration.Builder(controller.getGraph()).build();
        NavigationUI.setupWithNavController(clockstartFragmentBinding.clockStartFragmentToolBar,controller,appBarConfiguration);
        return clockstartFragmentBinding.getRoot();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        long clockId = 0;
        if (getArguments() != null) {
            clockId = getArguments().getLong("clockId");
        }
        long finalClockId = clockId;
        clockStartViewModel.getClockInfoInServer(clockId).observe(getViewLifecycleOwner(), new Observer<ReturnData<Clock>>() {
            @Override
            public void onChanged(ReturnData<Clock> clockReturnData) {
                if (clockReturnData.getCode() == RCodeEnum.OK.getCode()) {
                    clockStartViewModel.clockLiveData.setValue(clockReturnData.getData());
                    initView();
                }else {
                    clockStartViewModel.getClockInfoInDB(finalClockId).observe(getViewLifecycleOwner(), new Observer<Clock>() {
                        @Override
                        public void onChanged(Clock clock) {
                            if (clock != null) {
                                clockStartViewModel.clockLiveData.setValue(clock);
                            }
                        }
                    });
                }
            }
        });
        setListener();
    }

    private void initView() {
        objectAnimator = ObjectAnimator
                .ofFloat(clockstartFragmentBinding.clockCountDown,"progress",60,0);
        objectAnimator.setDuration(1000 * 60 * clockStartViewModel.clockLiveData.getValue().getClock_minute());
        //设置动画执行次数（ValueAnimator.INFINITE为无限）
        objectAnimator.setRepeatCount(ValueAnimator.RESTART);
        //当动画执行次数大于零或是无限（ValueAnimator.INFINITE）时setRepeatMode才有效
        objectAnimator.setRepeatMode(ValueAnimator.RESTART);
        objectAnimator.setInterpolator(new LinearInterpolator());
        objectAnimator.start();
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
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
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
        clockstartFragmentBinding.clockStartFragmentStartBtn.setOnClickListener(this);
        clockstartFragmentBinding.clockStartFragmentStopBtn.setOnClickListener(this);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.clockStartFragmentStartBtn: {
                Log.d("imageView", "onClick: 开始按钮点击");
                objectAnimator.resume();
                break;
            }
            case R.id.clockStartFragmentStopBtn: {
                Log.d("imageView", "onClick: 暂停按钮点击");
                objectAnimator.pause();
                break;
            }
            default:{
                break;
            }
        }
    }
}