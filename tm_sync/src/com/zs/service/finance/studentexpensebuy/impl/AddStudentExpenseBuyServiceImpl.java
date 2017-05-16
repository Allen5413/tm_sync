package com.zs.service.finance.studentexpensebuy.impl;

import com.feinno.framework.common.exception.BusinessException;
import com.feinno.framework.common.service.EntityServiceImpl;
import com.zs.dao.finance.studentexpensebuy.StudentExpenseBuyDao;
import com.zs.dao.sync.FindStudentByCodeDAO;
import com.zs.domain.finance.StudentExpenseBuy;
import com.zs.service.finance.studentexpense.AddStuEBuyService;
import com.zs.service.finance.studentexpensebuy.AddStudentExpenseBuyService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * Created by Allen on 2017/5/16.
 */
@Service("addStudentExpenseBuyService")
public class AddStudentExpenseBuyServiceImpl extends EntityServiceImpl<StudentExpenseBuy,StudentExpenseBuyDao> implements AddStudentExpenseBuyService {


    @Resource
    private StudentExpenseBuyDao studentExpenseBuyDao;
    @Resource
    private FindStudentByCodeDAO findStudentByCodeDAO;
    @Resource
    private AddStuEBuyService addStuEBuyService;



    @Override
    @Transactional(value = "transactionManager", rollbackFor = Exception.class)
    public void addStudentExpenseBuy(StudentExpenseBuy studentExpenseBuy,String userName) throws Exception {
        if (null != studentExpenseBuy){
            //验证学生学号是否存在
            if (null == findStudentByCodeDAO.getStudentByCode(studentExpenseBuy.getStudentCode())){
                throw new BusinessException("学号："+studentExpenseBuy.getStudentCode()+", 学生不存在");
            }
            //添加创建人
            studentExpenseBuy.setCreator(userName);
            studentExpenseBuyDao.save(studentExpenseBuy);
            //记录学生费用信息
            addStuEBuyService.addStuEBuy(studentExpenseBuy, userName);
        }
    }
}
