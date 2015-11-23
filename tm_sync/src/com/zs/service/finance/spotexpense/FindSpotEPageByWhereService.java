package com.zs.service.finance.spotexpense;

import com.feinno.framework.common.dao.support.PageInfo;
import com.feinno.framework.common.service.EntityService;
import com.zs.domain.finance.SpotExpense;

import java.util.Map;

/**
 * 分页查询学习中心费用的接口
 * Created by LihongZhang on 2015/5/17.
 */
public interface FindSpotEPageByWhereService extends EntityService {
    public PageInfo<SpotExpense> findPageByWhere(PageInfo pageInfo, Map<String, String> map, Map<String, Boolean> sortMap)throws Exception;

}
