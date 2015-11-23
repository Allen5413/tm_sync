package com.zs.service.finance.spotexpensebuy;

import com.feinno.framework.common.dao.support.PageInfo;
import com.feinno.framework.common.service.EntityService;
import com.zs.domain.finance.SpotExpenseBuy;

import java.util.Map;

/**
 * 根据学习中心编号分页查询学习中心消费的明细信息
 * Created by LihongZhang on 2015/5/17.
 */
public interface FindSpotEBPageBySpotCodeService extends EntityService<SpotExpenseBuy>{

    /**
     * 根据学习中心分页查询消费记录的方法
     * @param pageInfo
     * @param map
     * @param sortMap
     * @return
     * @throws Exception
     */
    public PageInfo<SpotExpenseBuy> findPageBySpotCode(PageInfo<SpotExpenseBuy> pageInfo, Map<String, String> map, Map<String, Boolean> sortMap) throws Exception;
}
