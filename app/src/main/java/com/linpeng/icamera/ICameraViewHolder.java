package com.linpeng.icamera;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.hardware.Camera;
import android.hardware.Camera.Size;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.linpeng.icamera.base.BaseActivity;
import com.linpeng.icamera.base.BaseViewHolder;
import com.linpeng.icamera.uicontrol.CameraManager;
import com.linpeng.icamera.uicontrol.FocusImageView;
import com.linpeng.icamera.uicontrol.FramingRect;
import com.linpeng.icamera.uicontrol.ViewfinderView;
import com.linpeng.icamera.utils.BitmapUtils;
import com.linpeng.icamera.utils.ToastUtil;
import com.linpeng.icamera.utils.UploadUtil;
import com.linpeng.icamera.widgets.KMDialog;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.lang.reflect.Method;
import java.util.List;

/**
 * 》
 * 》Created on 15/10/27 上午10:44
 * 》
 */
public class ICameraViewHolder extends BaseViewHolder implements SurfaceHolder.Callback, SensorEventListener {
    private BaseActivity activity;
    private ImageButton ib_back;
    private TextView title;
    private boolean enableModify;
    private Bundle bundle;
    private ViewfinderView viewfinderView;
    private SurfaceView surfaceView;
    private Button confirm;
    private TextView write;
    private TextView pic;
    private RelativeLayout rl_ib_back;
    public SensorManager sm;
    public Sensor acceleromererSensor;
    public FocusImageView focusImageView;
    private float mLastX = 0;
    private float mLastY = 0;
    private float mLastZ = 0;
    public Point centerPoint;
    public Camera camera;
    private Camera.Parameters parameters;
    private SurfaceHolder surfaceHolder;
    private static String IMG_PATH = getSDPath() + File.separator
            + "DriviLicense" + File.separator;
    public Handler mHandler;
    public boolean mAutoFocus = true;
    public boolean isTouching = false;
    public boolean isShowFocus = true;//是否显示聚焦图
    private boolean mInitialized = false;
    /**
     * 摇晃检测阈值，决定了对摇晃的敏感程度，越小越敏感。
     */
    private int shakeThreshold = 100;
    /**
     * 检测的时间间隔
     */
    private static final int UPDATE_INTERVAL = 100;
    /**
     * 上一次检测的时间
     */
    private long mLastUpdateTime;
    /***
     * 使用相册中的图片
     */
    private static final int SELECT_PIC_BY_PICK_PHOTO = 1;
    private Uri photoUri;
    /**
     * 获取到的图片路径
     */
    private String picPath;

    private ProgressDialog dialog;

    private final static String PROCESS_ERROR_TEXT = "解析失败,请确认行驶证是否拍摄完整!";
    private final static int DIALOG_SHOW_CODE = 000;
    private final static int DIALOG_DISMISS_CODE = 001;
    private final static int TOAST_SHOW_CODE = 002;
    private final static int PROCESS_ERROR_CODE = 003;
    private final static int PROCESS_SUCCESS_CODE = 004;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case DIALOG_SHOW_CODE:
                    if (dialog != null)
                        dialog.show();
                    break;
                case DIALOG_DISMISS_CODE:
                    if (dialog != null)
                        dialog.dismiss();
                    break;
                case TOAST_SHOW_CODE:
                    if (dialog != null)
                        dialog.dismiss();
                    ToastUtil.toast(activity, PROCESS_ERROR_TEXT);
                    if (camera != null)
                        camera.startPreview();
                    sm.registerListener(ICameraViewHolder.this, acceleromererSensor,
                            SensorManager.SENSOR_DELAY_NORMAL);
                    break;
                case PROCESS_ERROR_CODE:
                    if (dialog != null)
                        dialog.dismiss();
                    ToastUtil.toast(activity, PROCESS_ERROR_TEXT);
                    if (camera != null)
                        camera.startPreview();
                    sm.registerListener(ICameraViewHolder.this, acceleromererSensor,
                            SensorManager.SENSOR_DELAY_NORMAL);
                    break;
                default:
                    super.handleMessage(msg);//这里最好对不需要或者不关心的消息抛给父类，避免丢失消息
                    break;
            }
        }
    };

    public ICameraViewHolder(BaseActivity activity) {
        this.activity = activity;
        init();
    }

    @Override
    protected void init() {
        super.init();
        title.setText("添加车辆");
        ib_back.setVisibility(View.VISIBLE);
        dialog = KMDialog.ProgressDialog(activity, "正在解析...");
        centerPoint = FramingRect.getRectCenterPoint();
        initSensor();
        initSurface();
    }

    @Override
    protected void setContentView() {
        activity.setContentView(R.layout.activity_addcar);
    }

    @Override
    protected void findViewId() {
        ib_back = (ImageButton) activity.findViewById(R.id.ib_back);
        rl_ib_back = (RelativeLayout) activity.findViewById(R.id.rl_ib_back);
        title = (TextView) activity.findViewById(R.id.top_title);
        viewfinderView = (ViewfinderView) activity.findViewById(R.id.viewfinder);
        surfaceView = (SurfaceView) activity.findViewById(R.id.surfaceview);
        focusImageView = (FocusImageView) activity.findViewById(R.id.iv_focus);
        write = (TextView) activity.findViewById(R.id.write);
        pic = (TextView) activity.findViewById(R.id.pic);
        confirm = (Button) activity.findViewById(R.id.confirm);
    }

    @Override
    protected void registerClickListener() {
        ib_back.setOnClickListener(activity);
        rl_ib_back.setOnClickListener(activity);
        confirm.setOnClickListener(activity);
        write.setOnClickListener(activity);
        pic.setOnClickListener(activity);
    }

    public void write() {
        Bundle bundle = new Bundle();
        bundle.putBoolean("isFromPersonal", false);
        //activity.openActivity(ManualAddCar.class, bundle);
        Toast.makeText(activity,"test",Toast.LENGTH_SHORT).show();
    }

    public void selectPic() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        activity.startActivityForResult(intent, SELECT_PIC_BY_PICK_PHOTO);
    }

    /**
     * 选择图片后，获取图片的路径
     *
     * @param requestCode
     * @param data
     */
    public void handlePic(int requestCode, Intent data) {
        if (requestCode == SELECT_PIC_BY_PICK_PHOTO)  //从相册取图片，有些手机有异常情况，请注意
        {
            if (data == null) {
                Toast.makeText(activity, "选择图片文件出错", Toast.LENGTH_LONG).show();
                return;
            }
            photoUri = data.getData();
            if (photoUri == null) {
                Toast.makeText(activity, "选择图片文件出错", Toast.LENGTH_LONG).show();
                return;
            }
        }
        String[] pojo = {MediaStore.Images.Media.DATA};
        Cursor cursor = activity.managedQuery(photoUri, pojo, null, null, null);
        if (cursor != null) {
            int columnIndex = cursor.getColumnIndexOrThrow(pojo[0]);
            cursor.moveToFirst();
            picPath = cursor.getString(columnIndex);
            if (Build.VERSION.SDK_INT < 14) {
                cursor.close();
            }
        }
        if (picPath != null && (picPath.endsWith(".png") || picPath.endsWith(".PNG") || picPath.endsWith(".jpg") || picPath.endsWith(".JPG"))) {
            //解析并跳转
            Bitmap bitmap = BitmapUtils.getSmallBitmap(picPath, 2);
            String file = UploadUtil.Bitmap2StrByBase64(bitmap);
            ThreadUpload(file);
        } else {
            Toast.makeText(activity, "选择图片文件不正确", Toast.LENGTH_LONG).show();
        }
    }




    /**
     * 自动聚焦
     */
    private void autoFocus() {
        if (null != camera) {
            // 设置聚焦
            Point point = centerPoint;
            if (point != null) {
                if (isShowFocus) {
                    mAutoFocus = false;
                    focusImageView.startFocus(point);
                }
            }
            setCameraFocus(autoFocusCallback);
        }
    }

    private void setCameraFocus(Camera.AutoFocusCallback autoFocus) {
        if (camera.getParameters().getFocusMode().equals(camera.getParameters().FOCUS_MODE_AUTO) ||
                camera.getParameters().getFocusMode().equals(camera.getParameters().FOCUS_MODE_MACRO)) {
            camera.autoFocus(autoFocus);
        }
    }

    private Camera.AutoFocusCallback autoFocusCallback = new Camera.AutoFocusCallback() {

        @Override
        public void onAutoFocus(boolean success, Camera camera) {
            if (success) {
                //camera.cancelAutoFocus();
                focusImageView.onFocusSuccess();
                mAutoFocus = true;
            } else {
                // 聚焦失败显示的图片，由于未找到合适的资源，这里仍显示同一张图片
                focusImageView.onFocusFailed();
            }
        }
    };

    /**
     * 拍照
     */
    public void confirm() {
        Toast.makeText(activity,"test",Toast.LENGTH_SHORT).show();
       /* sm.unregisterListener(this);
        if (null != camera) {
            // 自动聚焦
            camera.autoFocus(new Camera.AutoFocusCallback() {

                @Override
                public void onAutoFocus(boolean success, Camera camera) {
                    // 聚焦完成, 开始捕获画面
                    camera.takePicture(null, null, new Camera.PictureCallback() {

                        @Override
                        public void onPictureTaken(byte[] data, Camera camera) {
                            // 图片拍摄完成, data就是图片的数据
                            try {
                                Bitmap bm = BitmapFactory.decodeByteArray(data, 0,
                                        data.length);
                                boolean isSuccess = savePicFile(bm,"license.jpg");
                                if (isSuccess) {
                                    String filePath = IMG_PATH + "license.jpg";
                                    Bitmap bitmap = BitmapUtils.getSmallBitmap(filePath, 1.5);
                                    String s_file = UploadUtil.Bitmap2StrByBase64(bitmap);
                                    ThreadUpload(s_file);
                                } else {
                                    ToastUtil.toast(activity, "拍摄失败,请重试!");
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
            });
        }*/
    }

    private boolean savePicFile(Bitmap bitmap, String fileName) {
        File file1 = new File(IMG_PATH);
        if (!file1.exists()) {
            file1.mkdirs();
        }
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(
                    IMG_PATH + fileName);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return  bitmap.compress(
                Bitmap.CompressFormat.JPEG, 100, fos);
    }

    private void ThreadUpload(final String filePath){

    }

    /**
     * 通过定时器聚焦
     */
    private void postDelayed() {
        mHandler.postDelayed(new Runnable() {
            public void run() {

                // 设置聚焦
                Point point = centerPoint;
                if (isShowFocus) {
                    focusImageView.startFocus(point);
                }

                if (camera != null) {
                    camera.cancelAutoFocus();
                    camera.autoFocus(new Camera.AutoFocusCallback() {

                        @Override
                        public void onAutoFocus(boolean success, Camera camera) {
                            if (success) {
                                focusImageView.onFocusSuccess();
                            } else {
                                // 聚焦失败显示的图片，由于未找到合适的资源，这里仍显示同一张图片
                                focusImageView.onFocusFailed();
                            }
                        }

                    });
                }
                mHandler.postDelayed(this, 3000);
            }
        }, 3000);

    }

    private void initSensor() {
        // 获取传感器管理器
        sm = (SensorManager) activity.getSystemService(Context.SENSOR_SERVICE);
        // 获取加速度传感器
        acceleromererSensor = sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
    }

    private void initSurface() {
        surfaceHolder = surfaceView.getHolder();
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        surfaceHolder.setKeepScreenOn(true);
        surfaceView.setFocusable(true);
        surfaceView.setBackgroundColor(activity.TRIM_MEMORY_BACKGROUND);
        surfaceHolder.addCallback(this);
    }

    // 相机参数的初始化设置
    public void initCamera() {
        parameters = camera.getParameters();
        parameters.setPictureFormat(PixelFormat.JPEG);
        List<Size> size_list = parameters.getSupportedPreviewSizes();
        List<Size> picSizeValues = camera.getParameters()
                .getSupportedPictureSizes();
        int surfaceViewHeight = surfaceView.getHeight();
        Size pictureS = CameraManager.getInstance().getPictureSize(picSizeValues, surfaceViewHeight);
        Size preViewS = CameraManager.getInstance().getPreviewSize(size_list, surfaceViewHeight);
        parameters.setPreviewSize(preViewS.width, preViewS.height);// 设置预览照片的大小
        parameters.setPictureSize(pictureS.width, pictureS.height);
        // parameters.setFlashMode(Parameters.FLASH_MODE_TORCH);
        parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
        parameters.set("rotation", 90);
        parameters.setJpegQuality(100);// 设置照片的质量
        setDispaly(parameters, camera);
        camera.setParameters(parameters);
        camera.startPreview();
        camera.cancelAutoFocus();
    }


    // 控制图像的正确显示方向
    private void setDispaly(Camera.Parameters parameters, Camera camera) {
        if (Integer.parseInt(Build.VERSION.SDK) >= 8) {
            setDisplayOrientation(camera, 90);
        } else {
            parameters.setRotation(90);
        }

    }

    // 实现的图像的正确显示
    private void setDisplayOrientation(Camera camera, int i) {
        Method downPolymorphic;
        try {
            downPolymorphic = camera.getClass().getMethod(
                    "setDisplayOrientation", new Class[]{int.class});
            if (downPolymorphic != null) {
                downPolymorphic.invoke(camera, new Object[]{i});
            }
        } catch (Exception e) {
            Log.e("Came_e", "图像出错");
        }
    }


    /**
     * 获取sd卡的路径
     *
     * @return 路径的字符串
     */
    public static String getSDPath() {
        String path = "";
        boolean sdCardExist = Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED); // 判断sd卡是否存在
        if (sdCardExist) {
            File sdDir = Environment.getExternalStorageDirectory();// 获取外存目录
            path = sdDir.toString();
        }
        return path;
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (null == camera) {
            // 打开摄像头, 开始预览画面效果
            try {
                camera = Camera.open();
            } catch (Exception e) {
                ToastUtil.toast(activity, "您拒绝了拍照权限，此处无法获取系统相机！请重新安装app并授予拍照权限！");
                focusImageView.setVisibility(View.GONE);
                e.printStackTrace();
            }
            if (null != camera) {
                try {
                    // 在传感器管理器中注册监听器
                    sm.registerListener(ICameraViewHolder.this, acceleromererSensor,
                            SensorManager.SENSOR_DELAY_NORMAL);
                    camera.setPreviewDisplay(surfaceHolder);
                    initCamera();
                    // 开始预览
                    camera.startPreview();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        if (null != camera) {
            if (isShowFocus) {
                focusImageView.startFocus(centerPoint);
            }
            // 实现自动对焦
            setCameraFocus(autoFocusCallback);
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        if (null != camera) {
            // 停止预览效果, 关闭摄像头
            camera.stopPreview(); // 停止预览
            camera.release();
            camera = null;
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        long currentTime = System.currentTimeMillis();
        long diffTime = currentTime - mLastUpdateTime;
        if (diffTime < UPDATE_INTERVAL)
            return;
        mLastUpdateTime = currentTime;
        float x = event.values[0];
        float y = event.values[1];
        float z = event.values[2];
        if (!mInitialized) {
            mLastX = x;
            mLastY = y;
            mLastZ = z;
            mInitialized = true;
        }
        float deltaX = Math.abs(mLastX - x);
        float deltaY = Math.abs(mLastY - y);
        float deltaZ = Math.abs(mLastZ - z);

        boolean isDeltaX = (deltaX > .5 && deltaX < .8);
        boolean isDeltaY = (deltaY > .5 && deltaY < .8);
        boolean isDeltaZ = (deltaZ > .5 && deltaZ < .8);
        float delta = (float) ((Math.sqrt(deltaX * deltaX + deltaY * deltaY + deltaZ * deltaZ)) / diffTime * 10000);

        if (delta < shakeThreshold) { // 当加速度的差值大于指定的阈值，认为这是一个摇晃
            if (isDeltaX && !isDeltaY && !isDeltaZ && mAutoFocus && !isTouching) {
                autoFocus();

            }
            if (isDeltaY && !isDeltaX && !isDeltaZ && mAutoFocus && !isTouching) {
                autoFocus();
            }
            if (isDeltaZ && !isDeltaX && !isDeltaY && mAutoFocus && !isTouching) {
                autoFocus();
            }
        }

        mLastX = x;
        mLastY = y;
        mLastZ = z;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // 如果传感器精度发生变化，可以在这里完成些工作。
    }

}
