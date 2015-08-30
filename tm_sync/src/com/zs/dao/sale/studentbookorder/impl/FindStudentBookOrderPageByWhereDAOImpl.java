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
 * Created by Allen on 2015/5/26.
 */
@Service("findStudentBookOrderPageByWhereDAO")
public class FindStudentBookOrderPageByWhereDAOImpl extends BaseQueryDao
        implements FindPageByWhereDAO {
    @Override
    public PageInfo findPageByWhere(PageInfo pageInfo, Map<String, String> paramsMap, Map<String, Boolean> sortMap) {
        PageInfo studentOrderPageInfo = new PageInfo();
        studentOrderPageInfo.setCurrentPage(pageInfo.getCurrentPage());
        studentOrderPageInfo.setCountOfCurrentPage(pageInfo.getCountOfCurrentPage());

        List<Object> param = new ArrayList<Object>();
        String semesterId = paramsMap.get("semesterId");
        String provCode = paramsMap.get("provCode");
        String spotCode = paramsMap.get("spotCode");
        String specCode = paramsMap.get("specCode");
        String levelCode = paramsMap.get("levelCode");
        String studentCode = paramsMap.get("studentCode");
        String studentName = paramsMap.get("studentName");
        String orderCode = paramsMap.get("orderCode");
        String state = paramsMap.get("state");
        String isStock = paramsMap.get("isStock");
        String enterYear = paramsMap.get("enterYear");
        String quarter = paramsMap.get("quarter");
        String packageId = paramsMap.get("packageId");
        String tmCount = paramsMap.get("tmCount");

        param.add(Long.parseLong(semesterId));


        String field = "p.name as provName, spot.name as spotName, spec.name as specName, level.name as levelName, t.*";
        StringBuilder sql = new StringBuilder("from (select sbo.id, sbo.order_code, stu.code, stu.name, stu.spot_code, stu.spec_code, stu.level_code, ifnull(sum(sbotm.count),0) as tmCount, ifnull(sum(sbotm.count * sbotm.price),0) as totalPrice, sbo.operator, sbo.operate_time, sbo.state from student_book_order sbo, sync_student stu, student_book_order_tm sbotm where sbo.student_code = stu.code and sbo.order_code = sbotm.order_code and sbo.semester_id = ? ");
        if(!StringUtils.isEmpty(spotCode)){
            sql.append(" and stu.spot_code = ? ");
            param.add(spotCode);
        }
        if(!StringUtils.isEmpty(specCode)){
            sql.append(" and stu.spec_code = ? ");
            param.add(specCode);
        }
        if(!StringUtils.isEmpty(levelCode)){
            sql.append(" and stu.level_code = ? ");
            param.add(levelCode);
        }
        if(!StringUtils.isEmpty(studentCode)){
            sql.append(" and stu.code = ? ");
            param.add(studentCode);
        }
        if(!StringUtils.isEmpty(studentName)){
            sql.append(" and stu.name like ? ");
            param.add("%"+studentName+"%");
        }
        if(!StringUtils.isEmpty(enterYear)){
            sql.append(" and stu.study_enter_year = ? ");
            param.add(Integer.parseInt(enterYear));
        }
        if(!StringUtils.isEmpty(quarter)){
            sql.append(" and study_quarter = ? ");
            param.add(Integer.parseInt(quarter));
        }
        if(!StringUtils.isEmpty(orderCode)){
            sql.append(" and sbo.order_code = ? ");
            param.add(orderCode);
        }
        if(!StringUtils.isEmpty(state)){
            sql.append(" and sbo.state = ? ");
            param.add(Integer.parseInt(state));
        }
        if(!StringUtils.isEmpty(isStock)){
            sql.append(" and sbo.is_stock = ? ");
            param.add(Integer.parseInt(isStock));
        }
        if(!StringUtils.isEmpty(packageId)){
            sql.append(" and sbo.package_id = ? ");
            param.add(Long.parseLong(packageId));
        }
        sql.append("group by sbo.order_code order by sbo.operate_time desc) t ");
        sql.append("left join sync_spot spot on t.spot_code = spot.code left join sync_spot_province sp on t.spot_code = sp.spot_code left join sync_province p on sp.province_code = p.code left join sync_spec spec on t.spec_code = spec.code left join sync_level level on t.level_code = level.code where 1=1 ");
        if(!StringUtils.isEmpty(provCode)){
            sql.append(" and p.code = ? ");
            param.add(provCode);
        }
        if("0".equals(tmCount)){
            sql.append(" and t.tmCount > 0 ");
        }
        if(null != sortMap) {
            sql.append(" order by ");
            for (Iterator it = sortMap.keySet().iterator(); it.hasNext(); ) {
                String key = it.next().toString();
                sql.append(key);
                sql.append(" ");
                sql.append(sortMap.get(key) ? "asc" : "desc");
            }
        }
        studentOrderPageInfo = super.pageSqlQueryByNativeSql(studentOrderPageInfo, sql.toString(), field, param.toArray());
        return studentOrderPageInfo;
    }
}
