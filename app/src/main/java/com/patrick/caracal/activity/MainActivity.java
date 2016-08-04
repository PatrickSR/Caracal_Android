package com.patrick.caracal.activity;

import android.os.Bundle;

import com.patrick.caracal.R;
import com.patrick.caracal.event.StartFragmentEvent;
import com.patrick.caracal.presenter.HomePresenter;
import com.patrick.caracal.ui.fragment.MainFragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import me.yokeyword.fragmentation.SupportActivity;
import me.yokeyword.fragmentation.anim.DefaultHorizontalAnimator;
import me.yokeyword.fragmentation.anim.FragmentAnimator;

public class MainActivity extends SupportActivity {

    private HomePresenter homePresenter;

    private MainFragment mainFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        if (savedInstanceState == null) {
            mainFragment = MainFragment.newInstance();
            loadRootFragment(R.id.container, mainFragment);

        }
    }

    private void initPresenter() {
        if (mainFragment != null)
            homePresenter = new HomePresenter(mainFragment.getHomeFragment());
    }

    @Override
    public void onBackPressedSupport() {
        super.onBackPressedSupport();
    }

    @Override
    public FragmentAnimator onCreateFragmentAnimator() {
        return new DefaultHorizontalAnimator();
    }

    @Override
    protected void onStart() {
        super.onStart();

        initPresenter();
    }

    /**
     * 启动其他fragment
     */
    @Subscribe
    public void startBrother(StartFragmentEvent event) {
        start(event.targetFragment);
    }

    @Override
    protected void onResume() {
        EventBus.getDefault().register(this);
        super.onResume();
    }

    @Override
    protected void onPause() {
        EventBus.getDefault().unregister(this);
        super.onPause();
    }
}
