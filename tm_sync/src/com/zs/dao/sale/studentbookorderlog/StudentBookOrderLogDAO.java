package com.zs.dao.sale.studentbookorderlog;

import com.feinno.framework.common.dao.jpa.EntityJpaDao;
import com.zs.domain.sale.StudentBookOrderLog;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

/**
 * Created by Allen on 2015/5/12.
 */
public interface StudentBookOrderLogDAO extends EntityJpaDao<StudentBookOrderLog, Long> {
    @Modifying
    @Query(nativeQuery = true, value = "INSERT into student_book_order_log(order_code, state, operator, operate_time) select DISTINCT sbo.order_code, 5, '管理员', now() from student_book_order sbo, student_book_order_package sbop \n" +
            "where sbo.package_id = sbop.id and sbop.logistic_code= ?1")
    public void addSignOrderLog(String nu);
}
