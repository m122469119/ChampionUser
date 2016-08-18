package com.goodchef.liking.activity;

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
 * 说明:选择体重
 * Author shaozucheng
 * Time:16/8/16 下午4:17
 */
public class SelectWeightActivity extends AppBarActivity {
    private LikingStateView mStateView;
    private HImageView mHImageView;
    private TextView mUserNameTextView;
    private TextView mBirthdayTextView;
    private TextView mHeightTextView;
    private ImageView mSexManImage;
    private ImageView mSexWomenImage;

    private String userName;
    private String mLocalHeadImageUrl;
    private int sex = 1;
    private String mBirthdayStr;

    private RulerView mRulerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_weight);
        setTitle("选择体重");
        initView();
        initData();
    }

    private void initView() {
        mStateView = (LikingStateView)findViewById(R.id.select_weight_state_view);
        mHImageView = (HImageView) findViewById(R.id.head_image);
        mUserNameTextView = (TextView) findViewById(R.id.user_name_text);
        mSexManImage = (ImageView) findViewById(R.id.sex_man_image);
        mSexWomenImage = (ImageView) findViewById(R.id.sex_women_image);
        mBirthdayTextView = (TextView) findViewById(R.id.birthday_text);
        mHeightTextView = (TextView) findViewById(R.id.height_text);
        mRulerView = (RulerView) findViewById(R.id.weight_ruler_view);
    }

    private void initData(){
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

        userName = getIntent().getStringExtra(WriteNameActivity.KEY_USER_NAME);
        mLocalHeadImageUrl = getIntent().getStringExtra(UserHeadImageActivity.KEY_HEAD_IMAGE);
        sex = getIntent().getIntExtra(SexActivity.KEY_SEX, 1);
        mBirthdayStr = getIntent().getStringExtra(SelectBirthdayActivity.KEY_BIRTHDAY);

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

        mBirthdayTextView.setText(mBirthdayStr);

    }


}
