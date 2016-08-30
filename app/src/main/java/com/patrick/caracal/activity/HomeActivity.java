package com.patrick.caracal.activity;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.MenuItem;

import com.patrick.caracal.R;
import com.patrick.caracal.event.StartFragmentEvent;
import com.patrick.caracal.ui.fragment.BaseLazyMainFragment;
import com.patrick.caracal.ui.fragment.HomeFragment;
import com.patrick.caracal.ui.fragment.SendExpressFragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.yokeyword.fragmentation.SupportActivity;
import me.yokeyword.fragmentation.SupportFragment;

public class HomeActivity extends SupportActivity
        implements NavigationView.OnNavigationItemSelectedListener,BaseLazyMainFragment.OnFragmentOpenDrawerListener {

//    @BindView(R.id.toolbar)
//    Toolbar toolbar;

//    @BindView(R.id.fab)
//    FloatingActionButton fab;

    @BindView(R.id.drawer_layout)
    DrawerLayout drawer;

    @BindView(R.id.nav_view)
    NavigationView navigationView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        ButterKnife.bind(this);

        initView();

        if (savedInstanceState == null) {
            loadRootFragment(R.id.fl_container, HomeFragment.newInstance());
        }
    }

    private void initView(){
//        setSupportActionBar(toolbar);
//
//        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
//                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
//        drawer.setDrawerListener(toggle);
//        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);
    }
    @Override
    public void onBackPressedSupport() {
//        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            finish();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        final SupportFragment topFragment = getTopFragment();
        switch (id){
            case R.id.nav_express:
            //打开快递界面
                HomeFragment homeFragment = findFragment(HomeFragment.class);
                Bundle newBundle = new Bundle();
                newBundle.putString("from", "主页-->来自:" + topFragment.getClass().getSimpleName());
                homeFragment.putNewBundle(newBundle);

                start(homeFragment, SupportFragment.SINGLETASK);
                break;

            case R.id.nav_send_express:
                SendExpressFragment sendExpressFragment = findFragment(SendExpressFragment.class);
                if (sendExpressFragment == null) {
                    popTo(HomeFragment.class, false, new Runnable() {
                        @Override
                        public void run() {
                            start(SendExpressFragment.newInstance());
                        }
                    });
                } else {
                    popTo(SendExpressFragment.class, false);
                }
                break;

            case R.id.nav_search:
                break;

            case R.id.nav_receiver:
                break;

            case R.id.nav_sender:
                break;

            case R.id.nav_share:
                break;

            case R.id.nav_about:
                break;
        }

//        if (id == R.id.nav_camera) {
//            // Handle the camera action
//        } else if (id == R.id.nav_gallery) {
//
//        } else if (id == R.id.nav_slideshow) {
//
//        } else if (id == R.id.nav_manage) {
//
//        } else if (id == R.id.nav_share) {
//
//        } else if (id == R.id.nav_send) {
//
//        }

//        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onOpenDrawer() {
        if (!drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.openDrawer(GravityCompat.START);
        }
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
