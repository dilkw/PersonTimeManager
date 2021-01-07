package com.demo.androidapp.view;

import androidx.annotation.RequiresApi;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import com.demo.androidapp.R;
import com.demo.androidapp.viewmodel.RetrievePasswordViewModel;

import java.util.Objects;

public class RetrievePasswordFragment extends Fragment {

    private RetrievePasswordViewModel mViewModel;

    public static RetrievePasswordFragment newInstance() {
        return new RetrievePasswordFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        //Log.d("imageView", "onActivityCreated: " + Objects.requireNonNull(requireActivity().getActionBar()).getTitle());
        return inflater.inflate(R.layout.retrieve_password_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(RetrievePasswordViewModel.class);
    }
}