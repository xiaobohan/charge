package com.starrypay.slice;

import com.starrypay.bean.ContactBean;
import com.starrypay.common.AbsItemProvider;
import com.starrypay.myapplication.ResourceTable;
import com.starrypay.utils.ContactUtils;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.agp.components.*;

import java.util.ArrayList;

public class ContactSlice extends AbilitySlice {

    private ListContainer list;

    private AbsItemProvider<ContactBean> provider = new AbsItemProvider<ContactBean>() {

        @Override
        public Component getComponent(int i, Component component, ComponentContainer componentContainer) {
            final Component cpt;
            if (component == null) {
                cpt = LayoutScatter.getInstance(getContext())
                        .parse(ResourceTable.Layout_item_contact, null, false);
            } else {
                cpt = component;
            }

            ContactBean bean = dataList.get(i);
            Text name = (Text) cpt.findComponentById(ResourceTable.Id_name);
            name.setText(bean.name);

            Text phone = (Text) cpt.findComponentById(ResourceTable.Id_phone);
            phone.setText(bean.phone);

            cpt.setClickedListener(component1 -> {
                onItemClick(bean);
            });

            return cpt;
        }

    };

    @Override
    protected void onStart(Intent intent) {
        super.onStart(intent);
        setUIContent(ResourceTable.Layout_page_contact);


        initView();

        initData();
    }

    private void onItemClick(ContactBean bean) {
        Intent resultIntent = new Intent();
        resultIntent.setParam("data", bean);
        setResult(resultIntent);
        terminate();
    }

    private void initData() {
        ArrayList<ContactBean> list = ContactUtils.getContacts(getApplicationContext());
        provider.refresh(list);
    }

    private void initView() {
        findComponentById(ResourceTable.Id_back).setClickedListener(component -> {
            terminate();
        });

        list = (ListContainer) findComponentById(ResourceTable.Id_contentList);
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
    }


}
