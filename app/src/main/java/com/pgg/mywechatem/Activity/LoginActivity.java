package com.pgg.mywechatem.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.TextView;


import com.pgg.mywechatem.Global.BaseApplication;
import com.pgg.mywechatem.R;
import com.pgg.mywechatem.Uitils.Constants;
import com.pgg.mywechatem.Uitils.PopupWindowFromBottom;
import com.pgg.mywechatem.Uitils.Utils;
import com.pgg.mywechatem.View.WarnTipDialog;

import org.apache.http.message.BasicNameValuePair;

/**
 * Created by PDD on 2017/11/13.
 * 登录界面
 */

public class LoginActivity extends BaseActivity implements View.OnClickListener{

    private ImageButton menu_exit;
    private ImageButton menu_push;
    private TextView tv_phone_login_title;
    private FrameLayout fl_container;
    private TextView tv_other_login_method;
    private TextView btn_next;
    private TextView tv_location_des;
    private EditText edit_phone_num;
    private EditText edit_id_num;
    private EditText edit_password_num;
    private View view_phone;
    private View view_other;
    private View view_of_id;
    private View view_of_password;
    private PopupWindow popupWindow=null;
    private WarnTipDialog tipDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.activity_login);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void initController() {
        menu_exit =findViewById(R.id.menu_exit);
        menu_push =findViewById(R.id.menu_push);
        tv_phone_login_title =findViewById(R.id.tv_phone_login_title);
        fl_container =findViewById(R.id.fl_container);
        tv_other_login_method =findViewById(R.id.tv_other_login_method);
        btn_next =findViewById(R.id.btn_next);
        view_phone = Utils.getView(R.layout.use_phone_login_page);
        tv_location_des = view_phone.findViewById(R.id.tv_location_des);
        edit_phone_num = view_phone.findViewById(R.id.edit_phone_num);
        view_other = Utils.getView(R.layout.use_other_login_page);
        edit_id_num = view_other.findViewById(R.id.edit_id_num);
        edit_password_num = view_other.findViewById(R.id.edit_password_num);
        view_of_id =view_other.findViewById(R.id.view_of_id);
        view_of_password =view_other.findViewById(R.id.view_of_password);
    }

    @Override
    public void initView() {
        fl_container.addView(view_phone);
    }

    @Override
    public void initData() {

    }

    @Override
    public void initListener() {
        menu_exit.setOnClickListener(this);
        menu_push.setOnClickListener(this);
        tv_location_des.setOnClickListener(this);
        tv_other_login_method.setOnClickListener(this);
        btn_next.setOnClickListener(this);
        edit_id_num.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus){
                    view_of_id.setBackgroundResource(R.color.greeny);
                    view_of_password.setBackgroundResource(R.color.line_gray);
                }else {
                    view_of_id.setBackgroundResource(R.color.line_gray);
                    if (edit_password_num.hasFocus()){
                        view_of_password.setBackgroundResource(R.color.greeny);
                    }
                }
            }
        });
        edit_password_num.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus){
                    view_of_password.setBackgroundResource(R.color.greeny);
                    view_of_id.setBackgroundResource(R.color.line_gray);
                }else {
                    view_of_password.setBackgroundResource(R.color.line_gray);
                    if (edit_id_num.hasFocus()){
                        view_of_id.setBackgroundResource(R.color.greeny);
                    }
                }
            }
        });

        edit_id_num.addTextChangedListener(new TextChange());
        edit_password_num.addTextChangedListener(new TextChange());
        edit_phone_num.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (Utils.isMobileNO(s.toString())){
                    setBtnEnable();
                }else {
                    setBtnDisable();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


    }

    private void setBtnDisable() {
        btn_next.setEnabled(false);
        btn_next.setBackgroundDrawable(getResources().getDrawable(
                R.drawable.btn_enable_green));
        btn_next.setTextColor(0xFFD0EFC6);
    }

    private void setBtnEnable() {
        btn_next.setEnabled(true);
        btn_next.setBackgroundDrawable(getResources().getDrawable(R.drawable.btn_bg_green));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.menu_exit:
                //退出按钮
                Utils.finish(LoginActivity.this);
                break;
            case R.id.menu_push:
                //弹出选择框popupWindow
                if (popupWindow!=null&&popupWindow.isShowing()){
                    popupWindow.dismiss();
                }else {
                    PopupWindowFromBottom.ShowPopupWindow(context,popupWindow,this,findViewById(R.id.main));
                }
                break;
            case R.id.tv_location_des:
                //点击选择国家地区
                break;
            case R.id.tv_other_login_method:
                //点击切换页面
                if (tv_other_login_method.getText().equals("用微信号/QQ号/邮箱登录")){
                    fl_container.removeAllViews();
                    fl_container.addView(view_other);
                    tv_phone_login_title.setText("微信号/QQ/邮箱登录");
                    tv_other_login_method.setText("用手机号登录");
                    btn_next.setText("登录");
                    setBtnDisable();
                    edit_phone_num.setText("");
                }else {
                    fl_container.removeAllViews();
                    fl_container.addView(view_phone);
                    tv_phone_login_title.setText("手机号登录");
                    tv_other_login_method.setText("用微信号/QQ号/邮箱登录");
                    btn_next.setText("下一步");
                    setBtnDisable();
                    edit_password_num.setText("");
                    edit_id_num.setText("");
                }
                break;
            case R.id.btn_next:
                //点击下一步，登录
                if (tv_other_login_method.getText().equals("用微信号/QQ号/邮箱登录")){
                    if (btn_next.isEnabled()){
                        if (!Utils.isMobileNO(edit_phone_num.getText().toString())){
                            tipDialog = new WarnTipDialog(context, "手机号码错误","你输入的是一个无效的手机号",View.GONE);
                            tipDialog.show();
                        }else {
                            getLoadingDialog("请稍后...").show();
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        Thread.sleep(1000);
                                        Utils.putBooleanValue(BaseApplication.getContext(),Constants.STATE_BIND,true);
                                        start_activity(LoginActivity.this,Login2Activity.class,new BasicNameValuePair(Constants.Phone,edit_phone_num.getText().toString()));
                                        finish_activity(LoginActivity.this);
                                        getLoadingDialog("请稍后...").dismiss();
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }).start();
                        }
                    }
                }else {
                    Utils.showShortToast(LoginActivity.this,"登录");
                }
                break;
            case R.id.tv_find_password:
                //找回密码的点击事件
                startActivity(new Intent(LoginActivity.this, FindPasswordActivity.class));
                overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
                if (popupWindow!=null){
                    popupWindow.dismiss();
                }
                break;
            case R.id.tv_safe_center:
                //微信安全中心的点击事件
                Utils.start_Activity(LoginActivity.this, WebSafeActivity.class,
                        new BasicNameValuePair(Constants.Title, getString(R.string.safeCenter)),
                        new BasicNameValuePair(Constants.URL, "https://weixin110.qq.com/security/readtemplate?t=security_center_website/index&"));
                if (popupWindow!=null){
                    popupWindow.dismiss();
                }
                break;
        }
    }


    // EditText监听器
    class TextChange implements TextWatcher {

        @Override
        public void afterTextChanged(Editable arg0) {

        }

        @Override
        public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                      int arg3) {

        }

        @Override
        public void onTextChanged(CharSequence cs, int start, int before,
                                  int count) {
            boolean Sign2 = edit_id_num.getText().length() > 0;
            boolean Sign3 = edit_password_num.getText().length() > 0;
            if (Sign2 & Sign3) {
                setBtnEnable();
            } else {
               setBtnDisable();
            }
        }
    }
}
