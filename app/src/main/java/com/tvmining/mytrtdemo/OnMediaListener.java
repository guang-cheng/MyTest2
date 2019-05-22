package com.tvmining.mytrtdemo;

import io.agora.rtc.IRtcEngineEventHandler;

/**
 * 通话时的接口
 */
public interface OnMediaListener {
    void onJoinSuccess(String channel, int uid, int elapsed);
    void onRejoinSuccess(String channel, int uid, int elapsed);
    void onLeaveChannel(IRtcEngineEventHandler.RtcStats stats);
    void onUserJoined(int uid, int elapsed);
    void onUserOffline(int uid, int reason);
}