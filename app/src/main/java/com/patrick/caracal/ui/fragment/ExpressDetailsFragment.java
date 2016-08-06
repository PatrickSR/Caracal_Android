package com.patrick.caracal.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.patrick.caracal.R;
import com.patrick.caracal.adapter.TimeLineAdapter;
import com.patrick.caracal.contract.ExpressDetailsContract;
import com.patrick.caracal.model.Trace;
import com.patrick.caracal.presenter.ExpressDetailsPresenter;
import com.rey.material.app.Dialog;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.RealmList;

/**
 * Created by 15920 on 2016/7/31.
 *
 */
public class ExpressDetailsFragment extends BaseBackFragment implements ExpressDetailsContract.View{

    private ExpressDetailsContract.Presenter presenter;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.timeline_recyclerview)
    RecyclerView view_timeline;

    public static ExpressDetailsFragment newInstance(Bundle bundle) {

        ExpressDetailsFragment fragment = new ExpressDetailsFragment();
        fragment.setArguments(bundle);
        return fragment;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String expCode = getArguments().getString("expCode");
        new ExpressDetailsPresenter(this,expCode);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_express_details, container, false);
        ButterKnife.bind(this,view);
        view_timeline.setLayoutManager(new LinearLayoutManager(getContext()));
        view_timeline.setHasFixedSize(true);

        initToolbar();

        return attachToSwipeBack(view);
    }

    /**
     * 初始化toolbar
     */
    private void initToolbar(){
        initToolbarNav(toolbar);
        toolbar.setTitle("查看详情");
        initMenu();
    }

    /**
     * 初始化右上角menu
     */
    private void initMenu() {
        toolbar.inflateMenu(R.menu.menu_details_fragment);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.action_del:
                        presenter.attemptDelete();
                        break;
                }
                return true;
            }
        });
    }

    @Override
    public void showDelDialog() {
        final Dialog delDialog = new Dialog(getContext());
        delDialog.title("Dialog title")
                .positiveAction("OK")
                .negativeAction("CANCEL")
                .positiveActionTextColor(getResources().getColor(android.R.color.black))
                .negativeActionTextColor(getResources().getColor(android.R.color.black))
                .positiveActionClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        presenter.deleteIt();
                        delDialog.dismiss();
                        _mActivity.onBackPressed();
                    }
                })
                .negativeActionClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        delDialog.dismiss();
                    }
                })
                .cancelable(true)
                .show();
    }

//    @Override
//    public void hideDelDialog() {
//
//    }

    @Override
    public void setupDetails(RealmList<Trace> traces) {
        TimeLineAdapter adapter = new TimeLineAdapter(getContext(),traces,genHeaderView());
        view_timeline.setAdapter(adapter);
    }

    private View genHeaderView(){
        return LayoutInflater.from(getContext()).inflate(R.layout.item_details_header,view_timeline,false);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (this.presenter != null) this.presenter.start();
    }

    @Override
    public void setPresenter(ExpressDetailsContract.Presenter presenter) {
        this.presenter = presenter;
    }
}
