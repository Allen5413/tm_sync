package com.zs.service.finance.spotexpense.impl;

import com.feinno.framework.common.exception.BusinessException;
import com.feinno.framework.common.service.EntityServiceImpl;
import com.zs.dao.basic.semester.FindNowSemesterDAO;
import com.zs.dao.finance.spotexpense.FindSpotRecordBySpotCodeDao;
import com.zs.dao.finance.spotexpense.SpotExpenseDao;
import com.zs.dao.sync.spot.FindSpotByCodeDAO;
import com.zs.domain.basic.Semester;
import com.zs.domain.finance.SpotExpense;
import com.zs.service.finance.spotexpense.AddSpotEService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 添加学习中心费用记录的接口的实现类
 * Created by LihongZhang on 2015/5/17.
 */
@Service("addSpotEService")
public class AddSpotEServiceImpl extends EntityServiceImpl<SpotExpense,SpotExpenseDao> implements AddSpotEService{

    @Resource
    private FindSpotByCodeDAO findSpotByCodeDAO;
    @Resource
    private FindSpotRecordBySpotCodeDao findSpotRecordBySpotCodeDao;
    @Resource
    private FindNowSemesterDAO findNowSemesterDAO;

    @Override
    public void addSpotE(SpotExpense spotExpense, String userName) throws Exception {
        if (null != spotExpense){
            //查询当前学期
            Semester semester = findNowSemesterDAO.getNowSemester();

            //验证学习中心编号是否存在
            if (null == findSpotByCodeDAO.getSpotByCode(spotExpense.getSpotCode())){
                throw new BusinessException("学习中心编号不存在");
            }
            //验证是否记录是否已存在
            if (null != findSpotRecordBySpotCodeDao.getSpotEBySpotCode(spotExpense.getSpotCode(), semester.getId())){
                throw new BusinessException("该学习中心费用记录已存在");
            }
            //执行添加，刚添加的费用信息已支付金额为0
            spotExpense.setPay(Float.parseFloat("0"));
            //添加创建人和操作人
            spotExpense.setCreator(userName);
            spotExpense.setOperator(userName);
            //执行添加
            findSpotRecordBySpotCodeDao.save(spotExpense);
        }
    }
}
