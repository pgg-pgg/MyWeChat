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

public class TXServiceViewAdapter extends BaseAdapter {

    private Context context;
    private List<WalletBean.TXServiceInfoBean> txServiceInfoBeen;
    public TXServiceViewAdapter(Context context, List<WalletBean.TXServiceInfoBean> txServiceInfoBeen) {
        this.context=context;
        this.txServiceInfoBeen=txServiceInfoBeen;
    }

    @Override
    public int getCount() {
        return txServiceInfoBeen.size();
    }

    @Override
    public WalletBean.TXServiceInfoBean getItem(int position) {
        return txServiceInfoBeen.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TXSerViewHolder viewHolder;
        if (convertView==null){
            convertView=View.inflate(context, R.layout.item_tx_grid_view,null);
            viewHolder=new TXSerViewHolder();
            viewHolder.iv_tx=convertView.findViewById(R.id.iv_tx);
            viewHolder.tv_name=convertView.findViewById(R.id.tv_tx_name);
            convertView.setTag(viewHolder);
        }else {
            viewHolder=(TXSerViewHolder)convertView.getTag();
        }
        viewHolder.iv_tx.setImageResource(txServiceInfoBeen.get(position).getImage());
        viewHolder.tv_name.setText(txServiceInfoBeen.get(position).getName());
        return convertView;
    }
    static class TXSerViewHolder{
        ImageView iv_tx;
        TextView tv_name;
    }
}
