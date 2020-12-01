package com.demo.androidapp.view;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.demo.androidapp.MainActivity;
import com.demo.androidapp.R;
import com.demo.androidapp.databinding.ActiveFragmentBinding;
import com.demo.androidapp.view.myView.IdentifyCodeView;
import com.demo.androidapp.viewmodel.ActiveViewModel;

public class ActiveFragment extends Fragment implements IdentifyCodeView.CodesChangedListener {

    private ActiveViewModel mViewModel;

    public static ActiveFragment newInstance() {
        return new ActiveFragment();
    }

    private MainActivity.FragmentTouchListener fragmentTouchListener;

    private ActiveFragmentBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        //View view = inflater.inflate(R.layout.active_fragment, container, false);
        binding = DataBindingUtil.inflate(inflater,R.layout.active_fragment,container,false);
        binding.getRoot().setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (binding.identifyCodeView.isFocused()) {
                    //binding.identifyCodeView.hideSoftInputOutOfVonClick();
                    binding.identifyCodeView.clearFocus();
                }
                return true;
            }
        });
        //this.getActivity().getSupportFragmentManager().putFragment(null,"activeFragment",this);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(ActiveViewModel.class);
        binding.setActiveViewModel(mViewModel);
        binding.setView(binding.identifyCodeView);
        binding.identifyCodeView.addCodesChangeListener(new IdentifyCodeView.CodesChangedListener() {
            @Override
            public void textChanged(String codes) {

            }
        });
        binding.identifyCodeView.setOnLongClickListener();
        binding.identifyCodeView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    binding.identifyCodeView.hideSoftInputOutOfVonClick();
                }
            }
        });

        mViewModel.getCodesLiveData().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                Log.d("imageView", "onChanged: mViewModel观察者" + s);
                Toast.makeText(getContext(),"changed",Toast.LENGTH_SHORT).show();
                if (s.length() == 6) {
                    binding.activeButton.setEnabled(true);
                }
            }
        });
//        this.fragmentTouchListener = new MainActivity.FragmentTouchListener() {
//            @Override
//            public boolean fragmentTouchEvent(MotionEvent event) {
//                Log.d("imageView", "fragmentTouchEvent: 收起键盘");
//                //if (event)
//                binding.identifyCodeView.hideSoftInputOutOfVonClick();
//                return false;
//            }
//        };

        //((MainActivity)getActivity()).addFragmentTouchListener(this.fragmentTouchListener);
        // TODO: Use the ViewModel
    }


    @Override
    public void textChanged(String codes) {
        if (codes.length() == 6) {
            binding.activeButton.setEnabled(true);
        }
    }
}