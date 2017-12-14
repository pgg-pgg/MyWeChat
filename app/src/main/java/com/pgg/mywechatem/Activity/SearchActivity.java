package com.pgg.mywechatem.Activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;


import com.pgg.mywechatem.R;
import com.pgg.mywechatem.Uitils.Utils;

import org.apache.http.message.BasicNameValuePair;

/**
 * Created by PDD on 2017/11/18.
 */

public class SearchActivity extends BaseActivity implements View.OnClickListener{

    private ImageButton ib_exit_01,ib_yuyin;
    private EditText et_search;
    private TextView tv_pengyouquan,tv_news,tv_public,tv_xiaoshuo,tv_yingyue,tv_biaoqing;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.activity_search);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ib_exit_01:
                Utils.finish(SearchActivity.this);
                break;
            case R.id.ib_yuyin:
                //语音
                break;
            case R.id.tv_pengyouquan:
                //朋友圈
                Utils.start_Activity(SearchActivity.this,SearchResultActivity.class,new BasicNameValuePair("result","搜索朋友圈"));
                break;
            case R.id.tv_news:
                Utils.start_Activity(SearchActivity.this,SearchResultActivity.class,new BasicNameValuePair("result","搜索资讯"));
                break;
            case R.id.tv_public:
                Utils.start_Activity(SearchActivity.this,SearchResultActivity.class,new BasicNameValuePair("result","搜索公众号"));
                break;
            case R.id.tv_xiaoshuo:
                Utils.start_Activity(SearchActivity.this,SearchResultActivity.class,new BasicNameValuePair("result","搜索小说"));
                break;
            case R.id.tv_yingyue:
                Utils.start_Activity(SearchActivity.this,SearchResultActivity.class,new BasicNameValuePair("result","搜索音乐"));
                break;
            case R.id.tv_biaoqing:
                Utils.start_Activity(SearchActivity.this,SearchResultActivity.class,new BasicNameValuePair("result","搜索表情"));
                break;
        }
    }

    @Override
    public void initController() {
        ib_exit_01=findViewById(R.id.ib_exit_01);
        ib_yuyin=findViewById(R.id.ib_yuyin);
        et_search=findViewById(R.id.et_search);
        tv_pengyouquan=findViewById(R.id.tv_pengyouquan);
        tv_news=findViewById(R.id.tv_news);
        tv_public=findViewById(R.id.tv_public);
        tv_xiaoshuo=findViewById(R.id.tv_xiaoshuo);
        tv_yingyue=findViewById(R.id.tv_yingyue);
        tv_biaoqing=findViewById(R.id.tv_biaoqing);
    }

    @Override
    public void initView() {

    }

    @Override
    public void initData() {

    }

    @Override
    public void initListener() {
        ib_exit_01.setOnClickListener(this);
        ib_yuyin.setOnClickListener(this);
        tv_pengyouquan.setOnClickListener(this);
        tv_biaoqing.setOnClickListener(this);
        tv_news.setOnClickListener(this);
        tv_public.setOnClickListener(this);
        tv_xiaoshuo.setOnClickListener(this);
        tv_yingyue.setOnClickListener(this);


    }
}
