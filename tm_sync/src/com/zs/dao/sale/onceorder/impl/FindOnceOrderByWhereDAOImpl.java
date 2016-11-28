package com.zs.dao.sale.onceorder.impl;

import com.zs.dao.BaseQueryDao;
import com.zs.dao.FindListByWhereDAO;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 根据一些条件来查询一次性订单
 * Created by Allen on 2015/9/4.
 */
@Service("findOnceOrderByWhereDAO")
public class FindOnceOrderByWhereDAOImpl extends BaseQueryDao
        implements FindListByWhereDAO {
    @Override
    public List<Object[]> findListByWhere(Map<String, String> paramsMap, Map<String, Boolean> sortMap) {

        List<Object> param = new ArrayList<Object>();
        String spotCode = paramsMap.get("spotCode");
        String year = paramsMap.get("year");
        String quarter = paramsMap.get("quarter");
        String specCode = paramsMap.get("specCode");
        String levelCode = paramsMap.get("levelCode");
        String studentCodes = paramsMap.get("studentCodes");

        StringBuilder sql = new StringBuilder();
        sql.append("select sbo.id, sbo.student_code, sbo.state from sync_student s, student_book_once_order sbo " +
                "where s.code = sbo.student_code and sbo.state < 2 ");

        if(!StringUtils.isEmpty(spotCode)){
            sql.append("and s.spot_code = ? ");
            param.add(spotCode);
        }
        if(!StringUtils.isEmpty(year)){
            sql.append("and s.study_enter_year = ? ");
            param.add(Integer.parseInt(year));
        }
        if(!StringUtils.isEmpty(quarter)){
            sql.append("and s.study_quarter = ? ");
            param.add(Integer.parseInt(quarter));
        }
        if(!StringUtils.isEmpty(specCode)){
            sql.append("and s.spec_code = ? ");
            param.add(specCode);
        }
        if(!StringUtils.isEmpty(levelCode)){
            sql.append("and s.level_code = ? ");
            param.add(levelCode);
        }
        if(!StringUtils.isEmpty(studentCodes)){
            String[] studentCodeArray = studentCodes.split(",");
            sql.append("and s.code in (");
            for(int i=0; i < studentCodeArray.length; i++){
                if(i == studentCodeArray.length-1){
                    sql.append("?)");
                }else{
                    sql.append("?,");
                }
                param.add(studentCodeArray[i]);
            }
        }
        List<Object[]> list = super.sqlQueryByNativeSql(sql.toString(), param.toArray());
        return list;
    }
}
