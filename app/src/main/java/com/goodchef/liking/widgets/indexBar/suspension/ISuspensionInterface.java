package com.goodchef.liking.widgets.indexBar.suspension;

import java.io.Serializable;

/**
 * 说明:分类悬停的接口
 * Author : shaozucheng
 * Time: 上午10:58
 * version 1.0.0
 */

public interface ISuspensionInterface  extends Serializable{

    //是否需要显示悬停title
    boolean isShowSuspension();

    //悬停的title
    String getSuspensionTag();
}
