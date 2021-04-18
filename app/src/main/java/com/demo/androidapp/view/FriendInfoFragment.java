package com.demo.androidapp.view;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
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

import com.bumptech.glide.Glide;
import com.demo.androidapp.MainActivity;
import com.demo.androidapp.R;
import com.demo.androidapp.databinding.FriendinfoFragmentBinding;
import com.demo.androidapp.model.FindFriendInfo;
import com.demo.androidapp.model.common.RCodeEnum;
import com.demo.androidapp.model.common.ReturnData;
import com.demo.androidapp.model.entity.Friend;
import com.demo.androidapp.util.DateTimeUtil;
import com.demo.androidapp.viewmodel.FriendInfoViewModel;

import java.util.Objects;

public class FriendInfoFragment extends Fragment implements View.OnClickListener {

    private FriendInfoViewModel friendInfoViewModel;

    private FriendinfoFragmentBinding friendinfoFragmentBinding;

    private FragmentManager fragmentManager;

    private NavHostFragment navHostFragment;

    private NavController controller;

    private LiveData<Boolean> isMe;

    private AppBarConfiguration appBarConfiguration;

    private long fid;

    private String fuid = "";

    public static FriendInfoFragment newInstance() {
        return new FriendInfoFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        friendinfoFragmentBinding = DataBindingUtil.inflate(inflater,R.layout.friendinfo_fragment,container,false);
        friendinfoFragmentBinding.setLifecycleOwner(this);
        fragmentManager = requireActivity().getSupportFragmentManager();
        navHostFragment = (NavHostFragment)fragmentManager.findFragmentById(R.id.fragment);
        assert navHostFragment != null;
        controller = navHostFragment.getNavController();
        appBarConfiguration = new AppBarConfiguration.Builder(controller.getGraph()).build();
        NavigationUI.setupWithNavController(friendinfoFragmentBinding.friendInfoFragmentToolBar,controller,appBarConfiguration);
        return friendinfoFragmentBinding.getRoot();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        friendInfoViewModel = new ViewModelProvider(this).get(FriendInfoViewModel.class);
        friendinfoFragmentBinding.setFriendInfoViewModel(friendInfoViewModel);
        if (getArguments() != null) {
            fid = getArguments().getLong("fid");
            fuid = getArguments().getString("fuid");
            init1();
        }else {
            init2();
        }
        setListener();
    }

    private void init1() {
        friendInfoViewModel.getFriendInfoByUid(fuid).observe(getViewLifecycleOwner(), new Observer<ReturnData<FindFriendInfo>>() {
            @Override
            public void onChanged(ReturnData<FindFriendInfo> findFriendInfoReturnData) {
                if (findFriendInfoReturnData.getCode() == RCodeEnum.OK.getCode()) {
                    friendInfoViewModel.findFriendInfoMutableLiveData.setValue(findFriendInfoReturnData.getData());
                    Glide.with(requireContext()).load(findFriendInfoReturnData.getData().getUser().getImg_url() + DateTimeUtil.getRandom()).into(friendinfoFragmentBinding.friendInfoImageView);
                }
                Log.d("imageView", "onChanged: " + findFriendInfoReturnData.getData().getUser().toString());
            }
        });
    }

    private void init2() {
        FindFriendInfo findFriendInfo = (FindFriendInfo) ((MainActivity)requireActivity()).getDataFromMapByKey("friendInfo");
        if (findFriendInfo == null) return;
        friendInfoViewModel.findFriendInfoMutableLiveData.setValue(findFriendInfo);
    }

    public void setListener() {
        friendinfoFragmentBinding.deleteOrAddFriendButton.setOnClickListener(this);
        friendinfoFragmentBinding.friendInfoImageView.setOnClickListener(this);
        friendinfoFragmentBinding.friendInfoFragmentSendMsgBtn.setOnClickListener(this);
    }

    @SuppressLint({"NonConstantResourceId", "ResourceAsColor"})
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.friendInfoImageView: {
                Log.d("imageView", "onClick: 用户头像点击");
                break;
            }
            case R.id.deleteOrAddFriendButton: {
                Log.d("imageView", "onClick: 添加或删除好友");
                if (friendInfoViewModel.getUserInfoLiveData().getValue().getIsFriend()) {
                    deleteFriend();
                }else {
                    addFriend();
                }
                break;
            }
            case R.id.friendInfoFragmentSendMsgBtn: {
                Log.d("imageView", "onClick: 发消息");
                String fName = friendInfoViewModel.findFriendInfoMutableLiveData.getValue().getUser().getName();
                String fImgUrl = friendInfoViewModel.findFriendInfoMutableLiveData.getValue().getUser().getImg_url();
                Bundle bundle = new Bundle();
                bundle.putString("fName",fName);
                bundle.putString("fImgUrl",fImgUrl);
                controller.navigate(R.id.action_friendInfoFragment_to_chatFragment,bundle);
                onDestroy();
                break;
            }
            default:{
                Log.d("imageView", "onClick: ");
                break;
            }
        }
    }

    //删除好友
    private void deleteFriend() {
        friendInfoViewModel.deleteFriend().observe(getViewLifecycleOwner(), new Observer<ReturnData<Object>>() {
            @Override
            public void onChanged(ReturnData<Object> objectReturnData) {
                if (objectReturnData.getCode() == RCodeEnum.OK.getCode()) {
                    Toast.makeText(getContext(),"删除好友成功",Toast.LENGTH_SHORT).show();
                    controller.navigateUp();
                }else {
                    Toast.makeText(getContext(),"删除好友失败" + objectReturnData.getMsg(),Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    //添加好友
    private void addFriend() {
        friendInfoViewModel.addFriend().observe(getViewLifecycleOwner(), new Observer<ReturnData<Friend>>() {
            @Override
            public void onChanged(ReturnData<Friend> friendReturnData) {
                if (friendReturnData.getCode() == RCodeEnum.OK.getCode()) {
                    Toast.makeText(getContext(),"添加好友成功",Toast.LENGTH_SHORT).show();
                    controller.navigateUp();
                }else {
                    Toast.makeText(getContext(),"添加好友失败" + friendReturnData.getMsg(),Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}