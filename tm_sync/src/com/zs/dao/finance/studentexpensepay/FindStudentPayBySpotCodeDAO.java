package com.zs.dao.finance.studentexpensepay;

import com.feinno.framework.common.dao.jpa.EntityJpaDao;
import com.zs.domain.finance.StudentExpensePay;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by Allen on 2015/12/17.
 */
public interface FindStudentPayBySpotCodeDAO extends EntityJpaDao<StudentExpensePay, Long> {
    @Query(nativeQuery = true, value = "SELECT sep.student_code, sum(money) FROM student_expense_pay sep, sync_student s " +
            "where sep.student_code = s.code and s.spot_code = ?1 group by sep.student_code")
    public List<Object[]> find(String spotCode);
}
