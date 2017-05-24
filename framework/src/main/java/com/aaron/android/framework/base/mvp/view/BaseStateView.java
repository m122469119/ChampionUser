package com.aaron.android.framework.base.mvp.view;

import com.aaron.android.framework.base.widget.refresh.StateView;

/**
 * Created on 17/5/18.
 *
 * @author aaron.huang
 * @version 1.0.0
 */

public interface BaseStateView extends BaseView{
    void changeStateView(StateView.State state);
}
