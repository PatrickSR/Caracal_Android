package com.patrick.caracal.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.patrick.caracal.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by 15920 on 2016/7/31.
 *
 */
public class ExpressDetailsFragment extends BaseBackFragment{

    public static ExpressDetailsFragment newInstance() {

        Bundle args = new Bundle();

        ExpressDetailsFragment fragment = new ExpressDetailsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_express_details, container, false);
        ButterKnife.bind(this,view);
        initToolbarNav(toolbar);
        return attachToSwipeBack(view);
    }
}
