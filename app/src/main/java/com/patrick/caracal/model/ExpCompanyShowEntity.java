package com.patrick.caracal.model;

import me.yokeyword.indexablelistview.IndexEntity;

/**
 * Created by Patrick on 16/6/26.
 *
 * 为了显示快递列表的实例
 */

public class ExpCompanyShowEntity extends IndexEntity {

    private String code;

    public ExpCompanyShowEntity(String name, String code) {
        super(name);
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
