package com.starrypay.slice;

import com.starrypay.myapplication.ResourceTable;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.agp.components.Component;

public class PrivacyAbilitySlice extends AbilitySlice {

    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_page_privacy);

        findComponentById(ResourceTable.Id_imageBack).setClickedListener(new Component.ClickedListener() {
            @Override
            public void onClick(Component component) {
                present(new SplashSlice(), intent);
                terminate();
            }
        });
    }

    @Override
    public void onActive() {
        super.onActive();
    }

    @Override
    public void onForeground(Intent intent) {
        super.onForeground(intent);
    }


}

