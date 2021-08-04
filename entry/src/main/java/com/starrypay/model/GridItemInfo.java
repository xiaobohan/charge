package com.starrypay.model;

/**
 * Grid item model
 */
public class GridItemInfo {

    private final String originPrice;
    private final String chargePrice;
    private final String tag;

    private boolean isSelect = false;

    /**
     * Item data model Constructor
     *
     * @param itemText item text
     * @param tag      component tag
     */
    public GridItemInfo(String itemText, String chargePrice, String tag) {
        this.originPrice = itemText;
        this.chargePrice = chargePrice;
        this.tag = tag;
    }

    public String getOriginPrice() {
        return originPrice;
    }

    public String getTag() {
        return tag;
    }

    public String getChargePrice() {
        return chargePrice;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }
}
