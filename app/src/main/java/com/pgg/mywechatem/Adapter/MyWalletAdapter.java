package com.pgg.mywechatem.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.pgg.mywechatem.Domian.WalletBean;
import com.pgg.mywechatem.Holder.FromOtherViewHolder;
import com.pgg.mywechatem.Holder.RecommendActViewHolder;
import com.pgg.mywechatem.Holder.TXServiceViewHolder;
import com.pgg.mywechatem.R;


/**
 * Created by PDD on 2017/11/19.
 */

public class MyWalletAdapter extends RecyclerView.Adapter {



    /**
     * 腾讯服务
     */
    private static final int TX_SERVICE=0;

    /**
     * 限时推广
     */
    private static final int RECOMMENTACT=1;

    /**
     * 第三方服务
     */
    private static final int FROM_OTHER=2;

    private int currentType=TX_SERVICE;

    private Context context;
    private WalletBean resultBean;
    private LayoutInflater inflater;

    public MyWalletAdapter(Context mContext, WalletBean resultBean) {
        this.context=mContext;
        this.resultBean=resultBean;
        inflater = LayoutInflater.from(context);
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType==TX_SERVICE){
            return new TXServiceViewHolder(context,inflater.inflate(R.layout.txservice_item, null));
        }else if (viewType==RECOMMENTACT){
            return new RecommendActViewHolder(context,inflater.inflate(R.layout.recomment_item,null));
        }else if (viewType==FROM_OTHER){
            return new FromOtherViewHolder(context,inflater.inflate(R.layout.from_other_item,null));
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position)==TX_SERVICE){
            TXServiceViewHolder txServiceViewHolder =(TXServiceViewHolder) holder;
            txServiceViewHolder.setData(resultBean.getTxServiceInfoBeen());
        }else if (getItemViewType(position)==RECOMMENTACT){
            RecommendActViewHolder recommendActViewHolder=(RecommendActViewHolder)holder;
            recommendActViewHolder.setData(resultBean.getRecommendActs());
        }else if (getItemViewType(position)==FROM_OTHER){
            FromOtherViewHolder fromOtherViewHolder=(FromOtherViewHolder)holder;
            fromOtherViewHolder.setData(resultBean.getFromOtherServices());
        }
    }

    @Override
    public int getItemViewType(int position) {
        switch (position){
            case TX_SERVICE:
                currentType=TX_SERVICE;
                break;
            case RECOMMENTACT:
                currentType=RECOMMENTACT;
                break;
            case FROM_OTHER:
                currentType=FROM_OTHER;
                break;
        }
        return currentType;
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
