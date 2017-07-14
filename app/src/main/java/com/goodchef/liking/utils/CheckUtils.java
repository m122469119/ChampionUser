package com.goodchef.liking.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 * Created on 2017/7/7
 * Created by sanfen
 *
 * @version 1.0.0
 */

public class CheckUtils {
    public static String replaceSpecialCharacter(String str) throws PatternSyntaxException {
        String regEx = "[^a-zA-Z0-9\\u4E00-\\u9FA5-_]"; //要过滤掉的字符
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        return m.replaceAll("").trim();
    }

}
