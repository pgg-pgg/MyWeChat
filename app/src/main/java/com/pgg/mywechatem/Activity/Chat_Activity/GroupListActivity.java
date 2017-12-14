package com.pgg.mywechatem.Activity.Chat_Activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.easemob.chat.EMGroup;
import com.easemob.chat.EMGroupManager;
import com.easemob.exceptions.EaseMobException;
import com.pgg.mywechatem.Activity.AddGroupChatActivity;
import com.pgg.mywechatem.Activity.BaseActivity;
import com.pgg.mywechatem.Adapter.ChatAdapter.MyGroupAdapter;
import com.pgg.mywechatem.R;
import com.pgg.mywechatem.Uitils.Utils;

import java.util.List;

/**
 * Created by PDD on 2017/12/3.
 */

public class GroupListActivity extends BaseActivity implements View.OnClickListener {
    private ImageButton ib_exit_01;
    private View vertical_line;
    private TextView title_tv_left;
    private TextView title_tv_center;
    private ListView mlistview;
    private ImageButton ib_right;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.activity_group_list);
        super.onCreate(savedInstanceState);
    }


    @Override
    public void initController() {
        ib_exit_01 =findViewById(R.id.ib_exit_01);
        vertical_line =findViewById(R.id.vertical_line);
        title_tv_left =findViewById(R.id.title_tv_left);
        title_tv_center =findViewById(R.id.title_tv_center);
        ib_right=findViewById(R.id.ib_right);
    }

    @Override
    public void initView() {
        ib_exit_01.setVisibility(View.VISIBLE);
        vertical_line.setVisibility(View.VISIBLE);
        title_tv_left.setVisibility(View.VISIBLE);
        title_tv_center.setVisibility(View.GONE);
        title_tv_left.setText("群聊列表");
        ib_exit_01.setBackgroundResource(R.drawable.ic_menu_exit_left);
        ib_right.setVisibility(View.VISIBLE);
        ib_right.setBackgroundResource(R.drawable.icon_add);
        mlistview =  findViewById(R.id.listview);
        View layout_head = Utils.getView(R.layout.layout_head_search);
        mlistview.addHeaderView(layout_head);
    }

    @Override
    public void initData() {
        try {
            List<EMGroup> grouplist = EMGroupManager.getInstance().getGroupsFromServer();
            if (grouplist != null && grouplist.size() > 0) {
                mlistview.setAdapter(new MyGroupAdapter(this, grouplist));
            } else {
                TextView txt_nodata = findViewById(R.id.txt_nochat);
                txt_nodata.setText("暂时没有群聊");
                txt_nodata.setVisibility(View.VISIBLE);
            }
        } catch (EaseMobException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initListener() {
        ib_exit_01.setOnClickListener(this);
        ib_right.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ib_exit_01:
                Utils.finish(GroupListActivity.this);
                break;
            case R.id.ib_right:
                Utils.start_Activity(GroupListActivity.this, AddGroupChatActivity.class);
                break;
        }
    }
}
