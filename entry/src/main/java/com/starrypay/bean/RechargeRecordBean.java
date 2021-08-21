package com.starrypay.bean;

import com.google.gson.annotations.SerializedName;

public class RechargeRecordBean {


    @SerializedName("mobile")
    public String mobile = "";

    @SerializedName("goodsName")
    public String name = "";

    @SerializedName("createTime")
    public String createTime = "";

    @SerializedName("status")
    public int status;

    @SerializedName("orderStatus")
    public int orderStatus;

    public int money ;

    public String getMontyStr() {
        return money / 100.00 + "元";
    }

    public boolean isPay() {
        return status == 2;
    }

    public RechargeRecordBean() {
    }

    public RechargeRecordBean(String mobile, String name, String createTime, int status, int orderStatus, int money) {
        this.mobile = mobile;
        this.name = name;
        this.createTime = createTime;
        this.status = status;
        this.orderStatus = orderStatus;
        this.money = money;
    }

}
