package com.pgg.mywechatem.Adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import com.pgg.mywechatem.Domian.MyMessageBean;
import com.pgg.mywechatem.R;
import com.pgg.mywechatem.Uitils.Utils;

import java.util.ArrayList;

/**
 * Created by PDD on 2017/11/19.
 */

public class MyAlbumAdapter extends BaseAdapter {

    private ArrayList<MyMessageBean> messageBeen;
    public MyAlbumAdapter(ArrayList<MyMessageBean> messageBeen) {
        this.messageBeen=messageBeen;
    }

    @Override
    public int getCount() {
        return messageBeen.size();
    }

    @Override
    public MyMessageBean getItem(int position) {
        return messageBeen.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MyAlbumViewHolder viewHolder=null;
        if (convertView==null){
            convertView= Utils.getView(R.layout.list_album_item);
            viewHolder=new MyAlbumViewHolder();
            viewHolder.tv_day=convertView.findViewById(R.id.tv_day);
            viewHolder.tv_month=convertView.findViewById(R.id.tv_month);
            viewHolder.tv_message=convertView.findViewById(R.id.tv_message);
            viewHolder.iv_message=convertView.findViewById(R.id.iv_message);
            convertView.setTag(viewHolder);
        }else {
            viewHolder=(MyAlbumViewHolder)convertView.getTag();
        }
        viewHolder.tv_day.setText(messageBeen.get(position).getDay());
        viewHolder.tv_month.setText(messageBeen.get(position).getMonth());
        viewHolder.tv_message.setText(messageBeen.get(position).getMessage());
        viewHolder.iv_message.setImageResource(messageBeen.get(position).getImg());
        return convertView;
    }
    static class MyAlbumViewHolder{
        TextView tv_day,tv_month,tv_message;
        ImageView iv_message;
    }
}
