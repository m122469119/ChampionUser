package com.goodchef.liking.activity;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aaron.http.code.RequestCallback;
import com.aaron.http.code.RequestError;
import com.aaron.android.framework.base.ui.actionbar.AppBarActivity;
import com.aaron.android.framework.base.widget.refresh.StateView;
import com.aaron.android.framework.library.imageloader.HImageLoaderSingleton;
import com.aaron.android.framework.library.imageloader.HImageView;
import com.aaron.common.utils.StringUtils;
import com.goodchef.liking.R;
import com.goodchef.liking.dialog.CameraCustomDialog;
import com.goodchef.liking.dialog.SelectDateDialog;
import com.goodchef.liking.dialog.SelectSexDialog;
import com.goodchef.liking.http.api.LiKingApi;
import com.goodchef.liking.http.result.UserImageResult;
import com.goodchef.liking.http.result.UserInfoResult;
import com.goodchef.liking.http.verify.LiKingVerifyUtils;
import com.goodchef.liking.module.login.LoginActivity;
import com.goodchef.liking.mvp.presenter.UserInfoPresenter;
import com.goodchef.liking.mvp.view.UserInfoView;
import com.goodchef.liking.module.data.local.Preference;
import com.goodchef.liking.utils.BitmapBase64Util;
import com.goodchef.liking.utils.ImageEnviromentUtil;
import com.goodchef.liking.utils.NumberConstantUtil;
import com.goodchef.liking.utils.VerifyDateUtils;
import com.goodchef.liking.widgets.base.LikingStateView;
import com.goodchef.liking.widgets.camera.CameraPhotoHelper;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 说明:修改个人信息
 * Author shaozucheng
 * Time:16/5/27 下午3:11
 */
public class MyInfoActivity extends AppBarActivity implements UserInfoView {
    @BindView(R.id.my_info_state_view)
    LikingStateView mStateView;
    @BindView(R.id.my_edit_userInfo_prompt)
    TextView mUserInfoPromptTextView;
    @BindView(R.id.head_image)
    HImageView mHeadImage;
    @BindView(R.id.layout_head_image)
    RelativeLayout mHeadImageLayout;
    @BindView(R.id.edit_user_name)
    EditText mUserNameEditText;
    @BindView(R.id.select_sex)
    TextView mSelectSexTextView;
    @BindView(R.id.select_sex_arrow)
    ImageView mSelectSexArrow;
    @BindView(R.id.select_birthday)
    TextView mSelectBirthdayTextView;
    @BindView(R.id.select_birthday_arrow)
    ImageView mSelectBirthdayArrow;
    @BindView(R.id.edit_height)
    EditText mUserHeightEditText;
    @BindView(R.id.edit_weight)
    EditText mUserWeightEditText;
    @BindView(R.id.finish_btn)
    TextView mFinishBtn;

    private CameraPhotoHelper mCameraPhotoHelper;
    private UserInfoPresenter mUserInfoPresenter;

    private Integer gender = null;

    private String title;
    private boolean isChange = false;
    private UserInfoResult.UserInfoData mUserInfoData;
    private String headUrl = "";
    private String yearStr = "";
    private String monthStr = "";
    private String dayStr = "";
    private int isUpdateBirthday;
    private int isUpdateGender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_info);
        ButterKnife.bind(this);
        initView();
        initData();
        showHomeUpIcon(R.drawable.app_bar_left_quit);
        mCameraPhotoHelper = new CameraPhotoHelper(this);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mCameraPhotoHelper.onConfigurationChanged(newConfig);
    }

    private void initData() {
        title = getIntent().getStringExtra(LoginActivity.KEY_TITLE_SET_USER_INFO);
        if (!StringUtils.isEmpty(title)) {
            setTitle(title);
        }
        setInfoRequest();
    }

    private void setInfoRequest() {
        if (mUserInfoPresenter == null) {
            mUserInfoPresenter = new UserInfoPresenter(this, this);
        }
        mUserInfoPresenter.getUserInfo();
    }

    private void initView() {
        setFlag();
        mStateView.setState(StateView.State.LOADING);
        mStateView.setOnRetryRequestListener(new StateView.OnRetryRequestListener() {
            @Override
            public void onRetryRequested() {
                setInfoRequest();
            }
        });
    }

    private void setFlag() {
        mUserNameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    if (s.toString().equals(mUserInfoData.getName())) {
                        isChange = false;
                    } else {
                        isChange = true;
                    }
                } else {
                    isChange = false;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        mUserWeightEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    if (s.toString().equals(String.valueOf(mUserInfoData.getWeight()))) {
                        isChange = false;
                    } else {
                        isChange = true;
                    }
                } else {
                    isChange = false;
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mUserHeightEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    if (s.toString().equals(String.valueOf(mUserInfoData.getHeight()))) {
                        isChange = false;
                    } else {
                        isChange = true;
                    }
                } else {
                    isChange = false;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @OnClick({R.id.layout_head_image,R.id.select_sex,R.id.select_birthday,R.id.finish_btn})
    public void onClick(View v) {
        if (v == mHeadImageLayout) {//选择头像
            showCameraDialog();
        } else if (v == mSelectSexTextView) {//选择性别
            if (isUpdateGender == NumberConstantUtil.ZERO) {//没有机会修改性别
                showToast(getString(R.string.user_can_not_revise_info));
            } else if (isUpdateGender == NumberConstantUtil.ONE) {
                showSelectSexDialog();
            }
        } else if (v == mSelectBirthdayTextView) {//选择出生日期
            if (isUpdateBirthday == NumberConstantUtil.ZERO) {
                showToast(getString(R.string.user_can_not_revise_info));
            } else if (isUpdateBirthday == NumberConstantUtil.ONE) {
                showSelectDateDialog();
            }
        } else if (v == mFinishBtn) {//完成按钮
            updateChangeData();
        }
    }

    private void updateChangeData() {
        String userName = mUserNameEditText.getText().toString().trim();
        String birthday = mSelectBirthdayTextView.getText().toString().trim();
        String height = mUserHeightEditText.getText().toString().trim();
        String weight = mUserWeightEditText.getText().toString().trim();


        if (!StringUtils.isEmpty(userName) && userName.equals(mUserInfoData.getName())) {
            userName = "";
        }
        if (birthday.equals(getString(R.string.select_birth_date))) {
            birthday = "";
        } else {
            birthday = compareBirthday(birthday);
        }
        if (userName.length() > 15) {
            showToast(getString(R.string.name_limit));
            return;
        }
        if (!StringUtils.isEmpty(height)) {
            int heightInt = Integer.parseInt(height);
            if (heightInt < 50 || heightInt > 250) {
                showToast(getString(R.string.height_input_error_prompt));
                return;
            }
        }
        if (!StringUtils.isEmpty(weight)) {
            double weightInt = Double.parseDouble(weight);
            if (weightInt < 25 || weightInt > 250) {
                showToast(getString(R.string.weight_input_error_prompt));
                return;
            }
        }
        height = compareHeight(height);
        weight = compareWeight(weight);
        compareGender();

        if (isChange) {
            if (StringUtils.isEmpty(userName) && StringUtils.isEmpty(headUrl) && StringUtils.isEmpty(birthday)
                    && StringUtils.isEmpty(weight) && StringUtils.isEmpty(height) && gender == null) {
                return;
            }
            mUserInfoPresenter.updateUserInfo(userName, headUrl, gender, birthday, weight, height);
        }
    }

    /**
     * 比较性别
     */
    private void compareGender() {
        if (gender != null && mUserInfoData.getGender() == gender) {
            gender = null;
        }
    }

    /**
     * 比较体重
     *
     * @param weight
     * @return
     */
    private String compareWeight(String weight) {
        if (!StringUtils.isEmpty(weight)) {
            double weightInt = Double.parseDouble(weight);
            if (weightInt == mUserInfoData.getWeight()) {
                weight = "";
            }
        }
        return weight;
    }

    /**
     * 比较身高
     *
     * @param height
     * @return
     */
    private String compareHeight(String height) {
        if (!StringUtils.isEmpty(height)) {
            int heightInt = Integer.parseInt(height);
            if (heightInt == mUserInfoData.getHeight()) {
                height = "";
            }
        }
        return height;
    }

    /**
     * 比较出生日期有木有发生更改
     *
     * @param birthday
     * @return
     */
    private String compareBirthday(String birthday) {
        String currentYear = "";
        String currentMonth = "";
        String currentDay = "";

        String primaryYear = "";
        String primaryMonth = "";
        String primaryDay = "";

        if (mUserInfoData != null) {
            String primaryBirthday = mUserInfoData.getBirthday();
            if (!StringUtils.isEmpty(birthday) && !StringUtils.isEmpty(primaryBirthday)) {
                String a[] = birthday.split("-");
                if (a.length >= 2) {
                    currentYear = a[0];
                    currentMonth = a[1];
                    currentDay = a[2];
                }
                String b[] = primaryBirthday.split("-");
                if (b.length >= 2) {
                    primaryYear = b[0];
                    primaryMonth = b[1];
                    primaryDay = b[2];
                }

                if (primaryYear.equals(currentYear) && primaryMonth.equals(currentMonth) && primaryDay.equals(currentDay)) {
                    birthday = "";
                }
            }
        }
        return birthday;
    }


    /**
     * 选择出生日期
     */
    private void showSelectDateDialog() {
        final SelectDateDialog dateDialog = new SelectDateDialog(this, yearStr, monthStr, dayStr);
        dateDialog.setTextViewOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.dialog_date_cancel:
                        dateDialog.dismiss();
                        isChange = false;
                        break;
                    case R.id.dialog_date_confirm:
                        yearStr = dateDialog.getYear();
                        String month = dateDialog.getMonth();
                        monthStr = month;
                        dayStr = dateDialog.getDay();
                        if (Integer.parseInt(month) < 10) {
                            month = "0" + month;
                        }
                        if (Integer.parseInt(dayStr) < 10) {
                            dayStr = "0" + dayStr;
                        }
                        String str = yearStr + "-" + month + "-" + dayStr;
                        if (VerifyDateUtils.isVerifyDate(str)) {
                            mSelectBirthdayTextView.setText(str);
                            isChange = true;
                           showToast(getString(R.string.user_birthday) + getString(R.string.myinfo_sex_and_birthday_only_change_one_times));
                            dateDialog.dismiss();
                        } else {
                            yearStr = "";
                            monthStr = "";
                            dayStr = "";
                            showToast(getString(R.string.select_date_error_prompt));
                        }
                        break;
                }
            }
        });
    }

    /**
     * 选择性别
     */
    private void showSelectSexDialog() {
        final SelectSexDialog dialog = new SelectSexDialog(this);
        dialog.setTextViewOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.dialog_text_one:
                        mSelectSexTextView.setText(R.string.sex_man);
                        gender = 1;
                        setSexChange();
                        showToast(getString(R.string.sex) + getString(R.string.myinfo_sex_and_birthday_only_change_one_times));
                        dialog.dismiss();
                        break;
                    case R.id.dialog_text_second:
                        mSelectSexTextView.setText(R.string.sex_men);
                        gender = 0;
                        setSexChange();
                        showToast(getString(R.string.sex) + getString(R.string.myinfo_sex_and_birthday_only_change_one_times));
                        dialog.dismiss();
                        break;
                }
            }
        });
        dialog.setNegativeClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isChange = false;
                dialog.dismiss();
            }
        });
    }

    private void setSexChange() {
        if (gender != mUserInfoData.getGender()) {
            isChange = true;
        } else {
            isChange = false;
        }
    }


    /**
     * 显示拍照和选择相册dialog
     */
    public void showCameraDialog() {
        final CameraCustomDialog cameraDialog = new CameraCustomDialog(this);
        cameraDialog.setTextViewOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.dialog_photograph://拍照
                        mCameraPhotoHelper.takePhotoFromCamera();
                        cameraDialog.dismiss();
                        break;
                    case R.id.dialog_album://从相册中选择
                        mCameraPhotoHelper.selectSingleFormAlbum();
                        cameraDialog.dismiss();
                        break;
                    case R.id.dialog_cancel_btn:
                        cameraDialog.dismiss();
                        break;
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mCameraPhotoHelper.onActivityResult(requestCode, resultCode, data, new CameraPhotoHelper.CameraPhotoCallBack() {
            @Override
            public void takePictureFromCamera(String imagePath) {
                Bitmap mBitmap = ImageEnviromentUtil.compressImageSize(imagePath);
                if (mBitmap != null) {
                    sendImageFile(mBitmap);
                } else {
                    showToast(getString(R.string.please_select_picture));
                }
            }

            @Override
            public void takePictureFromGallery(ArrayList<String> imagePathList) {
                List<Bitmap> bitmapList = ImageEnviromentUtil.getAlbumBitmapList(imagePathList);
                Bitmap mBitmap = bitmapList.get(0);
                if (mBitmap != null) {
                    sendImageFile(bitmapList.get(0));
                } else {
                    showToast(getString(R.string.please_select_picture));
                }
            }
        });
    }

    @Override
    public void updateGetUserInfoView(UserInfoResult.UserInfoData userInfoData) {
        if (userInfoData != null) {
            mStateView.setState(StateView.State.SUCCESS);
            String imageUrl = userInfoData.getAvatar();
            if (!StringUtils.isEmpty(imageUrl)) {
                HImageLoaderSingleton.getInstance().loadImage(mHeadImage, imageUrl);
                Preference.setUserIconUrl(imageUrl);
            }
            mUserInfoData = userInfoData;
            String name = userInfoData.getName();
            if (!StringUtils.isEmpty(name)) {
                mUserNameEditText.setText(name);
                Preference.setNickName(name);
            }
            String birthday = userInfoData.getBirthday();
            if (!StringUtils.isEmpty(birthday)) {
                mSelectBirthdayTextView.setText(birthday);
                String a[] = birthday.split("-");
                if (a.length >= 2) {
                    yearStr = a[0];
                    monthStr = a[1];
                    dayStr = a[2];
                }
            }
            int height = userInfoData.getHeight();
            if (height > 0) {
                mUserHeightEditText.setText(String.valueOf(height));
            }
            double weight = userInfoData.getWeight();
            if (weight > 0) {
                mUserWeightEditText.setText(String.valueOf(weight));
            }
            int gender = userInfoData.getGender();
            if (gender == NumberConstantUtil.ZERO) {
                mSelectSexTextView.setText(R.string.sex_men);
            } else if (gender == NumberConstantUtil.ONE) {
                mSelectSexTextView.setText(R.string.sex_man);
            } else {
                mSelectSexTextView.setText(R.string.select_gender);
            }

            isUpdateBirthday = userInfoData.getIsUpdateBirthday();
            isUpdateGender = userInfoData.getIsUpdateGender();
            if (isUpdateGender == NumberConstantUtil.ZERO) {//没有机会修改性别
                mSelectSexArrow.setVisibility(View.GONE);
            } else if (isUpdateGender == NumberConstantUtil.ONE) {
                mSelectSexArrow.setVisibility(View.VISIBLE);
            }

            if (isUpdateBirthday == NumberConstantUtil.ZERO) {//没有修改的机会生日
                mSelectBirthdayArrow.setVisibility(View.GONE);
            } else if (isUpdateBirthday == NumberConstantUtil.ONE) {
                mSelectBirthdayArrow.setVisibility(View.VISIBLE);
            }
            UserChangeInfoPromptTextView();
        } else {
            mStateView.setState(StateView.State.NO_DATA);
        }
    }

    private void UserChangeInfoPromptTextView() {
        if (isUpdateGender == NumberConstantUtil.ONE && isUpdateBirthday == NumberConstantUtil.ONE) {//性别和生日都能改
            mUserInfoPromptTextView.setVisibility(View.VISIBLE);
            mUserInfoPromptTextView.setText(getString(R.string.sex) + "、" + getString(R.string.user_birthday) + getString(R.string.myinfo_sex_and_birthday_only_change_one_times));
        } else if (isUpdateGender == NumberConstantUtil.ZERO && isUpdateBirthday == NumberConstantUtil.ONE) {//性别不能改，生日可以改
            mUserInfoPromptTextView.setVisibility(View.VISIBLE);
            mUserInfoPromptTextView.setText(getString(R.string.user_birthday) + getString(R.string.myinfo_sex_and_birthday_only_change_one_times));
        } else if (isUpdateGender == NumberConstantUtil.ONE && isUpdateBirthday == NumberConstantUtil.ZERO) {//性别可以改，生日不能改
            mUserInfoPromptTextView.setVisibility(View.VISIBLE);
            mUserInfoPromptTextView.setText(getString(R.string.sex) + getString(R.string.myinfo_sex_and_birthday_only_change_one_times));
        } else if (isUpdateGender == NumberConstantUtil.ZERO && isUpdateBirthday == NumberConstantUtil.ZERO) {//性别不能改，生日也不能改
            mUserInfoPromptTextView.setVisibility(View.GONE);
        }
    }

    @Override
    public void updateUserInfo() {
        showToast(getString(R.string.update_success));
        mUserInfoPresenter.getUserInfo();
    }


    private void sendImageFile(Bitmap mBitmap) {
        String image = BitmapBase64Util.bitmapToString(mBitmap);
        LiKingApi.uploadUserImage(image, new RequestCallback<UserImageResult>() {
            @Override
            public void onSuccess(UserImageResult result) {
                if (LiKingVerifyUtils.isValid(MyInfoActivity.this, result)) {
                    headUrl = result.getData().getUrl();
                    if (!StringUtils.isEmpty(headUrl)) {
                        mUserInfoPresenter.updateUserInfo("", headUrl, null, "", "", "");
                    }
                }
            }

            @Override
            public void onFailure(RequestError error) {

            }
        });
    }


    @Override
    public void handleNetworkFailure() {
        mStateView.setState(StateView.State.FAILED);
    }
}
