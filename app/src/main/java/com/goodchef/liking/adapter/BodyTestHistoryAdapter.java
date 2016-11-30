package com.goodchef.liking.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Build;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.aaron.android.framework.base.widget.recycleview.BaseRecycleViewAdapter;
import com.aaron.android.framework.base.widget.recycleview.BaseRecycleViewHolder;
import com.goodchef.liking.R;
import com.goodchef.liking.utils.TypefaseUtil;

/**
 * 说明:
 * Author : shaozucheng
 * Time: 下午7:19
 * version 1.0.0
 */

public class BodyTestHistoryAdapter extends BaseRecycleViewAdapter<BodyTestHistoryAdapter.BodyTestHistoryViewHolder, String> {

    private Context mContext;

    public BodyTestHistoryAdapter(Context context) {
        super(context);
        this.mContext = context;
    }

    @Override
    protected BodyTestHistoryViewHolder createViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.viewholder_bodytest_history, parent, false);
        return new BodyTestHistoryViewHolder(view);
    }

    class BodyTestHistoryViewHolder extends BaseRecycleViewHolder<String> {
        CardView mCardView;
        TextView mBodyIndexNumberTextView;
        TextView mBodyIndexNumberUnitTextView;
        TextView mBodyIndexBmiTextView;
        TextView mBodyFatNumberTextView;
        TextView mBodyFatNumberUnitTextView;
        TextView mBodyFatTextView;
        TextView mBodyWhrNumberTextView;
        TextView mBodyWhrTextView;
        TextView mBodyMeasureTimeTextView;

        public BodyTestHistoryViewHolder(View itemView) {
            super(itemView);
            mCardView = (CardView) itemView.findViewById(R.id.bodytest_history_cardView);
            mBodyIndexNumberTextView = (TextView) itemView.findViewById(R.id.body_index_number_TextView);
            mBodyIndexNumberUnitTextView = (TextView) itemView.findViewById(R.id.body_index_number_unit_TextView);
            mBodyIndexBmiTextView = (TextView) itemView.findViewById(R.id.body_index_bmi_TextView);
            mBodyFatNumberTextView = (TextView) itemView.findViewById(R.id.body_fat_number_TextView);
            mBodyFatNumberUnitTextView = (TextView) itemView.findViewById(R.id.body_fat_number_unit_TextView);
            mBodyFatTextView = (TextView) itemView.findViewById(R.id.body_fat_TextView);
            mBodyWhrNumberTextView = (TextView) itemView.findViewById(R.id.body_whr_number_TextView);
            mBodyWhrTextView = (TextView) itemView.findViewById(R.id.body_whr_TextView);
            mBodyMeasureTimeTextView = (TextView) itemView.findViewById(R.id.body_measure_time_TextView);
        }

        @Override
        public void bindViews(String object) {
            if (Build.VERSION.SDK_INT < 21) {
                mCardView.setCardElevation(0);
            } else {
                mCardView.setCardElevation(10);
            }
            setTextViewType();
            mBodyIndexNumberTextView.setText(object);
        }

        private void setTextViewType(){
            Typeface typeface = TypefaseUtil.getImpactTypeface(mContext);
            mBodyIndexNumberTextView.setTypeface(typeface);
            mBodyIndexNumberUnitTextView.setTypeface(typeface);
            mBodyIndexBmiTextView.setTypeface(typeface);
            mBodyFatNumberTextView.setTypeface(typeface);
            mBodyFatNumberUnitTextView.setTypeface(typeface);
            mBodyFatTextView.setTypeface(typeface);
            mBodyWhrNumberTextView.setTypeface(typeface);
            mBodyWhrTextView.setTypeface(typeface);
        }
    }
}
