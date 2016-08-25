package com.patrick.caracal;

import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.RawRes;
import android.text.TextUtils;

import com.jiongbull.jlog.JLog;
import com.patrick.caracal.event.WRegisterEvent;
import com.patrick.caracal.model.Company;
import com.patrick.caracal.model.Express;
import com.patrick.caracal.model.TraceList;
import com.patrick.caracal.net.Api;
import com.patrick.caracal.net.WilddogApi;
import com.wilddog.client.AuthData;
import com.wilddog.client.Wilddog;
import com.wilddog.client.WilddogError;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

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

    //Wilddog 应用地址
    public static final String WILDDOG_URL = "https://caracal.wilddogio.com";

    //QQ App ID 用于登录
    private static final String QQ_APPID = "1105546197";

    private static Caracal caracal;

    private static Context applicationContext;

    private Context context;

    private Api api;

    private Caracal(Context context) {
        api = new Api();
        this.context = context;
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

    /**
     * 使用QQ登录
     */
    public void authWithQQ(){

    }

    /**
     * 使用邮箱密码登录
     */
    public void authWithEmail(String email,String password){
        Wilddog loginRef = new Wilddog(WILDDOG_URL);
        loginRef.authWithPassword(email, password, new Wilddog.AuthResultHandler() {
            @Override
            public void onAuthenticated(AuthData authData) {
                JLog.d("onAuthenticated: " + authData.toString());

                //保存到RealmDB

                Realm realm = Realm.getDefaultInstance();

            }

            @Override
            public void onAuthenticationError(WilddogError wilddogError) {
                JLog.e("onAuthenticationError");
                JLog.e(wilddogError.toException());

            }
        });
    }

    /**
     * 注册一个邮箱用户
     * @param email 邮箱是唯一的
     * @param password 密码
     */
    public void regWithEmail(final String email, final String password){
        Wilddog regRef = new Wilddog(WILDDOG_URL);
        regRef.createUser(email, password, new Wilddog.ResultHandler() {
            @Override
            public void onSuccess() {
                //注册成功
                EventBus.getDefault().post(new WRegisterEvent(WRegisterEvent.STATE.SUCCESS));
            }

            @Override
            public void onError(WilddogError wilddogError) {
                EventBus.getDefault().post(new WRegisterEvent(WRegisterEvent.STATE.FAIL,wilddogError));
            }
        });
    }

    /**
     * 添加/订阅 快递
     */
    public void subExpress(final String expNo,
                           final String companyCode,
                           final String companyName,
                           final String remark,
                           final ResultCallback<String> resultCallback) {
        api.subscribe(expNo, companyCode, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                resultCallback.onFail(e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String resp = response.body().string();
                Realm realm = Realm.getDefaultInstance();

                //订阅成功后，保存到DB上
                realm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {

                        Express express = new Express();
                        express.code =expNo;
                        express.companyCode = companyCode;
                        express.companyName = companyName;

                        if (TextUtils.isEmpty(remark)){
                            express.remark = companyName + " " +expNo;
                        }else{
                            express.remark = remark;
                        }
                        realm.copyToRealmOrUpdate(express);

                        realm.close();
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
        Realm realm = Realm.getDefaultInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                Express express = realm.where(Express.class).equalTo("code", expNo).findFirst();
                if (express != null) {
                    express.deleteFromRealm();
                }

                realm.close();
            }
        });
    }

    // 归档 快递 更改快递状态
    public void keepExpress() {

    }

    // 获取全部快递单
    public void getAllExpress(Realm realm,final ResultCallback<RealmResults<Express>> callback) {
        callback.onSuccess(realm.where(Express.class).findAll());
    }

    /**
     * 获取活跃的快递单
     * @param realm
     * @param callback
     */
    public void getAllActiveExpress(Realm realm,final ResultCallback<RealmResults<Express>> callback ){
        callback.onSuccess(realm.where(Express.class).equalTo("isActive",true).findAll());
    }

    /**
     * 获取归档的快递单
     * @param realm
     * @param callback
     */
    public void getAllFileExpress(Realm realm,final ResultCallback<RealmResults<Express>> callback){callback.onSuccess(realm.where(Express.class).equalTo("isActive",true).findAll());
        callback.onSuccess(realm.where(Express.class).equalTo("isActive",false).findAll());
    }

    // 获取单个快递单
    public void getExpress(Realm realm,final String expNo, final ResultCallback<Express> resultCallback) {
        Express express = realm.where(Express.class).equalTo("code", expNo).findFirst();

        if (express == null) resultCallback.onFail(new RealmException("找不到快递单：" + expNo));
        else resultCallback.onSuccess(express);
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
    public void queryCompany(Realm realm,final String companyCode, final ResultCallback<Company> resultCallback) {
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
    public RealmResults<Company> getAllCompany(Realm realm){
        return realm.where(Company.class).findAll();
    }

    /**
     * 查询热门快递公司
     */
    public RealmResults<Company> getHotCompany(Realm realm){
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
    public void refresh(RealmResults<Express> results,ResultCallback callback) {
        networkRefresh(results,callback);
    }

    /**
     * 进行网络刷新
     * @param expresses
     */
    private void networkRefresh(RealmResults<Express> expresses,final ResultCallback callback) {

        for (final Express express :
                expresses) {
            api.query(express.code, express.companyCode, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    JLog.e("刷新快递失败");
                    callback.onFail(e);
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if (response.code() == 200) {
                        final String resp = response.body().string();
                        JLog.d("刷新快递 <---- "+resp);
                        callback.onSuccess(null);

                        if (resp.equals("null")|| TextUtils.isEmpty(resp)) return;

                        try {
                            JSONObject jsonResp = new JSONObject(resp);

                            if (jsonResp.has("error")){
                                //有错误
                                JLog.e(jsonResp.getString("error"));
                                return;
                            }

                            Realm realm = Realm.getDefaultInstance();
                            realm.beginTransaction();

                            realm.createOrUpdateObjectFromJson(Express.class,resp);
                            realm.commitTransaction();
                            realm.close();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
        }
    }

    private void updateExpressFromWilddog(String... list) {
        WilddogApi wilddogApi = new WilddogApi();

        for (String no:
                list) {
            wilddogApi.queryExpress(no, new WilddogApi.Callback() {
                @Override
                public void onSuccess(JSONObject json) {
                    updateExpressToRealm(json);
                }

                @Override
                public void onFail(Exception e) {
                    JLog.e("refresh from wilddog error "+e);
                }
            });
        }
    }

    private void updateTraceFromWilddog(String... list){
        WilddogApi wilddogApi = new WilddogApi();
        for (String no :
                list) {
            wilddogApi.queryTrace(no, new WilddogApi.Callback() {
                @Override
                public void onSuccess(JSONObject json) {
                    updateTraceListToRealm(json);
                }

                @Override
                public void onFail(Exception e) {
                    JLog.e("Update Trace From Wilddog "+e);
                }
            });

        }
    }

    private void updateExpressToRealm(final JSONObject jsonObject){
        Realm realm = Realm.getDefaultInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.createOrUpdateObjectFromJson(Express.class,jsonObject);
                realm.close();
            }
        });
    }

    private void updateTraceListToRealm(final JSONObject jsonObject){

        Realm realm = Realm.getDefaultInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.createOrUpdateObjectFromJson(TraceList.class,jsonObject);

                realm.close();
            }
        });
    }

    /**
     * 从raw导入公司信息
     */
    public void importCompanyFromRAW(){

        Realm realm = Realm.getDefaultInstance();
        //如果本地没有公司信息，就把raw目录的json文件导入
//        if (realm.where(Company.class).findAll().isEmpty()) {
//            realm.executeTransaction(loadDomesticExp); //国内数据
//            realm.executeTransaction(loadForeignExp); //国外数据
//            realm.executeTransaction(loadTransportExp); //转运数据
//        }

        realm.beginTransaction();

        try {
            InputStream domesticInputStream = getRawJsonFile(R.raw.domestic_express);
            realm.createOrUpdateAllFromJson(Company.class, domesticInputStream);
            InputStream foreignInputStream = getRawJsonFile(R.raw.foreign_express);
            realm.createOrUpdateAllFromJson(Company.class, foreignInputStream);
            InputStream transportInputStream = getRawJsonFile(R.raw.transport_express);
            realm.createOrUpdateAllFromJson(Company.class, transportInputStream);

        } catch (IOException e) {
            e.printStackTrace();
        }

        realm.commitTransaction();
        realm.close();

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
