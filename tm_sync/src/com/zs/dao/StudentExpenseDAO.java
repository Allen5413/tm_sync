package com.zs.dao;

import com.feinno.framework.common.dao.jpa.EntityJpaDao;
import com.zs.domain.finance.StudentExpense;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by Allen on 2015/9/12.
 */
public interface StudentExpenseDAO extends EntityJpaDao<StudentExpense, Long> {
    @Query("from StudentExpense where studentCode = ?1 and semesterId = 1")
    public StudentExpense getList(String studentCode);

    @Query("from StudentExpense where studentCode = ?1 and semesterId = 2")
    public StudentExpense getList2(String studentCode);
}
