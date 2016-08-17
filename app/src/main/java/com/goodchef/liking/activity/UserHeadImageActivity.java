package com.goodchef.liking.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.aaron.android.codelibrary.utils.LogUtils;
import com.aaron.android.codelibrary.utils.StringUtils;
import com.aaron.android.framework.base.ui.actionbar.AppBarActivity;
import com.aaron.android.framework.base.widget.refresh.StateView;
import com.aaron.android.framework.library.imageloader.HImageView;
import com.aaron.android.framework.utils.EnvironmentUtils;
import com.aaron.android.framework.utils.PopupUtils;
import com.goodchef.liking.R;
import com.goodchef.liking.dialog.CameraCustomDialog;
import com.goodchef.liking.utils.ImageEnviromentUtil;
import com.goodchef.liking.widgets.base.LikingStateView;
import com.goodchef.liking.widgets.camera.CameraPhotoHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * 说明:
 * Author shaozucheng
 * Time:16/8/15 上午11:16
 */
public class UserHeadImageActivity extends AppBarActivity implements View.OnClickListener {
    public static final String KEY_HEAD_IMAGE = "key_head_image";
    private LikingStateView mStateView;
    private TextView mUserNameTextView;
    private TextView mSelectImageTextView;
    private HImageView mHImageView;
    private TextView mNextBtn;

    private String userName;
    private CameraPhotoHelper mCameraPhotoHelper;
    private String mLoaclHeadUrl = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_head_image);
        setTitle("添加头像");
        initView();
        setViewOnClickListener();
        initData();
        mCameraPhotoHelper = new CameraPhotoHelper(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!StringUtils.isEmpty(mLoaclHeadUrl)) {
            mHImageView.setImageURI(Uri.parse(mLoaclHeadUrl));
        }
    }

    private void initView() {
        mStateView = (LikingStateView) findViewById(R.id.user_head_image_state_view);
        mUserNameTextView = (TextView) findViewById(R.id.user_name_text);
        mHImageView = (HImageView) findViewById(R.id.head_image);
        mSelectImageTextView = (TextView) findViewById(R.id.head_select_image_prompt);
        mNextBtn = (TextView) findViewById(R.id.head_image_next_btn);
    }

    private void setViewOnClickListener() {
        mNextBtn.setOnClickListener(this);
        mHImageView.setOnClickListener(this);
        mSelectImageTextView.setOnClickListener(this);
    }

    private void initData() {
        userName = getIntent().getStringExtra(WriteNameActivity.KEY_USER_NAME);
        mUserNameTextView.setText(userName);

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

    @Override
    public void onClick(View v) {
        if (v == mHImageView || v == mSelectImageTextView) {
            showCameraDialog();
        } else if (v == mNextBtn) {
            Intent intent = new Intent(this, SexActivity.class);
            intent.putExtra(WriteNameActivity.KEY_USER_NAME, userName);
            intent.putExtra(KEY_HEAD_IMAGE,mLoaclHeadUrl);
            startActivity(intent);
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
                    mLoaclHeadUrl = "file://" + imagePath;
                    mHImageView.setImageURI(Uri.parse(mLoaclHeadUrl));
                } else {
                    PopupUtils.showToast("请重新选图片");
                }
            }

            @Override
            public void takePictureFromGallery(ArrayList<String> imagePathList) {
                List<Bitmap> bitmapList = ImageEnviromentUtil.getAlbumBitmapList(imagePathList);
                Bitmap mBitmap = bitmapList.get(0);
                if (mBitmap != null) {
                    LogUtils.i("imagepath =", imagePathList.get(0));
                    mLoaclHeadUrl = "file://" + imagePathList.get(0);
                    mHImageView.setImageURI(Uri.parse(mLoaclHeadUrl));
                } else {
                    PopupUtils.showToast("请重新选图片");
                }
            }
        });
    }


}
