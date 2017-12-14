package com.pgg.mywechatem.Activity.Find_Activity;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.pgg.mywechatem.Activity.BaseActivity;
import com.pgg.mywechatem.R;
import com.pgg.mywechatem.Uitils.Constants;
import com.pgg.mywechatem.Uitils.Utils;


/**
 * Created by PDD on 2017/11/18.
 */

public class ShakeSettingActivity extends BaseActivity implements View.OnClickListener{
    private ImageButton ib_exit_01;
    private View vertical_line;
    private TextView title_tv_left;
    private TextView title_tv_center;
    private TextView tv_use_default,tv_change_back,tv_say_hello,tv_shake_history,tv_shake_message;
    private RelativeLayout rl_sound;
    private ImageView iv_set_sound;
    private boolean isOpen=true;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.activity_shake_setting);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void initController() {
        ib_exit_01 =findViewById(R.id.ib_exit_01);
        vertical_line =findViewById(R.id.vertical_line);
        title_tv_left =findViewById(R.id.title_tv_left);
        title_tv_center =findViewById(R.id.title_tv_center);
        tv_use_default=findViewById(R.id.tv_use_default);
        tv_change_back=findViewById(R.id.tv_change_back);
        tv_say_hello=findViewById(R.id.tv_say_hello);
        tv_shake_history=findViewById(R.id.tv_shake_history);
        tv_shake_message=findViewById(R.id.tv_shake_message);
        rl_sound=findViewById(R.id.rl_sound);
        iv_set_sound=findViewById(R.id.iv_set_sound);
        isOpen= Utils.getBooleanValue(this, Constants.IS_OPEN);
    }

    @Override
    public void initView() {
        ib_exit_01.setVisibility(View.VISIBLE);
        vertical_line.setVisibility(View.VISIBLE);
        title_tv_left.setVisibility(View.VISIBLE);
        title_tv_center.setVisibility(View.GONE);
        title_tv_left.setText("摇一摇设置");
        ib_exit_01.setBackgroundResource(R.drawable.ic_menu_exit_left);
        if (isOpen){
            iv_set_sound.setImageResource(R.drawable.set2);
        }else {
            iv_set_sound.setImageResource(R.drawable.set);
        }
    }

    @Override
    public void initData() {

    }

    @Override
    public void initListener() {
        ib_exit_01.setOnClickListener(this);
        tv_use_default.setOnClickListener(this);
        tv_change_back.setOnClickListener(this);
        tv_shake_history.setOnClickListener(this);
        tv_shake_message.setOnClickListener(this);
        rl_sound.setOnClickListener(this);
        iv_set_sound.setOnClickListener(this);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ib_exit_01:
                Utils.finish(ShakeSettingActivity.this);
                break;
            case R.id.tv_use_default:
                //使用默认图片
                break;
            case R.id.tv_change_back:
                //修改背景图片
                break;
            case R.id.tv_shake_history:
                //摇一摇历史
                break;
            case R.id.tv_shake_message:
                //摇一摇消息
                break;
            case R.id.rl_sound:
                //音效
                break;
            case R.id.iv_set_sound:
                //音效图片
                if (isOpen){
                    iv_set_sound.setImageResource(R.drawable.set);
                }else {
                    iv_set_sound.setImageResource(R.drawable.set2);
                }
                isOpen=!isOpen;
                Utils.putBooleanValue(this, Constants.IS_OPEN,isOpen);
                break;
        }
    }
}
