package com.goodchef.liking.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aaron.common.utils.StringUtils;
import com.aaron.android.framework.base.ui.actionbar.AppBarActivity;
import com.aaron.android.framework.base.widget.recycleview.BaseRecycleViewAdapter;
import com.aaron.android.framework.base.widget.recycleview.BaseRecycleViewHolder;
import com.aaron.imageloader.code.HImageLoaderSingleton;
import com.aaron.imageloader.code.HImageView;
import com.aaron.android.framework.utils.DisplayUtils;
import com.goodchef.liking.R;
import com.goodchef.liking.eventmessages.OrderGroupMessageSuccess;
import com.goodchef.liking.fragment.LikingLessonFragment;
import com.goodchef.liking.http.result.GymCoursesResult;
import com.goodchef.liking.module.teaching.teamcourse.details.GroupLessonDetailsActivity;
import com.goodchef.liking.mvp.presenter.GymCoursesPresenter;
import com.goodchef.liking.mvp.view.GymCoursesView;
import com.goodchef.liking.storage.UmengEventId;
import com.goodchef.liking.utils.UMengCountUtil;

import java.util.List;

/**
 * 说明:查看团体课场馆列表
 * Author shaozucheng
 * Time:16/7/3 下午8:32
 */
public class GymCoursesActivity extends AppBarActivity implements GymCoursesView, View.OnClickListener {
    private LinearLayout mCoursesDataLayout;
    private RecyclerView mRecyclerView;
    private TextView mCheckGymBtn;
    private RelativeLayout mNoCoursesLayout;
    private GymCoursesAdapter mGymCoursesAdapter;

    private GymCoursesPresenter mGymCoursesPresenter;
    private String gymId;
    private String gymName;
    private String distance;
    private List<GymCoursesResult.GymCoursesData.GymDate> dateList;
    private boolean isCheck = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gym_courses);
        initView();
        initData();
    }

    private void initView() {
        mNoCoursesLayout = (RelativeLayout) findViewById(R.id.layout_no_courses);
        mCoursesDataLayout = (LinearLayout) findViewById(R.id.gym_courses_date);
        mRecyclerView = (RecyclerView) findViewById(R.id.gym_courses_recyclerView);
        mCheckGymBtn = (TextView) findViewById(R.id.check_gym_courses_btn);

        mCheckGymBtn.setOnClickListener(this);
    }

    private void initData() {
        gymId = getIntent().getStringExtra(LikingLessonFragment.KEY_GYM_ID);
        gymName = getIntent().getStringExtra(LikingLessonFragment.KEY_GYM_NAME);
        distance = getIntent().getStringExtra(LikingLessonFragment.KEY_DISTANCE);
        setTitle(gymName);
        showRightMenu(distance);
        mGymCoursesPresenter = new GymCoursesPresenter(this, this);
        mGymCoursesPresenter.getGymCoursesList(gymId, "0");
    }

    @Override
    public void updateGymCoursesView(GymCoursesResult.GymCoursesData gymCoursesData) {
        dateList = gymCoursesData.getShowDayList();
        if (isCheck) {
            if (dateList != null && dateList.size() > 0) {
                setHeadDateView(dateList);
            }
        }
        List<GymCoursesResult.GymCoursesData.Courses> gymList = gymCoursesData.getCoursesList();
        setGymCoursesList(gymList);
    }

    private void setGymCoursesList(List<GymCoursesResult.GymCoursesData.Courses> gymList) {
        if (gymList != null && gymList.size() > 0) {
            mNoCoursesLayout.setVisibility(View.GONE);
            mRecyclerView.setVisibility(View.VISIBLE);
            mCheckGymBtn.setVisibility(View.VISIBLE);
            LinearLayoutManager mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
            mRecyclerView.setLayoutManager(mLayoutManager);
            mGymCoursesAdapter = new GymCoursesAdapter(this);
            mGymCoursesAdapter.setData(gymList);
            mRecyclerView.setAdapter(mGymCoursesAdapter);
        } else {
            mRecyclerView.setVisibility(View.GONE);
            mCheckGymBtn.setVisibility(View.VISIBLE);
            mNoCoursesLayout.setVisibility(View.VISIBLE);
        }
    }

    private void setHeadDateView(final List<GymCoursesResult.GymCoursesData.GymDate> dateList) {
        for (int i = 0; i < dateList.size(); i++) {
            View view = LayoutInflater.from(this).inflate(R.layout.item_gym_courses_head_date, mCoursesDataLayout, false);
            LinearLayout layout = (LinearLayout) view.findViewById(R.id.layout_gym_date);
            TextView friendDateTextView = (TextView) view.findViewById(R.id.gym_friend_date);
            TextView showDateTextView = (TextView) view.findViewById(R.id.gym_show_date);
            friendDateTextView.setText(dateList.get(i).getShow());
            showDateTextView.setText(dateList.get(i).getDate());
            layout.setTag(dateList.get(i));
            dateList.get(i).setIndex(i);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT);
            params.weight = 1;
            mCoursesDataLayout.addView(view, params);
            layout.setOnClickListener(dateClickListener);
        }
        mCoursesDataLayout.getChildAt(0).setSelected(true);
    }


    private View.OnClickListener dateClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            clearHeadViewSelectBack();
            GymCoursesResult.GymCoursesData.GymDate date = (GymCoursesResult.GymCoursesData.GymDate) v.getTag();
            if (date != null) {
                int index = date.getIndex();
                mCoursesDataLayout.getChildAt(index).setSelected(true);
                isCheck = false;
                mGymCoursesPresenter.getGymCoursesList(gymId, date.getFormat());
            }
        }
    };

    private void clearHeadViewSelectBack() {
        int count = mCoursesDataLayout.getChildCount();
        for (int i = 0; i < count; i++) {
            View childView = mCoursesDataLayout.getChildAt(i);
            childView.setSelected(false);
        }
    }

    @Override
    public void onClick(View v) {
        if (v == mCheckGymBtn) {
            UMengCountUtil.UmengCount(GymCoursesActivity.this,UmengEventId.ARENAACTIVITY);
            Intent intent = new Intent(this, ArenaActivity.class);
            intent.putExtra(LikingLessonFragment.KEY_GYM_ID, gymId);
            this.startActivity(intent);
            this.overridePendingTransition(R.anim.silde_bottom_in, R.anim.silde_bottom_out);
        }
    }

    @Override
    protected boolean isEventTarget() {
        return true;
    }

    public void onEvent(OrderGroupMessageSuccess orderGroupMessageSuccess){
        this.finish();
    }

    public class GymCoursesAdapter extends BaseRecycleViewAdapter<GymCoursesAdapter.GymCoursesViewHolder, GymCoursesResult.GymCoursesData.Courses> {

        private Context mContext;

        public GymCoursesAdapter(Context context) {
            super(context);
            this.mContext = context;
        }

        @Override
        protected GymCoursesViewHolder createViewHolder(ViewGroup parent) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_gym_courses, parent, false);
            return new GymCoursesViewHolder(view);
        }

        class GymCoursesViewHolder extends BaseRecycleViewHolder<GymCoursesResult.GymCoursesData.Courses> {
            private HImageView mHImageView;//底部图片
            private TextView mLessonNameTextView;//课程名称
            private TextView mLessonUseTextView;//课程用途
            private TextView mLessonTimeTextView;//课程时间
            private TextView mSurplusPersonTextView;//剩余名额
            private CardView mCardView;
            private RelativeLayout mLessonTypeLayout;//课程类型布局

            public GymCoursesViewHolder(View itemView) {
                super(itemView);
                mHImageView = (HImageView) itemView.findViewById(R.id.gym_lesson_image);
                mLessonNameTextView = (TextView) itemView.findViewById(R.id.gym_lesson_name);
                mLessonUseTextView = (TextView) itemView.findViewById(R.id.gym_lesson_use);
                mLessonTimeTextView = (TextView) itemView.findViewById(R.id.gym_lesson_time);
                mLessonTypeLayout = (RelativeLayout) itemView.findViewById(R.id.layout_gym_lesson_type);
                mSurplusPersonTextView = (TextView) itemView.findViewById(R.id.gym_surplus_person);
                mCardView = (CardView) itemView.findViewById(R.id.gym_cardView);
                setCardView();
            }

            private void setCardView() {
                if (mCardView != null && mLessonTypeLayout != null) {
                    if (Build.VERSION.SDK_INT < 21) {
                        mCardView.setCardElevation(0);
                        FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) mLessonTypeLayout.getLayoutParams();
                        lp.setMargins(DisplayUtils.dp2px(0), DisplayUtils.dp2px(12), DisplayUtils.dp2px(10), DisplayUtils.dp2px(0));
                        mLessonTypeLayout.setLayoutParams(lp);
                    } else {
                        mCardView.setCardElevation(10);
                        FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) mLessonTypeLayout.getLayoutParams();
                        lp.setMargins(DisplayUtils.dp2px(0), DisplayUtils.dp2px(12), DisplayUtils.dp2px(8), DisplayUtils.dp2px(0));
                        mLessonTypeLayout.setLayoutParams(lp);
                    }
                }
            }

            @Override
            public void bindViews(final GymCoursesResult.GymCoursesData.Courses object) {
                setCardView();
                List<String> imageList = object.getImgs();
                if (imageList != null && imageList.size() > 0) {
                    String imageUrl = imageList.get(0);
                    if (!StringUtils.isEmpty(imageUrl)) {
                        HImageLoaderSingleton.getInstance().loadImage(mHImageView, imageUrl);
                    }
                }
                mLessonNameTextView.setText(object.getCourseName());
                List<String> tagList = object.getTags();
                if (tagList != null && tagList.size() > 0) {
                    StringBuffer stringBuffer = new StringBuffer();
                    for (int i = 0; i < tagList.size(); i++) {
                        stringBuffer.append("#" + tagList.get(i) + " ");
                    }
                    mLessonUseTextView.setText(stringBuffer.toString());
                }
                mLessonTimeTextView.setText(object.getCourseDate());
                mSurplusPersonTextView.setText(object.getQuota());
                mCardView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        UMengCountUtil.UmengCount(GymCoursesActivity.this, UmengEventId.GROUPLESSONDETAILSACTIVITY);
                        Intent intent = new Intent(mContext, GroupLessonDetailsActivity.class);
                        intent.putExtra(LikingLessonFragment.KEY_SCHEDULE_ID, object.getScheduleId());
                        startActivity(intent);
                    }
                });
            }
        }
    }

}
