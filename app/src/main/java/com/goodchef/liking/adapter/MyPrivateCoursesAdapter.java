package com.goodchef.liking.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.aaron.android.codelibrary.utils.StringUtils;
import com.aaron.android.framework.base.widget.recycleview.BaseRecycleViewAdapter;
import com.aaron.android.framework.base.widget.recycleview.BaseRecycleViewHolder;
import com.aaron.android.framework.library.imageloader.HImageLoaderSingleton;
import com.aaron.android.framework.library.imageloader.HImageView;
import com.aaron.android.framework.utils.ResourceUtils;
import com.goodchef.liking.R;
import com.goodchef.liking.http.result.MyPrivateCoursesResult;

/**
 * 说明:
 * Author shaozucheng
 * Time:16/5/31 下午5:31
 */
public class MyPrivateCoursesAdapter extends BaseRecycleViewAdapter<MyPrivateCoursesAdapter.PrivateLessonViewHolder, MyPrivateCoursesResult.PrivateCoursesData.PrivateCourses> {
    private static final int COURSES_STATE_PAYED = 0;//0 已支付
    private static final int COURSES_STATE_COMPLETE = 1;//1 已完成
    private static final int COURSES_STATE_CANCEL = 2; //  2已取消
    private Context mContext;
    private View.OnClickListener mCompleteListener;

    public MyPrivateCoursesAdapter(Context context) {
        super(context);
        this.mContext = context;
    }

    public void setCompleteListener(View.OnClickListener listener) {
        this.mCompleteListener = listener;
    }

    @Override
    protected PrivateLessonViewHolder createHeaderViewHolder() {
        return null;
    }

    @Override
    protected PrivateLessonViewHolder createViewHolder(ViewGroup parent) {
        View mRootView = LayoutInflater.from(mContext).inflate(R.layout.item_private_my_lesson, parent, false);
        TextView mCompleteCoursesBtn = (TextView) mRootView.findViewById(R.id.complete_courses_btn);
        if (mCompleteListener != null) {
            mCompleteCoursesBtn.setOnClickListener(mCompleteListener);
        }
        return new PrivateLessonViewHolder(mRootView);
    }


    class PrivateLessonViewHolder extends BaseRecycleViewHolder<MyPrivateCoursesResult.PrivateCoursesData.PrivateCourses> {
        HImageView mHImageView;
        TextView mTeacherNameTextView;//教练名字
        TextView mCoursesTagsTextView;//课程名称
        TextView mPeriodOfValidityTextView;//有效期
        TextView mCoursesStateTextView;//状态
        TextView mCompleteCoursesBtn;//完成课程

        public PrivateLessonViewHolder(View itemView) {
            super(itemView);
            mHImageView = (HImageView) itemView.findViewById(R.id.private_lesson_image);
            mTeacherNameTextView = (TextView) itemView.findViewById(R.id.private_teacher_name);
            mCoursesTagsTextView = (TextView) itemView.findViewById(R.id.courses_tags);
            mPeriodOfValidityTextView = (TextView) itemView.findViewById(R.id.period_of_validity);
            mCoursesStateTextView = (TextView) itemView.findViewById(R.id.private_courses_state);
            mCompleteCoursesBtn = (TextView) itemView.findViewById(R.id.complete_courses_btn);
        }

        @Override
        public void bindViews(MyPrivateCoursesResult.PrivateCoursesData.PrivateCourses object) {
            mTeacherNameTextView.setText(object.getTrainerName());
            mCoursesTagsTextView.setText(object.getCourseName());
            mPeriodOfValidityTextView.setText(object.getStartTime() + " ~ " + object.getEndTime());
            String imageUrl = object.getTrainerAvatar();
            if (!StringUtils.isEmpty(imageUrl)) {
                HImageLoaderSingleton.getInstance().loadImage(mHImageView, imageUrl);
            }
            int state = object.getStatus();
            setCoursesState(state);
            mTeacherNameTextView.setTag(object);
            mCompleteCoursesBtn.setTag(object);
        }

        private void setCoursesState(int state) {
            if (state == COURSES_STATE_PAYED) {//已支付
                mCoursesStateTextView.setText(R.string.courses_state_payed);
                mCoursesStateTextView.setTextColor(ResourceUtils.getColor(R.color.my_group_green_text));
                mCompleteCoursesBtn.setVisibility(View.VISIBLE);
            } else if (state == COURSES_STATE_COMPLETE) {//已完成
                mCoursesStateTextView.setText(R.string.courses_state_complete);
                mCoursesStateTextView.setTextColor(ResourceUtils.getColor(R.color.my_group_green_text));
                mCompleteCoursesBtn.setVisibility(View.GONE);
            } else if (state == COURSES_STATE_CANCEL) {//已取消
                mCoursesStateTextView.setText(R.string.courses_state_cancel);
                mCoursesStateTextView.setTextColor(ResourceUtils.getColor(R.color.lesson_details_gray_back));
                mCompleteCoursesBtn.setVisibility(View.GONE);
            }
        }
    }
}
