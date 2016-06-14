package com.goodchef.liking.activity;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.aaron.android.framework.base.actionbar.AppBarActivity;
import com.aaron.android.framework.base.widget.listview.HBaseAdapter;
import com.aaron.android.framework.library.imageloader.HImageLoaderSingleton;
import com.aaron.android.framework.library.imageloader.HImageView;
import com.aaron.android.framework.utils.PopupUtils;
import com.goodchef.liking.R;
import com.goodchef.liking.fragment.LikingLessonFragment;
import com.goodchef.liking.http.result.GroupCoursesResult;
import com.goodchef.liking.http.result.PrivateCoursesResult;
import com.goodchef.liking.mvp.presenter.CoursesDetailsPresenter;
import com.goodchef.liking.mvp.view.CoursesDetailsView;
import com.goodchef.liking.utils.ListViewUtil;

import java.util.List;

/**
 * 说明:
 * Author shaozucheng
 * Time:16/5/24 下午5:55
 */
public class PrivateLessonDetailsActivity extends AppBarActivity implements CoursesDetailsView, View.OnClickListener {
    private HImageView mTeacherHImageView;
    private ListView mListView;
    private TextView mTeacherTagsTextView;
    private TextView mTeacherIntroduceTextView;
    private TextView mTrainPlanTextView;
    private TextView mImmediatelySubmitBtn;

    private CoursesDetailsPresenter mCoursesDetailsPresenter;
    private String trainerId;
    private String teacherName;

    private PrivateCoursesDetailsAdapter mPrivateCoursesDetailsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_private_lesson_details);
        initData();
    }

    private void initData() {
        trainerId = getIntent().getStringExtra(LikingLessonFragment.KEY_TRAINER_ID);
        teacherName = getIntent().getStringExtra(LikingLessonFragment.KEY_TEACHER_NAME);
        setRightIcon(R.drawable.icon_phone, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupUtils.showToast("开发中。。。");
            }
        });
        setTitle(teacherName);
        initView();
        sendDetailsRequest();
    }

    private void initView() {
        mTeacherHImageView = (HImageView) findViewById(R.id.private_lesson_details_teach_image);
        mTeacherTagsTextView = (TextView) findViewById(R.id.teacher_tags);
        mListView = (ListView) findViewById(R.id.private_lesson_listView);
        mTeacherIntroduceTextView = (TextView) findViewById(R.id.teacher_introduce);
        mTrainPlanTextView = (TextView) findViewById(R.id.train_plan);
        mImmediatelySubmitBtn = (TextView) findViewById(R.id.private_lesson_immediately_submit);

        mImmediatelySubmitBtn.setOnClickListener(this);
    }

    private void sendDetailsRequest() {
        mCoursesDetailsPresenter = new CoursesDetailsPresenter(this, this);
        mCoursesDetailsPresenter.getPrivateCouresDetails(trainerId);
    }

    @Override
    public void updateGroupLessonDetailsView(GroupCoursesResult.GroupLessonData groupLessonData) {

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
        for (int i = 0; i < tags.size(); i++) {
            stringBuffer.append("#" + tags.get(i) + "  ");
        }
        mTeacherTagsTextView.setText(stringBuffer.toString());
        mTeacherIntroduceTextView.setText(privateCoursesData.getDesc());
        mTrainPlanTextView.setText(privateCoursesData.getPlan());
        setListViewData(privateCoursesData.getPlanImgs());
    }

    private void setListViewData(List<String> imageList) {
        mPrivateCoursesDetailsAdapter = new PrivateCoursesDetailsAdapter(this);
        mPrivateCoursesDetailsAdapter.setData(imageList);
        mListView.setAdapter(mPrivateCoursesDetailsAdapter);
        ListViewUtil.setListViewHeightBasedOnChildren(mListView);
    }

    @Override
    public void onClick(View v) {
        if (v == mImmediatelySubmitBtn) {
            PopupUtils.showToast("开发中");
        }
    }


    public class PrivateCoursesDetailsAdapter extends HBaseAdapter<String> {

        private Context mContext;

        public PrivateCoursesDetailsAdapter(Context context) {
            super(context);
            this.mContext = context;
        }

        @Override
        protected BaseViewHolder<String> createViewHolder() {
            return new GroupLessonViewHolder();
        }

        class GroupLessonViewHolder extends BaseViewHolder<String> {
            View mRootView;
            TextView mTitleTextView;
            HImageView mHImageView;

            @Override
            public View inflateItemView() {
                mRootView = LayoutInflater.from(mContext).inflate(R.layout.item_private_lesson, null, false);
                mHImageView = (HImageView) mRootView.findViewById(R.id.private_lesson_details_hImageView);
                mTitleTextView = (TextView) mRootView.findViewById(R.id.private_lesson_title);
                return mRootView;
            }

            @Override
            public void bindViews(String object) {
                if (!TextUtils.isEmpty(object)) {
                    HImageLoaderSingleton.getInstance().requestImage(mHImageView, object);
                }
            }
        }
    }

}
