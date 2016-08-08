package com.patrick.caracal.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.patrick.caracal.R;
import com.patrick.caracal.adapter.OnItemClickListener;
import com.patrick.caracal.event.StartFragmentEvent;
import com.patrick.caracal.model.Express;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import co.moonmonkeylabs.realmsearchview.ClearableEditText;
import co.moonmonkeylabs.realmsearchview.RealmSearchAdapter;
import co.moonmonkeylabs.realmsearchview.RealmSearchView;
import co.moonmonkeylabs.realmsearchview.RealmSearchViewHolder;
import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by Patrick on 16/8/8.
 */
public class SearchExpressFragment extends BaseBackFragment {

    @BindView(R.id.search_view)
    RealmSearchView searchView;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.search_bar)
    ClearableEditText searchBar;

    private Realm realm;

    private SearchAdapter adapter;

    public static SearchExpressFragment newInstance(){
        Bundle args = new Bundle();
        SearchExpressFragment fragment = new SearchExpressFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        realm = Realm.getDefaultInstance();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search,container,false);
        ButterKnife.bind(this,view);

        initToolbar();
        initAdapter();
        initSearchBar();
        return attachToSwipeBack(view);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (realm!=null){
            realm.close();
            realm = null;
        }
        hideSoftInput();
    }

    private void initToolbar(){
        toolbar.setTitle("搜索");
        toolbar.setNavigationIcon(R.drawable.ic_back_button_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _mActivity.onBackPressed();
            }
        });
    }

    private void initAdapter(){

        adapter = new SearchAdapter(getContext(),realm,"code");
        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int position, View view, RecyclerView.ViewHolder viewHolder) {
                Bundle bundle = new Bundle();

                Express exp = adapter.getData().get(position);
                bundle.putString("expCode",exp.code);

                EventBus.getDefault().post(new StartFragmentEvent(ExpressDetailsFragment.newInstance(bundle)));
            }
        });
        searchView.setAdapter(adapter);
    }



    private void initSearchBar(){
//        searchBar.setBackgroundDrawable(null);
    }

    //------------------------------------Adapter----------------------------------------------//

    public class SearchAdapter extends RealmSearchAdapter<Express,SearchAdapter.ViewHolder>{

        private OnItemClickListener mClickListener;

        public SearchAdapter(@NonNull Context context, @NonNull Realm realm, @NonNull String filterKey) {
            super(context, realm, filterKey);
        }

        @Override
        public SearchAdapter.ViewHolder onCreateRealmViewHolder(ViewGroup viewGroup, int viewType) {
            View view = inflater.inflate(R.layout.item_search_express, viewGroup, false);
            final ViewHolder viewHolder = new SearchAdapter.ViewHolder(view);

            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mClickListener != null) {
                        mClickListener.onItemClick(viewHolder.getAdapterPosition(), v, viewHolder);
                    }
                }
            });
            return viewHolder;
        }

        @Override
        public void onBindRealmViewHolder(SearchAdapter.ViewHolder viewHolder, int position) {
            Express express = realmResults.get(position);
            viewHolder.bind(express);
        }

        public void setOnItemClickListener(OnItemClickListener listener) {
            this.mClickListener = listener;
        }

        public RealmResults<Express> getData(){
            return realmResults;
        }

        public class ViewHolder extends RealmSearchViewHolder{


            @BindView(R.id.exp_name)
            public TextView exp_name;   //单号


            @BindView(R.id.acceptTime)
            public TextView tv_acceptTime;    //接应时间

            @BindView(R.id.acceptStation)
            public TextView tv_acceptStation;  //接应站点

            public ViewHolder(View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
            }


            public void bind(Express express) {
                setExpName(express.code, express.companyName, express.remark);
                setAcceptStation(express.lastAcceptStation);
                setAcceptTime(express.lastAcceptTime);
//            setCompanyType(express.companyType);
            }

            private void setExpName(String code, String companyName, String remark) {
                if (TextUtils.isEmpty(remark)) {
                    //如果没有备注的，就显示快递公司+单号作为名字
                    exp_name.setText(companyName + " " + code);
                } else {
                    //如果有备注就显示备注
                    exp_name.setText(remark);
                }
            }

            public void setAcceptTime(String s) {
                if (TextUtils.isEmpty(s)) {
                    tv_acceptTime.setText("暂无更新");
                } else {
                    tv_acceptTime.setText("更新时间：" + s);
                }

            }

            public void setAcceptStation(String s) {
                if (TextUtils.isEmpty(s)) {
                    tv_acceptStation.setText("该单号暂无物流进展，请稍后刷新");
                } else {
                    tv_acceptStation.setText("最新到达：" + s);
                }
            }

        }
    }
}
