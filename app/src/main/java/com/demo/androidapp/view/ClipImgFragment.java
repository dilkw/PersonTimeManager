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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.demo.androidapp.R;
import com.demo.androidapp.databinding.ClipimgFragmentBinding;
import com.demo.androidapp.model.common.RCodeEnum;
import com.demo.androidapp.model.common.ReturnData;
import com.demo.androidapp.viewmodel.UserInfoViewModel;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import static android.app.Activity.RESULT_OK;
import static com.demo.androidapp.view.UserInfoFragment.REQUEST_IMAGE_OPEN;

public class ClipImgFragment extends Fragment implements View.OnClickListener {

    private ClipimgFragmentBinding clipimgFragmentBinding;

    private NavController controller;

    private UserInfoViewModel userInfoViewModel;

    private String uid = "";

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
        if (getArguments() != null) {
            uid = getArguments().getString("uid");
        }
        userInfoViewModel = new ViewModelProvider(this).get(UserInfoViewModel.class);
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

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
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
                //clipimgFragmentBinding.clipImageView.setImageBitmap(bitmap);
                uploadImg(bitmap);
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
                //clipimgFragmentBinding.clipImageView.setImageBitmap(null);
                break;
            }
            default:{
                break;
            }
        }
    }

    private void uploadImg(Bitmap bitmap) {
        File file = new File(getContext().getFilesDir().getPath() + "/" + uid + ".jpg");//将要保存图片的路径
        try {
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            bos.flush();
            bos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        userInfoViewModel.uploadImg(file).observe(getViewLifecycleOwner(), new Observer<ReturnData<String>>() {
            @Override
            public void onChanged(ReturnData<String> stringReturnData) {
                if (stringReturnData.getCode() == RCodeEnum.OK.getCode()) {
                    Toast.makeText(getContext(),"上传头像成功",Toast.LENGTH_SHORT).show();
                    controller.navigateUp();
                }else {
                    Toast.makeText(getContext(),"上传头像失败",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
