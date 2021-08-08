package com.starrypay.model;

/**
 * Grid item model
 */
public class GridItemInfo {

    private final String originPrice;
    private final String chargePrice;

    private boolean isSelect = false;

    /**
     * Item data model Constructor
     *
     * @param itemText item text
     */
    public GridItemInfo(String itemText, String chargePrice) {
        this.originPrice = itemText;
        this.chargePrice = chargePrice;
    }

    public String getOriginPrice() {
        return originPrice;
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
