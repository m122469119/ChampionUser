package com.goodchef.liking.activity;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aaron.android.codelibrary.http.RequestCallback;
import com.aaron.android.codelibrary.http.RequestError;
import com.aaron.android.codelibrary.utils.StringUtils;
import com.aaron.android.framework.base.actionbar.AppBarActivity;
import com.aaron.android.framework.library.imageloader.HImageLoaderSingleton;
import com.aaron.android.framework.library.imageloader.HImageView;
import com.aaron.android.framework.utils.PopupUtils;
import com.goodchef.liking.R;
import com.goodchef.liking.dialog.CameraCustomDialog;
import com.goodchef.liking.dialog.SelectDateDialog;
import com.goodchef.liking.dialog.SelectSexDialog;
import com.goodchef.liking.http.api.LiKingApi;
import com.goodchef.liking.http.result.UserImageResult;
import com.goodchef.liking.http.result.UserInfoResult;
import com.goodchef.liking.http.verify.LiKingVerifyUtils;
import com.goodchef.liking.mvp.presenter.UserInfoPresenter;
import com.goodchef.liking.mvp.view.UserInfoView;
import com.goodchef.liking.utils.BitmapBase64Util;
import com.goodchef.liking.utils.ImageEnviromentUtil;
import com.goodchef.liking.widgets.camera.CameraPhotoHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * 说明:
 * Author shaozucheng
 * Time:16/5/27 下午3:11
 */
public class MyInfoActivity extends AppBarActivity implements View.OnClickListener, UserInfoView {
    private static final String CONTENT_TYPE = "image/jpeg";
    private RelativeLayout mHeadImageLayout;
    private HImageView mHeadImage;
    private EditText mUserNameEditText;
    private TextView mSelectSexTextView;
    private TextView mSelectBirthdayTextView;
    private EditText mUserHeightEditText;
    private EditText mUserWeightEditText;
    private TextView mFinishBtn;

    private CameraPhotoHelper mCameraPhotoHelper;
    private UserInfoPresenter mUserInfoPresenter;

    private int gender = -1;
    private String title;
    private boolean isChange = false;
    private UserInfoResult.UserInfoData mUserInfoData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_info);
        setTitle("修改个人信息");
        initView();
        setViewOnClickListener();
        initData();
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
        mUserInfoPresenter = new UserInfoPresenter(this, this);
        mUserInfoPresenter.getUserInfo();
    }

    private void initView() {
        mHeadImageLayout = (RelativeLayout) findViewById(R.id.layout_head_image);
        mHeadImage = (HImageView) findViewById(R.id.head_image);
        mUserNameEditText = (EditText) findViewById(R.id.edit_user_name);
        mSelectSexTextView = (TextView) findViewById(R.id.select_sex);
        mSelectBirthdayTextView = (TextView) findViewById(R.id.select_birthday);
        mUserHeightEditText = (EditText) findViewById(R.id.edit_height);
        mUserWeightEditText = (EditText) findViewById(R.id.edit_weight);
        mFinishBtn = (TextView) findViewById(R.id.finish_btn);
        setFlag();
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
                        isChange = false;
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

    private void setViewOnClickListener() {
        mHeadImageLayout.setOnClickListener(this);
        mSelectSexTextView.setOnClickListener(this);
        mSelectBirthdayTextView.setOnClickListener(this);
        mFinishBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == mHeadImageLayout) {
            showCameraDialog();
        } else if (v == mSelectSexTextView) {
            showSelectSexDialog();
        } else if (v == mSelectBirthdayTextView) {
            showSelectDateDialog();
        } else if (v == mFinishBtn) {
            updateChangeData();
        }
    }


    private void updateChangeData() {
        String userName = mUserNameEditText.getText().toString().trim();
        String birthday = mSelectBirthdayTextView.getText().toString().trim();
        String height = mUserHeightEditText.getText().toString().trim();
        String weight = mUserWeightEditText.getText().toString().trim();

//        String originalName = mUserInfoData.getName();
//        String originalBirthday = mUserInfoData.getBirthday();
//        int originalHeight = mUserInfoData.getHeight();
//        double originalWeight = mUserInfoData.getWeight();
//        int originalGender = mUserInfoData.getGender();


        if (isChange) {
            mUserInfoPresenter.updateUserInfo(userName, "", gender, birthday, weight, height);
        }
    }


    private void showSelectDateDialog() {
        final SelectDateDialog dateDialog = new SelectDateDialog(this);
        dateDialog.setTextViewOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.dialog_date_cancel:
                        dateDialog.dismiss();
                        isChange = false;
                        break;
                    case R.id.dialog_date_confirm:
                        String month = dateDialog.getMonth();
                        if (Integer.parseInt(month) < 10) {
                            month = "0" + month;
                        }
                        String str = dateDialog.getYear() + "-" + month + "-" + dateDialog.getDay();
                        mSelectBirthdayTextView.setText(str);
                        isChange = true;
                        dateDialog.dismiss();
                        break;
                }
            }
        });
    }


    private void showSelectSexDialog() {
        final SelectSexDialog dialog = new SelectSexDialog(this);
        dialog.setTextViewOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.dialog_text_one:
                        mSelectSexTextView.setText(R.string.sex_man);
                        gender = 1;
                        isChange = true;
                        dialog.dismiss();
                        break;
                    case R.id.dialog_text_second:
                        mSelectSexTextView.setText(R.string.sex_men);
                        gender = 0;
                        isChange = true;
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
                sendImageFile(mBitmap);


            }

            @Override
            public void takePictureFromGallery(ArrayList<String> imagePathList) {
                List<Bitmap> bitmapList = ImageEnviromentUtil.getAlbumBitmapList(imagePathList);
                sendImageFile(bitmapList.get(0));
            }
        });
    }

    @Override
    public void updateGetUserInfoView(UserInfoResult.UserInfoData userInfoData) {
        mUserInfoData = userInfoData;
        String name = userInfoData.getName();
        if (!StringUtils.isEmpty(name)) {
            mUserNameEditText.setText(name);
        }
        String birthday = userInfoData.getBirthday();
        if (!StringUtils.isEmpty(birthday)) {
            mSelectBirthdayTextView.setText(birthday);
        }
        int height = userInfoData.getHeight();
        if (height > 0) {
            mUserHeightEditText.setText(String.valueOf(height));
        }
        double weight = userInfoData.getWeight();
        if (weight > 0) {
            mUserWeightEditText.setText(String.valueOf(weight));
        }
        gender = userInfoData.getGender();
        if (gender == 0) {
            mSelectSexTextView.setText(R.string.sex_men);
        } else if (gender == 1) {
            mSelectSexTextView.setText(R.string.sex_man);
        } else {
            mSelectSexTextView.setText("请选择性别");
        }
    }

    @Override
    public void updateUserInfo() {
        PopupUtils.showToast("更新成功");
    }


    private void sendImageFile(Bitmap mBitmap) {
        String image = BitmapBase64Util.bitmapToString(mBitmap);
        LiKingApi.uploadUserImage(image, new RequestCallback<UserImageResult>() {
            @Override
            public void onSuccess(UserImageResult result) {
                if (LiKingVerifyUtils.isValid(MyInfoActivity.this, result)) {
                    String imageUrl = result.getData().getUrl();
                    if (!StringUtils.isEmpty(imageUrl)) {
                        HImageLoaderSingleton.getInstance().requestImage(mHeadImage, imageUrl);
                    }
                }
            }

            @Override
            public void onFailure(RequestError error) {

            }
        });
    }


}
