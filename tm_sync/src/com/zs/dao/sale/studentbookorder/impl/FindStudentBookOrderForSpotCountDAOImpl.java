package com.zs.dao.sale.studentbookorder.impl;

import com.feinno.framework.common.dao.support.PageInfo;
import com.zs.dao.BaseQueryDao;
import com.zs.dao.FindPageByWhereDAO;
import com.zs.domain.sale.StudentBookOrder;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * 统计学习中心的教材总数和总价;如果查询分拣中的状态，那么就要查出分拣时间
 * Created by Allen on 2015/7/18.
 */
@Service("findStudentBookOrderForSpotCountDAO")
public class FindStudentBookOrderForSpotCountDAOImpl extends BaseQueryDao
        implements FindPageByWhereDAO {
    @Override
    public PageInfo findPageByWhere(PageInfo pageInfo, Map<String, String> paramsMap, Map<String, Boolean> sortMap) {
        PageInfo studentOrderPageInfo = new PageInfo();
        studentOrderPageInfo.setCurrentPage(pageInfo.getCurrentPage());
        studentOrderPageInfo.setCountOfCurrentPage(pageInfo.getCountOfCurrentPage());

        List<Object> param = new ArrayList<Object>();
        String semesterId = paramsMap.get("semesterId");
        String state = paramsMap.get("state");
        String spotCode = paramsMap.get("spotCode");

        String field = "t.code,t.name,count(t.order_code),sum(t.tmCount),sum(t.totalPrice)";
        if(state.equals(StudentBookOrder.STATE_SORTING+"")){
            field += ",t.operate_time,t.operator";
        }

        StringBuilder sql = new StringBuilder("from (select sp.code, sp.name, sbo.order_code, sum(sbotm.count) tmCount, sum(sbotm.count * sbotm.price) totalPrice ");
        if(state.equals(StudentBookOrder.STATE_SORTING+"")){
            sql.append(", sbo.operate_time, sbo.operator ");
        }
        sql.append("from sync_spot sp, sync_student s, student_book_order sbo, student_book_order_tm sbotm, teach_material tm ");
        sql.append("where sp.`code` = s.spot_code AND s.`code` = sbo.student_code AND sbo.order_code = sbotm.order_code AND sbotm.teach_material_id = tm.id AND tm.state = 0 AND sbo.is_stock = 0 AND sbotm.count > 0 ");
        if(!StringUtils.isEmpty(semesterId)){
            sql.append("AND sbo.semester_id = ? ");
            param.add(Long.parseLong(semesterId));
        }
        if(!StringUtils.isEmpty(state)){
            sql.append("AND sbo.state = ? ");
            param.add(Integer.parseInt(state));
        }
        if(!StringUtils.isEmpty(spotCode)){
            sql.append("AND sp.code = ? ");
            param.add(spotCode);
        }
        sql.append("GROUP BY sp.code, sp.name, sbo.order_code ");
        if(state.equals(StudentBookOrder.STATE_SORTING+"")){
            sql.append(", sbo.operate_time, sbo.operator ");
        }
        if(null != sortMap) {
            sql.append(" ORDER BY ");
            int i = 0;
            for (Iterator it = sortMap.keySet().iterator(); it.hasNext(); ) {
                if(0 < i){
                    sql.append(",");
                }
                String key = it.next().toString();
                sql.append(key);
                sql.append(" ");
                sql.append(sortMap.get(key) ? "ASC" : "DESC");
                i++;
            }
        }
        sql.append(" ) t WHERE t.tmCount > 0 GROUP BY t.code, t.name ");
        if(state.equals(StudentBookOrder.STATE_SORTING+"")){
            sql.append(", t.operate_time, t.operator ");
        }
        studentOrderPageInfo = super.pageSqlQueryByNativeSql(studentOrderPageInfo, sql.toString(), field, param.toArray());
        return studentOrderPageInfo;
    }
}
