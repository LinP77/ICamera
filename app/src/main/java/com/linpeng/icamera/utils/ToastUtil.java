package com.linpeng.icamera.utils;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.widget.Toast;

/**
 * 》
 * 》Created on 15/10/27 下午4:25
 * 》
 */
public class ToastUtil {
    private static final int length = 16;

    private static final int error = 0;

    private static final int info = 1;

    public static void toast(Context context, String text) {
        innerToast(context, text, info);
    }

    public static void toast(Context context, int resId) {
        toast(context, ResourcesUtils.getString(resId));
    }

    public static void toast(Fragment fragment, String text) {
        if (fragment != null && fragment.getActivity() != null) {
            toast(fragment.getActivity(), text);
        }
    }

    public static void toast(Fragment fragment, int resId) {
        toast(fragment, ResourcesUtils.getString(resId));
    }

    public static void toastError(Fragment fragment, String text) {
        innerToast(fragment, text, error);
    }

    public static void toastError(Fragment fragment, int resId) {
        toastError(fragment, ResourcesUtils.getString(resId));
    }

    public static void toastError(Context context, String text) {
        innerToast(context, text, error);
    }

    public static void toastError(Context context, int resId) {
        toastError(context, ResourcesUtils.getString(resId));
    }

    private static void innerToast(Fragment fragment, String text, int type) {
        if (fragment != null) {
            innerToast(fragment.getActivity(), text, type);
        }
    }

    private static void innerToast(Context context, String text, int type) {
        if (TextUtils.isEmpty(text) & context == null) {
            return;
        }
        try {
            Toast toast = Toast.makeText(context, text,
                    text.length() > length ? Toast.LENGTH_LONG
                            : Toast.LENGTH_SHORT);
            toast.show();
        } catch (Exception ignored) {

        }
    }
}
