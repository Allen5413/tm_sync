package com.zs.dao.sale.studentbookorder;

import com.feinno.framework.common.dao.jpa.EntityJpaDao;
import com.zs.domain.sale.StudentBookOrder;
import org.springframework.data.jpa.repository.Query;

/**
 * Created by Allen on 2015/7/17.
 */
public interface FindStudentBookOrderBySelectCourseDAO extends EntityJpaDao<StudentBookOrder, Long> {
    @Query(nativeQuery = true, value = "SELECT sbo.* FROM student_book_order sbo, tsync_selected_course sc WHERE sc.student_code = sbo.student_code AND sbo.state < ?1 AND sc.course_code = ?2")
    public StudentBookOrder findStudentBookOrderBySelectCourse(int state, String courseCode);
}
