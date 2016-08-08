package com.patrick.caracal.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.patrick.caracal.R;

import me.yokeyword.fragmentation_swipeback.SwipeBackFragment;

/**
 * Created by Patrick on 16/8/8.
 */
public class OnlineOrderExpressFragment extends SwipeBackFragment {

    public static OnlineOrderExpressFragment newInstance(){
        Bundle args = new Bundle();
        OnlineOrderExpressFragment fragment = new OnlineOrderExpressFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_online_send_express,container,false);

        return view;
    }
}
