package com.starrypay.component;

import com.starrypay.bean.BaseRespBean;
import com.starrypay.bean.RechargeRecordBean;
import com.starrypay.common.AbsItemProvider;
import com.starrypay.http.Apis;
import com.starrypay.http.HttpUtils;
import com.starrypay.myapplication.ResourceTable;
import ohos.agp.components.*;
import ohos.agp.utils.Color;
import ohos.agp.window.dialog.ToastDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.List;

public class RechargeRecordLayout {

    private ComponentContainer container;
    private Component rootView;

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
            phone.setText(bean.mobile);

            Text desc = (Text) cpt.findComponentById(ResourceTable.Id_desc);
            desc.setText(bean.name);

            Text time = (Text) cpt.findComponentById(ResourceTable.Id_time);
            time.setText(bean.createTime);

            Text amount = (Text) cpt.findComponentById(ResourceTable.Id_amount);
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

        ListContainer list = (ListContainer) rootView.findComponentById(ResourceTable.Id_list);

        list.setItemProvider(provider);

        container.addComponent(rootView);
    }

    private void initData() {
        Apis apis = HttpUtils.create(Apis.class);
        apis.getOrderList().enqueue(new Callback<BaseRespBean<List<RechargeRecordBean>>>() {
            @Override
            public void onResponse(Call<BaseRespBean<List<RechargeRecordBean>>> call, Response<BaseRespBean<List<RechargeRecordBean>>> response) {
                try {
                    BaseRespBean<List<RechargeRecordBean>> bean = response.body();
                    if (bean.isSuccess()) {
                        provider.refresh(bean.data);
                    } else {
                        new ToastDialog(rootView.getContext())
                                .setContentText(bean.message)
                                .show();
                    }
                } catch (Exception e) {

                }
            }

            @Override
            public void onFailure(Call<BaseRespBean<List<RechargeRecordBean>>> call, Throwable throwable) {
                new ToastDialog(rootView.getContext())
                        .setContentText(throwable.getMessage())
                        .show();
            }
        });
    }

    public Component getRootView() {
        return rootView;
    }
}
