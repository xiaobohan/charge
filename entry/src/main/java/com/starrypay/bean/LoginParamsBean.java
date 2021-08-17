package com.starrypay.bean;

public class LoginParamsBean {

    /**
     * 用户openID。
     */
    private String openID;

    /**
     * getNickName为0或不传时，按照匿名化帐号、昵称的先后顺序返回。
     * getNickName为1时，按照昵称、匿名化帐号的先后顺序返回。
     */
    private String displayName;

    /**
     * 头像。
     */
    private String headPictureURL;

    /**
     * 用户邮箱
     */
    private String email;

    public String getOpenID() {
        return openID;
    }

    public void setOpenID(String openID) {
        this.openID = openID;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getHeadPictureURL() {
        return headPictureURL;
    }

    public void setHeadPictureURL(String headPictureURL) {
        this.headPictureURL = headPictureURL;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
