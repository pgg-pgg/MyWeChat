package com.pgg.mywechatem.Activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.pgg.mywechatem.Activity.Chat_Activity.FriendMsgActivity;
import com.pgg.mywechatem.Global.BaseApplication;
import com.pgg.mywechatem.Protocol.HttpHelper;
import com.pgg.mywechatem.Protocol.Net.ThreadHelper;
import com.pgg.mywechatem.R;
import com.pgg.mywechatem.Uitils.Constants;
import com.pgg.mywechatem.Uitils.Utils;

import org.apache.http.message.BasicNameValuePair;

import okhttp3.FormBody;
import okhttp3.RequestBody;


/**
 * Created by PDD on 2017/11/18.
 */

public class SearchResultActivity extends BaseActivity {
    private ImageButton ib_exit_01;
    private EditText et_search;
    private String result;
    private RelativeLayout rl_add_friend;
    private ImageView iv_add_friend_icon;
    private TextView tv_search_friend_value;


    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what==0){
                getLoadingDialog("正在查找...请稍后").show();
            }else if(msg.what==1){
                getLoadingDialog("正在查找...请稍后").dismiss();
                Utils.start_Activity(SearchResultActivity.this, FriendMsgActivity.class,new BasicNameValuePair(Constants.User_ID,tv_search_friend_value.getText().toString()));
                BaseApplication.getBaseApplication().addActivity(SearchResultActivity.this);
            }else if (msg.what==2){
                getLoadingDialog("正在查找...请稍后").dismiss();
                Utils.showShortToast(BaseApplication.getContext(),"不存在此用户");
            }
        }
    };
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.activity_search_result);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void initController() {
        ib_exit_01=findViewById(R.id.ib_exit_01);
        et_search=findViewById(R.id.et_search);
        rl_add_friend=findViewById(R.id.rl_add_friend);
        iv_add_friend_icon=findViewById(R.id.iv_add_friend_icon);
        tv_search_friend_value=findViewById(R.id.tv_search_friend_value);
    }

    @Override
    public void initView() {
        Bundle bundle=getIntent().getExtras();
        if (bundle!=null&&(!bundle.getString("result").equals(""))){
            result = bundle.getString("result");
            et_search.setHint(result);
        }
    }

    @Override
    public void initData() {

    }

    @Override
    public void initListener() {
        ib_exit_01.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish_activity(SearchResultActivity.this);
            }
        });

        et_search.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    if (result.equals("搜索")){
                        if (!Utils.isMobileNO(et_search.getText().toString())){
                            Utils.showShortToast(BaseApplication.getContext(),"请输入正确的手机号");
                        }else {
                            rl_add_friend.setVisibility(View.VISIBLE);
                            tv_search_friend_value.setText(et_search.getText().toString());
                        }
                    }
                }
                return false;
            }
        });

        rl_add_friend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new ThreadHelper(new ThreadHelper.ThreadDoSomething() {

                    private int code=-100;

                    @Override
                    public void doPre() {
                        handler.sendEmptyMessage(0);
                    }
                    @Override
                    public void doing() {
                        RequestBody body = new FormBody.Builder().add("telephone", tv_search_friend_value.getText().toString()).build();
                        String json = HttpHelper.getJson(body, Constants.BASE_URL + Constants.SEARCH_BY_ID);
                        if (json != null) {
                            code = HttpHelper.ParseJsonCode(json);
                        }
                    }
                    @Override
                    public void doEnd() {
                        if (code==0){
                            handler.sendEmptyMessage(1);
                        }else {
                            handler.sendEmptyMessage(2);
                        }
                    }
                }).newThread();
            }
        });

    }

}
