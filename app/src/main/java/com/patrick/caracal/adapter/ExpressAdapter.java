package com.patrick.caracal.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
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
        View view = inflater.inflate(R.layout.item_home_express_list, parent, false);

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
        holder.bind(express);

    }

    public Express getExpress(int position) {
        return getData().get(position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mClickListener = listener;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

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
