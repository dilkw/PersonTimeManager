package com.demo.androidapp.view;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
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
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.demo.androidapp.R;
import com.demo.androidapp.databinding.BillFragmentBinding;
import com.demo.androidapp.model.common.RCodeEnum;
import com.demo.androidapp.model.common.ReturnData;
import com.demo.androidapp.model.entity.Bill;
import com.demo.androidapp.model.returnObject.ReturnListObject;
import com.demo.androidapp.view.myView.AddBillDialog;
import com.demo.androidapp.view.myView.adapter.BillItemAdapter;
import com.demo.androidapp.viewmodel.BillViewModel;

import java.util.ArrayList;
import java.util.List;

public class BillFragment extends Fragment implements View.OnClickListener {

    private BillViewModel billViewModel;

    private BillFragmentBinding billFragmentBinding;

    private FragmentManager fragmentManager;

    private NavHostFragment navHostFragment;

    private NavController controller;

    private BillItemAdapter billItemAdapter;

    private LiveData<List<Bill>> billsLiveData;

    private AppBarConfiguration appBarConfiguration;

    public static BillFragment newInstance() {
        return new BillFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        billFragmentBinding = DataBindingUtil.inflate(inflater,R.layout.bill_fragment,container,false);
        billViewModel = new ViewModelProvider(this).get(BillViewModel.class);
        fragmentManager = requireActivity().getSupportFragmentManager();
        navHostFragment = (NavHostFragment)fragmentManager.findFragmentById(R.id.fragment);
        assert navHostFragment != null;
        controller = navHostFragment.getNavController();
        appBarConfiguration = new AppBarConfiguration.Builder(controller.getGraph()).build();
        NavigationUI.setupWithNavController(billFragmentBinding.billFragmentToolBar,controller,appBarConfiguration);
        return billFragmentBinding.getRoot();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        billItemAdapter = new BillItemAdapter((List<Bill>)(new ArrayList<Bill>()));
        billFragmentBinding.billRecyclerView.setLayoutManager(new LinearLayoutManager(requireActivity()));
        billFragmentBinding.billRecyclerView.setAdapter(billItemAdapter);
        billViewModel.getAllBillsInServer().observe(getViewLifecycleOwner(), new Observer<ReturnData<ReturnListObject<Bill>>>() {
            @SuppressLint("ShowToast")
            @Override
            public void onChanged(ReturnData<ReturnListObject<Bill>> listReturnData) {
                RCodeEnum rCodeEnum = RCodeEnum.returnRCodeEnumByCode(listReturnData.getCode());
                if (rCodeEnum == RCodeEnum.OK) {
                    billItemAdapter.setBills(listReturnData.getData().getItems());
                    billViewModel.deleteAllBillsAndAdd(listReturnData.getData().getItems());
                }else {
                    Toast.makeText(getContext(),rCodeEnum.getMessage(),Toast.LENGTH_SHORT);
                }

            }
        });
        setListener();
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
            public void itemOnClick(Bill bill,int position) {
                AddBillDialog addBillDialog = new AddBillDialog(bill);
                addBillDialog.setEnterClicked(new AddBillDialog.EnterListener() {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void enterBtnOnClicked() {
                        Bill bill = addBillDialog.getBill();
                        upDateBill(bill,position);
                    }
                });
                addBillDialog.show(fragmentManager,"editBillDialog");
            }
        });

        //导航栏Menu菜单监听事件
        //导航栏Menu菜单，搜索框监听事件
        SearchView searchView = (SearchView) (billFragmentBinding.billFragmentToolBar.getMenu().findItem(R.id.billSearch).getActionView());
        searchView.setMaxWidth(500);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Log.d("imageView", "onQueryTextChange:");
                if (billsLiveData != null && billsLiveData.hasObservers()) {
                    billsLiveData.removeObservers(getViewLifecycleOwner());
                }
                billsLiveData = billViewModel.getBillsLiveDataByContentInDB(newText);
                billsLiveData.observe(getViewLifecycleOwner(), new Observer<List<Bill>>() {
                    @Override
                    public void onChanged(List<Bill> bills) {
                        if (bills == null || bills.size() == 0) {
                            return;
                        }
                        Log.d("imageView", bills.get(0).toString());
                        billItemAdapter.setBills(bills);
                        billItemAdapter.notifyDataSetChanged();
                    }
                });
                return true;
            }
        });
        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                if (billsLiveData != null && billsLiveData.hasObservers()) {
                    billsLiveData.removeObservers(getViewLifecycleOwner());
                }
                billsLiveData = billViewModel.getAllBillLiveDataInDB();
                billsLiveData.observe(getViewLifecycleOwner(), new Observer<List<Bill>>() {
                    @Override
                    public void onChanged(List<Bill> bills) {
                        billItemAdapter.setBills(bills);
                        billItemAdapter.notifyDataSetChanged();
                    }
                });
                return false;
            }
        });
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
                                addBill(addBillDialog.getBill());
                            }
                        });
                        addBillDialog.show(fragmentManager,"addBillDialogByMenu");
                        break;
                    }
                    default:{
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
                billViewModel.deleteClocksByIdsInServer(billItemAdapter.getEditModelSelectedBills()).observe(getViewLifecycleOwner(), new Observer<ReturnData<Object>>() {
                    @Override
                    public void onChanged(ReturnData<Object> objectReturnData) {
                        if (objectReturnData.getCode() == RCodeEnum.OK.getCode()) {
                            billItemAdapter.deleteSelectedBills();
                            Toast.makeText(getContext(),"删除成功",Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(getContext(),objectReturnData.getMsg(),Toast.LENGTH_SHORT).show();
                        }
                    }
                });
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
                        Bill bill = addBillDialog.getBill();
                        addBill(bill);
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

    //添加账单方法
    private void addBill(Bill bill) {
        billViewModel.addBillInServer(bill).observe(getViewLifecycleOwner(), new Observer<ReturnData<Bill>>() {
            @Override
            public void onChanged(ReturnData<Bill> billReturnData) {
                if (billReturnData.getCode() == RCodeEnum.OK.getCode()) {
                    Toast.makeText(getContext(),"添加账单成功",Toast.LENGTH_SHORT).show();
                    Log.d("imageView", "onChanged: " + billReturnData.getData().toString());
                    billItemAdapter.addBill(billReturnData.getData());
                    billViewModel.addBillsInDB(billReturnData.getData());
                }else {
                    Toast.makeText(getContext(),"添加账单失败",Toast.LENGTH_SHORT).show();
                    Log.d("imageView",billReturnData.getCode() + billReturnData.getMsg());
                }
            }
        });
    }

    //更新账单方法
    private void upDateBill(Bill bill,int position) {
        billViewModel.upDateBillInServer(bill).observe(getViewLifecycleOwner(), new Observer<ReturnData<Object>>() {
            @Override
            public void onChanged(ReturnData<Object> billReturnData) {
                if (billReturnData.getCode() == RCodeEnum.OK.getCode()) {
                    Toast.makeText(getContext(),"更新账单成功",Toast.LENGTH_SHORT).show();
                    billItemAdapter.notifyItemChanged(position,bill);
                    billViewModel.upDateBillsInDB(bill);
                }else {
                    Toast.makeText(getContext(),"更新账单失败",Toast.LENGTH_SHORT).show();
                    Log.d("imageView",billReturnData.getCode() + billReturnData.getMsg());
                }
            }
        });
    }
}