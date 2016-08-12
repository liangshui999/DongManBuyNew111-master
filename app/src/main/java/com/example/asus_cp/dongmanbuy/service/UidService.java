package com.example.asus_cp.dongmanbuy.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.asus_cp.dongmanbuy.constant.MyConstant;
import com.example.asus_cp.dongmanbuy.util.MyApplication;
import com.example.asus_cp.dongmanbuy.util.MyLog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * 定时任务：定时先清除uid，然后定时获取uid
 * Created by asus-cp on 2016-07-12.
 */
public class UidService extends Service{

    private String tag="UidService";
    private Context context=MyApplication.getContext();
    private RequestQueue requestQueue=MyApplication.getRequestQueue();
    private String loginUrl="http://www.zmobuy.com/PHP/index.php?url=/user/signin";

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    private long time=5*60*1000;

    private Handler handler=new MyHandler();

    class MyHandler extends Handler{
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1:
                    String userName=sharedPreferences.getString(MyConstant.USER_NAME, null);
                    String passWord=sharedPreferences.getString(MyConstant.PASS_WORD,null);
                    MyLog.d(tag,"userName为空");
                    if(userName!=null && !userName.isEmpty()){
                        getUidSid(userName, passWord);
                        MyLog.d(tag, "userName不空");
                    }
                    handler.sendEmptyMessageDelayed(1,time);
                    break;
            }
        }
    }




    @Override
    public void onCreate() {
        super.onCreate();
        sharedPreferences=getSharedPreferences(MyConstant.USER_SHAREPREFRENCE_NAME, MODE_APPEND);
        editor=sharedPreferences.edit();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        String userName=sharedPreferences.getString(MyConstant.USER_NAME, null);
        String passWord=sharedPreferences.getString(MyConstant.PASS_WORD,null);
        MyLog.d(tag,"userName为空");
        if(userName!=null && !userName.isEmpty()){
            getUidSid(userName, passWord);
            MyLog.d(tag, "userName不空");
        }

        Message message = handler.obtainMessage();
        message.what = 1;
        handler.sendMessageDelayed(message, time);
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        clearSharePerefrences();
    }

    /**
     * 获取uid和sid
     * @param userName
     * @param passWord
     */
    private void getUidSid(final String userName, final String passWord) {
        clearSharePerefrences();
        StringRequest loginRequest=new StringRequest(Request.Method.POST, loginUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                MyLog.d(tag, "登录的数据返回:" + s);
                try {
                    JSONObject jsonObject=new JSONObject(s);
                    JSONObject jsonObject1=jsonObject.getJSONObject("data");
                    JSONObject jsonObject2=jsonObject1.getJSONObject("session");
                    String sid=jsonObject2.getString("sid");
                    String uid=jsonObject2.getString("uid");
                    if(!sid.isEmpty() && !sid.equals("")){
                        writeToSharePreferences(uid, MyConstant.UID_KEY);
                        writeToSharePreferences(sid, MyConstant.SID_KEY);
                    }else {
                        // Toast.makeText(context,"登录失败",Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    // Toast.makeText(context,"登录失败",Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                //Toast.makeText(LoginActivity.this,"登录失败",Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map = new HashMap<String,String>();
                map.put("name",userName);
                map.put("password", passWord);
                //MyLog.d(tag,MyMd5.md5encode(password));
                return map;
            }
        };
        requestQueue.add(loginRequest);
    }


    /**
     * 清空shareprefrence
     */
    public void clearSharePerefrences(){
        editor.remove(MyConstant.UID_KEY);
        editor.remove(MyConstant.SID_KEY);
        editor.apply();
    }

    /**
     * 将数据写入shareprefrence里面
     * @param s 要写入的字符串
     * @param key 写入时的键
     */
    public void writeToSharePreferences(String s,String key){
        editor.putString(key, s);
        editor.apply();
    }

}
