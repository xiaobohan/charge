package com.starrypay.bean;

public class CreateOrderBean {

    public String openId;

    public int goodsId;

    public String mobile;

    public CreateOrderBean(String openId, int goodsId, String mobile) {
        this.openId = openId;
        this.goodsId = goodsId;
        this.mobile = mobile;
    }
}
