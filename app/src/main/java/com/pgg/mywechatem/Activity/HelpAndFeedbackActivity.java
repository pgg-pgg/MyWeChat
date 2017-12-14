package com.pgg.mywechatem.Activity;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;


import com.pgg.mywechatem.R;
import com.pgg.mywechatem.Uitils.Constants;
import com.pgg.mywechatem.Uitils.Utils;

import java.lang.reflect.InvocationTargetException;

/**
 * Created by PDD on 2017/11/17.
 */

public class HelpAndFeedbackActivity extends BaseActivity {
    private ImageButton ib_exit_01;
    private View vertical_line;
    private TextView title_tv_left;
    private TextView title_tv_center;
    private WebView mWebView;
    private ProgressBar progressbar;
    private String strurl;
    private int progress=0;
    private MyTimer mTimer;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.activity_help_and_feedback);
        super.onCreate(savedInstanceState);
    }

    //硬件加速，当activity调用onResume，onDestroy，onPause时，让webView也进行相应方法
    @Override
    protected void onResume() {
        super.onResume();
        try {
            mWebView.getClass().getMethod("onResume").invoke(mWebView,(Object[]) null);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        try {
            mWebView.getClass().getMethod("onPause").invoke(mWebView,(Object[]) null);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            mWebView.getClass().getMethod("onDestroy").invoke(mWebView,(Object[]) null);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initController() {
        ib_exit_01 =findViewById(R.id.ib_exit_01);
        vertical_line =findViewById(R.id.vertical_line);
        title_tv_left =findViewById(R.id.title_tv_left);
        title_tv_center =findViewById(R.id.title_tv_center);
        mWebView =findViewById(R.id.wv_help);
        progressbar=findViewById(R.id.progressbar);
    }



    private class WeiboWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            return super.shouldOverrideUrlLoading(view, url);
        }

        @Override
        public void onReceivedError(WebView view, int errorCode,
                                    String description, String failingUrl) {
            super.onReceivedError(view, errorCode, description, failingUrl);
            mWebView.loadDataWithBaseURL(null, "", "text/html", "utf-8", null);
            // TODO 显示错误404
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            if (mTimer == null) {
                mTimer = new MyTimer(15000, 50);
            }
            mTimer.start();
            progressbar.setVisibility(View.VISIBLE);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            if (!mWebView.getSettings().getLoadsImagesAutomatically()) {
                mWebView.getSettings().setLoadsImagesAutomatically(true);
            }
            mTimer.cancel();
            progress = 0;
            progressbar.setProgress(100);
            progressbar.setVisibility(View.GONE);
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public void initView() {
        ib_exit_01.setVisibility(View.VISIBLE);
        vertical_line.setVisibility(View.VISIBLE);
        title_tv_left.setVisibility(View.VISIBLE);
        title_tv_center.setVisibility(View.GONE);
        ib_exit_01.setBackgroundResource(R.drawable.ic_menu_exit_left);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null && bundle.getString(Constants.URL) != null) {
            strurl = "";
            strurl = bundle.getString(Constants.URL);
        }
        if (bundle != null && bundle.getString(Constants.Title) != null) {
            title_tv_left.setText(bundle.getString(Constants.Title));
        }
        if (!TextUtils.isEmpty(strurl)) {
            mWebView.getSettings().setJavaScriptEnabled(true);
            mWebView.setWebViewClient(new WeiboWebViewClient());
            mWebView.setWebChromeClient(new WebChromeClient());

            if (Build.VERSION.SDK_INT >= 19) {
                mWebView.getSettings().setLoadsImagesAutomatically(true);
            } else {
                mWebView.getSettings().setLoadsImagesAutomatically(false);
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                mWebView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
            }

            mWebView.loadUrl(strurl);
        }
    }

    @Override
    public void initData() {

    }

    @Override
    public void initListener() {
        ib_exit_01.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.finish(HelpAndFeedbackActivity.this);

            }
        });
    }

    /* 定义一个倒计时的内部类 */
    private class MyTimer extends CountDownTimer {
        public MyTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onFinish() {
            progress = 100;
            progressbar.setVisibility(View.GONE);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            if (progress == 100) {
                progressbar.setVisibility(View.GONE);
            } else {
                progressbar.setProgress(progress++);
            }
        }
    }
}
