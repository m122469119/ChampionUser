package com.goodchef.liking.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aaron.android.codelibrary.utils.StringUtils;
import com.aaron.android.framework.library.imageloader.HImageLoaderSingleton;
import com.aaron.android.framework.library.imageloader.HImageView;
import com.aaron.android.framework.utils.DialogUtils;
import com.goodchef.liking.R;
import com.goodchef.liking.activity.GymCoursesActivity;
import com.goodchef.liking.fragment.LikingLessonFragment;
import com.goodchef.liking.http.result.CheckGymListResult;
import com.goodchef.liking.utils.NavigationBarUtil;

/**
 * 说明:
 * Author shaozucheng
 * Time:16/7/8 下午7:40
 */
public class MapStoreDialog {
    private Context mContext;
    private Dialog mDialog;
    private TextView mNameTextView;
    private TextView mAddressTextView;
    private HImageView mHImageView;
    private RelativeLayout mLayout;

    private CheckGymListResult.CheckGymData.CheckGym mGymDto;

    public MapStoreDialog(Context context, CheckGymListResult.CheckGymData.CheckGym gymDto) {
        this.mContext = context;
        this.mGymDto = gymDto;
        mDialog = new android.app.AlertDialog.Builder(context, R.style.camera_dialog_no_screen).create();
        WindowManager wmManager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        boolean hasSoft = NavigationBarUtil.hasSoftKeys(wmManager);
        if (hasSoft) {
            int navigationBarHeight = NavigationBarUtil.getNavigationBarHeight(context);
            //这一行很重要
            DialogUtils.resetDialogScreenPosition(mDialog, Gravity.BOTTOM, 0, navigationBarHeight, WindowManager.LayoutParams.MATCH_PARENT,
                    WindowManager.LayoutParams.WRAP_CONTENT);
        } else {
            DialogUtils.resetDialogScreenPosition(mDialog, Gravity.BOTTOM, 0, 0, WindowManager.LayoutParams.MATCH_PARENT,
                    WindowManager.LayoutParams.WRAP_CONTENT);
        }

        mDialog.setCancelable(true);
        mDialog.setCanceledOnTouchOutside(true);
        mDialog.show();
        Window window = mDialog.getWindow();
        window.setContentView(R.layout.dialog_store_map_detials);
        window.setWindowAnimations(R.style.my_dialog_enter_exit);  //添加dialog进入和退出的动画

        iniView(window);
        setGymData();
    }

    private void iniView(Window window) {
        mNameTextView = (TextView) window.findViewById(R.id.map_store_name);
        mAddressTextView = (TextView) window.findViewById(R.id.store_address);
        mHImageView = (HImageView) window.findViewById(R.id.store_image);
        mLayout = (RelativeLayout) window.findViewById(R.id.layout_store_image);
    }


    private void setGymData() {
        mNameTextView.setText(mGymDto.getGymName().trim());
        mAddressTextView.setText(mGymDto.getGymAddress().trim());
        String imageUrl = mGymDto.getImg();
        if (!StringUtils.isEmpty(imageUrl)) {
            HImageLoaderSingleton.getInstance().requestImage(mHImageView, imageUrl);
        }

        mLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, GymCoursesActivity.class);
                intent.putExtra(LikingLessonFragment.KEY_GYM_ID, mGymDto.getGymId()+"");
                intent.putExtra(LikingLessonFragment.KEY_GYM_NAME, mGymDto.getGymName());
                mContext.startActivity(intent);
            }
        });
    }


    /**
     * 设置是否可以关闭
     *
     * @param cancelable true或false
     */
    public void setCancelable(boolean cancelable) {
        mDialog.setCancelable(cancelable);
    }

    /**
     * 设置是否可以点击dialog外面关闭dialog
     *
     * @param canceledOnTouchOutside true或false
     */
    public void setCanceledOnTouchOutside(boolean canceledOnTouchOutside) {
        mDialog.setCanceledOnTouchOutside(canceledOnTouchOutside);
    }

    /**
     * 关闭对话框
     */
    public void dismiss() {
        mDialog.dismiss();
    }
}
