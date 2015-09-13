package com.zs.service.scheduler;

import com.feinno.framework.common.service.EntityService;
import com.zs.domain.finance.StudentExpense;

/**
 * Created by Allen on 2015/9/12.
 */
public interface CreateStudentExpenseService extends EntityService<StudentExpense> {
    public void add();
}
