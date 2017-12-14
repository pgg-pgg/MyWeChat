package com.pgg.mywechatem.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.easemob.EMCallBack;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMGroupManager;
import com.pgg.mywechatem.R;
import com.pgg.mywechatem.Uitils.Constants;
import com.pgg.mywechatem.Uitils.Utils;



/**
 * Created by PDD on 2017/11/13.
 * 欢迎界面
 */

public class SplashActivity extends Activity {

    private TextView tv_choose_language;
    private Button btn_splash_login,btn_splash_register;
    private Intent intent;
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            Boolean isLogin = Utils.getBooleanValue(SplashActivity.this, Constants.STATE_LOGIN);//得到登录状态，登录状态存储在SharePreference文件中
            intent = new Intent();
            if (isLogin){//如果已经登录
                getLogin();
            }else {
                //没有登录，跳转到登录界面
                intent.setClass(SplashActivity.this,LoginActivity.class);
                startActivity(intent);
                finish();
            }
        }
    };
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        //在引导界面需要判断登录状态，如果是登录状态，不需要再登录，否则跳入登录界面
        //还需要初始化一些组件
        initView();
        Boolean isBind=Utils.getBooleanValue(SplashActivity.this,Constants.STATE_BIND);
        if (isBind){
            //如果绑定过账号，进入登录状态的判断
            tv_choose_language.setVisibility(View.GONE);
            btn_splash_login.setVisibility(View.GONE);
            btn_splash_register.setVisibility(View.GONE);
            handler.sendEmptyMessageDelayed(0,2000);
        }else {
            //如果没有绑定过账号，显示界面
            tv_choose_language.setVisibility(View.VISIBLE);
            btn_splash_login.setVisibility(View.VISIBLE);
            btn_splash_register.setVisibility(View.VISIBLE);
            initListener();
        }
    }

    private void initListener() {
        //选择语言的点击事件
        tv_choose_language.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(SplashActivity.this,ChooseLanguageActivity.class);
                startActivity(intent);
            }
        });

        //登录的点击事件
        btn_splash_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(SplashActivity.this,LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        //注册的点击事件
        btn_splash_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(SplashActivity.this,RegisterActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void initView() {
        tv_choose_language=findViewById(R.id.tv_choose_language);
        btn_splash_login=findViewById(R.id.btn_splash_login);
        btn_splash_register=findViewById(R.id.btn_splash_register);
    }

    private void getLogin() {
        String name = Utils.getValue(this, Constants.User_ID);
        String pwd = Utils.getValue(this, Constants.PWD);
        if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(name))
            getChatService(name, pwd);
        else {
            Utils.RemoveValue(SplashActivity.this, Constants.STATE_LOGIN);
            handler.sendEmptyMessageDelayed(0, 600);
        }
    }

    private void getChatService(final String userName, final String password) {
        EMChatManager.getInstance().login(userName, password, new EMCallBack() {// 回调
            @Override
            public void onSuccess() {
                runOnUiThread(new Runnable() {
                    public void run() {
                        // TODO 保存用户信息
                        Utils.putBooleanValue(SplashActivity.this, Constants.STATE_LOGIN, true);
                        Utils.putValue(SplashActivity.this, Constants.User_ID, userName);
                        Utils.putValue(SplashActivity.this, Constants.PWD, password);
                        Log.d("main", "登陆聊天服务器成功！");
                        // 加载群组和会话
                        EMGroupManager.getInstance().loadAllGroups();
                        EMChatManager.getInstance().loadAllConversations();
                        intent.setClass(SplashActivity.this,MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });
            }

            @Override
            public void onProgress(int progress, String status) {

            }

            @Override
            public void onError(int code, String message) {
                Log.d("main", "登陆聊天服务器失败！");
            }
        });
    }
}
