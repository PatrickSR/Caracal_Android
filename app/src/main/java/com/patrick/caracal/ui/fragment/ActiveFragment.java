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
import com.patrick.caracal.event.StartFragmentEvent;
import com.patrick.caracal.event.TabSelectedEvent;
import com.patrick.caracal.model.Express;
import com.patrick.caracal.presenter.ActivePresenter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.RealmResults;


/**
 * Created by 15920 on 2016/7/31.
 *
 * 首页，显示快递列表
 */
public class ActiveFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener,HomeContract.ActiveContract.View {

//    @BindView(R.id.toolbar)
//    Toolbar mToolbar;

    @BindView(R.id.recycler)
    RecyclerView recyclerview;

    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout mRefreshLayout;

//    @BindView(R.id.multiple_actions)
//    FloatingActionsMenu multiple_actions;
//
//    @OnClick(R.id.add_express)
//    void manualAddExpressNumber() {
//        Intent intent = new Intent(getActivity(), QueryExpressActivity.class);
//            startActivityForResult(intent, 100);
//
//        multiple_actions.collapse();
//    }

    private HomeContract.ActiveContract.Presenter presenter;

    private boolean mInAtTop = true;
    private int mScrollTotal;

    private ExpressAdapter adapter;

    public static ActiveFragment newInstance() {

        Bundle args = new Bundle();

        ActiveFragment fragment = new ActiveFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new ActivePresenter(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_active, container, false);

        ButterKnife.bind(this,view);
//        mToolbar.setTitle("首页");
        initRecyclerview();
        EventBus.getDefault().register(this);
        return view;
    }

    protected void initRecyclerview() {
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
        if (presenter!=null) {
            presenter.start();
            onRefresh();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (presenter!=null) {
            presenter.stop();
        }
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
//            mRefreshLayout.setRefreshing(true);
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
    public void startRefresh() {
        if (mRefreshLayout != null) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mRefreshLayout.setRefreshing(true);
                }
            });
        }
    }

    @Override
    public void closeRefresh() {
        if (mRefreshLayout != null && getActivity() != null) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mRefreshLayout.setRefreshing(false);
                }
            });
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
                Bundle bundle = new Bundle();

                Express exp = adapter.getData().get(position);
                bundle.putString("expCode",exp.code);

                EventBus.getDefault().post(new StartFragmentEvent(ExpressDetailsFragment.newInstance(bundle)));
            }
        });
    }

    @Override
    public void setPresenter(HomeContract.ActiveContract.Presenter presenter) {
        this.presenter = presenter;
    }
}
