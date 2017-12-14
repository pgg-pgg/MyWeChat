package com.pgg.mywechatem.Domian;

import android.content.Context;
import android.graphics.drawable.Drawable;

/**
 * Created by PDD on 2017/11/16.
 * 弹出框里的item的类
 */

public class ActionItem {

    public Drawable icon;
    public CharSequence title;
    public ActionItem(Drawable icon,CharSequence title) {
        this.icon=icon;
        this.title=title;
    }

    public ActionItem(Context context, int titleId, int drawableId) {
        this.title = context.getResources().getText(titleId);
        this.icon = context.getResources().getDrawable(drawableId);
    }

    public ActionItem(Context context, CharSequence title, int drawableId) {
        this.title = title;
        this.icon = context.getResources().getDrawable(drawableId);
    }

    public ActionItem(Context context, CharSequence title) {
        this.title = title;
        this.icon = null;
    }
}
