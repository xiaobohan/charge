package com.starrypay.component;

import com.huawei.hms.accountsdk.support.account.result.AuthAccount;
import com.huawei.log.Logger;
import com.huawei.paysdk.api.HuaweiPayImpl;
import com.huawei.paysdk.entities.MercOrderApply;
import com.huawei.paysdk.entities.PayResult;
import com.starrypay.http.RequestCallback;
import com.starrypay.login.HuaweiLoginManager;
import com.starrypay.model.GridItemInfo;
import com.starrypay.myapplication.ResourceTable;
import com.starrypay.provider.GridAdapter;
import com.starrypay.utils.GlobalTaskExecutor;
import com.starrypay.utils.ToastUtils;
import ohos.agp.components.*;
import ohos.eventhandler.EventHandler;
import ohos.eventhandler.EventRunner;
import ohos.eventhandler.InnerEvent;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;

import java.util.ArrayList;
import java.util.List;

public class RechargeLayout {

    private Component rootView;

    private GridView phoneGridView;
    private TextField etPhone;

    private static final HiLogLabel TAG = new HiLogLabel(HiLog.DEBUG, 0x0, "Pay");

    private final EventHandler eventHandler = new EventHandler(EventRunner.getMainEventRunner()) {
        @Override
        protected void processEvent(InnerEvent event) {
            try {
                if (event.object instanceof PayResult) {
                    PayResult payResult = (PayResult) event.object;
                }
            } catch (Exception e) {
                ToastUtils.showToast("支付失败");
                HiLog.error(TAG, Logger.getStackTraceString(e));
            }

        }
    };

    public RechargeLayout(ComponentContainer container) {
        init(container);
    }

    private void init(ComponentContainer container) {
        rootView = LayoutScatter.getInstance(container.getContext())
                .parse(ResourceTable.Layout_phone_charge, null, false);

        phoneGridView = (GridView) rootView.findComponentById(ResourceTable.Id_grid_view);
        etPhone = (TextField) rootView.findComponentById(ResourceTable.Id_etPhone);
        etPhone.addTextObserver(new Text.TextObserver() {
            @Override
            public void onTextUpdated(String s, int i, int i1, int i2) {
                if (s.length() > 11) {
                    etPhone.delete(1, true);
                }
            }
        });

        initChargeList();

        Button btnPay = (Button) rootView.findComponentById(ResourceTable.Id_btnPay);
        btnPay.setClickedListener(component -> {
            doPay();
        });

        container.addComponent(rootView);
    }

    private void initChargeList() {
        List<GridItemInfo> upperItemList = new ArrayList<>();

//        List<PhoneChargeInfoBean> upperItemList = new ArrayList<>();

        String[] moneyArr = new String[]{
                "1元",
                "2元",
                "5元",
                "10元",
                "20元",
                "30元"
        };

        String[] chargeArr = new String[]{
                "售价1.1元",
                "售价2.1元",
                "售价5.1元",
                "售价9.99元",
                "售价19.98元",
                "售价29.97元"
        };

        for (int i = 0; i < moneyArr.length; i++) {
            upperItemList.add(new GridItemInfo(moneyArr[i], chargeArr[i]));
        }

        upperItemList.get(0).setSelect(true);

        phoneGridView = (GridView) rootView.findComponentById(ResourceTable.Id_grid_view);

        GridAdapter adapter = new GridAdapter(phoneGridView.getContext(), upperItemList);

        setAdapter(adapter);
        phoneGridView.setTag("");
    }

    private void setAdapter(GridAdapter adapter) {
        phoneGridView.setAdapter(adapter, component -> {
            int tag = (int) component.getTag();
            for (GridItemInfo info : adapter.itemInfos) {
                info.setSelect(false);
            }
            adapter.itemInfos.get(tag).setSelect(true);

            adapter.refreshData();
        });
    }

    public Component getRootView() {
        return rootView;
    }

    public void doPay() {
        boolean isLogin = HuaweiLoginManager.checkIsLogin();
        if (isLogin){
            doLogin();
            return;
        }

        GlobalTaskExecutor.getInstance().IO(() -> {
            try {
                MercOrderApply mercOrderApply = new MercOrderApply();

                String js = "{\"allocationType\":\"NO_ALLOCATION\",\"authId\":\"104592475\",\"callbackUrl\":\"http://w3.huawei.com/next/indexa.html\",\"mercOrder\":{\"appId\":\"104592475\",\"bizType\":\"100002\",\"currency\":\"CNY\",\"goodsInfo\":{\"goodsDetail\":[{\"goodsNum\":8,\"goodsPrice\":1,\"goodsShortName\":\"衣服\"},{\"goodsNum\":1,\"goodsPrice\":100,\"goodsShortName\":\"裤子\"}],\"goodsListCnt\":2,\"goodsSum\":9},\"mercNo\":\"101610000031\",\"mercOrderNo\":\"PayCheckoutDemo_1621566619183\",\"totalAmount\":108,\"tradeSummary\":\"衣服裤子等商品\"},\"payload\":\"衣服裤子等商品\",\"returnUrl\":\"http://w3.huawei.com/next/indexa.html\",\"sign\":\"Am98RQA3hwVawPpJpSg7vwCFJg9/GWwbzwzFJPmThWIgxvbl2orHKqT4EBouRxCgntd59WqIlgUm\\nZgeyZaMmlkAh4ivV5fJXAC4Gt+2m9imDzlR8Yy5TmH0G7+MceDDSIhjgRKfOjXybnA4dlsU8rTpX\\npNDGojix4/E3L2gbo0s\\u003d\\n\",\"signType\":\"SHA256WithRSA/PSS\"}";
                HuaweiPayImpl huaweiPay = new HuaweiPayImpl(getRootView().getContext(), true);
//                PayResult payResult = huaweiPay.pay(MercOrderApply.toJson(mercOrderApply));
                PayResult payResult = huaweiPay.pay(js);

                InnerEvent event = InnerEvent.get();
                event.object = payResult;

                eventHandler.sendEvent(event);
            } catch (Throwable e) {
                ToastUtils.showToast("支付失败");
                HiLog.error(TAG, Logger.getStackTraceString(e));
            }
        });
    }


    private void doLogin() {
        HuaweiLoginManager.login(new HuaweiLoginManager.LoginAccountCallback() {
            @Override
            public void onSuccess(AuthAccount authAccount) {
                HuaweiLoginManager.loginToService(authAccount, new RequestCallback<String>() {
                    @Override
                    public void onSuccess(String respBean) {

                    }

                    @Override
                    public void onFailed(int code, Throwable e) {

                    }
                });
            }

            @Override
            public void onFailed(Throwable throwable) {
                String a = "";
                String aa = "";
            }
        });
    }


}
