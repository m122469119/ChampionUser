package com.goodchef.liking.module.card.my;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.aaron.android.framework.base.ui.actionbar.AppBarSwipeBackActivity;
import com.goodchef.liking.R;
import com.goodchef.liking.eventmessages.BuyCardSuccessMessage;
import com.goodchef.liking.module.card.buy.LikingBuyCardFragment;
import com.goodchef.liking.module.home.lessonfragment.LikingLessonFragment;

import static com.goodchef.liking.module.card.my.MyCardActivity.KEY_INTENT_TITLE;

/**
 * 说明:升级卡或者续卡
 * Author shaozucheng
 * Time:16/6/30 上午10:02
 */
public class UpgradeAndContinueCardActivity extends AppBarSwipeBackActivity{
    public static final String KEY_HIDE_TITLE = "hide_title";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upgrade_and_continue);

        int buyType = getIntent().getIntExtra(LikingBuyCardFragment.KEY_BUY_TYPE, 0);
        String gymId = getIntent().getStringExtra(LikingLessonFragment.KEY_GYM_ID);
        String title = getIntent().getStringExtra(KEY_INTENT_TITLE);
        boolean hideTitle = getIntent().getBooleanExtra(KEY_HIDE_TITLE,false);
        setTitle(title);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.upgrade_contains, LikingBuyCardFragment.newInstance(gymId, buyType,hideTitle))
                .commit();
    }

    @Override
    protected boolean isEventTarget() {
        return true;
    }

    public void onEvent(BuyCardSuccessMessage message) {
        if (message != null) {
            finish();
        }
    }
}
