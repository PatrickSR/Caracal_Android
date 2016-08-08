package com.patrick.caracal.presenter;

import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.patrick.caracal.Caracal;
import com.patrick.caracal.R;
import com.patrick.caracal.contract.ExpressDetailsContract;
import com.patrick.caracal.model.Express;

import io.realm.Realm;

/**
 * Created by 15920 on 2016/8/4.
 */
public class ExpressDetailsPresenter implements ExpressDetailsContract.Presenter {

    private ExpressDetailsContract.View view;

    private String expCode;

    private Realm realm;

    private Express express;

    public ExpressDetailsPresenter(ExpressDetailsContract.View view,String expCode) {
        view.setPresenter(this);
        this.view = view;

        this.expCode =expCode;
    }

    @Override
    public void attemptDelete() {
        view.showDelDialog();
    }

    @Override
    public void deleteIt() {
        Caracal.getInstance().delExpress(expCode);
    }

    @Override
    public void start() {
        realm = Realm.getDefaultInstance();
        //找到对应的快递单
        Caracal.getInstance().getExpress(realm,expCode, new Caracal.ResultCallback<Express>() {
            @Override
            public void onSuccess(Express express) {

                ExpressDetailsPresenter.this.express = express;
                //加载adapter
                view.setupDetails(
                        express.traces,
                        express.code,
                        express.companyName,
                        express.remark);

                setupMenu();
            }

            @Override
            public void onFail(Exception e) {

            }
        });
    }

    @Override
    public void stop() {
        realm.close();
    }

    /**
     * 设置菜单栏
     */
    private void setupMenu(){
        if (express.isActive){
            //活跃的快递单
            view.setupMenu(R.menu.menu_details_active,menuItemClickListener );

        }else {
            //已归档的快递单
            view.setupMenu(R.menu.menu_details_file,menuItemClickListener);
        }
    }

    /**
     * 归档当前的快递单
     */
    private void fileTheExpress(){
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                express.isActive = false;

                view.goBack();
            }
        });
    }

    /**
     * 把归档的快递单移到活跃中
     */
    private void activeTheExpress(){
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                express.isActive = true;

                view.goBack();
            }
        });
    }

    private final Toolbar.OnMenuItemClickListener menuItemClickListener = new Toolbar.OnMenuItemClickListener() {
        @Override
        public boolean onMenuItemClick(MenuItem item) {
            switch (item.getItemId()){
                case R.id.action_del:
                    attemptDelete();
                    break;
                case R.id.action_file:
                    fileTheExpress();
                    break;
                case R.id.action_active:
                    activeTheExpress();
                    break;
            }
            return true;
        }
    };
}
