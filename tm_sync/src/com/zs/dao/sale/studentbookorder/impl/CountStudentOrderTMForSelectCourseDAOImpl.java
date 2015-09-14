package com.zs.dao.sale.studentbookorder.impl;

import com.zs.dao.BaseQueryDao;
import com.zs.dao.FindListByWhereDAO;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 通过学生选课，查询学生需要购买的教材，用户生成学生订单
 * Created by Allen on 2015/7/10.
 */
@Service("countStudentOrderTMForSelectCourse")
public class CountStudentOrderTMForSelectCourseDAOImpl extends BaseQueryDao
        implements FindListByWhereDAO {
    @Override
    public List<Object[]> findListByWhere(Map<String, String> paramsMap, Map<String, Boolean> sortMap) {

        long semesterId = Long.parseLong(paramsMap.get("semesterId"));

        StringBuilder sql = new StringBuilder("select * from (");
        //统计教材的
        sql.append("SELECT ir.issue_channel_id, s.code, sc.course_code, tm.id, tm.price ");
        sql.append("FROM sync_selected_course sc, sync_student s, teach_material_course tmc, teach_material tm, issue_range ir ");
        sql.append("WHERE sc.student_code = s.code and tmc.course_code = sc.course_code and tmc.teach_material_id = tm.id and s.spot_code = ir.spot_code AND tm.state = 0 AND sc.semester_id = ? and s.operate_time > '2015-09-11' ");
        //sql合并
        sql.append("UNION ALL ");
        //统计套教材的
        sql.append("SELECT ir.issue_channel_id, s.`code`, sc.course_code, tm.id, tm.price ");
        sql.append("FROM sync_selected_course sc, sync_student s, set_teach_material stm, set_teach_material_tm stmtm, teach_material tm, issue_range ir ");
        sql.append("WHERE sc.student_code = s.code and stm.buy_course_code = sc.course_code and stmtm.set_teach_material_id = stm.id and stmtm.teach_material_id = tm.id and s.spot_code = ir.spot_code AND tm.state = 0 AND sc.semester_id = ? and s.operate_time > '2015-09-11' ");

        sql.append(") t ORDER BY t.issue_channel_id, code");

        Object[] param = {semesterId, semesterId};

        List<Object[]> list = super.sqlQueryByNativeSql(sql.toString(), param);
        return list;
    }
}
