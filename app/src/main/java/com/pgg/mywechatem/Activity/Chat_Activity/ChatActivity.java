package com.pgg.mywechatem.Activity.Chat_Activity;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.PowerManager;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.text.ClipboardManager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.easemob.EMCallBack;
import com.easemob.EMError;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMContactManager;
import com.easemob.chat.EMConversation;
import com.easemob.chat.EMGroupManager;
import com.easemob.chat.EMMessage;
import com.easemob.chat.GroupReomveListener;
import com.easemob.chat.ImageMessageBody;
import com.easemob.chat.LocationMessageBody;
import com.easemob.chat.NormalFileMessageBody;
import com.easemob.chat.TextMessageBody;
import com.easemob.chat.VideoMessageBody;
import com.easemob.chat.VoiceMessageBody;
import com.easemob.exceptions.EaseMobException;
import com.easemob.util.EMLog;
import com.easemob.util.ImageUtils;
import com.easemob.util.PathUtil;
import com.easemob.util.VoiceRecorder;
import com.pgg.mywechatem.Activity.BaseActivity;
import com.pgg.mywechatem.Activity.Find_Activity.ChangeBackgroundActivity;
import com.pgg.mywechatem.Adapter.ChatAdapter.ExpressionAdapter;
import com.pgg.mywechatem.Adapter.ChatAdapter.MessageAdapter;
import com.pgg.mywechatem.Adapter.ChatAdapter.SmileAdapter;
import com.pgg.mywechatem.Global.BaseApplication;
import com.pgg.mywechatem.Listener.VoicePlayClickListener;
import com.pgg.mywechatem.R;
import com.pgg.mywechatem.Uitils.CommonUtils;
import com.pgg.mywechatem.Uitils.Constants;
import com.pgg.mywechatem.Uitils.MPermissionUtils;
import com.pgg.mywechatem.Uitils.SmileUtils;
import com.pgg.mywechatem.Uitils.Utils;
import com.pgg.mywechatem.View.ExpandGridView;
import com.pgg.mywechatem.View.PasteEditText;

import org.apache.http.message.BasicNameValuePair;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by PDD on 2017/11/26.
 */

public class ChatActivity extends BaseActivity implements View.OnClickListener{

    private static final int REQUEST_CODE_EMPTY_HISTORY = 2;
    public static final int REQUEST_CODE_CONTEXT_MENU = 3;
    private static final int REQUEST_CODE_MAP = 4;
    public static final int REQUEST_CODE_TEXT = 5;
    public static final int REQUEST_CODE_VOICE = 6;
    public static final int REQUEST_CODE_PICTURE = 7;
    public static final int REQUEST_CODE_LOCATION = 8;
    public static final int REQUEST_CODE_NET_DISK = 9;
    public static final int REQUEST_CODE_FILE = 10;
    public static final int REQUEST_CODE_COPY_AND_PASTE = 11;
    public static final int REQUEST_CODE_PICK_VIDEO = 12;
    public static final int REQUEST_CODE_DOWNLOAD_VIDEO = 13;
    public static final int REQUEST_CODE_VIDEO = 14;
    public static final int REQUEST_CODE_DOWNLOAD_VOICE = 15;
    public static final int REQUEST_CODE_SELECT_USER_CARD = 16;
    public static final int REQUEST_CODE_SEND_USER_CARD = 17;
    public static final int REQUEST_CODE_CAMERA = 18;
    public static final int REQUEST_CODE_LOCAL = 19;
    public static final int REQUEST_CODE_CLICK_DESTORY_IMG = 20;
    public static final int REQUEST_CODE_GROUP_DETAIL = 21;
    public static final int REQUEST_CODE_SELECT_VIDEO = 23;
    public static final int REQUEST_CODE_SELECT_FILE = 24;
    public static final int REQUEST_CODE_ADD_TO_BLACKLIST = 25;

    public static final int RESULT_CODE_COPY = 1;
    public static final int RESULT_CODE_DELETE = 2;
    public static final int RESULT_CODE_FORWARD = 3;
    public static final int RESULT_CODE_OPEN = 4;
    public static final int RESULT_CODE_DWONLOAD = 5;
    public static final int RESULT_CODE_TO_CLOUD = 6;
    public static final int RESULT_CODE_EXIT_GROUP = 7;

    public static final int CHATTYPE_SINGLE = 1;
    public static final int CHATTYPE_GROUP = 2;
    static int resendPos;
    public String playMsgId;
    public static ChatActivity activityInstance = null;

    private ImageButton ib_exit_01;
    private View vertical_line;
    private TextView title_tv_left;
    private TextView title_tv_center;
    private ImageButton ib_right;
    private int chatType;
    public static final String COPY_IMAGE = "EASEMOBIMG";

    private File cameraFile;
    private VoiceRecorder voiceRecorder;
    private ProgressBar pb_load_more;
    private ListView list_chat;
    private LinearLayout view_talk;//录音动画
    private ImageView mic_image;//录音图片
    private TextView recording_hint;//上滑取消
    private LinearLayout bar_bottom;//底部输入栏总布局
    private LinearLayout rl_bottom;//输入栏内布局
    private Button btn_set_mode_voice;//左边发送语音，点击显示中间发送语音的按钮
    private Button btn_set_mode_keyboard;//用于从发送语音界面切换到键盘界面
    private LinearLayout btn_press_to_speak;//按住说话按钮
    private RelativeLayout edittext_layout;//edittext和表情按钮的容器
    private PasteEditText et_sendmessage;//用于接收从键盘输入放入文字，或者表情
    private ImageView iv_emoticons_normal;//用于显示表情展示框
    private ImageView iv_emoticons_checked;//用于隐藏表情展示框
    private Button btn_more;//用于打开工具栏，发送图片，文件，地理位置...
    private Button btn_send;//用于发送消息按钮
    private LinearLayout more;//表情以及工具栏的显示容器
    private LinearLayout ll_face_container;//表情显示容器
    private LinearLayout ll_btn_container;//工具栏显示容器
    private ViewPager vPager;//表情显示的viewpager，分页展示
    private AnimationDrawable animationDrawable;
    private List<String> reslist;

    private ClipboardManager clipboard;//用于剪贴板功能
    private InputMethodManager manager;//系统软件软键盘管理类
    private PowerManager.WakeLock wakeLock;//管理系统屏幕亮或暗，唤醒屏幕


    private EMConversation conversation;
    private MessageAdapter adapter;

    private String toChatUsername;
    private String Name;
    private NewMessageBroadcastReceiver receiver;
    private GroupListener groupListener;
    private String head_url;

    private boolean isloading;
    private final int pagesize = 20;
    private boolean haveMoreData = true;
    private ProgressBar loadmorePB;


    private Handler micImageHandler = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {
        }
    };
    private String[] filePathColumn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.activity_chat);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onResume() {
        super.onResume();
        adapter.refresh();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (wakeLock.isHeld())
            wakeLock.release();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        activityInstance = null;
        EMGroupManager.getInstance().removeGroupChangeListener(groupListener);
        // 注销广播
        try {
            unregisterReceiver(receiver);
            receiver = null;
        } catch (Exception e) {
        }
        try {
            unregisterReceiver(ackMessageReceiver);
            ackMessageReceiver = null;
            unregisterReceiver(deliveryAckMessageReceiver);
            deliveryAckMessageReceiver = null;
        } catch (Exception e) {
        }
    }

    /**
     * 设置系统返回键
     *
     * @param keyCode
     * @param event
     * @return
     */

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Utils.finish(this);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void initController() {
        ib_exit_01 = findViewById(R.id.ib_exit_01);
        vertical_line = findViewById(R.id.vertical_line);
        title_tv_left = findViewById(R.id.title_tv_left);
        title_tv_center = findViewById(R.id.title_tv_center);
        ib_right = findViewById(R.id.ib_right);
        pb_load_more = findViewById(R.id.pb_load_more);
        list_chat = findViewById(R.id.list);
        view_talk = findViewById(R.id.view_talk);
        mic_image = findViewById(R.id.mic_image);
        recording_hint = findViewById(R.id.recording_hint);
        bar_bottom = findViewById(R.id.bar_bottom);
        rl_bottom = findViewById(R.id.rl_bottom);
        btn_set_mode_voice = findViewById(R.id.btn_set_mode_voice);
        btn_set_mode_keyboard = findViewById(R.id.btn_set_mode_keyboard);
        btn_press_to_speak = findViewById(R.id.btn_press_to_speak);
        edittext_layout = findViewById(R.id.edittext_layout);
        et_sendmessage = findViewById(R.id.et_sendmessage);
        iv_emoticons_normal = findViewById(R.id.iv_emoticons_normal);
        iv_emoticons_checked = findViewById(R.id.iv_emoticons_checked);
        btn_more = findViewById(R.id.btn_more);
        btn_send = findViewById(R.id.btn_send);
        more = findViewById(R.id.more);
        ll_face_container = findViewById(R.id.ll_face_container);
        ll_btn_container = findViewById(R.id.ll_btn_container);
        vPager = findViewById(R.id.vPager);
        loadmorePB = findViewById(R.id.pb_load_more);
        iv_emoticons_normal.setVisibility(View.VISIBLE);
        iv_emoticons_checked.setVisibility(View.INVISIBLE);
        edittext_layout.setBackgroundResource(R.drawable.input_bar_bg_normal);
        animationDrawable = (AnimationDrawable) mic_image.getBackground();//初始化录音动画
        animationDrawable.setOneShot(false);
    }

    @Override
    public void initView() {
        ib_exit_01.setVisibility(View.VISIBLE);
        vertical_line.setVisibility(View.VISIBLE);
        title_tv_left.setVisibility(View.VISIBLE);
        title_tv_center.setVisibility(View.GONE);
        ib_right.setVisibility(View.VISIBLE);
        ib_right.setBackgroundResource(R.drawable.chat_friend_info);
        ib_exit_01.setBackgroundResource(R.drawable.ic_menu_exit_left);
        activityInstance=this;
        // 表情list
        reslist = getExpressionRes(62);
        // 初始化表情viewpager
        List<View> views = new ArrayList<View>();
        View gv1 = getGridChildView(1);
        View gv2 = getGridChildView(2);
        View gv3 = getGridChildView(3);
        views.add(gv1);
        views.add(gv2);
        views.add(gv3);
        vPager.setAdapter(new SmileAdapter(views));
        initReceiver();
    }

    @Override
    public void initData() {
        // 判断单聊还是群聊
        chatType = getIntent().getIntExtra(Constants.TYPE, CHATTYPE_SINGLE);
        Name = getIntent().getStringExtra(Constants.NAME);
        toChatUsername = getIntent().getStringExtra(Constants.User_ID);
        head_url=getIntent().getStringExtra(Constants.HEAD_URL);
        if (chatType == CHATTYPE_SINGLE) { // 单聊
            if (TextUtils.isEmpty(Name)) {
                //TODO 如果对方没有设置昵称，则将id作为显示头
                title_tv_left.setText(toChatUsername);
            } else {
                title_tv_left.setText(Name);
            }
        } else {
            // 群聊
            if (TextUtils.isEmpty(Name)) {
                //TODO 如果群名没有设置，则去自己服务器加载数据
                title_tv_left.setText("群聊");
            } else {
                title_tv_left.setText(Name);
            }
            findViewById(R.id.view_location_video).setVisibility(View.GONE);
            toChatUsername = getIntent().getStringExtra(Constants.GROUP_ID);
        }
        conversation = EMChatManager.getInstance().getConversation(toChatUsername);
        // 把此会话的未读数置为0
        conversation.resetUnreadMsgCount();
        adapter = new MessageAdapter(this, toChatUsername, chatType);
        // 显示消息
        list_chat.setAdapter(adapter);
        list_chat.setOnScrollListener(new ListScrollListener());
        int count = list_chat.getCount();
        if (count > 0) {
            list_chat.setSelection(count);
        }
        list_chat.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                hideKeyboard();
                more.setVisibility(View.GONE);
                iv_emoticons_normal.setVisibility(View.VISIBLE);
                iv_emoticons_checked.setVisibility(View.INVISIBLE);
                ll_face_container.setVisibility(View.GONE);
                ll_btn_container.setVisibility(View.GONE);
                return false;
            }
        });
    }

    @Override
    public void initListener() {
        ib_exit_01.setOnClickListener(this);
        ib_right.setOnClickListener(this);
        et_sendmessage.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                //监听EditText的焦点变化，根据焦点变换背景图
                if (hasFocus) {
                    edittext_layout.setBackgroundResource(R.drawable.input_bar_bg_active);
                } else {
                    edittext_layout.setBackgroundResource(R.drawable.input_bar_bg_normal);
                }
            }
        });

        et_sendmessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //监听EditText的点击事件，当点击之后，变换背景图，隐藏表情框和工具栏，只显示软键盘
                edittext_layout.setBackgroundResource(R.drawable.input_bar_bg_active);
                more.setVisibility(View.GONE);
                iv_emoticons_normal.setVisibility(View.VISIBLE);
                iv_emoticons_checked.setVisibility(View.INVISIBLE);
                ll_face_container.setVisibility(View.GONE);
                ll_btn_container.setVisibility(View.GONE);
            }
        });

        /**
         * 按住说话的触摸事件
         */
        voiceRecorder = new VoiceRecorder(micImageHandler);//初始化一个录音
        btn_press_to_speak.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN://当按钮按下时
                        animationDrawable.start();//动画开始
                        if (!CommonUtils.isExitsSdcard()) {//判断是否有sd卡
                            String st4 = getResources().getString(
                                    R.string.Send_voice_need_sdcard_support);
                            Toast.makeText(ChatActivity.this, st4, Toast.LENGTH_SHORT).show();
                            return false;
                        }
                        try {
                            v.setPressed(true);//将按钮状态设置为按下
                            wakeLock.acquire();//唤醒屏幕
                            if (VoicePlayClickListener.isPlaying)//如果有录音在工作
                                VoicePlayClickListener.currentPlayListener.stopPlayVoice();//停止当前正在工作的录音器
                            view_talk.setVisibility(View.VISIBLE);//显示录音图片
                            recording_hint.setText(getString(R.string.move_up_to_cancel));//设置"上滑取消录音"文字
                            recording_hint.setBackgroundColor(Color.TRANSPARENT);//设置背景色
                            voiceRecorder.startRecording(null, toChatUsername, getApplicationContext());//开始记录录音
                        } catch (Exception e) {//如果出现异常
                            e.printStackTrace();
                            v.setPressed(false);//取消按钮按下状态
                            if (wakeLock.isHeld())
                                wakeLock.release();//释放wakelock
                            if (voiceRecorder != null)
                                voiceRecorder.discardRecording();//释放记录的录音资源
                            view_talk.setVisibility(View.INVISIBLE);//隐藏录音动画
                            Toast.makeText(ChatActivity.this, R.string.recoding_fail, Toast.LENGTH_SHORT).show();
                            return false;
                        }
                        return true;
                    case MotionEvent.ACTION_MOVE: {//当在按下说话按钮中移动时
                        if (event.getY() < 0) {//如果是向上滑
                            recording_hint.setText(getString(R.string.release_to_cancel));
                            recording_hint.setBackgroundResource(R.drawable.recording_text_hint_bg);//文字变红
                        } else {//向下滑
                            recording_hint.setText(getString(R.string.move_up_to_cancel));
                            recording_hint.setBackgroundColor(Color.TRANSPARENT);
                            animationDrawable.start();
                        }
                        return true;
                    }
                    case MotionEvent.ACTION_UP://手指抬起
                        if (animationDrawable.isRunning()) {//如果动画正在播放
                            animationDrawable.stop();//停止动画
                        }
                        v.setPressed(false);
                        view_talk.setVisibility(View.INVISIBLE);
                        if (wakeLock.isHeld())
                            wakeLock.release();
                        if (event.getY() < 0) {
                            //抬起时向上滑
                        } else {
                            // stop recording and send voice file
                            String st1 = getResources().getString(R.string.Recording_without_permission);
                            String st2 = getResources().getString(R.string.The_recording_time_is_too_short);
                            String st3 = getResources().getString(R.string.send_failure_please);
                            try {
                                int length = voiceRecorder.stopRecoding();
                                if (length > 0) {//如果录音有效
                                    sendVoice(voiceRecorder.getVoiceFilePath(),
                                            voiceRecorder.getVoiceFileName(toChatUsername),
                                            Integer.toString(length), false);//发送录音
                                } else if (length == EMError.INVALID_FILE) {
                                    Toast.makeText(getApplicationContext(), st1, Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(getApplicationContext(), st2, Toast.LENGTH_SHORT).show();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                                Toast.makeText(ChatActivity.this, st3, Toast.LENGTH_SHORT).show();
                            }

                        }
                        return true;
                    default:
                        view_talk.setVisibility(View.INVISIBLE);
                        if (voiceRecorder != null)
                            voiceRecorder.discardRecording();
                        return false;
                }
            }
        });
        // 监听文字框,当有文字时，隐藏加号，显示发送按钮
        et_sendmessage.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!TextUtils.isEmpty(s)) {
                    btn_more.setVisibility(View.GONE);
                    btn_send.setVisibility(View.VISIBLE);
                } else {
                    btn_more.setVisibility(View.VISIBLE);
                    btn_send.setVisibility(View.GONE);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        iv_emoticons_normal.setOnClickListener(this);
        iv_emoticons_checked.setOnClickListener(this);

        //工具栏各item的点击事件注册
        findViewById(R.id.view_camera).setOnClickListener(this);
        findViewById(R.id.view_file).setOnClickListener(this);
        findViewById(R.id.view_video).setOnClickListener(this);
        findViewById(R.id.view_photo).setOnClickListener(this);
        findViewById(R.id.view_location).setOnClickListener(this);
        findViewById(R.id.view_audio).setOnClickListener(this);

        clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);//获取系统剪贴服务
        manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);//获取系统软键盘管理类
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        wakeLock = ((PowerManager) getSystemService(Context.POWER_SERVICE)).newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK, "demo");//初始化一个wakelock

    }

    /**
     * 显示语音图标按钮
     * 最左侧语音图标点击事件
     *
     * @param view
     */
    public void setModeVoice(View view) {
        hideKeyboard();
        edittext_layout.setVisibility(View.GONE);
        more.setVisibility(View.GONE);
        view.setVisibility(View.GONE);
        btn_set_mode_keyboard.setVisibility(View.VISIBLE);
        btn_send.setVisibility(View.GONE);
        btn_more.setVisibility(View.VISIBLE);
        btn_press_to_speak.setVisibility(View.VISIBLE);
        iv_emoticons_normal.setVisibility(View.VISIBLE);
        iv_emoticons_checked.setVisibility(View.INVISIBLE);
        ll_btn_container.setVisibility(View.VISIBLE);
        ll_face_container.setVisibility(View.GONE);

    }


    /**
     * 得到表情图片的drawable的id  R.drawable.f_static_0
     * @param getSum
     * @return
     */
    public List<String> getExpressionRes(int getSum) {
        List<String> reslist = new ArrayList<String>();
        for (int x = 0; x <= getSum; x++) {
            String filename = "f_static_0" + x;
            reslist.add(filename);
        }
        return reslist;
    }

    /**
     * 获取表情的viewPager的子GridView
     * 并设置表情的点击事件在EditText中显示
     * @param i
     * @return
     */
    private View getGridChildView(int i) {
        View view = Utils.getView(R.layout.expression_gridview);
        ExpandGridView gv = view.findViewById(R.id.gridview);
        List<String> list = new ArrayList<String>();
        if (i == 1) {
            List<String> list1 = reslist.subList(0, 21);
            list.addAll(list1);
        } else if (i == 2) {
            list.addAll(reslist.subList(21, reslist.size()));
        } else if (i == 3) {
            list.addAll(reslist.subList(42, reslist.size()));
        }
        list.add("delete_expression");
        final ExpressionAdapter expressionAdapter = new ExpressionAdapter(this, 1, list);
        gv.setAdapter(expressionAdapter);
        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String filename = expressionAdapter.getItem(position);
                try {
                    // 文字输入框可见时，才可输入表情
                    // 按住说话可见，不让输入表情
                    if (btn_set_mode_keyboard.getVisibility() != View.VISIBLE) {
                        if (filename != "delete_expression") { // 不是删除键，显示表情
                            // 这里用的反射，所以混淆的时候不要混淆SmileUtils这个类
                            Class clz = Class.forName("com.pgg.mywechatem.Uitils.SmileUtils");
                            Field field = clz.getField(filename);
                            et_sendmessage.append(SmileUtils.getSmiledText(ChatActivity.this, (String) field.get(null)));
                        } else { // 删除文字或者表情
                            if (!TextUtils.isEmpty(et_sendmessage.getText())) {
                                int selectionStart = et_sendmessage.getSelectionStart();// 获取光标的位置
                                if (selectionStart > 0) {
                                    String body = et_sendmessage.getText().toString();
                                    String tempStr = body.substring(0, selectionStart);
                                    int i = tempStr.lastIndexOf("[");// 获取最后一个表情的位置
                                    if (i != -1) {
                                        CharSequence cs = tempStr.substring(i, selectionStart);
                                        if (SmileUtils.containsKey(cs.toString()))
                                            et_sendmessage.getEditableText().delete(i, selectionStart);
                                        else
                                            et_sendmessage.getEditableText().delete(selectionStart - 1, selectionStart);
                                    } else {
                                        et_sendmessage.getEditableText().delete(selectionStart - 1, selectionStart);
                                    }
                                }
                            }
                        }
                    }
                } catch (Exception e) {
                }

            }
        });
        return view;
    }

    /**
     * listview滑动监听listener
     *
     */
    private class ListScrollListener implements AbsListView.OnScrollListener {

        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {
            switch (scrollState) {
                case AbsListView.OnScrollListener.SCROLL_STATE_IDLE:
                    if (view.getFirstVisiblePosition() == 0 && !isloading && haveMoreData) {
                        loadmorePB.setVisibility(View.VISIBLE);
                        // sdk初始化加载的聊天记录为20条，到顶时去db里获取更多
                        List<EMMessage> messages;
                        try {
                            // 获取更多messges，调用此方法的时候从db获取的messages
                            // sdk会自动存入到此conversation中
                            if (chatType == CHATTYPE_SINGLE)
                                messages = conversation.loadMoreMsgFromDB(adapter.getItem(0).getMsgId(), pagesize);
                            else
                                messages = conversation.loadMoreGroupMsgFromDB(adapter.getItem(0).getMsgId(), pagesize);
                        } catch (Exception e1) {
                            loadmorePB.setVisibility(View.GONE);
                            return;
                        }
                        try {
                            Thread.sleep(300);
                        } catch (InterruptedException e) {
                        }
                        if (messages.size() != 0) {
                            // 刷新ui
                            adapter.notifyDataSetChanged();
                            list_chat.setSelection(messages.size() - 1);
                            if (messages.size() != pagesize)
                                haveMoreData = false;
                        } else {
                            haveMoreData = false;
                        }
                        loadmorePB.setVisibility(View.GONE);
                        isloading = false;

                    }
                    break;
            }
        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem,
                             int visibleItemCount, int totalItemCount) {

        }

    }

    @Override
    protected void onNewIntent(Intent intent) {
        // 点击notification bar进入聊天页面，保证只有一个聊天页面
        String username = intent.getStringExtra("userId");
        if (toChatUsername.equals(username))
            super.onNewIntent(intent);
        else {
            finish();
            startActivity(intent);
        }

    }

    /**
     * 显示或隐藏图标按钮页
     * 最右侧加号点击事件
     *
     * @param view
     */
    public void more(View view) {
        if (more.getVisibility() == View.GONE) {
            System.out.println("more gone");
            hideKeyboard();
            more.setVisibility(View.VISIBLE);
            ll_btn_container.setVisibility(View.VISIBLE);
            ll_face_container.setVisibility(View.GONE);
        } else {
            if (ll_face_container.getVisibility() == View.VISIBLE) {
                ll_face_container.setVisibility(View.GONE);
                ll_btn_container.setVisibility(View.VISIBLE);
                iv_emoticons_normal.setVisibility(View.VISIBLE);
                iv_emoticons_checked.setVisibility(View.INVISIBLE);
            } else {
                more.setVisibility(View.GONE);
            }
        }
    }


    /**
     *
     * 点击文字输入框
     *
     * @param v
     */
    public void editClick(View v) {
        list_chat.setSelection(list_chat.getCount() - 1);
        if (more.getVisibility() == View.VISIBLE) {
            more.setVisibility(View.GONE);
            iv_emoticons_normal.setVisibility(View.VISIBLE);
            iv_emoticons_checked.setVisibility(View.INVISIBLE);
        }
    }

    /**
     * 显示键盘图标
     * 最左侧键盘图标点击事件
     *
     * @param view
     */
    public void setModeKeyboard(View view) {
        edittext_layout.setVisibility(View.VISIBLE);
        more.setVisibility(View.GONE);
        view.setVisibility(View.GONE);
        btn_set_mode_voice.setVisibility(View.VISIBLE);
        // mEditTextContent.setVisibility(View.VISIBLE);
        et_sendmessage.requestFocus();
        // buttonSend.setVisibility(View.VISIBLE);
        btn_press_to_speak.setVisibility(View.GONE);
        if (TextUtils.isEmpty(et_sendmessage.getText())) {
            btn_more.setVisibility(View.VISIBLE);
            btn_send.setVisibility(View.GONE);
        } else {
            btn_more.setVisibility(View.GONE);
            btn_send.setVisibility(View.VISIBLE);
        }

    }

    /**
     * 隐藏软键盘
     */
    private void hideKeyboard() {
        if (getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
            if (getCurrentFocus() != null)
                manager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }


    public String  getToChatUsername() {
        return toChatUsername;
    }

    public String getName(){
        return Name;
    }

    public String getHead_url(){
        return head_url;
    }

    private void initReceiver(){
        // 注册接收消息广播
        receiver = new NewMessageBroadcastReceiver();
        IntentFilter intentFilter = new IntentFilter(EMChatManager.getInstance().getNewMessageBroadcastAction());
        // 设置广播的优先级别大于Mainacitivity,这样如果消息来的时候正好在chat页面，直接显示消息，而不是提示消息未读
        intentFilter.setPriority(5);
        registerReceiver(receiver, intentFilter);

        // 注册一个ack回执消息的BroadcastReceiver
        IntentFilter ackMessageIntentFilter = new IntentFilter(EMChatManager.getInstance().getAckMessageBroadcastAction());
        ackMessageIntentFilter.setPriority(5);
        registerReceiver(ackMessageReceiver, ackMessageIntentFilter);

        // 监听当前会话的群聊解散被T事件
        groupListener = new GroupListener();
        EMGroupManager.getInstance().addGroupChangeListener(groupListener);

        //消息送达事件
        EMChatManager.getInstance().getChatOptions().setRequireDeliveryAck(true);
        //如果用到已发送的回执需要把这个flag设置成true
        IntentFilter deliveryAckMessageIntentFilter = new IntentFilter(EMChatManager.getInstance().getDeliveryAckMessageBroadcastAction());
        deliveryAckMessageIntentFilter.setPriority(5);
        registerReceiver(deliveryAckMessageReceiver, deliveryAckMessageIntentFilter);
    }

    @Override
    public void onClick(View v) {
        hideKeyboard();//隐藏软键盘
        switch (v.getId()) {
            case R.id.ib_exit_01:
                Utils.finish(ChatActivity.this);
                break;
            case R.id.ib_right:
                //查看会话详细信息，是好友的话，点击查看好友详细信息，是群聊，点击查看群聊信息，跳转活动
                if (chatType == CHATTYPE_SINGLE) { // 单聊
                    Utils.start_Activity(this, FriendMsgActivity.class, new BasicNameValuePair(Constants.User_ID, toChatUsername), new BasicNameValuePair(Constants.NAME, Name));
                } else {
                    // TODO 打开群组详情页面
                    Utils.start_Activity(this, GroupSettingActivity.class,
                            new BasicNameValuePair(Constants.GROUP_ID, toChatUsername), new BasicNameValuePair(Constants.NAME, Name));
                    BaseApplication.getBaseApplication().addActivity(ChatActivity.this);
                }
                break;
            case R.id.view_camera:
                //工具栏拍照点击事件
                MoreLayoutCamera();
                break;
            case R.id.view_file:
                //工具栏文件点击事件
                MoreLayoutFile();
                break;
            case R.id.view_video:
                //工具栏视频通话点击事件
                MoreLayoutVideo();
                break;
            case R.id.view_photo:
                //工具栏照片点击事件
                MoreLayoutPhoto();
                break;
            case R.id.view_location:
                //工具栏发送位置点击事件
                MoreLayoutLocation();
                break;
            case R.id.view_audio:
                //工具栏语音电话点击事件
                MoreLayoutAudio();
                break;
            case R.id.iv_emoticons_normal:
                //打开表情框按钮
                more.setVisibility(View.VISIBLE);
                iv_emoticons_normal.setVisibility(View.INVISIBLE);
                iv_emoticons_checked.setVisibility(View.VISIBLE);
                ll_btn_container.setVisibility(View.GONE);
                ll_face_container.setVisibility(View.VISIBLE);
                hideKeyboard();
                break;
            case R.id.iv_emoticons_checked:
                //关闭表情框按钮
                iv_emoticons_normal.setVisibility(View.VISIBLE);
                iv_emoticons_checked.setVisibility(View.INVISIBLE);
                ll_btn_container.setVisibility(View.VISIBLE);
                ll_face_container.setVisibility(View.GONE);
                more.setVisibility(View.GONE);
                break;
            case R.id.btn_send:
                //发送消息
                String message = et_sendmessage.getText().toString();
                sendMessage(message);
                break;
        }
    }

    //发送消息，发送按钮的点击事件
    private void sendMessage(String content) {
        if (content.length() > 0) {
            EMMessage message = EMMessage.createSendMessage(EMMessage.Type.TXT);
            // 如果是群聊，设置chattype,默认是单聊
            if (chatType == CHATTYPE_GROUP) message.setChatType(EMMessage.ChatType.GroupChat);
            TextMessageBody txtBody = new TextMessageBody(content);
            // 设置消息body
            message.addBody(txtBody);
            // 设置要发给谁,用户username或者群聊groupid
            message.setReceipt(toChatUsername);
            // 把messgage加到conversation中
            conversation.addMessage(message);
            // 通知adapter有消息变动，adapter会根据加入的这条message显示消息和调用sdk的发送方法
            adapter.refresh();
            list_chat.setSelection(list_chat.getCount() - 1);
            et_sendmessage.setText("");
            setResult(RESULT_OK);
        }
    }
    /**
     * 重发消息
     */
    private void resendMessage() {
        EMMessage msg = null;
        msg = conversation.getMessage(resendPos);
        // msg.setBackSend(true);
        msg.status = EMMessage.Status.CREATE;

        adapter.refresh();
        list_chat.setSelection(resendPos);
    }

    private void MoreLayoutAudio() {
        //发起语音通话
        // 语音通话
        if (!EMChatManager.getInstance().isConnected())
            Toast.makeText(this, Constants.NET_ERROR, Toast.LENGTH_SHORT).show();
        else
            startActivity(new Intent(ChatActivity.this, VoiceCallActivity.class).putExtra("username", toChatUsername).putExtra("isComingCall", false));

    }

    /**
     * 发送语音
     *
     * @param filePath
     * @param fileName
     * @param length
     * @param isResend
     */
    private void sendVoice(String filePath, String fileName, String length, boolean isResend) {
        if (!(new File(filePath).exists())) {
            return;
        }
        try {
            final EMMessage message = EMMessage.createSendMessage(EMMessage.Type.VOICE);
            // 如果是群聊，设置chattype,默认是单聊
            if (chatType == CHATTYPE_GROUP)
                message.setChatType(EMMessage.ChatType.GroupChat);
            message.setReceipt(toChatUsername);
            int len = Integer.parseInt(length);
            VoiceMessageBody body = new VoiceMessageBody(new File(filePath), len);
            message.addBody(body);
            conversation.addMessage(message);
            adapter.refresh();
            list_chat.setSelection(list_chat.getCount() - 1);
            setResult(RESULT_OK);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void MoreLayoutLocation() {
        //发送位置
        startActivityForResult(new Intent(this, BaiduMapActivity.class), REQUEST_CODE_MAP);
    }


    /**
     * 发送位置信息
     *
     * @param latitude
     * @param longitude
     * @param imagePath
     * @param locationAddress
     */
    private void sendLocationMsg(double latitude, double longitude, String imagePath, String locationAddress) {
        EMMessage message = EMMessage.createSendMessage(EMMessage.Type.LOCATION);
        // 如果是群聊，设置chattype,默认是单聊
        if (chatType == CHATTYPE_GROUP)
            message.setChatType(EMMessage.ChatType.GroupChat);
        LocationMessageBody locBody = new LocationMessageBody(locationAddress, latitude, longitude);
        message.addBody(locBody);
        message.setReceipt(toChatUsername);
        conversation.addMessage(message);
        list_chat.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        list_chat.setSelection(list_chat.getCount() - 1);
        setResult(RESULT_OK);
    }

    /**
     * 发送视频消息
     */
    private void sendVideo(final String filePath, final String thumbPath, final int length) {
        final File videoFile = new File(filePath);
        if (!videoFile.exists()) {
            return;
        }
        try {
            EMMessage message = EMMessage
                    .createSendMessage(EMMessage.Type.VIDEO);
            // 如果是群聊，设置chattype,默认是单聊
            if (chatType == CHATTYPE_GROUP)
                message.setChatType(EMMessage.ChatType.GroupChat);
            String to = toChatUsername;
            message.setReceipt(to);
            VideoMessageBody body = new VideoMessageBody(videoFile, thumbPath,
                    length, videoFile.length());
            message.addBody(body);
            conversation.addMessage(message);
            list_chat.setAdapter(adapter);
            adapter.refresh();
            list_chat.setSelection(list_chat.getCount() - 1);
            setResult(RESULT_OK);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void MoreLayoutPhoto() {
        //选择图片
        Intent intent;
        if (Build.VERSION.SDK_INT < 19) {
            intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
        } else {
            intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        }
        startActivityForResult(intent, REQUEST_CODE_LOCAL);

    }
    /**
     * 根据图库图片uri发送图片
     *
     * @param selectedImage
     */
    private void sendPicByUri(Uri selectedImage) {
        MPermissionUtils.requestPermissionsResult(this, 1, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}
                , new MPermissionUtils.OnPermissionListener() {
                    @Override
                    public void onPermissionGranted() {
                        filePathColumn = new String[]{ MediaStore.Images.Media.DATA };
                    }

                    @Override
                    public void onPermissionDenied() {
                        MPermissionUtils.showTipsDialog(ChatActivity.this);
                    }
                });
        Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
        String st8 = getResources().getString(R.string.cant_find_pictures);
        if (cursor != null) {
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex("_data");
            String picturePath = cursor.getString(columnIndex);
            cursor.close();
            cursor = null;

            if (picturePath == null || picturePath.equals("null")) {
                Toast toast = Toast.makeText(this, st8, Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
                return;
            }
            sendPicture(picturePath);
        } else {
            File file = new File(selectedImage.getPath());
            if (!file.exists()) {
                Toast toast = Toast.makeText(this, st8, Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
                return;

            }
            sendPicture(file.getAbsolutePath());
        }

    }
    /**
     * 发送图片
     *
     * @param filePath
     */
    private void sendPicture(final String filePath) {
        String to = toChatUsername;
        // create and add image message in view
        final EMMessage message = EMMessage.createSendMessage(EMMessage.Type.IMAGE);
        // 如果是群聊，设置chattype,默认是单聊
        if (chatType == CHATTYPE_GROUP)
            message.setChatType(EMMessage.ChatType.GroupChat);
        message.setReceipt(to);
        ImageMessageBody body = new ImageMessageBody(new File(filePath));
        // 默认超过100k的图片会压缩后发给对方，可以设置成发送原图
        // body.setSendOriginalImage(true);
        message.addBody(body);
        conversation.addMessage(message);

        list_chat.setAdapter(adapter);
        adapter.refresh();
        list_chat.setSelection(list_chat.getCount() - 1);
        setResult(RESULT_OK);
        // more(more);
    }

    private void MoreLayoutVideo() {
        //视频通话
        if (!EMChatManager.getInstance().isConnected())
            Toast.makeText(this, Constants.NET_ERROR, Toast.LENGTH_LONG).show();
        else
            startActivity(new Intent(this, VideoCallActivity.class).putExtra("username", toChatUsername).putExtra("isComingCall", false));
    }

    private void MoreLayoutFile() {
        //发送文件
        Intent intent = null;
        if (Build.VERSION.SDK_INT < 19) {
            intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("*/*");
            intent.addCategory(Intent.CATEGORY_OPENABLE);

        } else {
            intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        }
        startActivityForResult(intent, REQUEST_CODE_SELECT_FILE);
    }

    /**
     * 发送文件
     *
     * @param uri
     */
    private void sendFile(Uri uri) {
        String filePath = null;
        if ("content".equalsIgnoreCase(uri.getScheme())) {
            String[] projection = { "_data" };
            Cursor cursor = null;

            try {
                cursor = getContentResolver().query(uri, projection, null,
                        null, null);
                int column_index = cursor.getColumnIndexOrThrow("_data");
                if (cursor.moveToFirst()) {
                    filePath = cursor.getString(column_index);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            filePath = uri.getPath();
        }
        File file = new File(filePath);
        if (file == null || !file.exists()) {
            String st7 = getResources().getString(R.string.File_does_not_exist);
            Toast.makeText(getApplicationContext(), st7, Toast.LENGTH_SHORT).show();
            return;
        }
        if (file.length() > 10 * 1024 * 1024) {
            String st6 = getResources().getString(
                    R.string.The_file_is_not_greater_than_10_m);
            Toast.makeText(getApplicationContext(), st6, Toast.LENGTH_SHORT).show();
            return;
        }

        // 创建一个文件消息
        EMMessage message = EMMessage.createSendMessage(EMMessage.Type.FILE);
        // 如果是群聊，设置chattype,默认是单聊
        if (chatType == CHATTYPE_GROUP)
            message.setChatType(EMMessage.ChatType.GroupChat);

        message.setReceipt(toChatUsername);
        // add message body
        NormalFileMessageBody body = new NormalFileMessageBody(new File(
                filePath));
        message.addBody(body);
        conversation.addMessage(message);
        list_chat.setAdapter(adapter);
        adapter.refresh();
        list_chat.setSelection(list_chat.getCount() - 1);
        setResult(RESULT_OK);
    }


    private void MoreLayoutCamera() {
        //打开照相机
        if (!CommonUtils.isExitsSdcard()) {
            String st = getResources().getString(R.string.sd_card_does_not_exist);
            Toast.makeText(getApplicationContext(), st, Toast.LENGTH_SHORT).show();
            return;
        }

        cameraFile = new File(PathUtil.getInstance().getImagePath(), "Walk" + System.currentTimeMillis() + ".jpg");
        cameraFile.getParentFile().mkdirs();
        MPermissionUtils.requestPermissionsResult(this, 1, new String[]{Manifest.permission.CAMERA}
                , new MPermissionUtils.OnPermissionListener() {
                    @Override
                    public void onPermissionGranted() {
                        startActivityForResult(new Intent(MediaStore.ACTION_IMAGE_CAPTURE).putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(cameraFile)), REQUEST_CODE_CAMERA);
                    }

                    @Override
                    public void onPermissionDenied() {
                        MPermissionUtils.showTipsDialog(ChatActivity.this);
                    }
                });
    }

    /**
     * 加入到黑名单
     *
     * @param username
     */
    private void addUserToBlacklist(String username) {
        String st11 = getResources().getString(R.string.Move_into_blacklist_success);
        String st12 = getResources().getString(R.string.Move_into_blacklist_failure);
        try {
            EMContactManager.getInstance().addUserToBlackList(username, false);
            Toast.makeText(getApplicationContext(), st11, Toast.LENGTH_SHORT).show();
        } catch (EaseMobException e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), st12, Toast.LENGTH_SHORT).show();
        }
    }


    /**
     * 消息广播接收者
     *
     */
    private class NewMessageBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            // 记得把广播给终结掉
            abortBroadcast();
            String username = intent.getStringExtra("from");
            String msgid = intent.getStringExtra("msgid");
            // 收到这个广播的时候，message已经在db和内存里了，可以通过id获取mesage对象
            EMMessage message = EMChatManager.getInstance().getMessage(msgid);
            // 如果是群聊消息，获取到group id
            if (message.getChatType() == EMMessage.ChatType.GroupChat) {
                username = message.getTo();
            }
            if (!username.equals(toChatUsername)) {
                // 消息不是发给当前会话，return
                // notifyNewMessage(message);
                return;
            }
            // conversation =
            // EMChatManager.getInstance().getConversation(toChatUsername);
            // 通知adapter有新消息，更新ui
            adapter.refresh();
            list_chat.setSelection(list_chat.getCount() - 1);
        }
    }

    /**
     * 消息回执BroadcastReceiver
     */
    private BroadcastReceiver ackMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            abortBroadcast();
            String msgid = intent.getStringExtra("msgid");
            String from = intent.getStringExtra("from");
            EMConversation conversation = EMChatManager.getInstance().getConversation(from);
            if (conversation != null) {
                // 把message设为已读
                EMMessage msg = conversation.getMessage(msgid);
                if (msg != null) {
                    msg.isAcked = true;
                }
            }
            adapter.notifyDataSetChanged();

        }
    };

    /**
     * 消息送达BroadcastReceiver
     */
    private BroadcastReceiver deliveryAckMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            abortBroadcast();
            String msgid = intent.getStringExtra("msgid");
            String from = intent.getStringExtra("from");
            EMConversation conversation = EMChatManager.getInstance().getConversation(from);
            if (conversation != null) {
                // 把message设为已读
                EMMessage msg = conversation.getMessage(msgid);
                if (msg != null) {
                    msg.isDelivered = true;
                }
            }

            adapter.notifyDataSetChanged();
        }
    };

    /**
     * 监测群组解散或者被T事件
     *
     */
    class GroupListener extends GroupReomveListener {

        @Override
        public void onUserRemoved(final String groupId, String groupName) {
            runOnUiThread(new Runnable() {
                String st13 = getResources().getString(R.string.you_are_group);
                public void run() {
                    if (toChatUsername.equals(groupId)) {
                        Toast.makeText(ChatActivity.this, st13, Toast.LENGTH_SHORT).show();
                        // if (GroupDeatilActivity.instance != null)
                        // GroupDeatilActivity.instance.finish();
                        finish();
                    }
                }
            });
        }

        @Override
        public void onGroupDestroy(final String groupId, String groupName) {
            // 群组解散正好在此页面，提示群组被解散，并finish此页面
            runOnUiThread(new Runnable() {
                String st14 = getResources().getString(R.string.the_current_group);
                public void run() {
                    if (toChatUsername.equals(groupId)) {
                        Toast.makeText(ChatActivity.this, st14, Toast.LENGTH_SHORT).show();
                        // if (GroupDeatilActivity.instance != null)
                        // GroupDeatilActivity.instance.finish();
                        finish();
                    }
                }
            });
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ll_btn_container.setVisibility(View.GONE);
        list_chat.setSelection(list_chat.getCount());
        if (resultCode == RESULT_CODE_EXIT_GROUP) {
            setResult(RESULT_OK);
            finish();
            return;
        }
        if (requestCode == REQUEST_CODE_CONTEXT_MENU) {
            switch (resultCode) {
                case RESULT_CODE_COPY: // 复制消息
                    EMMessage copyMsg = ((EMMessage) adapter.getItem(data.getIntExtra("position", -1)));
                    clipboard.setText(((TextMessageBody) copyMsg.getBody()).getMessage());
                    break;
                case RESULT_CODE_DELETE: // 删除消息
                    EMMessage deleteMsg = adapter.getItem(data.getIntExtra("position", -1));
                    conversation.removeMessage(deleteMsg.getMsgId());
                    adapter.refresh();
                    list_chat.setSelection(data.getIntExtra("position", adapter.getCount()) - 1);
                    break;

                case RESULT_CODE_FORWARD: // 转发消息
                    break;
                default:
                    break;
            }
        }
        if (resultCode == RESULT_OK) { // 清空消息
            if (requestCode == REQUEST_CODE_EMPTY_HISTORY) {
                // 清空会话
                EMChatManager.getInstance().clearConversation(toChatUsername);
                adapter.refresh();
            } else if (requestCode == REQUEST_CODE_CAMERA) { // 发送照片
                if (cameraFile != null && cameraFile.exists()){
                    sendPicture(cameraFile.getAbsolutePath());
                }
            } else if (requestCode == REQUEST_CODE_SELECT_VIDEO) { // 发送本地选择的视频
                int duration = data.getIntExtra("dur", 0);
                String videoPath = data.getStringExtra("path");
                File file = new File(PathUtil.getInstance().getImagePath(),
                        "thvideo" + System.currentTimeMillis());
                Bitmap bitmap = null;
                FileOutputStream fos = null;
                try {
                    if (!file.getParentFile().exists()) {
                        file.getParentFile().mkdirs();
                    }
                    bitmap = ThumbnailUtils.createVideoThumbnail(videoPath, 3);
                    if (bitmap == null) {
                        EMLog.d("chatactivity",
                                "problem load video thumbnail bitmap,use default icon");
                        bitmap = BitmapFactory.decodeResource(getResources(),
                                R.drawable.app_panel_video_icon);
                    }
                    fos = new FileOutputStream(file);

                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);

                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (fos != null) {
                        try {
                            fos.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        fos = null;
                    }
                    if (bitmap != null) {
                        bitmap.recycle();
                        bitmap = null;
                    }

                }
                sendVideo(videoPath, file.getAbsolutePath(), duration / 1000);

            } else if (requestCode == REQUEST_CODE_LOCAL) { // 发送本地图片
                if (data != null) {
                    Uri selectedImage = data.getData();
                    if (selectedImage != null) {
                        sendPicByUri(selectedImage);
                    }
                }
            } else if (requestCode == REQUEST_CODE_SELECT_FILE) { // 发送选择的文件
                if (data != null) {
                    Uri uri = data.getData();
                    if (uri != null) {
                        sendFile(uri);
                    }
                }

            } else if (requestCode == REQUEST_CODE_MAP) { // 地图
                double latitude = data.getDoubleExtra("latitude", 0);
                double longitude = data.getDoubleExtra("longitude", 0);
                String locationAddress = data.getStringExtra("address");
                if (locationAddress != null && !locationAddress.equals("")) {
                    more(more);
                    sendLocationMsg(latitude, longitude, "", locationAddress);
                } else {
                    String st = getResources().getString(
                            R.string.unable_to_get_loaction);
                    Toast.makeText(this, st, Toast.LENGTH_SHORT).show();
                }
                // 重发消息
            } else if (requestCode == REQUEST_CODE_TEXT
                    || requestCode == REQUEST_CODE_VOICE
                    || requestCode == REQUEST_CODE_PICTURE
                    || requestCode == REQUEST_CODE_LOCATION
                    || requestCode == REQUEST_CODE_VIDEO
                    || requestCode == REQUEST_CODE_FILE) {
                resendMessage();
            } else if (requestCode == REQUEST_CODE_COPY_AND_PASTE) {
                // 粘贴
                if (!TextUtils.isEmpty(clipboard.getText())) {
                    String pasteText = clipboard.getText().toString();
                    if (pasteText.startsWith(COPY_IMAGE)) {
                        // 把图片前缀去掉，还原成正常的path
                        sendPicture(pasteText.replace(COPY_IMAGE, ""));
                    }

                }
            } else if (requestCode == REQUEST_CODE_ADD_TO_BLACKLIST) { // 移入黑名单
                EMMessage deleteMsg = (EMMessage) adapter.getItem(data
                        .getIntExtra("position", -1));
                addUserToBlacklist(deleteMsg.getFrom());
            } else if (conversation.getMsgCount() > 0) {
                adapter.refresh();
                setResult(RESULT_OK);
            } else if (requestCode == REQUEST_CODE_GROUP_DETAIL) {
                adapter.refresh();
            }
        }
    }
}
