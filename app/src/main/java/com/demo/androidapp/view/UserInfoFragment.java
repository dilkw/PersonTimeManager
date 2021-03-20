package com.demo.androidapp.view;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcel;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
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

import com.demo.androidapp.MyApplication;
import com.demo.androidapp.R;
import com.demo.androidapp.databinding.UserinfoFragmentBinding;
import com.demo.androidapp.model.common.RCodeEnum;
import com.demo.androidapp.model.common.ReturnData;
import com.demo.androidapp.view.myView.ResetEmailDialog;
import com.demo.androidapp.viewmodel.UserInfoViewModel;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects;

import static android.app.Activity.RESULT_OK;

public class UserInfoFragment extends Fragment implements View.OnClickListener {

    private UserInfoViewModel userInfoViewModel;

    private UserinfoFragmentBinding userinfoFragmentBinding;

    private FragmentManager fragmentManager;

    private NavHostFragment navHostFragment;

    private NavController controller;

    private LiveData<Boolean> isMe;

    private AppBarConfiguration appBarConfiguration;

    public static UserInfoFragment newInstance() {
        return new UserInfoFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        userinfoFragmentBinding = DataBindingUtil.inflate(inflater,R.layout.userinfo_fragment,container,false);
        userinfoFragmentBinding.setLifecycleOwner(this);
        fragmentManager = requireActivity().getSupportFragmentManager();
        navHostFragment = (NavHostFragment)fragmentManager.findFragmentById(R.id.fragment);
        assert navHostFragment != null;
        controller = navHostFragment.getNavController();
        appBarConfiguration = new AppBarConfiguration.Builder(controller.getGraph()).build();
        NavigationUI.setupWithNavController(userinfoFragmentBinding.userInfoFragmentToolBar,controller,appBarConfiguration);
        return userinfoFragmentBinding.getRoot();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        userInfoViewModel = new ViewModelProvider(this).get(UserInfoViewModel.class);
        userinfoFragmentBinding.setUserInfoViewModel(userInfoViewModel);
        setListener();
    }

    public void setListener() {
        userinfoFragmentBinding.userInfoImageView.setOnClickListener(this);
        userinfoFragmentBinding.userInfoEditNameButton.setOnClickListener(this);
        userinfoFragmentBinding.userInfoResetEmailButton.setOnClickListener(this);
        userinfoFragmentBinding.userInfoCancellationButton.setOnClickListener(this);
        userinfoFragmentBinding.userInfoResetPwdItem.setOnClickListener(this);
    }

    @SuppressLint({"NonConstantResourceId", "ResourceAsColor"})
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.userInfoImageView: {
                Log.d("imageView", "onClick: 用户头像点击");
                userImgOnclick();
                break;
            }
            case R.id.userInfoEditNameButton: {
                Log.d("imageView", "onClick: 更改昵称按钮");
                updateUserName();
                break;
            }
            case R.id.userInfoResetEmailButton: {
                Log.d("imageView", "onClick: 更改邮箱按钮");
                String email = Objects.requireNonNull(userInfoViewModel.getUserInfoLiveData().getValue()).getData().getEmail();
                ResetEmailDialog resetEmailDialog = new ResetEmailDialog(email);
                resetEmailDialog.show(fragmentManager,"resetEmailDialog");
                resetEmailDialog.setEnterClicked(new ResetEmailDialog.EnterListener() {
                    @Override
                    public void enterBtnOnClicked(String newEmail,String code) {
                        Log.d("imageView", "enterBtnOnClicked:" + newEmail + code);
                        resetEmail(email,newEmail,code);
                    }
                });
                break;
            }
            case R.id.userInfoCancellationButton: {
                Log.d("imageView", "onClick: 注销帐号按钮");
                cancellation();
                break;
            }
            case R.id.userInfoResetPwdItem: {
                Log.d("imageView", "onClick: 重置密码");
                controller.navigate(R.id.action_userInfoFragment_to_retrievePasswordFragment);
                break;
            }
            default:{
                Log.d("imageView", "onClick: ");
                break;
            }
        }
    }

    //更绑邮箱
    private void resetEmail(String email,String newEmail,String code) {
        userInfoViewModel.resetEmail(email,newEmail,code).observe(getViewLifecycleOwner(), new Observer<ReturnData<Object>>() {
            @Override
            public void onChanged(ReturnData<Object> objectReturnData) {
                if (objectReturnData.getCode() == RCodeEnum.OK.getCode()) {
                    Log.d("imageView", "onChanged: 更换邮箱成功");
                    Toast.makeText(getContext(),"更换邮箱成功",Toast.LENGTH_SHORT).show();
                    userInfoViewModel.getUserInfoLiveData().getValue().getData().setEmail(newEmail);
                    userinfoFragmentBinding.userInfoEmailTextView.setText(newEmail);
                }else {
                    Log.d("imageView", "onChanged: 更换邮箱失败" + objectReturnData.getMsg());
                    Toast.makeText(getContext(),"更换邮箱失败" + objectReturnData.getMsg(),Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    //更改昵称
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void updateUserName() {
        View contentView = getLayoutInflater().inflate(R.layout.edit_username_dialog,null,false);
        AlertDialog alertDialogs = new AlertDialog.Builder(getContext())
                .setView(contentView)
                .create();

        TextInputEditText newNameEditText = contentView.findViewById(R.id.editUserNameInputText);
        TextInputLayout newNameTextInputLayout = contentView.findViewById(R.id.editUserNameInputTextLayout);
        MaterialButton negativeBtn = contentView.findViewById(R.id.editUserNameDialogNegativeBtn);
        MaterialButton enterBtn = contentView.findViewById(R.id.editUserNameDialogEnterBtn);

        newNameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                enterBtn.setEnabled(s != null && s.length() > 0);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        negativeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialogs.dismiss();
            }
        });

        enterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = Objects.requireNonNull(newNameEditText.getText()).toString();
                userInfoViewModel.userInfoEditName(name).observe(getViewLifecycleOwner(), new Observer<ReturnData<Object>>() {
                    @Override
                    public void onChanged(ReturnData<Object> objectReturnData) {
                        if (objectReturnData.getCode() == RCodeEnum.OK.getCode()) {
                            Toast.makeText(getContext(),"更换昵称成功",Toast.LENGTH_SHORT).show();
                            userInfoViewModel.getUserInfoLiveData().getValue().getData().setName(name);
                            userinfoFragmentBinding.userInfoNameTextView.setText(name);
                            Log.d("imageView", "onChanged: " + userInfoViewModel.getUserInfoLiveData().getValue().getData().getName());
                            alertDialogs.dismiss();
                        }else {
                            newNameTextInputLayout.setError(objectReturnData.getMsg());
                            Toast.makeText(getContext(),"更换昵称失败" + objectReturnData.getMsg(),Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
        alertDialogs.show();
    }

    //注销账号
    private void cancellation() {
        View contentView = getLayoutInflater().inflate(R.layout.cancellation_dialog,null,false);
        AlertDialog cancellationAlertDialogs = new AlertDialog.Builder(getContext())
                .setView(contentView)
                .create();

        TextInputEditText codeEditText = contentView.findViewById(R.id.cancellationCodeInputText);
        TextView codeSendTipTextView = contentView.findViewById(R.id.cancellationCodeSendTip);
        TextInputLayout codeTextInputLayout = contentView.findViewById(R.id.cancellationCodeInputTextLayout);
        MaterialButton getCodeBtn = contentView.findViewById(R.id.cancellationCodeGetBtn);
        MaterialButton negativeBtn = contentView.findViewById(R.id.cancellationCodeDialogNegativeBtn);
        MaterialButton enterBtn = contentView.findViewById(R.id.cancellationCodeDialogEnterBtn);

        codeEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                boolean b = s != null && s.length() == 6;
                enterBtn.setEnabled(b);
                codeTextInputLayout.setHelperTextEnabled(b);
                codeTextInputLayout.setErrorEnabled(!b);
                if (!b) {
                    codeTextInputLayout.setError("请注意大小写，验证码长度为6位");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        getCodeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userInfoViewModel.getCancellationCode().observe(getViewLifecycleOwner(), new Observer<ReturnData<Object>>() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onChanged(ReturnData<Object> objectReturnData) {
                        if (objectReturnData.getCode() == RCodeEnum.OK.getCode()) {
                            Toast.makeText(getContext(),"验证码获取成功",Toast.LENGTH_SHORT).show();
                            codeSendTipTextView.setText("验证码已成功发送至您的邮箱：" + userInfoViewModel.userReturnLiveData.getValue().getData().getEmail());
                        }else {
                            codeSendTipTextView.setText("验证码发送失败！，请检查您的网络，或是稍后再次获取");
                            Toast.makeText(getContext(),"验证码获取失败" + objectReturnData.getMsg(),Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        negativeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancellationAlertDialogs.dismiss();
            }
        });

        enterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String code = codeEditText.getText().toString().trim();
                enterCancellationDialog(code,cancellationAlertDialogs);
            }
        });
        cancellationAlertDialogs.show();
    }

    //确定注销帐号确认框
    private void enterCancellationDialog(String code , AlertDialog cancellationAlertDialogs) {
        String msg = "是否确定注销";
        AlertDialog cancellationDialog = new AlertDialog.Builder(getContext())
                .setTitle("提示")
                .setMessage(msg)
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        userInfoViewModel.cancellation(code).observe(getViewLifecycleOwner(), new Observer<ReturnData<Object>>() {
                            @Override
                            public void onChanged(ReturnData<Object> objectReturnData) {
                                if (objectReturnData.getCode() == RCodeEnum.OK.getCode()) {
                                    Toast.makeText(getContext(),"帐号注销成功成功",Toast.LENGTH_SHORT).show();
                                    MyApplication.getApplication().signOut();
                                }else {
                                    Toast.makeText(getContext(),"验证码获取失败" + objectReturnData.getMsg(),Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                })
                .create();
        cancellationDialog.show();
    }

    //点击头像弹出对话框
    static final int REQUEST_IMAGE_OPEN = 1;
    private void userImgOnclick() {
        View contentView = requireActivity().getLayoutInflater().inflate(R.layout.imgonclick_dialog,null,false);
        AlertDialog alertDialog = new AlertDialog.Builder(getContext())
                .setView(contentView)
                .create();
        alertDialog.show();
        Button imgFromGalleryBtn = contentView.findViewById(R.id.imgClickDialogSelectImgBtn);
        Button alertDialogCancelBtn = contentView.findViewById(R.id.imgClickDialogCancelBtn);
        View.OnClickListener onClickListener = new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @SuppressLint("QueryPermissionsNeeded")
            @Override
            public void onClick(View v) {
                if (v.getId() == R.id.imgClickDialogSelectImgBtn) {
                    controller.navigate(R.id.action_userInfoFragment_to_clipImgFragment);
                }else{
                    alertDialog.dismiss();
                }
            }
        };
        imgFromGalleryBtn.setOnClickListener(onClickListener);
        alertDialogCancelBtn.setOnClickListener(onClickListener);
    }

}