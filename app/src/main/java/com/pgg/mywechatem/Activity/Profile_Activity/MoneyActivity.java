package com.pgg.mywechatem.Activity.Profile_Activity;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.pgg.mywechatem.Activity.BaseActivity;
import com.pgg.mywechatem.Activity.GetMoneyActivity;
import com.pgg.mywechatem.Adapter.MyWalletAdapter;
import com.pgg.mywechatem.Domian.WalletBean;
import com.pgg.mywechatem.R;
import com.pgg.mywechatem.Uitils.CreateDataUtils;
import com.pgg.mywechatem.Uitils.Utils;


/**
 * Created by PDD on 2017/11/18.
 */

public class MoneyActivity extends BaseActivity implements View.OnClickListener {

    private ImageButton ib_exit_01;
    private View vertical_line;
    private TextView title_tv_left;
    private TextView title_tv_center;
    private ImageButton ib_right;
    private RelativeLayout rl_shoufukuan,rl_linqian,rl_card;
    private RecyclerView rv_money;
    private Context mContext;
    private WalletBean resultBean;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.activity_money);
        mContext=MoneyActivity.this;
        super.onCreate(savedInstanceState);
    }

    @Override
    public void initController() {
        ib_exit_01 =findViewById(R.id.ib_exit_01);
        vertical_line =findViewById(R.id.vertical_line);
        title_tv_left =findViewById(R.id.title_tv_left);
        title_tv_center =findViewById(R.id.title_tv_center);
        ib_right=findViewById(R.id.ib_right);
        rl_card=findViewById(R.id.rl_card);
        rl_linqian=findViewById(R.id.rl_linqian);
        rl_shoufukuan=findViewById(R.id.rl_shoufukuan);
        rv_money=findViewById(R.id.rv_money);
    }

    @Override
    public void initView() {
        ib_exit_01.setVisibility(View.VISIBLE);
        vertical_line.setVisibility(View.VISIBLE);
        title_tv_left.setVisibility(View.VISIBLE);
        title_tv_center.setVisibility(View.GONE);
        title_tv_left.setText("我的钱包");
        ib_exit_01.setBackgroundResource(R.drawable.ic_menu_exit_left);
        ib_right.setVisibility(View.VISIBLE);
        ib_right.setBackgroundResource(R.drawable.money_menu);
    }

    @Override
    public void initData() {
        resultBean= CreateDataUtils.createWalletBean();
        MyWalletAdapter adapter=new MyWalletAdapter(mContext,resultBean);
        rv_money.setAdapter(adapter);
        GridLayoutManager manager=new GridLayoutManager(mContext,1);
        rv_money.setLayoutManager(manager);
    }

    @Override
    public void initListener() {
        rl_card.setOnClickListener(this);
        rl_shoufukuan.setOnClickListener(this);
        rl_linqian.setOnClickListener(this);
        ib_exit_01.setOnClickListener(this);
        ib_right.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_card:
                //银行卡
                Utils.start_Activity(MoneyActivity.this, CardActivity.class);
                break;
            case R.id.rl_shoufukuan:
                Utils.start_Activity(MoneyActivity.this, GetMoneyActivity.class);
                break;
            case R.id.rl_linqian:
                //零钱
                Utils.start_Activity(MoneyActivity.this,DibsActivity.class);
                break;
            case R.id.ib_exit_01:
                Utils.finish(MoneyActivity.this);
                break;
            case R.id.ib_right:
                //支付中心
                Utils.start_Activity(MoneyActivity.this,PayActivity.class);
                break;
        }
    }
}
