package com.goodchef.liking.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.aaron.android.framework.base.widget.recycleview.BaseRecycleViewAdapter;
import com.aaron.android.framework.base.widget.recycleview.BaseRecycleViewHolder;
import com.goodchef.liking.R;
import com.goodchef.liking.http.result.SelfHelpGroupCoursesResult;

/**
 * 说明:
 * Author : liking
 * Time: 下午6:13
 */

public class SelfHelpGroupCourseTimeAdapter extends BaseRecycleViewAdapter<SelfHelpGroupCourseTimeAdapter.SelfHelpGroupCourseTimeViewHolder, SelfHelpGroupCoursesResult.SelfHelpGroupCoursesData.TimeData.HourData> {

    private Context mContext;

    public SelfHelpGroupCourseTimeAdapter(Context context) {
        super(context);
        this.mContext = context;
    }

    @Override
    protected SelfHelpGroupCourseTimeViewHolder createViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.viewholder_self_help_group_time, parent, false);
        return new SelfHelpGroupCourseTimeViewHolder(view);
    }

    public class SelfHelpGroupCourseTimeViewHolder extends BaseRecycleViewHolder<SelfHelpGroupCoursesResult.SelfHelpGroupCoursesData.TimeData.HourData> {
        TextView mTimeTextView;

        public SelfHelpGroupCourseTimeViewHolder(View itemView) {
            super(itemView);
            mTimeTextView = (TextView) itemView.findViewById(R.id.self_help_courses_time);
        }

        @Override
        public void bindViews(SelfHelpGroupCoursesResult.SelfHelpGroupCoursesData.TimeData.HourData object) {
            mTimeTextView.setText(object.getHour());
        }
    }
}
