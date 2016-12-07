package com.zs.service.finance.studentexpense;

import com.feinno.framework.common.service.EntityService;
import com.zs.domain.finance.StudentExpense;
import com.zs.domain.finance.StudentExpenseBuy;

/**
 * Created by Allen on 2015/7/29.
 */
public interface AddStuEBuyService extends EntityService<StudentExpense> {
    /**
     * 添加已消费金额的方法
     * @param changeMoney
     * @param studentCode
     * @throws Exception
     */
    public void addStuEBuy(StudentExpenseBuy studentExpenseBuy, String userName) throws Exception;
}
