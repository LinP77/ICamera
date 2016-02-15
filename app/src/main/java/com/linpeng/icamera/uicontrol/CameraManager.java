package com.linpeng.icamera.uicontrol;

import android.hardware.Camera.Size;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * 》
 * 》Created on 15/11/17 下午2:42
 * 》
 */
public class CameraManager {
    private static final String tag = "Tag";
    private CameraSizeComparator sizeComparator = new CameraSizeComparator();
    private static CameraManager manager = null;
    private CameraManager(){

    }
    public static CameraManager getInstance(){
        if(manager == null){
            manager = new CameraManager();
            return manager;
        }
        else{
            return manager;
        }
    }

    public  Size getPreviewSize(List<Size> list, int th){
        Collections.sort(list, sizeComparator);

        int i = 0;
        for(Size s:list){
            if((s.width > th) && equalRate(s, 1.33f)){
                //Log.i(tag, "最终设置预览尺寸:w = " + s.width + "h = " + s.height);
                break;
            }
            i++;
            if (i>=list.size()){
                i = list.size()-1;
            }
        }

        return list.get(i);
    }
    public Size getPictureSize(List<Size> list, int th){
        Collections.sort(list, sizeComparator);

        int i = 0;
        for(Size s:list){
            if((s.width > th) && equalRate(s, 1.33f)){
                //Log.i(tag, "最终设置图片尺寸:w = " + s.width + "h = " + s.height);
                break;
            }
            i++;
            if (i>=list.size()){
                i = list.size()-1;
            }
        }

        return list.get(i);
    }

    public boolean equalRate(Size s, float rate){
        float r = (float)(s.width)/(float)(s.height);
        if(Math.abs(r - rate) <= 0.2)
        {
            return true;
        }
        else{
            return false;
        }
    }

    public  class CameraSizeComparator implements Comparator<Size> {
        //按升序排列
        public int compare(Size lhs, Size rhs) {
            // TODO Auto-generated method stub
            if(lhs.width == rhs.width){
                return 0;
            }
            else if(lhs.width > rhs.width){
                return 1;
            }
            else{
                return -1;
            }
        }

    }

   /* List<Camera.Size> size_list = parameters.getSupportedPreviewSizes();

    if (size_list.size() > 0) {
        Iterator<Size> itor = size_list.iterator();
        while (itor.hasNext()) {
            Camera.Size cur = itor.next();
            LogUtils.e("getSupportedPreviewSizes", "cur.width=" + cur.width + "---cur.height=" + cur.height + "\n");
            if (cur.width >= PreviewWidth && cur.height >= PreviewHeight) {
                PreviewWidth = cur.width;
                PreviewHeight = cur.height;
                // break;
            }
        }
    }
    LogUtils.e("initcamea", "PreviewWidth = " + PreviewWidth + "PreviewHeight = " + PreviewHeight);
    //parameters.setPreviewSize(PreviewWidth, PreviewHeight);// 设置预览照片的大小
    List<Camera.Size> picSizeValues = camera.getParameters()
            .getSupportedPictureSizes();
    if (picSizeValues.get(0).width > picSizeValues
            .get(picSizeValues.size() - 1).width) {
        for (Camera.Size size : picSizeValues) {
            LogUtils.e("getSupportedPictureSizes0", "size.width="+size.width + "size.height="+size.height+"\n");
            if (size.width <= 1024) {
                // parameters.setPictureSize(size.width, size.height);// 设置照片的大小，默认是和
                break;
            }
        }
    } else {
        for (int i = picSizeValues.size() - 1; i >= 0; i -= 1) {
            LogUtils.e("getSupportedPictureSizes1", "picSizeValues.get(i).width="+picSizeValues.get(i).width + "picSizeValues.get(i).height="+picSizeValues.get(i).height+"\n");
            if (picSizeValues.get(i).width <= 1024) {
                   *//*parameters.setPictureSize(picSizeValues.get(i).width,
                            picSizeValues.get(i).height);*//*
                break;
            }
        }
    }*/
}
