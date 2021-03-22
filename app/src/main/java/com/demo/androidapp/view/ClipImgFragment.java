package com.demo.androidapp.view;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.demo.androidapp.R;
import com.demo.androidapp.databinding.ClipimgFragmentBinding;

import java.io.File;
import java.io.IOException;

import static android.app.Activity.RESULT_OK;
import static com.demo.androidapp.view.UserInfoFragment.REQUEST_IMAGE_OPEN;

public class ClipImgFragment extends Fragment implements View.OnClickListener {

    private ClipimgFragmentBinding clipimgFragmentBinding;

    NavController controller;

    static final int REQUEST_IMAGE_GET = 1;

    public static UserInfoFragment newInstance() {
        return new UserInfoFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        clipimgFragmentBinding = DataBindingUtil.inflate(inflater, R.layout.clipimg_fragment,container,false);
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        NavHostFragment navHostFragment = (NavHostFragment)fragmentManager.findFragmentById(R.id.fragment);
        controller = navHostFragment.getNavController();
        return clipimgFragmentBinding.getRoot();
    }

    @SuppressLint("QueryPermissionsNeeded")
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        selectImgFromGallery();
        setOnClickListener();
    }

    //跳转图库获取图片返回
    @SuppressLint("QueryPermissionsNeeded")
    private void selectImgFromGallery() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        if (intent.resolveActivity(requireActivity().getPackageManager()) != null) {
            startActivityForResult(intent, REQUEST_IMAGE_GET);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode,resultCode,data);
        if (requestCode == REQUEST_IMAGE_GET && resultCode == RESULT_OK) {
            assert data != null;
            Bitmap bitmap = data.getParcelableExtra("data");
            Uri uri = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(requireActivity().getContentResolver(), uri);
            } catch (IOException e) {
                e.printStackTrace();
            }
            Log.d("imageView", "onActivityResult: " + (bitmap == null ? "null" : "notNull") + uri.getEncodedPath());
            clipimgFragmentBinding.clipImgFragmentClipImgView.setOriginalBitmap(bitmap);
        }
    }

    private void setOnClickListener() {
        clipimgFragmentBinding.clipImgFragmentBackBtn.setOnClickListener(this);
        clipimgFragmentBinding.clipImgFragmentEnterBtn.setOnClickListener(this);
        clipimgFragmentBinding.clipImgFragmentReSelectBtn.setOnClickListener(this);
        clipimgFragmentBinding.clipImgFragmentCancelBtn.setOnClickListener(this);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.clipImgFragmentBackBtn: {
                Log.d("imageView", "onClick: 返回按钮点击");
                controller.navigateUp();
                break;
            }
            case R.id.clipImgFragmentEnterBtn: {
                Log.d("imageView", "onClick: 确定按钮点击");
                Bitmap bitmap = clipimgFragmentBinding.clipImgFragmentClipImgView.getClipBitmap();
                if (bitmap == null) {
                    Log.d("imageView", "onClick: bitmap为空");
                    break;
                }
                clipimgFragmentBinding.clipImageView.setImageBitmap(bitmap);
                Log.d("imageView", "onClick: bitmap不为空");
                break;
            }
            case R.id.clipImgFragmentReSelectBtn: {
                Log.d("imageView", "onClick: 重选按钮点击");
                selectImgFromGallery();

                break;
            }
            case R.id.clipImgFragmentCancelBtn: {
                Log.d("imageView", "onClick: 取消按钮点击");
                clipimgFragmentBinding.clipImgFragmentClipImgView.setCancelClip();
                clipimgFragmentBinding.clipImageView.setImageBitmap(null);
                break;
            }
            default:{
                break;
            }
        }
    }
}
