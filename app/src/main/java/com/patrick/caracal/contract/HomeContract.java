package com.patrick.caracal.contract;

import com.patrick.caracal.BasePresenter;
import com.patrick.caracal.BaseView;
import com.patrick.caracal.model.Express;

import io.realm.RealmResults;

/**
 * Created by 15920 on 2016/7/31.
 */
public interface HomeContract {

    interface ActiveContract{
        interface View extends BaseView<Presenter>{

            /**
             * 开始刷新
             */
            void startRefresh();
            /**
             * 关闭刷新
             */
            void closeRefresh();

            /**
             * 显示快递单
             * @param results
             */
            void showAllExpresss(RealmResults<Express> results);
        }

        interface Presenter extends BasePresenter{

            /**
             * 更新快递单状态
             */
            void refreshAllExpress();


        }
    }

    interface FileContract{
        interface View extends BaseView<Presenter>{

        }
        interface Presenter extends BasePresenter{

        }
    }
}
