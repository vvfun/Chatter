package com.project.chatter.bean;

public class CallHttpBean {


    /**
     * roomId : 16177868161096176
     * remoteUid : 3001611888633527
     * agoId : null
     * id : null
     * token : null
     * msg : null
     * second : 8
     */

    private String roomId;
    private long remoteUid;
    private String agoId;
    private int id;
    private String token;
    private String msg;
    private String type;
    private int second;

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public long getRemoteUid() {
        return remoteUid;
    }

    public void setRemoteUid(long remoteUid) {
        this.remoteUid = remoteUid;
    }

    public String getAgoId() {
        return agoId;
    }

    public void setAgoId(String agoId) {
        this.agoId = agoId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getSecond() {
        return second;
    }

    public void setSecond(int second) {
        this.second = second;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
