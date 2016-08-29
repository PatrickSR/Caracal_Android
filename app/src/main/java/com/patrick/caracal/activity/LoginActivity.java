package com.patrick.caracal.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.jiongbull.jlog.JLog;
import com.patrick.caracal.Caracal;
import com.patrick.caracal.R;
import com.patrick.caracal.event.AuthEvent;
import com.patrick.caracal.model.User;
import com.tencent.connect.common.Constants;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONObject;

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

    private Tencent mTencent;

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

    @OnClick(R.id.btn_login_with_qq) void loginWithQQ(){
        showLoadding();

        String scope = "all";
        mTencent.login(this, scope, qqLoginListener);
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

        mTencent = Tencent.createInstance(Caracal.QQ_APPID, getApplicationContext());
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Constants.REQUEST_LOGIN) {
            mTencent.handleLoginData(data, qqLoginListener);
        }


        super.onActivityResult(requestCode, resultCode, data);

    }

    private IUiListener qqLoginListener = new IUiListener() {
        @Override
        public void onComplete(Object o) {
            JLog.d("QQ Login Success");

            /*
            {
                "ret": 0,
                "openid": "62681FB8EA6DEDFB0E42C3206D5CFB00",
                "access_token": "FA9B72D8C5336CD646D255E4AFB1259B",
                "pay_token": "936F7732ACC64A6564AA0645DC7D821D",
                "expires_in": 7776000,
                "pf": "desktop_m_qq-10000144-android-2002-",
                "pfkey": "91de35481763d481a6f25fccbcec75c6",
                "msg": "",
                "login_cost": 367,
                "query_authority_cost": 175,
                "authority_cost": -4216687
            }
            */
            JSONObject jsonObject = (JSONObject) o;
            Caracal.getInstance().authWithQQ(jsonObject.optString("access_token"),jsonObject.optString("openid"));
        }

        @Override
        public void onError(UiError uiError) {
            JLog.e("QQ Login error "+uiError.errorMessage);
        }

        @Override
        public void onCancel() {
            JLog.w("QQ Login Cancel");
        }
    };



    @Subscribe
    public void onAuthEvent(AuthEvent event){
        switch (event.state){
            case REG_FAIL:
                showToast("注册失败？！");
                break;
            case QQ_LOGIN_FAIL:
                showToast("QQ 登录失败？！");
        }
    }


}
