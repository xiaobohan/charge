package com.starrypay.login;

import com.huawei.hms.accountsdk.exception.ApiException;
import com.huawei.hms.accountsdk.support.account.AccountAuthManager;
import com.huawei.hms.accountsdk.support.account.request.AccountAuthParams;
import com.huawei.hms.accountsdk.support.account.request.AccountAuthParamsHelper;
import com.huawei.hms.accountsdk.support.account.result.AuthAccount;
import com.huawei.hms.accountsdk.support.account.service.AccountAuthService;
import com.huawei.hms.accountsdk.support.account.tasks.OnFailureListener;
import com.huawei.hms.accountsdk.support.account.tasks.OnSuccessListener;
import com.huawei.hms.accountsdk.support.account.tasks.Task;
import com.starrypay.utils.DataKeyDef;
import com.starrypay.utils.GlobalTaskExecutor;
import com.starrypay.utils.LocalConfigUtils;
import com.starrypay.utils.ToastUtils;
import ohos.aafwk.ability.AbilityPackage;

import java.util.concurrent.CopyOnWriteArrayList;

public class HuaweiLoginManager {

    private static volatile HuaweiLoginManager instance;

    public static HuaweiLoginManager getInstance() {
        if (instance == null) {
            synchronized (HuaweiLoginManager.class) {
                if (instance == null) {
                    instance = new HuaweiLoginManager();
                }
            }
        }
        return instance;
    }

    private AbilityPackage sPkg;

    private String openId = "";

    private final CopyOnWriteArrayList<LoginStateObserver> observers = new CopyOnWriteArrayList<LoginStateObserver>();

    public void initSdk(AbilityPackage pkg) {
        sPkg = pkg;
        try {
            // 调用AccountAuthManager.init方法初始化
            AccountAuthManager.init(pkg);
        } catch (ApiException apiException) {
            apiException.getStatusCode();
        }
    }

    public boolean checkIsLogin() {
        return isNotNullOrEmpty(openId);
    }

    public void login(LoginAccountCallback callback) {
        GlobalTaskExecutor.getInstance().IO().execute(() -> {
            AccountAuthParams accountAuthParams = new AccountAuthParamsHelper(AccountAuthParams.DEFAULT_AUTH_REQUEST_PARAM)
                    .setMobileNumber()
                    .setAuthorizationCode()
                    .createParams();
            try {
                AccountAuthService service = AccountAuthManager.getService(accountAuthParams);

                Task<AuthAccount> sign = service.signIn();
                sign.addOnSuccessListener(authAccount -> {
                    LocalConfigUtils.putData(DataKeyDef.KEY_AUTO_LOGIN, true);
                    String openId = authAccount.getOpenId();
                    if (isNotNullOrEmpty(openId)) {
                        this.openId = openId;
                    } else {
                        GlobalTaskExecutor.getInstance().MAIN(() -> {
                            if (callback != null) {
                                callback.onFailed(new Exception("open id is null`"));
                            }
                        });
                        return;
                    }

                    GlobalTaskExecutor.getInstance().MAIN(() -> {
                        if (callback != null) {
                            callback.onSuccess(authAccount);
                        }

                        for (LoginStateObserver observer : observers) {
                            observer.onLogin();
                        }
                    });
                });
                sign.addOnFailureListener(e -> {
                    GlobalTaskExecutor.getInstance().MAIN(() -> {
                        if (callback != null) {
                            callback.onFailed(e);
                        }
                    });
                });
            } catch (ApiException e) {
                GlobalTaskExecutor.getInstance().MAIN(() -> {
                    if (callback != null) {
                        callback.onFailed(e);
                    }
                });
            }
        });
    }

    public void logout() {
        openId = "";
        ToastUtils.showToast("已退出登录");
        GlobalTaskExecutor.getInstance().MAIN(() -> {
            for (LoginStateObserver observer : observers) {
                observer.onLogout();
            }
        });
        LocalConfigUtils.putData(DataKeyDef.KEY_AUTO_LOGIN, false);
        GlobalTaskExecutor.getInstance().IO(() -> {
            AccountAuthParams accountAuthParams = new AccountAuthParamsHelper(AccountAuthParams.DEFAULT_AUTH_REQUEST_PARAM)
                    .setMobileNumber()
                    .setAuthorizationCode()
                    .createParams();
            try {
                AccountAuthService service = AccountAuthManager.getService(accountAuthParams);
                Task<Void> signOut = service.signOut();
                signOut.addOnSuccessListener(unused -> {
                });
            } catch (Exception ignored) {
            }
        });
    }


    public void autoLogin() {
        GlobalTaskExecutor.getInstance().IO().execute(() -> {
            if (!LocalConfigUtils.getData(DataKeyDef.KEY_AUTO_LOGIN, false)) {
                return;
            }
            AccountAuthParams accountAuthParams = new AccountAuthParamsHelper(AccountAuthParams.DEFAULT_AUTH_REQUEST_PARAM)
                    .setMobileNumber()
                    .setAuthorizationCode()
                    .createParams();
            try {
                AccountAuthService service = AccountAuthManager.getService(accountAuthParams);
                Task<AuthAccount> signIn = service.silentSignIn();
                signIn.addOnSuccessListener(authAccount -> GlobalTaskExecutor.getInstance().MAIN(() -> {
                    String openId = authAccount.getOpenId();
                    if (isNotNullOrEmpty(openId)) {
                        this.openId = openId;
                        for (LoginStateObserver observer : observers) {
                            observer.onLogin();
                        }
                    }
                }));
            } catch (Exception ignored) {
            }
        });
    }

    public String getOpenId() {
        return openId;
    }

    public interface LoginAccountCallback {

        void onSuccess(AuthAccount authAccount);

        void onFailed(Throwable throwable);

    }

    public void registerObserver(LoginStateObserver observer) {
        observers.add(observer);
    }

    public void unRegisterObserver(LoginStateObserver observer) {
        observers.remove(observer);
    }

    private boolean isNotNullOrEmpty(String str) {
        return str != null && !"".equals(str);
    }

    public interface LoginStateObserver {

        void onLogin();

        void onLogout();

    }

}
