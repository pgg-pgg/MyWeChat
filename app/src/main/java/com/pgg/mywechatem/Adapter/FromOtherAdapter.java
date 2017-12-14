package com.pgg.mywechatem.Adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import com.pgg.mywechatem.Domian.WalletBean;
import com.pgg.mywechatem.R;

import java.util.List;

/**
 * Created by PDD on 2017/11/19.
 */

public class FromOtherAdapter extends BaseAdapter{

    private Context context;
    private List<WalletBean.FromOtherService> fromOtherServices;
    public FromOtherAdapter(Context context, List<WalletBean.FromOtherService> fromOtherServices) {
        this.context=context;
        this.fromOtherServices=fromOtherServices;
    }

    @Override
    public int getCount() {
        return fromOtherServices.size();
    }

    @Override
    public WalletBean.FromOtherService getItem(int position) {
        return fromOtherServices.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        FromOtherSerViewHolder viewHolder;
        if (convertView==null){
            convertView=View.inflate(context, R.layout.item_tx_grid_view,null);
            viewHolder=new FromOtherSerViewHolder();
            viewHolder.iv_tx=convertView.findViewById(R.id.iv_tx);
            viewHolder.tv_name=convertView.findViewById(R.id.tv_tx_name);
            convertView.setTag(viewHolder);
        }else {
            viewHolder=(FromOtherSerViewHolder)convertView.getTag();
        }
        viewHolder.iv_tx.setImageResource(fromOtherServices.get(position).getImage());
        viewHolder.tv_name.setText(fromOtherServices.get(position).getName());
        return convertView;
    }
    static class FromOtherSerViewHolder{
        ImageView iv_tx;
        TextView tv_name;
    }

}
