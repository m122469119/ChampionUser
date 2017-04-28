package com.goodchef.liking.activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.aaron.android.codelibrary.http.RequestError;
import com.aaron.android.codelibrary.imageloader.ImageLoader;
import com.aaron.android.framework.base.ui.actionbar.AppBarActivity;
import com.aaron.android.framework.base.widget.refresh.StateView;
import com.aaron.android.framework.library.imageloader.HImageConfigBuilder;
import com.aaron.android.framework.library.imageloader.HImageLoaderSingleton;
import com.aaron.android.framework.library.imageloader.HImageView;
import com.aaron.android.framework.utils.EnvironmentUtils;
import com.aaron.common.utils.LogUtils;
import com.aaron.common.utils.StringUtils;
import com.goodchef.liking.R;
import com.goodchef.liking.eventmessages.UpDateUserInfoMessage;
import com.goodchef.liking.http.api.LiKingApi;
import com.goodchef.liking.http.callback.RequestUiLoadingCallback;
import com.goodchef.liking.http.result.UserImageResult;
import com.goodchef.liking.http.result.UserInfoResult;
import com.goodchef.liking.http.verify.LiKingVerifyUtils;
import com.goodchef.liking.mvp.presenter.UserInfoPresenter;
import com.goodchef.liking.mvp.view.UserInfoView;
import com.goodchef.liking.module.data.local.Preference;
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
public class CompleteUserInfoActivity extends AppBarActivity implements UserInfoView {

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
    private String mLocalHeadImageUrl;
    private int sex = -1;
    private String mBirthdayStr;
    private String mBirthdayStrFormat;
    private int height;
    private String weight;
    private String headUrl = "";

    private UserInfoPresenter mUserInfoPresenter;
    private Bitmap mBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complete_user_info);
        ButterKnife.bind(this);
        mUserInfoPresenter = new UserInfoPresenter(this, this);
        setTitle(getString(R.string.activity_title_complete_userinfo));
        showHomeUpIcon(0);
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
        mLocalHeadImageUrl = getIntent().getStringExtra(UserHeadImageActivity.KEY_HEAD_IMAGE);
        sex = getIntent().getIntExtra(SexActivity.KEY_SEX, 0);
        mBirthdayStr = getIntent().getStringExtra(SelectBirthdayActivity.KEY_BIRTHDAY);
        mBirthdayStrFormat = getIntent().getStringExtra(SelectBirthdayActivity.KEY_BIRTHDAY_FORMAT);
        height = getIntent().getIntExtra(SelectHeightActivity.KEY_HEIGHT, 0);
        weight = getIntent().getStringExtra(SelectWeightActivity.KEY_WEIGHT);

        mUserNameTextView.setText(userName);
        if (!StringUtils.isEmpty(mLocalHeadImageUrl)) {
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
        mBirthdayTextView.setText(mBirthdayStr);
        mHeightTextView.setText(height + getString(R.string.cm));
        mWeightTextView.setText(weight + getString(R.string.kg));

        mBitmap = ImageEnviromentUtil.compressImageSize(mLocalHeadImageUrl);
    }

    @OnClick({R.id.complete_userInfo_btn})
    public void onClick(View v) {
        if (v == mCompleteBtn) {
            sendImageFile(mBitmap);
        }
    }

    /**
     * 提交用户信息
     */
    private void sendUserInfo() {
        if (mUserInfoPresenter == null) {
            mUserInfoPresenter = new UserInfoPresenter(this, this);
        }
        mUserInfoPresenter.updateUserInfo(userName, headUrl, sex, mBirthdayStrFormat, weight, height + "");
    }

    private void sendImageFile(Bitmap mBitmap) {
        String image = BitmapBase64Util.bitmapToString(mBitmap);
        LiKingApi.uploadUserImage(image, new RequestUiLoadingCallback<UserImageResult>(CompleteUserInfoActivity.this, R.string.loading) {
            @Override
            public void onSuccess(UserImageResult result) {
                super.onSuccess(result);
                if (LiKingVerifyUtils.isValid(CompleteUserInfoActivity.this, result)) {
                    headUrl = result.getData().getUrl();
                    LogUtils.i(TAG, "headUrl= " + headUrl);
                    sendUserInfo();
                }
            }

            @Override
            public void onFailure(RequestError error) {
                super.onFailure(error);
                sendUserInfo();
            }
        });
    }


    @Override
    public void updateGetUserInfoView(UserInfoResult.UserInfoData userInfoData) {
        if (userInfoData != null) {
            String imageUrl = userInfoData.getAvatar();
            if (!StringUtils.isEmpty(imageUrl)) {
                Preference.setUserIconUrl(imageUrl);
            }
            Preference.setNickName(userName);
            postEvent(new UpDateUserInfoMessage());
            finish();
        }
    }

    @Override
    public void updateUserInfo() {
        mUserInfoPresenter.getUserInfo();
    }

    @Override
    public void handleNetworkFailure() {

    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            showToast(getString(R.string.confirm_submit_user_info));
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

}
