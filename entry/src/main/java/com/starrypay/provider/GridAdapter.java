package com.starrypay.provider;

import com.starrypay.model.GridItemInfo;
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
public class GridAdapter {
    private static final int GRID_LAYOUT_BORDER_MARGIN = 24;
    private static final int GRID_ITEM_RIGHT_MARGIN = 8;

    private final List<Component> componentList = new ArrayList<>();

    public GridAdapter(Context context, List<GridItemInfo> itemInfos) {
        int itemPx = getItemWidthByScreen(context);
        for (GridItemInfo item : itemInfos) {
            Component gridItem = LayoutScatter.getInstance(context).parse(ResourceTable.Layout_grid_item, null, false);
            gridItem.setTag(item.getTag());

            gridItem.setSelected(item.isSelect());

            if (gridItem.findComponentById(ResourceTable.Id_price) instanceof Text) {
                Text textItem = (Text) gridItem.findComponentById(ResourceTable.Id_price);
                if (item.isSelect()) {
                    textItem.setTextColor(new Color(Color.rgb(39, 89, 238)));
                }else{
                    textItem.setTextColor(new Color(Color.rgb(51, 51, 51)));
                }
                textItem.setText(item.getOriginPrice());
            }

            if (gridItem.findComponentById(ResourceTable.Id_chargeMoney) instanceof Text) {
                Text textItem = (Text) gridItem.findComponentById(ResourceTable.Id_chargeMoney);
                if (item.isSelect()) {
                    textItem.setTextColor(new Color(Color.rgb(39, 89, 238)));
                }else{
                    textItem.setTextColor(new Color(Color.rgb(51, 51, 51)));
                }
                textItem.setText(item.getChargePrice());
            }

            gridItem.setWidth(itemPx);
            gridItem.setMarginRight(AttrHelper.vp2px(GRID_ITEM_RIGHT_MARGIN, context));
            componentList.add(gridItem);
        }
    }

    /**
     * method for get componentList
     *
     * @return componentList
     */
    public List<Component> getComponentList() {
        return componentList;
    }

    private int getItemWidthByScreen(Context context) {
        int screenWidth = AppUtils.getScreenInfo(context).getPointXToInt();

        return (screenWidth
                - AttrHelper.vp2px(GRID_LAYOUT_BORDER_MARGIN, context) * 2
                - AttrHelper.vp2px(GRID_ITEM_RIGHT_MARGIN, context) * 3)
                / AppUtils.getIntResource(context, ResourceTable.Integer_column_count);
    }
}
