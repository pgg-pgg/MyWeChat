package com.pgg.mywechatem.Uitils;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.pgg.mywechatem.R;


/**
 * Created by PDD on 2017/11/15.
 */

public class PopupWindowFromBottom {


    public static void ShowPopupWindow(final Activity context, PopupWindow popupWindow, View.OnClickListener listener, View parent) {
        View view=Utils.getView(R.layout.window_popup_item);
        TextView tv_find_password=view.findViewById(R.id.tv_find_password);
        TextView tv_safe_center=view.findViewById(R.id.tv_safe_center);
        tv_find_password.setOnClickListener(listener);
        tv_safe_center.setOnClickListener(listener);
        popupWindow = new PopupWindow(view, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT,true);
        popupWindow.setFocusable(true);
        popupWindow.setTouchInterceptor(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
                // 这里如果返回true的话，touch事件将被拦截
                // 拦截后 PopupWindow的onTouchEvent不被调用，这样点击外部区域无法dismiss
            }
        });
        setBackgroundAlpha(context,0.5f);
        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        popupWindow.showAtLocation(parent, Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL,0,0);

        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                // popupWindow隐藏时恢复屏幕正常透明度
                setBackgroundAlpha(context,1.0f);
            }
        });
    }

    /**
     * 设置添加屏幕的背景透明度
     *
     * @param bgAlpha
     *            屏幕透明度0.0-1.0 1表示完全不透明
     */
    private static void setBackgroundAlpha(Activity context,float bgAlpha) {
        WindowManager.LayoutParams lp =context.getWindow()
                .getAttributes();
        lp.alpha = bgAlpha;
        context.getWindow().setAttributes(lp);
    }
}
