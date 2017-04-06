package com.zs.dao.sync;

import com.feinno.framework.common.dao.jpa.EntityJpaDao;
import com.zs.domain.sync.SelectedCourse;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by Allen on 2015/5/8.
 */
public interface SelectedCourseDAO extends EntityJpaDao<SelectedCourse, Long> {

    /**
     * 通过学号查询学生的所有选课信息
     * @param studentCode
     * @return
     * @throws Exception
     */
    @Query("FROM SelectedCourse WHERE studentCode = ?1")
    public List<SelectedCourse> findByStudentCode(String studentCode)throws Exception;


    /**
     * 查询变更的选课， 把之前选了，后面没选的课程删掉
     * @return
     * @throws Exception
     */
    @Query(nativeQuery = true, value = "select t.* from " +
            "(select DISTINCT sc.* from sync_selected_course sc, sync_selected_course_temp sct where sc.student_code = sct.student_code) t " +
            "where not EXISTS(select * from sync_selected_course_temp sct where t.student_code = sct.student_code and t.course_code = sct.course_code)")
    public List<SelectedCourse> findDelSelectedCourse()throws Exception;

    @Modifying
    @Query(nativeQuery = true, value = "delete from sync_selected_course where student_code = ?1 and course_code = ?2")
    public void delByStudentCodeAndCourseCode(String studentCode, String courseCode);
}
