package com.zs.service.finance.spotexpense;

import com.feinno.framework.common.service.EntityService;
import com.zs.domain.finance.SpotExpense;

/**
 * 修改学习中心待确认金额的service接口
 * Created by LihongZhang on 2015/5/22.
 */
public interface UpdateSpotCMService extends EntityService<SpotExpense> {
    /**
     * 添加的方法
     * @param money
     * @param spotCode
     * @param userName
     * @throws Exception
     */
    public void addConfirmedMoney(Float money, String spotCode, String userName) throws Exception;

    /**
     * 减少的方法
     * @param money
     * @param spotCode
     * @param userName
     * @throws Exception
     */
    public void subConfirmedMoney(Float money, String spotCode, String userName) throws Exception;
}
