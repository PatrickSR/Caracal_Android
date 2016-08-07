package com.patrick.caracal.presenter;

import android.text.TextUtils;

import com.patrick.caracal.Caracal;
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
                //加载adapter
                if (TextUtils.isEmpty(express.remark)){
                    String name = express.companyName+" "+express.code;
                    view.setupDetails(express.traces,name,express.companyName);
                }else{
                    view.setupDetails(express.traces,express.remark,express.companyName);
                }
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
}
