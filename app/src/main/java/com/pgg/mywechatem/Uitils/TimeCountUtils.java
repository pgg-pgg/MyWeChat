package com.pgg.mywechatem.Uitils;

import android.os.CountDownTimer;
import android.widget.TextView;

/**
 * Created by PDD on 2017/7/28.
 */
public class TimeCountUtils extends CountDownTimer {

    private TextView button;
    public TimeCountUtils(long millisInFuture, long countDownInterval, TextView button) {
        super(millisInFuture, countDownInterval);
        this.button=button;
    }

    //计时过程显示
    @Override
    public void onTick(long millisUntilFinished) {
        button.setText(millisUntilFinished / 1000 + "秒后重新发送");
        button.setClickable(false);
    }
    //计时完成触发
    @Override
    public void onFinish() {
        button.setText("获取验证码");
        button.setClickable(true);
    }

}
