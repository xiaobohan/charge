package com.starrypay.component;

import com.huawei.paysdk.api.HuaweiPayImpl;
import com.huawei.paysdk.entities.MercOrderApply;
import com.huawei.paysdk.entities.PayResult;
import com.starrypay.model.GridItemInfo;
import com.starrypay.myapplication.ResourceTable;
import com.starrypay.provider.GridAdapter;
import com.starrypay.utils.AppUtils;
import ohos.agp.components.*;
import ohos.eventhandler.EventHandler;
import ohos.eventhandler.EventRunner;
import ohos.eventhandler.InnerEvent;
import ohos.telephony.PhoneNumberFormattingTextObserver;

import java.util.ArrayList;
import java.util.List;

public class RechargeLayout {

    private Component rootView;

    private GridView phoneGridView;
    private TextField etPhone;

    private final EventHandler eventHandler = new EventHandler(EventRunner.getMainEventRunner()) {
        @Override
        protected void processEvent(InnerEvent event) {
            if (event.object instanceof PayResult) {
                PayResult payResult = (PayResult) event.object;

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
                    etPhone.delete(1,true);
                }
            }
        });

        initChargeList();

        container.addComponent(rootView);
    }

    private void initChargeList() {
        List<GridItemInfo> upperItemList = new ArrayList<>();

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
        MercOrderApply mercOrderApply = new MercOrderApply();

        HuaweiPayImpl huaweiPay = new HuaweiPayImpl(getRootView().getContext(), true);
        PayResult payResult = huaweiPay.pay(MercOrderApply.toJson(mercOrderApply));

        InnerEvent event = InnerEvent.get();
        event.object = payResult;

        eventHandler.sendEvent(event);
    }


}
