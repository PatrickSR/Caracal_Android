package com.patrick.caracal.contract;

import com.patrick.caracal.BasePresenter;
import com.patrick.caracal.BaseView;
import com.patrick.caracal.model.Express;

import io.realm.RealmResults;

/**
 * Created by 15920 on 2016/7/31.
 */
public interface HomeContract {

    interface View extends BaseView<Presenter>{

    }

    interface Presenter extends BasePresenter{

        /**
         * 更新快递单状态
         */
        void refresh();

        /**
         * 获取全部快递单
         * @return
         */
        RealmResults<Express> getAllExpress();
    }
}
