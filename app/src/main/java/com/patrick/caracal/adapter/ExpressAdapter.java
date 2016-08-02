package com.patrick.caracal.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.patrick.caracal.R;
import com.patrick.caracal.model.Company;
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

        holder.exp_code.setText("单号："+express.code);
        holder.exp_company_name.setText(express.companyName);
        holder.setAcceptTime(express.lastAcceptTime);
        holder.setAcceptStation(express.lastAcceptStation);
        holder.setCompanyType(express.companyType);

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

        @BindView(R.id.company_type)
        public ImageView img_company_type;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void setAcceptTime(String s){
            if (TextUtils.isEmpty(s)){
                tv_acceptTime.setText("暂无更新");
            }else{
                tv_acceptTime.setText("更新时间："+s);
            }

        }

        public void setAcceptStation(String s){
            if (TextUtils.isEmpty(s)){
                tv_acceptStation.setText("该单号暂无物流进展，请稍后刷新");
            }else{
                tv_acceptStation.setText("最新到达："+s);
            }
        }

        public void setCompanyType(int companyType){
            Drawable drawable = null;
            Context context = this.itemView.getContext();

            switch (companyType){
                case Company.TYPE_DOMESTIC:
                    //国内
                    drawable = context.getResources().getDrawable(R.drawable.ic_domestic);
                    break;
                case Company.TYPE_FOREIGN:
                    //国外
                    drawable = context.getResources().getDrawable(R.drawable.ic_foreign);
                    break;
                case Company.TYPE_TRANSPORT:
                    //中转
                    drawable = context.getResources().getDrawable(R.drawable.ic_transport);
                    break;
                
            }

            if (drawable != null) img_company_type.setImageDrawable(drawable);

        }
    }
}
