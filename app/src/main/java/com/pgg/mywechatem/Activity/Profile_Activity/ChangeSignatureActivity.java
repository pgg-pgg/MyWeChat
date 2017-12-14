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
import com.pgg.mywechatem.Domian.Constant;
import com.pgg.mywechatem.Global.BaseApplication;
import com.pgg.mywechatem.Protocol.HttpHelper;
import com.pgg.mywechatem.Protocol.Net.ThreadHelper;
import com.pgg.mywechatem.R;
import com.pgg.mywechatem.Uitils.Constants;
import com.pgg.mywechatem.Uitils.StringUtils;
import com.pgg.mywechatem.Uitils.Utils;

import org.apache.http.message.BasicNameValuePair;

import okhttp3.FormBody;
import okhttp3.RequestBody;

/**
 * Created by PDD on 2017/11/19.
 */

public class ChangeSignatureActivity extends BaseActivity {

    private ImageButton ib_exit_01;
    private View vertical_line;
    private TextView title_tv_left;
    private TextView title_tv_center;
    private Button ib_right;
    private EditText et_my_nickname;
    private String name;
    private TextView tv_tishi;

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
    private String sign;

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
        tv_tishi=findViewById(R.id.tv_tishi);
    }

    @Override
    public void initView() {
        ib_exit_01.setVisibility(View.VISIBLE);
        vertical_line.setVisibility(View.VISIBLE);
        title_tv_left.setVisibility(View.VISIBLE);
        title_tv_center.setVisibility(View.GONE);
        title_tv_left.setText("个性签名");
        ib_right.setText("保存");
        ib_right.setVisibility(View.VISIBLE);
        ib_exit_01.setBackgroundResource(R.drawable.ic_menu_exit_left);
        tv_tishi.setVisibility(View.GONE);
        sign = Utils.getValue(BaseApplication.getContext(), Constants.LOGIN_SING);
        if (!StringUtils.isEmpty(sign)){
            et_my_nickname.setText(sign);
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
                if ((!s.toString().equals(sign))&&(!s.toString().equals(""))){
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
                Utils.start_Activity(ChangeSignatureActivity.this,MoreInfoActivity.class);
                Utils.finish(ChangeSignatureActivity.this);
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
                        RequestBody body=new FormBody.Builder().add("telephone",tel).add("signature",et_my_nickname.getText().toString()).build();
                        String json = HttpHelper.getJson(body, Constants.BASE_URL + Constants.UPDATE_SIGN);
                        code = HttpHelper.ParseJsonCode(json);
                    }

                    @Override
                    public void doEnd() {
                        if (code==0){
                            handler.sendEmptyMessage(1);
                            Utils.putValue(BaseApplication.getContext(),Constants.LOGIN_SING,et_my_nickname.getText().toString());
                            Utils.start_Activity(ChangeSignatureActivity.this,MyInfoActivity.class);
                            Utils.finish(ChangeSignatureActivity.this);
                        }
                    }
                }).newThread();

            }
        });
    }
}
