package com.goodchef.liking.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.aaron.android.framework.base.widget.recycleview.BaseRecycleViewAdapter;
import com.aaron.android.framework.base.widget.recycleview.BaseRecycleViewHolder;
import com.goodchef.liking.R;
import com.goodchef.liking.data.remote.retrofit.result.BodyHistoryResult;
import com.goodchef.liking.utils.TypefaseUtil;

/**
 * 说明:
 * Author : shaozucheng
 * Time: 下午7:19
 * version 1.0.0
 */

public class BodyTestHistoryAdapter extends BaseRecycleViewAdapter<BodyTestHistoryAdapter.BodyTestHistoryViewHolder, BodyHistoryResult.BodyHistoryData.ListData> {

    private Context context;

    public BodyTestHistoryAdapter(Context context) {
        super(context);
        this.context = context;
    }

    @Override
    protected BodyTestHistoryViewHolder createViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.viewholder_bodytest_history, parent, false);
        return new BodyTestHistoryViewHolder(view);
    }

    class BodyTestHistoryViewHolder extends BaseRecycleViewHolder<BodyHistoryResult.BodyHistoryData.ListData> {
        CardView mCardView;
        TextView mBodyHistoryGradeTextView;

        TextView mBodyIndexNumberTextView;
        TextView mBodyIndexNumberUnitTextView;
        TextView mBodyIndexBmiTextView;
        TextView mBmiChineseNameTextView;

        TextView mBodyFatNumberTextView;
        TextView mBodyFatNumberUnitTextView;
        TextView mBodyFatTextView;
        TextView mFatChineseNameTextView;

        TextView mBodyWhrNumberTextView;
        TextView mBodyWhrUnitTextView;
        TextView mBodyWhrTextView;
        TextView mWhrChineseNameTextView;

        TextView mBodyMeasureTimeTextView;

        public BodyTestHistoryViewHolder(View itemView) {
            super(itemView);
            mCardView = (CardView) itemView.findViewById(R.id.bodytest_history_cardView);
            mBodyHistoryGradeTextView = (TextView) itemView.findViewById(R.id.bodytest_history_grade_TextView);
            mBodyIndexNumberTextView = (TextView) itemView.findViewById(R.id.body_index_number_TextView);
            mBodyIndexNumberUnitTextView = (TextView) itemView.findViewById(R.id.body_index_number_unit_TextView);
            mBodyIndexBmiTextView = (TextView) itemView.findViewById(R.id.body_index_bmi_TextView);
            mBmiChineseNameTextView = (TextView) itemView.findViewById(R.id.bmi_chinese_name_TextView);

            mBodyFatNumberTextView = (TextView) itemView.findViewById(R.id.body_fat_number_TextView);
            mBodyFatNumberUnitTextView = (TextView) itemView.findViewById(R.id.body_fat_number_unit_TextView);
            mBodyFatTextView = (TextView) itemView.findViewById(R.id.body_fat_TextView);
            mFatChineseNameTextView = (TextView) itemView.findViewById(R.id.fat_chinese_name_textView);

            mBodyWhrNumberTextView = (TextView) itemView.findViewById(R.id.body_whr_number_TextView);
            mBodyWhrUnitTextView = (TextView) itemView.findViewById(R.id.body_whr_unit_TextView);
            mBodyWhrTextView = (TextView) itemView.findViewById(R.id.body_whr_TextView);
            mWhrChineseNameTextView = (TextView) itemView.findViewById(R.id.whr_chinese_name_TextView);

            mBodyMeasureTimeTextView = (TextView) itemView.findViewById(R.id.body_measure_time_TextView);
        }

        @Override
        public void bindViews(BodyHistoryResult.BodyHistoryData.ListData object) {
            setTextViewType();
            BodyHistoryResult.BodyHistoryData.ListData.BmiData bmiData = object.getBmi();
            BodyHistoryResult.BodyHistoryData.ListData.FatRateData fatRateData = object.getFatRate();
            BodyHistoryResult.BodyHistoryData.ListData.WaistHipData waistHipData = object.getWaistHip();

            mBodyHistoryGradeTextView.setText(object.getScore());
            if (bmiData != null) {
                mBodyIndexNumberTextView.setText(bmiData.getValue());
                mBodyIndexNumberUnitTextView.setText(bmiData.getUnit());
                mBodyIndexBmiTextView.setText(bmiData.getEnglishName());
                mBmiChineseNameTextView.setText(bmiData.getChineseName());
            }
            if (fatRateData != null) {
                mBodyFatNumberTextView.setText(fatRateData.getValue());
                mBodyFatNumberUnitTextView.setText(fatRateData.getUnit());
                mBodyFatTextView.setText(fatRateData.getEnglishName());
                mFatChineseNameTextView.setText(fatRateData.getChineseName());
            }
            if (waistHipData != null) {
                mBodyWhrNumberTextView.setText(waistHipData.getValue());
                mBodyWhrUnitTextView.setText(waistHipData.getUnit());
                mBodyWhrTextView.setText(waistHipData.getEnglishName());
                mWhrChineseNameTextView.setText(waistHipData.getChineseName());
            }
            mBodyMeasureTimeTextView.setText(context.getString(R.string.body_measure_time) + object.getBodyTime());
        }

        private void setTextViewType() {
            Typeface typeface = TypefaseUtil.getImpactTypeface(context);
            mBodyHistoryGradeTextView.setTypeface(typeface);
            mBodyIndexNumberTextView.setTypeface(typeface);
            mBodyIndexNumberUnitTextView.setTypeface(typeface);
            mBodyIndexBmiTextView.setTypeface(typeface);
            mBodyFatNumberTextView.setTypeface(typeface);
            mBodyFatNumberUnitTextView.setTypeface(typeface);
            mBodyFatTextView.setTypeface(typeface);
            mBodyWhrNumberTextView.setTypeface(typeface);
            mBodyWhrUnitTextView.setTypeface(typeface);
            mBodyWhrTextView.setTypeface(typeface);
        }
    }
}
