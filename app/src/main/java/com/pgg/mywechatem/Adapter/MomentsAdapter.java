package com.pgg.mywechatem.Adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;


import com.bumptech.glide.Glide;
import com.easemob.util.DateUtils;
import com.pgg.mywechatem.Domian.Comment;
import com.pgg.mywechatem.Domian.Moments;
import com.pgg.mywechatem.Global.BaseApplication;
import com.pgg.mywechatem.R;
import com.pgg.mywechatem.Uitils.Constants;
import com.pgg.mywechatem.Uitils.StringUtils;
import com.pgg.mywechatem.Uitils.Utils;
import com.pgg.mywechatem.View.ExpandListView;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by PDD on 2017/11/18.
 */

public class MomentsAdapter extends BaseAdapter {

    private ArrayList<Moments> momentses;
    private List<Comment> comments;
    HashMap<String, List<Comment>> map = new HashMap<>();

    public MomentsAdapter(ArrayList<Moments> momentses, List<Comment> comments) {
        this.momentses = momentses;
        this.comments = comments;
        for (int i = 0; i < momentses.size(); i++) {
            List<Comment> comments1 = new ArrayList<>();
            for (int j = 0; j < comments.size(); j++) {
                if (momentses.get(i).id.equals(comments.get(j).moments_id)) {
                    comments1.add(comments.get(j));
                } else {
                    continue;
                }
                map.put(momentses.get(i).id, comments1);
            }
        }
    }

    @Override
    public int getCount() {
        return momentses.size();
    }

    @Override
    public Moments getItem(int position) {
        return momentses.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        MomentsViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = Utils.getView(R.layout.list_moments_item);
            viewHolder = new MomentsViewHolder();
            viewHolder.txt_name = convertView.findViewById(R.id.txt_content);
            viewHolder.txt_message = convertView.findViewById(R.id.txt_msg);
            viewHolder.time = convertView.findViewById(R.id.txt_time);
            viewHolder.img_head = convertView.findViewById(R.id.img_head);
            viewHolder.img_photo1 = convertView.findViewById(R.id.img_photo1);
            viewHolder.img_photo2 = convertView.findViewById(R.id.img_photo2);
            viewHolder.img_photo3 = convertView.findViewById(R.id.img_photo3);
            viewHolder.img_zan = convertView.findViewById(R.id.img_zan);
            viewHolder.lv_comment = convertView.findViewById(R.id.lv_comment);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (MomentsViewHolder) convertView.getTag();
        }
        viewHolder.txt_name.setText(momentses.get(position).userName);
        viewHolder.txt_message.setText(momentses.get(position).text_content);
        viewHolder.time.setText(DateUtils.getTimestampString(new Date(momentses.get(position).time)));
//        if (changeLinerLayout!=null){
//            viewHolder.ll_comment_detail.setVisibility(changeLinerLayout.changeState()?View.VISIBLE:View.GONE);
//            LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,20);
//            viewHolder.ll_comment_detail.addView(changeLinerLayout.addView(),params);
//        }
        if (map.get(momentses.get(position).id) == null || map.get(momentses.get(position).id).size() == 0) {
            viewHolder.lv_comment.setVisibility(View.GONE);
        } else {
            viewHolder.lv_comment.setVisibility(View.VISIBLE);
            CommentAdapter adapter = new CommentAdapter(map.get(momentses.get(position).id));
            viewHolder.lv_comment.setAdapter(adapter);
        }
        if (momentses.get(position).userHeadUrl.contains("http")) {
            Glide.with(BaseApplication.getContext()).load(momentses.get(position).userHeadUrl).into(viewHolder.img_head);
        } else {
            Glide.with(BaseApplication.getContext()).load(Constants.BASE_URL + momentses.get(position).userHeadUrl).into(viewHolder.img_head);
        }
        if (!StringUtils.isEmpty(momentses.get(position).image_content)) {
            String[] split = momentses.get(position).image_content.split(";");
            if (split.length == 0) {
                viewHolder.img_photo1.setVisibility(View.GONE);
                viewHolder.img_photo2.setVisibility(View.GONE);
                viewHolder.img_photo3.setVisibility(View.GONE);
            } else if (split.length == 1) {
                Glide.with(BaseApplication.getContext()).load((split[0].contains("http")) ? (split[0]) : (Constants.BASE_URL + split[0])).into(viewHolder.img_photo1);
            } else if (split.length == 2) {
                Glide.with(BaseApplication.getContext()).load((split[0].contains("http")) ? (split[0]) : (Constants.BASE_URL + split[0])).into(viewHolder.img_photo1);
                Glide.with(BaseApplication.getContext()).load((split[1].contains("http")) ? (split[1]) : (Constants.BASE_URL + split[1])).into(viewHolder.img_photo2);
            } else if (split.length == 3) {
                Glide.with(BaseApplication.getContext()).load((split[0].contains("http")) ? (split[0]) : (Constants.BASE_URL + split[0])).into(viewHolder.img_photo1);
                Glide.with(BaseApplication.getContext()).load((split[1].contains("http")) ? (split[1]) : (Constants.BASE_URL + split[1])).into(viewHolder.img_photo2);
                Glide.with(BaseApplication.getContext()).load((split[2].contains("http")) ? (split[2]) : (Constants.BASE_URL + split[2])).into(viewHolder.img_photo3);
            }
        } else {
            viewHolder.img_photo1.setVisibility(View.GONE);
            viewHolder.img_photo2.setVisibility(View.GONE);
            viewHolder.img_photo3.setVisibility(View.GONE);
        }
        viewHolder.img_zan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onZanClick(position);
                }
            }
        });
        return convertView;
    }

    class MomentsViewHolder {
        public TextView txt_name;
        public TextView txt_message;
        public TextView time;
        public ImageView img_head;
        public ImageView img_photo1, img_photo2, img_photo3;
        public ImageView img_zan;
        public ExpandListView lv_comment;
    }

    public interface OnZanClickListener {
        void onZanClick(int position);
    }

    private OnZanClickListener listener;

    public void setOnZanClickListener(OnZanClickListener listener) {
        this.listener = listener;
    }

    private OnChangeLinerLayout changeLinerLayout;

    public void setChangeLinerLayout(OnChangeLinerLayout changeLinerLayout) {
        this.changeLinerLayout = changeLinerLayout;
    }

    public interface OnChangeLinerLayout {
        boolean changeState();

        View addView();
    }

}
