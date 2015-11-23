package com.zs.service.finance.spotexpensebuy.impl;

import com.feinno.framework.common.exception.BusinessException;
import com.feinno.framework.common.service.EntityServiceImpl;
import com.zs.dao.finance.spotexpensebuy.SpotExpenseBuyDao;
import com.zs.dao.sync.spot.FindSpotByCodeDAO;
import com.zs.domain.finance.SpotExpenseBuy;
import com.zs.service.finance.spotexpense.AddSpotEBuyService;
import com.zs.service.finance.spotexpensebuy.AddSpotExpenseBuyService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * 实现添加学生消费记录的接口
 * Created by LihongZhang on 2015/5/17.
 */
@Service("addSpotExpenseBuyService")
public class AddSpotExpenseBuyServiceImpl extends EntityServiceImpl<SpotExpenseBuy,SpotExpenseBuyDao> implements AddSpotExpenseBuyService{

    @Resource
    private SpotExpenseBuyDao spotExpenseBuyDao;
    @Resource
    private FindSpotByCodeDAO findSpotByCodeDAO;
    @Resource
    private AddSpotEBuyService addSpotEBuyService;

    @Override
    @Transactional(value = "transactionManager", rollbackFor = Exception.class)
    public void addSpotExpenseBuy(SpotExpenseBuy spotExpenseBuy,String userName) throws Exception {
        //业务逻辑
        if (null != spotExpenseBuy){
            //验证学习中心编号是否存在
            if (null == findSpotByCodeDAO.getSpotByCode(spotExpenseBuy.getSpotCode())){
                throw new BusinessException("学习中心编号："+spotExpenseBuy.getSpotCode()+", 不存在");
            }
            //增加创建人
            spotExpenseBuy.setCreator(userName);
            //新增入账记录
            spotExpenseBuyDao.save(spotExpenseBuy);
            //修改学习中心费用的
            addSpotEBuyService.addSpotEBuy(spotExpenseBuy, userName);
        }
    }
}
