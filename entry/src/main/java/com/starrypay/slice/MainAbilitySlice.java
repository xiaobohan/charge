package com.starrypay.slice;

import com.huawei.hms.accountsdk.support.account.result.AuthAccount;
import com.starrypay.component.RechargeLayout;
import com.starrypay.component.RechargeRecordLayout;
import com.starrypay.http.RequestCallback;
import com.starrypay.login.HuaweiLoginManager;
import com.starrypay.myapplication.ResourceTable;
import com.starrypay.utils.ContactUtils;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.agp.components.*;

/**
 * MainAbility slice
 */
public class MainAbilitySlice extends AbilitySlice {

    private TabList tabList;

    private PageSlider slider;

    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_ability_main);
        //UI
        initTab();
        initPhoneCharge();

//        requestPermissionsFromUser(new String[]{"ohos.permission.READ_CONTACTS"},200);
    }

    @Override
    protected void onAbilityResult(int requestCode, int resultCode, Intent resultData) {
        super.onAbilityResult(requestCode, resultCode, resultData);
    }

    @Override
    public void onActive() {
        super.onActive();
    }

    @Override
    public void onForeground(Intent intent) {
        super.onForeground(intent);
    }

    private void initPhoneCharge() {
        slider = (PageSlider) findComponentById(ResourceTable.Id_slider);
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
        if (findComponentById(ResourceTable.Id_tab_list) instanceof ScrollView) {
            tabList = (TabList) findComponentById(ResourceTable.Id_tab_list);
            tabList.setTabMargin(26);
            tabList.setTabLength(200);
            String[] tabArr = new String[]{"充值话费", "充值记录"};
            for (String s : tabArr) {
                TabList.Tab tab = tabList.new Tab(getContext());
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
