package com.goodchef.liking.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aaron.android.codelibrary.utils.StringUtils;
import com.aaron.android.framework.base.actionbar.AppBarActivity;
import com.aaron.android.framework.base.widget.recycleview.BaseRecycleViewAdapter;
import com.aaron.android.framework.base.widget.recycleview.BaseRecycleViewHolder;
import com.aaron.android.framework.library.imageloader.HImageLoaderSingleton;
import com.aaron.android.framework.library.imageloader.HImageView;
import com.goodchef.liking.R;
import com.goodchef.liking.fragment.LikingLessonFragment;
import com.goodchef.liking.http.result.GymCoursesResult;
import com.goodchef.liking.mvp.presenter.GymCoursesPresenter;
import com.goodchef.liking.mvp.view.GymCoursesView;

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
    private GymCoursesAdapter mGymCoursesAdapter;

    private GymCoursesPresenter mGymCoursesPresenter;
    private String gymId;
    private String gymName;
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
        mCoursesDataLayout = (LinearLayout) findViewById(R.id.gym_courses_date);
        mRecyclerView = (RecyclerView) findViewById(R.id.gym_courses_recyclerView);
        mCheckGymBtn = (TextView) findViewById(R.id.check_gym_courses_btn);

        mCheckGymBtn.setOnClickListener(this);
    }

    private void initData() {
        gymId = getIntent().getStringExtra(LikingLessonFragment.KEY_GYM_ID);
        gymName = getIntent().getStringExtra(LikingLessonFragment.KEY_GYM_NAME);
        setTitle(gymName);
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
            LinearLayoutManager mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
            mRecyclerView.setLayoutManager(mLayoutManager);
            mGymCoursesAdapter = new GymCoursesAdapter(this);
            mGymCoursesAdapter.setData(gymList);
            mRecyclerView.setAdapter(mGymCoursesAdapter);
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
            Intent intent = new Intent(this, ArenaActivity.class);
            intent.putExtra(LikingLessonFragment.KEY_GYM_ID, gymId);
            this.startActivity(intent);
            this.overridePendingTransition(R.anim.silde_bottom_in, R.anim.silde_bottom_out);
        }
    }

    public class GymCoursesAdapter extends BaseRecycleViewAdapter<GymCoursesAdapter.GymCoursesViewHolder, GymCoursesResult.GymCoursesData.Courses> {

        private Context mContext;

        public GymCoursesAdapter(Context context) {
            super(context);
            this.mContext = context;
        }

        @Override
        protected GymCoursesViewHolder createHeaderViewHolder() {
            return null;
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

            public GymCoursesViewHolder(View itemView) {
                super(itemView);
                mHImageView = (HImageView) itemView.findViewById(R.id.gym_lesson_image);
                mLessonNameTextView = (TextView) itemView.findViewById(R.id.gym_lesson_name);
                mLessonUseTextView = (TextView) itemView.findViewById(R.id.gym_lesson_use);
                mLessonTimeTextView = (TextView) itemView.findViewById(R.id.gym_lesson_time);
                mSurplusPersonTextView = (TextView) itemView.findViewById(R.id.gym_surplus_person);
                mCardView = (CardView) itemView.findViewById(R.id.gym_cardView);
            }

            @Override
            public void bindViews(final GymCoursesResult.GymCoursesData.Courses object) {
                List<String> imageList = object.getImgs();
                if (imageList != null && imageList.size() > 0) {
                    String imageUrl = imageList.get(0);
                    if (!StringUtils.isEmpty(imageUrl)) {
                        HImageLoaderSingleton.getInstance().requestImage(mHImageView, imageUrl);
                    }
                }
                mLessonNameTextView.setText(object.getCourseName());
                List<String> tagList = object.getTags();
                if (tagList != null && tagList.size() > 0) {
                    StringBuffer stringBuffer = new StringBuffer();
                    for (int i = 0; i < tagList.size(); i++) {
                        stringBuffer.append(" #" + tagList.get(i));
                    }
                    mLessonUseTextView.setText(stringBuffer.toString());
                }
                mLessonTimeTextView.setText(object.getCourseDate());
                mSurplusPersonTextView.setText("剩余名额：" + object.getQuota());
                mCardView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(mContext, GroupLessonDetailsActivity.class);
                        intent.putExtra(LikingLessonFragment.KEY_SCHEDULE_ID, object.getScheduleId());
                        startActivity(intent);
                    }
                });
            }
        }
    }

}
