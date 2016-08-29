package com.patrick.caracal;

import android.app.Application;

import com.facebook.stetho.Stetho;
import com.jiongbull.jlog.JLog;
import com.uphyca.stetho_realm.RealmInspectorModulesProvider;
import com.wilddog.client.Wilddog;

/**
 * Created by patrick on 16-6-14.
 */

public class CaracalApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        initJLog();
        initWilddog();

        Stetho.initialize(
                Stetho.newInitializerBuilder(this)
                        .enableDumpapp(Stetho.defaultDumperPluginsProvider(this))
                        .enableWebKitInspector(RealmInspectorModulesProvider.builder(this).build())
                        .build());
//        initRealmDB();
        Caracal.init(this);
        Caracal.getInstance().importCompanyFromRAW();
    }

    /**
     * 初始化Wilddog
     */
    private void initWilddog() {
        Wilddog.setAndroidContext(this);
    }

    /**
     * 初始化JLog
     */
    private void initJLog() {
        JLog.init(this)
                .setDebug(true)
                .writeToFile(false);
    }

}
