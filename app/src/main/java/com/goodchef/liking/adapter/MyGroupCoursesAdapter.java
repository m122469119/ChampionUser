package com.goodchef.liking.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aaron.common.utils.StringUtils;
import com.aaron.android.framework.base.widget.recycleview.BaseRecycleViewAdapter;
import com.aaron.android.framework.base.widget.recycleview.BaseRecycleViewHolder;
import com.goodchef.liking.utils.HImageLoaderSingleton;
import com.aaron.imageloader.code.HImageView;
import com.aaron.android.framework.utils.ResourceUtils;
import com.goodchef.liking.R;
import com.goodchef.liking.data.remote.retrofit.result.MyGroupCoursesResult;

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
    private static final int TYPE_SCHEDULE_TYPE_SELF = 2;//自助排课
    private Context mContext;


    private View.OnClickListener mCancelListener;
    private View.OnClickListener mShareListener;

    public MyGroupCoursesAdapter(Context context) {
        super(context);
        this.mContext = context;
    }

    public void setCancelListener(View.OnClickListener listener) {
        mCancelListener = listener;
    }

    public void setShareListener(View.OnClickListener listener) {
        mShareListener = listener;
    }

    @Override
    protected GroupLessonViewHolder createViewHolder(ViewGroup parent) {
        View mRootView = LayoutInflater.from(mContext).inflate(R.layout.item_group_lesson, parent, false);
        TextView mCancelOrderBtn = (TextView) mRootView.findViewById(R.id.cancel_order_btn);
        TextView mSelfShareBtn = (TextView) mRootView.findViewById(R.id.self_share_btn);
        if (mCancelListener != null) {
            mCancelOrderBtn.setOnClickListener(mCancelListener);
        }
        if(mShareListener != null) {
            mSelfShareBtn.setOnClickListener(mShareListener);
        }
        return new GroupLessonViewHolder(mRootView, mContext);
    }

    class GroupLessonViewHolder extends BaseRecycleViewHolder<MyGroupCoursesResult.MyGroupCoursesData.MyGroupCourses> {
        HImageView mHImageView;
        TextView mPeriodOfValidityTextView;//时间
        TextView mGroupCoursesNameTextView;//课程名称
        TextView mShopNameTextView;//门店名称
        TextView mGroupCoursesStateTextView;//课程状态
        TextView mCoursesMoneyTextView;//课程金额
        TextView mCancelOrderBtn;//取消预约
        TextView mSelfShareBtn;//分享给好友
        TextView mFreeType;
        RelativeLayout mMyGroupCoursesLayout;


        public GroupLessonViewHolder(View itemView, Context context) {
            super(itemView, context);
            mHImageView = (HImageView) itemView.findViewById(R.id.group_lesson_image);
            mPeriodOfValidityTextView = (TextView) itemView.findViewById(R.id.group_lesson_period_of_validity);
            mGroupCoursesNameTextView = (TextView) itemView.findViewById(R.id.group_courses_name);
            mShopNameTextView = (TextView) itemView.findViewById(R.id.shop_name);
            mGroupCoursesStateTextView = (TextView) itemView.findViewById(R.id.group_courses_state);
            mCoursesMoneyTextView = (TextView) itemView.findViewById(R.id.courses_money);
            mCancelOrderBtn = (TextView) itemView.findViewById(R.id.cancel_order_btn);
            mFreeType = (TextView) itemView.findViewById(R.id.free_type);
            mSelfShareBtn = (TextView) itemView.findViewById(R.id.self_share_btn);
            mMyGroupCoursesLayout = (RelativeLayout) itemView.findViewById(R.id.layout_my_group_courses);
        }

        @Override
        public void bindViews(MyGroupCoursesResult.MyGroupCoursesData.MyGroupCourses object) {
            mPeriodOfValidityTextView.setText(object.getStartDate() + "(" + object.getWeekDay() + ") " + object.getStartTime() + " ~ " + object.getEndTime());
            mGroupCoursesNameTextView.setText(object.getCourseName());
            mShopNameTextView.setText(object.getPlaceInfo());
            int state = object.getStatus();
            int showCalcel = object.getCancelBtnShow();
            setCoursesState(state);
            mPeriodOfValidityTextView.setTag(object);
            mCancelOrderBtn.setTag(object);
            mSelfShareBtn.setTag(object);
            String imageUrl = object.getCourseUrl();
            if (!StringUtils.isEmpty(imageUrl)) {
                HImageLoaderSingleton.loadImage(mHImageView, imageUrl, (Activity) mContext);
            }
            int scheduleType = object.getScheduleType();
            int isFree = object.getIsFee();
            String tagName = object.getTagName();
            mFreeType.setText(tagName);
            if (isFree == TYPE_IS_FREE) {//免费
                mCoursesMoneyTextView.setVisibility(View.GONE);
                if (showCalcel == 0) {
                    mMyGroupCoursesLayout.setVisibility(View.GONE);
                    mCancelOrderBtn.setVisibility(View.GONE);
                } else if (showCalcel == 1) {
                    mMyGroupCoursesLayout.setVisibility(View.VISIBLE);
                    mCancelOrderBtn.setVisibility(View.VISIBLE);
                }
            } else if (isFree == TYPE_NOT_FREE) {//收费
                mCoursesMoneyTextView.setVisibility(View.VISIBLE);
                mMyGroupCoursesLayout.setVisibility(View.VISIBLE);
                mCoursesMoneyTextView.setText("¥ " + object.getAmount());
                showCancelBtn(showCalcel);
            }

            if(scheduleType == TYPE_SCHEDULE_TYPE_SELF) {//如果是自主排课
                if (showCalcel == 0) {
                    mSelfShareBtn.setVisibility(View.GONE);
                } else if (showCalcel == 1) {
                    mSelfShareBtn.setVisibility(View.VISIBLE);
                }
            }else {
                mSelfShareBtn.setVisibility(View.GONE);
            }


        }

        private void showCancelBtn(int showCalcel) {
            if (showCalcel == 0) {
                mCancelOrderBtn.setVisibility(View.INVISIBLE);
            } else if (showCalcel == 1) {
                mCancelOrderBtn.setVisibility(View.VISIBLE);
            }
        }

        private void setCoursesState(int state) {
            if (state == COURSES_STATE_NOT_START) {//未开始
                mGroupCoursesStateTextView.setText(R.string.not_start);
                mGroupCoursesStateTextView.setTextColor(ResourceUtils.getColor(R.color.my_group_green_text));
                mGroupCoursesNameTextView.setTextColor(ResourceUtils.getColor(R.color.lesson_details_dark_back));
            } else if (state == COURSES_STATE_PROCESS) {//进行中
                mGroupCoursesStateTextView.setText(R.string.start_process);
                mGroupCoursesStateTextView.setTextColor(ResourceUtils.getColor(R.color.my_group_green_text));
                mGroupCoursesNameTextView.setTextColor(ResourceUtils.getColor(R.color.lesson_details_gray_back));
            } else if (state == COURSES_STATE_OVER) {//已结束
                mGroupCoursesStateTextView.setText(R.string.courses_complete);
                mGroupCoursesStateTextView.setTextColor(ResourceUtils.getColor(R.color.my_group_green_text));
                mGroupCoursesNameTextView.setTextColor(ResourceUtils.getColor(R.color.lesson_details_gray_back));
            } else if (state == COURSES_STATE_CANCEL) {//已取消
                mGroupCoursesStateTextView.setText(R.string.courses_cancel);
                mGroupCoursesStateTextView.setTextColor(ResourceUtils.getColor(R.color.lesson_details_gray_back));
                mGroupCoursesNameTextView.setTextColor(ResourceUtils.getColor(R.color.lesson_details_gray_back));
            }
        }

    }
}
