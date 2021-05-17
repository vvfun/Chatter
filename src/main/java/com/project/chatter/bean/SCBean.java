package com.project.chatter.bean;


import android.text.TextUtils;

public class SCBean extends BaseBean {


    private String agoId;
    private String avatar;
    private String chatNo;
    private String key;
    private int levelType;
    private String nickname;
    private int price;
    private int remoteId;
    private String remoteUid;
    private String requestId;
    private String roomId;
    private String temp;
    private String token;
    private String type;
    private String sdp;
    private int version;

    boolean isApiClient;

    public String getAgoId() {
        return agoId;
    }

    public void setAgoId(String agoId) {
        this.agoId = agoId;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getChatNo() {
        return chatNo;
    }

    public void setChatNo(String chatNo) {
        this.chatNo = chatNo;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public int getLevelType() {
        return levelType;
    }

    public void setLevelType(int levelType) {
        this.levelType = levelType;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getRemoteId() {
        return remoteId;
    }

    public void setRemoteId(int remoteId) {
        this.remoteId = remoteId;
    }

    public String getRemoteUid() {
        return remoteUid;
    }

    public void setRemoteUid(String remoteUid) {
        this.remoteUid = remoteUid;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public String getTemp() {
        return temp;
    }

    public void setTemp(String temp) {
        this.temp = temp;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public boolean isApiClient() {
        isApiClient = !TextUtils.isEmpty(agoId);
        return isApiClient;
    }

    public void setApiClient(boolean apiClient) {
        isApiClient = apiClient;
    }

    public String getSdp() {
        return sdp;
    }

    public void setSdp(String sdp) {
        this.sdp = sdp;
    }
}
