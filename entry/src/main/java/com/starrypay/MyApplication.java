package com.starrypay;

import com.starrypay.http.HttpUtils;
import ohos.aafwk.ability.AbilityPackage;

public class MyApplication extends AbilityPackage {
    @Override
    public void onInitialize() {
        super.onInitialize();

        HttpUtils.init("http://hm.starrypay.com/");
    }
}
