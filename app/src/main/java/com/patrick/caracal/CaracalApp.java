package com.patrick.caracal;

import android.app.Application;

import com.jiongbull.jlog.JLog;
import com.umeng.socialize.PlatformConfig;

/**
 * Created by patrick on 16-6-14.
 */

public class CaracalApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        initJLog();
        initUMeng();

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

    private void initUMeng(){

        PlatformConfig.setQQZone("1105546197", "KEYd4rl6yDEfzk6BYvN");
    }

}
