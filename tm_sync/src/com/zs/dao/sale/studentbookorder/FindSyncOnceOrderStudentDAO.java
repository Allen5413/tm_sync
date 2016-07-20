package com.zs.dao.sale.studentbookorder;

import com.feinno.framework.common.dao.jpa.EntityJpaDao;
import com.zs.domain.sale.StudentBookOnceOrder;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by Allen on 2016/7/19.
 */
public interface FindSyncOnceOrderStudentDAO extends EntityJpaDao<StudentBookOnceOrder,Long> {

    /**
     * 查询已经存在一次性订单的学生
     * @param semesterId
     * @return
     */
    @Query(nativeQuery = true, value = "SELECT DISTINCT sboo.id, sbo.student_code, s.study_enter_year, s.study_quarter, s.spec_code, s.level_code " +
            "FROM student_book_order sbo, sync_student s, student_book_once_order sboo where sbo.student_code = sboo.student_code and sboo.state = 0 " +
            "and sbo.semester_id = ?1 and sbo.student_code = s.code and s.is_once_order = 0")
    public List<Object[]> findExists(long semesterId);

    /**
     * 查询不存在的一次性订单的学生
     * @param semesterId
     * @return
     */
    @Query(nativeQuery = true, value = "SELECT DISTINCT sbo.student_code, s.spot_code, s.study_enter_year, s.study_quarter, s.spec_code, s.level_code " +
            "FROM student_book_order sbo, sync_student s " +
            "where NOT EXISTS(select * from student_book_once_order sboo where sbo.student_code = sboo.student_code) and sbo.semester_id = ?1 and sbo.student_code = s.code and s.is_once_order = 0 ")
    public List<Object[]> findNotExists(long semesterId);
}
