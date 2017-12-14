package com.pgg.mywechatem.Fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMContactListener;
import com.easemob.chat.EMContactManager;
import com.easemob.chat.EMNotifier;
import com.easemob.exceptions.EaseMobException;
import com.pgg.mywechatem.Activity.Chat_Activity.ChatActivity;
import com.pgg.mywechatem.Activity.Chat_Activity.FriendMsgActivity;
import com.pgg.mywechatem.Activity.Chat_Activity.GroupListActivity;
import com.pgg.mywechatem.Activity.MainActivity;
import com.pgg.mywechatem.Adapter.ContactAdapter;
import com.pgg.mywechatem.Domian.User;
import com.pgg.mywechatem.Global.BaseApplication;
import com.pgg.mywechatem.Protocol.HttpHelper;
import com.pgg.mywechatem.Protocol.Net.ThreadHelper;
import com.pgg.mywechatem.R;
import com.pgg.mywechatem.Uitils.Constants;
import com.pgg.mywechatem.Uitils.Utils;
import com.pgg.mywechatem.View.FlippingLoadingDialog;
import com.pgg.mywechatem.View.SideBar;

import org.apache.http.message.BasicNameValuePair;

import java.util.List;

import okhttp3.FormBody;
import okhttp3.RequestBody;

/**
 * Created by PDD on 2017/11/16.
 */

public class ContactFragment extends BaseFragment implements View.OnClickListener, AdapterView.OnItemClickListener {
    private ListView lvContact;
    private SideBar sideBar;
    private TextView mDialogText;
    private WindowManager mWindowManager;
    private Activity ctx;
    private View layout_head;
    private List<User> userList;
    public static ContactFragment contactFragment=null;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0) {
                userList = (List<User>) msg.obj;
                ContactAdapter adapter = new ContactAdapter(userList);
                lvContact.setAdapter(adapter);
            }
        }
    };

    @Override
    public View initView() {
        ctx = this.getActivity();
        mWindowManager = (WindowManager) ctx.getSystemService(Context.WINDOW_SERVICE);
        View view = Utils.getView(R.layout.fragment_contact_list);
        lvContact = view.findViewById(R.id.lvContact);
        sideBar = view.findViewById(R.id.sideBar);
        mDialogText = (TextView) Utils.getView(R.layout.list_position);
        mDialogText.setVisibility(View.INVISIBLE);
        sideBar.setListView(lvContact);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_APPLICATION,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);
        mWindowManager.addView(mDialogText, lp);
        sideBar.setTextView(mDialogText);
        layout_head = Utils.getView(R.layout.layout_head_friend);
        lvContact.addHeaderView(layout_head);
        contactFragment=this;
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        contactFragment=null;
    }

    @Override
    public void initData() {
        EMChatManager.getInstance().getChatOptions().setUseRoster(true);
        new ThreadHelper(new ThreadHelper.ThreadDoSomething() {
            List<String> usernames;
            List<User> users;

            @Override
            public void doPre() {
                try {
                    usernames = EMContactManager.getInstance().getContactUserNames();
                } catch (EaseMobException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void doing() {
                if (usernames != null) {
                    FormBody.Builder builder = new FormBody.Builder();
                    for (int i = 0; i < usernames.size(); i++) {
                        builder.add("telephone", usernames.get(i));
                    }
                    RequestBody body = builder.build();
                    String json = HttpHelper.getJson(body, Constants.BASE_URL + Constants.GET_FRIENDS);
                    if (json != null) {
                        JSONObject jo = JSON.parseObject(json);
                        JSONArray ja = jo.getJSONArray("data");
                        users = JSON.parseArray(ja.toJSONString(), User.class);
                    }
                }
            }

            @Override
            public void doEnd() {
                if (users != null) {
                    Message message = new Message();
                    message.what = 0;
                    message.obj = users;
                    handler.sendMessage(message);
                }
            }
        }).newThread();
    }

    @Override
    public void initListener() {
        layout_head.findViewById(R.id.layout_addfriend).setOnClickListener(this);
        layout_head.findViewById(R.id.layout_label).setOnClickListener(this);
        layout_head.findViewById(R.id.layout_group).setOnClickListener(this);
        layout_head.findViewById(R.id.layout_public).setOnClickListener(this);
        lvContact.setOnItemClickListener(this);
    }

    @Override
    public void onDestroy() {
        mWindowManager.removeView(mDialogText);
        super.onDestroy();
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        User user = userList.get(position - 1);
        if (user != null) {
            Utils.start_Activity(getActivity(), FriendMsgActivity.class, new BasicNameValuePair(Constants.User_ID, user.getTelephone())
                    , new BasicNameValuePair(Constants.NAME, user.getUserName())
                    , new BasicNameValuePair(Constants.HEAD_URL, user.getHeadUrl()));
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.layout_addfriend:
                //头布局"新的朋友"点击事件
                break;
            case R.id.layout_label:
                //头布局"标签"点击事件
                break;
            case R.id.layout_group:
                //头布局"群组"点击事件
                Utils.start_Activity(getActivity(), GroupListActivity.class);
                break;
            case R.id.layout_public:
                //头布局"公众号"点击事件
                break;
        }
    }
}
