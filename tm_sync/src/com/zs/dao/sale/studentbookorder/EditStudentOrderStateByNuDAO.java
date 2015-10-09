package com.zs.dao.sale.studentbookorder;

import com.feinno.framework.common.dao.jpa.EntityJpaDao;
import com.zs.domain.sale.StudentBookOrder;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;

/**
 * Created by Allen on 2015/10/6.
 */
public interface EditStudentOrderStateByNuDAO extends EntityJpaDao<StudentBookOrder,Long> {
    @Modifying
    @Query(nativeQuery = true, value = "update student_book_order sbo INNER JOIN student_book_order_package sbop on sbo.package_id = sbop.id " +
            "set sbo.state = ?1, sbo.operator = '管理员', sbo.operate_time = ?2 where sbop.logistic_code = ?3")
    public void editStudentOrderStateByNu(int state, Date operateTime, String nu);
}
