package com.goodchef.liking.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.aaron.android.codelibrary.utils.StringUtils;
import com.aaron.android.framework.base.ui.actionbar.AppBarActivity;
import com.aaron.android.framework.base.widget.refresh.StateView;
import com.aaron.android.framework.library.imageloader.HImageView;
import com.aaron.android.framework.utils.EnvironmentUtils;
import com.goodchef.liking.R;
import com.goodchef.liking.widgets.RulerView;
import com.goodchef.liking.widgets.base.LikingStateView;

/**
 * 说明:
 * Author shaozucheng
 * Time:16/8/16 下午3:25
 */
public class SelectBirthdayActivity extends AppBarActivity implements View.OnClickListener {
    public static final String KEY_BIRTHDAY = "key_birthday";
    private LikingStateView mStateView;
    private HImageView mHImageView;
    private TextView mUserNameTextView;
    private ImageView mSexManImage;
    private ImageView mSexWomenImage;
    private TextView mBirthdayTextView;
    private TextView mNextBtn;

    private String userName;
    private String mLocalHeadImageUrl;
    private int sex;

    private RulerView mYearRulerView;
    private RulerView mMontyRulerView;
    private RulerView mDayRulerView;

    private int year = 1950;
    private int month = 1;
    private int day = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_birthday);
        setTitle("选择出生日期");
        iniView();
        initData();
    }

    private void iniView() {
        mStateView = (LikingStateView) findViewById(R.id.select_birthady_state_view);
        mUserNameTextView = (TextView) findViewById(R.id.user_name_text);
        mBirthdayTextView = (TextView) findViewById(R.id.birthday_text);
        mHImageView = (HImageView) findViewById(R.id.head_image);
        mSexManImage = (ImageView) findViewById(R.id.sex_man_image);
        mSexWomenImage = (ImageView) findViewById(R.id.sex_women_image);
        mNextBtn = (TextView) findViewById(R.id.select_birthday_next_btn);

        mYearRulerView = (RulerView) findViewById(R.id.year_ruler_view);
        mMontyRulerView = (RulerView) findViewById(R.id.month_ruler_view);
        mDayRulerView = (RulerView) findViewById(R.id.day_ruler_view);

        mNextBtn.setOnClickListener(this);

        setRuleView();
    }

    private void initData() {
        if (EnvironmentUtils.Network.isNetWorkAvailable()) {
            mStateView.setState(StateView.State.SUCCESS);
        } else {
            mStateView.setState(StateView.State.FAILED);
        }

        userName = getIntent().getStringExtra(WriteNameActivity.KEY_USER_NAME);
        mLocalHeadImageUrl = getIntent().getStringExtra(UserHeadImageActivity.KEY_HEAD_IMAGE);
        sex = getIntent().getIntExtra(SexActivity.KEY_SEX, 1);
        mUserNameTextView.setText(userName);
        if (!StringUtils.isEmpty(mLocalHeadImageUrl)) {
            mHImageView.setImageURI(Uri.parse(mLocalHeadImageUrl));
        }

        if (sex == 1) {
            mSexManImage.setVisibility(View.VISIBLE);
            mSexWomenImage.setVisibility(View.GONE);
        } else if (sex == 2) {
            mSexManImage.setVisibility(View.GONE);
            mSexWomenImage.setVisibility(View.VISIBLE);
        }


        mStateView.setOnRetryRequestListener(new StateView.OnRetryRequestListener() {
            @Override
            public void onRetryRequested() {
                initData();
            }
        });
    }

    private void setRuleView() {
        mYearRulerView.smoothScrollTo(32);
        mMontyRulerView.smoothScrollTo(2);
        mDayRulerView.smoothScrollTo(15);

        mYearRulerView.setOnScaleListener(new RulerView.OnScaleListener() {
            @Override
            public void onScaleChanged(int scale) {
                year = scale;
                mBirthdayTextView.setText(year + "年" + month + "月" + day + "日");
            }
        });

        mMontyRulerView.setOnScaleListener(new RulerView.OnScaleListener() {
            @Override
            public void onScaleChanged(int scale) {
                month = scale;
                mBirthdayTextView.setText(year + "年" + month + "月" + day + "日");
            }
        });

        mDayRulerView.setOnScaleListener(new RulerView.OnScaleListener() {
            @Override
            public void onScaleChanged(int scale) {
                day = scale;
                mBirthdayTextView.setText(year + "年" + month + "月" + day + "日");
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
            intent.putExtra(KEY_BIRTHDAY,mBirthdayTextView.getText().toString().trim());
            startActivity(intent);
        }
    }
}
