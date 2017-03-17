package com.goodchef.liking.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.aaron.android.framework.base.widget.recycleview.BaseRecycleViewAdapter;
import com.aaron.android.framework.base.widget.recycleview.BaseRecycleViewHolder;
import com.goodchef.liking.http.result.CouponsCities;

/**
 * Created on 2017/3/15
 * Created by sanfen
 *
 * @version 1.0.0
 */

public class CouponsGymAdapter extends BaseRecycleViewAdapter<CouponsGymAdapter.ViewHolder, CouponsCities> {


    public CouponsGymAdapter(Context context) {
        super(context);
    }

    @Override
    protected ViewHolder createViewHolder(ViewGroup parent) {
        return null;
    }

    class ViewHolder extends BaseRecycleViewHolder<CouponsCities> {
        /**
         * Constructor
         *
         * @param itemView Item Root View
         */
        public ViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        public void bindViews(CouponsCities object) {

        }
    }
}
