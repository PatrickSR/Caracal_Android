package com.patrick.caracal.presenter;

import com.jiongbull.jlog.JLog;
import com.patrick.caracal.Caracal;
import com.patrick.caracal.contract.HomeContract;
import com.patrick.caracal.model.Express;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;

/**
 * Created by 15920 on 2016/7/31.
 */
public class ActivePresenter implements HomeContract.ActiveContract.Presenter {

    private HomeContract.ActiveContract.View view;

    private RealmResults<Express> allExpress;

    private Realm realm;

    public ActivePresenter(HomeContract.ActiveContract.View view){
        view.setPresenter(this);
        this.view = view;
    }

    @Override
    public void refreshAllExpress() {
        view.startRefresh();

        RealmResults<Express> results = realm.where(Express.class).notEqualTo("state", 3).findAll();

        //刷新全部快递
        Caracal.getInstance().refresh(results,new Caracal.ResultCallback() {
            @Override
            public void onSuccess(Object o) {
                view.closeRefresh();
            }

            @Override
            public void onFail(Exception e) {
                view.closeRefresh();
                JLog.e(e);
            }
        });
    }


    @Override
    public void start() {
//        this.view.startRefresh();
        realm = Realm.getDefaultInstance();
        Caracal.getInstance().getAllActiveExpress(realm,new Caracal.ResultCallback<RealmResults<Express>>() {
            @Override
            public void onSuccess(RealmResults<Express> results) {
                ActivePresenter.this.view.closeRefresh();

                ActivePresenter.this.allExpress = results;

                //添加数据监听
                ActivePresenter.this.allExpress.addChangeListener(listener);
                view.showAllExpresss(ActivePresenter.this.allExpress);
            }

            @Override
            public void onFail(Exception e) {
                JLog.e(e);
                ActivePresenter.this.view.closeRefresh();
            }
        });

    }

    @Override
    public void stop() {
        ActivePresenter.this.allExpress.removeChangeListener(listener);
        realm.close();
    }

    final RealmChangeListener<RealmResults<Express>> listener = new RealmChangeListener<RealmResults<Express>>() {
        @Override
        public void onChange(RealmResults<Express> element) {
            //如果数据发生变化
            //关闭刷新的view

            if (view != null){
                view.closeRefresh();
            }
        }
    };
}
