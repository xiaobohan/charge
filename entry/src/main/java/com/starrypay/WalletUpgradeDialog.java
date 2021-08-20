package com.starrypay;

import com.starrypay.myapplication.ResourceTable;
import ohos.aafwk.content.Intent;
import ohos.agp.components.AttrHelper;
import ohos.agp.components.Component;
import ohos.agp.components.DirectionalLayout;
import ohos.agp.components.LayoutScatter;
import ohos.agp.window.dialog.CommonDialog;
import ohos.app.Context;
import ohos.utils.net.Uri;

public class WalletUpgradeDialog extends CommonDialog {

    public WalletUpgradeDialog(Context context) {
        super(context);
        initView(context);
    }

    private void initView(Context context) {
        Component rootLayout = LayoutScatter.getInstance(context).parse(ResourceTable.Layout_dialog_wallet_upgrade, null, false);
        setSize(AttrHelper.vp2px(300, context), DirectionalLayout.LayoutConfig.MATCH_CONTENT);
        setCornerRadius(AttrHelper.vp2px(10, context));

        rootLayout.findComponentById(ResourceTable.Id_ok).setClickedListener(component -> {
            goApplicationMarket(context, "com.huawei.wallet");
            destroy();
        });

        setContentCustomComponent(rootLayout);
    }


    private void goApplicationMarket(Context context, String bundleName) {
        try {
            Uri uri = Uri.parse("market://details?id=" + bundleName);
            Intent intent = new Intent();
            intent.setUri(uri);
            intent.setAction("android.intent.action.VIEW");
            intent.setBundle("com.huawei.appmarket");
            intent.addFlags(Intent.FLAG_ABILITY_NEW_MISSION);
            context.startAbility(intent, 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
