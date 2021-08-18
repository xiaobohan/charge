package com.starrypay.component;

import com.starrypay.bean.BaseRespBean;
import com.starrypay.bean.PageRespBean;
import com.starrypay.bean.RechargeRecordBean;
import com.starrypay.common.AbsItemProvider;
import com.starrypay.http.Apis;
import com.starrypay.http.HttpUtils;
import com.starrypay.login.HuaweiLoginManager;
import com.starrypay.myapplication.ResourceTable;
import com.starrypay.utils.DataKeyDef;
import com.starrypay.utils.ToastUtils;
import ohos.agp.components.*;
import ohos.agp.text.Font;
import ohos.agp.utils.Color;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.HashMap;

public class RechargeRecordLayout {

    private ComponentContainer container;
    private Component rootView;

    private Component dlNoLogin;

    private AbsItemProvider<RechargeRecordBean> provider = new AbsItemProvider<RechargeRecordBean>() {

        @Override
        public Component getComponent(int i, Component component, ComponentContainer componentContainer) {
            final Component cpt;
            if (component == null) {
                cpt = LayoutScatter.getInstance(rootView.getContext())
                        .parse(ResourceTable.Layout_item_recharge_record, null, false);
            } else {
                cpt = component;
            }

            RechargeRecordBean bean = dataList.get(i);
            Text phone = (Text) cpt.findComponentById(ResourceTable.Id_phone);
            phone.setFont(Font.DEFAULT_BOLD);
            phone.setText(bean.mobile);

            Text desc = (Text) cpt.findComponentById(ResourceTable.Id_desc);
            desc.setText(bean.name);

            Text time = (Text) cpt.findComponentById(ResourceTable.Id_time);
            time.setText(bean.createTime);

            Text amount = (Text) cpt.findComponentById(ResourceTable.Id_amount);
            amount.setFont(Font.DEFAULT_BOLD);
            amount.setText(bean.getMontyStr());

            Text status = (Text) cpt.findComponentById(ResourceTable.Id_status);
            if (bean.isPay()) {
                status.setTextColor(Color.BLUE);
                status.setText("交易成功");
            } else {
                status.setTextColor(new Color(Color.rgb(102, 102, 102)));
                status.setText("待支付");
            }

            return cpt;
        }

    };

    public RechargeRecordLayout(ComponentContainer container) {
        this.container = container;
        init();

        initData();
    }

    private void init() {
        rootView = LayoutScatter.getInstance(container.getContext())
                .parse(ResourceTable.Layout_page_charge_record, null, false);

        dlNoLogin = rootView.findComponentById(ResourceTable.Id_dlNoLogin);
        onLoginStateChange(HuaweiLoginManager.getInstance().checkIsLogin());

        rootView.findComponentById(ResourceTable.Id_btnToLogin).setClickedListener(component -> {
            HuaweiLoginManager.getInstance().login(null);
        });

        ListContainer list = (ListContainer) rootView.findComponentById(ResourceTable.Id_list);
        list.setItemProvider(provider);

        list.setBindStateChangedListener(new Component.BindStateChangedListener() {
            @Override
            public void onComponentBoundToWindow(Component component) {
                // ListContainer初始化时数据统一在provider中创建，不直接调用这个接口；
                // 建议在onComponentBoundToWindow监听或者其他事件监听中调用。
                provider.notifyDataChanged();
            }

            @Override
            public void onComponentUnboundFromWindow(Component component) {
            }
        });

        container.addComponent(rootView);
    }

    private void initData() {
        HashMap<String, String> map = new HashMap<>();
        map.put("goodsType", DataKeyDef.DATA_TYPE);
        map.put("openId", HuaweiLoginManager.getInstance().getOpenId());
        HttpUtils.create(Apis.class)
                .getOrderList(map)
                .enqueue(new Callback<BaseRespBean<PageRespBean<RechargeRecordBean>>>() {
                    @Override
                    public void onResponse(Call<BaseRespBean<PageRespBean<RechargeRecordBean>>> call, Response<BaseRespBean<PageRespBean<RechargeRecordBean>>> response) {
                        if (response.isSuccessful() && response.body().isSuccess()) {
                            provider.refresh(response.body().data.list);
                        } else {
                            onFailure(call, new Exception("server error"));
                        }
                    }

                    @Override
                    public void onFailure(Call<BaseRespBean<PageRespBean<RechargeRecordBean>>> call, Throwable throwable) {
                        ToastUtils.showToast("网络异常");
                    }
                });
    }

    public Component getRootView() {
        return rootView;
    }


    public void onLoginStateChange(boolean isLogin) {
        if (isLogin) {
            dlNoLogin.setVisibility(Component.HIDE);
        } else {
            dlNoLogin.setVisibility(Component.VISIBLE);
        }
    }


}
