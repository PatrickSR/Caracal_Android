package com.patrick.caracal.ui.fragment;

import android.os.Bundle;
import android.support.annotation.MenuRes;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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
    }

    @Override
    public void setupMenu(@MenuRes int menu, Toolbar.OnMenuItemClickListener listener) {
        toolbar.inflateMenu(menu);
        toolbar.setOnMenuItemClickListener(listener);
    }

    @Override
    public void goBack() {
        _mActivity.onBackPressed();
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

    @Override
    public void setupDetails(RealmList<Trace> traces,String code,String companyName,String remark) {
        TimeLineAdapter adapter = new TimeLineAdapter(getContext(),traces,genHeaderView(code,companyName,remark));
        view_timeline.setAdapter(adapter);
    }



    /**
     * 组装Header
     * @param code 单号
     * @param companyName 快递公司名
     * @param remark 备注，如果没有备注，就用快递公司名字 + 单号作为显示到exp_name上
     * @return
     */
    private View genHeaderView(String code,String companyName,String remark){
        View headerView = LayoutInflater.from(getContext()).inflate(R.layout.item_details_header,view_timeline,false);

        if (TextUtils.isEmpty(remark)){
            String name = companyName + " "+ code;
            ((TextView)headerView.findViewById(R.id.exp_name)).setText(name);
        }else{
            ((TextView)headerView.findViewById(R.id.exp_name)).setText(remark);
            TextView exp_code = (TextView)headerView.findViewById(R.id.exp_code);
            exp_code.setVisibility(View.VISIBLE);
            exp_code.setText(code);
        }
        ((TextView)headerView.findViewById(R.id.exp_company)).setText(companyName);

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
