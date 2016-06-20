package com.goodchef.liking.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.aaron.android.framework.base.widget.recycleview.BaseRecycleViewAdapter;
import com.aaron.android.framework.base.widget.recycleview.BaseRecycleViewHolder;
import com.goodchef.liking.R;
import com.goodchef.liking.http.result.MyGroupCoursesResult;

/**
 * 说明:
 * Author shaozucheng
 * Time:16/5/31 下午5:01
 */
public class MyGroupLessonAdapter extends BaseRecycleViewAdapter<MyGroupLessonAdapter.GroupLessonViewHolder, MyGroupCoursesResult.MyGroupCoursesData.MyGroupCourses> {

    private Context mContext;

    public MyGroupLessonAdapter(Context context) {
        super(context);
        this.mContext = context;
    }

    @Override
    protected GroupLessonViewHolder createHeaderViewHolder() {
        return null;
    }

    @Override
    protected GroupLessonViewHolder createViewHolder(ViewGroup parent) {
        View mRootView = LayoutInflater.from(mContext).inflate(R.layout.item_group_lesson, parent, false);
        return new GroupLessonViewHolder(mRootView);
    }

    class GroupLessonViewHolder extends BaseRecycleViewHolder<MyGroupCoursesResult.MyGroupCoursesData.MyGroupCourses> {

        TextView mPeriodOfValidityTextView;

        public GroupLessonViewHolder(View itemView) {
            super(itemView);
            mPeriodOfValidityTextView = (TextView) itemView.findViewById(R.id.group_lesson_period_of_validity);
        }

        @Override
        public void bindViews(MyGroupCoursesResult.MyGroupCoursesData.MyGroupCourses object) {
            mPeriodOfValidityTextView.setText(object.getCourseName());
            mPeriodOfValidityTextView.setTag(object);
        }


    }
}
