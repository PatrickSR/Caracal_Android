package com.patrick.caracal.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.zxing.activity.CaptureActivity;
import com.jiongbull.jlog.JLog;
import com.patrick.caracal.Caracal;
import com.patrick.caracal.R;
import com.rey.material.widget.EditText;


import butterknife.BindView;
import butterknife.OnClick;

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
                companyName.setText(data.getStringExtra("name"));
                companyCode = data.getStringExtra("code");
//                Toast.makeText(QueryExpressActivity.this, selectExpressResult, Toast.LENGTH_SHORT).show();
            }

        }
    }

    /**
     * 快递公司名称
     */
    @BindView(R.id.choose_exp_company)
    TextView companyName;

    @OnClick(R.id.choose_exp_company)
    void selectCompany() {
        //TODO 选择快递公司，进入ExpCompanySelectActivity选择，选择后回到此界面把内容填充到 companyName里面
        //跳转到快递选择界面
        Intent intent = new Intent(QueryExpressActivity.this, ExpCompanySelectActivity.class);
        startActivityForResult(intent, SELECT_REQ_CODE);
    }

    //快递公司编码
    private String companyCode;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setSupportActionBar(toolbar);

        //弹框
//        mDialog = new AlertDialog.Builder(this)
//                .setMessage(getString(R.string.dialog_stream_none_error))
//                .setPositiveButton(getString(R.string.confirmation),
//                        new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                saveOrUpdateData();
//                                mDialog.dismiss();
//                            }
//                        })
//                .setNegativeButton(getString(R.string.cencel), new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        streamInfo = null;
//                        mDialog.dismiss();
//                    }
//                }).create();
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
     * 将查询结果进行保存
     */
//    private void saveOrUpdateData() {
//        realm.beginTransaction();
//        realm.createOrUpdateAllFromJson(Express.class, "[" + streamInfo + "]");
//        realm.commitTransaction();
//        QueryExpressActivity.this.setResult(RESULT_OK);
//        QueryExpressActivity.this.finish();
//    }

    /**
     * 添加快递
     */
    private void addExpress() {
        //检查快递单号和快递公司
        String expCode = inputExpCode.getText().toString();

        if (TextUtils.isEmpty(expCode)){
            showToast("请输入快递单号");
            return;
        }

        if (TextUtils.isEmpty(this.companyCode)){
            //如果公司编号是空，先去查询快递单归属
            queryExpressFrom(expCode);
        }else{
            //订阅快递
            subExpress(expCode,this.companyCode);
        }
    }



    private void queryExpressFrom(String expCode) {
        Caracal.getInstance().queryExpressCompany(expCode, new Caracal.ResultCallback<String>() {
            @Override
            public void onSuccess(String s) {
                //弹框选择
                JLog.d("Receive : <--- "+s);
            }

            @Override
            public void onFail(Exception e) {
                JLog.e(e);
                showToast(e.getMessage());
            }
        });
    }

    private void subExpress(String expCode, String companyCode) {
    }


    @Override
    int layout() {
        return R.layout.activity_express_add;
    }

}
