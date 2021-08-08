package com.starrypay.component;

import com.starrypay.provider.GridAdapter;
import ohos.agp.components.AttrSet;
import ohos.agp.components.Component;
import ohos.agp.components.TableLayout;
import ohos.app.Context;

/**
 * The GridView
 */
public class GridView extends TableLayout {
    public GridView(Context context) {
        super(context);
    }

    public GridView(Context context, AttrSet attrSet) {
        super(context, attrSet);
    }

    public GridView(Context context, AttrSet attrSet, String styleName) {
        super(context, attrSet, styleName);
    }

    /**
     * The setAdapter
     *
     * @param adapter         adapter
     * @param clickedListener longClickedListener
     */
    void setAdapter(GridAdapter adapter, ClickedListener clickedListener) {
        removeAllComponents();
        for (int i = 0; i < adapter.getComponentList().size(); i++) {
            Component component = adapter.getComponentList().get(i);
            component.setTag(i);
            component.setClickedListener(clickedListener);
            addComponent(adapter.getComponentList().get(i));
        }
    }
}
