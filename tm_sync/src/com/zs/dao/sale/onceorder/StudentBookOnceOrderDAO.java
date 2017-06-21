package com.zs.dao.sale.onceorder;

import com.feinno.framework.common.dao.jpa.EntityJpaDao;
import com.zs.domain.sale.StudentBookOnceOrder;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by Allen on 2016/6/16.
 */
public interface StudentBookOnceOrderDAO extends EntityJpaDao<StudentBookOnceOrder, Long> {

    @Query(nativeQuery = true, value = "select t.* from (" +
            "select sbo.student_code, sbotm.course_code from student_book_order sbo, student_book_order_tm sbotm " +
            "where sbo.order_code = sbotm.order_code and sbo.state > 3 and sbotm.count > 0 and sbotm.price > 0 " +
            "union " +
            "select sbo.student_code, sbotm.course_code from student_book_once_order sbo, student_book_once_order_tm sbotm " +
            "where sbo.id = sbotm.order_id and sbo.state > 4 and sbotm.count > 0  and sbotm.price > 0 " +
            ") t where t.student_code = ?1 and t.course_code = ?2")
    public List<Object[]> findByStudentCodeAndCourseCodeForSend(String studentCode, String courseCode)throws Exception;
}
