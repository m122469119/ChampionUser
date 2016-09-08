package com.goodchef.liking.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aaron.android.codelibrary.imageloader.ImageLoader;
import com.aaron.android.codelibrary.utils.StringUtils;
import com.aaron.android.framework.base.ui.actionbar.AppBarActivity;
import com.aaron.android.framework.base.widget.refresh.StateView;
import com.aaron.android.framework.library.imageloader.HImageConfigBuilder;
import com.aaron.android.framework.library.imageloader.HImageLoaderSingleton;
import com.aaron.android.framework.library.imageloader.HImageView;
import com.aaron.android.framework.utils.EnvironmentUtils;
import com.aaron.android.framework.utils.PopupUtils;
import com.aaron.android.framework.utils.ResourceUtils;
import com.goodchef.liking.R;
import com.goodchef.liking.eventmessages.UpDateUserInfoMessage;
import com.goodchef.liking.widgets.base.LikingStateView;

/**
 * 说明:首次登陆选择性别
 * Author shaozucheng
 * Time:16/8/15 下午3:34
 */
public class SexActivity extends AppBarActivity implements View.OnClickListener {
    public static final String KEY_SEX = "KEY_SEX";
    private LikingStateView mStateView;
    private HImageView mHImageView;
    private TextView mUserNameTextView;
    private LinearLayout mSexManLayout;
    private LinearLayout mSexWomenLayout;
    private TextView mSexManImage;
    private TextView mSexWomenImage;
    private TextView mSexManTextView;
    private TextView mSexWomenTextView;
    private TextView mNextButton;

    private String userName;
    private String mLocalHeadImageUrl;
    private int sex = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sex);
        initView();
        initData();
        setViewOnClickListener();
        setTitle(getString(R.string.activity_title_sex));
        getIntentData();
    }

    private void initView() {
        mStateView = (LikingStateView) findViewById(R.id.sex_state_view);
        mHImageView = (HImageView) findViewById(R.id.head_image);
        mUserNameTextView = (TextView) findViewById(R.id.user_name_text);
        mSexManLayout = (LinearLayout) findViewById(R.id.layout_sex_man);
        mSexWomenLayout = (LinearLayout) findViewById(R.id.layout_sex_women);
        mSexManImage = (TextView) findViewById(R.id.sex_man_image);
        mSexWomenImage = (TextView) findViewById(R.id.sex_women_image);
        mSexManTextView = (TextView) findViewById(R.id.sex_man_text);
        mSexWomenTextView = (TextView) findViewById(R.id.sex_women_text);
        mNextButton = (TextView) findViewById(R.id.sex_next_btn);
    }

    private void setViewOnClickListener() {
        mSexManLayout.setOnClickListener(this);
        mSexWomenLayout.setOnClickListener(this);
        mNextButton.setOnClickListener(this);
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

        mUserNameTextView.setText(userName);
        if (!StringUtils.isEmpty(mLocalHeadImageUrl)) {
            HImageLoaderSingleton.getInstance().loadImage(new HImageConfigBuilder(mHImageView, mLocalHeadImageUrl)
                    .resize(100, 100)
                    .setLoadType(ImageLoader.LoaderType.FILE)
                    .build());
        }

    }

    @Override
    public void onClick(View v) {
        if (v == mSexManLayout) {
            setSexManCheck();
        } else if (v == mSexWomenLayout) {
            setWomenCheck();
        } else if (v == mNextButton) {
            if (sex == -1) {
                PopupUtils.showToast("请选择性别");
                return;
            }
            Intent intent = new Intent(this, SelectBirthdayActivity.class);
            intent.putExtra(WriteNameActivity.KEY_USER_NAME, userName);
            intent.putExtra(UserHeadImageActivity.KEY_HEAD_IMAGE, mLocalHeadImageUrl);
            intent.putExtra(KEY_SEX, sex);
            startActivity(intent);
        }
    }

    private void setSexManCheck() {
        mSexManImage.setBackground(ResourceUtils.getDrawable(R.drawable.check_select_man));
        mSexManTextView.setTextColor(ResourceUtils.getColor(R.color.add_minus_dishes_text));
        mSexWomenImage.setBackground(ResourceUtils.getDrawable(R.drawable.check_no_select_men));
        mSexWomenTextView.setTextColor(ResourceUtils.getColor(R.color.white));
        sex = 1;
    }

    private void setWomenCheck() {
        mSexManImage.setBackground(ResourceUtils.getDrawable(R.drawable.check_no_select_man));
        mSexManTextView.setTextColor(ResourceUtils.getColor(R.color.white));
        mSexWomenImage.setBackground(ResourceUtils.getDrawable(R.drawable.check_select_men));
        mSexWomenTextView.setTextColor(ResourceUtils.getColor(R.color.sex_women));
        sex = 0;
    }

    @Override
    protected boolean isEventTarget() {
        return true;
    }

    public void onEvent(UpDateUserInfoMessage message) {
        this.finish();
    }
}
