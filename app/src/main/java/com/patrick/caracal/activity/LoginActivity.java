package com.patrick.caracal.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.patrick.caracal.Caracal;
import com.patrick.caracal.R;
import com.patrick.caracal.event.AuthEvent;
import com.patrick.caracal.model.User;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.BindView;
import butterknife.OnClick;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;

/**
 * Created by Patrick on 16/8/10.
 * <p>
 * 登录界面,只是一个demo,之后会改为fragment
 */
public class LoginActivity extends BaseActivity {

    private RealmResults<User> userResult;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.et_email)
    TextInputEditText et_email;

    @BindView(R.id.et_password)
    TextInputEditText et_password;

    @OnClick(R.id.btn_login_with_email) void loginWithEmail(){
        //通过Email来登录

        showLoadding();

        String email = et_email.getText().toString();
        String password = et_password.getText().toString();
        attemptLogin(email,password);

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginActivity.this.onBackPressed();
            }
        });

        EventBus.getDefault().register(this);
        userResult = realm.where(User.class).findAll();
        userResult.addChangeListener(new RealmChangeListener<RealmResults<User>>() {
            @Override
            public void onChange(RealmResults<User> element) {
                //当用户注册/登录完成后，会触发onChange
                if (!element.isEmpty()){
                    LoginActivity.this.finish();
                }
            }
        });
    }

    @Override
    int layout() {
        return R.layout.fragment_login;
    }

    /**
     * 尝试登录
     * @param email
     * @param password
     */
    private void attemptLogin(String email,String password){
        Caracal.getInstance().authWithEmail(email,password);
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    @Subscribe
    public void onAuthEvent(AuthEvent event){
        switch (event.state){
            case REG_FAIL:
                showToast("注册失败？！");
                break;
        }
    }


}
