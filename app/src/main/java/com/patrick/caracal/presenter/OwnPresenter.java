package com.patrick.caracal.presenter;

import android.text.TextUtils;

import com.patrick.caracal.contract.OwnContract;
import com.patrick.caracal.model.User;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by Patrick on 16/8/29.
 */
public class OwnPresenter implements OwnContract.Presenter{

    private OwnContract.View view;

    private Realm realm;

    private RealmResults<User> userResult;

    public OwnPresenter(OwnContract.View view){
        view.setPresenter(this);
        this.view = view;

        realm = Realm.getDefaultInstance();
        userResult = realm.where(User.class).findAll();
    }

    @Override
    public void start() {
        checkAuthState();
    }

    @Override
    public void stop() {
        if (!realm.isClosed()){
            realm.close();
        }
    }

    @Override
    public void checkAuthState() {
        //检测登录状态
        if (userResult.isEmpty()){
            //如果没登录，显示登录入口
            view.showLoginEnter();
        }else{
            //如果登录了，直接显示用户信息
            User user = userResult.first();

            String name = user.name;
            if (TextUtils.isEmpty(name)) name = user.email;
            if (TextUtils.isEmpty(name)) name = "Caracal 用户";

            view.showUserInfo(name,user.avatar);
        }
    }
}
