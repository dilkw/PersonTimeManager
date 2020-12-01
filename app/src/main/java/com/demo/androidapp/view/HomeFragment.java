package com.demo.androidapp.view;

import androidx.lifecycle.ViewModelProvider;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.demo.androidapp.MyApplication;
import com.demo.androidapp.R;
import com.demo.androidapp.model.Auth;
import com.demo.androidapp.util.DataSP;
import com.demo.androidapp.viewmodel.HomeViewModel;

public class HomeFragment extends Fragment {

    private HomeViewModel mViewModel;

    private Auth auth;

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_fragment, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(HomeViewModel.class);

        DataSP dataSP = new DataSP(getContext());
        Log.d("MyView", "onCreateView1: ");
        if(MyApplication.getApplication().getUSER_NAME().equals("userName")){
            Log.d("MyView", "onCreateView2: ");
            NavController navController = Navigation.findNavController(requireView());
            navController.navigate(R.id.action_homeFragment_to_loginFragment);
        }
        //Log.d("MyView",auth.toString());
        Log.d("MyView", "onCreateView3: ");
    }

    @Override
    public void onResume() {
        super.onResume();
        if (auth == null)
        MyApplication.getApplication().getUSER_NAME();
    }
}
