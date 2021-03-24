package com.demo.androidapp.view;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.bumptech.glide.Glide;
import com.demo.androidapp.R;
import com.demo.androidapp.databinding.FriendFragmentBinding;
import com.demo.androidapp.databinding.FriendinfoFragmentBinding;
import com.demo.androidapp.databinding.UserinfoFragmentBinding;
import com.demo.androidapp.model.FriendListItem;
import com.demo.androidapp.model.common.RCodeEnum;
import com.demo.androidapp.model.common.ReturnData;
import com.demo.androidapp.model.entity.Friend;
import com.demo.androidapp.model.entity.User;
import com.demo.androidapp.util.DateTimeUtil;
import com.demo.androidapp.view.myView.ResetEmailDialog;
import com.demo.androidapp.viewmodel.FriendInfoViewModel;
import com.demo.androidapp.viewmodel.FriendViewModel;
import com.demo.androidapp.viewmodel.UserInfoViewModel;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

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
        if (getArguments() != null) {
            fid = getArguments().getLong("fid");
            fuid = getArguments().getString("fuid");
        }
        friendInfoViewModel = new ViewModelProvider(this).get(FriendInfoViewModel.class);
        friendinfoFragmentBinding.setFriendInfoViewModel(friendInfoViewModel);
        friendInfoViewModel.getFriendInfoByUid(fuid).observe(getViewLifecycleOwner(), new Observer<ReturnData<FriendListItem>>() {
            @Override
            public void onChanged(ReturnData<FriendListItem> friendListItemReturnData) {
                if (friendListItemReturnData.getCode() == RCodeEnum.OK.getCode()) {
                    Glide.with(getContext()).load(friendListItemReturnData.getData().getUser().getImg_url() + DateTimeUtil.getRandom()).into(friendinfoFragmentBinding.friendInfoImageView);
                }
                Log.d("imageView", "onChanged: " + friendListItemReturnData.getData().getUser().toString());
            }
        });
        setListener();
    }

    public void setListener() {
        friendinfoFragmentBinding.deleteOrAddFriendButton.setOnClickListener(this);
        friendinfoFragmentBinding.friendInfoImageView.setOnClickListener(this);
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
                if (friendInfoViewModel.getUserInfoLiveData().getValue().getData().getIsFriend()) {
                    deleteFriend();
                }else {
                    addFriend();
                }
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
        friendInfoViewModel.deleteFriend(fid).observe(getViewLifecycleOwner(), new Observer<ReturnData<Object>>() {
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
                }else {
                    Toast.makeText(getContext(),"添加好友失败" + friendReturnData.getMsg(),Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}