package com.pgg.mywechatem.Adapter.ChatAdapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;


import com.pgg.mywechatem.R;
import com.pgg.mywechatem.Uitils.Utils;

import java.util.List;

/**
 * Created by PDD on 2017/11/23.
 * viewpager中的Framelayout中的表情布局适配器
 */

public class ExpressionAdapter extends ArrayAdapter<String> {

    Context context;

    public ExpressionAdapter(Context context, int textViewResourceId, List<String> objects) {
        super(context, textViewResourceId, objects);
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = Utils.getView(R.layout.row_expression);
        }
        ImageView imageView = convertView.findViewById(R.id.iv_expression);
        String filename = getItem(position);
        int resId = getContext().getResources().getIdentifier(filename, "drawable", getContext().getPackageName());
        imageView.setImageResource(resId);
        return convertView;
    }
}
