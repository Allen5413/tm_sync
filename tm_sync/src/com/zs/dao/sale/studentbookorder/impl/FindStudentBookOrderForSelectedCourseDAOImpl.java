package com.zs.dao.sale.studentbookorder.impl;

import com.zs.dao.BaseQueryDao;
import com.zs.dao.FindListByWhereDAO;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * 查询学生的选课没有订单明细的，用于更改了教材与课程关联；需要把教材添加到已发送状态前的学生订单里面。
 * Created by Allen on 2015/7/20.
 */
@Service("findStudentBookOrderForSelectedCourseDAO")
public class FindStudentBookOrderForSelectedCourseDAOImpl extends BaseQueryDao
        implements FindListByWhereDAO {
    @Override
    public List findListByWhere(Map<String, String> paramsMap, Map<String, Boolean> sortMap) {

        StringBuilder sql = new StringBuilder();
        List<Object> params = new ArrayList<Object>();

        String tmId = paramsMap.get("tmId");
        String state = paramsMap.get("state");
        String courseCode = paramsMap.get("courseCode");

        sql.append("SELECT ir.issue_channel_id, sc.student_code, sc.course_code, sbo.order_code ");
        sql.append("FROM sync_selected_course sc ");
        sql.append("INNER JOIN sync_student s ON sc.student_code = s.code ");
        sql.append("INNER JOIN issue_range ir ON s.spot_code = ir.spot_code ");
        sql.append("LEFT JOIN student_book_order sbo ON sc.student_code = sbo.student_code ");
        if(!StringUtils.isEmpty(state)){
            sql.append("AND sbo.state < ? ");
            params.add(Integer.parseInt(state));
        }
        if(!StringUtils.isEmpty(courseCode)){
            sql.append("WHERE sc.course_code = ? ");
            params.add(courseCode);
        }
        sql.append("AND NOT EXISTS (");
        sql.append("SELECT order_code ");
        sql.append("FROM student_book_order_tm sbotm ");
        sql.append("WHERE sbotm.order_code = sbo.order_code ");
        if(!StringUtils.isEmpty(tmId)){
            sql.append("AND sbotm.teach_material_id = ? ");
            params.add(Long.parseLong(tmId));
        }
        sql.append(")");
        if(null != sortMap) {
            sql.append("order by ");
            int i = 0;
            for (Iterator it = sortMap.keySet().iterator(); it.hasNext(); ) {
                if(0 < i){
                    sql.append(",");
                }
                String key = it.next().toString();
                sql.append(key);
                sql.append(" ");
                sql.append(sortMap.get(key) ? "asc" : "desc");
                i++;
            }
        }
        List<Object[]> list = super.sqlQueryByNativeSql(sql.toString(), params.toArray());
        return list;
    }
}
