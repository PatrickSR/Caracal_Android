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
import android.widget.Toast;

import com.jiongbull.jlog.JLog;
import com.patrick.caracal.Caracal;
import com.patrick.caracal.R;
import com.patrick.caracal.activity.LoginActivity;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;

import org.greenrobot.eventbus.EventBus;

import java.util.Map;

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
        Caracal.getInstance().loginQQ(getActivity(), new Caracal.LoginCallback() {

            @Override
            public void loginSuccess(String name, String avatarUrl, Map<String, String> info) {

            }

            @Override
            public void loginFail() {

            }

            @Override
            public void loginError() {

            }

            @Override
            public void loginCancel() {

            }
        });

    }

    public static OwnFragment newInstance() {

        Bundle args = new Bundle();

        OwnFragment fragment = new OwnFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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



    private UMAuthListener umAuthListener = new UMAuthListener() {
        @Override
        public void onComplete(SHARE_MEDIA platform, int action, Map<String, String> data) {
            Toast.makeText(getContext(), "Authorize succeed", Toast.LENGTH_SHORT).show();
//            umShareAPI.getPlatformInfo(getActivity(), SHARE_MEDIA.QQ, new UMAuthListener() {
//                @Override
//                public void onComplete(SHARE_MEDIA share_media, int i, Map<String, String> map) {
//                    JLog.d("QQ Login get info："+map.toString());
//                }
//
//                @Override
//                public void onError(SHARE_MEDIA share_media, int i, Throwable throwable) {
//
//                }
//
//                @Override
//                public void onCancel(SHARE_MEDIA share_media, int i) {
//
//                }
//            });
        }

        @Override
        public void onError(SHARE_MEDIA platform, int action, Throwable t) {
            Toast.makeText(getContext(), "Authorize fail", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCancel(SHARE_MEDIA platform, int action) {
            Toast.makeText(getContext(), "Authorize cancel", Toast.LENGTH_SHORT).show();
        }
    };
}
