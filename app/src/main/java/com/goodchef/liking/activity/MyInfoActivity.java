package com.goodchef.liking.activity;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aaron.android.framework.base.actionbar.AppBarActivity;
import com.aaron.android.framework.library.imageloader.HImageView;

import com.aaron.android.framework.utils.PopupUtils;
import com.goodchef.liking.R;
import com.goodchef.liking.dialog.CameraCustomDialog;
import com.goodchef.liking.dialog.SelectDateDialog;
import com.goodchef.liking.dialog.SelectSexDialog;
import com.goodchef.liking.http.result.UserInfoResult;
import com.goodchef.liking.mvp.presenter.UserInfoPresenter;
import com.goodchef.liking.mvp.view.UserInfoView;
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

    private int gender;

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

        }
    }

    private void showSelectDateDialog(){
        SelectDateDialog  dateDialog = new SelectDateDialog(this);

    }



    private void showSelectSexDialog() {
        final SelectSexDialog dialog = new SelectSexDialog(this);
        dialog.setTextViewOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.dialog_text_one:
                        mSelectSexTextView.setText(R.string.sex_man);
                        gender = 0;
                        dialog.dismiss();
                        break;
                    case R.id.dialog_text_second:
                        mSelectSexTextView.setText(R.string.sex_men);
                        gender = 1;
                        dialog.dismiss();
                        break;
                }
            }
        });
        dialog.setNegativeClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                mHeadImage.setImageBitmap(mBitmap);
            }

            @Override
            public void takePictureFromGallery(ArrayList<String> imagePathList) {
                List<Bitmap> bitmapList = ImageEnviromentUtil.getAlbumBitmapList(imagePathList);
                mHeadImage.setImageBitmap(bitmapList.get(0));
            }
        });
    }

    @Override
    public void updateGetUserInfoView(UserInfoResult.UserInfoData userInfoData) {
        mUserNameEditText.setText(userInfoData.getName());
        mSelectBirthdayTextView.setText(userInfoData.getBirthday());
        mUserHeightEditText.setText(userInfoData.getHeight()+"");
        mUserWeightEditText.setText(userInfoData.getWeight() + "");
    }

    @Override
    public void updateUserInfo() {
        PopupUtils.showToast("更新成功");
    }
}
