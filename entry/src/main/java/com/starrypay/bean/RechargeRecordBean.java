package com.starrypay.bean;

import com.google.gson.annotations.SerializedName;

public class RechargeRecordBean {


    @SerializedName("mobile")
    public String mobile;

    @SerializedName("goodsName")
    public String name;

    @SerializedName("createTime")
    public String createTime;

    @SerializedName("status")
    public int status;

    @SerializedName("orderStatus")
    public int orderStatus;

    public int money;

    public String getMontyStr() {
        return money / 100.00 + "元";
    }

    public boolean isPay() {
        return status == 1;
    }

}
