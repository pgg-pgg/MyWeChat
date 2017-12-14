package com.pgg.mywechatem.Activity.Find_Activity;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bumptech.glide.Glide;
import com.easemob.chat.EMContactManager;
import com.easemob.exceptions.EaseMobException;
import com.pgg.mywechatem.Activity.BaseActivity;
import com.pgg.mywechatem.Adapter.MomentsAdapter;
import com.pgg.mywechatem.Domian.Comment;
import com.pgg.mywechatem.Domian.Moments;
import com.pgg.mywechatem.Domian.User;
import com.pgg.mywechatem.Global.BaseApplication;
import com.pgg.mywechatem.Protocol.HttpHelper;
import com.pgg.mywechatem.Protocol.Net.ThreadHelper;
import com.pgg.mywechatem.R;
import com.pgg.mywechatem.Uitils.Constants;
import com.pgg.mywechatem.Uitils.CreateDataUtils;
import com.pgg.mywechatem.Uitils.StringUtils;
import com.pgg.mywechatem.Uitils.Utils;
import com.pgg.mywechatem.View.MyDialogView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import okhttp3.FormBody;
import okhttp3.RequestBody;

/**
 * Created by PDD on 2017/11/18.
 */

public class MomentsActivity extends BaseActivity implements View.OnClickListener{
    private ImageButton ib_exit_01,ib_right;
    private View vertical_line;
    private TextView title_tv_left;
    private TextView title_tv_center,txt_name;
    private ListView list_moments;
    private View layout_head;
    private ImageView head,iv_background;
    private String tel;
    private ArrayList<Moments> data;
    private MomentsAdapter adapter;
    private LinearLayout ll_comment;
    private EditText et_comment;
    private Button btn_comment_send;
    private Moments item;
    List<Comment> comments;
    private InputMethodManager manager;//系统软件软键盘管理类


    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what==0){
                data= (ArrayList<Moments>) msg.obj;
                getComment();

            }else if (msg.what==1){
                getLoadingDialog("正在刷新...请稍后").dismiss();
                Utils.showShortToast(BaseApplication.getContext(),"刷新失败，请检查网络重试");
            }else if (msg.what==2){
                getLoadingDialog("正在刷新...请稍后").dismiss();
                comments= (List<Comment>) msg.obj;
                sortMomentList(data);
                adapter=new MomentsAdapter(data,comments);
                list_moments.setAdapter(adapter);
                adapter.setOnZanClickListener(new MomentsAdapter.OnZanClickListener() {
                    @Override
                    public void onZanClick(int position) {
                        list_moments.setSelection(position);
                        ll_comment.setVisibility(View.VISIBLE);
                        item = adapter.getItem(position);
                    }
                });
            }else if (msg.what==3){
                getLoadingDialog("正在发送...请稍后").dismiss();
                Utils.showShortToast(BaseApplication.getContext(),"评论成功");
                ll_comment.setVisibility(View.GONE);
                hideKeyboard();
                getComment();
                adapter.notifyDataSetChanged();
            }else if (msg.what==4){
                getLoadingDialog("正在发送...请稍后").dismiss();
                Utils.showShortToast(BaseApplication.getContext(),"评论失败，请重试");
            }
        }

        private void getComment() {
            new ThreadHelper(new ThreadHelper.ThreadDoSomething() {

                private List<Comment> comments;
                private int code;
                private RequestBody body;

                @Override
                public void doPre() {
                    FormBody.Builder builder = new FormBody.Builder();
                    for (int i=0;i<data.size();i++){
                        String moments_id=data.get(i).id;
                        builder.add("moments_id",moments_id);
                    }
                    body = builder.build();
                }

                @Override
                public void doing() {
                    String json= HttpHelper.getJson(body, Constants.BASE_URL+Constants.SEARCH_ALL_COMMENT);
                    code = HttpHelper.ParseJsonCode(json);
                    JSONObject jo1 = JSON.parseObject(json);
                    JSONArray ja1 = jo1.getJSONArray("data");
                    comments = JSON.parseArray(ja1.toJSONString(), Comment.class);
                }

                @Override
                public void doEnd() {
                    if (code==0){
                        Message message=new Message();
                        message.obj=comments;
                        message.what=2;
                        handler.sendMessage(message);
                    }else {
                        handler.sendEmptyMessage(1);
                    }
                }
            }).newThread();
        }
    };
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.activity_moments);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void initController() {
        ib_exit_01 =findViewById(R.id.ib_exit_01);
        vertical_line =findViewById(R.id.vertical_line);
        title_tv_left =findViewById(R.id.title_tv_left);
        title_tv_center =findViewById(R.id.title_tv_center);
        ib_right=findViewById(R.id.ib_right);
        list_moments=findViewById(R.id.list_moments);
        layout_head= Utils.getView(R.layout.list_moments_head);
        txt_name=layout_head.findViewById(R.id.txt_name);
        head=layout_head.findViewById(R.id.head);
        iv_background=layout_head.findViewById(R.id.iv_background);
        ll_comment=findViewById(R.id.ll_comment);
        et_comment=findViewById(R.id.et_comment);
        btn_comment_send=findViewById(R.id.btn_comment_send);
    }

    @Override
    public void initView() {
        ib_exit_01.setVisibility(View.VISIBLE);
        vertical_line.setVisibility(View.VISIBLE);
        title_tv_left.setVisibility(View.VISIBLE);
        title_tv_center.setVisibility(View.GONE);
        title_tv_left.setText("朋友圈");
        ib_exit_01.setBackgroundResource(R.drawable.ic_menu_exit_left);
        ib_right.setVisibility(View.VISIBLE);
        list_moments.addHeaderView(layout_head);
        tel = Utils.getValue(BaseApplication.getContext(), Constants.LOGIN_TEL);
        String username=Utils.getValue(BaseApplication.getContext(),Constants.LOGIN_NICK);
        String headUrl=Utils.getValue(BaseApplication.getContext(),Constants.LOGIN_HEAD);
        String backgroundUrl=Utils.getValue(BaseApplication.getContext(),Constants.LOGIN_BACKGROUND);
        txt_name.setText(username);
        if (backgroundUrl.contains("http")){
            Glide.with(BaseApplication.getContext()).load(backgroundUrl).into(iv_background);
        }else {
            Glide.with(BaseApplication.getContext()).load(Constants.BASE_URL+backgroundUrl).into(iv_background);
        }
        if (headUrl.contains("http")){
            Glide.with(BaseApplication.getContext()).load(headUrl).into(head);
        }else {
            Glide.with(BaseApplication.getContext()).load(Constants.BASE_URL+headUrl).into(head);
        }
        getLoadingDialog("正在刷新...请稍后").show();
        manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);//获取系统软键盘管理类
    }

    @Override
    protected void onResume() {
        super.onResume();
        initData();
    }

    @Override
    public void initData() {
        //1获取该用户所有好友
        new ThreadHelper(new ThreadHelper.ThreadDoSomething() {

            private List<Moments> momentses;
            private List<String> usernames;
            private List<User> users;

            @Override
            public void doPre() {
                try {
                    //需异步执行
                    usernames = EMContactManager.getInstance().getContactUserNames();
                } catch (EaseMobException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void doing() {
                if (usernames != null) {
                    usernames.add(Utils.getValue(BaseApplication.getContext(),Constants.LOGIN_TEL));
                    FormBody.Builder builder1 = new FormBody.Builder();
                    FormBody.Builder builder2 = new FormBody.Builder();
                    for (int i = 0; i < usernames.size(); i++) {
                        builder1.add("telephone", usernames.get(i));
                        builder2.add("userId",usernames.get(i));
                    }
                    RequestBody body1 = builder1.build();
                    String jsonUser = HttpHelper.getJson(body1, Constants.BASE_URL + Constants.GET_FRIENDS);
                    RequestBody body2=builder2.build();
                    String jsonMoments=HttpHelper.getJson(body2,Constants.BASE_URL+Constants.SEARCH_MOMENTS);
                    if (jsonUser != null&jsonMoments!=null) {
                        JSONObject jo = JSON.parseObject(jsonUser);
                        JSONArray ja = jo.getJSONArray("data");
                        users = JSON.parseArray(ja.toJSONString(), User.class);
                        JSONObject jo1 = JSON.parseObject(jsonMoments);
                        JSONArray ja1 = jo1.getJSONArray("data");
                        momentses = JSON.parseArray(ja1.toJSONString(), Moments.class);
                        for (int i=0;i<users.size();i++){
                            for (int j=0;j<momentses.size();j++){
                                if (users.get(i).getTelephone().equals(momentses.get(j).userId)){
                                    momentses.get(j).userHeadUrl=users.get(i).getHeadUrl();
                                    momentses.get(j).userName=users.get(i).getUserName();
                                }
                            }
                        }
                    }
                }
            }

            @Override
            public void doEnd() {
                if (momentses != null) {
                    Message message = new Message();
                    message.what = 0;
                    message.obj = momentses;
                    handler.sendMessage(message);
                }else {
                    handler.sendEmptyMessage(1);
                }
            }
        }).newThread();
    }

    @Override
    public void initListener() {
        ib_exit_01.setOnClickListener(this);
        ib_right.setOnClickListener(this);
        iv_background.setOnClickListener(this);
        head.setOnClickListener(this);
        btn_comment_send.setOnClickListener(this);
        ib_right.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Utils.start_Activity(MomentsActivity.this,ShareMomentsActivity.class);
                return false;
            }
        });
        list_moments.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                hideKeyboard();
                ll_comment.setVisibility(View.GONE);
                return false;
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (ll_comment.getVisibility()==View.VISIBLE){
                ll_comment.setVisibility(View.GONE);
            }
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ib_exit_01:
                finish_activity(MomentsActivity.this);
                break;
            case R.id.ib_right:
                //发朋友圈
                showCameraDialog();
                break;
            case R.id.iv_background:
                //修改背景图片
                ChangeBackgroundDialog();
                break;
            case R.id.head:
                //跳转到自己的朋友圈
                break;
            case R.id.btn_comment_send:
                //发送评论按钮
                AddComment(item);
                break;
        }
    }

    private void AddComment(final Moments item) {
        final String content=et_comment.getText().toString();
        if (StringUtils.isEmpty(content)){
            Utils.showShortToast(BaseApplication.getContext(),"还没有填写你的评论内容哦！");
        }else {
            getLoadingDialog("正在发送...请稍后").show();
            new ThreadHelper(new ThreadHelper.ThreadDoSomething() {

                private int code;

                @Override
                public void doPre() {

                }

                @Override
                public void doing() {
                    RequestBody body=new FormBody.Builder().add("moments_id",item.id)
                            .add("content",content)
                            .add("userId",Utils.getValue(BaseApplication.getContext(),Constants.LOGIN_TEL))
                            .build();
                    String json=HttpHelper.getJson(body,Constants.BASE_URL+Constants.INSERT_COMMENT);
                    code = HttpHelper.ParseJsonCode(json);
                }

                @Override
                public void doEnd() {
                    if (code==0){
                        handler.sendEmptyMessage(3);
                    }else {
                        handler.sendEmptyMessage(4);
                    }
                }
            }).newThread();
        }

    }

    private void ChangeBackgroundDialog() {
        final MyDialogView dialog = new MyDialogView(MomentsActivity.this);
        View view1= Utils.getView(R.layout.activity_setting_pic);
        view1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dialog != null) {
                    dialog.cancel();
                }
            }
        });
        Button btn_change_back = view1.findViewById(R.id.btn_change_back);
        btn_change_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                if (dialog != null) {
                    dialog.cancel();
                    Utils.start_Activity(MomentsActivity.this,ChangeBackgroundActivity.class);
                }
            }
        });
        dialog.show();
        dialog.setCancelable(false);
        dialog.setContentView(view1);// show方法要在前面
    }

    private void showCameraDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MomentsActivity.this);
        final AlertDialog alertDialog = builder.create();
        alertDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        View view = Utils.getView(R.layout.dialog_show_face);
        RelativeLayout tv_open_camera = view.findViewById(R.id.tv_open_camera);
        TextView tv_open_photo =view.findViewById(R.id.tv_open_photo);
        alertDialog.setView(view,0,0,0,0);
        alertDialog.show();
        tv_open_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

        tv_open_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
    }

    /**
     * 隐藏软键盘
     */
    private void hideKeyboard() {
        if (getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
            if (getCurrentFocus() != null)
                manager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    private void sortMomentList(List<Moments> momentses) {
        Collections.sort(momentses, new Comparator<Moments>() {
            @Override
            public int compare(final Moments con1,
                               final Moments con2) {
                if (con1.time == con2.time) {
                    return 0;
                } else if (con1.time < con2.time) {
                    return 1;
                } else {
                    return -1;
                }
            }

        });
    }

}
