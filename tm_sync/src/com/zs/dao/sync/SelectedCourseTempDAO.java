package com.zs.dao.sync;

import com.feinno.framework.common.dao.jpa.EntityJpaDao;
import com.zs.domain.sync.SelectedCourseTemp;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by Allen on 2015/9/17.
 */
public interface SelectedCourseTempDAO extends EntityJpaDao<SelectedCourseTemp,Long> {
    /**
     * 通过学号查询学生的所有选课信息
     * @param studentCode
     * @return
     * @throws Exception
     */
    @Query("FROM SelectedCourseTemp WHERE studentCode = ?1")
    public List<SelectedCourseTemp> findByStudentCode(String studentCode)throws Exception;


    /**
     * 查询新增的选课信息，只针对在籍的学生
     * @return
     * @throws Exception
     */
    @Query(nativeQuery = true, value = "select sct.* from sync_selected_course_temp sct " +
            "where NOT EXISTS (select id from sync_selected_course sc where sct.student_code = sc.student_code and sct.course_code = sc.course_code) " +
            "AND EXISTS (select id from sync_student s where sct.student_code = s.code  and s.state = 0) " +
            "order by student_code, course_code")
    public List<SelectedCourseTemp> findNewSelectedCourse()throws Exception;
}
