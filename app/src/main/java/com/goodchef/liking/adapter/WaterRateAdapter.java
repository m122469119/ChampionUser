package com.goodchef.liking.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.aaron.android.framework.base.widget.recycleview.BaseRecycleViewAdapter;
import com.aaron.android.framework.base.widget.recycleview.BaseRecycleViewHolder;
import com.goodchef.liking.R;
import com.goodchef.liking.data.remote.retrofit.result.WaterRateResult;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created on 2017/6/27
 * Created by sanfen
 *
 * @version 1.0.0
 */

public class WaterRateAdapter extends BaseRecyclerAdapter<WaterRateResult.DataBean.WaterListBean> {


    public WaterRateAdapter(Context context) {
        super(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreate(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_water_rate, parent, false));
    }

    @Override
    public void onBind(RecyclerView.ViewHolder viewHolder, int RealPosition, WaterRateResult.DataBean.WaterListBean data) {
        ViewHolder holder = (ViewHolder) viewHolder;
        holder.mTime.setText(data.getWater_time() + mContext.getString(R.string.min_cn));
        holder.mPrice.setText(mContext.getString(R.string.rmb) + data.getWater_price());

        if (data.isChecked()) {
            holder.mContains.setBackgroundResource(R.drawable.water_checked_bg);
            holder.mRight.setVisibility(View.VISIBLE);
        } else {
            holder.mContains.setBackgroundResource(R.drawable.water_unchecked_bg);
            holder.mRight.setVisibility(View.GONE);
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.item_water_contains)
        View mContains;

        @BindView(R.id.item_water_time)
        TextView mTime;
        @BindView(R.id.item_water_price)
        TextView mPrice;
        @BindView(R.id.item_water_right)
        ImageView mRight;



        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
