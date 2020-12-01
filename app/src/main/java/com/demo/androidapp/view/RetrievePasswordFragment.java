package com.demo.androidapp.view;

import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.demo.androidapp.R;
import com.demo.androidapp.viewmodel.RetrievePasswordViewModel;

public class RetrievePasswordFragment extends Fragment {

    private RetrievePasswordViewModel mViewModel;

    public static RetrievePasswordFragment newInstance() {
        return new RetrievePasswordFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.retrieve_password_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(RetrievePasswordViewModel.class);
        // TODO: Use the ViewModel
    }

}