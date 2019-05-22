package com.tvmining.mytrtdemo;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.tencent.TIMCallBack;
import com.tencent.TIMUser;
import com.tencent.av.TIMAvManager;
import com.tencent.ilivesdk.ILiveCallBack;
import com.tencent.ilivesdk.core.ILiveRoomManager;
import com.tencent.ilivesdk.core.ILiveRoomOption;
import com.tencent.ilivesdk.view.AVRootView;

import java.util.ArrayList;
import java.util.List;

public class RoomActivity extends Activity implements ILiveRoomOption.onExceptionListener,ILiveRoomOption.onRoomDisconnectListener{
    AVRootView avRootView;
    TextView tv_msg;
    String roomKey = "11111111";
    private String userId = "Web_trtc_01";
    int roomid = 100;
    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room);
        avRootView = findViewById(R.id.av_root_view);
        tv_msg = findViewById(R.id.tv_msg);
        ILiveRoomManager.getInstance().initAvRootView(avRootView);
        String id = getIntent().getStringExtra("userid");
        if(userId.equals(id))
        sendRequest();
        else
        receive();
        List<TIMUser> list = new ArrayList<>();
        TIMUser user = new TIMUser();
//        TIMAvManager.getInstance().requestMultiVideoInvitation(0,0,0,0,null,list,timCallBack);//requestMultiVideoInvitation
        tv_msg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
    }

    /**
     *
     *发起视频请求
     */
    private void sendRequest(){
        ILiveRoomOption option = new ILiveRoomOption()
                .imsupport(false)       // 不需要IM功能
                .privateMapKey(roomKey)          // 设置进房签名
                .controlRole("Host")  // 使用Host角色
                .exceptionListener(this)  // 监听异常事件处理
                .roomDisconnectListener(this)   // 监听房间中断事件
                 .autoCamera(false);       // 进房间后不需要打开摄像头
        ILiveRoomManager.getInstance().createRoom(roomid, option, callback);
    }

    /**
     * 接收请求
     */
    private void receive(){
        ILiveRoomOption option = new ILiveRoomOption()
                 .imsupport(false)       // 不需要IM功能
                .privateMapKey(roomKey) // 进房签名
                .exceptionListener(this)  // 监听异常事件处理
                .roomDisconnectListener(this)   // 监听房间中断事件
                .controlRole("Guest")  // 使用Guest角色
                .autoCamera(true)       // 进房间后不需要打开摄像头
                .autoMic(false);         // 进房间后不需打开Mic

        ILiveRoomManager.getInstance().createRoom(roomid, option, callback);
    }
    @Override
    public void onException(int exceptionId, int errCode, String errMsg) {
        Log.e("tag","onException,"+errMsg);
    }

    @Override
    public void onRoomDisconnect(int errCode, String errMsg) {
        Log.e("tag","onRoomDisconnect,"+errMsg);
    }
    ILiveCallBack callback = new ILiveCallBack() {
        public void onSuccess(Object data) {
            Log.e("tag","onSuccess="+data.toString());
        }

        @Override
        public void onError(String module, int errCode, String errMsg) {
            Log.e("tag","onError="+errMsg);
        }
    };
        TIMCallBack timCallBack = new TIMCallBack() {
            @Override
            public void onError(int i, String s) {
                Log.e("tag","请求连接失败+"+s);
            }

            @Override
            public void onSuccess() {
               Log.e("tag","请求连接成功");
            }
        };
    @Override
    protected void onDestroy() {
        ILiveRoomManager.getInstance().onDestory();
        super.onDestroy();
    }
}
