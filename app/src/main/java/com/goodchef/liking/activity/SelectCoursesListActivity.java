package com.goodchef.liking.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.aaron.android.framework.base.ui.actionbar.AppBarActivity;
import com.goodchef.liking.R;
import com.goodchef.liking.fragment.SelectCoursesListFragment;

/**
 * 说明:选择排课列表
 * Author : shaozucheng
 * Time: 下午2:08
 */

public class SelectCoursesListActivity extends AppBarActivity {

    public static final String KEY_SELECT_COURSES_ID = "selectcoursesid";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_courses_list);
        setTitle(getString(R.string.title_select_courses));
        setSelectCoursesFragment();
    }

    private void setSelectCoursesFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.select_courses_FrameLayout, SelectCoursesListFragment.newInstance());
        fragmentTransaction.commit();
    }
}
