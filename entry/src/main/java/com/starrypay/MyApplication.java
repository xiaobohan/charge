package com.starrypay;

import com.huawei.hms.accountsdk.exception.ApiException;
import com.huawei.hms.accountsdk.support.account.AccountAuthManager;
import com.huawei.hms.accountsdk.support.account.tasks.OnFailureListener;
import com.huawei.hms.accountsdk.support.account.tasks.OnSuccessListener;
import com.huawei.hms.accountsdk.support.account.tasks.Task;
import com.huawei.log.Logger;
import com.starrypay.http.HttpUtils;
import com.starrypay.utils.LocalConfigUtils;
import com.starrypay.utils.ToastUtils;
import ohos.aafwk.ability.AbilityPackage;

public class MyApplication extends AbilityPackage {
    @Override
    public void onInitialize() {
        super.onInitialize();

        LocalConfigUtils.init(this);
        ToastUtils.initAppContenxt(this);
        HttpUtils.init("http://hm.starrypay.com/");

        initHuaweiAccountSDK();
    }

    // 示例：此方法中调用华为帐号SDK的初始化方法AccountAuthManager.init()进行初始化
    private void initHuaweiAccountSDK() {
        Task<Void> task;
        try {
            // 调用AccountAuthManager.init方法初始化
            task = AccountAuthManager.init(this);
        } catch (ApiException apiException) {
            apiException.getStatusCode();
            return;
        }
        task.addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void v) {
                Logger.i("hahaha","onSuccess",true);
            }
        });

        task.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception e) {
                // SDK初始化失败
                if (e instanceof ApiException) {
                    ApiException apiException = (ApiException) e;
                    // SDK初始化失败，status code标识了失败的原因，请参考API中的错误码参考了解详细错误原因
                    apiException.getStatusCode();
                }
            }
        });
    }

}
