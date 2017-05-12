package com.goodchef.liking.widgets.camera;

import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.aaron.android.framework.base.ui.actionbar.AppBarActivity;
import com.aaron.android.thirdparty.camera.ImageFolder;
import com.aaron.android.thirdparty.camera.OnImageDirSelected;
import com.aaron.android.thirdparty.camera.ScanPictureData;
import com.aaron.android.thirdparty.camera.ScanPictureTask;
import com.goodchef.liking.R;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * 说明:从相册中选择图片
 * Author shaozucheng
 * Time:16/4/8 上午11:06
 */
public class AlbumActivity extends AppBarActivity implements OnImageDirSelected {
    private GridView mGirdView;//展示图片GirdView
    private TextView mChooseDir;//目录
    private TextView mImageCount;//图片数量
    private RelativeLayout mBottomLayout;//底部布局
    private List<ImageFolder> mImageFolders = new ArrayList<>();//扫描拿到所有的图片文件夹
    private File mImgDir;//存放图片的文件夹
    private List<String> mImgDirPicture;//图片文件夹下所有的图片
    private AlbumAdapter mAlbumAdapter;//展示图片适配器
    private int mNeedSelectAmount = 1;//选择选择的图片数量
    private ImageDirListDialog mImageDirListDialog;//展示图片文件夹目录的对话框
    private ScanPictureTask scanPictureTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album);
        mNeedSelectAmount = getIntent().getIntExtra(CameraPhotoHelper.INTENT_KEY_NEED_SELECT_AMOUNT, 1);

        initView();
        getImageData();
        initBottomEvent();
    }

    //初始化组件
    private void initView() {
        mGirdView = (GridView) findViewById(R.id.album_gridView);
        mChooseDir = (TextView) findViewById(R.id.id_choose_dir);
        mImageCount = (TextView) findViewById(R.id.id_total_count);
        mBottomLayout = (RelativeLayout) findViewById(R.id.id_bottom_layout);

        setTitle("相册选择");
        showRightMenu("完成", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mAlbumAdapter == null) {
                    return;
                }
                CameraPhotoHelper.selectAlbumEnter(AlbumActivity.this, mAlbumAdapter.getSelectImageFormAlbum());
            }
        });
    }

    /**
     * 获取图片数据
     */
    private void getImageData() {
        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            Toast.makeText(this, "暂无外部存储", Toast.LENGTH_SHORT).show();
            return;
        }
        scanPictureTask = new ScanPictureTask(AlbumActivity.this, new ScanPictureTask.ScanPictureCallBack() {
            @Override
            public void ScanPictureComplete(ScanPictureData scanPictureData) {
                mImageFolders = scanPictureData.getImageFolders();
                mImgDir = scanPictureData.getPictureMaximumDir();
                initGridViewData();
                scanPictureTask.cancel(true);
            }
        });
        scanPictureTask.execute();
    }

    /**
     * 为GirdViewView绑定数据
     */
    private void initGridViewData() {
        if (mImgDir == null) {
            Toast.makeText(getApplicationContext(), "没有扫描到任何图片", Toast.LENGTH_SHORT).show();
            return;
        }
        mImgDirPicture = Arrays.asList(mImgDir.list());
        //把排序翻转，按时间最新的排序
        Collections.reverse(mImgDirPicture);
        //可以看到文件夹的路径和图片的路径分开保存，极大的减少了内存的消耗；
        mAlbumAdapter = new AlbumAdapter(this);
        mAlbumAdapter.setData(mImgDirPicture);
        mAlbumAdapter.setImageDirPath(mImgDir.getAbsolutePath(), mNeedSelectAmount);
        mGirdView.setAdapter(mAlbumAdapter);

        mImageCount.setText(mImgDirPicture.size() + "张");
        mChooseDir.setText(mImgDir.getName());
    }

    @Override
    public void selectedImageFolder(ImageFolder folder) {
        mImgDir = new File(folder.getDir());
        mImgDirPicture = Arrays.asList(mImgDir.list(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String filename) {
                if (filename.endsWith(".jpg") || filename.endsWith(".png") || filename.endsWith(".jpeg"))
                    return true;
                return false;
            }
        }));
        //把排序翻转，按时间最新的排序
        Collections.reverse(mImgDirPicture);
        //可以看到文件夹的路径和图片的路径分开保存，极大的减少了内存的消耗；
        mAlbumAdapter.setData(mImgDirPicture);
        mAlbumAdapter.setImageDirPath(mImgDir.getAbsolutePath(), mNeedSelectAmount);
        mAlbumAdapter.notifyDataSetChanged();

        mImageCount.setText(folder.getCount() + "张");
        mChooseDir.setText(folder.getName());
        mImageDirListDialog.dismiss();
    }

    /**
     * 为底部的布局设置点击事件，弹出dialog
     */
    private void initBottomEvent() {
        mBottomLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initImageDirDialog();
            }
        });
    }

    /**
     * 初始化展示文件夹的dialog
     */
    private void initImageDirDialog() {
        mImageDirListDialog = new ImageDirListDialog(AlbumActivity.this, mImageFolders);
        // 设置选择文件夹的回调
        mImageDirListDialog.setOnImageDirSelected(this);
    }


}
