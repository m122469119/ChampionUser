package com.goodchef.liking.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.aaron.android.codelibrary.utils.StringUtils;
import com.aaron.android.framework.base.widget.recycleview.BaseRecycleViewAdapter;
import com.aaron.android.framework.base.widget.recycleview.BaseRecycleViewHolder;
import com.goodchef.liking.R;
import com.goodchef.liking.http.result.BodyTestResult;
import com.goodchef.liking.utils.TypefaseUtil;

/**
 * 说明:
 * Author : shaozucheng
 * Time: 下午3:31
 * version 1.0.0
 */

public class BodyAnalyzeAdapter extends BaseRecycleViewAdapter<BodyAnalyzeAdapter.BodyAnalyzeViewHolder, BodyTestResult.BodyTestData.BodyAnalysisData.BodyDataData> {

    private Context mContext;
    private Typeface mTypeface;

    public BodyAnalyzeAdapter(Context context) {
        super(context);
        this.mContext = context;
        mTypeface = TypefaseUtil.getImpactTypeface(mContext);
    }

    @Override
    protected BodyAnalyzeViewHolder createViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.dialog_item_body_ingredient, parent, false);
        return new BodyAnalyzeViewHolder(view);
    }

    public class BodyAnalyzeViewHolder extends BaseRecycleViewHolder<BodyTestResult.BodyTestData.BodyAnalysisData.BodyDataData> {

        TextView typeTextView;
        TextView typeValueTextView;
        TextView typeValueUnitTextView;
        TextView typeStateTextView;
        TextView typeStandardTextView;

        public BodyAnalyzeViewHolder(View itemView) {
            super(itemView);
            typeTextView = (TextView) itemView.findViewById(R.id.dialog_type_TextView);
            typeValueTextView = (TextView) itemView.findViewById(R.id.dialog_type_value_TextView);
            typeValueUnitTextView = (TextView) itemView.findViewById(R.id.dialog_type_value_unit_TextView);
            typeStateTextView = (TextView) itemView.findViewById(R.id.dialog_type_state_TextView);
            typeStandardTextView = (TextView) itemView.findViewById(R.id.dialog_type_standard_TextView);
        }

        @Override
        public void bindViews(BodyTestResult.BodyTestData.BodyAnalysisData.BodyDataData object) {

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
                result = "偏高";
            } else if (Float.parseFloat(value) < Float.parseFloat(min)) {
                result = "偏低";
            } else {
                result = "正常";
            }
            return result;
        }
    }
}
