package com.zs.dao;

import java.util.List;
import java.util.Map;

/**
 * 动态SQL，集合查询
 * Created by Allen on 2015/5/7.
 */
public interface FindListByWhereDAO {
    public List findListByWhere(Map<String, String> paramsMap, Map<String, Boolean> sortMap);
}
