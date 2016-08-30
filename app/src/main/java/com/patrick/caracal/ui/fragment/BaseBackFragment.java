package com.patrick.caracal.ui.fragment;

import android.support.v7.widget.Toolbar;
import android.view.View;

import com.patrick.caracal.R;

import me.yokeyword.fragmentation_swipeback.SwipeBackFragment;

/**
 * Created by 15920 on 2016/8/4.
 */
public class BaseBackFragment extends SwipeBackFragment {
    private static final String TAG = "Fragmentation";

    protected void initToolbarNav(Toolbar toolbar) {
        toolbar.setNavigationIcon(R.drawable.ic_back_button_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pop();
            }
        });

    }

}
