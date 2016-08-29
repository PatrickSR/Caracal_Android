package com.patrick.caracal.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.patrick.caracal.R;
import com.patrick.caracal.activity.LoginActivity;
import com.patrick.caracal.contract.OwnContract;
import com.patrick.caracal.presenter.OwnPresenter;
import com.patrick.caracal.util.GlideCircleTransform;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.yokeyword.fragmentation.SupportFragment;

/**
 * Created by 15920 on 2016/7/31.
 *
 * 个人页面
 */
public class OwnFragment extends SupportFragment implements OwnContract.View{

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.userName)
    TextView tv_username;

    @BindView(R.id.userAvatar)
    ImageView image_userAvatar;

    @BindView(R.id.userframe)
    RelativeLayout userFrame;

    private OwnContract.Presenter presenter;

    public static OwnFragment newInstance() {

        Bundle args = new Bundle();

        OwnFragment fragment = new OwnFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new OwnPresenter(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_me, container, false);
        initView(view);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.checkAuthState();
    }

    private void initView(View view) {
        ButterKnife.bind(this,view);
        mToolbar.setTitle("我");

    }

    @Override
    public void showUserInfo(String name,String avatar){
        userFrame.setOnClickListener(null);
        tv_username.setText("登录/注册");

        if (!TextUtils.isEmpty(avatar)){
            Glide.with(this)
                    .load(avatar)
                    .transform(new GlideCircleTransform(getContext()))
                    .into(image_userAvatar);
        }

    }

    @Override
    public void showLoginEnter() {
        userFrame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //进入登陆界面
                startActivity(new Intent(getContext(), LoginActivity.class));
            }
        });
    }

    @Override
    public void setPresenter(OwnContract.Presenter presenter) {
        this.presenter = presenter;
    }
}
