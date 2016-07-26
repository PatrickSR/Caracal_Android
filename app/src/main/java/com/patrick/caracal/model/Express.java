package com.patrick.caracal.model;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Patrick on 16/7/26.
 */
public class Express extends RealmObject{
    @PrimaryKey
    public String code; //单号

    public String company; //公司编码

    public int state; //当前状态

    public RealmList<Trace> traces;
}
