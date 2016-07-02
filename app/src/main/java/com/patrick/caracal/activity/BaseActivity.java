package com.patrick.caracal.activity;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.widget.Toast;

import butterknife.ButterKnife;
import io.realm.Realm;

/**
 * Created by Patrick on 16/6/26.
 *
 * 创建的Activity必须继承此类
 *
 *
 */
public abstract class BaseActivity extends AppCompatActivity {

    //快递条形码扫描RequestCode
    public static final int CAPTURE_REQ_CODE = 100;
    //跳转快递公司RequestCode
    public static final int SELECT_REQ_CODE = 200;

    protected Realm realm;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layout());

        //通过Butter Knife绑定布局上的控件
        ButterKnife.bind(this);

        setupRealm();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        closeRealm();
    }

    /**
     * 初始化Realm
     */
    private void setupRealm() {
        realm = Realm.getDefaultInstance();
    }

    /**
     * 关闭Realm
     *
     * Realm 实例是基于引用计数的, 也就是说假设你在同一个线程中调用了 getInstance() 两次，
     * 你需要同样调用 close() 两次以关闭该实例。举例来说，如果你需要实现 Runnable，
     * 简单地在函数开始的时候调用 getInstance()，
     * 在函数结束的时候调用 close() 即可！
     */
    private void closeRealm(){
        if (realm != null) {
            realm.close();
        }
    }
    /**
     * 集成BaseActivity之后,必须通过实现layout()来提供布局给activity初始化
     * @return 填充的布局
     */
    @LayoutRes
    abstract int layout();

    /**
     * 显示一个Toast框,通常是为了显示提示文本
     * @param tips 提示
     */
    protected void showToast(final String tips){

        if (TextUtils.isEmpty(tips)) return;

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(BaseActivity.this,tips, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
