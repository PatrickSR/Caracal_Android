package com.patrick.caracal.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.patrick.caracal.R;
import com.patrick.caracal.activity.QueryExpressActivity;
import com.patrick.caracal.adapter.HomePagerFragmentAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by 15920 on 2016/8/8.
 *
 * 显示活跃的快递单
 */
public class HomeFragment extends BaseLazyMainFragment {

    @BindView(R.id.tab)
    TabLayout mTab;

    @BindView(R.id.viewPager)
    ViewPager mViewPager;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.multiple_actions)
    FloatingActionsMenu multiple_actions;

    @OnClick(R.id.add_express)
    void manualAddExpressNumber() {
        Intent intent = new Intent(getActivity(), QueryExpressActivity.class);
        startActivityForResult(intent, 100);

        multiple_actions.collapse();
    }

    public static HomeFragment newInstance() {

        Bundle args = new Bundle();

        HomeFragment fragment = new HomeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home,container,false);
        ButterKnife.bind(this,view);
        mTab.addTab(mTab.newTab().setText("活跃"));
        mTab.addTab(mTab.newTab().setText("归档"));
        return view;
    }

    @Override
    protected void initLazyView(@Nullable Bundle savedInstanceState) {
        mViewPager.setAdapter(new HomePagerFragmentAdapter(getChildFragmentManager()));
        mTab.setupWithViewPager(mViewPager);
    }
}
