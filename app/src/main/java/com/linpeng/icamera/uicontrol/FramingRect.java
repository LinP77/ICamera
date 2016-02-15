package com.linpeng.icamera.uicontrol;

import android.graphics.Point;
import android.graphics.Rect;

import com.linpeng.icamera.utils.ScreenUtils;

/**
 * @author Lin
 *
 */
public class FramingRect {
  public static Rect framingRect;
  public static int TOPOFFEST = 50;
  public static int LEFTOFFEST = 30;
  public static int POINTX;
  public static int POINTY;

  public static Rect getFramingRect() {
    if (framingRect == null) {
      int screenWidth = ScreenUtils.getScreenWidth();
      int screenHeight = ScreenUtils.getScreenHeight();
      if (screenWidth < screenHeight) {
        int temp = screenWidth;
        screenWidth = screenHeight;
        screenHeight = temp;
      }
      Point point = new Point(screenHeight, screenWidth);
      int width = point.x;
      int rightOffset = width-30;
      int bottomOffset = screenHeight;
      POINTX = (rightOffset-LEFTOFFEST)/2+LEFTOFFEST;
      POINTY = (bottomOffset-TOPOFFEST)/2+TOPOFFEST;
      framingRect = new Rect(LEFTOFFEST, TOPOFFEST, rightOffset, bottomOffset);
    }
    return framingRect;
  }

  public static Point getRectCenterPoint(){
    getFramingRect();
    return new Point(POINTX, POINTY);
  }
}
