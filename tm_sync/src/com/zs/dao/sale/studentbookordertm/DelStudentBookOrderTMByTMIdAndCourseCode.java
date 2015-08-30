package com.zs.dao.sale.studentbookordertm;

import com.feinno.framework.common.dao.jpa.EntityJpaDao;
import com.zs.domain.sale.StudentBookOrderTM;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

/**
 * Created by Allen on 2015/7/17.
 */
public interface DelStudentBookOrderTMByTMIdAndCourseCode extends EntityJpaDao<StudentBookOrderTM, Long> {
    @Modifying
    @Query(nativeQuery = true, value = "DELETE student_book_order_tm FROM student_book_order_tm, student_book_order WHERE student_book_order.state < ?1 AND student_book_order.order_code = student_book_order_tm.order_code AND student_book_order_tm.teach_material_id = ?2 AND student_book_order_tm.course_code = ?3")
    public void delStudentBookOrderTMByTMIdAndCourseCode(int state, long tmId, String courseCode);
}
