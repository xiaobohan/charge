package com.starrypay.bean;

public class BaseRespBean<T> {

    public String message;
    public int code;
    public T data;


    public boolean isSuccess() {
        return 200 == code;
    }

}
