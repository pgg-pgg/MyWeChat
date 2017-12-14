package com.pgg.mywechatem.Activity.Find_Activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.ui.ImageGridActivity;
import com.lzy.imagepicker.view.CropImageView;
import com.pgg.mywechatem.Activity.BaseActivity;
import com.pgg.mywechatem.Domian.FaceInfo;
import com.pgg.mywechatem.Global.BaseApplication;
import com.pgg.mywechatem.Protocol.HttpHelper;
import com.pgg.mywechatem.Protocol.Net.ThreadHelper;
import com.pgg.mywechatem.Protocol.UpdateImageHelper;
import com.pgg.mywechatem.R;
import com.pgg.mywechatem.Uitils.Constants;
import com.pgg.mywechatem.Uitils.GlideImageLoader;
import com.pgg.mywechatem.Uitils.StringUtils;
import com.pgg.mywechatem.Uitils.Utils;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;


/**
 * Created by PDD on 2017/11/18.
 */

public class ShareMomentsActivity extends BaseActivity implements View.OnClickListener {

    private ImageButton ib_exit_01;
    private View vertical_line;
    private Button btn_sure, btn_add_img;
    private TextView title_tv_left;
    private TextView title_tv_center;
    private EditText et_share_message;
    private HorizontalScrollView hsv_banner;
    private LinearLayout ll_banner;
    private TextView txt_location;
    private RelativeLayout txt_who;
    private TextView txt_tip;
    private ImageView img_qzone;
    private TextView tv_method_of_see;
    private ImagePicker imagePicker;
    ArrayList<ImageItem> images = null;
    private OkHttpClient client;
    private File[] files;
    private FaceInfo faceInfo;
    private ArrayList<String> faceInfo_urls = new ArrayList<>();

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0) {
                getLoadingDialog("正在发送...请稍后").dismiss();
                Utils.finish(ShareMomentsActivity.this);
            } else if (msg.what == 1) {
                getLoadingDialog("正在发送...请稍后").dismiss();
                Utils.showShortToast(BaseApplication.getContext(), "发送失败，请重试");
            } else if (msg.what == 2) {
                getLoadingDialog("正在发送...请稍后").dismiss();
                Utils.showShortToast(BaseApplication.getContext(), "服务器发生错误，发送失败，请重试");
            } else if (msg.what == 3) {
                faceInfo_urls.add(msg.obj + "");
                if (faceInfo_urls.size() == files.length) {
                    System.out.println("图片加载完");
                    new ThreadHelper(new ThreadHelper.ThreadDoSomething() {
                        String userId = Utils.getValue(BaseApplication.getContext(), Constants.LOGIN_TEL);
                        String text_content = et_share_message.getText().toString();
                        String image_content = null;
                        @Override
                        public void doPre() {
                            if (files != null) {
                                if (faceInfo_urls == null) {
                                    image_content = "";
                                } else if (faceInfo_urls.size() < 2) {
                                    image_content = faceInfo_urls.get(0);
                                } else {
                                    image_content = faceInfo_urls.get(0);
                                    for (int i = 1; i < faceInfo_urls.size(); i++) {
                                        image_content += ";" + faceInfo_urls.get(i);
                                    }
                                }
                            } else {
                                image_content = "";
                            }
                        }
                        @Override
                        public void doing() {
                            RequestBody body = new FormBody.Builder().add("userId", userId).add("text_content", text_content).add("image_content", image_content).build();
                            String json = HttpHelper.getJson(body, Constants.BASE_URL + Constants.INSERT_MOMENTS);
                            int code = HttpHelper.ParseJsonCode(json);
                            if (code == 0) {
                                handler.sendEmptyMessage(0);
                            } else {
                                handler.sendEmptyMessage(1);
                            }
                        }

                        @Override
                        public void doEnd() {

                        }
                    }).newThread();
                }
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.activity_share_moments);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void initController() {
        ib_exit_01 = findViewById(R.id.ib_exit_01);
        vertical_line = findViewById(R.id.vertical_line);
        title_tv_left = findViewById(R.id.title_tv_left);
        title_tv_center = findViewById(R.id.title_tv_center);
        btn_sure = findViewById(R.id.btn_sure);
        et_share_message = findViewById(R.id.et_share_message);
        hsv_banner = findViewById(R.id.hsv_banner);
        ll_banner = findViewById(R.id.ll_banner);
        btn_add_img = findViewById(R.id.btn_add_img);
        txt_location = findViewById(R.id.txt_location);
        txt_who = findViewById(R.id.txt_who);
        txt_tip = findViewById(R.id.txt_tip);
        img_qzone = findViewById(R.id.img_qzone);
        tv_method_of_see = findViewById(R.id.tv_method_of_see);
    }

    @Override
    public void initView() {
        ib_exit_01.setVisibility(View.VISIBLE);
        vertical_line.setVisibility(View.VISIBLE);
        title_tv_left.setVisibility(View.GONE);
        title_tv_center.setVisibility(View.GONE);
        btn_sure.setVisibility(View.VISIBLE);
        btn_sure.setText("发送");
        btn_sure.setEnabled(true);
        btn_sure.setBackgroundDrawable(getResources().getDrawable(R.drawable.btn_enable_green));
        btn_sure.setTextColor(0xFFD0EFC6);
        ib_exit_01.setBackgroundResource(R.drawable.ic_menu_exit_left);
        imagePicker = ImagePicker.getInstance();
        imagePicker.setImageLoader(new GlideImageLoader());
    }

    @Override
    public void initData() {

    }

    @Override
    public void initListener() {
        ib_exit_01.setOnClickListener(this);
        btn_add_img.setOnClickListener(this);
        txt_location.setOnClickListener(this);
        txt_who.setOnClickListener(this);
        txt_tip.setOnClickListener(this);
        img_qzone.setOnClickListener(this);
        btn_sure.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ib_exit_01:
                Utils.finish(ShareMomentsActivity.this);
                break;
            case R.id.btn_add_img:
                //添加照片
                ChooseImageMethod();
                break;
            case R.id.txt_location:
                //获取所在位置
                break;
            case R.id.txt_who:
                //设置谁可以看
                break;
            case R.id.txt_tip:
                //提醒谁看
                break;
            case R.id.img_qzone:
                //同步到QQ空间
                break;
            case R.id.btn_sure:
                getLoadingDialog("正在发送...请稍后").show();
                if (StringUtils.isEmpty(et_share_message.getText().toString()) && ll_banner.getChildCount() < 0) {
                    Utils.showShortToast(BaseApplication.getContext(), "还没有内容哦！");
                } else {
                    PostImage(files);
                }
                break;
        }
    }

    private void ChooseImageMethod() {
        imagePicker.setMultiMode(true);
        imagePicker.setShowCamera(false);
        imagePicker.setStyle(CropImageView.Style.RECTANGLE);
        Integer width = Integer.valueOf(200);
        Integer height = Integer.valueOf(200);
        width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, width, getResources().getDisplayMetrics());
        height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, height, getResources().getDisplayMetrics());
        imagePicker.setFocusWidth(width);
        imagePicker.setFocusHeight(height);
        imagePicker.setOutPutX(60);
        imagePicker.setOutPutY(60);
        Intent intent = new Intent(this, ImageGridActivity.class);
        intent.putExtra(ImageGridActivity.EXTRAS_IMAGES, images);
        startActivityForResult(intent, 100);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == ImagePicker.RESULT_CODE_ITEMS) {
            if (data != null && requestCode == 100) {
                images = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
                files = new File[images.size()];
                for (int i = 0; i < images.size(); i++) {
                    File file = new File(images.get(i).path);
                    files[i] = file;
                    Bitmap bm = BitmapFactory.decodeFile(images.get(i).path);
                    ImageView view = new ImageView(BaseApplication.getContext());
                    view.setImageBitmap(bm);
                    ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(60, 60);
                    view.setLayoutParams(params);
                    ll_banner.addView(view);
                    btn_add_img.setVisibility(View.GONE);
                }
            } else {
                Toast.makeText(this, "没有数据", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void PostImage(File[] files) {
        for (int i = 0; i < files.length; i++) {
            Request request = UpdateImageHelper.UpdateImage(files[i]);
            client = new OkHttpClient.Builder().build();
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    handler.sendEmptyMessage(2);
                    System.out.println("error:" + e.getMessage());
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    System.out.println("onResponse:" + response.code() + ",msg:" + response.message());
                    if (response.isSuccessful()) {
                        ResponseBody body = response.body();
                        if (body != null) {
                            faceInfo = UpdateImageHelper.AnalysisJson(body.string());
                            Message message = new Message();
                            message.what = 3;
                            message.obj = faceInfo.t_url;
                            handler.sendMessage(message);
                        }
                    }
                }
            });
        }
    }
}
