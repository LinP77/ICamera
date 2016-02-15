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
  public static int framingRect_center_leftOffset;
  public static int framingRect_center_topOffset;
  public Point centerPoint;

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
      int height = point.y;
      int leftOffset = 30;
      int topOffset = 50;
      int rightOffset = width-30;
      int bottomOffset = screenHeight;
      framingRect_center_leftOffset = (rightOffset-leftOffset)/2+leftOffset;
      framingRect_center_topOffset = (bottomOffset-topOffset)/2+topOffset;
      framingRect = new Rect(leftOffset, topOffset, rightOffset, bottomOffset);
    }
    return framingRect;
  }

  public static Point getRectCenterPoint(){
    getFramingRect();
    return new Point(framingRect_center_leftOffset, framingRect_center_topOffset);
  }
}
