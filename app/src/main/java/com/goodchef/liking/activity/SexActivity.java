package com.goodchef.liking.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aaron.android.codelibrary.imageloader.ImageLoader;
import com.aaron.common.utils.StringUtils;
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

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 说明:首次登陆选择性别
 * Author shaozucheng
 * Time:16/8/15 下午3:34
 */
public class SexActivity extends AppBarActivity {
    public static final String KEY_SEX = "KEY_SEX";
    @BindView(R.id.sex_state_view)
    LikingStateView mSexStateView;
    @BindView(R.id.head_image)
    HImageView mHImageView;
    @BindView(R.id.user_name_text)
    TextView mUserNameTextView;
    @BindView(R.id.sex_man_image)
    TextView mSexManImage;
    @BindView(R.id.sex_man_text)
    TextView mSexManTextView;
    @BindView(R.id.layout_sex_man)
    LinearLayout mSexManLayout;
    @BindView(R.id.sex_women_image)
    TextView mSexWomenImage;
    @BindView(R.id.sex_women_text)
    TextView mSexWomenTextView;
    @BindView(R.id.layout_sex_women)
    LinearLayout mSexWomenLayout;
    @BindView(R.id.sex_next_btn)
    TextView mNextButton;

    private String userName;
    private String mLocalHeadImageUrl;
    private int sex = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sex);
        ButterKnife.bind(this);
        initData();
        setTitle(getString(R.string.activity_title_sex));
        getIntentData();
    }


    private void initData() {
        if (EnvironmentUtils.Network.isNetWorkAvailable()) {
            mSexStateView.setState(StateView.State.SUCCESS);
        } else {
            mSexStateView.setState(StateView.State.FAILED);
        }
        mSexStateView.setOnRetryRequestListener(new StateView.OnRetryRequestListener() {
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

    @OnClick({R.id.layout_sex_man, R.id.layout_sex_women, R.id.sex_next_btn})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.layout_sex_man:
                setSexManCheck();
                break;
            case R.id.layout_sex_women:
                setWomenCheck();
                break;
            case R.id.sex_next_btn:
                if (sex == -1) {
                    PopupUtils.showToast(getString(R.string.select_gender));
                    return;
                }
                Intent intent = new Intent(this, SelectBirthdayActivity.class);
                intent.putExtra(WriteNameActivity.KEY_USER_NAME, userName);
                intent.putExtra(UserHeadImageActivity.KEY_HEAD_IMAGE, mLocalHeadImageUrl);
                intent.putExtra(KEY_SEX, sex);
                startActivity(intent);
                break;
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
        finish();
    }
}
