package com.goodchef.liking.mvp.presenter;

import android.content.Context;

import com.aaron.android.framework.base.mvp.BasePresenter;
import com.goodchef.liking.http.result.data.HomeAnnouncement;
import com.goodchef.liking.mvp.view.LikingHomeView;
import com.goodchef.liking.storage.Preference;

/**
 * Created on 2017/2/28
 * Created by sanfen
 *
 * @version 1.0.0
 */

public class LikingHomePresenter extends BasePresenter<LikingHomeView> {

    public LikingHomePresenter(Context context, LikingHomeView mainView) {
        super(context, mainView);
    }

    public void showPushDialog() {
        if (Preference.isHomeAnnouncement()) {
            HomeAnnouncement homeAnnouncement = Preference.getHomeAnnouncement();
            mView.showNoticesDialog(homeAnnouncement.getNoticeDatas());
            Preference.clearHomeAnnouncement();
        }
    }
}
