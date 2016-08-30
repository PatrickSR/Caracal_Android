package com.patrick.caracal.activity;

import android.os.Bundle;
import android.support.annotation.MenuRes;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.patrick.caracal.R;
import com.patrick.caracal.adapter.TimeLineAdapter;
import com.patrick.caracal.contract.ExpressDetailsContract;
import com.patrick.caracal.model.Trace;
import com.patrick.caracal.presenter.ExpressDetailsPresenter;
import com.rey.material.app.Dialog;

import butterknife.BindView;
import io.realm.RealmList;

/**
 * Created by Patrick on 16/8/30.
 */
public class DetailsActivity extends BaseActivity implements ExpressDetailsContract.View{

    private ExpressDetailsContract.Presenter presenter;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.timeline_recyclerview)
    RecyclerView view_timeline;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initView();

        String no = getIntent().getStringExtra("no");
        new ExpressDetailsPresenter(this, no);

    }

    /**
     * 初始化界面
     */
    private void initView() {
        initToolbarNav(toolbar);

        view_timeline.setLayoutManager(new LinearLayoutManager(this));
        view_timeline.setHasFixedSize(true);
    }

    private void initToolbarNav(Toolbar toolbar){
        toolbar.setNavigationIcon(R.drawable.ic_back_button_24dp);
        toolbar.setTitle("查看详情");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    @Override
    int layout() {
        return R.layout.fragment_express_details;
    }

    @Override
    public void setupMenu(@MenuRes int menu, Toolbar.OnMenuItemClickListener listener) {
        toolbar.inflateMenu(menu);
        toolbar.setOnMenuItemClickListener(listener);
    }

    @Override
    public void goBack() {
        onBackPressed();
    }

    @Override
    public void showDelDialog() {
        final Dialog delDialog = new Dialog(this);
        delDialog.title("是否删除快递单记录?")
                .positiveAction("确定")
                .negativeAction("取消")
                .positiveActionTextColor(getResources().getColor(android.R.color.black))
                .negativeActionTextColor(getResources().getColor(android.R.color.black))
                .positiveActionClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        presenter.deleteIt();
                        delDialog.dismiss();
                        onBackPressed();
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

    @Override
    public void setupDetails(RealmList<Trace> traces, String code, String companyName, String remark) {
        TimeLineAdapter adapter = new TimeLineAdapter(this, traces, genHeaderView(code, companyName, remark));
        view_timeline.setAdapter(adapter);
    }


    /**
     * 组装Header
     *
     * @param code        单号
     * @param companyName 快递公司名
     * @param remark      备注，如果没有备注，就用快递公司名字 + 单号作为显示到exp_name上
     * @return
     */
    private View genHeaderView(String code, String companyName, String remark) {
        View headerView = LayoutInflater.from(this).inflate(R.layout.item_details_header, view_timeline, false);

        ((TextView) headerView.findViewById(R.id.exp_name)).setText(remark);
        ((TextView) headerView.findViewById(R.id.exp_code)).setText(code);
        ((TextView) headerView.findViewById(R.id.exp_company)).setText(companyName);

        return headerView;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (this.presenter != null) this.presenter.start();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (this.presenter != null) this.presenter.stop();

    }

    @Override
    public void setPresenter(ExpressDetailsContract.Presenter presenter) {
        this.presenter = presenter;
    }
}
