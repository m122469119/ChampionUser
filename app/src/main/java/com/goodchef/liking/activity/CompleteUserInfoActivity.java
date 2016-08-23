package com.goodchef.liking.activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.aaron.android.codelibrary.http.RequestError;
import com.aaron.android.codelibrary.imageloader.ImageLoader;
import com.aaron.android.codelibrary.utils.StringUtils;
import com.aaron.android.framework.base.ui.actionbar.AppBarActivity;
import com.aaron.android.framework.base.widget.refresh.StateView;
import com.aaron.android.framework.library.imageloader.HImageConfigBuilder;
import com.aaron.android.framework.library.imageloader.HImageLoaderSingleton;
import com.aaron.android.framework.library.imageloader.HImageView;
import com.aaron.android.framework.utils.EnvironmentUtils;
import com.goodchef.liking.R;
import com.goodchef.liking.http.api.LiKingApi;
import com.goodchef.liking.http.callback.RequestUiLoadingCallback;
import com.goodchef.liking.http.result.UserImageResult;
import com.goodchef.liking.http.result.UserInfoResult;
import com.goodchef.liking.http.verify.LiKingVerifyUtils;
import com.goodchef.liking.mvp.presenter.UserInfoPresenter;
import com.goodchef.liking.mvp.view.UserInfoView;
import com.goodchef.liking.storage.Preference;
import com.goodchef.liking.utils.BitmapBase64Util;
import com.goodchef.liking.utils.ImageEnviromentUtil;
import com.goodchef.liking.widgets.base.LikingStateView;

/**
 * 说明:首次登陆完成个人信息界面
 * Author shaozucheng
 * Time:16/8/19 下午2:35
 */
public class CompleteUserInfoActivity extends AppBarActivity implements View.OnClickListener, UserInfoView {

    private LikingStateView mStateView;
    private HImageView mHImageView;
    private TextView mUserNameTextView;
    private TextView mBirthdayTextView;
    private TextView mHeightTextView;
    private ImageView mSexManImage;
    private ImageView mSexWomenImage;
    private TextView mWeightTextView;
    private TextView mCompleteBtn;

    private String userName;
    private String mLocalHeadImageUrl;
    private int sex = 1;
    private String mBirthdayStr;
    private int height;
    private String weight;
    private String headUrl;

    private UserInfoPresenter mUserInfoPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complete_user_info);
        setTitle(getString(R.string.activity_title_complete_userinfo));
        showHomeUpIcon(0);
        initView();
        initData();
        getIntentData();
        mUserInfoPresenter = new UserInfoPresenter(this, this);
    }

    private void initView() {
        mStateView = (LikingStateView) findViewById(R.id.complete_userInfo_state_view);
        mHImageView = (HImageView) findViewById(R.id.head_image);
        mUserNameTextView = (TextView) findViewById(R.id.user_name_text);
        mSexManImage = (ImageView) findViewById(R.id.sex_man_image);
        mSexWomenImage = (ImageView) findViewById(R.id.sex_women_image);
        mBirthdayTextView = (TextView) findViewById(R.id.birthday_text);
        mHeightTextView = (TextView) findViewById(R.id.height_text);
        mWeightTextView = (TextView) findViewById(R.id.weight_text);
        mCompleteBtn = (TextView) findViewById(R.id.complete_userInfo_btn);
        mCompleteBtn.setOnClickListener(this);
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
        height = getIntent().getIntExtra(SelectHeightActivity.KEY_HEIGHT, 0);
        weight = getIntent().getStringExtra(SelectWeightActivity.KEY_WEIGHT);

        mUserNameTextView.setText(userName);
        if (!StringUtils.isEmpty(mLocalHeadImageUrl)) {
            HImageLoaderSingleton.getInstance().loadImage(new HImageConfigBuilder(mHImageView,mLocalHeadImageUrl)
                    .setLoadType(ImageLoader.LoaderType.FILE)
                    .build());
        }
        if (sex == 1) {
            mSexManImage.setVisibility(View.VISIBLE);
            mSexWomenImage.setVisibility(View.GONE);
        } else if (sex == 2) {
            mSexManImage.setVisibility(View.GONE);
            mSexWomenImage.setVisibility(View.VISIBLE);
        }
        mBirthdayTextView.setText(mBirthdayStr);
        mHeightTextView.setText(height + " cm");
        mWeightTextView.setText(weight);
    }

    @Override
    public void onClick(View v) {
        if (v == mCompleteBtn) {
            Bitmap mBitmap = ImageEnviromentUtil.compressImageSize(mLocalHeadImageUrl);
            sendImageFile(mBitmap);
        }
    }

    private void sendImageFile(Bitmap mBitmap) {
        String image = BitmapBase64Util.bitmapToString(mBitmap);
        LiKingApi.uploadUserImage(image, new RequestUiLoadingCallback<UserImageResult>(CompleteUserInfoActivity.this, R.string.loading) {
            @Override
            public void onSuccess(UserImageResult result) {
                super.onSuccess(result);
                if (LiKingVerifyUtils.isValid(CompleteUserInfoActivity.this, result)) {
                    headUrl = result.getData().getUrl();
                    Log.e("headUrl", headUrl);
                    if (!StringUtils.isEmpty(headUrl)) {
                        mUserInfoPresenter.updateUserInfo(userName, headUrl, sex, mBirthdayStr, weight, height + "");
                    }
                }
            }

            @Override
            public void onFailure(RequestError error) {
                super.onFailure(error);
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
            this.finish();
        }
    }

    @Override
    public void updateUserInfo() {
        mUserInfoPresenter.getUserInfo();
    }

    @Override
    public void handleNetworkFailure() {

    }
}
