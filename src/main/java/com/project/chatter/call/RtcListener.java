package com.project.chatter.call;

public interface RtcListener {
    public void connectRoom(String token, String roomId);

    public void chatMessage(String message);
    public void MSG(String key, String value);

    public void endVideoChat();
    public void commit(String key, String requestId);

    public void requestGift(String remoteUid);
}
