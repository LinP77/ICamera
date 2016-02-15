package com.linpeng.icamera.base;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

/**
 * 》      ╭∩╮(︶︿︶）╭∩╮
 * 》
 * 》Created on 15/10/29 下午3:29
 * 》
 */
public abstract class KMAdapter<T> extends BaseAdapter {
    public Context context;
    public List<T> list;

    public KMAdapter(Context context, List<T> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list == null ? 0 : list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public abstract View getView(int position, View convertView, ViewGroup parent);
}
