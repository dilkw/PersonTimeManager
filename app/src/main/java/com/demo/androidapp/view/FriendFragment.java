package com.demo.androidapp.view;

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
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.demo.androidapp.R;
import com.demo.androidapp.databinding.FriendFragmentBinding;
import com.demo.androidapp.model.common.RCodeEnum;
import com.demo.androidapp.model.common.ReturnData;
import com.demo.androidapp.model.entity.Bill;
import com.demo.androidapp.model.entity.Friend;
import com.demo.androidapp.model.entity.Task;
import com.demo.androidapp.model.returnObject.ReturnListObject;
import com.demo.androidapp.view.myView.AddBillDialog;
import com.demo.androidapp.view.myView.AddFriendDialog;
import com.demo.androidapp.view.myView.adapter.BillItemAdapter;
import com.demo.androidapp.view.myView.adapter.FriendItemAdapter;
import com.demo.androidapp.viewmodel.FriendViewModel;

import java.util.ArrayList;
import java.util.List;

public class FriendFragment extends Fragment implements View.OnClickListener {

    private FriendViewModel friendViewModel;

    private FriendFragmentBinding friendFragmentBinding;

    private FragmentManager fragmentManager;

    private NavHostFragment navHostFragment;

    private NavController controller;

    private FriendItemAdapter friendItemAdapter;

    private LiveData<List<Friend>> friendsLiveData;

    private AppBarConfiguration appBarConfiguration;

    public static FriendFragment newInstance() {
        return new FriendFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        friendFragmentBinding = DataBindingUtil.inflate(inflater,R.layout.friend_fragment,container,false);
        friendViewModel = new ViewModelProvider(this).get(FriendViewModel.class);
        fragmentManager = requireActivity().getSupportFragmentManager();
        navHostFragment = (NavHostFragment)fragmentManager.findFragmentById(R.id.fragment);
        assert navHostFragment != null;
        controller = navHostFragment.getNavController();
        appBarConfiguration = new AppBarConfiguration.Builder(controller.getGraph()).build();
        NavigationUI.setupWithNavController(friendFragmentBinding.friendFragmentToolBar,controller,appBarConfiguration);
        return friendFragmentBinding.getRoot();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        friendItemAdapter = new FriendItemAdapter((List<Friend>)(new ArrayList<Friend>()));
        friendFragmentBinding.friendRecyclerView.setLayoutManager(new LinearLayoutManager(requireActivity()));
        friendFragmentBinding.friendRecyclerView.setAdapter(friendItemAdapter);
        friendViewModel.getAllFriendsInServer();
        friendViewModel.getAllFriendsLiveDataInServer().observe(getViewLifecycleOwner(), new Observer<ReturnData<ReturnListObject<Friend>>>() {
            @SuppressLint("ShowToast")
            @Override
            public void onChanged(ReturnData<ReturnListObject<Friend>> listReturnData) {
                RCodeEnum rCodeEnum = RCodeEnum.returnRCodeEnumByCode(listReturnData.getCode());
                if (rCodeEnum == RCodeEnum.OK) {
                    friendItemAdapter.setFriends(listReturnData.getData().getItems());
                }else {
                    Toast.makeText(getContext(),rCodeEnum.getMessage(),Toast.LENGTH_SHORT);
                }

            }
        });
        setListener();
    }
    public void setListener() {
        Log.d("imageView", "setListener: ");
        friendFragmentBinding.friendCancelImageButton.setOnClickListener(this);
        friendFragmentBinding.friendDeleteImageButton.setOnClickListener(this);
        friendFragmentBinding.friendAllSelectImageButton.setOnClickListener(this);
        friendFragmentBinding.friendMyFloatingActionButton.setOnClickListener(this);
        friendItemAdapter.setItemLongOnClickListener(new FriendItemAdapter.ItemLongOnClickListener() {
            @Override
            public void itemLongOnClick() {
                Log.d("imageView", "setListener: 长按");
                friendItemAdapter.setCheckBoxIsShow();
                friendFragmentBinding.friendItemLongClickEditWindow.setVisibility(View.VISIBLE);
            }
        });

        //item点击跳转聊天页面
        friendItemAdapter.setItemOnClickListener(new FriendItemAdapter.ItemOnClickListener() {
            @Override
            public void itemOnClick(int position,String fUid,String fName,String fImgUrl) {
                Bundle bundle = new Bundle();
                bundle.putString("fName",fName);
                bundle.putString("fImgUrl",fImgUrl);
                bundle.putString("fUid",fUid);
                controller.navigate(R.id.action_friendFragment_to_chatFragment,bundle);
            }
        });

        //头像点击跳转好友信息页面
        friendItemAdapter.setFInfoOnClickListener(new FriendItemAdapter.FInfoOnClickListener() {
            @Override
            public void fInfoOnClick (int position,long id,String fuid) {
                Bundle bundle = new Bundle();
                bundle.putLong("fid",id);
                bundle.putString("fuid",fuid);
                controller.navigate(R.id.action_friendFragment_to_friendInfoFragment,bundle);
            }
        });

        SearchView searchView = (SearchView) (friendFragmentBinding.friendFragmentToolBar.getMenu().findItem(R.id.friendSearch).getActionView());
        searchView.setMaxWidth(500);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Log.d("imageView", "onQueryTextChange:");
                if (friendsLiveData != null && friendsLiveData.hasObservers()) {
                    friendsLiveData.removeObservers(getViewLifecycleOwner());
                }
                friendsLiveData = friendViewModel.getAllFriendsInDBByFName(newText);
                friendsLiveData.observe(getViewLifecycleOwner(), new Observer<List<Friend>>() {
                    @Override
                    public void onChanged(List<Friend> friends) {
                        if (friends == null || friends.size() == 0) {
                            return;
                        }
                        Log.d("imageView", friends.get(0).toString());
                        friendItemAdapter.setFriends(friends);
                        friendItemAdapter.notifyDataSetChanged();
                    }
                });
                return true;
            }
        });
        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                if (friendsLiveData != null && friendsLiveData.hasObservers()) {
                    friendsLiveData.removeObservers(getViewLifecycleOwner());
                }
                friendsLiveData = friendViewModel.getAllFriendsInDB();
                friendsLiveData.observe(getViewLifecycleOwner(), new Observer<List<Friend>>() {
                    @Override
                    public void onChanged(List<Friend> friends) {
                        friendItemAdapter.setFriends(friends);
                        friendItemAdapter.notifyDataSetChanged();
                    }
                });
                return false;
            }
        });

        //导航栏Menu菜单监听事件
        friendFragmentBinding.friendFragmentToolBar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.friendAdd:{
                        AddFriendDialog addFriendDialog = new AddFriendDialog();
                        addFriendDialog.setEnterClicked(new AddFriendDialog.EnterListener() {
                            @RequiresApi(api = Build.VERSION_CODES.O)
                            @Override
                            public void enterBtnOnClicked() {
                                addFriend(addFriendDialog.getFriendEmail());
                            }
                        });
                        addFriendDialog.show(fragmentManager,"addBillDialogByMenu");
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
            case R.id.friendCancelImageButton: {
                Log.d("imageView", "onClick: 取消按钮");
                friendItemAdapter.cancelFriend();
                friendFragmentBinding.friendItemLongClickEditWindow.setVisibility(View.GONE);
                break;
            }
            case R.id.friendDeleteImageButton: {
                Log.d("imageView", "onClick: 删除按钮");
                friendViewModel.deleteFriendsByIdsInServer(friendItemAdapter.getEditModelSelectedBills()).observe(getViewLifecycleOwner(), new Observer<ReturnData<Object>>() {
                    @Override
                    public void onChanged(ReturnData<Object> objectReturnData) {
                        if (objectReturnData.getCode() == RCodeEnum.OK.getCode()) {
                            friendItemAdapter.deleteSelectedFriends();
                            Toast.makeText(getContext(),"删除成功",Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(getContext(),objectReturnData.getMsg(),Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                break;
            }
            case R.id.friendAllSelectImageButton: {
                Log.d("imageView", "onClick: 全选按钮");
                friendFragmentBinding.friendAllSelectImageButton.setBackgroundColor(R.color.colorTextTrue);
                friendItemAdapter.selectedAllFriends();
                break;
            }
            case R.id.friendMyFloatingActionButton: {
                Log.d("imageView", "onClick: 添加按钮");
                AddFriendDialog addFriendDialog = new AddFriendDialog();
                addFriendDialog.setEnterClicked(new AddFriendDialog.EnterListener() {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void enterBtnOnClicked() {
                        String friendEmail = addFriendDialog.getFriendEmail();
                        addFriend(friendEmail);
                    }
                });
                addFriendDialog.show(fragmentManager,"addFriendDialogByFloatingActionButton");
                break;
            }
            default:{
                Log.d("imageView", "onClick: ");
                break;
            }
        }
    }

    //添加账单方法
    private void addFriend(String friendEmail) {
        friendViewModel.addFriendInServer(friendEmail).observe(getViewLifecycleOwner(), new Observer<ReturnData<Friend>>() {
            @Override
            public void onChanged(ReturnData<Friend> friendReturnData) {
                if (friendReturnData.getCode() == RCodeEnum.OK.getCode()) {
                    Toast.makeText(getContext(),"添加好友成功",Toast.LENGTH_SHORT).show();
                    Log.d("imageView", "onChanged: " + friendReturnData.getData().toString());
                    friendItemAdapter.addFriend(friendReturnData.getData());
                    friendViewModel.addFriendsInDB(friendReturnData.getData());
                }else {
                    Toast.makeText(getContext(),friendReturnData.getMsg(),Toast.LENGTH_SHORT).show();
                    Log.d("imageView",friendReturnData.getCode() + friendReturnData.getMsg());
                }
            }
        });
    }
}