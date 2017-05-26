package com.goodchef.liking.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.aaron.android.framework.base.widget.recycleview.BaseRecycleViewAdapter;
import com.aaron.android.framework.base.widget.recycleview.BaseRecycleViewHolder;
import com.goodchef.liking.R;
import com.goodchef.liking.data.remote.retrofit.result.data.ConfirmCard;
import com.goodchef.liking.data.remote.retrofit.result.data.TimeLimitData;
import com.goodchef.liking.utils.ListViewUtil;

import java.util.List;

/**
 * 说明:
 * Author shaozucheng
 * Time:16/6/29 下午5:34
 */
public class CardRecyclerAdapter extends BaseRecycleViewAdapter<CardRecyclerAdapter.CardRecyclerViewHolder, ConfirmCard> {

    private View.OnClickListener mClickListener;
    private View.OnClickListener mExplainClickListener;

    public CardRecyclerAdapter(Context context) {
        super(context);
    }

    public void setLayoutOnClickListener(View.OnClickListener listener) {
        this.mClickListener = listener;
    }

    public void setExplainClickListener(View.OnClickListener listener) {
        this.mExplainClickListener = listener;
    }

    @Override
    protected CardRecyclerViewHolder createViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.item_card, parent, false);
        return new CardRecyclerViewHolder(view);
    }

    class CardRecyclerViewHolder extends BaseRecycleViewHolder<ConfirmCard> {
        TextView mCardNameTextView;
        ListView mListView;
        CheckBox mCheckBox;
        LinearLayout mLayout;
        TextView mExplainTextView;

        public CardRecyclerViewHolder(View itemView) {
            super(itemView);
            mCardNameTextView = (TextView) itemView.findViewById(R.id.card_category_name);
            mListView = (ListView) itemView.findViewById(R.id.item_card_titel_recyclerView);
            mCheckBox = (CheckBox) itemView.findViewById(R.id.whole_day_checkBox);
            mLayout = (LinearLayout) itemView.findViewById(R.id.layout_confirm_card);
            mExplainTextView = (TextView) itemView.findViewById(R.id.explain);
        }

        @Override
        public void bindViews(ConfirmCard object) {
            mCardNameTextView.setText(object.getName());
            int canSelect = object.getCanSelect();
            if (canSelect == 0) {//不能选择
                mLayout.setEnabled(false);
                mExplainTextView.setVisibility(View.VISIBLE);
                mCheckBox.setVisibility(View.GONE);
            } else if (canSelect == 1) {//可以选择
                mLayout.setEnabled(true);
                mExplainTextView.setVisibility(View.GONE);
                mExplainTextView.setOnClickListener(null);
                mCheckBox.setVisibility(View.VISIBLE);
                int qulification = object.getQulification();
                if (qulification == 0) {
                    mCheckBox.setChecked(false);
                } else if (qulification == 1) {
                    mCheckBox.setChecked(true);
                }
            }

            List<TimeLimitData> limitDataList = object.getTimeLimit();
            if (limitDataList != null && limitDataList.size() > 0) {
                CardTimeLimitAdapter adapter = new CardTimeLimitAdapter(getContext());
                adapter.setData(limitDataList);
                mListView.setAdapter(adapter);
                ListViewUtil.setListViewHeightBasedOnChildren(mListView);
            }
            mLayout.setTag(object);
            if (mClickListener != null) {
                mLayout.setOnClickListener(mClickListener);
            }
            if (mExplainClickListener != null) {
                mExplainTextView.setOnClickListener(mExplainClickListener);
                mExplainTextView.setTag(object);
            }
        }

    }
}
