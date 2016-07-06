package com.goodchef.liking.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.aaron.android.framework.base.actionbar.AppBarActivity;
import com.aaron.android.framework.base.widget.recycleview.BaseRecycleViewAdapter;
import com.aaron.android.framework.base.widget.recycleview.BaseRecycleViewHolder;
import com.aaron.android.framework.library.imageloader.HImageLoaderSingleton;
import com.aaron.android.framework.library.imageloader.HImageView;
import com.aaron.android.framework.utils.PopupUtils;
import com.goodchef.liking.R;
import com.goodchef.liking.fragment.LikingLessonFragment;
import com.goodchef.liking.http.result.PrivateCoursesResult;
import com.goodchef.liking.mvp.presenter.PrivateCoursesDetailsPresenter;
import com.goodchef.liking.mvp.view.PrivateCoursesDetailsView;
import com.goodchef.liking.storage.Preference;

import java.util.List;

/**
 * 说明:私教课详情
 * Author shaozucheng
 * Time:16/5/24 下午5:55
 */
public class PrivateLessonDetailsActivity extends AppBarActivity implements PrivateCoursesDetailsView, View.OnClickListener {
    private HImageView mTeacherHImageView;
    // private RecyclerView mRecyclerView;
    private TextView mTeacherTagsTextView;
    private TextView mTeacherIntroduceTextView;
    //  private TextView mTrainPlanTextView;
    private TextView mImmediatelySubmitBtn;

    private PrivateCoursesDetailsPresenter mCoursesDetailsPresenter;
    private String trainerId;
    private String teacherName;

    //  private PrivateCoursesDetailsAdapter mPrivateCoursesDetailsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_private_lesson_details);
        initData();
    }

    private void initData() {
        trainerId = getIntent().getStringExtra(LikingLessonFragment.KEY_TRAINER_ID);
        teacherName = getIntent().getStringExtra(LikingLessonFragment.KEY_TEACHER_NAME);
        setTitle(teacherName);
        setRightMenu();
        initView();
        sendDetailsRequest();
    }

    private void setRightMenu() {
        setRightIcon(R.drawable.icon_phone, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupUtils.showToast("开发中。。。");
            }
        });
    }

    private void initView() {
        mTeacherHImageView = (HImageView) findViewById(R.id.private_lesson_details_teach_image);
        mTeacherTagsTextView = (TextView) findViewById(R.id.teacher_tags);
        //  mRecyclerView = (RecyclerView) findViewById(R.id.private_lesson_listView);
        mTeacherIntroduceTextView = (TextView) findViewById(R.id.teacher_introduce);
        //   mTrainPlanTextView = (TextView) findViewById(R.id.train_plan);
        mImmediatelySubmitBtn = (TextView) findViewById(R.id.private_lesson_immediately_submit);

        mImmediatelySubmitBtn.setOnClickListener(this);
    }

    private void sendDetailsRequest() {
        mCoursesDetailsPresenter = new PrivateCoursesDetailsPresenter(this, this);
        mCoursesDetailsPresenter.getPrivateCoursesDetails(trainerId);
    }


    @Override
    public void updatePrivateCoursesDetailsView(PrivateCoursesResult.PrivateCoursesData privateCoursesData) {
        setTitle(privateCoursesData.getTrainerName());
        List<String> imageList = privateCoursesData.getTrainerImgs();
        if (imageList != null) {
            String imageUrl = imageList.get(0);
            if (!TextUtils.isEmpty(imageUrl)) {
                HImageLoaderSingleton.getInstance().requestImage(mTeacherHImageView, imageUrl);
            }
        }

        StringBuffer stringBuffer = new StringBuffer();
        List<String> tags = privateCoursesData.getTags();
        if (tags != null && tags.size() > 0) {
            for (int i = 0; i < tags.size(); i++) {
                stringBuffer.append("#" + tags.get(i) + "  ");
            }
        }
        mTeacherTagsTextView.setText(stringBuffer.toString());
        mTeacherIntroduceTextView.setText(privateCoursesData.getDesc());
        //    mTrainPlanTextView.setText(privateCoursesData.getPlan());
        // setListViewData(privateCoursesData.getPlanImgs());
    }

//    private void setListViewData(List<PrivateCoursesResult.PrivateCoursesData.PlanImageData> imageList) {
//        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
//        mRecyclerView.setLayoutManager(mLayoutManager);
//        mPrivateCoursesDetailsAdapter = new PrivateCoursesDetailsAdapter(this);
//        mPrivateCoursesDetailsAdapter.setData(imageList);
//        mRecyclerView.setAdapter(mPrivateCoursesDetailsAdapter);
//    }

    @Override
    public void onClick(View v) {
        if (v == mImmediatelySubmitBtn) {
            if (Preference.isLogin()) {
                Intent intent = new Intent(this, OrderPrivateCoursesConfirmActivity.class);
                intent.putExtra(LikingLessonFragment.KEY_TRAINER_ID, trainerId);
                intent.putExtra(LikingLessonFragment.KEY_TEACHER_NAME, teacherName);
                startActivity(intent);
            } else {
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
            }
        }
    }


    public class PrivateCoursesDetailsAdapter extends BaseRecycleViewAdapter<PrivateCoursesDetailsAdapter.GroupLessonViewHolder, PrivateCoursesResult.PrivateCoursesData.PlanImageData> {

        private Context mContext;

        public PrivateCoursesDetailsAdapter(Context context) {
            super(context);
            this.mContext = context;
        }

        @Override
        protected GroupLessonViewHolder createHeaderViewHolder() {
            return null;
        }

        @Override
        protected GroupLessonViewHolder createViewHolder(ViewGroup parent) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_private_lesson, parent, false);
            return new GroupLessonViewHolder(view);
        }


        public class GroupLessonViewHolder extends BaseRecycleViewHolder<PrivateCoursesResult.PrivateCoursesData.PlanImageData> {
            TextView mTitleTextView;
            HImageView mHImageView;

            public GroupLessonViewHolder(View itemView) {
                super(itemView);
                mHImageView = (HImageView) itemView.findViewById(R.id.private_lesson_details_hImageView);
                mTitleTextView = (TextView) itemView.findViewById(R.id.private_lesson_title);
            }

            @Override
            public void bindViews(PrivateCoursesResult.PrivateCoursesData.PlanImageData object) {
                String imageUrl = object.getUrl();
                if (!TextUtils.isEmpty(imageUrl)) {
                    HImageLoaderSingleton.getInstance().requestImage(mHImageView, imageUrl);
                }
                mTitleTextView.setText(object.getDesc());
            }
        }
    }

}
