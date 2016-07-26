package com.patrick.caracal.model;

import io.realm.RealmObject;

/**
 * Created by Patrick on 16/7/26.
 */
public class Trace extends RealmObject{
    public String acceptStation;    //接收站点

    public String acceptTime;   //接收时间

    public String remark;       //备注
}
