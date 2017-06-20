package com.zs.dao.sync;

import com.feinno.framework.common.dao.jpa.EntityJpaDao;
import com.zs.domain.sync.OldSelectedCourseTemp;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by Allen on 2016/10/27.
 */
public interface OldSelectedCourseTempDAO  extends EntityJpaDao<OldSelectedCourseTemp,Long> {

    /**
     * 查询以前没有选过课的学生剩余选课
     * @return
     */
//    @Query(nativeQuery = true, value = "SELECT osc.student_code, s.spot_code, osc.course_code, s.is_send_student " +
//            "FROM sync_old_selected_course_temp osc, sync_student s " +
//            "where not EXISTS(select * from sync_selected_course sc where osc.student_code = sc.student_code and osc.course_code = sc.course_code) " +
//            "and osc.student_code = s.code " +
//            "order by osc.student_code, osc.course_code")
    @Query(nativeQuery = true, value = "select osc.student_code, s.spot_code, osc.course_code " +
            "from sync_old_selected_course_temp osc, sync_student s " +
            "where osc.student_code = s.code and (osc.score = 0 or osc.score = '' or osc.score is null) " +
            "and not EXISTS(select * from ( " +
            "select sbo.student_code, sbotm.course_code from student_book_order sbo, student_book_order_tm sbotm where sbo.order_code = sbotm.order_code and sbo.state > 3 and sbotm.count > 0 " +
            "union all " +
            "select sbo.student_code, sbotm.course_code from student_book_once_order sbo, student_book_once_order_tm sbotm where sbo.id = sbotm.order_id and sbo.state > 5 and sbotm.count > 0 " +
            ") t where osc.student_code = t.student_code and osc.course_code = t.course_code) " +
            "order by osc.student_code, osc.course_code")
    public List<Object[]> find();
}
