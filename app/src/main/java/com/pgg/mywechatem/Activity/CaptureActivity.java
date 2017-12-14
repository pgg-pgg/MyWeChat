package com.pgg.mywechatem.Activity;

import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.zxing.BarcodeFormat;
import com.pgg.mywechatem.R;
import com.pgg.mywechatem.Uitils.Utils;
import com.pgg.mywechatem.zxing.camera.CameraManager;
import com.pgg.mywechatem.zxing.decoding.CaptureActivityHandler;
import com.pgg.mywechatem.zxing.decoding.InactivityTimer;
import com.pgg.mywechatem.zxing.view.ViewfinderView;


import java.io.IOException;
import java.util.Vector;

/**
 * Created by PDD on 2017/11/17.
 */

public class CaptureActivity extends BaseActivity implements SurfaceHolder.Callback {
    private ImageButton ib_exit_01;
    private View vertical_line;
    private TextView title_tv_left;
    private TextView title_tv_center;


    private CaptureActivityHandler handler;
    private ViewfinderView viewfinderView;
    private boolean hasSurface;
    private Vector<BarcodeFormat> decodeFormats;
    private String characterSet;
    private InactivityTimer inactivityTimer;
    private MediaPlayer mediaPlayer;
    private boolean playBeep;
    private static final float BEEP_VOLUME = 0.10f;
    private boolean vibrate;
    private boolean isNoCute = true;
    private static final long VIBRATE_DURATION = 200L;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.activity_capture);
        super.onCreate(savedInstanceState);
        CameraManager.init(getApplication());

    }

    @Override
    public void initController() {
        viewfinderView = findViewById(R.id.viewfinder_view);
        ib_exit_01 =findViewById(R.id.ib_exit_01);
        vertical_line =findViewById(R.id.vertical_line);
        title_tv_left =findViewById(R.id.title_tv_left);
        title_tv_center =findViewById(R.id.title_tv_center);
        title_tv_left.setText("二维码/条码");
        ib_exit_01.setBackgroundResource(R.drawable.ic_menu_exit_left);
        hasSurface = false;
        inactivityTimer = new InactivityTimer(this);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null && bundle.getBoolean("isNoCute")) {
            isNoCute = true;
        } else {
            isNoCute = false;
        }

    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (isNoCute) {
                Utils.finish(CaptureActivity.this);
            } else {
                Utils.finish(CaptureActivity.this);
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onResume() {
        super.onResume();
        SurfaceView surfaceView = findViewById(R.id.preview_view);
        SurfaceHolder surfaceHolder = surfaceView.getHolder();
        if (hasSurface) {
            initCamera(surfaceHolder);
        } else {
            surfaceHolder.addCallback(this);
            surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        }
        decodeFormats = null;
        characterSet = null;

        playBeep = true;
        AudioManager audioService = (AudioManager) getSystemService(AUDIO_SERVICE);
        if (audioService.getRingerMode() != AudioManager.RINGER_MODE_NORMAL) {
            playBeep = false;
        }
        initBeepSound();
        vibrate = true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (handler != null) {
            handler.quitSynchronously();
            handler = null;
        }
        CameraManager.get().closeDriver();
    }

    @Override
    public void onDestroy() {
        if (inactivityTimer != null)
            inactivityTimer.shutdown();
        super.onDestroy();
    }

    private void initCamera(SurfaceHolder surfaceHolder) {
        try {
            CameraManager.get().openDriver(surfaceHolder);
        } catch (IOException ioe) {
            return;
        } catch (RuntimeException e) {
            return;
        }
        if (handler == null) {
            handler = new CaptureActivityHandler(this, decodeFormats, characterSet);
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width,
                               int height) {

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (!hasSurface) {
            hasSurface = true;
            initCamera(holder);
        }

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        hasSurface = false;
    }


    public ViewfinderView getViewfinderView() {
        return viewfinderView;
    }

    public Handler getHandler() {
        return handler;
    }
    @Override
    public void initView() {
        ib_exit_01.setVisibility(View.VISIBLE);
        vertical_line.setVisibility(View.VISIBLE);
        title_tv_left.setVisibility(View.VISIBLE);
        title_tv_center.setVisibility(View.GONE);
    }

    @Override
    public void initData() {

    }

    @Override
    public void initListener() {
        ib_exit_01.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.finish(CaptureActivity.this);
            }
        });
    }

    public void drawViewfinder() {
        viewfinderView.drawViewfinder();
    }

    /**
     * 扫描正确后的震动声音,如果感觉apk大了,可以删除
     */
    private void initBeepSound() {
        if (playBeep && mediaPlayer == null) {
            // The volume on STREAM_SYSTEM is not adjustable, and users found it
            // too loud,
            // so we now play on the music stream.
            setVolumeControlStream(AudioManager.STREAM_MUSIC);
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setOnCompletionListener(beepListener);

            AssetFileDescriptor file = getResources().openRawResourceFd(
                    R.raw.beep);
            try {
                mediaPlayer.setDataSource(file.getFileDescriptor(),
                        file.getStartOffset(), file.getLength());
                file.close();
                mediaPlayer.setVolume(BEEP_VOLUME, BEEP_VOLUME);
                mediaPlayer.prepare();
            } catch (IOException e) {
                mediaPlayer = null;
            }
        }
    }
    private void playBeepSoundAndVibrate() {
        if (playBeep && mediaPlayer != null) {
            mediaPlayer.start();
        }
        if (vibrate) {
            Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
            vibrator.vibrate(VIBRATE_DURATION);
        }
    }

    /**
     * When the beep has finished playing, rewind to queue up another one.
     */
    private final MediaPlayer.OnCompletionListener beepListener = new MediaPlayer.OnCompletionListener() {
        public void onCompletion(MediaPlayer mediaPlayer) {
            mediaPlayer.seekTo(0);
        }
    };

    public void handleDecode(com.google.zxing.Result result, Bitmap barcode) {
        inactivityTimer.onActivity();
        playBeepSoundAndVibrate();

        final String resultString = result.getText();
        Intent resultIntent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putString("result", resultString);
        resultIntent.putExtras(bundle);
        this.setResult(RESULT_OK, resultIntent);

        if (!isNoCute) {
            if (TextUtils.isEmpty(resultString)) {
                Utils.showLongToast(CaptureActivity.this, "二维码信息错误！");
                return;
            } else {
                if (resultString.startsWith("JUNS_WeChat@User")) {
//                    String[] name = resultString.split(":");
//                    Log.e("", "扫描到的好友为：" + name[1]);
//                    Utils.start_Activity(CaptureActivity.this,
//                            FriendMsgActivity.class, new BasicNameValuePair(
//                                    Constants.User_ID, name[1]),
//                            new BasicNameValuePair(Constants.NAME, name[1]));
                } else if (resultString.startsWith("JUNS_WeChat@getMoney")) {
//                    String[] msg = resultString.split(":");
//                    String[] money_msg = msg[1].split(",");
//                    Log.e("", "扫描到的好友ID为：" + money_msg[1]);
//                    Utils.start_Activity(
//                            CaptureActivity.this,
//                            SetMoneyActivity.class,
//                            new BasicNameValuePair(Constants.User_ID,
//                                    money_msg[0]),
//                            new BasicNameValuePair(Constants.NAME, money_msg[1]));
                } else if (resultString.startsWith("http://")
                        || resultString.startsWith("https://")) {
                    Uri uri = Uri.parse(resultString);
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent);
                } else {
                    Utils.showLongToast(this, "扫描结果为：" + result);
                }
            }
        }
        finish();
    }
}
