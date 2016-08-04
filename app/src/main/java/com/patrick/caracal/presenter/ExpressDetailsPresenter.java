package com.patrick.caracal.presenter;

import com.patrick.caracal.Caracal;
import com.patrick.caracal.contract.ExpressDetailsContract;
import com.patrick.caracal.model.Express;

/**
 * Created by 15920 on 2016/8/4.
 */
public class ExpressDetailsPresenter implements ExpressDetailsContract.Presenter {

    private ExpressDetailsContract.View view;

    private String expCode;

    public ExpressDetailsPresenter(ExpressDetailsContract.View view,String expCode) {
        view.setPresenter(this);
        this.view = view;

        this.expCode =expCode;
    }

    @Override
    public void attemptDelete() {

    }

    @Override
    public void deleteIt() {

    }

    @Override
    public void start() {
        //找到对应的快递单
        Caracal.getInstance().getExpress(expCode, new Caracal.ResultCallback<Express>() {
            @Override
            public void onSuccess(Express express) {
                //加载adapter
                view.setupDetails(express.traces);
            }

            @Override
            public void onFail(Exception e) {

            }
        });
    }
}
