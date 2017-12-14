package com.pgg.mywechatem.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.TextView;



import com.easemob.EMCallBack;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMGroupManager;
import com.pgg.mywechatem.Domian.User;
import com.pgg.mywechatem.Protocol.HttpHelper;
import com.pgg.mywechatem.R;
import com.pgg.mywechatem.Uitils.Constants;
import com.pgg.mywechatem.Uitils.PopupWindowFromBottom;
import com.pgg.mywechatem.Uitils.StringUtils;
import com.pgg.mywechatem.Uitils.TimeCountUtils;
import com.pgg.mywechatem.Uitils.Utils;
import com.pgg.mywechatem.View.WarnTipDialog;

import org.apache.http.message.BasicNameValuePair;

import okhttp3.FormBody;
import okhttp3.RequestBody;


/**
 * Created by PDD on 2017/11/15.
 */

public class Login2Activity extends BaseActivity implements View.OnClickListener {

    private ImageButton menu_exit;
    private ImageButton menu_push;
    private FrameLayout fl_container2;
    private TextView tv_msg_login_method;
    private TextView btn_login;
    private TextView tv_phone_des, tv_msg_phone_des;
    private EditText edit_password_num, edit_msg_password_num;
    private View view_of_password;
    private View view_of_msg_password;
    private PopupWindow popupWindow = null;
    private View view_phone2;
    private View view_msg;
    private TextView btn_get_msg;

    private WarnTipDialog Tipdialog;//警告框
    private TimeCountUtils timeCountUtils;
    private int code=-100;
    private String json;
    private User user;
    private String message;



    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what==0){
                getLoadingDialog("正在登录...").show();
            }
        }
    };
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.activity_login2);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void initController() {
        menu_exit = findViewById(R.id.menu_exit);
        menu_push = findViewById(R.id.menu_push);
        fl_container2 = findViewById(R.id.fl_container2);
        tv_msg_login_method = findViewById(R.id.tv_msg_login_method);//切换页面按钮
        btn_login = findViewById(R.id.btn_login);//登录按钮
        view_phone2 = Utils.getView(R.layout.use_phone_login_page2);//用密码登录页面
        view_msg = Utils.getView(R.layout.use_msg_login_page);//用短信验证码登录页面
        tv_phone_des = view_phone2.findViewById(R.id.tv_phone_des);//用密码登录的手机号的tv
        edit_password_num = view_phone2.findViewById(R.id.edit_password_num);//密码输入框
        tv_msg_phone_des = view_msg.findViewById(R.id.tv_msg_phone_des);//用短信验证码登录的手机号tv
        edit_msg_password_num = view_msg.findViewById(R.id.edit_msg_password_num);//验证码输入框
        btn_get_msg = view_msg.findViewById(R.id.btn_get_msg);//获取验证码的按钮
        view_of_password = view_phone2.findViewById(R.id.view_of_password);
        view_of_msg_password = view_msg.findViewById(R.id.view_of_msg_password);
    }

    @Override
    public void initView() {
        fl_container2.addView(view_phone2);
    }

    @Override
    public void initData() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null && bundle.getString(Constants.Phone) != null) {
            tv_phone_des.setText(bundle.getString(Constants.Phone));
        }
    }

    @Override
    public void initListener() {
        menu_exit.setOnClickListener(this);
        menu_push.setOnClickListener(this);
        btn_get_msg.setOnClickListener(this);
        btn_login.setOnClickListener(this);
        tv_msg_login_method.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.menu_exit:
                finish_activity(this);
                break;
            case R.id.menu_push:
                //底部弹出popupwindow
                if (popupWindow != null && popupWindow.isShowing()) {
                    popupWindow.dismiss();
                } else {
                    PopupWindowFromBottom.ShowPopupWindow(context, popupWindow, this, findViewById(R.id.main_login2));
                }
                break;
            case R.id.btn_get_msg:
                //获取验证码
                String phone = tv_phone_des.getText().toString().trim();
                Tipdialog = new WarnTipDialog(context, "确认手机号码", "我们将发送验证码短信到下面的号码：\n" + phone, View.VISIBLE);
                Tipdialog.setBtnOkListener(btnOkOnClickListener);
                Tipdialog.show();
                break;
            case R.id.btn_login:
                //登录按钮
                if (tv_msg_login_method.getText().equals(getString(R.string.use_msg_login))) {
                    if (StringUtils.isEmpty(edit_password_num.getText().toString())) {
                        Tipdialog = new WarnTipDialog(context, "登录失败", "密码不能为空，请输入密码", View.GONE);
                        Tipdialog.show();
                    } else {
                        //判断登录密码是否正确事件
                        getLogin();
                    }
                } else {
                    if (edit_msg_password_num.getText().toString().equals("")) {
                        Tipdialog = new WarnTipDialog(context, "登录失败", "验证码不能为空", View.GONE);
                        Tipdialog.show();
                    } else {
                        //判断验证码是否正确
                        if (edit_msg_password_num.getText().toString().equals("3429")) {

                            Utils.showShortToast(context, "登录成功");
                        }
                    }
                }
                break;
            case R.id.tv_msg_login_method:
                //点击切换页面
                if (tv_msg_login_method.getText().equals(getString(R.string.use_msg_login))) {
                    fl_container2.removeAllViews();
                    fl_container2.addView(view_msg);
                    Bundle bundle = getIntent().getExtras();
                    if (bundle != null && bundle.getString(Constants.Phone) != null) {
                        tv_msg_phone_des.setText( bundle.getString(Constants.Phone));
                    }
                    edit_msg_password_num.setFocusable(true);
                    tv_msg_login_method.setText(getString(R.string.use_password_login));
                } else {
                    fl_container2.removeAllViews();
                    fl_container2.addView(view_phone2);
                    edit_password_num.setFocusable(true);
                    tv_msg_login_method.setText(getString(R.string.use_msg_login));
                }
                break;

            case R.id.tv_find_password:
                //找回密码的点击事件
                startActivity(new Intent(Login2Activity.this, FindPasswordActivity.class));
                overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
                if (popupWindow != null) {
                    popupWindow.dismiss();
                }
                break;
            case R.id.tv_safe_center:
                //微信安全中心的点击事件
                Utils.start_Activity(Login2Activity.this, WebSafeActivity.class,
                        new BasicNameValuePair(Constants.Title, getString(R.string.safeCenter)),
                        new BasicNameValuePair(Constants.URL, "https://weixin110.qq.com/security/readtemplate?t=security_center_website/index&"));
                if (popupWindow != null) {
                    popupWindow.dismiss();
                }
                break;
        }
    }

    private DialogInterface.OnClickListener btnOkOnClickListener = new DialogInterface.OnClickListener() {

        @Override
        public void onClick(DialogInterface dialog, int which) {
            Utils.showShortToast(context, "已发送");
            timeCountUtils = new TimeCountUtils(60000, 1000, btn_get_msg);
            timeCountUtils.start();
            edit_msg_password_num.setText("3429");
            Tipdialog.dismiss();
            Tipdialog.setBtnOkListener(null);
        }
    };

    private void getLogin() {
        String userName = tv_phone_des.getText().toString().trim();
        String password = edit_password_num.getText().toString().trim();
        getLogin(userName, password);
    }

    private void getLogin(final String userName, final String password) {
        if (!TextUtils.isEmpty(userName) && !TextUtils.isEmpty(password)) {

            Thread a=new Thread(new Runnable() {
                @Override
                public void run() {
                    handler.sendEmptyMessage(0);
                    RequestBody body=new FormBody.Builder().add("telephone",userName).add("password",password).build();
                    json=HttpHelper.getJson(body,Constants.BASE_URL+Constants.LOGIN_URL);
                    if (json!=null){
                        code=HttpHelper.ParseJsonCode(json);
                        user=HttpHelper.ParseJsonUser(json);
                        message=HttpHelper.ParseJsonMessage(json);
                    }else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                getLoadingDialog("正在登录...").dismiss();
                                Utils.showShortToast(Login2Activity.this,"服务器正在维护，请稍后重试");
                            }
                        });
                    }

                }
            });
            a.start();
            try {
                a.join();
                if (code==0){
                    getChatService(userName, password);
                    Utils.putValue(Login2Activity.this,Constants.LOGIN_ID,user.getId());
                    Utils.putValue(Login2Activity.this,Constants.LOGIN_BACKGROUND,user.getBackgroundUrl());
                    Utils.putValue(Login2Activity.this,Constants.LOGIN_BIRTHDAY,user.getBirthday());
                    Utils.putValue(Login2Activity.this,Constants.LOGIN_HEAD,user.getHeadUrl());
                    Utils.putValue(Login2Activity.this,Constants.LOGIN_LOCATION,user.getLocation());
                    Utils.putValue(Login2Activity.this,Constants.LOGIN_NICK,user.getUserName());
                    Utils.putValue(Login2Activity.this,Constants.LOGIN_PWD,user.getPassword());
                    Utils.putIntValue(Login2Activity.this,Constants.LOGIN_SEX,user.getSex());
                    Utils.putValue(Login2Activity.this,Constants.LOGIN_SING,user.getSignature());
                    Utils.putValue(Login2Activity.this,Constants.LOGIN_TEL,user.getTelephone());
                    Utils.putValue(Login2Activity.this,Constants.LOGIN_TYPE,user.getType());
                }else {
                    Utils.showShortToast(Login2Activity.this,message);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } else {
            Utils.showLongToast(Login2Activity.this, "请填写账号或密码！");
        }
    }

    private void getChatService(final String userName, final String password) {
        EMChatManager.getInstance().login(userName, password, new EMCallBack() {// 回调
            @Override
            public void onSuccess() {
                runOnUiThread(new Runnable() {
                    public void run() {
                        Utils.putBooleanValue(Login2Activity.this,Constants.STATE_BIND,true);
                        Utils.putBooleanValue(Login2Activity.this, Constants.STATE_LOGIN, true);
                        Utils.putValue(Login2Activity.this, Constants.User_ID, userName);
                        Utils.putValue(Login2Activity.this, Constants.PWD, password);
                        Log.d("main", "登陆聊天服务器成功！");
                        // 加载群组和会话
                        EMGroupManager.getInstance().loadAllGroups();
                        EMChatManager.getInstance().loadAllConversations();
                        getLoadingDialog("正在登录...").dismiss();
                        Intent intent = new Intent(Login2Activity.this,MainActivity.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.push_up_in, R.anim.push_up_out);
                        finish();
                    }
                });
            }

            @Override
            public void onProgress(int progress, String status) {
            }

            @Override
            public void onError(int code, String message) {
                Log.d("main", "登陆聊天服务器失败！"+message);
                runOnUiThread(new Runnable() {
                    public void run() {
                        getLoadingDialog("正在登录...").dismiss();
                        Utils.showLongToast(Login2Activity.this, "登陆失败！");
                    }
                });
            }
        });
    }


}
