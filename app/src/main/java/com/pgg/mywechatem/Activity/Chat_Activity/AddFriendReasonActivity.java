package com.pgg.mywechatem.Activity.Chat_Activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMContactManager;
import com.easemob.exceptions.EaseMobException;
import com.pgg.mywechatem.Activity.BaseActivity;
import com.pgg.mywechatem.Activity.Profile_Activity.ChangeNicknameActivity;
import com.pgg.mywechatem.Activity.Profile_Activity.MyInfoActivity;
import com.pgg.mywechatem.Global.BaseApplication;
import com.pgg.mywechatem.Protocol.Net.ThreadHelper;
import com.pgg.mywechatem.R;
import com.pgg.mywechatem.Uitils.Constants;
import com.pgg.mywechatem.Uitils.StringUtils;
import com.pgg.mywechatem.Uitils.Utils;

/**
 * Created by PDD on 2017/12/2.
 */

public class AddFriendReasonActivity extends BaseActivity {

    private ImageButton ib_exit_01;
    private View vertical_line;
    private TextView title_tv_left;
    private TextView title_tv_center;
    private Button ib_right;
    private EditText et_add_friend_reason;
    private String tel;

    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what==0){
                getLoadingDialog("正在发送...请稍后").show();
            }else if (msg.what==1){
                getLoadingDialog("正在发送...请稍后").dismiss();
                Utils.showShortToast(BaseApplication.getContext(),"发送成功");
                finish_activity(AddFriendReasonActivity.this);
                BaseApplication.getBaseApplication().removeActivity();
            }else if (msg.what==2){
                getLoadingDialog("正在发送...请稍后").dismiss();
                Utils.showShortToast(BaseApplication.getContext(),"发送失败，请稍后重试");
            }
        }
    };
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.activity_add_friend_reason);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void initController() {
        ib_exit_01 = findViewById(R.id.ib_exit_01);
        vertical_line = findViewById(R.id.vertical_line);
        title_tv_left = findViewById(R.id.title_tv_left);
        title_tv_center = findViewById(R.id.title_tv_center);
        ib_right=findViewById(R.id.btn_sure);
        et_add_friend_reason=findViewById(R.id.et_add_friend_reason);
    }

    @Override
    public void initView() {
        ib_exit_01.setVisibility(View.VISIBLE);
        vertical_line.setVisibility(View.VISIBLE);
        title_tv_left.setVisibility(View.VISIBLE);
        title_tv_center.setVisibility(View.GONE);
        title_tv_left.setText("验证申请");
        ib_exit_01.setBackgroundResource(R.drawable.ic_menu_exit_left);
        ib_right.setText("发送");
        ib_right.setVisibility(View.VISIBLE);
        ib_right.setEnabled(true);
        ib_right.setBackgroundDrawable(getResources().getDrawable(R.drawable.btn_bg_green));
    }

    @Override
    public void initData() {
        tel = getIntent().getStringExtra(Constants.User_ID);
    }

    @Override
    public void initListener() {
        ib_exit_01.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.finish(AddFriendReasonActivity.this);
            }
        });
        ib_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addContact();
            }
        });
    }

    /**
     *  添加contact
     */
    public void addContact(){
        new ThreadHelper(new ThreadHelper.ThreadDoSomething() {
            @Override
            public void doPre() {
                handler.sendEmptyMessage(0);
            }

            @Override
            public void doing() {
                String s = et_add_friend_reason.getText().toString();
                if (StringUtils.isEmpty(s)){
                    s=getResources().getString(R.string.Add_a_friend);
                }
                try {
                    EMContactManager.getInstance().addContact(tel, s);
                    handler.sendEmptyMessage(1);
                } catch (EaseMobException e) {
                    e.printStackTrace();
                    handler.sendEmptyMessage(2);
                }
            }

            @Override
            public void doEnd() {

            }
        }).newThread();
    }
}
