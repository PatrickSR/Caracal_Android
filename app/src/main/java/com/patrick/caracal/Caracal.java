package com.patrick.caracal;

import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.RawRes;

import com.jiongbull.jlog.JLog;
import com.patrick.caracal.model.Company;
import com.patrick.caracal.model.Express;
import com.patrick.caracal.net.Api;

import java.io.IOException;
import java.io.InputStream;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;
import io.realm.exceptions.RealmException;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by Patrick on 16/7/26.
 */
public class Caracal {

    private static Caracal caracal;

    private static Context applicationContext;

    private Context context;

    private Api api;

    private Realm realm;

    private Caracal(Context context) {
        api = new Api();
        this.context = context;
        if (realm == null) realm = Realm.getDefaultInstance();
    }

    private void closeRealm(){
        if (realm != null) realm.close();
    }

    public static Caracal getInstance() {
        if (caracal == null) {
            caracal = new Caracal(applicationContext);
        }
        return caracal;
    }

    public static void init(Context context) {
        Caracal.applicationContext = context;

        RealmConfiguration config = new RealmConfiguration.Builder(context)
                .deleteRealmIfMigrationNeeded()
                .build();
        Realm.setDefaultConfiguration(config);
    }

    public static void release(){
        caracal.closeRealm();
    }

    /**
     * 添加/订阅 快递
     */
    public void subExpress(String expNo, String company, final ResultCallback<String> resultCallback) {
        api.subscribe(expNo, company, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                resultCallback.onFail(e);
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
                resultCallback.onSuccess(resp);
            }
        });
    }

    /**
     * 删除 快递 从DB上删除
     *
     * @param expNo 快递单号
     */
    public void delExpress(final String expNo) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                Express express = realm.where(Express.class).equalTo("code", expNo).findFirst();
                if (express != null) {
                    express.deleteFromRealm();
                }
            }
        });
    }

    // 归档 快递 更改快递状态
    public void keepExpress() {

    }

    // 获取全部快递单
    public void getAllExpress(final ResultCallback<RealmResults<Express>> callback) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                callback.onSuccess(realm.where(Express.class).findAll());
            }
        });
    }

    // 获取单个快递单
    public void getExpress(final String expNo, final ResultCallback<Express> resultCallback) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                Express express = realm.where(Express.class).equalTo("code", expNo).findFirst();

                if (express == null) resultCallback.onFail(new RealmException("找不到快递单：" + expNo));
                else resultCallback.onSuccess(express);
            }
        });
    }

    // 查询快递属于哪个公司
    public void queryExpressCompany(String expNo, final ResultCallback<String> resultCallback) {
        api.queryCompanyWithExpNo(expNo, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                JLog.e("查询快递公司归属失败：", e.getMessage());
                resultCallback.onFail(e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String resp = response.body().string();
                resultCallback.onSuccess(resp);
            }
        });
    }

    // 根据快递公司编码查询公司信息
    public void queryCompany(final String companyCode, final ResultCallback<Company> resultCallback) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                Company company = realm.where(Company.class).equalTo("code", companyCode).findFirst();

                if (company == null)
                    resultCallback.onFail(new RealmException("找不到公司：" + companyCode));
                else resultCallback.onSuccess(company);
            }
        });

    }

    /**
     * 查询全部快递公司
     */
    public RealmResults<Company> getAllCompany(){
        return realm.where(Company.class).findAll();
    }

    /**
     * 查询热门快递公司
     */
    public RealmResults<Company> getHotCompany(){
        return realm.where(Company.class)
                .equalTo("hot",true)
                .findAll();
    }

    /**
     * 刷新全部快递单
     * <p>
     * 从Realm里面获取全部单号，然后单独发送请求Get最新的状态
     * 仅仅获取 state != 3的单号，因为这些是还没完成的快递单
     */
    public void refresh() {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                RealmResults<Express> results = realm.where(Express.class).notEqualTo("state", 3).findAll();
                networkRefresh(results);
            }
        });
    }


    private void networkRefresh(RealmResults<Express> expresses) {

        for (final Express express :
                expresses) {
            api.query(express.code, express.companyCode, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    JLog.e("刷新快递失败：" + express.code);
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if (response.code() == 200) {
                        final String resp = response.body().string();
                        Realm realm = Realm.getDefaultInstance();
                        realm.executeTransaction(new Realm.Transaction() {
                            @Override
                            public void execute(Realm realm) {
                                realm.createOrUpdateObjectFromJson(Express.class,resp);
                            }
                        });
                    }
                }
            });
        }
    }


    /**
     * 从raw导入公司信息
     */
    public void importCompanyFromRAW(){
        //如果本地没有公司信息，就把raw目录的json文件导入
        if (realm.where(Company.class).findAll().isEmpty()) {
            realm.executeTransaction(loadDomesticExp); //国内数据
            realm.executeTransaction(loadForeignExp); //国外数据
            realm.executeTransaction(loadTransportExp); //转运数据
        }
    }

    /**
     * 导入国内快递
     */
    private Realm.Transaction loadDomesticExp = new Realm.Transaction() {
        @Override
        public void execute(Realm realm) {
            try {
                InputStream inputStream = getRawJsonFile(R.raw.domestic_express);
                realm.createAllFromJson(Company.class, inputStream);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    };

    /**
     * 导入国外快递
     */
    private Realm.Transaction loadForeignExp = new Realm.Transaction() {
        @Override
        public void execute(Realm realm) {
            try {
                InputStream inputStream = getRawJsonFile(R.raw.foreign_express);
                realm.createAllFromJson(Company.class, inputStream);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    };

    /**
     * 导入中转物流
     */
    private Realm.Transaction loadTransportExp = new Realm.Transaction() {
        @Override
        public void execute(Realm realm) {
            try {
                InputStream inputStream = getRawJsonFile(R.raw.transport_express);
                realm.createAllFromJson(Company.class, inputStream);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    };

    /**
     * 获取raw目录下的文件流
     * @param res raw文件夹的文件
     * @return
     * @throws Resources.NotFoundException
     */
    private InputStream getRawJsonFile(@RawRes int res) throws Resources.NotFoundException {
        return context.getResources().openRawResource(res);
    }

    //////////////////////////////////////////////////////////////////////////////
    //////////////////////////////Callback////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////

    /**
     * 回调
     *
     * @param <T>
     */
    public interface ResultCallback<T> {
        void onSuccess(T t);

        void onFail(Exception e);
    }

}
