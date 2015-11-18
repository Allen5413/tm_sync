package com.zs.dao.sale.studentbookorder;

import com.feinno.framework.common.dao.jpa.EntityJpaDao;
import com.zs.domain.sale.StudentBookOrder;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by Allen on 2015/10/26.
 */
public interface TempDAO extends EntityJpaDao<StudentBookOrder, Long> {
    @Query(nativeQuery = true, value = "select sbo.student_code, sc.course_code from student_book_order sbo, " +
            "(select sbo.order_code from student_book_order sbo, student_book_order_tm sbotm " +
            "where sbo.order_code = sbotm.order_code and sbo.semester_id = 2 " +
            "group by order_code HAVING (count(*) = 1)) t, " +
            "sync_selected_course sc " +
            "where sbo.order_code = t.order_code and sbo.student_code = sc.student_code " +
            "order by sbo.student_code, sc.course_code")
    public List<Object[]> find();

    @Query(nativeQuery = true, value = "select sbotm.course_code from student_book_order_tm sbotm, (" +
            "select sbo.order_code from student_book_order sbo, student_book_order_tm sbotm " +
            "where sbo.order_code = sbotm.order_code and sbo.semester_id = 2 and sbo.student_code = ?1 " +
            "group by order_code HAVING (count(*) = 1)) t " +
            "where sbotm.order_code = t.order_code")
    public List<Object> findExistsCourse(String code);

    @Query(nativeQuery = true, value = "select sbo.student_code from student_book_order sbo where sbo.semester_id = ?1 and sbo.state = 0 GROUP BY student_code having(count(*)>1)")
    public List<String> findStudentByMoreOrder(long semesterId);
}
