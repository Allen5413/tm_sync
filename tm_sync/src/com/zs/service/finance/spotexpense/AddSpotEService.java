package com.zs.service.finance.spotexpense;

import com.feinno.framework.common.service.EntityService;
import com.zs.domain.finance.SpotExpense;

/**
 * 添加学习中心费用记录的接口
 * Created by LihongZhang on 2015/5/17.
 */
public interface AddSpotEService extends EntityService<SpotExpense>{

    /**
     * 添加的方法
     * @param spotExpense
     * @param userName
     * @throws Exception
     */
    public void addSpotE(SpotExpense spotExpense, String userName) throws Exception;
}
