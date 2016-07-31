package com.patrick.caracal.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Patrick on 16/7/26.
 */
public class Company extends RealmObject{

    public static final int TYPE_DOMESTIC = 1;
    public static final int TYPE_FOREIGN = 2;
    public static final int TYPE_TRANSPORT = 3;

    @PrimaryKey
    public String code; //公司编码

    public boolean hot;  //是否热门

    public String name; //公司名称

    public int type; //公司类型，1->国内  2->国外 3->中转
}
