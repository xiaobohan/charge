package com.starrypay.utils.sign;

/**
* 功能描述
*
* @author y00452957
* @since 2021-04-09
*/
public class JSONException extends Exception {
 
    public JSONException(String s) {
        super(s);
    }
 
    public JSONException(String message, Throwable cause) {
        super(message, cause);
    }
 
    public JSONException(Throwable cause) {
        super(cause);
    }
 
}