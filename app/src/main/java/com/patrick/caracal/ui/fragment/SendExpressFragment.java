package com.patrick.caracal.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.patrick.caracal.R;
import com.patrick.caracal.event.StartFragmentEvent;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.yokeyword.fragmentation.SupportFragment;

/**
 * Created by 15920 on 2016/7/31.
 *
 * 发送快递
 */
public class SendExpressFragment extends SupportFragment {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @OnClick(R.id.online)void onLine(){
        //跳转到在线寄快递界面
        EventBus.getDefault().post(new StartFragmentEvent(OnlineOrderExpressFragment.newInstance()));
    }

    public static SendExpressFragment newInstance() {

        Bundle args = new Bundle();

        SendExpressFragment fragment = new SendExpressFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_send_express, container, false);
        initView(view);
        ButterKnife.bind(this,view);

        return view;
    }

    private void initView(View view) {
        mToolbar = (Toolbar) view.findViewById(R.id.toolbar);
//        EventBus.getDefault().register(this);

        mToolbar.setTitle("寄快递");
//        initToolbarMenu(mToolbar);
    }
}
