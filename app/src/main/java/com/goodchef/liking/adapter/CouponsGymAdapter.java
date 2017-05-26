package com.goodchef.liking.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.aaron.android.framework.base.widget.recycleview.BaseRecycleViewAdapter;
import com.aaron.android.framework.base.widget.recycleview.BaseRecycleViewHolder;
import com.goodchef.liking.R;
import com.goodchef.liking.data.remote.retrofit.result.CouponsCities;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created on 2017/3/15
 * Created by sanfen
 *
 * @version 1.0.0
 */

public class CouponsGymAdapter extends BaseRecycleViewAdapter<CouponsGymAdapter.ViewHolder, CouponsCities.DataBean.GymListBean> {

    public Context mContext;


    public CouponsGymAdapter(Context context) {
        super(context);
        this.mContext = context;
    }

    @Override
    protected ViewHolder createViewHolder(ViewGroup parent) {
        View inflate = LayoutInflater.from(mContext).inflate(R.layout.item_coupons_gym, null);
        return new ViewHolder(inflate);
    }

    class ViewHolder extends BaseRecycleViewHolder<CouponsCities.DataBean.GymListBean> {
        @BindView(R.id.txt_item_coupons_gym_name)
        TextView mName;
        @BindView(R.id.txt_item_coupons_gym_address)
        TextView mAddress;
        @BindView(R.id.txt_item_coupons_gym_distance)
        TextView mDistance;

        /**
         * Constructor
         *
         * @param itemView Item Root View
         */
        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @Override
        public void bindViews(CouponsCities.DataBean.GymListBean object) {
            mName.setText(object.getGymName());
            mDistance.setText(object.getDistance());
            mAddress.setText(object.getGymAddress());

        }
    }
}
