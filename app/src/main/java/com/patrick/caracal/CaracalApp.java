package com.patrick.caracal;

import android.app.Application;

import com.jiongbull.jlog.JLog;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by patrick on 16-6-14.
 */

public class CaracalApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        initJLog();

//        initRealmDB();
        Caracal.init(this);
        Caracal.getInstance().importCompanyFromRAW();
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
