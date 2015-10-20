package com.zs.dao.sync;

import com.feinno.framework.common.dao.jpa.EntityJpaDao;
import com.zs.domain.sync.Student;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by Allen on 2015/10/20.
 */
public interface CreateStudentOrderFor15DAO extends EntityJpaDao<Student, Long> {

    @Query(nativeQuery = true, value = "select so.student_code, so.course_code, so.name, so.author, so.price from student_order15 so order by so.student_code, so.course_code")
    public List<Object[]> find();
}
