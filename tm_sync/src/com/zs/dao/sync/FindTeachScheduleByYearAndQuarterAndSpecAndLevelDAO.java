package com.zs.dao.sync;

import com.feinno.framework.common.dao.jpa.EntityJpaDao;
import com.zs.domain.sync.TeachSchedule;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * 查询一个入学年、入学季、专业、层次，到毕业前课程（包括4个学期的开课）
 * Created by Allen on 2016/6/16.
 */
public interface FindTeachScheduleByYearAndQuarterAndSpecAndLevelDAO extends EntityJpaDao<TeachSchedule, Long> {
    @Query(nativeQuery = true, value = "select t.course_code, t.course_type, t2.course_code code, t.xf from (" +
            "SELECT DISTINCT ts.course_code, ts.course_type, ts.xf FROM sync_teach_schedule ts " +
            "where ts.enter_year = ?1 and ts.quarter = ?2 " +
            "and ts.academic_year*10+ts.term <= (ts.enter_year+ts.quarter)*10+ (3 - ts.quarter) " +
            "and ts.spec_code = ?3 and ts.level_code = ?4" +
            ") t LEFT JOIN " +
            "(select DISTINCT sbotm.course_code from student_book_order sbo, student_book_order_tm sbotm " +
            "where sbo.order_code = sbotm.order_code and sbo.student_code = ?5 and sbotm.course_code is not null and sbo.state > 0 and sbotm.count > 0 " +
            ") t2 on t.course_code = t2.course_code")
    public List<Object[]> find(int year, int quarter, String specCode, String levelCode, String studentCode);
}
