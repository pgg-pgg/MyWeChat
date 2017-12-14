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

public class RecommendActAdapter extends BaseAdapter{

    private Context context;
    private List<WalletBean.RecommendAct> recommendActs;
    public RecommendActAdapter(Context context, List<WalletBean.RecommendAct> recommendActs) {
        this.context=context;
        this.recommendActs=recommendActs;
    }

    @Override
    public int getCount() {
        return recommendActs.size();
    }

    @Override
    public WalletBean.RecommendAct getItem(int position) {
        return recommendActs.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        RecommendSerViewHolder viewHolder;
        if (convertView==null){
            convertView=View.inflate(context, R.layout.item_tx_grid_view,null);
            viewHolder=new RecommendSerViewHolder();
            viewHolder.iv_tx=convertView.findViewById(R.id.iv_tx);
            viewHolder.tv_name=convertView.findViewById(R.id.tv_tx_name);
            convertView.setTag(viewHolder);
        }else {
            viewHolder=(RecommendSerViewHolder)convertView.getTag();
        }
        viewHolder.iv_tx.setImageResource(recommendActs.get(position).getImage());
        viewHolder.tv_name.setText(recommendActs.get(position).getName());
        return convertView;
    }
    static class RecommendSerViewHolder{
        ImageView iv_tx;
        TextView tv_name;
    }
}
