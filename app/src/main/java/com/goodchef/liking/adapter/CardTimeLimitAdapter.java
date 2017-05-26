package com.goodchef.liking.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.aaron.android.framework.base.widget.listview.HBaseAdapter;
import com.goodchef.liking.R;
import com.goodchef.liking.data.remote.retrofit.result.data.TimeLimitData;

/**
 * 说明:购卡时间限制适配器
 * Author shaozucheng
 * Time:16/6/29 下午5:54
 */
public class CardTimeLimitAdapter extends HBaseAdapter<TimeLimitData> {


    private Context mContext;

    @Override
    protected BaseViewHolder<TimeLimitData> createViewHolder() {
        return new CardTimeLimitViewHolder();
    }

    public CardTimeLimitAdapter(Context context) {
        super(context);
        this.mContext = context;
    }


    public class CardTimeLimitViewHolder extends BaseViewHolder<TimeLimitData> {
        TextView mLimitTitleTextView;
        TextView mCardPeriodTextView;

        @Override
        public View inflateItemView() {
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_card_destriation, null, false);
            mLimitTitleTextView = (TextView) view.findViewById(R.id.card_limit_title);
            mCardPeriodTextView = (TextView) view.findViewById(R.id.card_period);
            return view;
        }

        @Override
        public void bindViews(TimeLimitData object) {
            mLimitTitleTextView.setText(object.getTitle());
            mCardPeriodTextView.setText(object.getDesc());
        }
    }
}
