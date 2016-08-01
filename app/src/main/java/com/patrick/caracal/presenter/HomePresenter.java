package com.patrick.caracal.presenter;

import com.jiongbull.jlog.JLog;
import com.patrick.caracal.Caracal;
import com.patrick.caracal.contract.HomeContract;
import com.patrick.caracal.model.Express;

import io.realm.RealmChangeListener;
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
        //刷新全部快递
        Caracal.getInstance().refresh();
    }


    @Override
    public void start() {
        this.view.startRefresh();

        Caracal.getInstance().getAllExpress(new Caracal.ResultCallback<RealmResults<Express>>() {
            @Override
            public void onSuccess(RealmResults<Express> results) {
                HomePresenter.this.view.closeRefresh();

                HomePresenter.this.allExpress = results;

                //添加数据监听
                HomePresenter.this.allExpress.addChangeListener(listener);
                view.showAllExpresss(HomePresenter.this.allExpress);
            }

            @Override
            public void onFail(Exception e) {
                JLog.e(e);
                HomePresenter.this.view.closeRefresh();
            }
        });
    }

    RealmChangeListener<RealmResults<Express>> listener = new RealmChangeListener<RealmResults<Express>>() {
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
