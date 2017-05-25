package com.goodchef.liking.widgets.camera;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.aaron.imageloader.ImageLoader;
import com.aaron.android.framework.base.widget.listview.HBaseAdapter;
import com.aaron.imageloader.code.HImageConfigBuilder;
import com.goodchef.liking.utils.HImageLoaderSingleton;
import com.aaron.imageloader.code.HImageView;
import com.goodchef.liking.R;
import com.aaron.camera.SelectImageFromAlbum;

import java.util.ArrayList;

/**
 * 说明:展示扫描后的图片适配器
 * Author shaozucheng
 * Time:16/5/30 下午5:03
 */
public class AlbumAdapter extends HBaseAdapter<String> implements SelectImageFromAlbum {
    private Context mContext;
    private String mImageDirPath;//图片文件夹路径
    private int mNeedSelectAmount;//需要选择的图片数量
    //用户选择的图片，存储为图片的完整路径
    private ArrayList<String> mSelectedImage = new ArrayList<>();

    public AlbumAdapter(Context context) {
        super(context);
        this.mContext = context;
    }

    public void setImageDirPath(String imageDirPath, int needSelectAmount) {
        mImageDirPath = imageDirPath;
        mNeedSelectAmount = needSelectAmount;
    }

    @Override
    protected BaseViewHolder<String> createViewHolder() {
        return new AlbumViewHolder();
    }

    @Override
    public ArrayList<String> getSelectImageFormAlbum() {
        return mSelectedImage;
    }


    class AlbumViewHolder extends BaseViewHolder<String> {
        View mRootView;
        HImageView mImageView;
        ImageButton mImageButton;

        @Override
        public View inflateItemView() {
            mRootView = LayoutInflater.from(mContext).inflate(R.layout.grid_item, null, false);
            mImageView = (HImageView) mRootView.findViewById(R.id.id_item_image);
            mImageButton = (ImageButton) mRootView.findViewById(R.id.id_item_select);
            return mRootView;
        }

        @Override
        public void bindViews(final String dirPath) {
            HImageLoaderSingleton.getInstance().loadImage(new HImageConfigBuilder(mImageView, mImageDirPath+"/"+dirPath)
                    .resize(100,100)
                    .setLoadType(ImageLoader.LoaderType.FILE).build());
            mImageButton.setImageResource(R.drawable.pay_radio_gray_uncheck);
            mImageView.setColorFilter(null);
            mImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // 已经选择过该图片
                    if (mSelectedImage.contains(mImageDirPath + "/" + dirPath)) {
                        mSelectedImage.remove(mImageDirPath + "/" + dirPath);
                        mImageButton.setImageResource(R.drawable.pay_radio_gray_uncheck);
                        mImageView.setColorFilter(null);
                    } else {// 未选择该图片
                        if (mSelectedImage.size() >= mNeedSelectAmount) {
                            Toast.makeText(mContext, "最多可选择" + mNeedSelectAmount + "张图片", Toast.LENGTH_LONG).show();
                        } else {
                            mSelectedImage.add(mImageDirPath + "/" + dirPath);
                            mImageButton.setImageResource(R.drawable.pay_radio_green_check);
                            mImageView.setColorFilter(Color.parseColor("#77000000"));
                        }
                    }
                }
            });
        }
    }

}
