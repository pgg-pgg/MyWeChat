package com.pgg.mywechatem.Activity.Profile_Activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.pgg.mywechatem.Activity.BaseActivity;
import com.pgg.mywechatem.R;
import com.pgg.mywechatem.Uitils.Utils;


/**
 * Created by PDD on 2017/11/19.
 */

public class PayActivity extends BaseActivity implements View.OnClickListener{

    private ImageButton ib_exit_01;
    private View vertical_line;
    private TextView title_tv_left;
    private TextView title_tv_center;
    private TextView tv_record,tv_manage,tv_pay_safe,tv_pay_help;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.activity_pay);
        super.onCreate(savedInstanceState);
    }
    @Override
    public void initController() {
        ib_exit_01 =findViewById(R.id.ib_exit_01);
        vertical_line =findViewById(R.id.vertical_line);
        title_tv_left =findViewById(R.id.title_tv_left);
        title_tv_center =findViewById(R.id.title_tv_center);
        tv_record=findViewById(R.id.tv_record);
        tv_manage=findViewById(R.id.tv_manage);
        tv_pay_safe=findViewById(R.id.tv_pay_safe);
        tv_pay_help=findViewById(R.id.tv_pay_help);
    }
    @Override
    public void initView() {
        ib_exit_01.setVisibility(View.VISIBLE);
        vertical_line.setVisibility(View.VISIBLE);
        title_tv_left.setVisibility(View.VISIBLE);
        title_tv_center.setVisibility(View.GONE);
        title_tv_left.setText("支付中心");
        ib_exit_01.setBackgroundResource(R.drawable.ic_menu_exit_left);
    }

    @Override
    public void initData() {

    }

    @Override
    public void initListener() {
        ib_exit_01.setOnClickListener(this);
        tv_pay_help.setOnClickListener(this);
        tv_pay_safe.setOnClickListener(this);
        tv_manage.setOnClickListener(this);
        tv_record.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ib_exit_01:
                Utils.finish(PayActivity.this);
                break;
            case R.id.tv_pay_help:
                //帮助中心
                break;
            case R.id.tv_pay_safe:
                //支付安全
                break;
            case R.id.tv_record:
                //交易记录
                break;
            case R.id.tv_manage:
                //支付管理
                break;

        }
    }
}
