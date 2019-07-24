package com.tvmining.mytrtdemo;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.tencent.ilivesdk.ILiveCallBack;
import com.tencent.ilivesdk.ILiveSDK;
import com.tencent.ilivesdk.core.ILiveLoginManager;
import com.tencent.ilivesdk.core.ILiveRoomConfig;
import com.tencent.ilivesdk.core.ILiveRoomManager;
import com.tvmining.mytrtdemo.adapter.FuncAdapter;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
/*"sdkappid": 1400210753,
	"users": [{	}, */
    private String userId;
     private String userToken;
//	private String userId = "Web_trtc_02";
//    private String userToken = "eJxlj11PgzAARd-5FaSvGtMWS9VkD2NBHIKzzmnmS8NH0YYMutKRgfG-G1FjE*-rObk3991xXRc8JuuzrCjaQ2O4GZQA7pULIDj9g0rJkmeGe7r8B8VRSS14VhmhJ4gIIRhC25GlaIys5I-xLHJutCk4xJbUlTWflr5bziHECFLi2Yp8nWAassUyost4jPb3MdwkTynddAzju7IK8tR-iORbtm8Cugv96z5N5nKutnWFDsfbOG1ZMLKmFf1qQSITDuP2Ra9rfxV3SX4y3BA2m1mTRu7E7y16gTG5RBbthe5k20wChogg7MGvAOfD*QR4Bl7*";
    public int REQ_PERMISSION_CODE = 1001;

    private ListView lvFuncs;
    private FuncAdapter funcAdapter;
    private ArrayList<UserInfo> userList;
    private int mSdkAppId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
// 初始化iLiveSDK
        ILiveSDK.getInstance().initSdk(this, Constantss.SDKAPPID, Constantss.ACCOUNTTYPE);
// 初始化iLiveSDK房间管理模块
        ILiveRoomManager.getInstance().init(new ILiveRoomConfig());

	//修改调试11
        checkPermission();
        lvFuncs = (ListView) findViewById(R.id.lv_menu);

        loadJsonData(getJson());


        funcAdapter = new FuncAdapter(this, userList);
        lvFuncs.setAdapter(funcAdapter);

        lvFuncs.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                UserInfo info = userList.get(position);
                userId = info.getUserId();
                userToken = info.getUserToken();
                enterFunc(info);
            }
        });

    }
    ILiveCallBack callback = new ILiveCallBack() {
        @Override
        public void onSuccess(Object data) {
            Log.e("tag","onSuccess="+data.toString());
            Intent intent = new Intent(MainActivity.this,RoomActivity.class);
            intent.putExtra("userid",userId);
            startActivity(intent);
        }

        @Override
        public void onError(String module, int errCode, String errMsg) {
            Log.e("tag","onError="+errMsg);
        }

    };

    private boolean checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            List<String> permissions = new ArrayList<>();
            if (PackageManager.PERMISSION_GRANTED != ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            }
            if (PackageManager.PERMISSION_GRANTED != ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA)) {
                permissions.add(Manifest.permission.CAMERA);
            }
            if (PackageManager.PERMISSION_GRANTED != ActivityCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)) {
                permissions.add(Manifest.permission.RECORD_AUDIO);
            }
            if (PackageManager.PERMISSION_GRANTED != ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE)) {
                permissions.add(Manifest.permission.READ_PHONE_STATE);
            }
            if (PackageManager.PERMISSION_GRANTED != ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                permissions.add(Manifest.permission.READ_EXTERNAL_STORAGE);
            }
            if (permissions.size() != 0) {
                ActivityCompat.requestPermissions(MainActivity.this,
                        (String[]) permissions.toArray(new String[0]),
                        REQ_PERMISSION_CODE);
                return false;
            }
        }

        return true;
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1001:
                for (int ret : grantResults) {
                    if (PackageManager.PERMISSION_GRANTED != ret) {
                      //  DlgMgr.showMsg(getContext(), "用户没有允许需要的权限，使用可能会受到限制！");
                    }
                }
                break;
            default:
                break;
        }
    }



    private void enterFunc(UserInfo userInfo) {

        ILiveLoginManager.getInstance().iLiveLogin(userId, userToken, callback);
    }
    /** 解析JSON配置文件 */
    private void loadJsonData(String jsonData) {
        if (TextUtils.isEmpty(jsonData)) return;
        userList = new ArrayList<>();
        try {
            JSONTokener jsonTokener = new JSONTokener(jsonData);
            JSONObject msgJson = (JSONObject) jsonTokener.nextValue();
            mSdkAppId = msgJson.getInt("sdkappid");
            JSONArray jsonUsersArr = msgJson.getJSONArray("users");
            if (null != jsonUsersArr) {
                for (int i = 0; i < jsonUsersArr.length(); i++) {
                    JSONObject jsonUser = jsonUsersArr.getJSONObject(i);
                    UserInfo userInfo = new UserInfo();
                    userInfo.setUserId(jsonUser.getString("userId"));
                    userInfo.setUserToken(jsonUser.getString("userToken"));
                    userList.add(userInfo);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            mSdkAppId = -1;
        }
    }
    public  String getJson() {
        //将json数据变成字符串
        StringBuilder stringBuilder = new StringBuilder();
        try {
            //获取assets资源管理器
            InputStreamReader inputReader = new InputStreamReader(getResources().openRawResource(R.raw.config));
            //通过管理器打开文件并读取
            BufferedReader bf = new BufferedReader(inputReader);
            String line;
            while ((line = bf.readLine()) != null) {
                stringBuilder.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();

    }

}
