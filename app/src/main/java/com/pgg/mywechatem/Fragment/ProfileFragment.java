package com.pgg.mywechatem.Fragment;

import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.bumptech.glide.Glide;
import com.pgg.mywechatem.Activity.Profile_Activity.AlbumActivity;
import com.pgg.mywechatem.Activity.Profile_Activity.CardBoxActivity;
import com.pgg.mywechatem.Activity.Profile_Activity.CollectActivity;
import com.pgg.mywechatem.Activity.Profile_Activity.MoneyActivity;
import com.pgg.mywechatem.Activity.Profile_Activity.MyInfoActivity;
import com.pgg.mywechatem.Activity.Profile_Activity.SettingActivity;
import com.pgg.mywechatem.Activity.WebSafeActivity;
import com.pgg.mywechatem.Global.BaseApplication;
import com.pgg.mywechatem.R;
import com.pgg.mywechatem.Uitils.Constants;
import com.pgg.mywechatem.Uitils.Utils;

import org.apache.http.message.BasicNameValuePair;

/**
 * Created by PDD on 2017/11/16.
 */

public class ProfileFragment extends BaseFragment implements View.OnClickListener {

    private RelativeLayout view_user;
    private ImageView head,iv_sex;
    private TextView tvname,tvmsg,txt_album,txt_collect,txt_money,txt_card,txt_smail,txt_setting;

    @Override
    public View initView() {
        View view= Utils.getView(R.layout.fragment_profile);
        view_user=view.findViewById(R.id.view_user);
        head=view.findViewById(R.id.head);
        iv_sex=view.findViewById(R.id.iv_sex);
        tvname=view.findViewById(R.id.tvname);
        tvmsg=view.findViewById(R.id.tvmsg);
        txt_album=view.findViewById(R.id.txt_album);
        txt_collect=view.findViewById(R.id.txt_collect);
        txt_money=view.findViewById(R.id.txt_money);
        txt_card=view.findViewById(R.id.txt_card);
        txt_smail=view.findViewById(R.id.txt_smail);
        txt_setting=view.findViewById(R.id.txt_setting);
        return view;
    }

    @Override
    public void initData() {
        String head_url=Utils.getValue(BaseApplication.getContext(),Constants.LOGIN_HEAD);
        String username=Utils.getValue(BaseApplication.getContext(),Constants.LOGIN_NICK);
        if (head_url.contains("http")){
            Glide.with(getActivity()).load(head_url).into(head);
        }else {
            Glide.with(getActivity()).load(Constants.BASE_URL+head_url).into(head);
        }
        tvname.setText(username);
        int sex=Utils.getIntValue(BaseApplication.getContext(),Constants.LOGIN_SEX);
        String tel=Utils.getValue(BaseApplication.getContext(),Constants.LOGIN_TEL);
        iv_sex.setImageResource(sex==1?R.drawable.ic_sex_male:R.drawable.ic_sex_female);
        tvmsg.setText("微信号："+tel);
    }

    @Override
    public void onResume() {
        super.onResume();
        initData();
    }

    @Override
    public void initListener() {
        view_user.setOnClickListener(this);
        txt_album.setOnClickListener(this);
        txt_collect.setOnClickListener(this);
        txt_money.setOnClickListener(this);
        txt_card.setOnClickListener(this);
        txt_smail.setOnClickListener(this);
        txt_setting.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.view_user:
                //个人信息
                Utils.start_Activity(getActivity(), MyInfoActivity.class);
                break;
            case R.id.txt_album:
                //相册
                Utils.start_Activity(getActivity(), AlbumActivity.class);
                break;
            case R.id.txt_collect:
                //收藏
                Utils.start_Activity(getActivity(), CollectActivity.class);
                break;
            case R.id.txt_money:
                //钱包
                Utils.start_Activity(getActivity(), MoneyActivity.class);
                break;
            case R.id.txt_card:
                //卡包
                Utils.start_Activity(getActivity(), CardBoxActivity.class);
                break;
            case R.id.txt_smail:
                //表情
                Utils.start_Activity(getActivity(), WebSafeActivity.class,
                        new BasicNameValuePair(Constants.Title,"表情商店"),
                        new BasicNameValuePair(Constants.URL,"https://sticker.weixin.qq.com/cgi-bin/mmemoticon-bin/loginpage?t=login/index"));
                break;
            case R.id.txt_setting:
                //设置
                Utils.start_Activity(getActivity(), SettingActivity.class);
                break;
        }
    }
}
