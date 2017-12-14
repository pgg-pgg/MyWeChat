package com.pgg.mywechatem.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.pgg.mywechatem.Domian.User;
import com.pgg.mywechatem.Global.BaseApplication;
import com.pgg.mywechatem.Holder.ViewHolder;
import com.pgg.mywechatem.R;
import com.pgg.mywechatem.Uitils.Constants;
import com.pgg.mywechatem.Uitils.PingYinUtil;
import com.pgg.mywechatem.Uitils.PinyinComparator;
import com.pgg.mywechatem.Uitils.Utils;

import java.util.Collections;
import java.util.List;

/**
 * Created by PDD on 2017/11/30.
 */

public class ContactAdapter extends BaseAdapter implements SectionIndexer {

    private List<User> users;

    public ContactAdapter(List<User> users) {
        this.users=users;
        // 排序(实现了中英文混排)
        Collections.sort(users, new PinyinComparator());
    }

    @Override
    public int getCount() {
        return users.size();
    }

    @Override
    public User getItem(int position) {
        return users.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        User user = users.get(position);
        if (convertView == null) {
            convertView = Utils.getView(R.layout.contact_item);
        }
        ImageView ivAvatar = ViewHolder.get(convertView, R.id.contactitem_avatar_iv);
        TextView tvCatalog = ViewHolder.get(convertView, R.id.contactitem_catalog);
        TextView tvNick = ViewHolder.get(convertView, R.id.contactitem_nick);
        String catalog = PingYinUtil.converterToFirstSpell(user.getUserName()).substring(0, 1);
        if (position == 0) {
            tvCatalog.setVisibility(View.VISIBLE);
            tvCatalog.setText(catalog);
        } else {
            User Nextuser = users.get(position - 1);
            String lastCatalog = PingYinUtil.converterToFirstSpell(Nextuser.getUserName()).substring(0, 1);
            if (catalog.equals(lastCatalog)) {
                tvCatalog.setVisibility(View.GONE);
            } else {
                tvCatalog.setVisibility(View.VISIBLE);
                tvCatalog.setText(catalog);
            }
        }
        if (user.getHeadUrl().contains("http")){
            Glide.with(BaseApplication.getContext()).load(user.getHeadUrl()).into(ivAvatar);
        }else {
            Glide.with(BaseApplication.getContext()).load(Constants.BASE_URL+user.getHeadUrl()).into(ivAvatar);
        }
        tvNick.setText(user.getUserName());
        return convertView;
    }


    @Override
    public int getPositionForSection(int section) {
        for (int i = 0; i < users.size(); i++) {
            User user = users.get(i);
            String l = PingYinUtil.converterToFirstSpell(user.getUserName()).substring(0, 1);
            char firstChar = l.toUpperCase().charAt(0);
            if (firstChar == section) {
                return i;
            }
        }
        return 0;
    }

    @Override
    public int getSectionForPosition(int position) {
        return 0;
    }

    @Override
    public Object[] getSections() {
        return null;
    }
}
