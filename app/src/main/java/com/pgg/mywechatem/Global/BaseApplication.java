package com.pgg.mywechatem.Global;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ImageView;

import com.easemob.chat.EMChat;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMChatOptions;
import com.easemob.chat.EMMessage;
import com.easemob.chat.OnMessageNotifyListener;
import com.easemob.chat.OnNotificationClickListener;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.loader.ImageLoader;
import com.lzy.imagepicker.view.CropImageView;

import com.pgg.mywechatem.Activity.Chat_Activity.ChatActivity;
import com.pgg.mywechatem.Activity.MainActivity;
import com.pgg.mywechatem.R;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;


import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by PDD on 2017/11/13.
 */

public class BaseApplication extends Application {

    private static Context context;
    private static Handler handler;
    private static int threadId;
    @Override
    public void onCreate() {
        super.onCreate();
        context=getApplicationContext();

        handler = new Handler();
        threadId = android.os.Process.myTid();
        ImagePicker imagePicker = ImagePicker.getInstance();
        imagePicker.setImageLoader(new PicassoImageLoader());   //设置图片加载器
        imagePicker.setShowCamera(true);  //显示拍照按钮
        imagePicker.setCrop(true);        //允许裁剪（单选才有效）
        imagePicker.setSaveRectangle(true); //是否按矩形区域保存
        imagePicker.setSelectLimit(9);    //选中数量限制
        imagePicker.setStyle(CropImageView.Style.RECTANGLE);  //裁剪框的形状
        imagePicker.setFocusWidth(800);   //裁剪框的宽度。单位像素（圆形自动取宽高最小值）
        imagePicker.setFocusHeight(800);  //裁剪框的高度。单位像素（圆形自动取宽高最小值）
        imagePicker.setOutPutX(1000);//保存文件的宽度。单位像素
        imagePicker.setOutPutY(1000);//保存文件的高度。单位像素

        initEMChat();
    }



    private void initEMChat() {
        EMChat.getInstance().init(context);

/**
 * debugMode == true 时为打开，SDK会在log里输入调试信息
 * @param debugMode
 * 在做代码混淆的时候需要设置成false
 */
        EMChat.getInstance().setDebugMode(true);//在做打包混淆时，要关闭debug模式，避免消耗不必要的资源
        int pid = android.os.Process.myPid();
        String processAppName = getAppName(pid);
        if (processAppName == null
                || !processAppName.equalsIgnoreCase("com.pgg.mywechatem")) {
            return;
        }
        EMChatOptions options = EMChatManager.getInstance().getChatOptions();
        // 获取到EMChatOptions对象
        // 设置自定义的文字提示
        options.setNotifyText(new OnMessageNotifyListener() {

            @Override
            public String onNewMessageNotify(EMMessage message) {
                return "你的好友发来了一条消息哦";
            }

            @Override
            public String onLatestMessageNotify(EMMessage message,
                                                int fromUsersNum, int messageNum) {
                return fromUsersNum + "个好友，发来了" + messageNum + "条消息";
            }

            @Override
            public String onSetNotificationTitle(EMMessage arg0) {
                return null;
            }

            @Override
            public int onSetSmallIcon(EMMessage arg0) {
                return 0;
            }
        });
        options.setOnNotificationClickListener(new OnNotificationClickListener() {

            @Override
            public Intent onNotificationClick(EMMessage message) {
                Intent intent = new Intent(context, MainActivity.class);
                EMMessage.ChatType chatType = message.getChatType();
                if (chatType == EMMessage.ChatType.Chat) { // 单聊信息
                    intent.putExtra("userId", message.getFrom());
                    intent.putExtra("chatType", ChatActivity.CHATTYPE_SINGLE);
                } else { // 群聊信息
                    // message.getTo()为群聊id
                    intent.putExtra("groupId", message.getTo());
                    intent.putExtra("chatType", ChatActivity.CHATTYPE_GROUP);
                }
                return intent;
            }
        });
        // 默认添加好友时，是不需要验证的true，改成需要验证false
        options.setAcceptInvitationAlways(false);
        // 默认环信是不维护好友关系列表的，如果app依赖环信的好友关系，把这个属性设置为true
        options.setUseRoster(true);

    }

    private String getAppName(int pID) {
        String processName = null;
        ActivityManager am = (ActivityManager) this
                .getSystemService(ACTIVITY_SERVICE);
        List l = am.getRunningAppProcesses();
        Iterator i = l.iterator();
        PackageManager pm = this.getPackageManager();
        while (i.hasNext()) {
            ActivityManager.RunningAppProcessInfo info = (ActivityManager.RunningAppProcessInfo) (i
                    .next());
            try {
                if (info.pid == pID) {
                    CharSequence c = pm.getApplicationLabel(pm
                            .getApplicationInfo(info.processName,
                                    PackageManager.GET_META_DATA));
                    processName = info.processName;
                    return processName;
                }
            } catch (Exception e) {
            }
        }
        return processName;
    }

    public static Handler getHandler() {
        return handler;
    }

    public static int getThreadId() {
        return threadId;
    }


    public static Context getContext(){
        return context;
    }

    private static BaseApplication baseApplication;
    public synchronized static BaseApplication getBaseApplication(){
        if (baseApplication==null){
            baseApplication=new BaseApplication();
        }
        return baseApplication;
    }

    private List<Activity> list=new LinkedList<>();//创建一个存储Activity的list，这样方便关闭活动

    //添加一个活动到list中
    public void addActivity(Activity activity){
        list.add(activity);
    }

    //移除list中的所有活动
    public void removeActivity(){
        try {
            for (Activity activity:list){
                if (activity!=null){
                    activity.finish();
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            System.exit(0);
        }
    }


    public class PicassoImageLoader implements ImageLoader {
        @Override
        public void displayImage(Activity activity, String path, ImageView imageView, int width, int height) {
            Picasso.with(activity)//
                    .load(Uri.fromFile(new File(path)))//
                    .placeholder(R.drawable.ic_menu_camera)//
                    .error(R.drawable.ic_menu_camera)//
                    .resize(width, height)//
                    .centerInside()//
                    .memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE)//
                    .into(imageView);
        }
        @Override
        public void displayImagePreview(Activity activity, String path, ImageView imageView, int width, int height) {

        }

        @Override
        public void clearMemoryCache() {
            //这里是清除缓存的方法,根据需要自己实现
        }
    }
    @Override
    public void onLowMemory() {
        super.onLowMemory();
        try {
            deleteCacheDirFile(getHJYCacheDir(), true);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.gc();
    }
    public static void deleteCacheDirFile(String filePath, boolean deleteThisPath) throws IOException {
        if (!TextUtils.isEmpty(filePath)) {
            File file = new File(filePath);
            if (file.isDirectory()) {// 处理目录
                File files[] = file.listFiles();
                for (int i = 0; i < files.length; i++) {
                    deleteCacheDirFile(files[i].getAbsolutePath(), true);
                }
            }
            if (deleteThisPath) {
                if (!file.isDirectory()) {// 如果是文件，删除
                    file.delete();
                } else {// 目录
                    if (file.listFiles().length == 0) {// 目录下没有文件或者目录，删除
                        file.delete();
                    }
                }
            }
        }
    }

    public static String getHJYCacheDir() {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
            return Environment.getExternalStorageDirectory().toString() + "/Health/Cache";
        else
            return "/System/com.pgg.Walk/Walk/Cache";
    }

    public static String getHJYDownLoadDir() {
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED))
            return Environment.getExternalStorageDirectory().toString()
                    + "/Walk/Download";
        else {
            return "/System/com.pgg.Walk/Walk/Download";
        }
    }
}
