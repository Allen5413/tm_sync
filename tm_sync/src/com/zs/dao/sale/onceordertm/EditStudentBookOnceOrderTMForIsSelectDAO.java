package com.zs.dao.sale.onceordertm;

import com.feinno.framework.common.dao.jpa.EntityJpaDao;
import com.zs.domain.sale.StudentBookOnceOrderTM;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

/**
 * Created by Allen on 2016/6/22.
 */
public interface EditStudentBookOnceOrderTMForIsSelectDAO extends EntityJpaDao<StudentBookOnceOrderTM, Long> {
    @Modifying
    @Query(nativeQuery = true, value = "update student_book_once_order_tm sbotm INNER JOIN student_book_once_order sbo on sbo.id = sbotm.order_id set sbotm.is_select = 1 " +
            "where sbo.student_code = ?1 and sbotm.course_code = ?2")
    public void editor(String studentCode, String courseCode)throws Exception;
}
