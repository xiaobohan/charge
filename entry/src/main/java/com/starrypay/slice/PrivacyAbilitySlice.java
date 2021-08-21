package com.starrypay.slice;

import com.starrypay.myapplication.ResourceTable;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.agp.components.Component;
import ohos.multimodalinput.event.KeyEvent;

public class PrivacyAbilitySlice extends AbilitySlice {

    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_page_privacy);

        findComponentById(ResourceTable.Id_imageBack).setClickedListener(component -> {
            present(new SplashSlice(), new Intent());
            terminate();
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

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent keyEvent) {
        if (keyCode==KeyEvent.KEY_BACK){
            present(new SplashSlice(), new Intent());
            terminate();
            return true;
        }
        return super.onKeyDown(keyCode, keyEvent);
    }
}

