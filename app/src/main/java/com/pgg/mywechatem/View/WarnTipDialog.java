package com.pgg.mywechatem.View;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.pgg.mywechatem.R;


/**
 * Created by PDD on 2017/11/15.
 */

public class WarnTipDialog extends BaseDialog implements View.OnClickListener {

    private TextView btn_ok,btn_cancel;
    private TextView mTitleText,mContentText;
    private String mTText,mCText;
    private static OnClickListener mOnClickListener;
    private static BaseDialog mBaseDialog;// 当前的对话框

    public WarnTipDialog(Context context,String mTText,String mCText,int state) {
        super(context);
        this.mCText=mCText;
        this.mTText=mTText;
        mBaseDialog=new BaseDialog(context);
        init();
        setBtnCancelVisibility(state);
    }
    private void init() {
        setContentView(R.layout.layout_dialog_warntip);
        mTitleText=findViewById(R.id.dialog_generic_htv_title);
        mContentText=findViewById(R.id.dialog_generic_htv_message);
        mTitleText.setText(mTText);
        mContentText.setText(mCText);
        btn_ok = findViewById(R.id.btn_ok);
        btn_cancel=findViewById(R.id.btn_cancel);
        btn_ok.setOnClickListener(this);
        btn_cancel.setOnClickListener(this);
    }
    public void setText(String TText,String CText) {
        if (mTText != null&&CText!=null) {
            mTText=TText;
            mCText=CText;
            mTitleText.setText(mTText);
            mContentText.setText(mCText);
        }
    }

    private void setBtnCancelVisibility(int state){
        btn_cancel.setVisibility(state);
    }


    public void setBtnOkListener(OnClickListener listener) {
        mOnClickListener = listener;
    }

    @Override
    public void dismiss() {
        if (isShowing()) {
            super.dismiss();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_cancel:
                if (btn_cancel.getVisibility()==View.VISIBLE){
                    if (isShowing()) {
                        super.dismiss();
                    }
                }
                break;
            case R.id.btn_ok:
                if (mOnClickListener!=null){
                    mOnClickListener.onClick(mBaseDialog, 1);
                }else {
                    if (isShowing()) {
                        super.dismiss();
                    }
                    break;
                }

        }
    }


}
