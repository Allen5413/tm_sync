package com.zs.service.finance.spotexpensebuy.impl;

import com.feinno.framework.common.dao.support.PageInfo;
import com.feinno.framework.common.service.EntityServiceImpl;
import com.zs.dao.FindPageByWhereDAO;
import com.zs.dao.finance.spotexpensebuy.SpotExpenseBuyDao;
import com.zs.domain.finance.SpotExpenseBuy;
import com.zs.service.finance.spotexpensebuy.FindSpotEBPageBySpotCodeService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;

/**
 * 实现分页查询的接口
 * Created by LihongZhang on 2015/5/17.
 */
@Service("findSpotEBPageByWhereService")
public class FindSpotEBPageBySpotCodeServiceImpl extends EntityServiceImpl<SpotExpenseBuy,SpotExpenseBuyDao> implements FindSpotEBPageBySpotCodeService {

    @Resource
    private FindPageByWhereDAO findSpotEBPageByWhereDao;

    @Override
    public PageInfo<SpotExpenseBuy> findPageBySpotCode(PageInfo<SpotExpenseBuy> pageInfo, Map<String,String> map,Map<String,Boolean> sortMap) throws Exception {
        return findSpotEBPageByWhereDao.findPageByWhere(pageInfo,map,sortMap);
    }
}
