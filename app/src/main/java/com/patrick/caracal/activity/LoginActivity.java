package com.patrick.caracal.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.patrick.caracal.R;

/**
 * Created by Patrick on 16/8/10.
 *
 * 登录界面,只是一个demo,之后会改为fragment
 */
public class LoginActivity extends BaseActivity{

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    int layout() {
        return R.layout.fragment_login;
    }
}
