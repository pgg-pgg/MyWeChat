package com.pgg.mywechatem.Activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.ui.ImageGridActivity;
import com.lzy.imagepicker.view.CropImageView;
import com.pgg.mywechatem.R;
import com.pgg.mywechatem.Uitils.GlideImageLoader;
import com.pgg.mywechatem.Uitils.Utils;


import java.io.File;
import java.util.ArrayList;

/**
 * Created by PDD on 2017/11/13.
 */

public class RegisterActivity extends BaseActivity implements View.OnClickListener {

    private ImageButton ib_exit_01;
    private View vertical_line;
    private TextView title_tv_left;
    private TextView title_tv_center;
    private EditText edit_nickname_des;
    private RelativeLayout rl_choose_country;
    private ImageView iv_choose_head_sculpture;
    private EditText edit_phone_num;
    private EditText edit_password_num;
    private Button btn_register;
    private View view_of_nickname;
    private View view_of_phone;
    private View view_of_password;

    private ImagePicker imagePicker;
    ArrayList<ImageItem> images = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.activity_register);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void initController() {
        edit_nickname_des =findViewById(R.id.edit_nickname_des);
        rl_choose_country =findViewById(R.id.rl_choose_country);
        iv_choose_head_sculpture =findViewById(R.id.iv_choose_head_sculpture);
        edit_phone_num =findViewById(R.id.edit_phone_num);
        edit_password_num =findViewById(R.id.edit_password_num);
        btn_register =findViewById(R.id.btn_register);
        ib_exit_01 =findViewById(R.id.ib_exit_01);
        vertical_line =findViewById(R.id.vertical_line);
        title_tv_left =findViewById(R.id.title_tv_left);
        title_tv_center =findViewById(R.id.title_tv_center);
        view_of_nickname =findViewById(R.id.view_of_nickname);
        view_of_phone =findViewById(R.id.view_of_phone);
        view_of_password =findViewById(R.id.view_of_password);

    }

    @Override
    public void initView() {
        ib_exit_01.setVisibility(View.VISIBLE);
        vertical_line.setVisibility(View.VISIBLE);
        title_tv_left.setVisibility(View.VISIBLE);
        title_tv_center.setVisibility(View.GONE);
        ib_exit_01.setBackgroundResource(R.drawable.ic_menu_exit_left);
        title_tv_left.setText("填写手机号");
        imagePicker = ImagePicker.getInstance();
        imagePicker.setImageLoader(new GlideImageLoader());
    }

    @Override
    public void initData() {

    }

    @Override
    public void initListener() {
        edit_nickname_des.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus){
                    view_of_nickname.setBackgroundResource(R.color.greeny);
                    view_of_phone.setBackgroundResource(R.color.line_gray);
                    view_of_password.setBackgroundResource(R.color.line_gray);
                }else {
                    view_of_nickname.setBackgroundResource(R.color.line_gray);
                    if (edit_password_num.hasFocus()){
                        view_of_password.setBackgroundResource(R.color.greeny);
                        view_of_phone.setBackgroundResource(R.color.line_gray);
                    }else if (edit_phone_num.hasFocus()){
                        view_of_phone.setBackgroundResource(R.color.greeny);
                        view_of_password.setBackgroundResource(R.color.line_gray);
                    }
                }
            }
        });
        edit_password_num.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus){
                    view_of_password.setBackgroundResource(R.color.greeny);
                    view_of_phone.setBackgroundResource(R.color.line_gray);
                    view_of_nickname.setBackgroundResource(R.color.line_gray);
                }else {
                    view_of_password.setBackgroundResource(R.color.line_gray);
                    if (edit_phone_num.hasFocus()){
                        view_of_phone.setBackgroundResource(R.color.greeny);
                        view_of_nickname.setBackgroundResource(R.color.line_gray);
                    }else if (edit_nickname_des.hasFocus()){
                        view_of_nickname.setBackgroundResource(R.color.greeny);
                        view_of_phone.setBackgroundResource(R.color.line_gray);
                    }
                }
            }
        });
        edit_phone_num.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus){
                    view_of_phone.setBackgroundResource(R.color.greeny);
                    view_of_password.setBackgroundResource(R.color.line_gray);
                    view_of_nickname.setBackgroundResource(R.color.line_gray);
                }else {
                    view_of_phone.setBackgroundResource(R.color.line_gray);
                    if (edit_password_num.hasFocus()){
                        view_of_password.setBackgroundResource(R.color.greeny);
                        view_of_nickname.setBackgroundResource(R.color.line_gray);
                    }else if (edit_nickname_des.hasFocus()){
                        view_of_nickname.setBackgroundResource(R.color.greeny);
                        view_of_password.setBackgroundResource(R.color.line_gray);
                    }
                }
            }
        });

        iv_choose_head_sculpture.setOnClickListener(this);
        btn_register.setOnClickListener(this);
        ib_exit_01.setOnClickListener(this);
        rl_choose_country.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_choose_head_sculpture:
                //点击选择头像
                ChooseHeaderMethod();
                break;
            case R.id.btn_register:
                //点击注册
                break;
            case R.id.ib_exit_01:
                //点击退出页面
                Utils.finish(this);
                break;
            case R.id.rl_choose_country:
                //点击选择国家
                break;
        }
    }

    private void ChooseHeaderMethod() {
        imagePicker.setMultiMode(false);
        imagePicker.setStyle(CropImageView.Style.RECTANGLE);
        Integer width = Integer.valueOf(300);
        Integer height = Integer.valueOf(300);
        width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, width, getResources().getDisplayMetrics());
        height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, height, getResources().getDisplayMetrics());
        imagePicker.setFocusWidth(width);
        imagePicker.setFocusHeight(height);
        imagePicker.setOutPutX(Integer.valueOf(70));
        imagePicker.setOutPutY(Integer.valueOf(70));
        Intent intent = new Intent(this, ImageGridActivity.class);
        intent.putExtra(ImageGridActivity.EXTRAS_IMAGES,images);
        //ImagePicker.getInstance().setSelectedImages(images);
        startActivityForResult(intent, 100);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == ImagePicker.RESULT_CODE_ITEMS) {
            if (data != null && requestCode == 100) {
                images = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
                File file=new File(images.get(0).path);
                if (file.exists()){
                    Bitmap bm=BitmapFactory.decodeFile(images.get(0).path);
                    iv_choose_head_sculpture.setImageBitmap(bm);
                }
            } else {
                Toast.makeText(this, "没有数据", Toast.LENGTH_SHORT).show();
            }
        }
    }


}
