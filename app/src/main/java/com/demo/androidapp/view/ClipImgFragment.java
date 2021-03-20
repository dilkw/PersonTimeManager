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

import com.demo.androidapp.R;
import com.demo.androidapp.databinding.ClipimgFragmentBinding;

import static android.app.Activity.RESULT_OK;
import static com.demo.androidapp.view.UserInfoFragment.REQUEST_IMAGE_OPEN;

public class ClipImgFragment extends Fragment {

    private ClipimgFragmentBinding clipimgFragmentBinding;

    static final int REQUEST_IMAGE_GET = 1;

    public static UserInfoFragment newInstance() {
        return new UserInfoFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        clipimgFragmentBinding = DataBindingUtil.inflate(inflater, R.layout.clipimg_fragment,container,false);
        return clipimgFragmentBinding.getRoot();
    }

    @SuppressLint("QueryPermissionsNeeded")
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        if (intent.resolveActivity(requireActivity().getPackageManager()) != null) {
            startActivityForResult(intent, REQUEST_IMAGE_GET);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == REQUEST_IMAGE_GET && resultCode == RESULT_OK) {
            assert data != null;
            Bitmap thumbnail = data.getParcelableExtra("data");
            Uri fullPhotoUri = data.getData();
            Log.d("imageView", "onActivityResult: " + (thumbnail == null ? "null" : "notNull") + fullPhotoUri.getPath());
            clipimgFragmentBinding.clipImgFragmentClipImgView.setOriginalBitmapByUrl(fullPhotoUri.getPath());
        }

    }
}
