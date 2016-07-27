package com.patrick.caracal.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Patrick on 16/7/26.
 */
public class Company extends RealmObject{

    @PrimaryKey
    public String code; //公司编码

    public String hot;  //是否热门

    public String name; //公司名称

    public int type; //公司类型，1->国内  2->国外 3->中转
}
