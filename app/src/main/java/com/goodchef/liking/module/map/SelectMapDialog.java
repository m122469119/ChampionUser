package com.goodchef.liking.module.map;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.support.v7.app.AppCompatDialog;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aaron.android.framework.utils.DialogUtils;
import com.aaron.android.framework.utils.ResourceUtils;
import com.goodchef.liking.R;

/**
 * 说明:
 * Author : shaozucheng
 * Time: 下午6:53
 * version 1.0.0
 */

public class SelectMapDialog {


    private Context mContext;
    private AppCompatDialog mDialog;
    private LinearLayout mMapListLayout;
    private TextView mCancelTextView;
    private TextView mRecommendButton;
    private Double latitude, longitude;
    private Handler mHandler = new Handler();


    public SelectMapDialog(final Context context, Double latitude, Double longitude) {
        mContext = context;
        this.latitude = latitude;
        this.longitude = longitude;
        mDialog = new AppCompatDialog(context, R.style.camera_dialog_no_screen);
        mDialog.setCancelable(true);
        mDialog.setCanceledOnTouchOutside(true);
        mDialog.show();
        Window window = mDialog.getWindow();
        window.setContentView(R.layout.dialog_select_map);
        window.setWindowAnimations(R.style.my_dialog_enter_exit);  //添加dialog进入和退出的动画

        DialogUtils.resetDialogScreenPosition(mDialog, Gravity.BOTTOM, 0, 0,
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT);

        mMapListLayout = (LinearLayout) window.findViewById(R.id.dialog_map_layout);
        mCancelTextView = (TextView) window.findViewById(R.id.map_cancel);
        mRecommendButton = (TextView) window.findViewById(R.id.recommend_button);
        mCancelTextView.setOnClickListener(cancelListener);
        mRecommendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //HDefaultWebActivity.launch(context, "http://a.app.qq.com/o/simple.jsp?pkgname=com.autonavi.minimap", "");
               // gotoUrl();
                goToMarket(mContext,"com.autonavi.minimap");
                dismiss();
            }
        });
        loadMapView();
    }

    private void gotoUrl(){
        Intent intent = new Intent();
        intent.setAction("android.intent.action.VIEW");
        Uri content_url = Uri.parse("http://a.app.qq.com/o/simple.jsp?pkgname=com.autonavi.minimap");
        intent.setData(content_url);
        mContext.startActivity(intent);
    }

    public static void goToMarket(Context context, String packageName) {
        Uri uri = Uri.parse("market://details?id=" + packageName);
        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
        try {
            context.startActivity(goToMarket);
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
        }
    }



    private void loadMapView() {
        addAppMapView("使用百度地图导航", NavigationMap.NAVIGATION_MAP_PACKAGENAME_BAIDU, NavigationMap.MapType.BAIDU, mMapListLayout);
        addAppMapView("使用高德地图导航", NavigationMap.NAVIGATION_MAP_PACKAGENAME_GAODE, NavigationMap.MapType.GAODE, mMapListLayout);
        addAppMapView("使用腾讯地图导航", NavigationMap.NAVIGATION_MAP_PACKAGENAME_TENCENT, NavigationMap.MapType.TENCENT, mMapListLayout);

        if (mMapListLayout.getChildCount() == 0) {
            mRecommendButton.setVisibility(View.VISIBLE);
        } else {
            mRecommendButton.setVisibility(View.GONE);
        }
    }

    private void addAppMapView(String name, String packgename, NavigationMap.MapType type, LinearLayout layout) {
        if (NavigationMap.isPackageInstalled(mContext, packgename)) {
            layout.addView(addMapNameView(name, type));
        }
    }

    private TextView addMapNameView(String mapNmae, NavigationMap.MapType mapType) {
        TextView tv = new TextView(mContext);
        tv.setText(mapNmae);
        tv.setTag(mapType);
        tv.setGravity(Gravity.CENTER);
        tv.setTextColor(ResourceUtils.getColor(R.color.lesson_details_dark_back));
        tv.setPadding(20, 30, 20, 30);
        tv.setTextSize(18.0f);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        tv.setLayoutParams(layoutParams);
        tv.setOnClickListener(mOnClickListener);
        return tv;
    }

    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            NavigationMap.MapType type = (NavigationMap.MapType) v.getTag();
            if (type != null) {
                NavigationMap.navigationGoMap(mContext, type, latitude, longitude);
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        dismiss();
                    }
                }, 2000);
            }
        }
    };

    private View.OnClickListener cancelListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            dismiss();
        }
    };


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
