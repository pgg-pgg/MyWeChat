package com.pgg.mywechatem.Activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bumptech.glide.Glide;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMContactManager;
import com.easemob.chat.EMGroup;
import com.easemob.chat.EMGroupManager;
import com.easemob.exceptions.EaseMobException;
import com.easemob.util.DensityUtil;
import com.pgg.mywechatem.Activity.Chat_Activity.ChatActivity;
import com.pgg.mywechatem.Adapter.ContactAdapter;
import com.pgg.mywechatem.Domian.User;
import com.pgg.mywechatem.Global.BaseApplication;
import com.pgg.mywechatem.Holder.ViewHolder;
import com.pgg.mywechatem.Protocol.HttpHelper;
import com.pgg.mywechatem.Protocol.Net.ThreadHelper;
import com.pgg.mywechatem.R;
import com.pgg.mywechatem.Uitils.Constants;
import com.pgg.mywechatem.Uitils.PingYinUtil;
import com.pgg.mywechatem.Uitils.PinyinComparator;
import com.pgg.mywechatem.Uitils.StringUtils;
import com.pgg.mywechatem.Uitils.Utils;
import com.pgg.mywechatem.View.SideBar;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import okhttp3.FormBody;
import okhttp3.RequestBody;


/**
 * Created by PDD on 2017/11/17.
 */

public class AddGroupChatActivity extends BaseActivity implements AdapterView.OnItemClickListener {

    private ImageButton ib_exit_01;
    private View vertical_line;
    private TextView title_tv_left;
    private TextView title_tv_center;
    private Button btn_sure;
    private LinearLayout menuLinerLayout;
    private EditText et_search;
    private ListView listView;
    private ImageView iv_search;
    private TextView mDialogText;
    private SideBar indexBar;
    private WindowManager mWindowManager;
    private TextView tv_header;
    private String hxid;
    private String groupId;
    private String userId;
    private boolean isCreatingNewGroup;
    private EMGroup group;
    private List<String> exitingMembers=new ArrayList<>();;
    private String groupname;
    private int total;

    private List<String> addList = new ArrayList<String>();
    private ArrayList<User> alluserList;
    private ContactAdapter contactAdapter;
    private boolean isSignleChecked;

    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0) {
                alluserList = (ArrayList<User>) msg.obj;
                contactAdapter = new ContactAdapter(AddGroupChatActivity.this, alluserList);
                listView.setAdapter(contactAdapter);
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.activity_add_chat);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mWindowManager.removeView(mDialogText);
    }

    @Override
    public void initController() {
        ib_exit_01 = findViewById(R.id.ib_exit_01);
        vertical_line = findViewById(R.id.vertical_line);
        title_tv_left = findViewById(R.id.title_tv_left);
        title_tv_center = findViewById(R.id.title_tv_center);
        menuLinerLayout = findViewById(R.id.linearLayoutMenu);
        btn_sure = findViewById(R.id.btn_sure);
        btn_sure.setTextColor(0xFF45C01A);
        et_search = findViewById(R.id.et_search);
        listView = findViewById(R.id.list);
        iv_search = findViewById(R.id.iv_search);
        mDialogText = (TextView) Utils.getView(R.layout.list_position);
        mDialogText.setVisibility(View.INVISIBLE);
        indexBar = findViewById(R.id.sideBar);
        indexBar.setListView(listView);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_APPLICATION,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
                        | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);
        mWindowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        mWindowManager.addView(mDialogText, lp);
        indexBar.setTextView(mDialogText);
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        View headerView = layoutInflater.inflate(R.layout.item_chatroom_header, null);
        tv_header =  headerView.findViewById(R.id.tv_header);
        listView.addHeaderView(headerView);
        listView.setOnItemClickListener(this);
    }

    @Override
    public void initView() {
        ib_exit_01.setVisibility(View.VISIBLE);
        vertical_line.setVisibility(View.VISIBLE);
        title_tv_left.setVisibility(View.VISIBLE);
        title_tv_center.setVisibility(View.GONE);
        title_tv_left.setText("发起群聊");
        ib_exit_01.setBackgroundResource(R.drawable.ic_menu_exit_left);
        btn_sure.setVisibility(View.VISIBLE);

        hxid = Utils.getValue(AddGroupChatActivity.this, Constants.User_ID);
        groupId = getIntent().getStringExtra(Constants.GROUP_ID);
        userId = getIntent().getStringExtra(Constants.User_ID);
        if (groupId != null) {
            isCreatingNewGroup = false;
            group = EMGroupManager.getInstance().getGroup(groupId);
            if (group != null) {
                exitingMembers = group.getMembers();
                groupname = group.getGroupName();
            }
        } else if (userId != null) {
            isCreatingNewGroup = true;
            exitingMembers.add(userId);
            total = 1;
            addList.add(userId);
        } else {
            isCreatingNewGroup = true;
        }
        btn_sure.setEnabled(true);
        btn_sure.setBackgroundDrawable(getResources().getDrawable(R.drawable.btn_bg_green));
    }

    @Override
    public void initData() {
        // 获取好友列表
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
        ib_exit_01.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.finish(AddGroupChatActivity.this);
            }
        });
        btn_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                save();
            }
        });

        et_search.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    String str_s = et_search.getText().toString().trim();
                    List<User> users_temp = new ArrayList<User>();
                    for (User user : alluserList) {
                        String usernick = user.getUserName();
                        if (usernick.contains(str_s)) {
                            users_temp.add(user);
                        }
                        contactAdapter = new ContactAdapter(AddGroupChatActivity.this, users_temp);
                        listView.setAdapter(contactAdapter);
                    }
                } else {
                    contactAdapter = new ContactAdapter(AddGroupChatActivity.this, alluserList);
                    listView.setAdapter(contactAdapter);
                }
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void afterTextChanged(Editable s) {
            }
        });
    }


    /**
     * 确认选择的members
     */
    public void save() {
        if (addList.size() == 0) {
            Utils.showLongToast(AddGroupChatActivity.this, "请选择用户");
            return;
        }
        // 如果只有一个用户说明只是单聊,并且不是从群组加人
        if (addList.size() == 1 && isCreatingNewGroup) {
            String userId = addList.get(0);
            RequestBody body=new FormBody.Builder().add("telephone",userId).build();
            User user = HttpHelper.ParseJsonUser(HttpHelper.getJson(body,Constants.BASE_URL+Constants.GET_INFO));
            Intent intent = new Intent(AddGroupChatActivity.this, ChatActivity.class);
            intent.putExtra(Constants.NAME, user.getUserName());
            intent.putExtra(Constants.TYPE, ChatActivity.CHATTYPE_SINGLE);
            intent.putExtra(Constants.User_ID, user.getTelephone());
            startActivity(intent);
            overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
        } else {
            if (isCreatingNewGroup) {
                getLoadingDialog("正在创建群聊...").show();
                creatNewGroup(addList);// 创建群组
            } else {
                getLoadingDialog("正在加人...").show();
                addNewMembersToGroup(addList);
            }

        }
    }

    private void addNewMembersToGroup(final List<String> addList) {
        // TODO 请求服务器创建群组，服务端实现接口
        new Thread(new Runnable() {
            @Override
            public void run() {
                // 调用sdk创建群组方法
                String[] names=new String[addList.size()];
                for (int i=0;i<addList.size();i++){
                    names[i]=addList.get(i);
                }
                try {
                    EMGroupManager.getInstance().addUsersToGroup(groupId, names);//需异步处理
                    runOnUiThread(new Runnable() {
                        public void run() {
                            if (group != null) {
                                // TODO 保存本地数据库
                                Intent intent = new Intent(AddGroupChatActivity.this, ChatActivity.class);
                                intent.putExtra(Constants.NAME, groupName);
                                intent.putExtra(Constants.TYPE, ChatActivity.CHATTYPE_GROUP);
                                intent.putExtra(Constants.GROUP_ID, group.getGroupId());
                                startActivity(intent);
                                getLoadingDialog("正在加人...").dismiss();
                                BaseApplication.getBaseApplication().addActivity(AddGroupChatActivity.this);
                                BaseApplication.getBaseApplication().removeActivity();
                            }
                        }

                    });
                } catch (final EaseMobException e) {
                    runOnUiThread(new Runnable() {
                        public void run() {
                            Utils.showLongToast(AddGroupChatActivity.this, "创建失败");
                            getLoadingDialog("正在加人...").dismiss();
                        }
                    });
                }

            }
        }).start();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        CheckBox checkBox = view.findViewById(R.id.checkbox);
        checkBox.toggle();
    }


    class ContactAdapter extends BaseAdapter implements SectionIndexer {
        private Context mContext;
        private boolean[] isCheckedArray;
        private Bitmap[] bitmaps;
        private List<User> list = new ArrayList<User>();

        @SuppressWarnings("unchecked")
        public ContactAdapter(Context mContext, List<User> users) {
            this.mContext = mContext;
            this.list = users;
            bitmaps = new Bitmap[list.size()];
            isCheckedArray = new boolean[list.size()];
            // 排序(实现了中英文混排)
            Collections.sort(list, new PinyinComparator());
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            final User user = list.get(position);
            if (convertView == null) {
                convertView = LayoutInflater.from(mContext).inflate(
                        R.layout.contact_item, null);
            }
            ImageView ivAvatar = ViewHolder.get(convertView,
                    R.id.contactitem_avatar_iv);
            TextView tvCatalog = ViewHolder.get(convertView,
                    R.id.contactitem_catalog);
            TextView tvNick = ViewHolder
                    .get(convertView, R.id.contactitem_nick);
            final CheckBox checkBox = ViewHolder
                    .get(convertView, R.id.checkbox);
            checkBox.setVisibility(View.VISIBLE);
            String catalog = PingYinUtil.converterToFirstSpell(
                    user.getUserName()).substring(0, 1);
            if (position == 0) {
                tvCatalog.setVisibility(View.VISIBLE);
                tvCatalog.setText(catalog);
            } else {
                User Nextuser = list.get(position - 1);
                String lastCatalog = PingYinUtil.converterToFirstSpell(
                        Nextuser.getUserName()).substring(0, 1);
                if (catalog.equals(lastCatalog)) {
                    tvCatalog.setVisibility(View.GONE);
                } else {
                    tvCatalog.setVisibility(View.VISIBLE);
                    tvCatalog.setText(catalog);
                }
            }
            ivAvatar.setImageResource(R.drawable.head);
            tvNick.setText(user.getUserName());
            if (exitingMembers != null
                    && exitingMembers.contains(user.getTelephone())) {
                checkBox.setChecked(true);
            } else {
                checkBox.setChecked(false);
            }
            if (addList != null && addList.contains(user.getTelephone())) {
                checkBox.setChecked(true);
                isCheckedArray[position] = true;
            }
            if (checkBox != null) {
                checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView,
                                                 boolean isChecked) {
                        if (exitingMembers!=null){
                            // 群组中原来的成员一直设为选中状态
                            if (exitingMembers.contains(user.getTelephone())) {
                                isChecked = true;
                                checkBox.setChecked(true);
                            }
                        }
                        isCheckedArray[position] = isChecked;
                        // 如果是单选模式
                        if (isSignleChecked && isChecked) {
                            for (int i = 0; i < isCheckedArray.length; i++) {
                                if (i != position) {
                                    isCheckedArray[i] = false;
                                }
                            }
                            contactAdapter.notifyDataSetChanged();
                        }

                        if (isChecked) {
                            // 选中用户显示在滑动栏显示
                            showCheckImage(list.get(position).getHeadUrl(), list.get(position));
                        } else {
                            // 用户显示在滑动栏删除
                            deleteImage(list.get(position));
                        }
                    }
                });
                // 群组中原来的成员一直设为选中状态
                if (exitingMembers!=null){
                    if (exitingMembers.contains(user.getTelephone())) {
                        checkBox.setChecked(true);
                        isCheckedArray[position] = true;
                    } else {
                        checkBox.setChecked(isCheckedArray[position]);
                    }
                }

            }
            return convertView;
        }

        @Override
        public int getPositionForSection(int section) {
            for (int i = 0; i < list.size(); i++) {
                User user = list.get(i);
                String l = PingYinUtil.converterToFirstSpell(user.getUserName()).substring(0, 1);
                char firstChar = l.toUpperCase().charAt(0);
                if (firstChar == section) {
                    return i;
                }
            }
            return 0;
        }

        @Override
        public int getSectionForPosition(int position) {
            return 0;
        }

        @Override
        public Object[] getSections() {
            return null;
        }
    }

    // 即时显示被选中用户的头像和昵称。
    private void showCheckImage(String url, User glufineid) {
        if (exitingMembers.contains(glufineid.getUserName()) && groupId != null) {
            return;
        }
        if (addList.contains(glufineid.getTelephone())) {
            return;
        }
        total++;

        final ImageView imageView = new ImageView(this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(DensityUtil.dip2px(this, 40), DensityUtil.dip2px(this, 40));
        lp.setMargins(0, 0, DensityUtil.dip2px(this, 5), 0);
        imageView.setLayoutParams(lp);

        // 设置id，方便后面删除
        if (StringUtils.isEmpty(url)) {
            imageView.setImageResource(R.drawable.head);
        } else {
            if (url.contains("http")){
                Glide.with(BaseApplication.getContext()).load(url).into(imageView);
            }else {
                Glide.with(BaseApplication.getContext()).load(Constants.BASE_URL+url).into(imageView);
            }
        }

        menuLinerLayout.addView(imageView);
        btn_sure.setText("确定(" + total + ")");
        if (total > 0) {
            if (iv_search.getVisibility() == View.VISIBLE) {
                iv_search.setVisibility(View.GONE);
            }
        }
        addList.add(glufineid.getTelephone());
    }

    private void deleteImage(User glufineid) {
        View view = menuLinerLayout.findViewWithTag(glufineid);

        menuLinerLayout.removeView(view);
        total--;
        btn_sure.setText("确定(" + total + ")");
        addList.remove(glufineid.getTelephone());
        if (total < 1) {
            if (iv_search.getVisibility() == View.GONE) {
                iv_search.setVisibility(View.VISIBLE);
            }
        }
    }


    /**
     * 创建新群组
     *
     * @param newmembers
     */
    String groupName = "";
    String manber = "";

    private void creatNewGroup(final List<String> members) {
        // TODO 请求服务器创建群组，服务端实现接口
        new Thread(new Runnable() {
            @Override
            public void run() {
                // 调用sdk创建群组方法
                try {
                    final String[] strmembers = new String[members.size()];
                    for (int i = 0; i < members.size(); i++) {
                        RequestBody body=new FormBody.Builder().add("telephone",members.get(i)).build();
                        User user = HttpHelper.ParseJsonUser(HttpHelper.getJson(body,Constants.BASE_URL+Constants.GET_INFO));
                        if (user != null) {
                            if (i < 3) {
                                if (i == 0)
                                    groupName = user.getUserName();
                                else
                                    groupName += "、" + user.getUserName();
                            } else if (i == 4) {
                                groupName += "...";
                            }
                            strmembers[i] = user.getTelephone();
                            if (i == 0) {
                                manber = user.getTelephone();
                            } else {
                                manber += "、" + user.getTelephone();
                            }
                        }
                    }
                    final EMGroup group = EMGroupManager.getInstance().createPublicGroup(groupName, "", strmembers, true);
                    runOnUiThread(new Runnable() {
                        public void run() {
                            if (group != null) {
                                // TODO 保存本地数据库
                                Intent intent = new Intent(AddGroupChatActivity.this, ChatActivity.class);
                                intent.putExtra(Constants.NAME, groupName);
                                intent.putExtra(Constants.TYPE, ChatActivity.CHATTYPE_GROUP);
                                intent.putExtra(Constants.GROUP_ID, group.getGroupId());
                                startActivity(intent);
                                getLoadingDialog("正在创建群聊...").dismiss();
                                finish();
                            }
                        }

                    });
                } catch (final EaseMobException e) {
                    runOnUiThread(new Runnable() {
                        public void run() {
                            Utils.showLongToast(AddGroupChatActivity.this, "创建失败");
                            getLoadingDialog("正在创建群聊...").dismiss();
                        }
                    });
                }

            }
        }).start();
    }
}
