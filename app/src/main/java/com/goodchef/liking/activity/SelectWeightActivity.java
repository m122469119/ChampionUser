package com.goodchef.liking.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.aaron.android.codelibrary.imageloader.ImageLoader;
import com.aaron.android.codelibrary.utils.LogUtils;
import com.aaron.android.codelibrary.utils.StringUtils;
import com.aaron.android.framework.base.ui.actionbar.AppBarActivity;
import com.aaron.android.framework.base.widget.refresh.StateView;
import com.aaron.android.framework.library.imageloader.HImageConfigBuilder;
import com.aaron.android.framework.library.imageloader.HImageLoaderSingleton;
import com.aaron.android.framework.library.imageloader.HImageView;
import com.aaron.android.framework.utils.EnvironmentUtils;
import com.goodchef.liking.R;
import com.goodchef.liking.eventmessages.UpDateUserInfoMessage;
import com.goodchef.liking.widgets.RulerView;
import com.goodchef.liking.widgets.base.LikingStateView;

import java.util.List;

/**
 * 说明:选择体重
 * Author shaozucheng
 * Time:16/8/16 下午4:17
 */
public class SelectWeightActivity extends AppBarActivity implements View.OnClickListener {
    public static final String KEY_WEIGHT = "key_weight";
    private LikingStateView mStateView;
    private HImageView mHImageView;
    private TextView mUserNameTextView;
    private TextView mBirthdayTextView;
    private TextView mHeightTextView;
    private ImageView mSexManImage;
    private ImageView mSexWomenImage;
    private TextView mWeightTextView;
    private RulerView mWeightRulerView;
    private TextView mNextBtn;

    private String userName;
    private String mLocalHeadImageUrl;
    private int sex;
    private String mBirthdayStr;
    private String mBirthdayStrFormat;
    private int height;
    private int mScale;

    private List<String> weightList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_weight);
        setTitle(getString(R.string.activity_title_weight));
        initView();
        initData();
        getIntentData();
    }

    private void initView() {
        mStateView = (LikingStateView) findViewById(R.id.select_weight_state_view);
        mHImageView = (HImageView) findViewById(R.id.select_weight_head_image);
        mUserNameTextView = (TextView) findViewById(R.id.user_name_text);
        mSexManImage = (ImageView) findViewById(R.id.sex_man_image);
        mSexWomenImage = (ImageView) findViewById(R.id.sex_women_image);
        mBirthdayTextView = (TextView) findViewById(R.id.birthday_text);
        mHeightTextView = (TextView) findViewById(R.id.height_text);
        mWeightTextView = (TextView) findViewById(R.id.weight_text);
        mWeightRulerView = (RulerView) findViewById(R.id.weight_ruler_view);
        mNextBtn = (TextView) findViewById(R.id.select_weight_next_btn);

        mNextBtn.setOnClickListener(this);
        setRulerView();
    }

    private void initData() {
        if (EnvironmentUtils.Network.isNetWorkAvailable()) {
            mStateView.setState(StateView.State.SUCCESS);
        } else {
            mStateView.setState(StateView.State.FAILED);
        }
        mStateView.setOnRetryRequestListener(new StateView.OnRetryRequestListener() {
            @Override
            public void onRetryRequested() {
                initData();
            }
        });
    }

    private void getIntentData() {
        userName = getIntent().getStringExtra(WriteNameActivity.KEY_USER_NAME);
        mLocalHeadImageUrl = getIntent().getStringExtra(UserHeadImageActivity.KEY_HEAD_IMAGE);
        sex = getIntent().getIntExtra(SexActivity.KEY_SEX, 0);
        mBirthdayStr = getIntent().getStringExtra(SelectBirthdayActivity.KEY_BIRTHDAY);
        mBirthdayStrFormat = getIntent().getStringExtra(SelectBirthdayActivity.KEY_BIRTHDAY_FORMAT);
        height = getIntent().getIntExtra(SelectHeightActivity.KEY_HEIGHT, 0);

        mUserNameTextView.setText(userName);
        if (!StringUtils.isEmpty(mLocalHeadImageUrl)) {
            LogUtils.i(TAG, mLocalHeadImageUrl);
            HImageLoaderSingleton.getInstance().loadImage(new HImageConfigBuilder(mHImageView, mLocalHeadImageUrl)
                    .resize(100, 100)
                    .setLoadType(ImageLoader.LoaderType.FILE)
                    .build());
        }
        if (sex == 1) {
            mSexManImage.setVisibility(View.VISIBLE);
            mSexWomenImage.setVisibility(View.GONE);
            mWeightRulerView.smoothScrollTo(67);
        } else if (sex == 0) {
            mSexManImage.setVisibility(View.GONE);
            mSexWomenImage.setVisibility(View.VISIBLE);
            mWeightRulerView.smoothScrollTo(57);
        }
        mBirthdayTextView.setText(R.string.birthday + mBirthdayStr);
        mHeightTextView.setText(getString(R.string.select_weight) + height + " cm");
    }

    private void setRulerView() {
        weightList = mWeightRulerView.getWeightList();
        mWeightRulerView.setOnScaleListener(new RulerView.OnScaleListener() {
            @Override
            public void onScaleChanged(int scale) {
                mScale = scale;
                mWeightTextView.setText(weightList.get(scale) + " kg");
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (v == mNextBtn) {
            Intent intent = new Intent(this, CompleteUserInfoActivity.class);
            intent.putExtra(WriteNameActivity.KEY_USER_NAME, userName);
            intent.putExtra(UserHeadImageActivity.KEY_HEAD_IMAGE, mLocalHeadImageUrl);
            intent.putExtra(SexActivity.KEY_SEX, sex);
            intent.putExtra(SelectBirthdayActivity.KEY_BIRTHDAY, mBirthdayStr);
            intent.putExtra(SelectBirthdayActivity.KEY_BIRTHDAY_FORMAT, mBirthdayStrFormat);
            intent.putExtra(SelectHeightActivity.KEY_HEIGHT, height);
            intent.putExtra(KEY_WEIGHT, weightList.get(mScale));
            startActivity(intent);
        }
    }


    @Override
    protected boolean isEventTarget() {
        return true;
    }

    public void onEvent(UpDateUserInfoMessage message) {
        this.finish();
    }
}
