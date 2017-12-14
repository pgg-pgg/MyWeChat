package com.pgg.mywechatem.Activity.Find_Activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.ui.ImageGridActivity;
import com.lzy.imagepicker.view.CropImageView;
import com.pgg.mywechatem.Activity.BaseActivity;
import com.pgg.mywechatem.Activity.Profile_Activity.MyInfoActivity;
import com.pgg.mywechatem.Domian.FaceInfo;
import com.pgg.mywechatem.Global.BaseApplication;
import com.pgg.mywechatem.Protocol.HttpHelper;
import com.pgg.mywechatem.Protocol.UpdateImageHelper;
import com.pgg.mywechatem.R;
import com.pgg.mywechatem.Uitils.Constants;
import com.pgg.mywechatem.Uitils.GlideImageLoader;
import com.pgg.mywechatem.Uitils.Utils;

import java.io.File;
import java.io.FileOutputStream;
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

public class ChangeBackgroundActivity extends BaseActivity implements View.OnClickListener {
    private ImageButton ib_exit_01;
    private View vertical_line;
    private TextView title_tv_left;
    private TextView title_tv_center;
    private TextView tv_from_phone;
    private TextView tv_play_photo;
    private TextView tv_from_net;
    private ImagePicker imagePicker;
    ArrayList<ImageItem> images = null;
    private OkHttpClient client;


    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0) {
                getLoadingDialog("正在设置...请稍后").show();
            } else if (msg.what == 1) {
                getLoadingDialog("正在设置...请稍后").dismiss();
                Utils.showShortToast(ChangeBackgroundActivity.this, "更新成功！！！");
            } else if (msg.what == 2) {
                getLoadingDialog("正在设置...请稍后").dismiss();
                Utils.showShortToast(ChangeBackgroundActivity.this, "更新失败，请检查网络设置");
            }
        }
    };
    private FaceInfo faceInfo;
    private String tel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.activity_change_background);
        super.onCreate(savedInstanceState);
    }


    @Override
    public void initController() {
        ib_exit_01 = findViewById(R.id.ib_exit_01);
        vertical_line = findViewById(R.id.vertical_line);
        title_tv_left = findViewById(R.id.title_tv_left);
        title_tv_center = findViewById(R.id.title_tv_center);
        tv_from_net = findViewById(R.id.tv_from_net);
        tv_from_phone = findViewById(R.id.tv_from_phone);
        tv_play_photo = findViewById(R.id.tv_play_photo);
    }

    @Override
    public void initView() {
        ib_exit_01.setVisibility(View.VISIBLE);
        vertical_line.setVisibility(View.VISIBLE);
        title_tv_left.setVisibility(View.VISIBLE);
        title_tv_center.setVisibility(View.GONE);
        title_tv_left.setText("更换相册封面");
        ib_exit_01.setBackgroundResource(R.drawable.ic_menu_exit_left);
        imagePicker = ImagePicker.getInstance();
        imagePicker.setImageLoader(new GlideImageLoader());
    }

    @Override
    public void initData() {
        tel = Utils.getValue(BaseApplication.getContext(), Constants.LOGIN_TEL);
    }

    @Override
    public void initListener() {
        ib_exit_01.setOnClickListener(this);
        tv_from_phone.setOnClickListener(this);
        tv_from_net.setOnClickListener(this);
        tv_play_photo.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ib_exit_01:
                Utils.finish(ChangeBackgroundActivity.this);
                break;
            case R.id.tv_from_phone:
                //从手机选一张
                ChooseHeaderMethod();
                break;
            case R.id.tv_from_net:
                //摄影师作品

                break;
            case R.id.tv_play_photo:
                //拍一张
                ChooseCameraMethod();
                break;
        }
    }

    private void ChooseCameraMethod() {
        //打开相机
        String state = Environment.getExternalStorageState();// 获取内存卡可用状态
        if (state.equals(Environment.MEDIA_MOUNTED)) {
            // 内存卡状态可用
            boolean isAllGranted = checkPermissionAllGranted(new String[]{Manifest.permission.CAMERA});
            // 如果权限拥有
            if (isAllGranted) {
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, 1);
                return;
            }
            /**
             * 第 2 步: 请求权限
             */
            // 一次请求多个权限, 如果其他有权限是已经授予的将会自动忽略掉
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.CAMERA
            }, 100);
        } else {
            // 不可用
            Toast.makeText(ChangeBackgroundActivity.this, "内存不可用", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * 第 3 步: 申请权限结果返回处理
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            boolean isAllGranted = true;
            // 判断是否所有的权限都已经授予了
            for (int grant : grantResults) {
                if (grant != PackageManager.PERMISSION_GRANTED) {
                    isAllGranted = false;
                    break;
                }
            }
            if (isAllGranted) {
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, 1);
            } else {
                Utils.showShortToast(BaseApplication.getContext(),"请手动设置允许应用调用相机权限");
            }
        }
    }
    /**
     * 检查是否拥有指定的所有权限
     */
    private boolean checkPermissionAllGranted(String[] permissions) {
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                // 只要有一个权限没有被授予, 则直接返回 false
                return false;
            }
        }
        return true;
    }

    private void ChooseHeaderMethod() {
        imagePicker.setMultiMode(false);
        imagePicker.setShowCamera(false);
        imagePicker.setStyle(CropImageView.Style.RECTANGLE);
        Integer width = Integer.valueOf(400);
        Integer height = Integer.valueOf(250);
        width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, width, getResources().getDisplayMetrics());
        height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, height, getResources().getDisplayMetrics());
        imagePicker.setFocusWidth(width);
        imagePicker.setFocusHeight(height);
        imagePicker.setOutPutX(Integer.valueOf(Utils.getWMWidth()));
        imagePicker.setOutPutY(Integer.valueOf(240));
        Intent intent = new Intent(this, ImageGridActivity.class);
        intent.putExtra(ImageGridActivity.EXTRAS_IMAGES, images);
        startActivityForResult(intent, 100);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == ImagePicker.RESULT_CODE_ITEMS) {
            if (data != null && requestCode == 100) {
                handler.sendEmptyMessage(0);
                images = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
                File file = new File(images.get(0).path);
                if (file.exists()) {
                    PostImage(file);
                }
            } else {
                Toast.makeText(this, "没有数据", Toast.LENGTH_SHORT).show();
            }
        } else if (data != null && requestCode == 1) {
            if (data.getData() != null || data.getExtras() != null) { // 防止没有返回结果
                Uri uri = data.getData();
                if (uri != null) {
                    Bundle bundle = data.getExtras();
                    if (bundle != null) {
                        try {
                            String saveDir = Environment.getExternalStorageDirectory() + "/dhj_Photos";// 新建目录
                            File dir = new File(saveDir);
                            if (!dir.exists())
                                dir.mkdir();// 生成文件名
                            SimpleDateFormat t = new SimpleDateFormat("yyyyMMddssSSS");
                            final String filename = "MT" + (t.format(new Date())) + ".jpg";// 新建文件
                            final File file = new File(saveDir, filename);// 打开文件输出流
                            PostImage(file);
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }

    private void PostImage(File file) {
        Request request = UpdateImageHelper.UpdateImage(file);
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
                        RequestBody body1 = new FormBody.Builder().add("telephone", tel).add("backgroundUrl", faceInfo.t_url).build();
                        String json = HttpHelper.getJson(body1, Constants.BASE_URL + Constants.UPDATE_BACKGROUND);
                        int code = HttpHelper.ParseJsonCode(json);
                        if (code == 0) {
                            Utils.putValue(BaseApplication.getContext(), Constants.LOGIN_BACKGROUND, faceInfo.t_url);
                            handler.sendEmptyMessage(1);
                        } else {
                            handler.sendEmptyMessage(2);
                        }
                    } else {
                        handler.sendEmptyMessage(2);
                    }
                }
            }
        });
    }
}
