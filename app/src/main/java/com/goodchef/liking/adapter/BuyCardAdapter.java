package com.goodchef.liking.adapter;

import android.content.Context;
import android.os.Build;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.aaron.android.framework.base.widget.recycleview.BaseRecycleViewAdapter;
import com.aaron.android.framework.base.widget.recycleview.BaseRecycleViewHolder;
import com.goodchef.liking.R;
import com.goodchef.liking.data.remote.retrofit.result.CardResult;
import com.goodchef.liking.widgets.OutTextView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 说明:买卡适配器
 * Author shaozucheng
 * Time:16/6/17 上午11:38
 */
public class BuyCardAdapter extends BaseRecyclerAdapter<CardResult.CardData.Category.CardBean> {

    private View.OnClickListener mClickListener;

    public BuyCardAdapter(Context context) {
        super(context);
    }

    boolean isActivity;

    @Override
    public RecyclerView.ViewHolder onCreate(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_buy_card, parent, false);
        return new BuyCardViewHolder(view);
    }

    @Override
    public void onBind(RecyclerView.ViewHolder viewHolder, int RealPosition, CardResult.CardData.Category.CardBean data) {
        BuyCardViewHolder holder = (BuyCardViewHolder) viewHolder;
        holder.bindViews(data);
    }

    public void setBuyCardListener(View.OnClickListener onClickListener) {
        mClickListener = onClickListener;
    }

    public void setIsActivity(boolean activity) {
        isActivity = activity;
    }


    public class BuyCardViewHolder extends BaseRecycleViewHolder<CardResult.CardData.Category.CardBean> {

        @BindView(R.id.buy_card_card_view)
        View mItem;
        @BindView(R.id.left_icon)
        ImageView mLeftImg;
        @BindView(R.id.type)
        TextView mTypeText;
        @BindView(R.id.price)
        TextView mPrice;
        @BindView(R.id.out_price)
        OutTextView mOutPrice;

        public BuyCardViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @Override
        public void bindViews(CardResult.CardData.Category.CardBean object) {
            mTypeText.setText(object.getCategory_name());
            mOutPrice.setText(object.getOld_price());
            mPrice.setText(object.getPrice());
            setLeftIcon(object);

            if (isActivity) {
                mOutPrice.setVisibility(View.VISIBLE);
            } else {
                mOutPrice.setVisibility(View.GONE);
            }
        }

        private void setLeftIcon(CardResult.CardData.Category.CardBean object) {
            switch (object.getCategory_type()) {
                case 1: //月卡
                    mLeftImg.setImageResource(R.mipmap.buy_card_item_icon_0);
                    break;
                case 2: //季卡
                    mLeftImg.setImageResource(R.mipmap.buy_card_item_icon_1);
                    break;
                case 3: //半年卡
                    mLeftImg.setImageResource(R.mipmap.buy_card_item_icon_2);
                    break;
                case 4: //年卡
                    mLeftImg.setImageResource(R.mipmap.buy_card_item_icon_4);
                    break;
            }
        }
    }
}
