package com.linpeng.icamera.utils;

import android.content.res.ColorStateList;

import com.linpeng.icamera.base.KMApplication;


/**
 * 》
 * 》Created on 15/10/27 下午4:26
 * 》
 */
public class ResourcesUtils {
    public static String getString(int id) {
        return KMApplication.getAppContext().getResources().getString(id);
    }

    public static String getString(int id, Object... formatArgs) {
        return KMApplication.getAppContext().getString(id, formatArgs);
    }

    public static int getColor(int id) {
        return KMApplication.getAppContext().getResources().getColor(id);
    }

    public static ColorStateList getColorStateList(int id) {
        return KMApplication.getAppContext().getResources().getColorStateList(id);
    }
}
