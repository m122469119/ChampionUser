package com.goodchef.liking.activity;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import com.aaron.android.framework.base.actionbar.AppBarActivity;
import com.aaron.android.framework.library.imageloader.HImageView;

import com.goodchef.liking.R;
import com.goodchef.liking.dialog.CameraCustomDialog;
import com.goodchef.liking.utils.ImageEnviromentUtil;
import com.goodchef.liking.widgets.camera.CameraPhotoHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * 说明:
 * Author shaozucheng
 * Time:16/5/27 下午3:11
 */
public class MyInfoActivity extends AppBarActivity implements View.OnClickListener {

    private RelativeLayout mHeadImageLayout;
    private HImageView mHeadImage;

    private CameraPhotoHelper mCameraPhotoHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_info);
        setTitle("XXX");
        initData();
        mCameraPhotoHelper = new CameraPhotoHelper(this);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mCameraPhotoHelper.onConfigurationChanged(newConfig);
    }


    private void initData() {
        initView();
        setViewOnClickListener();
    }

    private void initView() {
        mHeadImageLayout = (RelativeLayout) findViewById(R.id.layout_head_image);
        mHeadImage = (HImageView) findViewById(R.id.head_image);
    }

    private void setViewOnClickListener() {
        mHeadImageLayout.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == mHeadImageLayout) {
            showCameraDialog();
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
                mHeadImage.setImageBitmap(mBitmap);
            }

            @Override
            public void takePictureFromGallery(ArrayList<String> imagePathList) {
                List<Bitmap> bitmapList = ImageEnviromentUtil.getAlbumBitmapList(imagePathList);
                mHeadImage.setImageBitmap(bitmapList.get(0));
            }
        });
    }
}
