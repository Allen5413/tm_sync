package com.zs.dao.basic.finance.studentexpense;

import com.feinno.framework.common.dao.jpa.EntityJpaDao;
import com.zs.domain.finance.StudentExpense;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * 查询一个学生的费用情况
 * Created by Allen on 2015/9/18.
 */
public interface FindByStudentCodeDAO extends EntityJpaDao<StudentExpense, Long> {
    @Query("from StudentExpense where studentCode = ?1 ORDER BY semesterId")
    public List<StudentExpense> findByStudentCode(String studentCode);
}
