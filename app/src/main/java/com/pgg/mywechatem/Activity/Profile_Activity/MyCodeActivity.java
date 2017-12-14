package com.pgg.mywechatem.Activity.Profile_Activity;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.pgg.mywechatem.Activity.BaseActivity;
import com.pgg.mywechatem.Global.BaseApplication;
import com.pgg.mywechatem.R;
import com.pgg.mywechatem.Uitils.Constants;
import com.pgg.mywechatem.Uitils.Utils;


/**
 * Created by PDD on 2017/11/19.
 */

public class MyCodeActivity extends BaseActivity {
    private ImageButton ib_exit_01;
    private View vertical_line;
    private TextView title_tv_left;
    private TextView title_tv_center;
    private TextView txt_title, txt_right, tvname, tv_accout;
    private ImageView head,iv_sex;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.activity_mycode);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void initController() {
        ib_exit_01 =findViewById(R.id.ib_exit_01);
        vertical_line =findViewById(R.id.vertical_line);
        title_tv_left =findViewById(R.id.title_tv_left);
        title_tv_center =findViewById(R.id.title_tv_center);
        tvname = findViewById(R.id.tvname);
        tv_accout = findViewById(R.id.tvmsg);
        head=findViewById(R.id.head);
        iv_sex=findViewById(R.id.iv_sex);
    }

    @Override
    public void initView() {
        ib_exit_01.setVisibility(View.VISIBLE);
        vertical_line.setVisibility(View.VISIBLE);
        title_tv_left.setVisibility(View.VISIBLE);
        title_tv_center.setVisibility(View.GONE);
        title_tv_left.setText("二维码名片");
        ib_exit_01.setBackgroundResource(R.drawable.ic_menu_exit_left);
    }

    @Override
    public void initData() {
        Bitmap qrcode = generateQRCode("http://baidu.com");
        ImageView imageView = findViewById(R.id.img_code);
        imageView.setImageBitmap(qrcode);

        String head_url=Utils.getValue(BaseApplication.getContext(), Constants.LOGIN_HEAD);
        String name=Utils.getValue(BaseApplication.getContext(),Constants.LOGIN_NICK);
        int sex=Utils.getIntValue(BaseApplication.getContext(),Constants.LOGIN_SEX);
        String tel=Utils.getValue(BaseApplication.getContext(),Constants.LOGIN_TEL);
        Glide.with(BaseApplication.getContext()).load(Constants.BASE_URL+head_url).into(head);
        tvname.setText(name);
        iv_sex.setImageResource(sex==1?R.drawable.ic_sex_male:R.drawable.ic_sex_female);
        tv_accout.setText("微信号："+tel);
    }

    @Override
    public void initListener() {
        ib_exit_01.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.finish(MyCodeActivity.this);
            }
        });
    }

    private Bitmap bitMatrix2Bitmap(BitMatrix matrix) {
        int w = matrix.getWidth();
        int h = matrix.getHeight();
        int[] rawData = new int[w * h];
        for (int i = 0; i < w; i++) {
            for (int j = 0; j < h; j++) {
                int color = Color.WHITE;
                if (matrix.get(i, j)) {
                    color = Color.BLACK;
                }
                rawData[i + (j * w)] = color;
            }
        }

        Bitmap bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.RGB_565);
        bitmap.setPixels(rawData, 0, w, 0, 0, w, h);
        return bitmap;
    }

    private Bitmap generateQRCode(String content) {
        try {
            QRCodeWriter writer = new QRCodeWriter();
            // MultiFormatWriter writer = new MultiFormatWriter();
            BitMatrix matrix = writer.encode(content, BarcodeFormat.QR_CODE,
                    500, 500);
            return bitMatrix2Bitmap(matrix);
        } catch (WriterException e) {
            e.printStackTrace();
        }
        return null;
    }
}
