package com.zs.dao.sale.studentbookordertm;

import com.feinno.framework.common.dao.jpa.EntityJpaDao;
import com.zs.domain.sale.StudentBookOrderTM;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by Allen on 2015/5/12.
 */
public interface StudentBookOrderTmDAO extends EntityJpaDao<StudentBookOrderTM, Long> {
    @Query("from StudentBookOrderTM where orderCode = ?1")
    public List<StudentBookOrderTM> findStudentBookOrderTMByOrderCode(String orderCode);

    @Query(nativeQuery = true, value = "select t.* from " +
            "(select DISTINCT sbotm.id, sbo.student_code, sbotm.course_code from student_book_order sbo, student_book_order_tm sbotm, sync_selected_course_temp sct " +
            "where sbo.student_code = sct.student_code and sbo.order_code = sbotm.order_code and sbo.state < 4) t " +
            "where not EXISTS(select * from sync_selected_course_temp sct where t.student_code = sct.student_code and t.course_code = sct.course_code)")
    public List<Object[]> findDelChangeSelectCourse()throws Exception;

    @Modifying
    @Query(nativeQuery = true, value = "delete sbotm from student_book_order_tm sbotm, student_book_order sbo where sbo.order_code and sbotm.order_code and sbo.student_code = ?1 and sbotm.course_code = ?2")
    public void delByStudentCodeAndCourseCode(String studentCode, String courseCode);
}
