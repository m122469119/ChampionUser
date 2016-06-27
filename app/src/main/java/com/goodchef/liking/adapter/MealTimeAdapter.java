package com.goodchef.liking.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.aaron.android.framework.base.widget.listview.HBaseAdapter;
import com.aaron.android.framework.utils.ResourceUtils;
import com.goodchef.liking.R;
import com.goodchef.liking.http.result.data.MealTimeData;

/**
 * 说明:
 * Author shaozucheng
 * Time:16/6/27 下午4:20
 */
public class MealTimeAdapter extends HBaseAdapter<MealTimeData> {
    public MealTimeAdapter(Context context) {
        super(context);
    }

    @Override
    protected BaseViewHolder<MealTimeData> createViewHolder() {
        return new MealTimeViewHolder();
    }

    class MealTimeViewHolder extends BaseViewHolder<MealTimeData> {
        View mRootView;
        TextView mTextView;

        @Override
        public View inflateItemView() {
            mRootView = LayoutInflater.from(getContext()).inflate(R.layout.item_meal_time, null, false);
            mTextView = (TextView) mRootView.findViewById(R.id.select_meal_time);
            return mRootView;
        }

        @Override
        public void bindViews(MealTimeData object) {
            boolean isSelect = object.isSelect();
            if (isSelect) {
                mTextView.setTextColor(ResourceUtils.getColor(R.color.add_minus_dishes_text));
            } else {
                mTextView.setTextColor(ResourceUtils.getColor(R.color.lesson_details_dark_back));
            }
            mTextView.setText(object.getMealTime());
            mTextView.setTag(object);
        }
    }
}
