package com.patrick.caracal.contract;

import com.patrick.caracal.BasePresenter;
import com.patrick.caracal.BaseView;

/**
 * Created by Patrick on 16/8/29.
 */
public interface OwnContract {

    interface View extends BaseView<Presenter>{

        /**
         * 更新用户信息
         */
        void showUserInfo(String name,String avatar);

        /**
         * 显示登录入口
         */
        void showLoginEnter();
    }

    interface Presenter extends BasePresenter{

        void checkAuthState();
    }
}
