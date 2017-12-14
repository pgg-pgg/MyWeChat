package com.pgg.mywechatem.Activity.Profile_Activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.pgg.mywechatem.Activity.BaseActivity;
import com.pgg.mywechatem.R;
import com.pgg.mywechatem.Uitils.Utils;


/**
 * Created by PDD on 2017/11/18.
 */

public class SettingActivity extends BaseActivity implements View.OnClickListener{

    private ImageButton ib_exit_01;
    private View vertical_line;
    private TextView title_tv_left;
    private TextView title_tv_center;
    private TextView tv_set_new_msg;
    private TextView tv_set_no_inter;
    private TextView tv_set_chat;
    private TextView tv_set_secret;
    private TextView tv_set_public;
    private TextView tv_set_about_wechat;
    private TextView tv_set_help;
    private TextView tv_set_exit;
    private RelativeLayout rl_set_safe;
    private LinearLayout ll_set_chajian;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.activity_setting);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void initController() {
        ib_exit_01 =findViewById(R.id.ib_exit_01);
        vertical_line =findViewById(R.id.vertical_line);
        title_tv_left =findViewById(R.id.title_tv_left);
        title_tv_center =findViewById(R.id.title_tv_center);
        tv_set_new_msg=findViewById(R.id.tv_set_new_msg);
        tv_set_no_inter=findViewById(R.id.tv_set_no_inter);
        tv_set_chat=findViewById(R.id.tv_set_chat);
        tv_set_secret=findViewById(R.id.tv_set_secret);
        tv_set_public=findViewById(R.id.tv_set_public);
        tv_set_about_wechat=findViewById(R.id.tv_set_about_wechat);
        tv_set_help=findViewById(R.id.tv_set_help);
        tv_set_exit=findViewById(R.id.tv_set_exit);
        rl_set_safe=findViewById(R.id.rl_set_safe);
        ll_set_chajian=findViewById(R.id.ll_set_chajian);
    }

    @Override
    public void initView() {
        ib_exit_01.setVisibility(View.VISIBLE);
        vertical_line.setVisibility(View.VISIBLE);
        title_tv_left.setVisibility(View.VISIBLE);
        title_tv_center.setVisibility(View.GONE);
        title_tv_left.setText("设置");
        ib_exit_01.setBackgroundResource(R.drawable.ic_menu_exit_left);
    }

    @Override
    public void initData() {

    }

    @Override
    public void initListener() {
        ib_exit_01.setOnClickListener(this);
        tv_set_new_msg.setOnClickListener(this);
        tv_set_no_inter.setOnClickListener(this);
        tv_set_chat.setOnClickListener(this);
        tv_set_secret.setOnClickListener(this);
        tv_set_public.setOnClickListener(this);
        tv_set_about_wechat.setOnClickListener(this);
        tv_set_help.setOnClickListener(this);
        tv_set_exit.setOnClickListener(this);
        rl_set_safe.setOnClickListener(this);
        ll_set_chajian.setOnClickListener(this);

    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ib_exit_01:
                Utils.finish(SettingActivity.this);
                break;
            case R.id.tv_set_new_msg:
                //新消息提醒
                break;
            case R.id.tv_set_no_inter:
                //勿扰模式
                break;
            case R.id.tv_set_chat:
                //聊天
                break;
            case R.id.tv_set_secret:
                //隐私
                break;
            case R.id.tv_set_public:
                //通用
                break;
            case R.id.tv_set_about_wechat:
                //关于微信
                break;
            case R.id.tv_set_help:
                //帮助与反馈
                break;
            case R.id.tv_set_exit:
                //退出
                break;
            case R.id.rl_set_safe:
                //账号与安全
                break;
            case R.id.ll_set_chajian:
                //插件
                break;
        }
    }
}
