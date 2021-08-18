package com.starrypay.utils;

import ohos.agp.components.Component;
import ohos.agp.utils.LayoutAlignment;
import ohos.agp.window.dialog.CommonDialog;
import ohos.agp.window.dialog.ToastDialog;
import ohos.app.Context;

public class ToastUtils {

    private static Context appContext;

    private static ToastDialog toastDialog;

    public static void initAppContenxt(Context context) {
        appContext = context;
    }

    public static void showToast(String msg) {
        GlobalTaskExecutor.getInstance().MAIN(() -> {
            if (toastDialog != null && toastDialog.isShowing()) {
                toastDialog.cancel();
            }
            toastDialog = new ToastDialog(appContext);
            toastDialog.setContentText(msg);
            toastDialog.setAutoClosable(true);
            toastDialog.setAlignment(LayoutAlignment.CENTER);
            toastDialog.show();
        });
    }

}
