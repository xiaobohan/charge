package com.starrypay.http;

import com.starrypay.bean.BaseRespBean;

public interface RequestCallback<T> {

    void onSuccess(T respBean);

    void onFailed(int code, Throwable e);

}
