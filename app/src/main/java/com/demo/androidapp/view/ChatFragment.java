package com.demo.androidapp.view;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
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
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.demo.androidapp.MainActivity;
import com.demo.androidapp.MyApplication;
import com.demo.androidapp.R;
import com.demo.androidapp.databinding.ChatFragmentBinding;
import com.demo.androidapp.model.common.RCodeEnum;
import com.demo.androidapp.model.common.ReturnData;
import com.demo.androidapp.model.entity.Clock;
import com.demo.androidapp.model.entity.Task;
import com.demo.androidapp.util.DateTimeUtil;
import com.demo.androidapp.view.myView.DateTimePickerDialog;
import com.demo.androidapp.view.myView.adapter.ChatItemAdapter;
import com.demo.androidapp.view.myView.adapter.itemModel.ChatItemModel;
import com.demo.androidapp.viewmodel.ChatViewModel;
import com.hyphenate.EMCallBack;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMCustomMessageBody;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMTextMessageBody;
import com.hyphenate.exceptions.HyphenateException;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChatFragment extends Fragment implements View.OnClickListener, EMMessageListener {

    private ChatFragmentBinding chatFragmentBinding;

    private FragmentManager fragmentManager;

    private NavHostFragment navHostFragment;

    private NavController controller;

    private AppBarConfiguration appBarConfiguration;

    private ChatItemAdapter chatItemAdapter;

    private ChatViewModel chatViewModel;

    private List<Clock> clockList;

    private List<Task> taskList;

    private String fName = "";
    private String fImgUrl = "";
    private String fUid = "";

    public static ChatFragment newInstance() {
        return new ChatFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        Log.d("imageView", "onCreateView: ");
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
        Log.d("imageView", "onActivityCreated: ");
        super.onActivityCreated(savedInstanceState);
        try {
            init();
        } catch (HyphenateException | JSONException e) {
            e.printStackTrace();
        }
        setListener();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void init() throws HyphenateException, JSONException {
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
        chatItemAdapter = new ChatItemAdapter(new ArrayList<ChatItemModel>(),fImgUrl);
        chatFragmentBinding.chatRecordRecyclerView.setLayoutManager(new LinearLayoutManager(requireActivity()));
        chatFragmentBinding.chatRecordRecyclerView.setAdapter(chatItemAdapter);
        //删除和某个user会话，如果需要保留聊天记录，传false
        //EMClient.getInstance().chatManager().deleteConversation(fName, true);
        EMConversation conversation = EMClient.getInstance().chatManager().getConversation(fName);
        if (conversation == null) {
            Log.d("imageView", "init: 空对象");
            return ;
        }
        //获取此会话的所有消息
        List<EMMessage> messages = conversation.getAllMessages();
        for (EMMessage message : messages) {
            ChatItemModel chatItemModel;
            if (message.getBooleanAttribute("isCustomMsg")) {
                EMCustomMessageBody emCustomMessageBody = (EMCustomMessageBody)message.getBody();
                if (emCustomMessageBody.event().equals("clock")){
                    Map<String,String> map = emCustomMessageBody.getParams();
                    List<Clock> clocks = (List<Clock>) com.alibaba.fastjson.JSONArray.parseArray(map.get("clocks"),Clock.class);
                    if (clockList == null) {
                        clockList = new ArrayList<>();
                    }
                    clockList.addAll(clocks);
                    for (Clock clock : clocks) {
                        chatItemAdapter.addChatItemModel(new ChatItemModel(message.getFrom().equals(fName) ? 5 : 6,clock));
                    }
                }else {
                    Map<String,String> map = emCustomMessageBody.getParams();
                    List<Task> tasks = (List<Task>) com.alibaba.fastjson.JSONArray.parseArray(map.get("tasks"),Task.class);
                    if (taskList == null) {
                        taskList = new ArrayList<>();
                    }
                    taskList.addAll(tasks);
                    for (Task task : tasks) {
                        chatItemAdapter.addChatItemModel(new ChatItemModel(message.getFrom().equals(fName) ? 3 : 4,task));
                    }
                }
            }else {
                Log.d("imageView", "init: notIsCustomMsg" + message.getTo() + message.getFrom());
                chatItemModel = new ChatItemModel(message.getFrom().equals(fName) ? 1 : 2,((EMTextMessageBody)message.getBody()).getMessage());
                chatItemAdapter.addChatItemModel(chatItemModel);
            }
            Log.d("imageView", "init: null");
        }
        Log.d("imageView", "init: 遍历结束");
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

        //为时钟分享信息item设置监听事件
        chatItemAdapter.setClockItemOnClickListener(new ChatItemAdapter.ClockItemOnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void itemOnClick(int position, Clock clock, int type) {
                //5接收、6发送
                showClockDialog(clock,type);
            }
        });

        //为任务分享信息item设置监听事件
        chatItemAdapter.setTaskItemOnClickListener(new ChatItemAdapter.TaskItemOnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void itemOnClick(int position, Task task, int type) {
                //3接收、4发送
                showTaskDialog(task,type);
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
                sendTextMsg(context);
                break;
            }
            case R.id.addOtherMsgBtn: {
                Log.d("imageView", "onClick: 添加其他点击");
                AlertDialog alertDialog = new AlertDialog.Builder(getContext())
                        .setTitle("分享")
                        .setItems(new String[]{"时钟", "任务"}, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Log.d("imageView", "onClick: which" + which);
                                Bundle bundle = new Bundle();
                                if (which == 0) {
                                    bundle.putString("type","clock");
                                }else {
                                    bundle.putString("type","task");
                                }
                                controller.navigate(R.id.action_chatFragment_to_shareFragment,bundle);
                            }
                        })
                        .setCancelable(true)
                        .create();
                alertDialog.show();
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

            }

            @Override
            public void onProgress(int progress, String status) {

            }

            @Override
            public void onError(int code, String message) {

            }
        });
    }

    @Override
    public void onResume() {
        Log.d("imageView", "onResume: ");
        super.onResume();
        EMClient.getInstance().chatManager().addMessageListener(this);
        String shareType = (String)((MainActivity)requireActivity()).getDataFromMapByKey("shareType");
        //检查是否是从分享页面返回
        if (shareType == null)return;
        if (shareType.equals("clock")) {
            List<Clock> clocks = (List<Clock>)(((MainActivity)requireActivity()).getDataFromMapByKey("clocks"));
            Log.d("imageView", "onResume: " + clocks.size());
            try {
                sendClockMsg(fName,clocks);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }else {
            List<Task> tasks = (List<Task>)(((MainActivity)requireActivity()).getDataFromMapByKey("tasks"));
            Log.d("imageView", "onResume: " + tasks.size());
            try {
                sendTaskMsg(fName,tasks);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onStop() {
        Log.d("imageView", "onStop: ");
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
        Log.d("imageView", "onMessageReceived:数据长度 " + list.size());
        for (EMMessage emMessage : list) {
            try {
                if (emMessage.getBooleanAttribute("isCustomMsg")){
                    String type = ((EMTextMessageBody)emMessage.getBody()).getMessage();
                    if (type.equals("clock")){
                        JSONObject jsonObject = emMessage.getJSONObjectAttribute("clocks");
                        List<Clock> clocks = (List<Clock>) jsonObject.opt("clocks");
                        clockList.addAll(clocks);
                        for (Clock clock : clocks) {
                            chatItemAdapter.addChatItemModel(new ChatItemModel(5,clock));
                        }

                    }else if(type.equals("task")) {
                        JSONObject jsonObject = emMessage.getJSONObjectAttribute("tasks");
                        List<Task> tasks = (List<Task>) jsonObject.opt("tasks");
                        taskList.addAll(tasks);
                        for (Task task : tasks) {
                            chatItemAdapter.addChatItemModel(new ChatItemModel(3,task));
                        }
                    }
                }
            } catch (HyphenateException e) {
                String context = ((EMTextMessageBody)emMessage.getBody()).getMessage();
                ChatItemModel chatItemModel = new ChatItemModel(1,context);
                chatItemAdapter.addChatItemModel(chatItemModel);
                //e.printStackTrace();
            }
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

    //发送文本信息
    private void sendTextMsg(String content) {
        //发送消息
        EMMessage message = EMMessage.createTxtSendMessage(content,fName);
        EMClient.getInstance().chatManager().sendMessage(message);
        message.setAttribute("isCustomMsg",false);
        message.setMessageStatusCallback(new EMCallBack() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onSuccess() {
                Log.d("imageView", "onSuccess: 发送成功" + fName);
                chatItemAdapter.addChatItemModel(new ChatItemModel(2,content));
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
    }


    //发送时钟信息
    private void sendClockMsg(String fName,List<Clock> clocks) throws JSONException {
        EMMessage customMessage = EMMessage.createSendMessage(EMMessage.Type.CUSTOM);
        // event为需要传递的自定义消息事件，比如礼物消息，可以设置event = "gift"
        String event = "clock";
        EMCustomMessageBody customBody = new EMCustomMessageBody(event);
        // params类型为Map<String, String>
        Map<String,String> params = new HashMap<>();
        params.put("clocks",com.alibaba.fastjson.JSONObject.toJSONString(clocks));
        customBody.setParams(params);
        customMessage.addBody(customBody);
        // to指另一方环信id（或者群组id，聊天室id）
        customMessage.setTo(fName);
        customMessage.setAttribute("isCustomMsg",true);
        EMClient.getInstance().chatManager().sendMessage(customMessage);
        customMessage.setMessageStatusCallback(new EMCallBack() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onSuccess() {
                Log.d("imageView", "onSuccess: 发送成功" + fName);
                for (Clock clock : clocks) {
                    chatItemAdapter.addChatItemModel(new ChatItemModel(6,clock));
                }
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
    }

    //发送任务信息
    private void sendTaskMsg(String fName, List<Task> tasks) throws JSONException {
        //发送消息
        EMMessage customMessage = EMMessage.createSendMessage(EMMessage.Type.CUSTOM);
        // event为需要传递的自定义消息事件，比如礼物消息，可以设置event = "gift"
        String event = "task";
        EMCustomMessageBody customBody = new EMCustomMessageBody(event);
        // params类型为Map<String, String>
        Map<String,String> params = new HashMap<>();
        params.put("tasks",com.alibaba.fastjson.JSONObject.toJSONString(tasks));
        customBody.setParams(params);
        customMessage.addBody(customBody);
        // to指另一方环信id（或者群组id，聊天室id）
        customMessage.setTo(fName);
        customMessage.setAttribute("isCustomMsg",true);
        EMClient.getInstance().chatManager().sendMessage(customMessage);
        customMessage.setMessageStatusCallback(new EMCallBack() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onSuccess() {
                Log.d("imageView", "onSuccess: 发送成功" + fName);
                for (Task task : tasks) {
                    chatItemAdapter.addChatItemModel(new ChatItemModel(4,task));
                }
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
    }

    @SuppressLint("SetTextI18n")
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void showClockDialog(Clock clock, int type) {
        View contentView = getLayoutInflater().inflate(R.layout.share_clock_detail_dialog,null);
        AlertDialog alertDialog = new AlertDialog.Builder(requireContext())
                .setView(contentView)
                .create();
        ImageButton closeButton = contentView.findViewById(R.id.shareClockDialogCloseImgBtn);
        TextView clockNameTextView = contentView.findViewById(R.id.shareClockDialogClockName);
        TextView clockMinuteTextView = contentView.findViewById(R.id.shareClockDialogClockMinuteTextView);
        Button saveButton = contentView.findViewById(R.id.shareClockEnterBtn);
        ImageView shareClockStateImageView = contentView.findViewById(R.id.shareClockStateImageView);
        shareClockStateImageView.setVisibility(clock.isState() ? View.VISIBLE : View.GONE);
        clockNameTextView.setText(clock.getTask());
        clockMinuteTextView.setText(clock.getClock_minute() + "分钟");
        LinearLayout linearLayout = contentView.findViewById(R.id.shareClockDialogAlertTimeLinearLayout);
        TextView clockAlertTimeTextView = contentView.findViewById(R.id.shareClockAlertTimeTextView);
        linearLayout.setVisibility(clock.isAlert()? View.VISIBLE : View.GONE);
        clockAlertTimeTextView.setText(DateTimeUtil.longToStrYMDHM(clock.getAlert_time()));
        saveButton.setVisibility(type == 5 ? View.VISIBLE : View.GONE);

        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chatViewModel.saveShareClockToServer(clock).observe(getViewLifecycleOwner(), new Observer<ReturnData<Clock>>() {
                    @Override
                    public void onChanged(ReturnData<Clock> clockReturnData) {
                        if (clockReturnData == null)return;
                        if (clockReturnData.getCode() == RCodeEnum.OK.getCode()) {
                            Toast.makeText(requireContext(),"保存成功",Toast.LENGTH_SHORT).show();
                            chatViewModel.saveShareClockToDB(clockReturnData.getData());
                        }else {
                            Toast.makeText(requireContext(),clockReturnData.getMsg(),Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        alertDialog.show();
    }

    @SuppressLint("SetTextI18n")
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void showTaskDialog(Task task, int type) {
        View contentView = getLayoutInflater().inflate(R.layout.share_task_detail_dialog,null);
        AlertDialog alertDialog = new AlertDialog.Builder(requireContext())
                .setView(contentView)
                .create();
        ImageButton closeButton = contentView.findViewById(R.id.shareTaskDialogCloseImgBtn);
        TextView taskNameTextView = contentView.findViewById(R.id.shareTaskDialogTaskNameTextView);
        TextView taskStartTimeTextView = contentView.findViewById(R.id.shareTaskDialogTaskStartTimeTextView);
        TextView taskEndTimeTextView = contentView.findViewById(R.id.shareTaskEndTimeTextView);
        Button saveButton = contentView.findViewById(R.id.shareTaskEnterBtn);
        ImageView shareTaskStateImageView = contentView.findViewById(R.id.shareTaskStateImageView);
        shareTaskStateImageView.setVisibility(task.isState() ? View.VISIBLE : View.GONE);
        taskNameTextView.setText(task.getTask());
        taskStartTimeTextView.setText(DateTimeUtil.longToStrYMDHM(task.getCreated_at()));
        taskEndTimeTextView.setText(DateTimeUtil.longToStrYMDHM(task.getEnd_time()));

        LinearLayout linearLayout = contentView.findViewById(R.id.shareTaskDialogAlertTimeLinearLayout);
        TextView taskAlertTimeTextView = contentView.findViewById(R.id.shareTaskAlertTimeTextView);
        linearLayout.setVisibility(task.isAlert()? View.VISIBLE : View.GONE);
        taskAlertTimeTextView.setText(DateTimeUtil.longToStrYMDHM(task.getAlert_time()));
        saveButton.setVisibility(type == 3 ? View.VISIBLE : View.GONE);

        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DateTimePickerDialog dateTimePickerDialog = new DateTimePickerDialog(true);
                dateTimePickerDialog.show(fragmentManager,"shareTaskSelectEndTimeDialog");
                dateTimePickerDialog.setEnterClicked(new DateTimePickerDialog.EnterListener() {
                    @Override
                    public void enterBtnOnClicked(String dateTimeStr) {
                        task.setEnd_time(dateTimePickerDialog.getSelectedDateToLong());
                        chatViewModel.saveShareTaskToServer(task).observe(getViewLifecycleOwner(), new Observer<ReturnData<Task>>() {
                            @Override
                            public void onChanged(ReturnData<Task> taskReturnData) {
                                if (taskReturnData == null)return;
                                if (taskReturnData.getCode() == RCodeEnum.OK.getCode()) {
                                    Toast.makeText(requireContext(),"保存成功",Toast.LENGTH_SHORT).show();
                                    chatViewModel.saveShareTaskToDB(taskReturnData.getData());
                                }else {
                                    Toast.makeText(requireContext(),taskReturnData.getMsg(),Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                });

            }
        });

        alertDialog.show();
    }

}