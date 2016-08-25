package com.patrick.caracal;

import android.app.Application;

import com.jiongbull.jlog.JLog;
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
