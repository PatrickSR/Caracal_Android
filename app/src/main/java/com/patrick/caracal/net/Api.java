package com.patrick.caracal.net;

import com.jiongbull.jlog.JLog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.TimeUnit;

import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * Created by Patrick on 16/7/26.
 */
public class Api {

    //Api地址
    private static final String API_URL = "http://www.caracal.club/api/";

    //订阅地址
    private static final String SUBSCRIBE_URL = API_URL + "subscribe";

    //查询快递地址
    private static final String QUERY_URL = API_URL + "query";

    //查询快递单对应的公司
    private static final String QUERY_EXPRESS_FROM_URL = API_URL + "queryExpressFrom";


    private OkHttpClient client;

    public Api(){
        //设置60s超时
        client = new OkHttpClient.Builder()
                .connectTimeout(60,TimeUnit.SECONDS)
                .readTimeout(60,TimeUnit.SECONDS)
                .writeTimeout(60,TimeUnit.SECONDS)
                .build();
    }

    /**
     * 查询快递
     * <p/>
     * 查询的快递前提必须订阅，如果没订阅去查询，会返回string "null"
     *
     * @param expNo   快递单号
     * @param company 快递公司编号
     */
    public void query(String expNo, String company, Callback callback) {
        String url = QUERY_URL + "/" + company + "/" + expNo;

        Request request = new Request.Builder()
                .url(url)
                .build();

        sendRequest(request, callback);
    }

    /**
     * 订阅快递
     *
     * @param expNo   快递单号
     * @param company 快递公司编号
     */
    public void subscribe(String expNo, String company, Callback callback) {
        try {
            MediaType mediaType = MediaType.parse("application/json");

            JSONObject jsonBody = new JSONObject();
            jsonBody.put("company", company);
            jsonBody.put("code", expNo);

            RequestBody body = RequestBody.create(mediaType, jsonBody.toString());

            Request request = new Request.Builder()
                    .url(SUBSCRIBE_URL)
                    .post(body)
                    .addHeader("content-type", "application/json")
                    .build();

            sendRequest(request,callback);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 查询快递来源哪个公司
     *
     * @param expNo
     */
    public void queryCompanyWithExpNo(String expNo,Callback callback) {
        String url = QUERY_EXPRESS_FROM_URL + "/"+expNo;
        Request request = new Request.Builder()
                .url(url)
                .build();

        sendRequest(request,callback);
    }


    /**
     * 发送请求
     *
     * @param req
     * @param callback
     */
    private void sendRequest(Request req, Callback callback) {
        JLog.d("Send Request --->" + req.toString());
        client.newCall(req).enqueue(callback);
    }

}
