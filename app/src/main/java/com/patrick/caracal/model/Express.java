package com.patrick.caracal.model;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Patrick on 16/7/26.
 */
public class Express extends RealmObject{

    //当前状态 2-在途中，3-签收,4-问题件
    public static final int STATE_ON_THE_WAY = 2;

    public static final int STATE_RECEIVED = 3;

    public static final int STATE_PROBLEM = 4;

    @PrimaryKey
    public String code; //单号

    public String company; //公司编码

    public int state; //当前状态 2-在途中，3-签收,4-问题件

    public RealmList<Trace> traces;
}
