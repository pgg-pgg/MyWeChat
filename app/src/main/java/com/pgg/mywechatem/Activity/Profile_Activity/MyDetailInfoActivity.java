package com.pgg.mywechatem.Activity.Profile_Activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.pgg.mywechatem.Activity.BaseActivity;
import com.pgg.mywechatem.R;
import com.pgg.mywechatem.Uitils.Utils;


/**
 * Created by PDD on 2017/11/20.
 */

public class MyDetailInfoActivity extends BaseActivity implements View.OnClickListener{
    private ImageButton ib_exit_01;
    private View vertical_line;
    private TextView title_tv_left;
    private TextView title_tv_center;
    private RelativeLayout rl_my_album;
    private TextView tv_more;
    private Button btn_send;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.activity_my_detail);
        super.onCreate(savedInstanceState);
    }


    @Override
    public void initController() {
        ib_exit_01 =findViewById(R.id.ib_exit_01);
        vertical_line =findViewById(R.id.vertical_line);
        title_tv_left =findViewById(R.id.title_tv_left);
        title_tv_center =findViewById(R.id.title_tv_center);
        rl_my_album=findViewById(R.id.rl_my_album);
        tv_more=findViewById(R.id.tv_more);
        btn_send=findViewById(R.id.btn_send);
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

    }

    @Override
    public void initListener() {
        ib_exit_01.setOnClickListener(this);
        rl_my_album.setOnClickListener(this);
        tv_more.setOnClickListener(this);
        btn_send.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ib_exit_01:
                Utils.finish(MyDetailInfoActivity.this);
                break;
            case R.id.rl_my_album:
                Utils.start_Activity(MyDetailInfoActivity.this,AlbumActivity.class);
                Utils.finish(MyDetailInfoActivity.this);
                break;
            case R.id.tv_more:
                //更多相册
                break;
            case R.id.btn_send:
                //发消息
                break;
        }
    }

}
