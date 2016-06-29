package com.goodchef.liking.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.aaron.android.framework.base.widget.recycleview.BaseRecycleViewAdapter;
import com.aaron.android.framework.base.widget.recycleview.BaseRecycleViewHolder;
import com.goodchef.liking.R;
import com.goodchef.liking.http.result.data.TimeLimitData;

/**
 * 说明:
 * Author shaozucheng
 * Time:16/6/29 下午5:54
 */
public class CardTimeLimitAdapter extends BaseRecycleViewAdapter<CardTimeLimitAdapter.CardTimeLimitViewHolder, TimeLimitData> {


    private Context mContext;

    public CardTimeLimitAdapter(Context context) {
        super(context);
        this.mContext = context;
    }

    @Override
    protected CardTimeLimitViewHolder createHeaderViewHolder() {
        return null;
    }

    @Override
    protected CardTimeLimitViewHolder createViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_card_destriation, parent, false);
        return new CardTimeLimitViewHolder(view);
    }

    public class CardTimeLimitViewHolder extends BaseRecycleViewHolder<TimeLimitData> {
        TextView mLimitTitleTextView;
        TextView mCardPeriodTextView;

        public CardTimeLimitViewHolder(View itemView) {
            super(itemView);
            mLimitTitleTextView = (TextView) itemView.findViewById(R.id.card_limit_title);
            mCardPeriodTextView = (TextView) itemView.findViewById(R.id.card_period);
        }

        @Override
        public void bindViews(TimeLimitData object) {
            mLimitTitleTextView.setText(object.getTitle());
            mCardPeriodTextView.setText(object.getDesc());
        }
    }
}
