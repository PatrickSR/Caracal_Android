package com.patrick.caracal.model;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Patrick on 16/8/25.
 */
public class TraceList extends RealmObject {

    @PrimaryKey
    public String no;

    public RealmList<Trace> list;
}
