package com.patrick.caracal.net;

import okhttp3.OkHttpClient;

/**
 * Created by Patrick on 16/7/26.
 */
public class Api {

    //Api地址
    private static final String API_URL = "www.caracal.club/api/";

    private static final OkHttpClient client = new OkHttpClient();

    /**
     * 查询快递
     *
     * 查询的快递前提必须订阅，如果没订阅去查询，会返回string "null"
     *
     * @param expNo 快递单号
     * @param company 快递公司编号
     */
    public void query(String expNo,String company){

    }

    /**
     * 订阅快递
     *
     * @param expNo 快递单号
     * @param company 快递公司编号
     */
    public void subscribe(String expNo,String company){

    }


}
