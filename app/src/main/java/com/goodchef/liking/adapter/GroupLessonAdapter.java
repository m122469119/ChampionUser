package com.goodchef.liking.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.aaron.android.framework.base.widget.listview.HBaseAdapter;
import com.goodchef.liking.R;

/**
 * 说明:
 * Author shaozucheng
 * Time:16/5/31 下午5:01
 */
public class GroupLessonAdapter extends HBaseAdapter<String> {

    private Context mContext;

    public GroupLessonAdapter(Context context) {
        super(context);
        this.mContext = context;
    }

    @Override
    protected BaseViewHolder<String> createViewHolder() {
        return new GroupLessonViewHolder();
    }

    class GroupLessonViewHolder extends BaseViewHolder<String> {
        View mRootView;
        TextView mPeriodOfValidityTextView;

        @Override
        public View inflateItemView() {
            mRootView = LayoutInflater.from(mContext).inflate(R.layout.item_group_lesson, null, false);
            mPeriodOfValidityTextView = (TextView) mRootView.findViewById(R.id.group_lesson_period_of_validity);
            return mRootView;
        }

        @Override
        public void bindViews(String object) {
            mPeriodOfValidityTextView.setText("时间：2016/05/31 17:" + object);
        }
    }
}
