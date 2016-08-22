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
import com.goodchef.liking.http.result.MyGroupCoursesResult;

/**
 * 说明:
 * Author shaozucheng
 * Time:16/5/31 下午5:01
 */
public class MyGroupCoursesAdapter extends BaseRecycleViewAdapter<MyGroupCoursesAdapter.GroupLessonViewHolder, MyGroupCoursesResult.MyGroupCoursesData.MyGroupCourses> {
    private static final int COURSES_STATE_NOT_START = 0;// 未开始
    private static final int COURSES_STATE_PROCESS = 1;//进行中
    private static final int COURSES_STATE_OVER = 2;//已结束
    private static final int COURSES_STATE_CANCEL = 3;//已取消
    private static final int TYPE_IS_FREE = 0;//免费
    private static final int TYPE_NOT_FREE = 1;//收费
    private Context mContext;


    private View.OnClickListener mCancelListener;

    public MyGroupCoursesAdapter(Context context) {
        super(context);
        this.mContext = context;
    }

    public void setCancelListener(View.OnClickListener listener) {
        mCancelListener = listener;
    }

    @Override
    protected GroupLessonViewHolder createHeaderViewHolder() {
        return null;
    }

    @Override
    protected GroupLessonViewHolder createViewHolder(ViewGroup parent) {
        View mRootView = LayoutInflater.from(mContext).inflate(R.layout.item_group_lesson, parent, false);
        TextView mCancelOrderBtn = (TextView) mRootView.findViewById(R.id.cancel_order_btn);
        if (mCancelListener != null) {
            mCancelOrderBtn.setOnClickListener(mCancelListener);
        }
        return new GroupLessonViewHolder(mRootView);
    }

    class GroupLessonViewHolder extends BaseRecycleViewHolder<MyGroupCoursesResult.MyGroupCoursesData.MyGroupCourses> {
        HImageView mHImageView;
        TextView mPeriodOfValidityTextView;//时间
        TextView mGroupCoursesNameTextView;//课程名称
        TextView mShopNameTextView;//门店名称
        TextView mGroupCoursesStateTextView;//课程状态
        TextView mShopAddressTextView;//门店地址
        TextView mCoursesMoneyTextView;//课程金额
        TextView mCancelOrderBtn;//取消预约


        public GroupLessonViewHolder(View itemView) {
            super(itemView);
            mHImageView = (HImageView) itemView.findViewById(R.id.group_lesson_image);
            mPeriodOfValidityTextView = (TextView) itemView.findViewById(R.id.group_lesson_period_of_validity);
            mGroupCoursesNameTextView = (TextView) itemView.findViewById(R.id.group_courses_name);
            mShopNameTextView = (TextView) itemView.findViewById(R.id.shop_name);
            mGroupCoursesStateTextView = (TextView) itemView.findViewById(R.id.group_courses_state);
            mShopAddressTextView = (TextView) itemView.findViewById(R.id.shop_address);
            mCancelOrderBtn = (TextView) itemView.findViewById(R.id.cancel_order_btn);
            mCoursesMoneyTextView = (TextView) itemView.findViewById(R.id.courses_money);
        }

        @Override
        public void bindViews(MyGroupCoursesResult.MyGroupCoursesData.MyGroupCourses object) {
            mPeriodOfValidityTextView.setText(object.getStartDate() + "(" + object.getWeekDay() + ") " + object.getStartTime() + " ~ " + object.getEndTime());
            mGroupCoursesNameTextView.setText(object.getCourseName());
            mShopNameTextView.setText(object.getGymName());
            mShopAddressTextView.setText(object.getGymAddress());
            int state = object.getStatus();
            setCoursesState(state);
            mPeriodOfValidityTextView.setTag(object);
            mCancelOrderBtn.setTag(object);
            String imageUrl = object.getCourseUrl();
            if (!StringUtils.isEmpty(imageUrl)) {
                HImageLoaderSingleton.getInstance().requestImage(mHImageView, imageUrl);
            }
            int isFree = object.getIsFee();
            if (isFree == TYPE_IS_FREE) {//免费
                mCoursesMoneyTextView.setVisibility(View.GONE);
            } else if (isFree == TYPE_NOT_FREE) {//收费
                mCoursesMoneyTextView.setVisibility(View.VISIBLE);
                mCoursesMoneyTextView.setText("¥ " + object.getAmount());
            }

        }

        private void setCoursesState(int state) {
            if (state == COURSES_STATE_NOT_START) {//未开始
                mGroupCoursesStateTextView.setText(R.string.not_start);
                mGroupCoursesStateTextView.setTextColor(ResourceUtils.getColor(R.color.my_group_green_text));
                mGroupCoursesNameTextView.setTextColor(ResourceUtils.getColor(R.color.lesson_details_dark_back));
                mCancelOrderBtn.setVisibility(View.VISIBLE);
            } else if (state == COURSES_STATE_PROCESS) {//进行中
                mGroupCoursesStateTextView.setText(R.string.start_process);
                mGroupCoursesStateTextView.setTextColor(ResourceUtils.getColor(R.color.my_group_green_text));
                mGroupCoursesNameTextView.setTextColor(ResourceUtils.getColor(R.color.lesson_details_gray_back));
                mCancelOrderBtn.setVisibility(View.INVISIBLE);
            } else if (state == COURSES_STATE_OVER) {//已结束
                mGroupCoursesStateTextView.setText(R.string.courses_complete);
                mGroupCoursesStateTextView.setTextColor(ResourceUtils.getColor(R.color.my_group_green_text));
                mGroupCoursesNameTextView.setTextColor(ResourceUtils.getColor(R.color.lesson_details_gray_back));
                mCancelOrderBtn.setVisibility(View.INVISIBLE);
            } else if (state == COURSES_STATE_CANCEL) {//已取消
                mGroupCoursesStateTextView.setText(R.string.courses_cancel);
                mGroupCoursesStateTextView.setTextColor(ResourceUtils.getColor(R.color.lesson_details_gray_back));
                mGroupCoursesNameTextView.setTextColor(ResourceUtils.getColor(R.color.lesson_details_gray_back));
                mCancelOrderBtn.setVisibility(View.INVISIBLE);
            }
        }

    }
}
