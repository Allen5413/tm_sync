package com.zs.service.finance.spotexpensebuy;

import com.feinno.framework.common.service.EntityService;
import com.zs.domain.finance.SpotExpenseBuy;

/**
 * 添加学习中心消费记录
 * Created by LihongZhang on 2015/5/17.
 */
public interface AddSpotExpenseBuyService extends EntityService<SpotExpenseBuy> {

    /**
     * 添加消费记录的方法
     * @param spotExpenseBuy
     * @throws Exception
     */
    public void addSpotExpenseBuy(SpotExpenseBuy spotExpenseBuy, String userName) throws Exception;
}
