package com.pgg.mywechatem.Adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.easemob.util.DateUtils;
import com.pgg.mywechatem.Domian.Comment;
import com.pgg.mywechatem.R;
import com.pgg.mywechatem.Uitils.Utils;

import java.util.Date;
import java.util.List;

/**
 * Created by PDD on 2017/12/7.
 */

public class CommentAdapter extends BaseAdapter {

    List<Comment> comments;
    public CommentAdapter(List<Comment> comments) {
        this.comments=comments;
    }

    @Override
    public int getCount() {
        return comments.size();
    }

    @Override
    public Comment getItem(int position) {
        return comments.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolderComment holder=null;
        if (convertView==null){
            convertView= Utils.getView(R.layout.list_comment_item);
            holder=new ViewHolderComment();
            holder.tv_comment_id=convertView.findViewById(R.id.tv_comment_id);
            holder.tv_comment_value=convertView.findViewById(R.id.tv_comment_value);
            holder.tv_comment_time=convertView.findViewById(R.id.tv_comment_time);
            convertView.setTag(holder);
        }else {
            holder=(ViewHolderComment)convertView.getTag();
        }
        holder.tv_comment_id.setText(comments.get(position).userName);
        holder.tv_comment_value.setText(comments.get(position).content);
        holder.tv_comment_time.setText(DateUtils.getTimestampString(new Date(comments.get(position).time)));
        return convertView;

    }

    static class ViewHolderComment{
        public TextView tv_comment_id,tv_comment_value,tv_comment_time;
    }
}
