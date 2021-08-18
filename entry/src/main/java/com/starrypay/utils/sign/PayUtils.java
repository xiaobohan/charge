package com.starrypay.utils.sign;

import com.google.gson.Gson;
import com.huawei.paysdk.entities.MercOrder;
import com.huawei.paysdk.entities.MercOrderApply;
import com.starrypay.bean.OrderInfoBean;

import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.*;


/**
 * 文件名称：com.huawei.gp.wiseandroid.gm.java
 * 版    权：Copyright Huawei Tech. Co. Ltd. All Rights Reserved.
 * 描    述：
 *
 * @author g00520444
 * @since 2021/1/4 10:21
 */
public class PayUtils {
    private static final String TAG = "SignUtil";

    // https://developer.android.com/reference/java/security/Signature 23才开始支持PSS
    public static final String SHA256_WITH_RSA_PSS_ALGORITHM = "SHA256WithRSA/PSS";

    private static final String EMPTY = "";

    /**
     * signType 取值，如果RSA加密算法用的是SHA256WithRSA，则取本值，老板取值涉及CP透传
     */
    public static final String SIGN_TYPE_RSA256 = "RSA256";

    /**
     * 字符串编码
     */
    private static final String CHARSET = "UTF-8";

    public static boolean isEmpty(String content) {
        return content == null || "".equals(content);
    }

    public static String sign(String content, String privateKey) {
        if (isEmpty(content) || isEmpty(privateKey)) {
            return EMPTY;
        }

        try {
            PKCS8EncodedKeySpec priPKCS8 = new PKCS8EncodedKeySpec(Base64.decode(privateKey, Base64.DEFAULT));//android.util包名
            KeyFactory keyf = KeyFactory.getInstance("RSA");
            PrivateKey priKey = keyf.generatePrivate(priPKCS8);
            Signature signature = Signature.getInstance(SHA256_WITH_RSA_PSS_ALGORITHM);
            signature.initSign(priKey);
            signature.update(content.getBytes(StandardCharsets.UTF_8));
            return Base64.encodeToString(signature.sign(), 0);
        } catch (GeneralSecurityException var3) {
            return EMPTY;
        } catch (Exception e) {
            return EMPTY;
        }

    }

    public static boolean verifySign(String content, String sign, String publicKey) {
        if (isEmpty(content) || isEmpty(publicKey)) {
            return false;
        }

        try {
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            byte[] encodedKey = Base64.decode(publicKey, Base64.DEFAULT);
            PublicKey pubKey = keyFactory.generatePublic(new X509EncodedKeySpec(encodedKey));
            Signature signature = Signature.getInstance(SHA256_WITH_RSA_PSS_ALGORITHM);
            signature.initVerify(pubKey);
            signature.update(content.getBytes(CHARSET));
            return signature.verify(Base64.decode(sign, Base64.DEFAULT));
        } catch (Exception e) {
        }
        return false;
    }

    /**
     * 对数组里的每一个值从a到z的顺序排序
     *
     * @param params 参数
     * @return String
     */
    public static String getSignData(JSONObject params) {
        if (params == null) {
            return null;
        }
        return getSignData(getSignMap(params));
    }

    /**
     * 对数组里的每一个值从a到z的顺序排序
     *
     * @param params map
     * @return String
     */
    public static String getSignData(Map<String, String> params) {
        StringBuffer content = new StringBuffer();

        // 按照key做排序
        List<String> keys = new ArrayList<String>(params.keySet());
        Collections.sort(keys);
        for (int i = 0; i < keys.size(); i++) {
            String key = (String) keys.get(i);
            if ("sign".equals(key)) {
                continue;
            }
            String value = (String) params.get(key);
            if (value != null && !value.isEmpty()) {
                content.append((i == 0 ? "" : "&") + key + "=" + value);
            } else {
                continue;
            }
        }
        return content.toString();
    }

    /**
     * 将json组转成map 方法表述
     *
     * @param params JSONObject
     * @return Map<String, String>
     */
    private static Map<String, String> getSignMap(JSONObject params) {
        Map<String, String> map = new HashMap<String, String>();

        Iterator<String> it = params.keys();

        String key = null;
        while (it.hasNext()) {
            // 遍历JSONObject
            try {
                key = it.next();
                if ("sign".equals(key)) {
                    continue;
                }

                Object value = params.get(key);
                // 判断是否integer类型
                if (value instanceof Integer) {
                    if ((Integer) value < 0) {
                        continue;
                    }
                    value = (Integer) value + "";
                }

                if (value instanceof ArrayList) {
                    List<Object> payInfors = (List<Object>) value;
                    StringBuilder sb = new StringBuilder();
                    for (Object payInfor : payInfors) {
                        Map<String, String> tmap = (Map<String, String>) payInfor;
                        if (sb.length() > 0) {
                            sb.append(",");
                        }
                        sb.append(getSignData(tmap));
                    }

                    value = sb.toString();
                }

                if (value instanceof JSONArray) {
                    JSONArray payInfors = (JSONArray) value;
                    StringBuilder sb = new StringBuilder();
                    JSONObject payInfo;
                    int length = payInfors.length();
                    for (int i = 0; i < length; i++) {
                        payInfo = payInfors.getJSONObject(i);
                        if (sb.length() > 0) {
                            sb.append(",");
                        }

                        sb.append(getSignData(payInfo));
                    }
                    value = sb.toString();
                }

                if (value instanceof JSONObject) {
                    value = getSignData((JSONObject) value);
                }

                if (value == null) {
                    continue;
                }

                map.put(key, value.toString());
            } catch (JSONException e) {
                //   LogC.e(TAG, "json exception", false);
            }
        }

        return map;
    }

    private static final String PRIVATE_KEY = "MIIEowIBAAKCAQEAns0bHXeLvLaL2rcIPLWvFKiKmcrz8j5jR3wvXg5Sdf8rPwCFqduf5i5MiEVF2pGI7X55Uv27XtdHoQ8x56HCwWcMNZarObNsY+wwqXV+pCY3QIdWDLbeRtUDY8hqLGAnAdoZDRlKCeZUchofPncl+76M9AdUvif2RNUyPlsV0c9LIHeIft2YJWdF9YTjs/UPB+z1IgWmgfZu13sNYaU4DQ385GHQ1EJQMja5o4PQrFF5EGCjZxJPBIJmV0SkNhLnfHK0XREiVzz2r9LeGwsakrgofkQrNwSxxKIFNpmWZDIpdVhKiEW17Cr1QDBG/z8cUtdhbRq7cSyG87QN2JvtfQIDAQABAoIBAHWe+fyDdW8bzs77TxCYicvFYpOzLak56JMkOnlyJkXb8I5Dtr0vPWpi1LECjlDsBv3R9c8wvL3sutNiy9YDjJtv1i1DIiQk7527bfe2XUz8OWSEWYolUNIqKM6ZvXqM5ZeLkY6QMrTOc7HoA0hjPDEQ3JpLa8tFEjW9q+9fy/OQadlVwA9SE8SzSSWKQeJZCZh+HCWBzVQ8H4wZwbbFgzHWPOqi7r9JONVSfZN2kXiqU/DEkijB8HHPXfGUWsTHQexD0M7Ka38qiw/occGpvEvF6+hHIVOyQHUDQhtR5PZGKb0iJEb+2sApf0nURiafDv5BhWpeqlpKacM6GSlVLYkCgYEA0vFJNcojvE3rv9tTpJQcJg3gLsH4kNJZ8HChnGUDQ5rK7VvQ8i+VmRA4eU9JrOq5y+JJwYEW9Rbucaxr6MrOjSVJ1FL1YR3QPOjCu4yj92aCjNbBzHrDWGjYoi8OW969PBVhOJgAxjWmBsxiZ8IyqADrOiDSPAapblgKocPxTz8CgYEAwLijkrFBUL8ySvqVnsmjHiZHA+QAXtf9guzsVUEnWaHwoKG4/vMFrEQTb8WvwCuN7WX9IYOoKrU5Kd/2a/KaSm/v8bwv43l1coQOk2INlAYReUdc2r0fVRSB+VGWeIeSJNklkR14Xh6Z8fJ44/AebfE9opwzoX1+YY6SsKyv0EMCgYAo2fmnpSIOkbiaS0uDj/tzkzRbWc1dnp80ZVunAhkDurKaDTIF9Rkvr2iAdc564mphBc3q36kmhe5frViomL4vr40AxaAn0rJFWX58+9u1SfD2fZDCnPO/524s2pgZSqWzQSC26RkdSb6Biot0NLPCvYrQkxKj9VZSvFIyydOt2QKBgHfa3K/AWTSUk/yZdy+3iyCXAZdj1u8lON6R5jDnQke2NOZRbbHxBdgqxkO/GBxVegDoatLLBGz7I7/tL13XFSmyD/Gw7b90rBw6EK6MTpF+bKOWRCMtdyz8ghDCMuhmt/b+rSRgxZkTQv57o79ueB475+8XYezPFSgq+QOzgevtAoGBAJde3JeGSAlyuK8rX0trTq/PzEXoxBswlSbhfJdgqVFDc/dhHJI5+N4lclyYJs8rzWFjMMFWTzdzsIGUWexOqNpW9buSuXRFx5+hbylOcuAcsXhCX6AZ9d1nI6gSNH1vY4hT0DinLgk17GloybY8Uov5OSZXns1eLXTKm2bSL/Bq";


    public static MercOrderApply getMercOrderApply(OrderInfoBean orderInfo) {
        MercOrderApply apply = new MercOrderApply();

        apply.setMercOrder(orderInfo.mercOrder);

        // 添加分账类型
        apply.setAllocationType(orderInfo.allocationType);//一级商户不分
        apply.setPayload(orderInfo.payload);

        apply.setReturnUrl(orderInfo.returnUrl);
        apply.setCallbackUrl(orderInfo.callbackUrl);

        apply.setAuthId(orderInfo.authId);
        apply.setSignType(orderInfo.signType);

        try {
            JSONObject jsonObject = new JSONObject(new Gson().toJson(orderInfo));
            String signStr = PayUtils.getSignData(jsonObject);
            String signData = PayUtils.sign(signStr, PRIVATE_KEY);
            apply.setSign(signData);
        } catch (JSONException e) {
            return null;
        }

        return apply;
    }


}