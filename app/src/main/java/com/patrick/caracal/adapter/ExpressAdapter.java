package com.patrick.caracal.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import com.patrick.caracal.R;
import com.patrick.caracal.model.Express;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.OrderedRealmCollection;
import io.realm.RealmRecyclerViewAdapter;

/**
 * Created by 15920 on 2016/7/31.
 */
public class ExpressAdapter extends RealmRecyclerViewAdapter<Express, ExpressAdapter.ViewHolder> {

    private OnItemClickListener mClickListener;

    public ExpressAdapter(Context context, OrderedRealmCollection<Express> data, boolean autoUpdate) {
        super(context, data, autoUpdate);
    }

    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.adaptet_parcel_item, parent, false);

        final ViewHolder viewHolder = new ViewHolder(view);

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
    public void onBindViewHolder(ViewHolder holder, int position) {
        Express express = getData().get(position);

        holder.exp_code.setText(express.code);
        holder.exp_company_name.setText(express.companyName);
        holder.tv_acceptTime.setText(express.lastAcceptTime);
        holder.tv_acceptStation.setText(express.lastAcceptStation);

    }

    public Express getExpress(int position){return getData().get(position);}

    public void setOnItemClickListener(OnItemClickListener listener){
        this.mClickListener = listener;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.exp_code)
        public TextView exp_code;   //单号

        @BindView(R.id.exp_company_name)
        public TextView exp_company_name;    //公司名称

        @BindView(R.id.acceptTime)
        public TextView tv_acceptTime;    //接应时间

        @BindView(R.id.acceptStation)
        public TextView tv_acceptStation;  //接应站点

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
