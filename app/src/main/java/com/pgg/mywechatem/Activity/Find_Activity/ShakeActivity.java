package com.pgg.mywechatem.Activity.Find_Activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.pgg.mywechatem.Activity.BaseActivity;
import com.pgg.mywechatem.R;
import com.pgg.mywechatem.Uitils.Utils;


/**
 * Created by PDD on 2017/11/18.
 */

public class ShakeActivity extends BaseActivity implements View.OnClickListener {
    private ImageButton ib_exit_01,ib_right;
    private View vertical_line;
    private TextView title_tv_left;
    private TextView title_tv_center;
    private RelativeLayout rl_people,rl_music,rl_tv;
    private ImageView[] imageViews;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.activity_shake);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void initController() {
        ib_exit_01 =findViewById(R.id.ib_exit_01);
        vertical_line =findViewById(R.id.vertical_line);
        title_tv_left =findViewById(R.id.title_tv_left);
        title_tv_center =findViewById(R.id.title_tv_center);
        ib_right=findViewById(R.id.ib_right);
        rl_people=findViewById(R.id.rl_people);
        rl_music=findViewById(R.id.rl_music);
        rl_tv=findViewById(R.id.rl_tv);
    }

    @Override
    public void initView() {
        ib_exit_01.setVisibility(View.VISIBLE);
        vertical_line.setVisibility(View.VISIBLE);
        title_tv_left.setVisibility(View.VISIBLE);
        title_tv_center.setVisibility(View.GONE);
        title_tv_left.setText("摇一摇");
        ib_exit_01.setBackgroundResource(R.drawable.ic_menu_exit_left);
        ib_right.setVisibility(View.VISIBLE);
        ib_right.setBackgroundResource(R.drawable.menu_setting);
        imageViews=new ImageView[3];
        imageViews[0]=findViewById(R.id.ib_people);
        imageViews[1]=findViewById(R.id.ib_music);
        imageViews[2]=findViewById(R.id.ib_tv);
        imageViews[0].setSelected(true);
    }

    @Override
    public void initData() {

    }

    @Override
    public void initListener() {
        ib_exit_01.setOnClickListener(this);
        ib_right.setOnClickListener(this);
        rl_people.setOnClickListener(this);
        rl_music.setOnClickListener(this);
        rl_tv.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ib_exit_01:
                finish_activity(ShakeActivity.this);
                break;
            case R.id.ib_right:
                //设置摇一摇
                Utils.start_Activity(ShakeActivity.this,ShakeSettingActivity.class);
                break;
            case R.id.rl_people:
                //摇一摇朋友
                imageViews[0].setSelected(true);
                imageViews[1].setSelected(false);
                imageViews[2].setSelected(false);
                title_tv_left.setText("摇一摇");
                break;
            case R.id.rl_music:
                //摇一摇音乐
                imageViews[0].setSelected(false);
                imageViews[1].setSelected(true);
                imageViews[2].setSelected(false);
                title_tv_left.setText("摇歌曲");
                break;
            case R.id.rl_tv:
                //摇一摇电视
                imageViews[0].setSelected(false);
                imageViews[1].setSelected(false);
                imageViews[2].setSelected(true);
                title_tv_left.setText("摇电视");
                break;
        }
    }
}
