package com.starrypay.bean;

public class PhoneChargeInfoBean {

    /**
     * 商品ID
     */
    private Integer goodsId;
    /**
     * 商品结算单价
     */
    private String goodsPrice;

    /**
     * 商品名称
     */
    private String goodsName;
    /**
     * 商品详情
     */
    private String goodsInfo;

    /**
     * 市场价
     */
    private String marketPrice;

    public String getMarketPrice() {
        return marketPrice;
    }
    public void setMarketPrice(String marketPrice) {
        this.marketPrice = marketPrice;
    }
    public Integer getGoodsId() {
        return goodsId;
    }
    public void setGoodsId(Integer goodsId) {
        this.goodsId = goodsId;
    }
    public String getGoodsPrice() {
        return goodsPrice;
    }
    public void setGoodsPrice(String goodsPrice) {
        this.goodsPrice = goodsPrice;
    }

    public String getGoodsName() {
        return goodsName;
    }
    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }
    public String getGoodsInfo() {
        return goodsInfo;
    }
    public void setGoodsInfo(String goodsInfo) {
        this.goodsInfo = goodsInfo;
    }

}
