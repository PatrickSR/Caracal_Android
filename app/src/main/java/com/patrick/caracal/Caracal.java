package com.patrick.caracal;

import android.content.Context;

import com.patrick.caracal.net.Api;

import java.io.IOException;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by Patrick on 16/7/26.
 */
public class Caracal {

    private static Caracal caracal;

    private Api api;

    private Caracal() {
        api = new Api();

    }

    public static Caracal getInstance() {
        if (caracal == null) {
            caracal = new Caracal();
        }
        return caracal;
    }

    public static void init(Context context){
        RealmConfiguration config = new RealmConfiguration.Builder(context)
                .deleteRealmIfMigrationNeeded()
                .build();
        Realm.setDefaultConfiguration(config);
    }

    /**
     * 添加/订阅 快递
     */
    public void subExpress(String expNo, String company, final CaracalCallback caracalCallback) {
        api.subscribe(expNo, company, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                caracalCallback.onFail(e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String resp = response.body().string();
                //订阅成功后，保存到DB上
                Realm realm = Realm.getDefaultInstance();
                realm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                    }
                });
                caracalCallback.onSuccess(resp);
            }
        });
    }

    // 删除 快递 从DB上删除
    public void delExpress() {


    }

    // 归档 快递 更改快递状态
    public void keepExpress() {
    }

    // 获取全部快递单
    public void getAllExpress() {
    }

    // 获取单个快递单
    public void getExpress(String expNo) {
    }

    // 查询快递属于哪个公司
    public void queryExpressCompany(String expNo) {
    }

    // 根据快递公司编码查询公司信息
    public void queryCompany(String companyCode) {
    }

    // 刷新全部快递单
    public void refresh() {
    }

    interface CaracalCallback{
        void onSuccess(String... s);

        void onFail(Exception e);
    }
}
