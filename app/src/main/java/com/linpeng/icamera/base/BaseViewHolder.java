package com.linpeng.icamera.base;

/**
 * 》
 * 》Created on 15/10/25 下午3:05
 * 》
 */
public abstract class BaseViewHolder {
    protected abstract void setContentView();
    protected abstract void findViewId();
    protected abstract void registerClickListener();

    protected  void init(){
        setContentView();
        findViewId();
        registerClickListener();
    };



}
