package com.goodchef.liking.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.aaron.android.codelibrary.imageloader.ImageLoader;
import com.aaron.common.utils.LogUtils;
import com.aaron.common.utils.StringUtils;
import com.aaron.android.framework.base.ui.actionbar.AppBarActivity;
import com.aaron.android.framework.base.widget.refresh.StateView;
import com.aaron.android.framework.library.imageloader.HImageConfigBuilder;
import com.aaron.android.framework.library.imageloader.HImageLoaderSingleton;
import com.aaron.android.framework.library.imageloader.HImageView;
import com.aaron.android.framework.utils.EnvironmentUtils;
import com.aaron.android.framework.utils.ResourceUtils;
import com.goodchef.liking.R;
import com.goodchef.liking.eventmessages.UpDateUserInfoMessage;
import com.goodchef.liking.utils.VerifyDateUtils;
import com.goodchef.liking.widgets.RulerView;
import com.goodchef.liking.widgets.base.LikingStateView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 说明:首次登陆选择出生日期
 * Author shaozucheng
 * Time:16/8/16 下午3:25
 */
public class SelectBirthdayActivity extends AppBarActivity  {
    public static final String KEY_BIRTHDAY = "key_birthday";
    public static final String KEY_BIRTHDAY_FORMAT = "key_birthday_format";
    @BindView(R.id.select_birthady_state_view)
    LikingStateView mSelectBirthadyStateView;
    @BindView(R.id.select_birthday_head_image)
    HImageView mHImageView;
    @BindView(R.id.user_name_text)
    TextView mUserNameTextView;
    @BindView(R.id.sex_man_image)
    ImageView mSexManImage;
    @BindView(R.id.sex_women_image)
    ImageView mSexWomenImage;
    @BindView(R.id.birthday_text)
    TextView mBirthdayTextView;
    @BindView(R.id.year_ruler_view)
    RulerView mYearRulerView;
    @BindView(R.id.month_ruler_view)
    RulerView mMonthRulerView;
    @BindView(R.id.day_ruler_view)
    RulerView mDayRulerView;
    @BindView(R.id.select_birthday_next_btn)
    TextView mNextBtn;

    private String userName;
    private String mLocalHeadImageUrl;
    private int sex;

    private int year = 1950;
    private int month = 1;
    private int day = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_birthday);
        ButterKnife.bind(this);
        setTitle(getString(R.string.activity_title_select_birthday));
        iniView();
        initData();
        getIntentData();
    }

    private void iniView() {
        setRuleView();
    }

    private void initData() {
        if (EnvironmentUtils.Network.isNetWorkAvailable()) {
            mSelectBirthadyStateView.setState(StateView.State.SUCCESS);
        } else {
            mSelectBirthadyStateView.setState(StateView.State.FAILED);
        }
        mSelectBirthadyStateView.setOnRetryRequestListener(new StateView.OnRetryRequestListener() {
            @Override
            public void onRetryRequested() {
                initData();
            }
        });
    }

    private void getIntentData() {
        userName = getIntent().getStringExtra(WriteNameActivity.KEY_USER_NAME);
        mLocalHeadImageUrl = getIntent().getStringExtra(UserHeadImageActivity.KEY_HEAD_IMAGE);
        sex = getIntent().getIntExtra(SexActivity.KEY_SEX, -1);

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
        } else if (sex == 0) {
            mSexManImage.setVisibility(View.GONE);
            mSexWomenImage.setVisibility(View.VISIBLE);
        }

    }

    private void setRuleView() {
        mYearRulerView.smoothScrollTo(37);
        mMonthRulerView.smoothScrollTo(2);
        mDayRulerView.smoothScrollTo(11);

        mYearRulerView.setOnScaleListener(new RulerView.OnScaleListener() {
            @Override
            public void onScaleChanged(int scale) {
                year = scale;
                mBirthdayTextView.setText(year + getString(R.string.year) + month + getString(R.string.month) + day + getString(R.string.day));
                checkDate();
            }
        });

        mMonthRulerView.setOnScaleListener(new RulerView.OnScaleListener() {
            @Override
            public void onScaleChanged(int scale) {
                month = scale;
                mBirthdayTextView.setText(year + getString(R.string.year) + month + getString(R.string.month) + day + getString(R.string.day));
                checkDate();
            }
        });

        mDayRulerView.setOnScaleListener(new RulerView.OnScaleListener() {
            @Override
            public void onScaleChanged(int scale) {
                day = scale;
                mBirthdayTextView.setText(year + getString(R.string.year) + month + getString(R.string.month) + day + getString(R.string.day));
                checkDate();
            }
        });
    }

    /**
     * 校验日期是否正确
     */
    private void checkDate() {
        if (VerifyDateUtils.isVerifyDate(year + "-" + month + "-" + day)) {
            mNextBtn.setBackgroundColor(ResourceUtils.getColor(R.color.add_minus_dishes_text));
            mNextBtn.setEnabled(true);
        } else {
            mNextBtn.setBackgroundColor(ResourceUtils.getColor(R.color.dishes_details_tags));
            mNextBtn.setEnabled(false);
        }
    }

    @OnClick({R.id.select_birthday_next_btn})
    public void onClick(View v) {
        if (v == mNextBtn) {
            doBirthdayData();
        }
    }

    /**
     * 处理出生日期
     */
    private void doBirthdayData() {
        String monthFormat;
        String dayFormat;
        if (month < 10) {
            monthFormat = "0" + month;
        } else {
            monthFormat = month + "";
        }
        if (day < 10) {
            dayFormat = "0" + day;
        } else {
            dayFormat = day + "";
        }
        Intent intent = new Intent(this, SelectHeightActivity.class);
        intent.putExtra(WriteNameActivity.KEY_USER_NAME, userName);
        intent.putExtra(UserHeadImageActivity.KEY_HEAD_IMAGE, mLocalHeadImageUrl);
        intent.putExtra(SexActivity.KEY_SEX, sex);
        intent.putExtra(KEY_BIRTHDAY, mBirthdayTextView.getText().toString().trim());
        intent.putExtra(KEY_BIRTHDAY_FORMAT, year + "-" + monthFormat + "-" + dayFormat);
        startActivity(intent);
    }


    @Override
    protected boolean isEventTarget() {
        return true;
    }

    public void onEvent(UpDateUserInfoMessage message) {
        finish();
    }


}
