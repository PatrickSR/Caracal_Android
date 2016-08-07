package com.patrick.caracal.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.patrick.caracal.Caracal;
import com.patrick.caracal.R;
import com.patrick.caracal.model.ExpCompanyShowEntity;
import com.patrick.caracal.adapter.ExpCompanyAdapter;
import com.patrick.caracal.model.Company;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import io.realm.Realm;
import io.realm.RealmResults;
import me.yokeyword.indexablelistview.IndexEntity;
import me.yokeyword.indexablelistview.IndexHeaderEntity;
import me.yokeyword.indexablelistview.IndexableStickyListView;

public class ExpCompanySelectActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.listView)
    IndexableStickyListView exp_listview;

    //全部的快递列表(Adapter显示用的)
    private List<ExpCompanyShowEntity> expCompanyList = new ArrayList<>();

    //热门的快递列表(Adapter显示用的)
    private List<ExpCompanyShowEntity> hotCompanyList = new ArrayList<>();

    private Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        realm = Realm.getDefaultInstance();
        initView();

        initData();

        //热门快递公司header,adapter使用
        IndexHeaderEntity<ExpCompanyShowEntity> hotCompanyHeader = new IndexHeaderEntity<>(
                "热",
                "热门快递",
                hotCompanyList);

        exp_listview.bindDatas(expCompanyList, hotCompanyHeader);
    }

    private void initData() {
        RealmResults<Company> hotCompany = Caracal.getInstance().getHotCompany(realm);
        for (Company c :
                hotCompany) {
            hotCompanyList.add(new ExpCompanyShowEntity(c.name,c.code));
        }

        RealmResults<Company> allCompany = Caracal.getInstance().getAllCompany(realm);
        for (Company c :
                allCompany) {
            expCompanyList.add(new ExpCompanyShowEntity(c.name,c.code));
        }
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
            ExpCompanyShowEntity entity = (ExpCompanyShowEntity)indexEntity;

            //跳转至上界面
            Intent intent = new Intent();
            intent.putExtra("name", entity.getName());
            intent.putExtra("code", entity.getCode());
            ExpCompanySelectActivity.this.setResult(RESULT_OK, intent);
            ExpCompanySelectActivity.this.finish();
        }
    };

    @Override
    protected void onDestroy() {
        realm.close();
        super.onDestroy();
    }
}
