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
            "where sbo.student_code = sct.student_code and sbo.order_code = sbotm.order_code and sbo.state < 2) t " +
            "where not EXISTS(select * from sync_selected_course_temp sct where t.student_code = sct.student_code and t.course_code = sct.course_code)")
    public List<Object[]> findDelChangeSelectCourse()throws Exception;

    @Modifying
    @Query(nativeQuery = true, value = "delete sbotm from student_book_order_tm sbotm, student_book_order sbo where sbo.order_code and sbotm.order_code and sbo.student_code = ?1 and sbotm.course_code = ?2")
    public void delByStudentCodeAndCourseCode(String studentCode, String courseCode);

    /**
     * 删除掉函授学生的0481课程对应教材和0483课程对应的id：1393教材
     * @param semesterId
     */
    @Modifying
    @Query(nativeQuery = true, value = "delete sbotm.* from student_book_order sbo, student_book_order_tm sbotm " +
            "where sbo.order_code = sbotm.order_code and (SUBSTR(sbo.student_code,1,1) = 'H' or SUBSTR(sbo.student_code,1 ,1) = 'h') " +
            "and (sbotm.course_code = '0481' or (sbotm.course_code = '0483' and sbotm.teach_material_id = 1393)) and sbo.semester_id = ?1")
    public void delHSTmBySermesterId(long semesterId);

    @Modifying
    @Query(nativeQuery = true, value = "delete sbotm.* from student_book_order sbo, student_book_order_tm sbotm " +
            "where sbo.order_code = sbotm.order_code and sbo.state < 4 and substr(sbo.order_code, 1, 3) = 'FSA' and sbo.student_code = ?1 and sbotm.course_code = ?2")
    public void delByStudentCodeAndCourseCodeForNotSend(String studentCode, String courseCode)throws Exception;
}
