package com.waiterxiaoyy.backandroiddesign.utils.util;

import java.io.UnsupportedEncodingException;

/**
 * @author :WaiterXiaoYY
 * @description: TODO
 * @data :2020/12/5 18:11
 */
public class UnicodeUtil {
    public static String convertEncodingFormat(String str, String formatFrom, String FormatTo) {
        String result = null;
        if (!(str == null || str.length() == 0)) {
            try {
                result = new String(str.getBytes(formatFrom), FormatTo);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return result;
    }
}
