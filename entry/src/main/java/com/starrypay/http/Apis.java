package com.starrypay.http;

import com.starrypay.bean.BaseRespBean;
import com.starrypay.bean.RechargeRecordBean;
import retrofit2.Call;
import retrofit2.http.GET;

import java.util.List;

public interface Apis {

    static final String APP_ID = "9c878d48a2f54e46863de5c";

    @GET("api/v1/" + APP_ID + "/queryGoods")
    public void getShopList();


    @GET("api/v1/" + APP_ID + "/queryOrder")
    public Call<BaseRespBean<List<RechargeRecordBean>>> getOrderList();

}
