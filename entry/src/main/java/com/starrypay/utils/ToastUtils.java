package com.starrypay.utils;

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
        if (toastDialog != null && toastDialog.isShowing()) {
            toastDialog.cancel();
        }
        toastDialog = new ToastDialog(appContext);
        toastDialog.setContentText(msg);
        toastDialog.show();
    }

}
