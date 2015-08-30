package com.zs.dao.sale.studentbookorder.impl;

import com.zs.dao.BaseQueryDao;
import com.zs.dao.FindListByWhereDAO;
import com.zs.domain.sale.StudentBookOrder;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * 打印学生中心发书单
 * Created by Allen on 2015/7/18.
 */
@Service("findStudentBookOrderForSpotPrintDAO")
public class FindStudentBookOrderForSpotPrintDAOImpl extends BaseQueryDao
        implements FindListByWhereDAO {
    @Override
    public List findListByWhere(Map<String, String> paramsMap, Map<String, Boolean> sortMap) {

        StringBuilder sql = new StringBuilder();

        sql.append("SELECT CASE WHEN count(t2.deptName) = 1 THEN t2.deptName ELSE '公共' END AS deptName,t.* ");
        sql.append("FROM (SELECT sp.code as spotCode, sp.name as spotName, tm. NAME AS tmName, tm.author, tm.price, sbotm.course_code, sum(sbotm.count) AS count, sum(sbotm.count * sbotm.price) AS totalPrice ");
        sql.append("FROM sync_spot sp, sync_student s, student_book_order sbo, student_book_order_tm sbotm, teach_material tm ");
        sql.append("WHERE sp.code = s.spot_code AND s.code = sbo.student_code AND sbo.order_code = sbotm.order_code AND tm.id = sbotm.teach_material_id AND tm.state = 0 AND sbo.is_stock = 0 ");

        String semesterId = paramsMap.get("semesterId");
        String spotCode = paramsMap.get("spotCode");
        String state = paramsMap.get("state");
        String operateTime = paramsMap.get("operateTime");

        List<Object> params = new ArrayList<Object>();
        if(!StringUtils.isEmpty(semesterId)){
            sql.append("AND sbo.semester_id = ? ");
            params.add(Long.parseLong(semesterId));
        }
        if(!StringUtils.isEmpty(state)){
            sql.append("AND sbo.state = ? ");
            params.add(Integer.parseInt(state));
        }
        if(!StringUtils.isEmpty(spotCode)){
            sql.append("AND s.spot_code = ? ");
            params.add(spotCode);
        }
        if(state.equals(StudentBookOrder.STATE_SORTING+"")){
            sql.append("AND sbo.operate_time = ? ");
            params.add(operateTime);
        }
        sql.append("GROUP BY sp.code, sp.name, tm.name, tm.author, tm.price, sbotm.course_code) t ");
        sql.append("LEFT JOIN (SELECT dept.name as deptName, bs.course_code ");
        sql.append("FROM semester s, sync_begin_schedule bs, sync_spec spec, sync_department dept ");
        sql.append("WHERE s.year = bs.academic_year AND s.quarter = bs.term AND bs.spec_code = spec.code AND spec.dept_code = dept.code ");

        if(!StringUtils.isEmpty(semesterId)){
            sql.append("AND s.id = ? ");
            params.add(Long.parseLong(semesterId));
        }

        sql.append("GROUP BY dept.name, bs.course_code ) t2 ON t.course_code = t2.course_code WHERE t.count > 0 ");
        sql.append("GROUP BY course_code, tmName, author, price, count, totalPrice ");


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
