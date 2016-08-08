package com.patrick.caracal.presenter;

import com.jiongbull.jlog.JLog;
import com.patrick.caracal.Caracal;
import com.patrick.caracal.contract.HomeContract;
import com.patrick.caracal.model.Express;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by Patrick on 16/8/8.
 */
public class FilePresenter implements HomeContract.FileContract.Presenter {

    private HomeContract.FileContract.View view;

    private Realm realm;

    public FilePresenter(HomeContract.FileContract.View view){
        view.setPresenter(this);
        this.view = view;
    }

    @Override
    public void start() {
        realm = Realm.getDefaultInstance();
        Caracal.getInstance().getAllFileExpress(realm, new Caracal.ResultCallback<RealmResults<Express>>() {
            @Override
            public void onSuccess(RealmResults<Express> expresses) {
                view.showFileExpress(expresses);
            }

            @Override
            public void onFail(Exception e) {
                JLog.e(e);
            }
        });
    }

    @Override
    public void stop() {
        if (!realm.isClosed()){
            realm.close();
        }
    }
}
