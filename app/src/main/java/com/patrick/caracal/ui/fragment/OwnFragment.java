package com.patrick.caracal.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.patrick.caracal.R;
import com.patrick.caracal.activity.LoginActivity;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.yokeyword.fragmentation.SupportFragment;

/**
 * Created by 15920 on 2016/7/31.
 *
 * 个人页面
 */
public class OwnFragment extends SupportFragment{

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.userName)
    TextView tv_username;

    @BindView(R.id.userAvatar)
    ImageView image_userAvatar;

    @OnClick(R.id.login)void login(){
        //进入登陆界面
        startActivity(new Intent(getContext(), LoginActivity.class));
    }

    public static OwnFragment newInstance() {

        Bundle args = new Bundle();

        OwnFragment fragment = new OwnFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_me, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        ButterKnife.bind(this,view);
//        EventBus.getDefault().register(this);
        mToolbar.setTitle("我");
//        initToolbarMenu(mToolbar);
    }
}
