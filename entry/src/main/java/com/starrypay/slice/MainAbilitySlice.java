package com.starrypay.slice;

import com.starrypay.component.RechargeLayout;
import com.starrypay.component.RechargeRecordLayout;
import com.starrypay.login.HuaweiLoginManager;
import com.starrypay.myapplication.ResourceTable;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.agp.components.*;
import ohos.agp.window.dialog.CommonDialog;
import ohos.bundle.IBundleManager;
import ohos.net.NetHandle;
import ohos.net.NetManager;
import ohos.net.NetStatusCallback;
import ohos.security.SystemPermission;

/**
 * MainAbility slice
 */
public class MainAbilitySlice extends AbilitySlice implements RechargeLayout.RechargeCallback, HuaweiLoginManager.LoginStateObserver {

    private static final int CODE_CONTACT = 100;

    private TabList tabList;

    private PageSlider slider;

    private Image imgLogout;

    private RechargeLayout rechargeLayout;
    private RechargeRecordLayout recordLayout;

    private final NetStatusCallback netStatusCallback = new NetStatusCallback() {
        @Override
        public void onAvailable(NetHandle handle) {
            super.onAvailable(handle);
            if (rechargeLayout != null) {
                rechargeLayout.onReconnect();
            }
            if (recordLayout != null) {
                recordLayout.onReconnect();
            }
        }
    };

    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_ability_main);
        //UI
        imgLogout = (Image) findComponentById(ResourceTable.Id_ivLogout);
        imgLogout.setClickedListener(component -> {
            showLogoutDialog();
        });

        if (HuaweiLoginManager.getInstance().checkIsLogin()) {
            imgLogout.setVisibility(Component.VISIBLE);
        } else {
            imgLogout.setVisibility(Component.HIDE);
        }

        initTab();
        initPhoneCharge();

        HuaweiLoginManager.getInstance().registerObserver(this);

        NetManager.getInstance(this).addDefaultNetStatusCallback(netStatusCallback);
    }

    @Override
    protected void onStop() {
        super.onStop();
        HuaweiLoginManager.getInstance().unRegisterObserver(this);
        NetManager.getInstance(this).removeNetStatusCallback(netStatusCallback);
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
                    if (rechargeLayout == null) {
                        rechargeLayout = new RechargeLayout(componentContainer);
                    }
                    rechargeLayout.setCallback(MainAbilitySlice.this);
                    component = rechargeLayout.getRootView();
                } else {
                    if (recordLayout == null) {
                        recordLayout = new RechargeRecordLayout(componentContainer);
                    }
                    component = recordLayout.getRootView();
                }
                return component;
            }

            @Override
            public void destroyPageFromContainer(ComponentContainer componentContainer, int i, Object o) {
//                componentContainer.removeComponent((Component) o);
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

    @Override
    public void clickContact() {
        if (verifySelfPermission(SystemPermission.READ_CONTACTS) != IBundleManager.PERMISSION_GRANTED) {
            getAbility().requestPermissionsFromUser(new String[]{SystemPermission.READ_CONTACTS}, 0);
        } else {
            presentForResult(new ContactSlice(), new Intent(), CODE_CONTACT);
        }
    }

    @Override
    protected void onResult(int requestCode, Intent resultIntent) {
        super.onResult(requestCode, resultIntent);
        if (requestCode == CODE_CONTACT && resultIntent != null) {
            if (rechargeLayout != null) {
                rechargeLayout.onContactSelect(resultIntent.getSerializableParam("data"));
            }
        }

    }

    @Override
    public void onLogin() {
        imgLogout.setVisibility(Component.VISIBLE);
        if (recordLayout != null) {
            recordLayout.onLoginStateChange(true);
        }
    }

    @Override
    public void onLogout() {
        imgLogout.setVisibility(Component.HIDE);
        if (recordLayout != null) {
            recordLayout.onLoginStateChange(false);
        }
    }


    private void showLogoutDialog() {
        CommonDialog dialog = new CommonDialog(this);

        Component rootLayout = LayoutScatter.getInstance(this).parse(ResourceTable.Layout_dialog_logout, null, false);
        dialog.setSize(AttrHelper.vp2px(300, this), DirectionalLayout.LayoutConfig.MATCH_CONTENT);
        dialog.setCornerRadius(AttrHelper.vp2px(10, this));

        Button btnCancel = (Button) rootLayout.findComponentById(ResourceTable.Id_cancel);
        btnCancel.setClickedListener(component -> {
            dialog.destroy();
        });
        Button btnSubmit = (Button) rootLayout.findComponentById(ResourceTable.Id_btnSubmit);
        btnSubmit.setClickedListener(component -> {
            HuaweiLoginManager.getInstance().logout();
            dialog.destroy();
        });

        dialog.setContentCustomComponent(rootLayout);

        dialog.show();
    }

}
