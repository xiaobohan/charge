package com.starrypay.http;

import com.starrypay.bean.BaseRespBean;
import com.starrypay.bean.LoginParamsBean;
import com.starrypay.bean.PhoneChargeInfoBean;
import com.starrypay.bean.RechargeRecordBean;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

import java.util.List;

public interface Apis {

    static final String APP_ID = "9c878d48a2f54e46863de5c";


    @GET("api/v1/" + APP_ID + "/queryOrder")
    public Call<BaseRespBean<List<RechargeRecordBean>>> getOrderList();


    @POST("hmApi/api/v1/1/queryGoods")
    public Call<BaseRespBean<List<PhoneChargeInfoBean>>> getShopList();

    @POST("hmApi/api/v1/1/login")
    public Call<BaseRespBean<String>> login(@Body LoginParamsBean bean);

}
