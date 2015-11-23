package com.zs.dao.sync.spot.impl;

import com.feinno.framework.common.dao.support.PageInfo;
import com.zs.dao.BaseQueryDao;
import com.zs.dao.FindPageByWhereDAO;
import com.zs.domain.sync.Spot;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by Allen on 2015/5/22.
 */
@Service("findSpotPageByWhereDAO")
public class FindSpotPageByWhereDAOImpl extends BaseQueryDao implements FindPageByWhereDAO {

    @Override
    public PageInfo findPageByWhere(PageInfo pageInfo, Map<String, String> paramsMap, Map<String, Boolean> sortMap) {
        PageInfo<Spot> spotPageInfo = new PageInfo<Spot>();
        spotPageInfo.setCurrentPage(pageInfo.getCurrentPage());
        spotPageInfo.setCountOfCurrentPage(pageInfo.getCountOfCurrentPage());

        StringBuilder sql = new StringBuilder("FROM Spot where 1=1 ");
        String code = paramsMap.get("code");
        String name = paramsMap.get("name");

        List<Object> param = new ArrayList<Object>();
        if(!StringUtils.isEmpty(code)){
            sql.append(" and code = ? ");
            param.add(code);
        }
        if(!StringUtils.isEmpty(name)){
            sql.append(" and name like ? ");
            param.add("%"+name+"%");
        }
        sql.append(" order by ");
        for (Iterator it = sortMap.keySet().iterator(); it.hasNext();){
            String key = it.next().toString();
            sql.append(key);
            sql.append(" ");
            sql.append(sortMap.get(key) ? "asc" : "desc");
        }
        spotPageInfo = super.pagedQueryByJpql(spotPageInfo, sql.toString(), param.toArray());
        return spotPageInfo;
    }
}

