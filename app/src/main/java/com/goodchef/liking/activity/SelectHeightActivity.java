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

/**
 * 说明:首次登陆选择身高
 * Author shaozucheng
 * Time:16/8/19 上午10:12
 */
public class SelectHeightActivity extends AppBarActivity implements View.OnClickListener {

    public static final String KEY_HEIGHT = "key_height";
    private LikingStateView mStateView;
    private HImageView mHImageView;
    private TextView mUserNameTextView;
    private ImageView mSexManImage;
    private ImageView mSexWomenImage;
    private TextView mBirthdayTextView;
    private TextView mHeightTextView;
    private RulerView mHeightRulerView;
    private TextView mNextBtn;

    private String userName;
    private String mLocalHeadImageUrl;
    private int sex;
    private String mBirthdayStr;
    private String mBirthdayStrFormat;
    private int height;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_height);
        setTitle(getString(R.string.title_activity_height));
        initView();
        initData();
        getIntentData();
    }

    private void initView() {
        mStateView = (LikingStateView) findViewById(R.id.select_height_state_view);
        mHImageView = (HImageView) findViewById(R.id.select_height_head_image);
        mUserNameTextView = (TextView) findViewById(R.id.user_name_text);
        mSexManImage = (ImageView) findViewById(R.id.sex_man_image);
        mSexWomenImage = (ImageView) findViewById(R.id.sex_women_image);
        mBirthdayTextView = (TextView) findViewById(R.id.birthday_text);
        mHeightTextView = (TextView) findViewById(R.id.height_text);
        mHeightRulerView = (RulerView) findViewById(R.id.height_ruler_view);
        mNextBtn = (TextView) findViewById(R.id.select_height_next_btn);

        mNextBtn.setOnClickListener(this);
        setHeightRuler();
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
        sex = getIntent().getIntExtra(SexActivity.KEY_SEX, 1);
        mBirthdayStr = getIntent().getStringExtra(SelectBirthdayActivity.KEY_BIRTHDAY);
        mBirthdayStrFormat = getIntent().getStringExtra(SelectBirthdayActivity.KEY_BIRTHDAY_FORMAT);
        mUserNameTextView.setText(userName);

        if (!StringUtils.isEmpty(mLocalHeadImageUrl)) {
            LogUtils.i(TAG,mLocalHeadImageUrl);
            HImageLoaderSingleton.getInstance().loadImage(new HImageConfigBuilder(mHImageView,mLocalHeadImageUrl)
                    .resize(100, 100)
                    .setLoadType(ImageLoader.LoaderType.FILE)
                    .build());
        }
        if (sex == 1) {
            mSexManImage.setVisibility(View.VISIBLE);
            mSexWomenImage.setVisibility(View.GONE);
            mHeightRulerView.smoothScrollTo(117);
        } else if (sex == 0) {
            mHeightRulerView.smoothScrollTo(112);
            mSexManImage.setVisibility(View.GONE);
            mSexWomenImage.setVisibility(View.VISIBLE);
        }
        mBirthdayTextView.setText(getString(R.string.birthday) + mBirthdayStr);

    }

    private void setHeightRuler() {
        mHeightRulerView.setOnScaleListener(new RulerView.OnScaleListener() {
            @Override
            public void onScaleChanged(int scale) {
                height = scale;
                mHeightTextView.setText(height + getString(R.string.cm));
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (v == mNextBtn) {
            Intent intent = new Intent(this, SelectWeightActivity.class);
            intent.putExtra(WriteNameActivity.KEY_USER_NAME, userName);
            intent.putExtra(UserHeadImageActivity.KEY_HEAD_IMAGE, mLocalHeadImageUrl);
            intent.putExtra(SexActivity.KEY_SEX, sex);
            intent.putExtra(SelectBirthdayActivity.KEY_BIRTHDAY, mBirthdayStr);
            intent.putExtra(SelectBirthdayActivity.KEY_BIRTHDAY_FORMAT, mBirthdayStrFormat);
            intent.putExtra(KEY_HEIGHT,height);
            startActivity(intent);
        }
    }

    @Override
    protected boolean isEventTarget() {
        return true;
    }

    public void onEvent(UpDateUserInfoMessage message){
        finish();
    }
}
