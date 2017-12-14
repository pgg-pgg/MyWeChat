package com.pgg.mywechatem.Holder;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;


import com.pgg.mywechatem.Activity.WebSafeActivity;
import com.pgg.mywechatem.Adapter.FromOtherAdapter;
import com.pgg.mywechatem.Domian.WalletBean;
import com.pgg.mywechatem.R;
import com.pgg.mywechatem.Uitils.Constants;
import com.pgg.mywechatem.Uitils.Utils;

import org.apache.http.message.BasicNameValuePair;

import java.util.List;

/**
 * Created by PDD on 2017/11/19.
 */

public class FromOtherViewHolder extends RecyclerView.ViewHolder {
    private Context context;
    private GridView gv_recommend;
    private FromOtherAdapter adapter;
    public FromOtherViewHolder(Context context, View inflate) {
        super(inflate);
        this.context=context;
        this.gv_recommend=inflate.findViewById(R.id.gv_recommend);
    }

    public void setData(final List<WalletBean.FromOtherService> fromOtherServices) {
        adapter=new FromOtherAdapter(context,fromOtherServices);
        gv_recommend.setAdapter(adapter);
        gv_recommend.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Utils.start_Activity((Activity)context, WebSafeActivity.class
                        ,new BasicNameValuePair(Constants.Title,fromOtherServices.get(position).getName())
                        ,new BasicNameValuePair(Constants.URL,fromOtherServices.get(position).getUrl()));
            }
        });
    }
}
