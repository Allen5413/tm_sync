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
            "order by osc.student_code, osc.course_code")
    public List<Object[]> find();
}
