package com.patrick.caracal;

/**
 * Created by Patrick on 16/7/26.
 */
public class Caracal {

    private static Caracal caracal;

    private Caracal(){}

    public static Caracal getInstance() {
        if (caracal == null) {
            caracal = new Caracal();
        }
        return caracal;
    }

    // 添加/订阅 快递
    public void subExpress(){}

    // 删除 快递 从DB上删除
    public void delExpress(){}

    // 归档 快递 更改快递状态
    public void keepExpress(){}

    // 获取全部快递单
    public void getAllExpress(){}

    // 获取单个快递单
    public void getExpress(String expNo){}

    // 查询快递属于哪个公司
    public void queryExpressCompany(String expNo){}

    // 根据快递公司编码查询公司信息
    public void queryCompany(String companyCode){}

    // 刷新全部快递单
    public void refresh(){}
}
