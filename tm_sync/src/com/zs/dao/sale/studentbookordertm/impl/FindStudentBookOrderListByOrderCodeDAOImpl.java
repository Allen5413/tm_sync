package com.zs.dao.sale.studentbookordertm.impl;

import com.zs.dao.BaseQueryDao;
import com.zs.dao.FindListByWhereDAO;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by Allen on 2015/5/26.
 */
@Service("findStudentBookOrderListByOrderCodeDAO")
public class FindStudentBookOrderListByOrderCodeDAOImpl extends BaseQueryDao
        implements FindListByWhereDAO {
    @Override
    public List findListByWhere(Map<String, String> paramsMap, Map<String, Boolean> sortMap) {

        StringBuilder sql = new StringBuilder();

        sql.append("select sbotm.id, sc.code as courseCode, sc.name as courseName, tm.name, sbotm.price, sbotm.count, sbotm.operator, sbotm.operate_time, sbo.package_id from ");
        sql.append("student_book_order sbo, student_book_order_tm sbotm, teach_material tm, sync_course sc where ");
        sql.append("sbo.order_code = sbotm.order_code and sbotm.teach_material_id = tm.id and sbotm.course_code = sc.code and sbo.order_code = ? ");

        List<Object> params = new ArrayList<Object>();
        String orderCode = paramsMap.get("orderCode");
        params.add(orderCode);
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
