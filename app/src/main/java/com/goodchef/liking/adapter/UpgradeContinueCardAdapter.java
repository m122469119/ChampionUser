package com.goodchef.liking.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.aaron.android.framework.base.widget.recycleview.BaseRecycleViewAdapter;
import com.aaron.android.framework.base.widget.recycleview.BaseRecycleViewHolder;
import com.goodchef.liking.R;
import com.goodchef.liking.http.result.CardResult;

/**
 * 说明:
 * Author shaozucheng
 * Time:16/6/30 上午11:21
 */
public class UpgradeContinueCardAdapter extends BaseRecycleViewAdapter<UpgradeContinueCardAdapter.UpgradeContinueCardViewHolder, CardResult.CardData.Card> {

    private Context mContext;
    public UpgradeContinueCardAdapter(Context context) {
        super(context);
        this.mContext = context;
    }

    @Override
    protected UpgradeContinueCardViewHolder createHeaderViewHolder() {
        return null;
    }

    @Override
    protected UpgradeContinueCardViewHolder createViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_continue_card, parent, false);
        return new UpgradeContinueCardViewHolder(view);
    }

    public class UpgradeContinueCardViewHolder extends BaseRecycleViewHolder<CardResult.CardData.Card> {
        TextView mCardMoneyTextView;
        TextView mCardTypeTextView;
        TextView mBuyCardBtn;

        public UpgradeContinueCardViewHolder(View itemView) {
            super(itemView);
            mCardMoneyTextView = (TextView) itemView.findViewById(R.id.upgrade_card_money);
            mCardTypeTextView = (TextView) itemView.findViewById(R.id.upgrade_card_type);
            mBuyCardBtn = (TextView) itemView.findViewById(R.id.upgrade_buy_card_btn);
        }

        @Override
        public void bindViews(CardResult.CardData.Card object) {
            mCardMoneyTextView.setText(object.getPrice());
            mCardTypeTextView.setText(object.getCategoryName());
        }
    }
}
