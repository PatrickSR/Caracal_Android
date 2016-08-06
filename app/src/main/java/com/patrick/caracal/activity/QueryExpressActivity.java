package com.patrick.caracal.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.activity.CaptureActivity;
import com.jiongbull.jlog.JLog;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.OnItemClickListener;
import com.patrick.caracal.Caracal;
import com.patrick.caracal.R;
import com.patrick.caracal.model.Company;
import com.rey.material.app.BottomSheetDialog;
import com.rey.material.widget.Button;
import com.rey.material.widget.EditText;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;
import io.realm.Realm;

/**
 * Created by junz on 2016/6/18.
 * 1、本界面主要是用户填写和选择需查询快递公司的信息
 */

public class QueryExpressActivity extends BaseActivity {

//    private Dialog mDialog;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    /**
     * 快递单号文本框
     */
    @BindView(R.id.input_exp_code)
    EditText inputExpCode;

    @OnClick(R.id.scan_exp_code)
    void scanExpBarcode() {
        /*
        * TODO 跳入扫码界面，然后当扫码完成后，跳回本界面，把扫码的String填充到 inputExpCode 里面
        */
        // 调用扫码界面
        Intent intent = new Intent(this, CaptureActivity.class);
        startActivityForResult(intent, CAPTURE_REQ_CODE);
    }

    // 返回结果
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) { //RESULT_OK = -1*

            if (requestCode == CAPTURE_REQ_CODE) {
                //扫码后的结果
                Bundle bundle = data.getExtras();
                String scanResult = bundle.getString("result");
                inputExpCode.setText(scanResult);
//                Toast.makeText(QueryExpressActivity.this, scanResult, Toast.LENGTH_LONG).show();
            } else if (requestCode == SELECT_REQ_CODE) {
                //选择快递公司结果
                tv_companyName.setText(data.getStringExtra("name"));
                companyName = data.getStringExtra("name");
                companyCode = data.getStringExtra("code");
//                Toast.makeText(QueryExpressActivity.this, selectExpressResult, Toast.LENGTH_SHORT).show();
            }

        }
    }

    /**
     * 快递公司名称
     */
    @BindView(R.id.choose_exp_company)
    TextView tv_companyName;

    @OnClick(R.id.choose_exp_company)
    void selectCompany() {
        //TODO 选择快递公司，进入ExpCompanySelectActivity选择，选择后回到此界面把内容填充到 companyName里面
        //跳转到快递选择界面
        Intent intent = new Intent(QueryExpressActivity.this, ExpCompanySelectActivity.class);
        startActivityForResult(intent, SELECT_REQ_CODE);
    }

    //快递公司编码
    private String companyCode;

    //快递公司名称
    private String companyName;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_express_add, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.done:
                addExpress();
                break;
        }
        return true;
    }
    /**
     * 添加快递
     */
    private void addExpress() {
        //检查快递单号和快递公司
        String expCode = inputExpCode.getText().toString();

        if (TextUtils.isEmpty(expCode)) {
            showToast("请输入快递单号");
            return;
        }

        if (TextUtils.isEmpty(this.companyCode)) {
            //如果公司编号是空，先去查询快递单归属
            queryExpressFrom(expCode);
        } else {
            //订阅快递
            subExpress(expCode, this.companyCode,this.companyName);
        }
    }


    private void queryExpressFrom(final String expCode) {
        Caracal.getInstance().queryExpressCompany(expCode, new Caracal.ResultCallback<String>() {
            @Override
            public void onSuccess(final String s) {
                //弹框选择
                JLog.d("Receive : <--- " + s);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        showCompanySelectView(expCode,s);
                    }
                });

            }

            @Override
            public void onFail(Exception e) {
                JLog.e(e);
                showToast(e.getMessage());
            }
        });
    }

    /**
     * 显示快递公司选择的列表
     *
     * @param company_json
     */
    private void showCompanySelectView(final String expCode, String company_json) {
        try {
            JSONObject jsonObject = new JSONObject(company_json);
            JSONArray companies = jsonObject.getJSONArray("companies");

            ArrayList<Company> list = new ArrayList<>();

            if (companies.length() < 1) {
                showToast("暂时无法识别该快递单公司");
                return;
            }

            for (int i = 0; i < companies.length(); i++) {
                String code = companies.getJSONObject(i).optString("code", "");
                String name = companies.getJSONObject(i).optString("name", "");

                //如果code和name都不为空才保存到list里
                if (!TextUtils.isEmpty(code) && !TextUtils.isEmpty(name)) {
                    list.add(new Company(code, name));
                }
            }

            SelectAdapter adapter = new SelectAdapter(this, list);

            DialogPlus dialog = DialogPlus.newDialog(this)
                    .setAdapter(adapter)
                    .setHeader(R.layout.select_company_header)
                    .setOnItemClickListener(new OnItemClickListener() {
                        @Override
                        public void onItemClick(DialogPlus dialog, Object item, View view, int position) {
                            Company company = (Company) item;

                            JLog.d("code:" + company.code);
                            JLog.d("name:" + company.name);

                            subExpress(expCode,company.code, company.name);

                            dialog.dismiss();
                        }
                    })
                    .setExpanded(true)  // This will enable the expand feature, (similar to android L share dialog)
                    .create();
            dialog.show();

        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    /**
     * 订阅这个快递单，然后保存到realm
     *
     * @param expCode
     * @param companyCode
     */
    private void subExpress(String expCode, String companyCode, String companyName) {

        Caracal.getInstance().subExpress(expCode, companyCode,companyName, new Caracal.ResultCallback<String>() {
            @Override
            public void onSuccess(String s) {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        Toast.makeText(QueryExpressActivity.this, "订阅成功", Toast.LENGTH_SHORT).show();
                    }
                });

                QueryExpressActivity.this.finish();
            }

            @Override
            public void onFail(Exception e) {
                //订阅失败，提示用户
                JLog.e(e);
            }
        });
    }


    /**
     * 当自动识别公司的列表adapter
     */
    private static class SelectAdapter extends BaseAdapter {

        private LayoutInflater layoutInflater;

        private ArrayList<Company> list;

        public SelectAdapter(Context context, ArrayList<Company> list) {
            layoutInflater = LayoutInflater.from(context);
            this.list = list;
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = layoutInflater.inflate(android.R.layout.simple_list_item_1, parent, false);

            TextView textView = (TextView) view.findViewById(android.R.id.text1);
            textView.setText(list.get(position).name);
            return view;
        }
    }


    @Override
    int layout() {
        return R.layout.activity_express_add;
    }

}
