package com.goodchef.liking.module.writeuserinfo;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.ImageView;
import android.widget.TextView;

import com.aaron.android.framework.base.mvp.BaseMVPActivity;
import com.aaron.android.framework.base.widget.refresh.StateView;
import com.aaron.android.framework.utils.EnvironmentUtils;
import com.aaron.common.utils.LogUtils;
import com.aaron.common.utils.StringUtils;
import com.aaron.imageloader.ImageLoader;
import com.aaron.imageloader.code.HImageConfigBuilder;
import com.goodchef.liking.utils.HImageLoaderSingleton;
import com.aaron.imageloader.code.HImageView;
import com.goodchef.liking.R;
import com.goodchef.liking.eventmessages.UpDateUserInfoMessage;
import com.goodchef.liking.data.remote.retrofit.result.UserImageResult;
import com.goodchef.liking.data.remote.retrofit.result.UserInfoResult;
import com.goodchef.liking.data.local.LikingPreference;
import com.goodchef.liking.utils.BitmapBase64Util;
import com.goodchef.liking.utils.ImageEnviromentUtil;
import com.goodchef.liking.widgets.base.LikingStateView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 说明:首次登陆完成个人信息界面
 * Author shaozucheng
 * Time:16/8/19 下午2:35
 */
public class CompleteUserInfoActivity extends BaseMVPActivity<CompleteUserInfoContract.Presenter> implements CompleteUserInfoContract.View {

    @BindView(R.id.complete_userInfo_state_view)
    LikingStateView mStateView;
    @BindView(R.id.complete_user_head_image)
    HImageView mHImageView;
    @BindView(R.id.user_name_text)
    TextView mUserNameTextView;
    @BindView(R.id.sex_man_image)
    ImageView mSexManImage;
    @BindView(R.id.sex_women_image)
    ImageView mSexWomenImage;
    @BindView(R.id.birthday_text)
    TextView mBirthdayTextView;
    @BindView(R.id.height_text)
    TextView mHeightTextView;
    @BindView(R.id.weight_text)
    TextView mWeightTextView;
    @BindView(R.id.complete_userInfo_btn)
    TextView mCompleteBtn;

    private String userName;
    private int sex = -1;
    private String mBirthdayStrFormat;
    private int height;
    private String weight;
    private String headUrl = "";

    private Bitmap mBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complete_user_info);
        ButterKnife.bind(this);
        setTitle(getString(R.string.activity_title_complete_userinfo));
        initData();
        getIntentData();
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
        String localHeadImageUrl = getIntent().getStringExtra(UserHeadImageActivity.KEY_HEAD_IMAGE);
        sex = getIntent().getIntExtra(SexActivity.KEY_SEX, 0);
        String birthdayStr = getIntent().getStringExtra(SelectBirthdayActivity.KEY_BIRTHDAY);
        mBirthdayStrFormat = getIntent().getStringExtra(SelectBirthdayActivity.KEY_BIRTHDAY_FORMAT);
        height = getIntent().getIntExtra(SelectHeightActivity.KEY_HEIGHT, 0);
        weight = getIntent().getStringExtra(SelectWeightActivity.KEY_WEIGHT);

        mUserNameTextView.setText(userName);
        if (!StringUtils.isEmpty(localHeadImageUrl)) {
            HImageLoaderSingleton.loadImage(new HImageConfigBuilder(mHImageView, localHeadImageUrl)
                    .resize(100, 100)
                    .setLoadType(ImageLoader.LoaderType.FILE)
                    .build(), this);
        }
        if (sex == 1) {
            mSexManImage.setVisibility(android.view.View.VISIBLE);
            mSexWomenImage.setVisibility(android.view.View.GONE);
        } else if (sex == 0) {
            mSexManImage.setVisibility(android.view.View.GONE);
            mSexWomenImage.setVisibility(android.view.View.VISIBLE);
        }
        mBirthdayTextView.setText(birthdayStr);
        mHeightTextView.setText(height + getString(R.string.cm));
        mWeightTextView.setText(weight + getString(R.string.kg));

        mBitmap = ImageEnviromentUtil.compressImageSize(localHeadImageUrl);
    }

    @OnClick({R.id.complete_userInfo_btn})
    public void onClick(android.view.View v) {
        if (v == mCompleteBtn) {
            sendImageFile(mBitmap);
        }
    }

    /**
     * 提交用户信息
     */
    private void sendUserInfo() {
        mPresenter.updateUserInfo(this, headUrl, sex, mBirthdayStrFormat, weight, height + "", userName);
    }

    private void sendImageFile(Bitmap mBitmap) {
        String image = BitmapBase64Util.bitmapToString(mBitmap);
        mPresenter.uploadImage(this, image);
    }


    @Override
    public void updateUploadImage(UserImageResult.UserImageData userImageData) {
        headUrl = userImageData.getUrl();
        LogUtils.i(TAG, "headUrl= " + headUrl);
        sendUserInfo();
    }

    @Override
    public void uploadImageError() {
        sendUserInfo();
    }

    @Override
    public void updateGetUserInfoView(UserInfoResult.UserInfoData userInfoData) {
        if (userInfoData != null) {
            String imageUrl = userInfoData.getAvatar();
            if (!StringUtils.isEmpty(imageUrl)) {
                LikingPreference.setUserIconUrl(imageUrl);
            }
            LikingPreference.setNickName(userName);
            postEvent(new UpDateUserInfoMessage());
            finish();
        }
    }

    @Override
    public void updateUserInfo() {
        mPresenter.getUserInfo();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            showToast(getString(R.string.confirm_submit_user_info));
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void changeStateView(StateView.State state) {
        mStateView.setState(state);
    }

    @Override
    public void setPresenter() {
        mPresenter = new CompleteUserInfoContract.Presenter();
    }
}
