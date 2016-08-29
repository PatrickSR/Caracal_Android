package com.patrick.caracal.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Patrick on 16/8/23.
 */
public class User extends RealmObject {

    @PrimaryKey
    public String uid;

    public String name;

    public String avatar;

    public String email;

    public User(){}

    public User(String uid) {
        this.uid = uid;
    }
}
