package com.patrick.caracal.presenter;

import com.jiongbull.jlog.JLog;
import com.patrick.caracal.Caracal;
import com.patrick.caracal.contract.HomeContract;
import com.patrick.caracal.model.Express;

import io.realm.RealmResults;

/**
 * Created by 15920 on 2016/7/31.
 */
public class HomePresenter implements HomeContract.Presenter {

    private HomeContract.View view;

    private RealmResults<Express> allExpress;

    public HomePresenter(HomeContract.View view){
        view.setPresenter(this);
        this.view = view;
    }

    @Override
    public void refreshAllExpress() {

    }


    @Override
    public void start() {
        this.view.startRefresh();

        Caracal.getInstance().getAllExpress(new Caracal.ResultCallback<RealmResults<Express>>() {
            @Override
            public void onSuccess(RealmResults<Express> results) {
                HomePresenter.this.view.closeRefresh();

                HomePresenter.this.allExpress = results;
                view.showAllExpresss(HomePresenter.this.allExpress);
            }

            @Override
            public void onFail(Exception e) {
                JLog.e(e);
                HomePresenter.this.view.closeRefresh();
            }
        });
    }
}
