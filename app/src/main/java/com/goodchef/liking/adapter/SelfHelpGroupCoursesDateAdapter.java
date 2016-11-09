package com.goodchef.liking.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
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
 * Time: 下午4:19
 */

public class SelfHelpGroupCoursesDateAdapter extends BaseRecycleViewAdapter<SelfHelpGroupCoursesDateAdapter.SelfHelpGroupCoursesDateViewHolder, SelfHelpGroupCoursesResult.SelfHelpGroupCoursesData.TimeData> {

    private Context mContext;

    public SelfHelpGroupCoursesDateAdapter(Context context) {
        super(context);
        this.mContext = context;
    }

    @Override
    protected SelfHelpGroupCoursesDateViewHolder createViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.viewholder_self_help_group_date, parent, false);
        return new SelfHelpGroupCoursesDateViewHolder(view);
    }

    public class SelfHelpGroupCoursesDateViewHolder extends BaseRecycleViewHolder<SelfHelpGroupCoursesResult.SelfHelpGroupCoursesData.TimeData> {

        TextView mDateTextView;
        RecyclerView mRecyclerView;

        public SelfHelpGroupCoursesDateViewHolder(View itemView) {
            super(itemView);
            mDateTextView = (TextView) itemView.findViewById(R.id.self_help_courses_date);
            mRecyclerView = (RecyclerView) itemView.findViewById(R.id.self_help_courses_time_RecyclerView);
        }

        @Override
        public void bindViews(SelfHelpGroupCoursesResult.SelfHelpGroupCoursesData.TimeData object) {
           mDateTextView.setText(object.getDate());
        }
    }
}
