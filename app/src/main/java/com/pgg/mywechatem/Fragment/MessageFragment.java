package com.pgg.mywechatem.Fragment;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.bumptech.glide.Glide;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMConversation;
import com.easemob.chat.EMGroup;
import com.easemob.chat.EMGroupManager;
import com.easemob.chat.EMMessage;
import com.easemob.exceptions.EaseMobException;
import com.pgg.mywechatem.Activity.Chat_Activity.ChatActivity;
import com.pgg.mywechatem.Activity.Chat_Activity.PublishMsgListActivity;
import com.pgg.mywechatem.Activity.MainActivity;
import com.pgg.mywechatem.Adapter.MyMessageFragAdapter;
import com.pgg.mywechatem.Domian.GroupInfo;
import com.pgg.mywechatem.Domian.PublicMsgInfo;
import com.pgg.mywechatem.Domian.User;
import com.pgg.mywechatem.Global.BaseApplication;
import com.pgg.mywechatem.Global.GloableParams;
import com.pgg.mywechatem.Protocol.HttpHelper;
import com.pgg.mywechatem.Protocol.Net.ThreadHelper;
import com.pgg.mywechatem.R;
import com.pgg.mywechatem.Uitils.Constants;
import com.pgg.mywechatem.Uitils.NetUtil;
import com.pgg.mywechatem.Uitils.Utils;

import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Hashtable;
import java.util.List;

import okhttp3.FormBody;
import okhttp3.RequestBody;


/**
 * Created by PDD on 2017/11/16.
 */

public class MessageFragment extends BaseFragment implements AdapterView.OnItemClickListener,View.OnClickListener{

    private TextView txt_nochat;
    private ListView list_of_wechat_message;
    private MyMessageFragAdapter adapter;
    private List<EMConversation> conversationList = new ArrayList<EMConversation>();
    private View view;
    private RelativeLayout errorItem;
    public static MessageFragment fragment=null;

    @Override
    public View initView() {
        view = Utils.getView(R.layout.fragment_wechat);
        txt_nochat = view.findViewById(R.id.txt_nochat);
        list_of_wechat_message = view.findViewById(R.id.list_of_wechat_message);
        errorItem = view.findViewById(R.id.rl_error_item);
        fragment=this;
        initViews();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        conversationList.clear();
        initViews();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        fragment=null;
    }

    /**
     * 刷新页面
     */
    public void refresh() {
        conversationList.clear();
        initViews();
    }

    private void initViews() {
        conversationList.addAll(loadConversationsWithRecentChat());
        if (conversationList != null && conversationList.size() > 0) {
            txt_nochat.setVisibility(View.GONE);
            adapter = new MyMessageFragAdapter(getActivity(),conversationList);
            list_of_wechat_message.setAdapter(adapter);
        } else {
            view.findViewById(R.id.txt_nochat).setVisibility(View.VISIBLE);
        }
    }

    /**
     * 获取所有会话
     *
     * @return +
     */
    private List<EMConversation> loadConversationsWithRecentChat() {
        // 获取所有会话，包括陌生人
        Hashtable<String, EMConversation> conversations = EMChatManager.getInstance().getAllConversations();
        List<EMConversation> list = new ArrayList<EMConversation>();
        // 过滤掉messages seize为0的conversation
        for (EMConversation conversation : conversations.values()) {
            if (conversation.getAllMessages().size() != 0)
                list.add(conversation);
        }
        // 排序
        sortConversationByLastChatTime(list);
        return list;
    }

    /**
     * 根据最后一条消息的时间排序
     *
     */
    private void sortConversationByLastChatTime(List<EMConversation> conversationList) {
        Collections.sort(conversationList, new Comparator<EMConversation>() {
            @Override
            public int compare(final EMConversation con1,
                               final EMConversation con2) {

                EMMessage con2LastMessage = con2.getLastMessage();
                EMMessage con1LastMessage = con1.getLastMessage();
                if (con2LastMessage.getMsgTime() == con1LastMessage.getMsgTime()) {
                    return 0;
                } else if (con2LastMessage.getMsgTime() > con1LastMessage.getMsgTime()) {
                    return 1;
                } else {
                    return -1;
                }
            }

        });
    }

    @Override
    public void initData() {

    }

    @Override
    public void initListener() {
        list_of_wechat_message.setOnItemClickListener(this);
        errorItem.setOnClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (adapter.PublicMsg != null && position == 0) {
            // 打开订阅号列表页面
            Utils.start_Activity(getActivity(), PublishMsgListActivity.class);
        } else {
            ((MainActivity) getActivity()).updateUnreadLabel();
            final EMConversation conversation = conversationList.get(position);
            final Intent intent = new Intent(getActivity(), ChatActivity.class);
            Hashtable<String, String> ChatRecord = adapter.getChatRecord();
            if (ChatRecord != null) {
                if (conversation.isGroup()) {
                    intent.putExtra(Constants.TYPE, ChatActivity.CHATTYPE_GROUP);
                    intent.putExtra(Constants.GROUP_ID, conversation.getUserName());
                    EMGroup group = null;
                    try {
                        group = EMGroupManager.getInstance().getGroupFromServer(conversation.getUserName());
                    } catch (EaseMobException e) {
                        e.printStackTrace();
                    }
                    intent.putExtra(Constants.NAME,group.getGroupName() );// 设置标题
                    getActivity().startActivity(intent);
                    ((MainActivity) getActivity()).updateUnreadLabel();
                } else {
                    new ThreadHelper(new ThreadHelper.ThreadDoSomething() {
                        User user = null;
                        @Override
                        public void doPre() {

                        }

                        @Override
                        public void doing() {
                            RequestBody body = new FormBody.Builder().add("telephone", conversation.getUserName()).build();
                            String json = HttpHelper.getJson(body, Constants.BASE_URL+Constants.GET_INFO);
                            if (json != null) {
                                user = HttpHelper.ParseJsonUser(json);
                            }
                        }

                        @Override
                        public void doEnd() {
                            Utils.RunningMainThread(new Runnable() {
                                @Override
                                public void run() {
                                    intent.putExtra(Constants.TYPE, ChatActivity.CHATTYPE_SINGLE);
                                    intent.putExtra(Constants.User_ID,user.getTelephone());
                                    intent.putExtra(Constants.NAME,  user.getUserName());// 设置标题
                                    intent.putExtra(Constants.HEAD_URL,user.getHeadUrl());
                                    getActivity().startActivity(intent);
                                    ((MainActivity) getActivity()).updateUnreadLabel();
                                }
                            });
                        }
                    }).newThread();
                }
            }

        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_error_item:
                NetUtil.openSetNetWork(getActivity());
                break;
            default:
                break;
        }
    }
}
