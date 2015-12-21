package com.zs.dao.finance.studentexpensepay;

import com.feinno.framework.common.dao.jpa.EntityJpaDao;
import com.zs.domain.finance.StudentExpensePay;
import org.springframework.data.jpa.repository.Query;

/**
 * Created by Allen on 2015/12/21.
 */
public interface FindStudentPayByStudentCodeDAO extends EntityJpaDao<StudentExpensePay, Long> {
    @Query(nativeQuery = true, value = "SELECT sep.student_code, sum(money) FROM student_expense_pay sep " +
            "where sep.student_code = ?1")
    public Object[] find(String studentCode);
}
