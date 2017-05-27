package com.goodchef.liking.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.aaron.common.utils.StringUtils;
import com.aaron.android.framework.base.widget.recycleview.BaseRecycleViewAdapter;
import com.aaron.android.framework.base.widget.recycleview.BaseRecycleViewHolder;
import com.goodchef.liking.R;
import com.goodchef.liking.data.remote.retrofit.result.BodyTestResult;
import com.goodchef.liking.utils.TypefaseUtil;

/**
 * 说明:
 * Author : shaozucheng
 * Time: 下午3:58
 * version 1.0.0
 */

public class FatAnalyzeAdapter extends BaseRecycleViewAdapter<FatAnalyzeAdapter.FatAnalyzeViewHolder, BodyTestResult.BodyTestData.FatAnalysisData.BodyDataData> {

    private Context context;
    private Typeface mTypeface;

    public FatAnalyzeAdapter(Context context) {
        super(context);
        this.context = context;
        mTypeface = TypefaseUtil.getImpactTypeface(context);
    }

    @Override
    protected FatAnalyzeViewHolder createViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_item_body_ingredient, parent, false);
        return new FatAnalyzeViewHolder(view);
    }


    class FatAnalyzeViewHolder extends BaseRecycleViewHolder<BodyTestResult.BodyTestData.FatAnalysisData.BodyDataData> {
        TextView typeTextView;
        TextView typeValueTextView;
        TextView typeValueUnitTextView;
        TextView typeStateTextView;
        TextView typeStandardTextView;

        public FatAnalyzeViewHolder(View itemView) {
            super(itemView);
            typeTextView = (TextView) itemView.findViewById(R.id.dialog_type_TextView);
            typeValueTextView = (TextView) itemView.findViewById(R.id.dialog_type_value_TextView);
            typeValueUnitTextView = (TextView) itemView.findViewById(R.id.dialog_type_value_unit_TextView);
            typeStateTextView = (TextView) itemView.findViewById(R.id.dialog_type_state_TextView);
            typeStandardTextView = (TextView) itemView.findViewById(R.id.dialog_type_standard_TextView);
        }

        @Override
        public void bindViews(BodyTestResult.BodyTestData.FatAnalysisData.BodyDataData object) {

            typeValueTextView.setTypeface(mTypeface);
            typeValueUnitTextView.setTypeface(mTypeface);
            typeStandardTextView.setTypeface(mTypeface);

            String weightValue = object.getValue();
            String weightMin = object.getCriterionMin();
            String weightMax = object.getCriterionMax();

            typeTextView.setText(object.getChineseName() + ": ");
            typeValueTextView.setText(weightValue);
            typeValueUnitTextView.setText(object.getUnit() + " ");
            typeStateTextView.setText(compareValue(weightMax, weightMin, weightValue));
            typeStandardTextView.setText(weightMin + "~" + weightMax);
        }

        /**
         * 比较值得大小
         *
         * @param max   最大值
         * @param min   最小值
         * @param value 现在的值
         * @return result
         */
        private String compareValue(String max, String min, String value) {
            String result;
            if (StringUtils.isEmpty(max) || StringUtils.isEmpty(min) || StringUtils.isEmpty(value)) {
                return "";
            }
            if (Float.parseFloat(value) > Float.parseFloat(max)) {
                result = context.getString(R.string.on_the_high_side);
            } else if (Float.parseFloat(value) < Float.parseFloat(min)) {
                result = context.getString(R.string.flat);
            } else {
                result = context.getString(R.string.normal);
            }
            return result;
        }
    }
}
