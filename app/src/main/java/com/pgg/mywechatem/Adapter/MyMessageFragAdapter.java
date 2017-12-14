package com.pgg.mywechatem.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import com.bumptech.glide.Glide;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMConversation;
import com.easemob.chat.EMGroup;
import com.easemob.chat.EMGroupManager;
import com.easemob.chat.EMMessage;
import com.easemob.chat.ImageMessageBody;
import com.easemob.chat.TextMessageBody;
import com.easemob.exceptions.EaseMobException;
import com.easemob.util.DateUtils;
import com.pgg.mywechatem.Domian.Constant;
import com.pgg.mywechatem.Domian.PublicMsgInfo;
import com.pgg.mywechatem.Domian.User;
import com.pgg.mywechatem.Global.BaseApplication;
import com.pgg.mywechatem.Holder.ViewHolder;
import com.pgg.mywechatem.Protocol.HttpHelper;
import com.pgg.mywechatem.Protocol.Net.ThreadHelper;
import com.pgg.mywechatem.R;
import com.pgg.mywechatem.Uitils.Constants;
import com.pgg.mywechatem.Uitils.SmileUtils;
import com.pgg.mywechatem.Uitils.Utils;
import com.pgg.mywechatem.View.SwipeLayout;
import com.pgg.mywechatem.View.WarnTipDialog;

import java.util.Date;
import java.util.Hashtable;
import java.util.List;

import okhttp3.FormBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;

/**
 * Created by PDD on 2017/11/21.
 */

public class MyMessageFragAdapter extends BaseAdapter {


    public PublicMsgInfo PublicMsg = null;
    public List<EMConversation> conversationList;
    private Hashtable<String, String> ChatRecord = new Hashtable<>();
    private WarnTipDialog Tipdialog;
    private int deleteID;
    private String ChatID;
    private String userid;
    private User user;

    public MyMessageFragAdapter(Context ctx, List<EMConversation> conversationList) {
        this.conversationList = conversationList;
        userid = Utils.getValue(BaseApplication.getContext(), Constants.User_ID);
    }

    @Override
    public int getCount() {
        return conversationList.size();
    }

    public Hashtable<String, String> getChatRecord() {
        return ChatRecord;
    }

    @Override
    public EMConversation getItem(int position) {
        return conversationList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = Utils.getView(R.layout.item_wechat_list);
        }
        final ImageView img_avar = ViewHolder.get(convertView, R.id.contactitem_avatar_iv);
        final TextView txt_name = ViewHolder.get(convertView, R.id.txt_name);
        TextView txt_state = ViewHolder.get(convertView, R.id.txt_state);
        TextView txt_del = ViewHolder.get(convertView, R.id.txt_del);
        TextView txt_content = ViewHolder.get(convertView, R.id.txt_content);
        TextView txt_time = ViewHolder.get(convertView, R.id.txt_time);
        TextView unreadLabel = ViewHolder.get(convertView, R.id.unread_msg_number);
        SwipeLayout swipe = ViewHolder.get(convertView, R.id.swipe);
        if (PublicMsg != null && position == 0) {
            txt_name.setText(R.string.official_accounts);
            img_avar.setImageResource(R.drawable.icon_public);
            txt_time.setText(PublicMsg.getTime());
            txt_content.setText(PublicMsg.getContent());
            unreadLabel.setText("3");
            unreadLabel.setVisibility(View.VISIBLE);
            swipe.setSwipeEnabled(false);
        } else {
            swipe.setSwipeEnabled(true);
            // 获取与此用户/群组的会话
            final EMConversation conversation = conversationList.get(position);
            // 获取用户username或者群组groupid
            ChatID = conversation.getUserName();
            if (conversation.isGroup()) {
                img_avar.setImageResource(R.drawable.defult_group);
                //根据群聊ID从本地获取群聊基本信息
                EMGroup group = null;
                try {
                    group = EMGroupManager.getInstance().getGroupFromServer(ChatID);
                } catch (EaseMobException e) {
                    e.printStackTrace();
                }
                if (group != null) {
                    txt_name.setText(group.getGroupName());
                } else {

                }
            } else {
                new ThreadHelper(new ThreadHelper.ThreadDoSomething() {
                    User user = null;

                    @Override
                    public void doPre() {

                    }

                    @Override
                    public void doing() {
                        RequestBody body = new FormBody.Builder().add("telephone", ChatID).build();
                        String json = HttpHelper.getJson(body, Constants.BASE_URL + Constants.GET_INFO);
                        if (json != null) {
                            user = HttpHelper.ParseJsonUser(json);
                        }
                    }

                    @Override
                    public void doEnd() {
                        Utils.RunningMainThread(new Runnable() {
                            @Override
                            public void run() {
                                if (user != null) {
                                    txt_name.setText(user.getUserName());
                                    if (user.getHeadUrl().contains("http")){
                                        Glide.with(BaseApplication.getContext()).load(user.getHeadUrl()).into(img_avar);
                                    }else {
                                        Glide.with(BaseApplication.getContext()).load(Constants.BASE_URL+user.getHeadUrl()).into(img_avar);
                                    }
                                }
                            }
                        });
                    }
                }).newThread();
            }
            txt_del.setTag(ChatID);
            if (conversation.getUnreadMsgCount() > 0) {
                // 显示与此用户的消息未读数
                unreadLabel.setText(String.valueOf(conversation
                        .getUnreadMsgCount()));
                unreadLabel.setVisibility(View.VISIBLE);
            } else {
                unreadLabel.setVisibility(View.INVISIBLE);
            }
            if (conversation.getMsgCount() != 0) {
                // 把最后一条消息的内容作为item的message内容
                EMMessage lastMessage = conversation.getLastMessage();
                txt_content.setText(
                        SmileUtils.getSmiledText(BaseApplication.getContext(), getMessageDigest(lastMessage, BaseApplication.getContext())), TextView.BufferType.SPANNABLE);
                txt_time.setText(DateUtils.getTimestampString(new Date(
                        lastMessage.getMsgTime())));
                if (lastMessage.status == EMMessage.Status.SUCCESS) {
                    txt_state.setText("送达");
                } else if (lastMessage.status == EMMessage.Status.FAIL) {
                    txt_state.setText("失败");
                } else if (lastMessage.direct == EMMessage.Direct.RECEIVE) {
                    txt_state.setText("已读");
                    txt_state.setBackgroundResource(R.drawable.btn_bg_blue);
                }
            }

            txt_del.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    deleteID = position;
                    Tipdialog = new WarnTipDialog(BaseApplication.getContext(), "您确定要删除该聊天吗？", "  ", View.VISIBLE);
                    Tipdialog.setBtnOkListener(onclick);
                    Tipdialog.show();
                }
            });
        }
        return convertView;
    }

    private DialogInterface.OnClickListener onclick = new DialogInterface.OnClickListener() {

        @Override
        public void onClick(DialogInterface dialog, int which) {
            EMConversation conversation = conversationList.get(deleteID);
            EMChatManager.getInstance().deleteConversation(conversation.getUserName());
            conversationList.remove(deleteID);
            notifyDataSetChanged();
            Tipdialog.dismiss();
        }
    };

    /**
     * 根据消息内容和消息类型获取消息内容提示
     *
     * @param message
     * @param context
     * @return
     */
    private String getMessageDigest(EMMessage message, Context context) {
        String digest = "";
        switch (message.getType()) {
            case LOCATION: // 位置消息
                if (message.direct == EMMessage.Direct.RECEIVE) {
                    digest = getString(context, R.string.location_recv);
                    return digest;
                } else {
                    digest = getString(context, R.string.location_prefix);
                }
                break;
            case IMAGE: // 图片消息
                ImageMessageBody imageBody = (ImageMessageBody) message.getBody();
                digest = getString(context, R.string.picture) + imageBody.getFileName();
                break;
            case VOICE:// 语音消息
                digest = getString(context, R.string.voice_msg);
                break;
            case VIDEO: // 视频消息
                digest = getString(context, R.string.video);
                break;
            case TXT: // 文本消息
                if (!message.getBooleanAttribute(Constant.MESSAGE_ATTR_IS_VOICE_CALL, false)) {
                    TextMessageBody txtBody = (TextMessageBody) message.getBody();
                    digest = txtBody.getMessage();
                } else {
                    TextMessageBody txtBody = (TextMessageBody) message.getBody();
                    digest = getString(context, R.string.voice_call) + txtBody.getMessage();
                }
                break;
            case FILE: // 普通文件消息
                digest = getString(context, R.string.file);
                break;
            default:
                System.err.println("error, unknow type");
                return "";
        }
        return digest;
    }

    String getString(Context context, int resId) {
        return context.getResources().getString(resId);
    }

}
