package com.starrypay.login;

import com.huawei.hms.accountsdk.exception.ApiException;
import com.huawei.hms.accountsdk.support.account.AccountAuthManager;
import com.huawei.hms.accountsdk.support.account.request.AccountAuthParams;
import com.huawei.hms.accountsdk.support.account.request.AccountAuthParamsHelper;
import com.huawei.hms.accountsdk.support.account.result.AuthAccount;
import com.huawei.hms.accountsdk.support.account.service.AccountAuthService;
import com.huawei.hms.accountsdk.support.account.tasks.Task;
import com.starrypay.bean.BaseRespBean;
import com.starrypay.bean.LoginParamsBean;
import com.starrypay.http.Apis;
import com.starrypay.http.HttpUtils;
import com.starrypay.http.RequestCallback;
import com.starrypay.utils.GlobalTaskExecutor;
import ohos.aafwk.ability.AbilityPackage;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HuaweiLoginManager {

    private static AbilityPackage sPkg;

    private static String token = "";

    public static void initSdk(AbilityPackage pkg) {
        sPkg = pkg;
        try {
            // 调用AccountAuthManager.init方法初始化
            AccountAuthManager.init(pkg);
        } catch (ApiException apiException) {
            apiException.getStatusCode();
        }
    }

    public static boolean checkIsLogin() {
        return token == "" || token == null;
    }

    public static void login(LoginAccountCallback callback) {
        GlobalTaskExecutor.getInstance().IO().execute(() -> {
            AccountAuthParams accountAuthParams = new AccountAuthParamsHelper(AccountAuthParams.DEFAULT_AUTH_REQUEST_PARAM)
                    .setMobileNumber()
                    .setAuthorizationCode()
                    .createParams();
            try {
                AccountAuthService service = AccountAuthManager.getService(accountAuthParams);

                Task<AuthAccount> sign = service.signIn();
                sign.addOnSuccessListener(authAccount -> {
                    GlobalTaskExecutor.getInstance().MAIN(() -> {
                        if (callback != null) {
                            callback.onSuccess(authAccount);
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

    public interface LoginAccountCallback {

        void onSuccess(AuthAccount authAccount);

        void onFailed(Throwable throwable);

    }

    public static void loginToService(AuthAccount account, RequestCallback<String> callback) {
        LoginParamsBean bean = new LoginParamsBean();
        bean.setDisplayName(account.getDisplayName());
        bean.setEmail(account.getEmail());
        bean.setHeadPictureURL(account.getAvatarUriString());

        HttpUtils.create(Apis.class)
                .login(bean)
                .enqueue(new Callback<BaseRespBean<String>>() {
                    @Override
                    public void onResponse(Call<BaseRespBean<String>> call, Response<BaseRespBean<String>> response) {
                        BaseRespBean<String> body = response.body();
                        if (body.isSuccess()) {
                            token = body.data;
                            if (callback != null) {
                                callback.onSuccess(token);
                            }
                        } else {
                            if (callback != null) {
                                callback.onFailed(body.code, new Exception("service failed"));
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<BaseRespBean<String>> call, Throwable throwable) {
                        if (callback != null) {
                            callback.onFailed(-1, throwable);
                        }
                    }
                });

    }

}
