package com.pgg.mywechatem.Activity.Chat_Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMGroup;
import com.easemob.chat.EMGroupManager;
import com.easemob.exceptions.EaseMobException;
import com.easemob.util.NetUtils;
import com.pgg.mywechatem.Activity.AddGroupChatActivity;
import com.pgg.mywechatem.Activity.BaseActivity;
import com.pgg.mywechatem.Domian.User;
import com.pgg.mywechatem.Global.BaseApplication;
import com.pgg.mywechatem.Protocol.HttpHelper;
import com.pgg.mywechatem.Protocol.Net.ThreadHelper;
import com.pgg.mywechatem.R;
import com.pgg.mywechatem.Uitils.Constants;
import com.pgg.mywechatem.Uitils.Utils;
import com.pgg.mywechatem.View.ExpandGridView;

import java.util.ArrayList;
import java.util.List;

import okhttp3.FormBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;

/**
 * Created by PDD on 2017/11/28.
 */

public class GroupSettingActivity extends BaseActivity implements View.OnClickListener {


    private ImageButton ib_exit_01;
    private View vertical_line;
    private TextView title_tv_left;
    private TextView title_tv_center;
    private TextView tv_groupname;
    int m_total = 0;// 成员总数
    private ExpandGridView gridview;// 成员列表
    // 修改群名称、置顶、、、、
    private RelativeLayout re_change_groupname;
    private RelativeLayout rl_switch_chattotop;
    private RelativeLayout rl_switch_block_groupmsg;
    private RelativeLayout re_clear;

    // 状态变化
    private CheckBox check_top, check_closetip;
    // 删除并退出

    private Button exitBtn;
    private String hxid;
    boolean is_admin = false;// 是否是管理员
    List<User> members = new ArrayList<>();
    String longClickUsername = null;

    private String groupId;
    private EMGroup group;
    private GridAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.activity_groupsetting);
        super.onCreate(savedInstanceState);
    }



    @Override
    public void initController() {
        ib_exit_01 = findViewById(R.id.ib_exit_01);
        vertical_line = findViewById(R.id.vertical_line);
        title_tv_left = findViewById(R.id.title_tv_left);
        title_tv_center = findViewById(R.id.title_tv_center);

        tv_groupname =  findViewById(R.id.txt_groupname);
        gridview =  findViewById(R.id.gridview);

        re_change_groupname =  findViewById(R.id.re_change_groupname);
        rl_switch_chattotop =  findViewById(R.id.rl_switch_chattotop);
        rl_switch_block_groupmsg =  findViewById(R.id.rl_switch_block_groupmsg);
        re_clear =  findViewById(R.id.re_clear);

        exitBtn =  findViewById(R.id.btn_exit_grp);
    }

    @Override
    public void initView() {
        ib_exit_01.setVisibility(View.VISIBLE);
        vertical_line.setVisibility(View.VISIBLE);
        title_tv_left.setVisibility(View.VISIBLE);
        title_tv_center.setVisibility(View.GONE);
        title_tv_left.setText("群聊信息");
        ib_exit_01.setBackgroundResource(R.drawable.ic_menu_exit_left);
        hxid = Utils.getValue(GroupSettingActivity.this, Constants.User_ID);
    }

    @Override
    public void initData() {
        // 获取传过来的groupid
        groupId = getIntent().getStringExtra(Constants.GROUP_ID);
        // 获取本地该群数据
        try {
            group = EMGroupManager.getInstance().getGroupFromServer(groupId);
        } catch (EaseMobException e) {
            e.printStackTrace();
        }
        if (group == null) {
            try {
                // 去网络中查找该群
                group = EMGroupManager.getInstance().getGroupFromServer(groupId);
                if (group == null) {
                    Toast.makeText(GroupSettingActivity.this, "该群已经被解散...", Toast.LENGTH_SHORT).show();
                    setResult(100);
                    finish();
                    return;
                }
            } catch (EaseMobException e) {
                e.printStackTrace();
                return;
            }
        }

        // 获取封装的群名（里面封装了显示的群名和群组成员的信息）
        String group_name = group.getGroupName();
        // 获取群成员信息
        tv_groupname.setText(group_name);

        if (group != null) {
            final List<String> users=group.getMembers();
            if (users != null && users.size() > 0) {
                m_total = users.size();
                title_tv_left.setText("聊天信息(" + String.valueOf(m_total) + ")");
                // 解析群组成员信息
                for (int i = 0; i < m_total; i++) {
                    final int finalI = i;
                    new ThreadHelper(new ThreadHelper.ThreadDoSomething() {
                        @Override
                        public void doPre() {
                            RequestBody body=new FormBody.Builder().add("telephone",users.get(finalI)).build();
                            User user = HttpHelper.ParseJsonUser(HttpHelper.getJson(body,Constants.BASE_URL+Constants.GET_INFO));
                            if (user == null) {
                                user = new User();
                                user.setTelephone(users.get(finalI));
                            }
                            members.add(user);
                        }

                        @Override
                        public void doing() {

                        }

                        @Override
                        public void doEnd() {
                            // 显示群组成员头像和昵称
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    showMembers(members);
                                }
                            });
                        }
                    }).newThread();
                }

                // 判断是否是群主，是群主有删成员的权限，并显示减号按钮
                if (null != group.getOwner() && null != hxid && hxid.equals(group.getOwner())) {
                    is_admin = true;
                }
            }
        }
    }

    // 显示群成员头像昵称的gridview
    private void showMembers(List<User> members) {
        adapter = new GridAdapter(this, members);
        gridview.setAdapter(adapter);

        // 设置OnTouchListener,为了让群主方便地推出删除模》
        gridview.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        if (adapter.isInDeleteMode) {
                            adapter.isInDeleteMode = false;
                            adapter.notifyDataSetChanged();
                            return true;
                        }
                        break;
                    default:
                        break;
                }
                return false;
            }
        });
    }

    @Override
    public void initListener() {
        re_change_groupname.setOnClickListener(this);
        rl_switch_chattotop.setOnClickListener(this);
        rl_switch_block_groupmsg.setOnClickListener(this);
        re_clear.setOnClickListener(this);
        exitBtn.setOnClickListener(this);
        ib_exit_01.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ib_exit_01:
                Utils.finish(GroupSettingActivity.this);
                break;
            case R.id.btn_exit_grp:
                // deleteMembersFromGroup(hxid);
                break;
            default:
                break;
        }
    }


    class GridAdapter extends BaseAdapter {

        public boolean isInDeleteMode;
        private List<User> objects;
        Context context;

        public GridAdapter(Context context, List<User> objects) {

            this.objects = objects;
            this.context = context;
            isInDeleteMode = false;
        }

        @Override
        public View getView(final int position, View convertView, final ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(R.layout.item_chatsetting_gridview, null);
            }
            ImageView iv_avatar =  convertView
                    .findViewById(R.id.iv_avatar);
            TextView tv_username = (TextView) convertView
                    .findViewById(R.id.tv_username);
            ImageView badge_delete = (ImageView) convertView
                    .findViewById(R.id.badge_delete);

            // 最后一个item，减人按钮
            if (position == getCount() - 1 && is_admin) {
                tv_username.setText("");
                badge_delete.setVisibility(View.GONE);
                iv_avatar.setImageResource(R.drawable.icon_btn_deleteperson);
                if (isInDeleteMode) {
                    // 正处于删除模式下，隐藏删除按钮
                    convertView.setVisibility(View.GONE);
                } else {

                    convertView.setVisibility(View.VISIBLE);
                }

                iv_avatar.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        isInDeleteMode = true;
                        notifyDataSetChanged();
                    }
                });

            } else if ((is_admin && position == getCount() - 2)
                    || (!is_admin && position == getCount() - 1)) { // 添加群组成员按钮
                tv_username.setText("");
                badge_delete.setVisibility(View.GONE);
                iv_avatar.setImageResource(R.drawable.jy_drltsz_btn_addperson);
                // 正处于删除模式下,隐藏添加按钮
                if (isInDeleteMode) {
                    convertView.setVisibility(View.GONE);
                } else {
                    convertView.setVisibility(View.VISIBLE);
                }
                iv_avatar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // 进入选人页面
                        startActivity((new Intent(GroupSettingActivity.this,
                                AddGroupChatActivity.class).putExtra(
                                Constants.GROUP_ID, groupId)));
                        BaseApplication.getBaseApplication().addActivity(GroupSettingActivity.this);
                    }
                });
            } else { // 普通item，显示群组成员

                final User user = members.get(position);
                String usernick = user.getUserName();
                final String userhid = user.getTelephone();
                final String useravatar = user.getHeadUrl();
                tv_username.setText(usernick);
                if (user.getHeadUrl().contains("http")){
                    Glide.with(BaseApplication.getContext()).load(user.getHeadUrl()).into(iv_avatar);
                }else {
                    Glide.with(BaseApplication.getContext()).load(Constants.BASE_URL+user.getHeadUrl()).into(iv_avatar);
                }
                if (isInDeleteMode) {
                    // 如果是删除模式下，显示减人图标
                    convertView.findViewById(R.id.badge_delete).setVisibility(
                            View.VISIBLE);
                } else {
                    convertView.findViewById(R.id.badge_delete).setVisibility(
                            View.INVISIBLE);
                }
                iv_avatar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (isInDeleteMode) {
                            // 如果是删除自己，return
                            if (EMChatManager.getInstance().getCurrentUser().equals(userhid)) {
                                Utils.showLongToast(GroupSettingActivity.this, "不能删除自己");
                                return;
                            }
                            if (!NetUtils.hasNetwork(getApplicationContext())) {
                                Toast.makeText(
                                        getApplicationContext(),
                                        getString(R.string.network_unavailable),
                                        Toast.LENGTH_SHORT).show();
                                return;
                            }
                            // deleteMembersFromGroup(userhid);//TODO
                        } else {
                            // 正常情况下点击user，可以进入用户详情或者聊天页面等等
                            Intent intent = new Intent(GroupSettingActivity.this, ChatActivity.class);
                            intent.putExtra(Constants.NAME, user.getUserName());
                            intent.putExtra(Constants.TYPE, ChatActivity.CHATTYPE_SINGLE);
                            intent.putExtra(Constants.User_ID, user.getTelephone());
                            intent.putExtra(Constants.HEAD_URL,user.getHeadUrl());
                            startActivity(intent);
                            overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                        }
                    }
                });
            }
            return convertView;
        }

        @Override
        public int getCount() {
            if (is_admin) {
                return objects.size() + 2;
            } else {

                return objects.size() + 1;
            }
        }

        @Override
        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return objects.get(position);
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return position;
        }
    }
}
