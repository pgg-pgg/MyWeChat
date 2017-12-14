package com.pgg.mywechatem.Fragment;

import android.view.View;
import android.widget.TextView;


import com.pgg.mywechatem.Activity.CaptureActivity;
import com.pgg.mywechatem.Activity.Find_Activity.MomentsActivity;
import com.pgg.mywechatem.Activity.Find_Activity.NearByActivity;
import com.pgg.mywechatem.Activity.Find_Activity.ShakeActivity;
import com.pgg.mywechatem.Activity.WebSafeActivity;
import com.pgg.mywechatem.R;
import com.pgg.mywechatem.Uitils.Constants;
import com.pgg.mywechatem.Uitils.Utils;

import org.apache.http.message.BasicNameValuePair;

/**
 * Created by PDD on 2017/11/16.
 */

public class FindFragment extends BaseFragment implements View.OnClickListener {
    private TextView txt_pengyouquan,txt_saoyisao,txt_yaoyiyao,txt_nearby,txt_shop,txt_game,txt_small_program;

    @Override
    public View initView() {
        View view= Utils.getView(R.layout.fragment_find);
        txt_pengyouquan=view.findViewById(R.id.txt_pengyouquan);
        txt_saoyisao=view.findViewById(R.id.txt_saoyisao);
        txt_yaoyiyao=view.findViewById(R.id.txt_yaoyiyao);
        txt_nearby=view.findViewById(R.id.txt_nearby);
        txt_shop=view.findViewById(R.id.txt_shop);
        txt_game=view.findViewById(R.id.txt_game);
        txt_small_program=view.findViewById(R.id.txt_small_program);
        return view;
    }

    @Override
    public void initData() {

    }

    @Override
    public void initListener() {
        txt_pengyouquan.setOnClickListener(this);
        txt_saoyisao.setOnClickListener(this);
        txt_yaoyiyao.setOnClickListener(this);
        txt_nearby.setOnClickListener(this);
        txt_shop.setOnClickListener(this);
        txt_game.setOnClickListener(this);
        txt_small_program.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.txt_pengyouquan:
                //进入朋友圈
                Utils.start_Activity(getActivity(), MomentsActivity.class);
                break;
            case R.id.txt_saoyisao:
                //扫一扫
                Utils.start_Activity(getActivity(), CaptureActivity.class);
                break;
            case R.id.txt_yaoyiyao:
                //摇一摇
                Utils.start_Activity(getActivity(), ShakeActivity.class);
                break;
            case R.id.txt_nearby:
                //附近的人
                Utils.start_Activity(getActivity(), NearByActivity.class);
                break;
            case R.id.txt_shop:
                //购物
                Utils.start_Activity(getActivity(), WebSafeActivity.class,new BasicNameValuePair(Constants.Title,"京东购物")
                        ,new BasicNameValuePair(Constants.URL,"https://www.jd.com/?cu=true&utm_source=baidu-pinzhuan&utm_medium=cpc&utm_campaign=t_288551095_baidupinzhuan&utm_term=0f3d30c8dba7459bb52f2eb5eba8ac7d_0_7028b35fd98c4bdfabbb925cbe74bafe"));
                break;
            case R.id.txt_game:
                //游戏
                Utils.start_Activity(getActivity(), WebSafeActivity.class,new BasicNameValuePair(Constants.Title,"微信游戏")
                        ,new BasicNameValuePair(Constants.URL,"http://news.4399.com/wxyx/"));
                break;
            case R.id.txt_small_program:
                //小程序
                Utils.start_Activity(getActivity(), WebSafeActivity.class,new BasicNameValuePair(Constants.Title,"小程序")
                        ,new BasicNameValuePair(Constants.URL,"https://www.youzan.com/intro/weapp?from_source=sembd0065&utm_source=baidu&utm_medium=cpc&utm_campaign=sem&utm_content=keyword&utm_term=%E5%BE%AE%E4%BF%A1%E5%85%AC%E4%BC%97%E5%B9%B3%E5%8F%B0%E5%B0%8F%E7%A8%8B%E5%BA%8F"));
                break;

        }
    }
}
