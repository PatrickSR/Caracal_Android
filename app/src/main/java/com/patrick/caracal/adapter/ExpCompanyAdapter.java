package com.patrick.caracal.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.patrick.caracal.R;
import com.patrick.caracal.entity.ExpCompanyShowEntity;
import me.yokeyword.indexablelistview.IndexableAdapter;

/**
 * Created by Patrick on 16/6/26.
 * 快递城市列表选择页面的adapter
 */

public class ExpCompanyAdapter extends IndexableAdapter<ExpCompanyShowEntity> {
    private Context context;

    public ExpCompanyAdapter(Context context) {
        this.context = context;
    }

    @Override
    protected TextView onCreateTitleViewHolder(ViewGroup parent) {
        // 创建 Sticky字母的Header布局
        return (TextView)LayoutInflater.from(context).inflate(R.layout.item_exp_company_title,parent, false);
    }

    @Override
    protected ViewHolder onCreateViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_exp_company,parent,false);
        return new ExpCompanyViewHolder(view);
    }

    @Override
    protected void onBindViewHolder(ViewHolder holder, ExpCompanyShowEntity cityEntity) {
        ExpCompanyViewHolder viewHolder = (ExpCompanyViewHolder)holder;
        viewHolder.expName.setText(cityEntity.getName());
    }

    private class ExpCompanyViewHolder extends IndexableAdapter.ViewHolder{

        TextView expName;
        ExpCompanyViewHolder(View view) {
            super(view);
            expName = (TextView)view.findViewById(R.id.exp_name);
        }
    }
}
