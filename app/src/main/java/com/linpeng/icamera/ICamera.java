package com.linpeng.icamera;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Point;
import android.hardware.Camera;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;

import com.linpeng.icamera.base.BaseActivity;
import com.linpeng.icamera.uicontrol.FramingRect;

public class ICamera extends BaseActivity<ICameraViewHolder> {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewHolder = new ICameraViewHolder(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ib_back:
            case R.id.rl_ib_back:
                finish();
                break;
            case R.id.write:
                mViewHolder.write();
                break;
            case R.id.confirm:
                mViewHolder.confirm();
                break;
            case R.id.pic:
                mViewHolder.selectPic();
                break;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //只处理规定区域的点击事件 FIXME 该区域需要优化
        if (event.getY() < 2 * FramingRect.getRectCenterPoint().y) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {// 按下时自动对焦
                // 设置聚焦
                mViewHolder.isTouching = true;
                mViewHolder.sm.unregisterListener(mViewHolder);
                Point point = mViewHolder.centerPoint;
                if (mViewHolder.isShowFocus) {
                    mViewHolder.mAutoFocus = false;
                    mViewHolder.focusImageView.startFocus(point);
                }
                mViewHolder.camera.autoFocus(new Camera.AutoFocusCallback() {

                    @Override
                    public void onAutoFocus(boolean success, Camera camera) {
                        if (success) {
                            mViewHolder.isTouching = false;
                            mViewHolder.mAutoFocus = true;
                            mViewHolder.focusImageView.onFocusSuccess();
                        } else {
                            mViewHolder.isTouching = false;
                            // 聚焦失败显示的图片，由于未找到合适的资源，这里仍显示同一张图片
                            mViewHolder.focusImageView.onFocusFailed();
                        }
                    }
                });
            }
            if (event.getAction() == MotionEvent.ACTION_UP) {
                if(mViewHolder.mHandler != null){
                    mViewHolder.mHandler.postDelayed(new Runnable() {

                        @Override
                        public void run() {
                            // 在传感器管理器中注册监听器
                            mViewHolder.sm.registerListener(mViewHolder, mViewHolder.acceleromererSensor,
                                    SensorManager.SENSOR_DELAY_NORMAL);
                        }
                    }, 3000);

                }
            }
        }
        return true;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            mViewHolder.handlePic(requestCode, data);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

}
