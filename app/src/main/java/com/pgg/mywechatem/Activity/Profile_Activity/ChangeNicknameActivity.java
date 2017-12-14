package com.pgg.mywechatem.Activity.Profile_Activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;


import com.pgg.mywechatem.Activity.BaseActivity;
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
 * Created by PDD on 2017/11/19.
 */

public class ChangeNicknameActivity extends BaseActivity {

    private ImageButton ib_exit_01;
    private View vertical_line;
    private TextView title_tv_left;
    private TextView title_tv_center;
    private Button ib_right;
    private EditText et_my_nickname;
    private String name;

    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what==0){
                getLoadingDialog("正在设置，请稍后").show();
            }else if(msg.what==1){
                getLoadingDialog("正在设置，请稍后").dismiss();
                Utils.showShortToast(BaseApplication.getContext(),"更新成功");
            }
        }
    };
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.activity_change_nickname);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void initController() {
        ib_exit_01 =findViewById(R.id.ib_exit_01);
        vertical_line =findViewById(R.id.vertical_line);
        title_tv_left =findViewById(R.id.title_tv_left);
        title_tv_center =findViewById(R.id.title_tv_center);
        ib_right=findViewById(R.id.btn_sure);
        et_my_nickname=findViewById(R.id.et_my_nickname);
    }

    @Override
    public void initView() {
        ib_exit_01.setVisibility(View.VISIBLE);
        vertical_line.setVisibility(View.VISIBLE);
        title_tv_left.setVisibility(View.VISIBLE);
        title_tv_center.setVisibility(View.GONE);
        title_tv_left.setText("更改名字");
        ib_right.setText("保存");
        ib_right.setVisibility(View.VISIBLE);
        ib_exit_01.setBackgroundResource(R.drawable.ic_menu_exit_left);
        Bundle bundle=getIntent().getExtras();
        name = bundle.getString(Constants.NAME);
        if (bundle!=null&&name!=null){
            et_my_nickname.setText(name);
        }
    }

    @Override
    public void initData() {

    }

    @Override
    public void initListener() {
        et_my_nickname.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if ((!s.toString().equals(name))&&(!s.toString().equals(""))){
                    ib_right.setEnabled(true);
                    ib_right.setBackgroundDrawable(getResources().getDrawable(R.drawable.btn_bg_green));
                }else {
                    ib_right.setEnabled(false);
                    ib_right.setBackgroundDrawable(getResources().getDrawable(R.drawable.btn_enable_green));
                    ib_right.setTextColor(0xFFD0EFC6);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        ib_exit_01.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.start_Activity(ChangeNicknameActivity.this,MyInfoActivity.class);
                Utils.finish(ChangeNicknameActivity.this);
            }
        });
        ib_right.setOnClickListener(new View.OnClickListener() {
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
                        String tel=Utils.getValue(BaseApplication.getContext(),Constants.LOGIN_TEL);
                        RequestBody body=new FormBody.Builder().add("telephone",tel).add("username",et_my_nickname.getText().toString()).build();
                        String json = HttpHelper.getJson(body, Constants.BASE_URL + Constants.UPDATE_NICK);
                        code = HttpHelper.ParseJsonCode(json);
                    }

                    @Override
                    public void doEnd() {
                        if (code==0){
                            handler.sendEmptyMessage(1);
                            Utils.putValue(BaseApplication.getContext(),Constants.LOGIN_NICK,et_my_nickname.getText().toString());
                            Utils.start_Activity(ChangeNicknameActivity.this,MyInfoActivity.class, new BasicNameValuePair(Constants.NAME,et_my_nickname.getText().toString()));
                            Utils.finish(ChangeNicknameActivity.this);
                        }
                    }
                }).newThread();

            }
        });
    }
}
