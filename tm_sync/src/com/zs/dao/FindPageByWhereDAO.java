package com.zs.dao;

import com.feinno.framework.common.dao.support.PageInfo;

import java.util.Map;

/**
 * 分页查询
 * Created by Allen on 2015/4/27.
 */
public interface FindPageByWhereDAO {

    public PageInfo findPageByWhere(PageInfo pageInfo, Map<String, String> paramsMap, Map<String, Boolean> sortMap);
}
