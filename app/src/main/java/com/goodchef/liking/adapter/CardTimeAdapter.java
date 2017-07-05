package com.goodchef.liking.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.aaron.android.framework.base.widget.listview.HBaseAdapter;
import com.aaron.android.framework.base.widget.recycleview.BaseRecycleViewAdapter;
import com.aaron.android.framework.base.widget.recycleview.BaseRecycleViewHolder;
import com.goodchef.liking.R;
import com.goodchef.liking.data.remote.retrofit.result.ConfirmBuyCardResult;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created on 2017/7/5
 * Created by sanfen
 *
 * @version 1.0.0
 */

public class CardTimeAdapter extends BaseRecycleViewAdapter<CardTimeAdapter.ViewHolder, ConfirmBuyCardResult.DataBean.CardsBean.TimeLimitBean> {

    public CardTimeAdapter(Context context) {
        super(context);
    }

    @Override
    protected ViewHolder createViewHolder(ViewGroup parent) {
        View inflate = LayoutInflater.from(getContext()).inflate(R.layout.item_card_time, parent, false);
        return new ViewHolder(inflate);
    }

    public class ViewHolder extends BaseRecycleViewHolder<ConfirmBuyCardResult.DataBean.CardsBean.TimeLimitBean> {

        @BindView(R.id.title)
        TextView mTitleText;

        @BindView(R.id.content)
        TextView mContentText;



        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @Override
        public void bindViews(ConfirmBuyCardResult.DataBean.CardsBean.TimeLimitBean object) {
            mTitleText.setText(object.getTitle());
            mContentText.setText(object.getDesc());
        }
    }
}
