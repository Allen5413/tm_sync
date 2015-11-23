package com.zs.service.finance.spotexpense;

import com.feinno.framework.common.service.EntityService;
import com.zs.domain.finance.SpotExpense;

/**
 * 增加学习中心待确认金额数量的接口
 * Created by LihongZhang on 2015/5/17.
 */
public interface AddConfirmedMoneyService extends EntityService<SpotExpense> {

    public void addConfirmedMoney(Float money, String spotCode, String userName) throws Exception;
}
