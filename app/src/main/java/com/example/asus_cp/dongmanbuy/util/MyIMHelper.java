package com.example.asus_cp.dongmanbuy.util;

import android.app.Notification;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;

import com.alibaba.mobileim.IYWLoginService;
import com.alibaba.mobileim.YWIMKit;
import com.alibaba.mobileim.YWLoginParam;
import com.alibaba.mobileim.channel.cloud.contact.YWProfileInfo;
import com.alibaba.mobileim.channel.event.IWxCallback;
import com.alibaba.mobileim.contact.IYWContact;
import com.alibaba.mobileim.contact.IYWContactHeadClickListener;
import com.alibaba.mobileim.contact.IYWContactService;
import com.alibaba.mobileim.contact.IYWCrossContactProfileCallback;
import com.alibaba.mobileim.conversation.EServiceContact;
import com.alibaba.mobileim.conversation.IYWConversationService;
import com.alibaba.mobileim.conversation.YWConversation;
import com.alibaba.mobileim.conversation.YWMessage;
import com.alibaba.mobileim.conversation.YWMessageChannel;
import com.alibaba.mobileim.lib.model.contact.Contact;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.StringRequest;
import com.example.asus_cp.dongmanbuy.R;
import com.example.asus_cp.dongmanbuy.constant.AliBaiChuanConstant;
import com.example.asus_cp.dongmanbuy.constant.MyConstant;
import com.example.asus_cp.dongmanbuy.model.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * IM操作的帮助类
 * Created by asus-cp on 2016-08-16.
 */
public class MyIMHelper {
    private String tag="MyIMHelper";
    private  Context context=MyApplication.getContext();
    private  boolean enableUseLocalUserProfile = true;
    private RequestQueue requestQueue;
    private String userInfoUrl="http://www.zmobuy.com/PHP/?url=/user/info";//用户信息的接口

    public MyIMHelper(){
        requestQueue=MyApplication.getRequestQueue();

    }

    /**
     * 弹出客服聊天对话框，并且向客服发送商品名称
     * @param s 需要发送的消息，可以是商品名称，或者不发
     */
    public void openKeFuLiaoTianAndSendMessage(final String s) {

        //开始登录
        String userid = "zmobuy1";
        String password = "123456";
        //initProfileCallback(userid);
        final YWIMKit mIMKit= MyYWIMKitHelper.getYwimkit(userid);//需要userId才能得到这个
        IYWLoginService loginService = mIMKit.getLoginService();
        YWLoginParam loginParam = YWLoginParam.createLoginParam(userid, password);
        loginService.login(loginParam, new IWxCallback() {
            @Override
            public void onSuccess(Object... arg0) {
                MyLog.d(tag, "登陆成功了");
                IYWConversationService conversationService = mIMKit.getConversationService();
                final EServiceContact contact = new EServiceContact("动漫卡哇伊周小沫", 161017570);
                //获取会话对象
                YWConversation conversation = conversationService.getConversation(contact);
                if("".equals(s)){
                    openKeFuActivity(mIMKit,contact);
                }else{
                    final YWMessage nameMessage=YWMessageChannel.createTextMessage("商品名称是：" + s);
                    conversation.getMessageSender().sendMessage(nameMessage, 10, new IWxCallback() {
                        @Override
                        public void onSuccess(Object... arg0) {
                            // 发送成功
                            MyLog.d(tag, "发送到千牛的消息是：" + nameMessage);
                            openKeFuActivity(mIMKit, contact);
                        }

                        @Override
                        public void onProgress(int arg0) {
                        }

                        @Override
                        public void onError(int arg0, String arg1) {
                            // 发送失败
                        }
                    });
                }

            }

            @Override
            public void onProgress(int arg0) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onError(int errCode, String description) {
                //如果登录失败，errCode为错误码,description是错误的具体描述信息
            }
        });

    }

    /**
     * 打开客服界面
     * @param mIMKit
     * @param contact
     */
    private void openKeFuActivity(YWIMKit mIMKit, EServiceContact contact) {
        Intent intent = mIMKit.getChattingActivityIntent(contact);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }



    //初始化，建议放在登录之前
    public  void initProfileCallback(String userid) {
        if (!enableUseLocalUserProfile){
            //目前SDK会自动获取导入到OpenIM的帐户昵称和头像，如果用户设置了回调，则SDK不会从服务器获取昵称和头像
            return;
        }
        YWIMKit imKit = MyYWIMKitHelper.getYwimkit(userid);
        if(imKit == null) {
            return;
        }
        final IYWContactService contactManager = imKit.getContactService();

        //头像点击的回调（开发者可以按需设置）
        contactManager.setContactHeadClickListener(new IYWContactHeadClickListener() {
            @Override
            public void onUserHeadClick(Fragment fragment, YWConversation conversation, String userId, String appKey, boolean isConversationListPage) {

            }

            @Override
            public void onTribeHeadClick(Fragment fragment, YWConversation conversation, long tribeId) {

            }

            @Override
            public void onCustomHeadClick(Fragment fragment, YWConversation conversation) {

            }
        });


        //设置用户信息回调，如果开发者已经把用户信息导入了IM服务器，则不需要再调用该方法，IMSDK会自动到IM服务器获取用户信息
        contactManager.setCrossContactProfileCallback(new IYWCrossContactProfileCallback() {

            /**
             * 设置头像点击事件, 该方法已废弃，后续请使用
             * {@link IYWContactService#)}
             * @param userId 需要打开页面的用户
             * @param appKey 需要返回个人信息的用户所属站点
             * @return
             * @deprecated
             */
            @Override
            public Intent onShowProfileActivity(String userId, String appKey) {
                return null;
            }

            @Override
            public void updateContactInfo(Contact contact) {

            }
            //此方法会在SDK需要显示头像和昵称的时候，调用。同一个用户会被多次调用的情况。
            //比如显示会话列表，显示聊天窗口时同一个用户都会被调用到。
            @Override
            public IYWContact onFetchContactInfo(final String userId, final String appKey) {
                //[需特殊处理的账号(如客服账号)] 或者 [昵称等Profile信息未导入云旺服务器]时。此为示例代码
                if(isNeedSpecialHandleAccount(userId, appKey)){
                    //首先从内存中获取用户信息
                    // todo 由于每次更新UI都会调用该方法，所以必现创建一个内存缓存，先从内存中拿用户信息，
                    // 内存中没有才访问数据库或者网络，如果不创建内存缓存，而是每次都访问数据库或者网络，会导致死循环！！
                    final IYWContact userInfo = mUserInfo.get(userId);
                    //若内存中有用户信息，则直接返回该用户信息
                    if (userInfo != null){
                        MyLog.d(tag , "onFetchContactInfo Hit! " + userInfo.toString());
                        return userInfo;
                    } else {
                        //若内存中没有用户信息则从服务器获取用户信息
                        //先创建一个临时的IYWContact对象保存到mUserInfo，这是为了避免服务器请求成功之前SDK又一次调用了onFetchContactInfo()方法，
                        // 从而导致再次触发服务器请求
                        IYWContact temp = new UserInfo(userId, null,userId,appKey);
                        mUserInfo.put(userId, temp);
                        List<String> uids = new ArrayList<String>();
                        uids.add(userId);
                        //从服务端获取用户信息，这里的示例的代码是从IM服务端获取用户信息的，在实际使用时开发者可以根据需求
                        // 从自己的服务器或者从本地数据库获取用户信息
                        MyLog.d(tag, "onFetchContactInfo Not-Hit! fetchUserProfile " + userId);

                        getDataFromIntenetAndSetView(userId,contactManager);
                        return temp;
                    }
                }
                //todo 当返回null时，SDK内部才会从云旺服务器获取对应的profile,因此这里必须返回null
                return null;
            }
        });
    }

    /**
     *  todo 1. 账号的昵称和头像已经导入云旺服务器的情况，一般情况直接返回false即可，除非需"特殊处理"的账号
     *      2.未导入云旺服务器的情况，返回true即可（建议导入）
     * @param userid
     * @param appkey
     * @return
     */
    private static boolean isNeedSpecialHandleAccount(String userid, String appkey){
//        if(!TextUtils.isEmpty(userid)&&userid.startsWith("云")){
//            return true;
//        }
//        return  false;

        return true;
    }

    // 这个只是个示例，开发者需要自己管理用户昵称和头像
    private static Map<String, IYWContact> mUserInfo = new HashMap<String, IYWContact>();

    private static class UserInfo implements IYWContact {

        private String mUserNick;    // 用户昵称
        private String mAvatarPath;    // 用户头像URL
        private int mLocalResId;//主要用于本地资源
        private String mUserId;    // 用户id
        private String mAppKey;    // 用户appKey

        public UserInfo(String nickName, String avatarPath,String userId,String appKey) {
            this.mUserNick = nickName;
            this.mAvatarPath = avatarPath;
            this.mUserId = userId;
            this.mAppKey = appKey;
        }

        @Override
        public String getAppKey() {
            return mAppKey;
        }

        @Override
        public String getAvatarPath() {
            if (mLocalResId != 0) {
                return mLocalResId + "";
            } else {
                return mAvatarPath;
            }
        }

        @Override
        public String getShowName() {
            return mUserNick;
        }

        @Override
        public String getUserId() {
            return mUserId;
        }

        @Override
        public String toString() {
            return "UserInfo{" +
                    "mUserNick='" + mUserNick + '\'' +
                    ", mAvatarPath='" + mAvatarPath + '\'' +
                    ", mUserId='" + mUserId + '\'' +
                    ", mAppKey='" + mAppKey + '\'' +
                    ", mLocalResId=" + mLocalResId +
                    '}';
        }
    }



    /**
     * 联网获取数据，并给view赋值
     */
    private void getDataFromIntenetAndSetView(final String userId, final IYWContactService contactManager) {
        SharedPreferences sharedPreferences=context.getSharedPreferences(MyConstant.USER_SHAREPREFRENCE_NAME,Context.MODE_APPEND);
        final String uid=sharedPreferences.getString(MyConstant.UID_KEY, null);
        final String sid=sharedPreferences.getString(MyConstant.SID_KEY, null);
        StringRequest userInfoRequest=new StringRequest(Request.Method.POST, userInfoUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        User user=parseJson(s);
                        MyLog.d(tag, "用户头像的数据：" + MyConstant.YU_MING + user.getPic());
                        IYWContact contact = new UserInfo(user.getName(), MyConstant.YU_MING + user.getPic(),userId, AliBaiChuanConstant.APP_KEY);
                        //todo 更新内存中的用户信息，这里必须要更新内存中的数据，以便IMSDK刷新UI时可以直接从内存中拿到数据
                        mUserInfo.remove(userId);   //移除临时的IYWContact对象
                        mUserInfo.put(userId, contact);  //保存从服务器获取到的数据
                        //todo 通知IMSDK更新UI
                        MyLog.d(tag, "fetchUserProfile notifyContactProfileUpdate! " + contact.toString());
                        contactManager.notifyContactProfileUpdate(contact.getUserId(), AliBaiChuanConstant.APP_KEY);

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map=new HashMap<String,String>();
                String json="{\"session\":{\"uid\":\""+uid+"\",\"sid\":\""+sid+"\"}}";
                map.put("json",json);
                return map;
            }
        };
        requestQueue.add(userInfoRequest);
    }


    /**
     * 解析json数据
     * @param s
     */
    private User parseJson(String s) {
        MyLog.d(tag, "返回的数据是：" + s);
        User user=new User();
        try {
            JSONObject jsonObject=new JSONObject(s);
            JSONObject jsonObject1=jsonObject.getJSONObject("data");
            user.setId(jsonObject1.getString("user_id"));
            user.setEmail(jsonObject1.getString("email"));
            user.setName(jsonObject1.getString("user_name"));
            user.setMoney(jsonObject1.getString("user_money"));
            user.setDongJieJinE(jsonObject1.getString("frozen_money"));
            user.setJiFen(jsonObject1.getString("pay_points"));
            user.setPhone(jsonObject1.getString("mobile_phone"));
            user.setPic(jsonObject1.getString("user_picture"));
            user.setSex(JsonHelper.decodeUnicode(jsonObject1.getString("sexcn")));
            user.setBankCards(jsonObject1.getString("bank_cards"));
            user.setHongBao(jsonObject1.getString("bonus"));
            user.setRankName(JsonHelper.decodeUnicode(jsonObject1.getString("rank_name")));
            user.setShouCangShu(jsonObject1.getString("collection_num"));
            JSONObject jsonObject2=jsonObject1.getJSONObject("order_num");
            user.setAwaitPay(jsonObject2.getString("await_pay"));
            user.setAwaitShip(jsonObject2.getString("await_ship"));

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return user;
    }

}
