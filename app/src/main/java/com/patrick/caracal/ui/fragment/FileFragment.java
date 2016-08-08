package com.patrick.caracal.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.patrick.caracal.R;
import com.patrick.caracal.adapter.ExpressAdapter;
import com.patrick.caracal.adapter.OnItemClickListener;
import com.patrick.caracal.contract.HomeContract;
import com.patrick.caracal.event.StartFragmentEvent;
import com.patrick.caracal.model.Express;
import com.patrick.caracal.presenter.FilePresenter;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.RealmResults;

/**
 * Created by 15920 on 2016/8/8.
 *
 * 归档Fragment
 */
public class FileFragment extends BaseFragment implements HomeContract.FileContract.View{

    @BindView(R.id.recycler)
    RecyclerView recyclerView;

    private ExpressAdapter adapter;

    private HomeContract.FileContract.Presenter presenter;

    public static FileFragment newInstance(){
        Bundle args = new Bundle();

        FileFragment fragment = new FileFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new FilePresenter(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_file, container, false);
        ButterKnife.bind(this,view);
        initRecyclerView();
        return view;
    }

    private void initRecyclerView(){
        recyclerView.setLayoutManager(new LinearLayoutManager(_mActivity));
        recyclerView.setHasFixedSize(true);
    }

    @Override
    public void showFileExpress(RealmResults<Express> results) {
        adapter = new ExpressAdapter(getContext(),results,true);
        recyclerView.setAdapter(this.adapter);

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
    public void onResume() {
        super.onResume();
        if (presenter!=null) {
            presenter.start();
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
    public void setPresenter(HomeContract.FileContract.Presenter presenter) {
        this.presenter = presenter;
    }
}
