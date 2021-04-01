package com.demo.androidapp.view;

import android.animation.Animator;
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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.demo.androidapp.R;
import com.demo.androidapp.databinding.ClockstartFragmentBinding;
import com.demo.androidapp.model.common.RCodeEnum;
import com.demo.androidapp.model.common.ReturnData;
import com.demo.androidapp.model.entity.Clock;
import com.demo.androidapp.viewmodel.ClockStartViewModel;

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
                Log.d("imageView", "onChanged: " + finalClockId +  clockReturnData.getData().toString());
                if (clockReturnData.getCode() == RCodeEnum.OK.getCode()) {
                    clockStartViewModel.clockLiveData.setValue(clockReturnData.getData());
                    initView();
                    setListener();
                }else {
                    clockStartViewModel.getClockInfoInDB(finalClockId).observe(getViewLifecycleOwner(), new Observer<Clock>() {
                        @Override
                        public void onChanged(Clock clock) {
                            if (clock != null) {
                                clockStartViewModel.clockLiveData.setValue(clock);
                                initView();
                                setListener();
                            }
                        }
                    });
                }
            }
        });
    }

    private void initView() {
        if (clockStartViewModel.clockLiveData.getValue() == null) {
            Log.d("imageView", "initView:null");
        }
        long progress = clockStartViewModel.clockLiveData.getValue().getClock_minute() * 60;
        clockstartFragmentBinding.clockCountDown.setMultiple(360 / (float)progress);
        objectAnimator = ObjectAnimator
                .ofFloat(clockstartFragmentBinding.clockCountDown,"progress",progress,0);
        objectAnimator.setDuration(1000 * progress);
        //设置动画执行次数（ValueAnimator.INFINITE为无限）
        objectAnimator.setRepeatCount(ValueAnimator.RESTART);
        //当动画执行次数大于零或是无限（ValueAnimator.INFINITE）时setRepeatMode才有效
        objectAnimator.setRepeatMode(ValueAnimator.RESTART);
        objectAnimator.setInterpolator(new LinearInterpolator());
        objectAnimator.start();
    }

    public void setListener() {
        Log.d("imageView", "setListener: ");
        clockstartFragmentBinding.clockStartFragmentStartBtn.setOnClickListener(this);
        clockstartFragmentBinding.clockStartFragmentStopBtn.setOnClickListener(this);

        objectAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                updateClockIsComplete();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
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

    //完成时更新时钟状态（改为完成状态）
    private void updateClockIsComplete() {
        Clock clock = clockStartViewModel.clockLiveData.getValue();
        clock.setState(true);
        clockStartViewModel.upDateClockInfoInServer(clock).observe(getViewLifecycleOwner(), new Observer<ReturnData<Object>>() {
            @Override
            public void onChanged(ReturnData<Object> objectReturnData) {
                if (objectReturnData.getCode() == RCodeEnum.OK.getCode()) {

                }
            }
        });
        controller.navigateUp();
    }
}