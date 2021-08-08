package com.starrypay.component;

import com.starrypay.myapplication.ResourceTable;
import ohos.aafwk.ability.AbilitySlice;
import ohos.agp.components.*;

/**
 * The DragLayout
 */
public class DragLayout {

    private AbilitySlice slice;

    private TabList tabList;

    private PageSlider slider;

    public DragLayout(AbilitySlice slice) {
        this.slice = slice;
        init();
    }

    /**
     * method for init view
     */
    public void init() {
        initTab();
        initPhoneCharge();
    }

    private void initPhoneCharge() {
        slider = (PageSlider) slice.findComponentById(ResourceTable.Id_slider);
        slider.setOrientation(Component.HORIZONTAL);

        slider.addPageChangedListener(new PageSlider.PageChangedListener() {
            @Override
            public void onPageSliding(int itemPos, float itemPosOffset, int itemPosPixles) {
            }

            @Override
            public void onPageSlideStateChanged(int state) {
            }

            @Override
            public void onPageChosen(int itemPos) {
                tabList.getTabAt(itemPos).select();
            }
        });
        slider.setProvider(new PageSliderProvider() {
            @Override
            public int getCount() {
                return 2;
            }

            @Override
            public Object createPageInContainer(ComponentContainer componentContainer, int i) {
                Component component = null;
                if (i == 0) {
                    component = new RechargeLayout(componentContainer).getRootView();
                } else {
                    component = new RechargeRecordLayout(componentContainer).getRootView();
                }
                return component;
            }

            @Override
            public void destroyPageFromContainer(ComponentContainer componentContainer, int i, Object o) {
                componentContainer.removeComponent((Component) o);
            }

            @Override
            public boolean isPageMatchToObject(Component component, Object o) {
                return true;
            }

        });

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
                    slider.setCurrentPage(tab.getPosition(), true);
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


}
