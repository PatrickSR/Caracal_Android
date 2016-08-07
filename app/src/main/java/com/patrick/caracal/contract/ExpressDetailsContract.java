package com.patrick.caracal.contract;

import com.patrick.caracal.BasePresenter;
import com.patrick.caracal.BaseView;
import com.patrick.caracal.model.Trace;

import io.realm.RealmList;

/**
 * Created by Patrick on 16/8/3.
 *
 * 详情界面 contract
 */
public interface ExpressDetailsContract {

    interface View extends BaseView<Presenter>{

        //显示删除提示dialog
        void showDelDialog();

        //隐藏删除提示dialog
//        void hideDelDialog();

        void setupDetails(RealmList<Trace> traces,String code,String companyName,String remark);

    }

    interface Presenter extends BasePresenter{

        //尝试删除，然后弹出dialog让用户确认
        void attemptDelete();

        void deleteIt();

    }

}
