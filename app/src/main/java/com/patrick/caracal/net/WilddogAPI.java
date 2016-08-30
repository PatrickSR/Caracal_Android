package com.patrick.caracal.net;

import com.patrick.caracal.model.Express;
import com.wilddog.client.DataSnapshot;
import com.wilddog.client.ValueEventListener;
import com.wilddog.client.Wilddog;
import com.wilddog.client.WilddogError;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Patrick on 16/8/25.
 */
public class WilddogApi {

    private static final String WILDDOG_URL = "https://caracal.wilddogio.com/";
    private static final String EXPRESS_URL = WILDDOG_URL + "express";
    private static final String TRACE_URL = WILDDOG_URL + "trace";
    private static final String USER_URL = WILDDOG_URL + "user";

    /**
     * QQ登录
     */
    public void loginQQ(String accessToken,String openId, Wilddog.AuthResultHandler qqLoginCallback) {
        Wilddog loginRef = new Wilddog(WILDDOG_URL);

        Map<String, String> options = new HashMap<>();
        options.put("access_token", accessToken);
        options.put("openId", openId);

        loginRef.authWithOAuthToken("qq",options,qqLoginCallback);
    }

    /**
     * 邮箱登录
     */
    public void loginEmail(String email, String password, Wilddog.AuthResultHandler loginCallback) {
        Wilddog loginRef = new Wilddog(WILDDOG_URL);
        loginRef.authWithPassword(email, password, loginCallback);
    }

    /**
     * 查询快递数据
     */
    public void queryExpress(String no, final Callback callback) {
        Wilddog ref = new Wilddog(EXPRESS_URL);
        ref.child(no).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    //DataSnapshot to json
                    JSONObject json = new JSONObject((Map) dataSnapshot.getValue());
                    System.out.println(json.toString());
                    callback.onSuccess(json);

                }
            }

            @Override
            public void onCancelled(WilddogError wilddogError) {
                callback.onFail(wilddogError.toException());
            }
        });
    }

    /**
     * 查询单个快递跟踪痕迹
     */
    public void queryTrace(String no, final Callback callback) {
        Wilddog ref = new Wilddog(TRACE_URL);
        ref.child(no).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {

                    JSONObject json = new JSONObject((Map) dataSnapshot.getValue());
                    System.out.println(json.toString());

                    callback.onSuccess(json);

                }
            }

            @Override
            public void onCancelled(WilddogError wilddogError) {
                callback.onFail(wilddogError.toException());
            }
        });
    }

    /**
     * 把某个快递归档
     */
    public void fileExpress(String uid, Express express, Callback callback) {

    }


    /**
     *
     */
    public interface Callback {

        void onSuccess(JSONObject json);

        void onFail(Exception e);

    }
}
