package com.starrypay.component;

import com.huawei.hms.accountsdk.support.account.result.AuthAccount;
import com.huawei.log.Logger;
import com.huawei.paysdk.api.HuaweiPayImpl;
import com.huawei.paysdk.entities.MercOrderApply;
import com.huawei.paysdk.entities.PayResult;
import com.starrypay.bean.*;
import com.starrypay.common.NetWatcher;
import com.starrypay.http.Apis;
import com.starrypay.http.HttpUtils;
import com.starrypay.login.HuaweiLoginManager;
import com.starrypay.myapplication.ResourceTable;
import com.starrypay.provider.EmptyChargeAdapter;
import com.starrypay.provider.GridAdapter;
import com.starrypay.utils.DataKeyDef;
import com.starrypay.utils.GlobalTaskExecutor;
import com.starrypay.utils.NetUtils;
import com.starrypay.utils.ToastUtils;
import com.starrypay.utils.sign.PayUtils;
import ohos.agp.components.*;
import ohos.agp.window.dialog.CommonDialog;
import ohos.app.Context;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RechargeLayout implements NetWatcher {

    private Component rootView;

    private GridView phoneGridView;
    private TextField etPhone;
    private Text textChargeName;
    private Component networkError;

    private RechargeCallback callback;

    private GridAdapter mAdapter;

    private static final HiLogLabel TAG = new HiLogLabel(HiLog.DEBUG, 0x0, "Pay");

    public RechargeLayout(ComponentContainer container) {
        init(container);
        getGoodList();
    }

    public void setCallback(RechargeCallback callback) {
        this.callback = callback;
    }

    private void init(ComponentContainer container) {
        rootView = LayoutScatter.getInstance(container.getContext())
                .parse(ResourceTable.Layout_phone_charge, null, false);

        rootView.findComponentById(ResourceTable.Id_imgContact).setClickedListener(component -> {
            if (callback != null) {
                callback.clickContact();
            }
        });

        phoneGridView = (GridView) rootView.findComponentById(ResourceTable.Id_grid_view);

        ArrayList<PhoneChargeInfoBean> list = new ArrayList<>();
        list.add(new PhoneChargeInfoBean());
        list.add(new PhoneChargeInfoBean());
        list.add(new PhoneChargeInfoBean());
        list.add(new PhoneChargeInfoBean());
        list.add(new PhoneChargeInfoBean());
        list.add(new PhoneChargeInfoBean());
        EmptyChargeAdapter adapter = new EmptyChargeAdapter(rootView.getContext(), list);
        phoneGridView.setAdapter(adapter, null);

        etPhone = (TextField) rootView.findComponentById(ResourceTable.Id_etPhone);
        etPhone.addTextObserver((s, i, i1, i2) -> {
            if (s.length() > 11) {
                etPhone.delete(1, true);
            }
            if (s.length() < 11) {
                textChargeName.setText("");
            }
        });

        Button btnPay = (Button) rootView.findComponentById(ResourceTable.Id_btnPay);
        btnPay.setClickedListener(component -> {
            doPay();
        });

        textChargeName = (Text) rootView.findComponentById(ResourceTable.Id_textChargeName);

        networkError = rootView.findComponentById(ResourceTable.Id_networkError);
        if (NetUtils.isNetworkConnected(rootView.getContext())) {
            networkError.setVisibility(Component.HIDE);
        } else {
            networkError.setVisibility(Component.VISIBLE);
        }

        container.addComponent(rootView);
    }

    private void setAdapter(GridAdapter adapter) {
        phoneGridView.setAdapter(adapter, component -> {
            int tag = (int) component.getTag();
            for (PhoneChargeInfoBean info : adapter.itemInfos) {
                info.setSelect(false);
            }
            adapter.itemInfos.get(tag).setSelect(true);

            adapter.refreshData();
        });
        mAdapter = adapter;
    }

    public Component getRootView() {
        return rootView;
    }

    private void getGoodList() {
        HashMap<String, String> map = new HashMap<>();
        map.put("goodsType", DataKeyDef.DATA_TYPE);
        Callback<BaseRespBean<List<PhoneChargeInfoBean>>> callback = new Callback<BaseRespBean<List<PhoneChargeInfoBean>>>() {

            private int retryTime = 0;

            @Override
            public void onResponse(Call<BaseRespBean<List<PhoneChargeInfoBean>>> call, Response<BaseRespBean<List<PhoneChargeInfoBean>>> response) {
                GlobalTaskExecutor.getInstance().MAIN(() -> {
                    BaseRespBean<List<PhoneChargeInfoBean>> body = response.body();
                    if (body.isSuccess()) {
                        List<PhoneChargeInfoBean> data = body.data;
                        if (!data.isEmpty()) {
                            data.get(0).setSelect(true);
                        }
                        setAdapter(new GridAdapter(rootView.getContext(), data));
                    } else {
                        onFailure(call, new Exception("server error"));
                    }
                });
            }

            @Override
            public void onFailure(Call<BaseRespBean<List<PhoneChargeInfoBean>>> call, Throwable throwable) {
                retryTime++;
                if (retryTime < 2) {
                    HttpUtils.create(Apis.class)
                            .getShopList(map)
                            .enqueue(this);
                }
            }
        };
        HttpUtils.create(Apis.class)
                .getShopList(map)
                .enqueue(callback);
    }

    public void doPay() {
        boolean isLogin = HuaweiLoginManager.getInstance().checkIsLogin();
        if (!isLogin) {
            HuaweiLoginManager.getInstance().login(new HuaweiLoginManager.LoginAccountCallback() {

                @Override
                public void onSuccess(AuthAccount authAccount) {
                    doPay();
                }

                @Override
                public void onFailed(Throwable throwable) {

                }
            });
            return;
        }
        if (mAdapter == null) {
            return;
        }
        String phone = etPhone.getText().trim();
        if (phone.length() != 11) {
            ToastUtils.showToast("请检查手机号码格式");
            return;
        }
        PhoneChargeInfoBean item = mAdapter.getSelectItem();
        if (item == null) {
            return;
        }

        HttpUtils.create(Apis.class)
                .createOrder(new CreateOrderBean(HuaweiLoginManager.getInstance().getOpenId(), item.getGoodsId(), phone))
                .enqueue(new Callback<BaseRespBean<OrderInfoBean>>() {
                    @Override
                    public void onResponse(Call<BaseRespBean<OrderInfoBean>> call, Response<BaseRespBean<OrderInfoBean>> response) {

                        if (response.isSuccessful() && response.body().isSuccess()) {
                            MercOrderApply orderApply = PayUtils.getMercOrderApply(response.body().data);
                            launchHuaweiPay(orderApply);
                        } else {
                            onFailure(call, new Exception("server error"));
                        }
                    }

                    @Override
                    public void onFailure(Call<BaseRespBean<OrderInfoBean>> call, Throwable throwable) {
                        ToastUtils.showToast("创建订单失败");
                    }
                });
    }

    private void launchHuaweiPay(MercOrderApply mercOrderApply) {
        GlobalTaskExecutor.getInstance().IO(() -> {
            try {
                HuaweiPayImpl huaweiPay = new HuaweiPayImpl(getRootView().getContext(), false);
                PayResult payResult = huaweiPay.pay(MercOrderApply.toJson(mercOrderApply));

                if (PayResult.PAY_SUCCESS.equals(payResult.getReturnCode())) {
                    GlobalTaskExecutor.getInstance().MAIN(this::showRechargeSuccessDialog);
                } else {
                    ToastUtils.showToast("支付失败");
                }
            } catch (Throwable e) {
                ToastUtils.showToast("支付失败");
                HiLog.error(TAG, Logger.getStackTraceString(e));
            }
        });
    }

    public void onContactSelect(Serializable data) {
        if (data instanceof ContactBean) {
            ContactBean bean = (ContactBean) data;
            etPhone.setText(bean.phone.replace(" ", ""));

            textChargeName.setText(bean.name);
        }
    }

    @Override
    public void onReconnect() {
        if (mAdapter == null || !mAdapter.hasData()) {
            getGoodList();
        }
        GlobalTaskExecutor.getInstance().MAIN(() -> networkError.setVisibility(Component.HIDE));
    }


    public interface RechargeCallback {

        void clickContact();

    }

    private void showRechargeSuccessDialog() {
        Context context = rootView.getContext();
        CommonDialog dialog = new CommonDialog(context);

        Component rootLayout = LayoutScatter.getInstance(context).parse(ResourceTable.Layout_dialog_recharge_success, null, false);
        dialog.setSize(AttrHelper.vp2px(300, context), DirectionalLayout.LayoutConfig.MATCH_CONTENT);
        dialog.setCornerRadius(AttrHelper.vp2px(10, context));

        StringBuilder sb = new StringBuilder("您的手机充值已成功提交！具体到账时间以运营商为准。感谢您的使用！");

        Text textContent = (Text) rootLayout.findComponentById(ResourceTable.Id_content);
        textContent.setText(sb.toString());

        Button btnSubmit = (Button) rootLayout.findComponentById(ResourceTable.Id_ok);
        btnSubmit.setClickedListener(component -> {
            dialog.destroy();
        });

        dialog.setContentCustomComponent(rootLayout);

        dialog.show();
    }

}
