package com.starrypay.utils;

import ohos.app.Context;
import ohos.data.DatabaseHelper;
import ohos.data.preferences.Preferences;

public class LocalConfigUtils {

    private volatile static Preferences sharePreference;

    public static void init(Context context) {
        sharePreference = new DatabaseHelper(context).getPreferences("starrypay");
    }

    public static void putData(String key, Object data) {
        checkInit();
        if (data instanceof String) {
            sharePreference.putString(key, (String) data);
        } else if (data instanceof Boolean) {
            sharePreference.putBoolean(key, (Boolean) data);
        }

        sharePreference.flush();
    }

    public static <T> T getData(String key, T defValue) {
        checkInit();
        if (defValue instanceof String) {
            return ((T) sharePreference.getString(key, (String) defValue));
        } else if (defValue instanceof Boolean) {
            return ((T) new Boolean(sharePreference.getBoolean(key, (Boolean) defValue)));
        }
        return defValue;
    }

    private static void checkInit() {
        if (sharePreference == null) {
            throw new RuntimeException("sp not init");
        }
    }

}
