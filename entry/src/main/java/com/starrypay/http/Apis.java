package com.starrypay.http;

import com.starrypay.bean.*;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

import java.util.List;
import java.util.Map;

public interface Apis {

    static final String APP_ID = "9c878d48a2f54e46863de5c";


    @POST("hmApi/api/v1/1/order/query")
    public Call<BaseRespBean<PageRespBean<RechargeRecordBean>>> getOrderList(@Body Map<String,String> map);

    @POST("hmApi/api/v1/1/queryGoods")
    public Call<BaseRespBean<List<PhoneChargeInfoBean>>> getShopList(@Body Map<String, String> map);

    @POST("hmApi/api/v1/1/login")
    public Call<BaseRespBean<String>> login(@Body LoginParamsBean bean);

    @POST("hmApi/api/v1/1/order/add")
    public Call<BaseRespBean<OrderInfoBean>> createOrder(@Body CreateOrderBean bean);


}
