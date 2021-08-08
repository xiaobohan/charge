package com.starrypay.component;

import com.starrypay.model.GridItemInfo;
import com.starrypay.myapplication.ResourceTable;
import com.starrypay.provider.GridAdapter;
import com.starrypay.utils.AppUtils;
import ohos.agp.components.Component;
import ohos.agp.components.ComponentContainer;
import ohos.agp.components.LayoutScatter;

import java.util.ArrayList;
import java.util.List;

public class RechargeLayout {

    private ComponentContainer container;
    private Component rootView;

    private GridView phoneGridView;

    public RechargeLayout(ComponentContainer container) {
        this.container = container;
        init();
    }

    private void init() {
        rootView = LayoutScatter.getInstance(container.getContext())
                .parse(ResourceTable.Layout_phone_charge, null, false);

        phoneGridView = (GridView) rootView.findComponentById(ResourceTable.Id_grid_view);

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

        GridAdapter adapter = new GridAdapter(container.getContext(), upperItemList);

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
}
