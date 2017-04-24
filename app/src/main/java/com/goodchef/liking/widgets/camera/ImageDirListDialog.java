package com.goodchef.liking.widgets.camera;

import android.app.Dialog;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;

import com.aaron.android.framework.utils.DialogUtils;
import com.goodchef.liking.R;
import com.goodchef.liking.utils.NavigationBarUtil;
import com.aaron.camera.ImageFolder;
import com.aaron.camera.OnImageDirSelected;

import java.util.List;

/**
 * 说明:
 * Author shaozucheng
 * Time:16/4/12 上午10:10
 */
public class ImageDirListDialog {
    private Context mContext;
    private Dialog mDialog;
    private ListView mListDir;
    private OnImageDirSelected mImageDirSelected;
    private static DisplayMetrics sDisplayMetrics = null;
    private static final float ROUND_DIFFERENCE = 0.5f;
    private ImageDirAdapter imageDirAdapter;

    public ImageDirListDialog(Context context, final List<ImageFolder> mImageFolders) {
        mContext = context;
        mDialog = new android.app.AlertDialog.Builder(context, R.style.camera_dialog_no_screen).create();
        WindowManager wmManager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        boolean hasSoft = NavigationBarUtil.hasSoftKeys(wmManager);//判断是否有虚拟键盘

        sDisplayMetrics = new DisplayMetrics();
        wmManager.getDefaultDisplay().getMetrics(sDisplayMetrics);
        int mScreenHeight = sDisplayMetrics.heightPixels;//获取屏幕的高度
        int dialogHeight = (int) (mScreenHeight * 0.7);//设置dialog的高度
        int gravityBottom = dp2px(42);
        if (hasSoft) {
            int navigationBarHeight = NavigationBarUtil.getNavigationBarHeight(context);//获取虚拟键盘的高度
            //这一行很重要，将dialog对话框设置在虚拟键盘上面
            DialogUtils.resetDialogScreenPosition(mDialog, Gravity.BOTTOM, 0, (navigationBarHeight + gravityBottom), WindowManager.LayoutParams.MATCH_PARENT, dialogHeight);
        } else {
            DialogUtils.resetDialogScreenPosition(mDialog, Gravity.BOTTOM, 0, gravityBottom, WindowManager.LayoutParams.MATCH_PARENT, dialogHeight);
        }

        mDialog.setCancelable(true);
        mDialog.setCanceledOnTouchOutside(true);
        mDialog.show();
        Window window = mDialog.getWindow();
        window.setContentView(R.layout.list_dir);
        window.setWindowAnimations(R.style.camera_dialog_enter_exit);  //添加dialog进入和退出的动画

        mListDir = (ListView) window.findViewById(R.id.id_list_dir);

        imageDirAdapter = new ImageDirAdapter(context);
        imageDirAdapter.setData(mImageFolders);
        mListDir.setAdapter(imageDirAdapter);

        mListDir.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (mImageDirSelected != null) {
                    mImageDirSelected.selectedImageFolder(mImageFolders.get(position));
                }
            }
        });
    }


    public void setOnImageDirSelected(OnImageDirSelected mImageDirSelected) {
        this.mImageDirSelected = mImageDirSelected;
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


    /**
     * dp 转 px
     * 注意正负数的四舍五入规则
     *
     * @param dp dp值
     * @return 转换后的像素值
     */
    public static int dp2px(int dp) {
        return (int) (dp * sDisplayMetrics.density + (dp > 0 ? ROUND_DIFFERENCE : -ROUND_DIFFERENCE));
    }
}
