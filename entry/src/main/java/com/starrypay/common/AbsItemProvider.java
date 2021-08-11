package com.starrypay.common;

import com.starrypay.bean.RechargeRecordBean;

import java.util.ArrayList;
import java.util.List;

public abstract class AbsItemProvider<T> extends ohos.agp.components.BaseItemProvider {

    public List<T> dataList = new ArrayList<>();

    @Override
    public int getCount() {
        return dataList.size();
    }

    public void refresh(List<T> list) {
        this.dataList = list;
        notifyDataChanged();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

}
