package com.zs.service.finance.studentexpensepay;

import com.feinno.framework.common.service.EntityService;
import com.zs.domain.finance.StudentExpensePay;

/**
 *学生入账记录的添加service接口
 * Created by LihongZhang on 2015/5/15.
 */
public interface AddStudentExpensePayService extends EntityService<StudentExpensePay> {
    /**
     * 添加入账记录的方法
     * 逻辑是，添加入账记录的同时，修改学生费用的已支付金额
     * @param studentExpensePay
     * @throws Exception
     */
    public void addStudentExpensePay(StudentExpensePay studentExpensePay, String userName, String loginName) throws Exception;
}
