package com.pgg.mywechatem.Activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;


import com.pgg.mywechatem.Global.BaseApplication;
import com.pgg.mywechatem.Uitils.MPermissionUtils;
import com.pgg.mywechatem.Uitils.Utils;
import com.pgg.mywechatem.View.FlippingLoadingDialog;

import org.apache.http.message.BasicNameValuePair;

/**
 * Created by PDD on 2017/11/13.
 */

public abstract class BaseActivity extends Activity {

    protected Activity context;
    protected FlippingLoadingDialog mLoadingDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context=this;
        BaseApplication.getBaseApplication().addActivity(context);
        initController();
        initView();
        initData();
        initListener();
    }


    public void start_activity(Activity activity, Class<?> cls, BasicNameValuePair... name){
        Utils.start_Activity(activity,cls,name);
    }

    public void finish_activity(Activity activity){
        Utils.finish(activity);
    }

    /**
     * 绑定控件id
     */
    public abstract void initController();

    /**
     * 初始化控件
     */
    public abstract void initView();

    /**
     * 初始化数据
     */
    public abstract void initData();

    /**
     * 初始化监听器
     */
    public abstract void initListener();


    public FlippingLoadingDialog getLoadingDialog(String msg) {
        if (mLoadingDialog == null)
            mLoadingDialog = new FlippingLoadingDialog(this, msg);
        return mLoadingDialog;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        MPermissionUtils.onRequestPermissionsResult(requestCode, permissions, grantResults);
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }



}
