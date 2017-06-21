package com.zs.dao.sale.onceordertm;

import com.feinno.framework.common.dao.jpa.EntityJpaDao;
import com.zs.domain.sale.StudentBookOnceOrderTM;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

/**
 * Created by Allen on 2016/6/17.
 */
public interface DelStudentBookOnceOrderTMByOrderIdDAO extends EntityJpaDao<StudentBookOnceOrderTM, Long> {
    @Modifying
    @Query(nativeQuery = true, value = "delete from student_book_once_order_tm where order_id = ?1")
    public void del(long order_id);

    @Modifying
    @Query(nativeQuery = true, value = "delete sbotm.* from student_book_once_order sbo, student_book_once_order_tm sbotm " +
            "where sbo.id = sbotm.order_id and sbo.state < 5 and substr(sbo.order_code, 1, 3) = 'FSO' and sbo.student_code = ?1 and sbotm.course_code = ?2")
    public void delByStudentCodeAndCourseCodeForNotSend(String studentCode, String courseCode)throws Exception;
}
