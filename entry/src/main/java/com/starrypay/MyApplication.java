package com.starrypay;

import com.starrypay.http.HttpUtils;
import com.starrypay.login.HuaweiLoginManager;
import com.starrypay.utils.LocalConfigUtils;
import com.starrypay.utils.ToastUtils;
import ohos.aafwk.ability.AbilityPackage;

public class MyApplication extends AbilityPackage {


    @Override
    public void onInitialize() {
        super.onInitialize();

        LocalConfigUtils.init(this);
        ToastUtils.initAppContenxt(this);
//        HttpUtils.init("http://hm.starrypay.com/");
        HttpUtils.init("http://192.168.68.23:8022");

        HuaweiLoginManager.getInstance().initSdk(this);
    }

}
