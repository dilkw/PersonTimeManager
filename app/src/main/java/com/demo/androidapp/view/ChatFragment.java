package com.demo.androidapp.view;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.AsyncNotedAppOp;
import android.app.WallpaperColors;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
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
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.demo.androidapp.MyApplication;
import com.demo.androidapp.R;
import com.demo.androidapp.databinding.ChatFragmentBinding;
import com.demo.androidapp.databinding.UserinfoFragmentBinding;
import com.demo.androidapp.model.common.RCodeEnum;
import com.demo.androidapp.model.common.ReturnData;
import com.demo.androidapp.model.entity.ChatRecord;
import com.demo.androidapp.model.entity.Clock;
import com.demo.androidapp.model.entity.Friend;
import com.demo.androidapp.model.entity.User;
import com.demo.androidapp.util.DateTimeUtil;
import com.demo.androidapp.view.myView.ResetEmailDialog;
import com.demo.androidapp.view.myView.adapter.ChatItemAdapter;
import com.demo.androidapp.viewmodel.ChatViewModel;
import com.demo.androidapp.viewmodel.FriendViewModel;
import com.demo.androidapp.viewmodel.UserInfoViewModel;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.hyphenate.EMCallBack;
import com.hyphenate.EMClientListener;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMChatManager;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMCmdMessageBody;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMTextMessageBody;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ChatFragment extends Fragment implements View.OnClickListener, EMMessageListener {

    private ChatFragmentBinding chatFragmentBinding;

    private FragmentManager fragmentManager;

    private NavHostFragment navHostFragment;

    private NavController controller;

    private AppBarConfiguration appBarConfiguration;

    private ChatItemAdapter chatItemAdapter;

    private ChatViewModel chatViewModel;

    private String fName = "";
    private String fImgUrl = "";
    private String fUid = "";

    public static ChatFragment newInstance() {
        return new ChatFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        chatFragmentBinding = DataBindingUtil.inflate(inflater,R.layout.chat_fragment,container,false);
        chatFragmentBinding.setLifecycleOwner(this);
        fragmentManager = requireActivity().getSupportFragmentManager();
        navHostFragment = (NavHostFragment)fragmentManager.findFragmentById(R.id.fragment);
        assert navHostFragment != null;
        controller = navHostFragment.getNavController();
        appBarConfiguration = new AppBarConfiguration.Builder(controller.getGraph()).build();
        NavigationUI.setupWithNavController(chatFragmentBinding.chatFragmentToolBar,controller,appBarConfiguration);
        return chatFragmentBinding.getRoot();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init();
        setListener();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void init() {
        chatViewModel = new ViewModelProvider(this).get(ChatViewModel.class);
        String userName = MyApplication.getApplication().getUser().getName();
        String userPassword = MyApplication.getApplication().getUser().getPassword();
        login(userName,userPassword);
        if (getArguments() != null) {
            fName = getArguments().getString("fName");
            fImgUrl = getArguments().getString("fImgUrl");
            fUid = getArguments().getString("fUid");
        }
        chatFragmentBinding.chatFragmentToolBar.setTitle(fName);
        chatItemAdapter = new ChatItemAdapter(new ArrayList<ChatRecord>(),fImgUrl);
        chatFragmentBinding.chatRecordRecyclerView.setLayoutManager(new LinearLayoutManager(requireActivity()));
        chatFragmentBinding.chatRecordRecyclerView.setAdapter(chatItemAdapter);
        chatViewModel.getAllChatRecordLiveDataByUidAndFId(fUid).observe(getViewLifecycleOwner(), new Observer<List<ChatRecord>>() {
            @Override
            public void onChanged(List<ChatRecord> chatRecords) {
                if (chatRecords != null && chatRecords.size() > 0) {
                    chatItemAdapter.setChatRecords(chatRecords);
                }
            }
        });
        EMConversation conversation = EMClient.getInstance().chatManager().getConversation(fName);
        if (conversation == null) {
            Log.d("imageView", "init: 空对象");
            return ;
        }
        //获取此会话的所有消息
        List<EMMessage> messages = conversation.getAllMessages();
        for (EMMessage message : messages) {
            Log.d("imageView","msgId" + message.getUserName());
            String context = ((EMTextMessageBody)message.getBody()).getMessage();
            Log.d("imageView", "onMessageReceived: " + context);
            long createTime = DateTimeUtil.localDateTimeToLong(LocalDateTime.now());
            String uid = MyApplication.getApplication().getUser().getUid();
            ChatRecord chatRecord = new ChatRecord(createTime,uid,fUid,context,"接收");
            chatItemAdapter.addChatRecords(chatRecord);
        }
        //SDK初始化加载的聊天记录为20条，到顶时需要去DB里获取更多
        //获取startMsgId之前的pagesize条消息，此方法获取的messages SDK会自动存入到此会话中，APP中无需再次把获取到的messages添加到会话中
        //List<EMMessage> messages = conversation.loadMoreMsgFromDB(0, 10);
    }

    public void setListener() {
        chatFragmentBinding.msgSendBtn.setOnClickListener(this);
        chatFragmentBinding.addOtherMsgBtn.setOnClickListener(this);

        chatFragmentBinding.msgEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s != null && s.length() > 0) {
                    chatFragmentBinding.msgSendBtn.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @SuppressLint({"NonConstantResourceId", "ResourceAsColor"})
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.msgSendBtn: {
                Log.d("imageView", "onClick: 发送点击");

                //隐藏键盘
                InputMethodManager inputMethodManager = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                if (inputMethodManager.isActive()) {
                    Log.d("imageView", "hideSoftInputOutOfVonClick: 键盘收起");
                }
                inputMethodManager.hideSoftInputFromWindow(chatFragmentBinding.msgEditText.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                String context = chatFragmentBinding.msgEditText.getText().toString().trim();
                //创建一条文本消息，content为消息文字内容，toChatUsername为对方用户或者群聊的id，后文皆是如此

                EMMessage message = EMMessage.createTxtSendMessage(context,fName);
                //message.setChatType(EMMessage.ChatType.GroupChat);
                //发送消息
                EMClient.getInstance().chatManager().sendMessage(message);
                message.setMessageStatusCallback(new EMCallBack() {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void onSuccess() {
                        long createTime = DateTimeUtil.localDateTimeToLong(LocalDateTime.now());
                        String uid = MyApplication.getApplication().getUser().getUid();
                        ChatRecord chatRecord = new ChatRecord(createTime,uid,fUid,context,"发送");
                        chatItemAdapter.addChatRecords(chatRecord);
                        chatFragmentBinding.msgEditText.setText("");
                        Log.d("imageView", "onSuccess: 发送成功" + fName);
                    }

                    @Override
                    public void onError(int i, String s) {
                        Log.d("imageView", "onError: 发送失败" + fName);
                    }

                    @Override
                    public void onProgress(int i, String s) {
                        Log.d("imageView", "onProgress: 发送失败" + fName);
                    }
                });
                break;
            }
            case R.id.addOtherMsgBtn: {
                Log.d("imageView", "onClick: 添加其他点击");
                break;
            }
            default:{
                Log.d("imageView", "onClick: ");
                break;
            }
        }
    }

    //先登录到环信
    private void login(String name,String password) {
        EMClient.getInstance().login(name, password, new EMCallBack() {
            @Override
            public void onSuccess() {
                EMClient.getInstance().groupManager().loadAllGroups();
                EMClient.getInstance().chatManager().loadAllConversations();
            }

            @Override
            public void onError(int i, String s) {
                requireActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        chatFragmentBinding.msgEditText.setEnabled(false);
                        chatFragmentBinding.addOtherMsgBtn.setEnabled(false);
                        chatFragmentBinding.msgSendBtn.setEnabled(false);
                        AlertDialog alertDialog = new AlertDialog
                                .Builder(requireContext())
                                .setTitle("提示")
                                .setMessage("无法连接到服务器")
                                .setCancelable(false)
                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        controller.navigateUp();
                                    }
                                })
                                .create();
                        alertDialog.show();
                    }
                });
            }

            @Override
            public void onProgress(int i, String s) {

            }
        });
    }

    private void sigOut() {
        EMClient.getInstance().logout(true, new EMCallBack() {
            @Override
            public void onSuccess() {
                // TODO Auto-generated method stub

            }

            @Override
            public void onProgress(int progress, String status) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onError(int code, String message) {
                // TODO Auto-generated method stub

            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        EMClient.getInstance().chatManager().addMessageListener(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EMClient.getInstance().chatManager().removeMessageListener(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
//        sigOut();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onMessageReceived(List<EMMessage> list) {
        for (EMMessage emMessage : list) {
            String context = ((EMTextMessageBody)emMessage.getBody()).getMessage();
            Log.d("imageView", "onMessageReceived: " + context);
            long createTime = DateTimeUtil.localDateTimeToLong(LocalDateTime.now());
            String uid = MyApplication.getApplication().getUser().getUid();
            ChatRecord chatRecord = new ChatRecord(createTime,uid,fUid,context,"接收");
            chatItemAdapter.addChatRecords(chatRecord);
        }
    }

    @Override
    public void onCmdMessageReceived(List<EMMessage> list) {

    }

    @Override
    public void onMessageRead(List<EMMessage> list) {

    }

    @Override
    public void onMessageDelivered(List<EMMessage> list) {

    }

    @Override
    public void onMessageRecalled(List<EMMessage> list) {

    }

    @Override
    public void onMessageChanged(EMMessage emMessage, Object o) {

    }


}