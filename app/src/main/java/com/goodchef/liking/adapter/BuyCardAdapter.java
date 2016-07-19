package com.goodchef.liking.adapter;

import android.content.Context;
import android.os.Build;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.aaron.android.framework.base.widget.recycleview.BaseRecycleViewAdapter;
import com.aaron.android.framework.base.widget.recycleview.BaseRecycleViewHolder;
import com.goodchef.liking.R;
import com.goodchef.liking.http.result.CardResult;

/**
 * 说明:买卡适配器
 * Author shaozucheng
 * Time:16/6/17 上午11:38
 */
public class BuyCardAdapter extends BaseRecycleViewAdapter<BuyCardAdapter.BuyCardViewHolder, CardResult.CardData.Card> {

    private Context mContext;
    private View.OnClickListener mClickListener;

    public BuyCardAdapter(Context context) {
        super(context);
        this.mContext = context;
    }

    @Override
    protected BuyCardViewHolder createHeaderViewHolder() {
        return new BuyCardViewHolder(getHeaderView());
    }

    public void setBuyCardListener(View.OnClickListener onClickListener) {
        mClickListener = onClickListener;
    }

    @Override
    protected BuyCardViewHolder createViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_buy_card, parent, false);
        return new BuyCardViewHolder(view);
    }

    public class BuyCardViewHolder extends BaseRecycleViewHolder<CardResult.CardData.Card> {
        TextView mCardMoneyTextView;
        TextView mCardTypeTextView;
        TextView mBuyCardBtn;
        CardView mCardView;

        public BuyCardViewHolder(View itemView) {
            super(itemView);
            mCardMoneyTextView = (TextView) itemView.findViewById(R.id.card_money);
            mCardTypeTextView = (TextView) itemView.findViewById(R.id.card_type);
            mBuyCardBtn = (TextView) itemView.findViewById(R.id.buy_card_btn);
            mCardView = (CardView) itemView.findViewById(R.id.buy_card_card_view);
            setCardView();
        }


        @Override
        public void bindViews(CardResult.CardData.Card object) {
            mCardMoneyTextView.setText(object.getPrice());
            mCardTypeTextView.setText(object.getCategoryName());
            if (mClickListener != null) {
                mBuyCardBtn.setOnClickListener(mClickListener);
            }
        }

        private void setCardView() {//设置兼容低版本
            if (mCardView != null) {
                if (Build.VERSION.SDK_INT < 21) {
                    mCardView.setCardElevation(0);
                    mCardView.setContentPadding(-10, -10, -10, -10);
                } else {
                    mCardView.setCardElevation(10);
                    mCardView.setContentPadding(0, 0, 0, 0);
                }
            }
        }
    }
}
