package com.zs.service.finance.spotexpense;

import com.feinno.framework.common.service.EntityService;
import com.zs.domain.finance.SpotExpense;
import com.zs.domain.finance.SpotExpenseBuy;

/**
 * Created by Allen on 2015/8/3.
 */
public interface AddSpotEBuyService extends EntityService<SpotExpense> {
    /**
     * 添加已消费金额的方法
     * @param changeMoney
     * @param spotCode
     * @throws Exception
     */
    public void addSpotEBuy(SpotExpenseBuy spotExpenseBuy, String userName) throws Exception;
}
