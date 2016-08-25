package com.patrick.caracal.net;

import com.wilddog.client.DataSnapshot;
import com.wilddog.client.ValueEventListener;
import com.wilddog.client.Wilddog;
import com.wilddog.client.WilddogError;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

/**
 * Created by Patrick on 16/8/25.
 */
public class WilddogApi {

    private static final String WILDDOG_URL = "https://caracal.wilddogio.com/";
    private static final String EXPRESS_URL = WILDDOG_URL+"express";
    private static final String TRACE_URL = WILDDOG_URL+"trace";

    /**
     * QQ登录
     */
    public void loginQQ(){}

    /**
     * 邮箱登录
     */
    public void loginEmail(){}

    /**
     * 查询快递数据
     */
    public void queryExpress(String no, final Callback callback){
        Wilddog ref = new Wilddog(EXPRESS_URL);
        ref.child(no).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null){
                    try {

                        JSONObject json = new JSONObject();
                        json.put(dataSnapshot.getKey(), new JSONObject((Map) dataSnapshot.getValue()));
                        System.out.println(json.toString());

                        callback.onSuccess(json);

                    } catch (JSONException e) {
                        e.printStackTrace();
//                        callback.onFail(e);
                    }
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
    public void queryTrace(String no, final Callback callback){
        Wilddog ref = new Wilddog(TRACE_URL);
        ref.child(no).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null){
                    try {

                        JSONObject json = new JSONObject();
                        json.put(dataSnapshot.getKey(), new JSONObject((Map) dataSnapshot.getValue()));
                        System.out.println(json.toString());

                        callback.onSuccess(json);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onCancelled(WilddogError wilddogError) {
                callback.onFail(wilddogError.toException());
            }
        });
    }

    /**
     *
     */
    public interface Callback{

        void onSuccess(JSONObject json);

        void onFail(Exception e);

    }
}