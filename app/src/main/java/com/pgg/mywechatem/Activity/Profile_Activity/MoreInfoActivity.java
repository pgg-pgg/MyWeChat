package com.pgg.mywechatem.Activity.Profile_Activity;

import android.app.AlertDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.pgg.mywechatem.Activity.BaseActivity;
import com.pgg.mywechatem.Activity.Find_Activity.MomentsActivity;
import com.pgg.mywechatem.Global.BaseApplication;
import com.pgg.mywechatem.Protocol.HttpHelper;
import com.pgg.mywechatem.Protocol.Net.ThreadHelper;
import com.pgg.mywechatem.R;
import com.pgg.mywechatem.Uitils.Constants;
import com.pgg.mywechatem.Uitils.StringUtils;
import com.pgg.mywechatem.Uitils.Utils;

import okhttp3.FormBody;
import okhttp3.RequestBody;


/**
 * Created by PDD on 2017/11/19.
 */

public class MoreInfoActivity extends BaseActivity implements View.OnClickListener {
    private ImageButton ib_exit_01;
    private View vertical_line;
    private TextView title_tv_left;
    private TextView title_tv_center;
    private RelativeLayout rl_sex,rl_location,rl_mark;
    private TextView tv_sex_content,tv_location_content,tv_mark_content;

    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what==0){
                getLoadingDialog("正在设置...请稍后").show();
            }else if (msg.what==1){
                getLoadingDialog("正在设置...请稍后").dismiss();
                Utils.showShortToast(MoreInfoActivity.this,"更新成功");
            }
        }
    };
    private String tel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.activity_more_info);
        super.onCreate(savedInstanceState);
    }


    @Override
    public void initController() {
        ib_exit_01 =findViewById(R.id.ib_exit_01);
        vertical_line =findViewById(R.id.vertical_line);
        title_tv_left =findViewById(R.id.title_tv_left);
        title_tv_center =findViewById(R.id.title_tv_center);
        rl_sex=findViewById(R.id.rl_sex);
        rl_location=findViewById(R.id.rl_location);
        rl_mark=findViewById(R.id.rl_mark);
        tv_sex_content=findViewById(R.id.tv_sex_content);
        tv_location_content=findViewById(R.id.tv_location_content);
        tv_mark_content=findViewById(R.id.tv_mark_content);
    }

    @Override
    public void initView() {
        ib_exit_01.setVisibility(View.VISIBLE);
        vertical_line.setVisibility(View.VISIBLE);
        title_tv_left.setVisibility(View.VISIBLE);
        title_tv_center.setVisibility(View.GONE);
        title_tv_left.setText("更多信息");
        ib_exit_01.setBackgroundResource(R.drawable.ic_menu_exit_left);
    }

    @Override
    public void initData() {
        int sex=Utils.getIntValue(BaseApplication.getContext(), Constants.LOGIN_SEX);
        String location=Utils.getValue(BaseApplication.getContext(),Constants.LOGIN_LOCATION);
        String sign=Utils.getValue(BaseApplication.getContext(),Constants.LOGIN_SING);
        tv_sex_content.setText(sex==1?"男":"女");
        tv_location_content.setText(location);
        if (StringUtils.isEmpty(sign)){
            tv_mark_content.setText("未填写");
        }
        tv_mark_content.setText(sign);
    }

    @Override
    public void initListener() {
        ib_exit_01.setOnClickListener(this);
        rl_sex.setOnClickListener(this);
        rl_location.setOnClickListener(this);
        rl_mark.setOnClickListener(this);

    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ib_exit_01:
                Utils.finish(MoreInfoActivity.this);
                break;
            case R.id.rl_sex:
                //修改性别
                showCameraDialog();
                break;
            case R.id.rl_location:
                //修改地区
                break;
            case R.id.rl_mark:
                //修改个性签名
                Utils.start_Activity(MoreInfoActivity.this,ChangeSignatureActivity.class);
                Utils.finish(this);
                break;
        }
    }

    private void showCameraDialog() {
        tel = Utils.getValue(BaseApplication.getContext(), Constants.LOGIN_TEL);
        AlertDialog.Builder builder = new AlertDialog.Builder(MoreInfoActivity.this);
        final AlertDialog alertDialog = builder.create();
        alertDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        View view = Utils.getView(R.layout.dialog_show_sex);
        TextView choose_man=view.findViewById(R.id.choose_man);
        TextView choose_woman=view.findViewById(R.id.choose_woman);
        alertDialog.setView(view,0,0,0,0);
        alertDialog.show();
        choose_man.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UpdateInfoMethod(tel,"sex","1",Constants.BASE_URL+Constants.UPDATE_SEX);
                Utils.putIntValue(BaseApplication.getContext(),Constants.LOGIN_SEX,1);
                tv_sex_content.setText("男");
                alertDialog.dismiss();
            }
        });

        choose_woman.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UpdateInfoMethod(tel,"sex","0",Constants.BASE_URL+Constants.UPDATE_SEX);
                Utils.putIntValue(BaseApplication.getContext(),Constants.LOGIN_SEX,0);
                tv_sex_content.setText("女");
                alertDialog.dismiss();
            }
        });
    }
    private void UpdateInfoMethod(final String telephone, final String changeEle, final String changeEleValue, final String url){
        new ThreadHelper(new ThreadHelper.ThreadDoSomething() {

            private int code=-100;
            private RequestBody body;

            @Override
            public void doPre() {
                handler.sendEmptyMessage(0);
                body = new FormBody.Builder().add("telephone",telephone).add(changeEle,changeEleValue).build();
            }

            @Override
            public void doing() {
                String json = HttpHelper.getJson(body, url);
                code = HttpHelper.ParseJsonCode(json);
            }

            @Override
            public void doEnd() {
                if (code==0){
                    handler.sendEmptyMessage(1);
                }
            }
        }).newThread();
    }
}
