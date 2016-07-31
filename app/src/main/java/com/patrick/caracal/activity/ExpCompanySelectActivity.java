package com.patrick.caracal.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.patrick.caracal.R;
import com.patrick.caracal.entity.ExpCompanyShowEntity;
import com.patrick.caracal.adapter.ExpCompanyAdapter;

import butterknife.BindView;
import me.yokeyword.indexablelistview.IndexEntity;
import me.yokeyword.indexablelistview.IndexHeaderEntity;
import me.yokeyword.indexablelistview.IndexableStickyListView;

public class ExpCompanySelectActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.listView)
    IndexableStickyListView exp_listview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initView();
//
//        expCompanyModel = new ExpCompanyModel(this, realm);

        //热门快递公司header,adapter使用
//        IndexHeaderEntity<ExpCompanyShowEntity> hotCompanyHeader = new IndexHeaderEntity<>(
//                "热",
//                "热门快递",
//                expCompanyModel.getHotExpCompany());
//
//        exp_listview.bindDatas(expCompanyModel.getAllExpCompany(),hotCompanyHeader);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_select_exp_company, menu);
        MenuItem menuItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(menuItem);
        searchView.setQueryHint("请输入快递公司");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                exp_listview.searchTextChange(newText);
                return true;
            }
        });
        return true;
    }

    @Override
    int layout() {
        return R.layout.activity_indexable_listview;
    }

    /**
     * 初始化快递列表和toolbar
     */
    private void initView() {
        ExpCompanyAdapter adapter = new ExpCompanyAdapter(this);
        exp_listview.setAdapter(adapter);
        exp_listview.setOnItemContentClickListener(onClickExpCompany);

        //设置toolbar
        setSupportActionBar(toolbar);
    }

    private IndexableStickyListView.OnItemContentClickListener onClickExpCompany = new IndexableStickyListView.OnItemContentClickListener() {
        @Override
        public void onItemClick(View v, IndexEntity indexEntity) {
//            Log.d(TAG, "onItemClick: "+indexEntity.getName());
            //TODO 点击后，把结果返回到上一个界面
            //跳转至上界面
            Intent intent = new Intent();
            intent.putExtra("SelectExpress", indexEntity.getName());
            ExpCompanySelectActivity.this.setResult(RESULT_OK, intent);
            ExpCompanySelectActivity.this.finish();
        }
    };
}
