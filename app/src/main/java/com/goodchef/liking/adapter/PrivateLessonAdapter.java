package com.goodchef.liking.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.aaron.android.framework.base.widget.recycleview.BaseRecycleViewAdapter;
import com.aaron.android.framework.base.widget.recycleview.BaseRecycleViewHolder;
import com.goodchef.liking.R;
import com.goodchef.liking.http.result.MyPrivateCoursesResult;

/**
 * 说明:
 * Author shaozucheng
 * Time:16/5/31 下午5:31
 */
public class PrivateLessonAdapter extends BaseRecycleViewAdapter<PrivateLessonAdapter.PrivateLessonViewHolder, MyPrivateCoursesResult.PrivateCoursesData.PrivateCourses> {
    private Context mContext;

    public PrivateLessonAdapter(Context context) {
        super(context);
        this.mContext = context;
    }

    @Override
    protected PrivateLessonViewHolder createHeaderViewHolder() {
        return null;
    }

    @Override
    protected PrivateLessonViewHolder createViewHolder(ViewGroup parent) {
        View mRootView = LayoutInflater.from(mContext).inflate(R.layout.item_private_my_lesson, parent, false);
        return new PrivateLessonViewHolder(mRootView);
    }


    class PrivateLessonViewHolder extends BaseRecycleViewHolder<MyPrivateCoursesResult.PrivateCoursesData.PrivateCourses> {

        TextView mTeacherNameTextView;

        public PrivateLessonViewHolder(View itemView) {
            super(itemView);
            mTeacherNameTextView = (TextView) itemView.findViewById(R.id.private_teacher_name);
        }

        @Override
        public void bindViews(MyPrivateCoursesResult.PrivateCoursesData.PrivateCourses object) {
            mTeacherNameTextView.setText(object.getCourseName());
            mTeacherNameTextView.setTag(object);
        }
    }
}
