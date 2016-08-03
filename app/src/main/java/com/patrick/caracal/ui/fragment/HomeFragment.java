package com.patrick.caracal.ui.fragment;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.patrick.caracal.R;
import com.patrick.caracal.activity.QueryExpressActivity;
import com.patrick.caracal.adapter.ExpressAdapter;

import com.patrick.caracal.adapter.OnItemClickListener;
import com.patrick.caracal.contract.HomeContract;
import com.patrick.caracal.event.TabSelectedEvent;
import com.patrick.caracal.model.Express;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.RealmResults;


/**
 * Created by 15920 on 2016/7/31.
 *
 * 首页，显示快递列表
 */
public class HomeFragment extends BaseLazyMainFragment implements SwipeRefreshLayout.OnRefreshListener,HomeContract.View {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.recycler)
    RecyclerView recyclerview;

    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout mRefreshLayout;

    @BindView(R.id.multiple_actions)
    FloatingActionsMenu multiple_actions;

    @OnClick(R.id.add_express)
    void manualAddExpressNumber() {
        Intent intent = new Intent(getActivity(), QueryExpressActivity.class);
        startActivityForResult(intent, 100);

        multiple_actions.collapse();
    }

    private HomeContract.Presenter presenter;

    private boolean mInAtTop = true;
    private int mScrollTotal;

    private ExpressAdapter adapter;

    public static HomeFragment newInstance() {

        Bundle args = new Bundle();

        HomeFragment fragment = new HomeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        ButterKnife.bind(this,view);
        mToolbar.setTitle("首页");
        EventBus.getDefault().register(this);
        return view;
    }

    @Override
    protected void initLazyView(@Nullable Bundle savedInstanceState) {
        mRefreshLayout.setOnRefreshListener(this);

        recyclerview.setLayoutManager(new LinearLayoutManager(_mActivity));
        recyclerview.setHasFixedSize(true);
        final int space = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 0.5f, getResources().getDisplayMetrics());
        recyclerview.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                outRect.set(0, 0, 0, space);
            }
        });
//
        recyclerview.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                mScrollTotal += dy;
                if (mScrollTotal <= 0) {
                    mInAtTop = true;
                } else {
                    mInAtTop = false;
                }
            }
        });

//        adapter.setOnItemClickListener(new OnItemClickListener() {
//            @Override
//            public void onItemClick(int position, View view, RecyclerView.ViewHolder vh) {
                // 因为启动的MsgFragment是MainFragment的兄弟Fragment,所以需要MainFragment.start()

                // 这里我使用EventBus通知父MainFragment处理跳转(接耦),
//                EventBus.getDefault().post(new StartFragmentEvent(MsgFragment.newInstance(mAdapter.getMsg(position))));
//                EventBus.getDefault().post(new StartFragmentEvent(MsgFragment.newInstance(mAdapter.getMsg(position))));

                // 也可以像使用getParentFragment()的方式,拿到父Fragment的引用来操作 (不建议)
//              ((MainFragment) getParentFragment()).startMsgBrother(MsgFragment.newInstance());
//            }
//        });
    }

    /**
     * 下拉刷新
     */
    @Override
    public void onRefresh() {
        presenter.refreshAllExpress();
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.start();
    }

    @Override
    protected void onFragmentResult(int requestCode, int resultCode, Bundle data) {

    }

    /**
     * Reselected Tab
     */
    @Subscribe
    public void onTabSelectedEvent(TabSelectedEvent event) {
        if (event.position != MainFragment.FIRST) return;

        if (mInAtTop) {
            mRefreshLayout.setRefreshing(true);
            onRefresh();
        } else {
            scrollToTop();
        }
    }

    private void scrollToTop() {
        recyclerview.smoothScrollToPosition(0);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        recyclerview.setAdapter(null);
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void setPresenter(HomeContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void startRefresh() {
        if (mRefreshLayout != null) {
            mRefreshLayout.setRefreshing(true);
        }
    }

    @Override
    public void closeRefresh() {
        if (mRefreshLayout != null) {
            mRefreshLayout.setRefreshing(false);
        }
    }

    /**
     * 显示全部快递列表
     * @param results
     */
    @Override
    public void showAllExpresss(RealmResults<Express> results) {
        adapter = new ExpressAdapter(getContext(),results,true);
        recyclerview.setAdapter(this.adapter);

        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int position, View view, RecyclerView.ViewHolder viewHolder) {

            }
        });
    }
}