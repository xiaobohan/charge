package com.starrypay.provider;

import com.starrypay.bean.PhoneChargeInfoBean;
import com.starrypay.myapplication.ResourceTable;
import com.starrypay.utils.AppUtils;
import ohos.agp.components.AttrHelper;
import ohos.agp.components.Component;
import ohos.agp.components.LayoutScatter;
import ohos.agp.components.Text;
import ohos.agp.utils.Color;
import ohos.app.Context;

import java.util.ArrayList;
import java.util.List;

/**
 * The GridAdapter
 */
public class EmptyChargeAdapter extends GridAdapter {

    public EmptyChargeAdapter(Context context, List<PhoneChargeInfoBean> itemInfos) {
        super(context, itemInfos);
    }

    @Override
    protected void initItems(Context context) {
        int itemPx = getItemWidthByScreen(context);
        for (PhoneChargeInfoBean item : itemInfos) {
            Component gridItem = LayoutScatter.getInstance(context).parse(ResourceTable.Layout_item_charge_empty, null, false);

            gridItem.setWidth(itemPx);
            gridItem.setMarginRight(AttrHelper.vp2px(GRID_ITEM_RIGHT_MARGIN, context));
            componentList.add(gridItem);
        }
    }

}
