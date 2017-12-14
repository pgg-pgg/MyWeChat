package com.pgg.mywechatem.Activity.Profile_Activity;

import android.app.AlertDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.pgg.mywechatem.Activity.BaseActivity;
import com.pgg.mywechatem.Activity.Find_Activity.ChangeBackgroundActivity;
import com.pgg.mywechatem.Adapter.MyAlbumAdapter;
import com.pgg.mywechatem.Domian.MyMessageBean;
import com.pgg.mywechatem.R;
import com.pgg.mywechatem.Uitils.CreateDataUtils;
import com.pgg.mywechatem.Uitils.Utils;
import com.pgg.mywechatem.View.MyDialogView;

import java.util.ArrayList;


/**
 * Created by PDD on 2017/11/18.
 */

public class AlbumActivity extends BaseActivity implements View.OnClickListener {
    private ImageButton ib_exit_01;
    private View vertical_line;
    private TextView title_tv_left;
    private TextView title_tv_center,txt_name;
    private ImageButton ib_right;
    private View layout_head;
    private RelativeLayout rl_today;
    private FrameLayout fl_camera;
    private ListView list_album;
    private ImageView head;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.activity_album);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void initController() {
        ib_exit_01 =findViewById(R.id.ib_exit_01);
        vertical_line =findViewById(R.id.vertical_line);
        title_tv_left =findViewById(R.id.title_tv_left);
        title_tv_center =findViewById(R.id.title_tv_center);
        ib_right=findViewById(R.id.ib_right);
        layout_head= Utils.getView(R.layout.list_moments_head);
        rl_today=layout_head.findViewById(R.id.rl_today);
        rl_today.setVisibility(View.VISIBLE);
        fl_camera=layout_head.findViewById(R.id.fl_camera);
        list_album=findViewById(R.id.list_album);
        txt_name=layout_head.findViewById(R.id.txt_name);
        head=layout_head.findViewById(R.id.head);
    }

    @Override
    public void initView() {
        ib_exit_01.setVisibility(View.VISIBLE);
        vertical_line.setVisibility(View.VISIBLE);
        title_tv_left.setVisibility(View.VISIBLE);
        title_tv_center.setVisibility(View.GONE);
        title_tv_left.setText("我的相册");
        ib_exit_01.setBackgroundResource(R.drawable.ic_menu_exit_left);
        ib_right.setBackgroundResource(R.drawable.menu_pinglun);
        ib_right.setVisibility(View.VISIBLE);
        list_album.addHeaderView(layout_head);
    }

    @Override
    public void initData() {
        ArrayList<MyMessageBean> messageBeen= CreateDataUtils.createMyMessage();
        list_album.setAdapter(new MyAlbumAdapter(messageBeen));
    }

    @Override
    public void initListener() {
        ib_exit_01.setOnClickListener(this);
        ib_right.setOnClickListener(this);
        fl_camera.setOnClickListener(this);
        txt_name.setOnClickListener(this);
        head.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ib_exit_01:
                Utils.finish(AlbumActivity.this);
                break;
            case R.id.ib_right:
                Utils.start_Activity(AlbumActivity.this,MyMessageActivity.class);
                break;
            case R.id.fl_camera:
                showCameraDialog();
                break;
            case R.id.txt_name:
                //修改背景图片
                ChangeBackgroundDialog();
                break;
            case R.id.head:
                //跳转到自己的详细资料
                Utils.start_Activity(AlbumActivity.this,MyDetailInfoActivity.class);
                break;
        }
    }
    private void ChangeBackgroundDialog() {
        final MyDialogView dialog = new MyDialogView(AlbumActivity.this);
        View view1= Utils.getView(R.layout.activity_setting_pic);
        view1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dialog != null) {
                    dialog.cancel();
                }
            }
        });
        Button btn_change_back = view1.findViewById(R.id.btn_change_back);
        btn_change_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                if (dialog != null) {
                    dialog.cancel();
                    Utils.start_Activity(AlbumActivity.this,ChangeBackgroundActivity.class);
                }
            }
        });
        dialog.show();
        dialog.setCancelable(false);
        dialog.setContentView(view1);// show方法要在前面
    }

    private void showCameraDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(AlbumActivity.this);
        final AlertDialog alertDialog = builder.create();
        alertDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        View view = Utils.getView(R.layout.dialog_show_face);
        RelativeLayout tv_open_camera = view.findViewById(R.id.tv_open_camera);
        TextView tv_open_photo =view.findViewById(R.id.tv_open_photo);
        alertDialog.setView(view,0,0,0,0);
        alertDialog.show();
        tv_open_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

        tv_open_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
    }
}
