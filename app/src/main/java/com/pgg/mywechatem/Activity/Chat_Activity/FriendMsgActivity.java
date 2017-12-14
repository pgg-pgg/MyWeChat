package com.pgg.mywechatem.Activity.Chat_Activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMContactManager;
import com.easemob.exceptions.EaseMobException;
import com.pgg.mywechatem.Activity.BaseActivity;
import com.pgg.mywechatem.Domian.User;
import com.pgg.mywechatem.Global.BaseApplication;
import com.pgg.mywechatem.Protocol.HttpHelper;
import com.pgg.mywechatem.Protocol.Net.ThreadHelper;
import com.pgg.mywechatem.R;
import com.pgg.mywechatem.Uitils.Constants;
import com.pgg.mywechatem.Uitils.Utils;

import org.apache.http.message.BasicNameValuePair;

import java.util.List;

import okhttp3.FormBody;
import okhttp3.RequestBody;

/**
 * Created by PDD on 2017/11/28.
 */

public class FriendMsgActivity extends BaseActivity implements View.OnClickListener {

    private ImageButton ib_exit_01;
    private View vertical_line;
    private TextView title_tv_left;
    private TextView title_tv_center;
    private RelativeLayout rl_user_photo;//用户相册
    private ImageView iv_avatar, iv_sex, img_photo1, img_photo2, img_photo3;
    private TextView tv_name, tv_accout, tv_region, tv_sign;
    private Button btn_sendmsg;
    private User user;


    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what==1){
                user= (User) msg.obj;
                if (user!=null){
                    tv_name.setText(user.getUserName());
                    if (user.getHeadUrl().contains("http")){
                        Glide.with(FriendMsgActivity.this).load(user.getHeadUrl()).into(iv_avatar);
                    }else {
                        Glide.with(FriendMsgActivity.this).load(Constants.BASE_URL + user.getHeadUrl()).into(iv_avatar);
                    }
                    iv_sex.setImageResource(user.getSex() == 1 ? R.drawable.ic_sex_male : R.drawable.ic_sex_female);
                    tv_accout.setText("微信号：" + user.getTelephone());
                    tv_region.setText(user.getLocation());
                    tv_sign.setText(user.getSignature());
                }
            }
        }
    };
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.activity_friend_msg);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void initController() {
        ib_exit_01 = findViewById(R.id.ib_exit_01);
        vertical_line = findViewById(R.id.vertical_line);
        title_tv_left = findViewById(R.id.title_tv_left);
        title_tv_center = findViewById(R.id.title_tv_center);
        tv_name = findViewById(R.id.tv_name);
        rl_user_photo = findViewById(R.id.rl_user_photo);
        iv_avatar = findViewById(R.id.iv_avatar);
        iv_sex = findViewById(R.id.iv_sex);
        img_photo1 = findViewById(R.id.img_photo1);
        img_photo2 = findViewById(R.id.img_photo2);
        img_photo3 = findViewById(R.id.img_photo3);
        tv_accout = findViewById(R.id.tv_accout);
        tv_region = findViewById(R.id.tv_region);
        tv_sign = findViewById(R.id.tv_sign);
        btn_sendmsg = findViewById(R.id.btn_sendmsg);
    }

    @Override
    public void initView() {
        ib_exit_01.setVisibility(View.VISIBLE);
        vertical_line.setVisibility(View.VISIBLE);
        title_tv_left.setVisibility(View.VISIBLE);
        title_tv_center.setVisibility(View.GONE);
        title_tv_left.setText("详细资料");
        ib_exit_01.setBackgroundResource(R.drawable.ic_menu_exit_left);

    }

    @Override
    public void initData() {
        final String user_id=getIntent().getStringExtra(Constants.User_ID);
        EMChatManager.getInstance().getChatOptions().setUseRoster(true);
        try {
            List<String> userNames = EMContactManager.getInstance().getContactUserNames();
            if (userNames.contains(user_id)){
                //是好友
                btn_sendmsg.setText("发消息");
            }else {
                btn_sendmsg.setText("添加到通讯录");
            }
        } catch (EaseMobException e) {
            e.printStackTrace();
        }
        new ThreadHelper(new ThreadHelper.ThreadDoSomething() {
            User user = null;

            @Override
            public void doPre() {

            }

            @Override
            public void doing() {
                RequestBody body = new FormBody.Builder().add("telephone", user_id).build();
                String json = HttpHelper.getJson(body, Constants.BASE_URL + Constants.GET_INFO);
                if (json != null) {
                    user = HttpHelper.ParseJsonUser(json);
                }
            }

            @Override
            public void doEnd() {
                Utils.RunningMainThread(new Runnable() {
                    @Override
                    public void run() {
                        if (user != null) {
                           Message message=new Message();
                            message.what=1;
                            message.obj=user;
                            handler.sendMessage(message);
                        }
                    }
                });
            }
        }).newThread();
    }

    @Override
    public void initListener() {
        ib_exit_01.setOnClickListener(this);
        btn_sendmsg.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ib_exit_01:
                Utils.finish(FriendMsgActivity.this);
                break;
            case R.id.btn_sendmsg:
                //发消息的点击事件
                if (btn_sendmsg.getText().toString().equals("发消息")){
                    Intent intent = new Intent(this, ChatActivity.class);
                    intent.putExtra(Constants.NAME, user.getUserName());
                    intent.putExtra(Constants.TYPE, ChatActivity.CHATTYPE_SINGLE);
                    intent.putExtra(Constants.User_ID, user.getTelephone());
                    intent.putExtra(Constants.HEAD_URL,user.getHeadUrl());
                    startActivity(intent);
                    overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                }else if (btn_sendmsg.getText().toString().equals("添加到通讯录")){
                    Utils.start_Activity(FriendMsgActivity.this,AddFriendReasonActivity.class,new BasicNameValuePair(Constants.User_ID,user.getTelephone()));
                    BaseApplication.getBaseApplication().addActivity(FriendMsgActivity.this);
                }

                break;
        }
    }
}
