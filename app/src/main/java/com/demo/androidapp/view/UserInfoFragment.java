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
import com.demo.androidapp.databinding.BillFragmentBinding;
import com.demo.androidapp.model.entity.Bill;
import com.demo.androidapp.view.myView.AddBillDialog;
import com.demo.androidapp.view.myView.adapter.BillItemAdapter;
import com.demo.androidapp.viewmodel.BillViewModel;

import java.util.ArrayList;
import java.util.List;

public class UserInfoFragment extends Fragment implements View.OnClickListener {

    private BillViewModel billViewModel;

    private BillFragmentBinding billFragmentBinding;

    private FragmentManager fragmentManager;

    private NavHostFragment navHostFragment;

    private NavController controller;

    private BillItemAdapter billItemAdapter;

    private LiveData<List<Bill>> billsLiveData;

    private AppBarConfiguration appBarConfiguration;

    public static UserInfoFragment newInstance() {
        return new UserInfoFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        billFragmentBinding = DataBindingUtil.inflate(inflater,R.layout.bill_fragment,container,false);
        billViewModel = new ViewModelProvider(this).get(BillViewModel.class);
        View view = billFragmentBinding.getRoot();
        Log.d("imageView", "onCreateView: " + view.getClass().getSimpleName());
        fragmentManager = requireActivity().getSupportFragmentManager();
        navHostFragment = (NavHostFragment)fragmentManager.findFragmentById(R.id.fragment);
        assert navHostFragment != null;
        controller = navHostFragment.getNavController();
        appBarConfiguration = new AppBarConfiguration.Builder(controller.getGraph()).build();
        NavigationUI.setupWithNavController(billFragmentBinding.billFragmentToolBar,controller,appBarConfiguration);
        return view;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        billItemAdapter = new BillItemAdapter((List<Bill>)(new ArrayList<Bill>()));
        billFragmentBinding.billRecyclerView.setLayoutManager(new LinearLayoutManager(requireActivity()));
        billFragmentBinding.billRecyclerView.setAdapter(billItemAdapter);
        billsLiveData = billViewModel.getReturnLiveData();
        billsLiveData.observe(getViewLifecycleOwner(), new Observer<List<Bill>>() {
            @Override
            public void onChanged(List<Bill> bills) {
                billItemAdapter.setBills(bills);
            }
        });
        setListener();
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.billfragment_bar,menu);
        SearchView searchView = (SearchView) menu.findItem(R.id.billSearch).getActionView();
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
                billsLiveData.removeObservers(getViewLifecycleOwner());
                billsLiveData = billViewModel.getBillsLiveDataByContent(newText.trim());
                billsLiveData.observe(getViewLifecycleOwner(), new Observer<List<Bill>>() {
                    @Override
                    public void onChanged(List<Bill> bills) {
                        Log.d("imageView", "onChanged: 查找返回" + bills.size());
                        billItemAdapter.setBills(bills);
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
        billFragmentBinding.billCancelImageButton.setOnClickListener(this);
        billFragmentBinding.billDeleteImageButton.setOnClickListener(this);
        billFragmentBinding.billAllSelectImageButton.setOnClickListener(this);
        billFragmentBinding.billMyFloatingActionButton.setOnClickListener(this);
        billItemAdapter.setItemLongOnClickListener(new BillItemAdapter.ItemLongOnClickListener() {
            @Override
            public void itemLongOnClick() {
                Log.d("imageView", "setListener: 长按");
                billItemAdapter.setCheckBoxIsShow();
                billFragmentBinding.billItemLongClickEditWindow.setVisibility(View.VISIBLE);
            }
        });
        billItemAdapter.setItemOnClickListener(new BillItemAdapter.ItemOnClickListener() {
            @Override
            public void itemOnClick(int position) {
                Log.d("imageView", "setListener: " + billsLiveData.getValue().get(position).toString());
                AddBillDialog addBillDialog = new AddBillDialog(billsLiveData.getValue().get(position));
                addBillDialog.setEnterClicked(new AddBillDialog.EnterListener() {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void enterBtnOnClicked() {
                        billViewModel.upDateBillsInDB(addBillDialog.getBill());
                        billsLiveData = billViewModel.getReturnLiveData();
                    }
                });
                addBillDialog.show(fragmentManager,"editBillDialog");
            }
        });

        //导航栏Menu菜单监听事件
        billFragmentBinding.billFragmentToolBar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.billAdd:{
                        AddBillDialog addBillDialog = new AddBillDialog();
                        addBillDialog.setEnterClicked(new AddBillDialog.EnterListener() {
                            @RequiresApi(api = Build.VERSION_CODES.O)
                            @Override
                            public void enterBtnOnClicked() {
                                billViewModel.addBillsInDB(addBillDialog.getBill());
                                billsLiveData = billViewModel.getReturnLiveData();
                            }
                        });
                        addBillDialog.show(fragmentManager,"addBillDialogByMenu");
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
            case R.id.billCancelImageButton: {
                Log.d("imageView", "onClick: 取消按钮");
                billItemAdapter.cancelBill();
                billFragmentBinding.billItemLongClickEditWindow.setVisibility(View.GONE);
                break;
            }
            case R.id.billDeleteImageButton: {
                Log.d("imageView", "onClick: 删除按钮");
                billViewModel.deleteClocksByUidInDB(billItemAdapter.deleteSelectedBills());
                break;
            }
            case R.id.billAllSelectImageButton: {
                Log.d("imageView", "onClick: 全选按钮");
                billFragmentBinding.billAllSelectImageButton.setBackgroundColor(R.color.colorTextTrue);
                billItemAdapter.selectedAllBills();
                break;
            }
            case R.id.billMyFloatingActionButton: {
                Log.d("imageView", "onClick: 添加按钮");
                AddBillDialog addBillDialog = new AddBillDialog();
                addBillDialog.setEnterClicked(new AddBillDialog.EnterListener() {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void enterBtnOnClicked() {
                        billViewModel.addBillsInDB(addBillDialog.getBill());
                        billsLiveData = billViewModel.getReturnLiveData();
                    }
                });
                addBillDialog.show(fragmentManager,"addBillDialogByFloatingActionButton");
                break;
            }
            default:{
                Log.d("imageView", "onClick: ");
                break;
            }
        }
    }
}