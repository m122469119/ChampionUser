package com.goodchef.liking.adapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aaron.android.framework.base.widget.recycleview.BaseRecycleViewAdapter;
import com.aaron.android.framework.base.widget.recycleview.BaseRecycleViewHolder;
import com.goodchef.liking.R;
import com.goodchef.liking.http.result.data.ConfirmCard;
import com.goodchef.liking.http.result.data.TimeLimitData;

import java.util.List;

/**
 * 说明:
 * Author shaozucheng
 * Time:16/6/29 下午5:34
 */
public class CardRecyclerAdapter extends BaseRecycleViewAdapter<CardRecyclerAdapter.CardRecyclerViewHolder, ConfirmCard> {

    private Context mContext;
    private View.OnClickListener mClickListener;

    public CardRecyclerAdapter(Context context) {
        super(context);
        this.mContext = context;
    }

    public void setLayoutOnClickListner(View.OnClickListener listener) {
        this.mClickListener = listener;
    }

    @Override
    protected CardRecyclerViewHolder createHeaderViewHolder() {
        return null;
    }

    @Override
    protected CardRecyclerViewHolder createViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_card, parent, false);
        return new CardRecyclerViewHolder(view);
    }

     class CardRecyclerViewHolder extends BaseRecycleViewHolder<ConfirmCard> {
        TextView mCardNameTextView;
        RecyclerView mRecyclerView;
        CheckBox mCheckBox;
        LinearLayout mLayout;

        public CardRecyclerViewHolder(View itemView) {
            super(itemView);
            mCardNameTextView = (TextView) itemView.findViewById(R.id.card_category_name);
            mRecyclerView = (RecyclerView) itemView.findViewById(R.id.item_card_titel_recyclerView);
            mCheckBox = (CheckBox) itemView.findViewById(R.id.whole_day_checkBox);
            mLayout = (LinearLayout) itemView.findViewById(R.id.layout_confirm_card);
        }

        @Override
        public void bindViews(ConfirmCard object) {
            mCardNameTextView.setText(object.getName());
            boolean isSelect = object.isSelect();
            if (isSelect) {
                mCheckBox.setChecked(true);
            } else {
                mCheckBox.setChecked(false);
            }
            List<TimeLimitData> limitDataList = object.getTimeLimit();
            if (limitDataList != null && limitDataList.size() > 0) {
                LinearLayoutManager mLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
                mRecyclerView.setLayoutManager(mLayoutManager);
                CardTimeLimitAdapter adapter = new CardTimeLimitAdapter(mContext);
                adapter.setData(limitDataList);
                mRecyclerView.setAdapter(adapter);
            }
            mLayout.setTag(object);
            mLayout.setOnClickListener(mClickListener);
        }

    }
}
