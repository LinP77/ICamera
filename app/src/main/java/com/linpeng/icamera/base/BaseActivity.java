package com.linpeng.icamera.base;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.linpeng.icamera.AppManager;

/**
 * 》
 * 》Created on 15/10/25 下午2:35
 * 》
 */
public abstract  class BaseActivity<T extends BaseViewHolder> extends Activity implements View.OnClickListener{
    protected T mViewHolder = null;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppManager.getInstance().addActivity(this);
        useTranslucentBars(false);
    }

    /**
     * 通过类名启动Activity
     *
     * @param pClass
     */
    public void openActivity(Class<?> pClass) {
        openActivity(pClass, null);
    }

    /**
     * ͨ通过类名启动Activity，并且含有Bundle数据
     *
     * @param pClass
     * @param pBundle
     */
    public void openActivity(Class<?> pClass, Bundle pBundle) {
        Intent intent = new Intent(this, pClass);
        if (pBundle != null) {
            intent.putExtras(pBundle);

        }
        startActivity(intent);
    }

    /**
     * 通过类名启动Activity，并且Intent含有int数据
     *
     * @param pClass
     * @param name
     * @param value
     */
    public void openActivity (Class<?> pClass, String name, int value) {
        Intent intent = new Intent(this, pClass);
        intent.putExtra(name, value);
        startActivity(intent);
    }

    /**
     * 通过Action启动Activity
     *
     * @param pAction
     */
    public void openActivity(String pAction) {
        openActivity(pAction, null);
    }

    /**
     * 通过Action启动Activity，并且含有Bundle数据
     *
     * @param pAction
     * @param pBundle
     */
    public void openActivity(String pAction, Bundle pBundle) {
        Intent intent = new Intent(pAction);
        if (pBundle != null) {
            intent.putExtras(pBundle);
        }
        startActivity(intent);
    }

    public void openActivity (Class<?> pClass, String name, int value,Bundle pBundle) {
        Intent intent = new Intent(this, pClass);
        intent.putExtra(name, value);
        if (pBundle != null) {
            intent.putExtras(pBundle);
        }
        startActivity(intent);
    }

    /**
     * 提示toast消息
     *
     * @param str
     */
    public void toast(String str) {
        Toast.makeText(this, str, Toast.LENGTH_LONG).show();
    }

    /**
     * 提示toast消息
     *
     * @param msgResId
     */
    public void toast(int msgResId) {
        Toast.makeText(this, msgResId, Toast.LENGTH_LONG).show();
    }

    /**
     * 是否使用沉浸式顶栏（状态栏透明，只支持4.4版本以上）
     * @param isUse
     */
    public void useTranslucentBars(boolean isUse){
        if (isUse){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                Window window = getWindow();
                // Translucent status bar
                window.setFlags(
                        WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                        WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                // Translucent navigation bar
                window.setFlags(
                        WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION,
                        WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            }
        }
    }
}
