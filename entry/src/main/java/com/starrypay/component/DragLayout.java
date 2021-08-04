package com.starrypay.component;

import com.starrypay.model.GridItemInfo;
import com.starrypay.myapplication.ResourceTable;
import com.starrypay.provider.GridAdapter;
import com.starrypay.utils.AppUtils;
import ohos.aafwk.ability.AbilitySlice;
import ohos.agp.components.Component;
import ohos.agp.components.ScrollView;
import ohos.agp.components.TabList;

import java.util.ArrayList;
import java.util.List;

/**
 * The DragLayout
 */
public class DragLayout {
    // Item count
    private static final int UP_ITEM_COUNT = 16;
    private static final int DOWN_ITEM_COUNT = 8;
    private static final String UP_GRID_TAG = "upGrid";
    private static final String DOWN_GRID_TAG = "downGrid";
    private static final int INVALID_POSITION = -1;

    private AbilitySlice slice;
    private boolean isViewOnDrag;
    private boolean isViewOnExchange;
    private boolean isScroll;

    private GridView phoneGridView;

    private TabList tabList;

    private Component phoneChargePage;
    private Component recordPage;

    // Item when dragged
    private int scrollViewTop;
    private int scrollViewLeft;
    private Component selectedView;

    private int currentDragX;
    private int currentDragY;

    public DragLayout(AbilitySlice slice) {
        this.slice = slice;
        init();
    }

    /**
     * method for init view
     */
    public void init() {
        initTab();
        initChargeList();
        initPhoneCharge();
    }

    private void initPhoneCharge() {
        phoneChargePage = slice.findComponentById(ResourceTable.Id_phoneCharge);
    }

    private void initTab() {
        if (slice.findComponentById(ResourceTable.Id_tab_list) instanceof ScrollView) {
            tabList = (TabList) slice.findComponentById(ResourceTable.Id_tab_list);
            tabList.setTabMargin(26);
            tabList.setTabLength(200);
            String[] tabArr = new String[]{"充值话费", "充值记录"};
            for (String s : tabArr) {
                TabList.Tab tab = tabList.new Tab(slice.getContext());
                tab.setText(s);
                tab.setName(s);
                tab.setMinWidth(64);
                tab.setPadding(10, 10, 10, 10);
                tabList.addTab(tab);
            }
            tabList.getTabAt(0).select();

            tabList.addTabSelectedListener(new TabList.TabSelectedListener() {
                @Override
                public void onSelected(TabList.Tab tab) {
                    showLayout(tab.getPosition());
                }

                @Override
                public void onUnselected(TabList.Tab tab) {

                }

                @Override
                public void onReselected(TabList.Tab tab) {

                }
            });
        }
    }

    private void showLayout(int index) {
        switch (index) {
            case 0:
                phoneChargePage.setVisibility(Component.VISIBLE);
                break;
            case 1:
                phoneChargePage.setVisibility(Component.HIDE);
                break;
            default:
        }

    }

    private void initChargeList() {

        String itemText = AppUtils.getStringResource(slice, ResourceTable.String_grid_item_text);
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
            upperItemList.add(new GridItemInfo(moneyArr[i], chargeArr[i], UP_GRID_TAG));
        }

        upperItemList.get(0).setSelect(true);

        phoneGridView = (GridView) slice.findComponentById(ResourceTable.Id_grid_view);

        GridAdapter adapter = new GridAdapter(slice.getContext(), upperItemList);
        phoneGridView.setAdapter(adapter, new Component.LongClickedListener() {
            @Override
            public void onLongClicked(Component component) {

            }
        });
        phoneGridView.setTag(UP_GRID_TAG);
    }


}
