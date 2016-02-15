package com.linpeng.icamera.base;

import android.app.Application;
import android.content.Context;

import com.android.volley.RequestQueue;

/**
 * 》
 * 》Created on 15/10/24 下午7:32
 * 》
 */
public class KMApplication extends Application {
    private static Context context;
    private static RequestQueue mRequestQueue;
    private static KMApplication instance;

    public static KMApplication getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        context = getApplicationContext();
    }


    public static Context getAppContext() {
        return context;
    }

    /**
     * 当设备内存过低时，提醒垃圾回收机制进行数据回收
     */
    @Override
    public void onLowMemory() {
        super.onLowMemory();
        System.gc();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }

}
