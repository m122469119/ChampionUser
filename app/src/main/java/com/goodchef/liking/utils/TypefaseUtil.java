package com.goodchef.liking.utils;

import android.content.Context;
import android.graphics.Typeface;

/**
 * 说明:获取字体工具类
 * Author : shaozucheng
 * Time: 下午11:24
 * version 1.0.0
 */

public class TypefaseUtil {

    public static Typeface getImpactTypeface(Context context) {
        Typeface typeFace = Typeface.createFromAsset(context.getAssets(), "fonts/Impact.ttf");
        return typeFace;
    }

}
