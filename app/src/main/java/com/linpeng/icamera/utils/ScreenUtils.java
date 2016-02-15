package com.linpeng.icamera.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

import com.linpeng.icamera.base.KMApplication;

/**
 * 获取屏幕相关参数的工具类
 * @author Lin
 *
 */
public class ScreenUtils {
	
	/**
	 * 获取屏幕的宽度
	 * 
	 * @return
	 */
	public static int getScreenWidth() {
		WindowManager manager = (WindowManager) KMApplication.getAppContext().getSystemService(Context.WINDOW_SERVICE);
		Display display = manager.getDefaultDisplay();
		return display.getWidth();
	}

	/**
	 * 获取屏幕的高度
	 * 
	 * @return
	 */
	public static int getScreenHeight() {
		WindowManager manager = (WindowManager) KMApplication.getAppContext().getSystemService(Context.WINDOW_SERVICE);
		Display display = manager.getDefaultDisplay();
		return display.getHeight();
	}
	
	 /**
     * Note:
     * 只有activity可以使用getWindowManager，否则应该使用
     * Context.getResources().getDisplayMetrics()来获取
    */
     
    /**
     * 获取DisplayMetric相关参数
     * @param context
     * @return
     */
    public static String getMetricParams(Activity context){
        DisplayMetrics dm = new DisplayMetrics();
        context.getWindowManager().getDefaultDisplay().getMetrics(dm);
        return "density:"+dm.density+";densityDpi:"+dm.densityDpi
            +";height:"+dm.heightPixels+";width:"+dm.widthPixels
            +";scaledDensity:"+dm.scaledDensity+";xdpi:"+dm.xdpi
            +";ydpi:"+dm.ydpi;
    }
     
    /**
     * 获取屏幕尺寸，单位为像素
     * @param context
     * @return
     */
    public static String getScreenSizeInInPixels(Activity context){
        DisplayMetrics dm = new DisplayMetrics();
        context.getWindowManager().getDefaultDisplay().getMetrics(dm);
        double heightInInPixels = (double)dm.heightPixels;
        double widthInInPixels = (double)dm.widthPixels;
        return "高："+heightInInPixels+" 宽："+widthInInPixels+" 单位（像�?";
    }

    /**
     * 获取屏幕尺寸，单位为英寸
     * 计算屏幕尺寸应该使用精确密度：xdpi ydpi来计算
     * 使用归一化密度：densitydpi是错误的，它是固定值，
     * 120 160 240 320 480,根据dp计算像素才使用它
     * @param context
     * @return
     */
    public static String getScreenSizeInInch(Activity context){
        DisplayMetrics dm = new DisplayMetrics();
        context.getWindowManager().getDefaultDisplay().getMetrics(dm);
        double heightInInch = (double)dm.heightPixels / (double)dm.ydpi;
        double widthInInch = (double)dm.widthPixels / (double)dm.xdpi;
        double ScrrenSizeInInch = Math.sqrt(heightInInch * heightInInch
                + widthInInch * widthInInch);
        return "高："+heightInInch+" 宽："+widthInInch+" 尺寸:"+ScrrenSizeInInch
                +" 单位（英�?";
    }
     
    /**
     * 获取屏幕尺寸，单位为dp
     * @param context
     * @return
     */
    public static String getScreenSizeInInDp(Activity context){
        DisplayMetrics dm = new DisplayMetrics();
        context.getWindowManager().getDefaultDisplay().getMetrics(dm);
        float heightInInDp = px2dip((Context)context, (float)dm.heightPixels);
        float widthInInDp = px2dip((Context) context, (float) dm.widthPixels);
        return "高："+heightInInDp+" 宽："+widthInInDp+" 单位（dp)";
    }
     
    /**
     * dp转px
     * @param context
     * @param dpValue
     * @return
     */
    public static int dip2px (Context context, float dpValue){
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int)(dpValue*scale+0.5f);
    }
     
    /**
     * px转dp
     * @param context
     * @param pxValue
     * @return
     */
    public static int px2dip(Context context, float pxValue){
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int)(pxValue/scale+0.5f);
    }

    public static Point getScreenMetrics(Context context){
        Point point = new Point(getScreenWidth(),getScreenHeight());
        return point;
    }
     
}
