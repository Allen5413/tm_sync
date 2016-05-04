package com.zs.service.finance.studentexpensepay.impl;

import com.feinno.framework.common.exception.BusinessException;
import com.feinno.framework.common.service.EntityServiceImpl;
import com.zs.dao.basic.semester.FindNowSemesterDAO;
import com.zs.dao.finance.spotexpenseoth.SpotExpenseOthDAO;
import com.zs.dao.finance.studentexpense.CenterPayLogDAO;
import com.zs.dao.finance.studentexpense.StudentExpenseDao;
import com.zs.dao.finance.studentexpensepay.FindStudentPayBySpotCodeDAO;
import com.zs.dao.sync.FindStudentByCodeDAO;
import com.zs.domain.basic.Semester;
import com.zs.domain.finance.CenterPayLog;
import com.zs.domain.finance.SpotExpenseOth;
import com.zs.domain.finance.StudentExpense;
import com.zs.domain.finance.StudentExpensePay;
import com.zs.domain.sync.Student;
import com.zs.service.finance.studentexpensepay.AddStudentExpensePayService;
import com.zs.tools.DateTools;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 实现添加学生入账记录接口
 * Created by LihongZhang on 2015/5/15.
 */
@Service("addStudentExpensePayService")
public class AddStudentExpensePayServiceImpl extends EntityServiceImpl<StudentExpensePay,FindStudentPayBySpotCodeDAO> implements AddStudentExpensePayService{

    @Resource
    private FindStudentByCodeDAO findStudentByCodeDAO;
    @Resource
    private FindStudentPayBySpotCodeDAO findStudentPayBySpotCodeDAO;
    @Resource
	private StudentExpenseDao studentExpenseDao;
    @Resource
	private CenterPayLogDAO centerPayLogDAO;
    @Resource
	private SpotExpenseOthDAO spotExpenseOthDAO;
	@Resource
	private FindNowSemesterDAO findNowSemesterDAO;

    @Override
    @Transactional(value = "transactionManager", rollbackFor = Exception.class)
    public void addStudentExpensePay(StudentExpensePay studentExpensePay, String userName, String loginName) throws Exception {
        if (null !=studentExpensePay){
			Date operateTime = DateTools.getLongNowTime();
			//得到当前学期
			Semester semester = findNowSemesterDAO.getNowSemester();
			//验证支付金额是否为负
			if (0 >= studentExpensePay.getMoney()){
				throw new BusinessException("请输入正确的缴费金额");
			}
			//验证学生是否存在
			Student student = findStudentByCodeDAO.getStudentByCode(studentExpensePay.getStudentCode());
            if (null == student){
                throw new BusinessException("该学生不存在");
            }
            //写入创建人
            studentExpensePay.setCreator(userName);
            //写入入账记录
			findStudentPayBySpotCodeDAO.save(studentExpensePay);

            //更新学生消费数据
			List<StudentExpense> stuExpList = this.studentExpenseDao.queryStudentExpenseOnSemeter(studentExpensePay.getStudentCode());
            //不存在时，则为该学生添加记录
            if (null == stuExpList || stuExpList.size() == 0){
				StudentExpense studentExpense = new StudentExpense();
				studentExpense.setStudentCode(studentExpensePay.getStudentCode());
				studentExpense.setBuy(0f);
				studentExpense.setPay(studentExpensePay.getMoney());
				studentExpense.setCreator(userName);
				studentExpense.setOperator(userName);
				studentExpense.setSemesterId(semester.getId());
				studentExpense.setState(StudentExpense.STATE_YES);
				studentExpense.setClearTime(operateTime);
				this.studentExpenseDao.save(studentExpense);
				//更新中心交费
				this.updateSpotExpenseOth(student.getSpotCode(), studentExpense.getSemesterId(), studentExpensePay.getMoney(), userName);
            }else {
				//当前缴费剩的钱
				double tempMoney = studentExpensePay.getMoney();
				//每学期多的钱
				double moreMoney = 0;
				//是否当前学期
				boolean isNowSemester = false;
				for (StudentExpense studentExpense : stuExpList) {
					float pay = null == studentExpense.getPay() ? 0 : studentExpense.getPay();
					float buy = null == studentExpense.getBuy() ? 0 : studentExpense.getBuy();
					//是否当前学期
					if (semester.getId() == studentExpense.getSemesterId()) {
						isNowSemester = true;
						studentExpense.setPay(new BigDecimal(tempMoney).add(new BigDecimal(moreMoney)).add(new BigDecimal(pay)).setScale(2, BigDecimal.ROUND_HALF_UP).floatValue());
						if(studentExpense.getPay() >= studentExpense.getBuy()){
							studentExpense.setState(StudentExpense.STATE_YES);
							studentExpense.setClearTime(operateTime);
						}else{
							studentExpense.setState(StudentExpense.STATE_NO);
							studentExpense.setClearTime(null);
						}
						studentExpense.setOperator(userName);
						studentExpense.setOperateTime(operateTime);
						studentExpenseDao.update(studentExpense);
						//更新中心交费
						this.updateSpotExpenseOth2(student.getSpotCode(), studentExpense.getSemesterId(), moreMoney, tempMoney, pay, buy, userName);
					} else {
						double temp = new BigDecimal(buy).subtract(new BigDecimal(pay)).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
						if (buy > pay) {
							if (tempMoney >= temp) {
								studentExpense.setPay(new BigDecimal(studentExpense.getPay()).add(new BigDecimal(temp)).setScale(2, BigDecimal.ROUND_HALF_UP).floatValue());
								tempMoney = new BigDecimal(tempMoney).subtract(new BigDecimal(temp)).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
								studentExpense.setState(StudentExpense.STATE_YES);
								studentExpense.setClearTime(operateTime);
							} else {
								studentExpense.setPay(new BigDecimal(studentExpense.getPay()).add(new BigDecimal(tempMoney)).setScale(2, BigDecimal.ROUND_HALF_UP).floatValue());
								tempMoney = 0;
							}
						}
						if (pay > buy) {
							moreMoney = new BigDecimal(moreMoney).subtract(new BigDecimal(temp)).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
							studentExpense.setPay(new BigDecimal(studentExpense.getPay()).add(new BigDecimal(temp)).setScale(2, BigDecimal.ROUND_HALF_UP).floatValue());
						}
						if (pay != buy) {
							studentExpense.setOperator(userName);
							studentExpense.setOperateTime(operateTime);
							studentExpenseDao.update(studentExpense);
							//更新中心交费
							this.updateSpotExpenseOth3(student.getSpotCode(), studentExpense.getSemesterId(), moreMoney, studentExpense.getPay(), pay, buy, userName);
						}
					}
				}
				if (!isNowSemester) {
					//新增费用记录
					StudentExpense studentExpense = new StudentExpense();
					studentExpense.setStudentCode(studentExpensePay.getStudentCode());
					studentExpense.setBuy(0f);
					studentExpense.setPay(new BigDecimal(tempMoney).add(new BigDecimal(moreMoney)).setScale(2, BigDecimal.ROUND_HALF_UP).floatValue());
					studentExpense.setCreator(userName);
					studentExpense.setOperator(userName);
					studentExpense.setClearTime(operateTime);
					studentExpense.setSemesterId(semester.getId());
					//添加
					studentExpenseDao.save(studentExpense);
					//更新中心交费
					this.updateSpotExpenseOth4(student.getSpotCode(), semester.getId(), studentExpense.getPay(), studentExpense.getBuy(), userName);
				}

				//日志
				CenterPayLog centerPayLog = new CenterPayLog();
				centerPayLog.setSpotCode(loginName);
				centerPayLog.setDoPayDescription("学号:" + studentExpensePay.getStudentCode() + "支付" + studentExpensePay.getMoney() + "元" + ",学生交费");
				centerPayLog.setStuCode(studentExpensePay.getStudentCode());
				centerPayLogDAO.save(centerPayLog);

			}
        }
    }

	protected void updateSpotExpenseOth(String spotCode, long semesterId, float pay, String userName){
		Date operateTime = DateTools.getLongNowTime();
		List<SpotExpenseOth> spotExpenseOthList = spotExpenseOthDAO.querySpotExpenseOthBySemeter(semesterId, spotCode);
		if(null == spotExpenseOthList || 0 == spotExpenseOthList.size()){
			SpotExpenseOth spotExpenseOth = new SpotExpenseOth();
			spotExpenseOth.setPay(pay);
			spotExpenseOth.setBuy(0);
			spotExpenseOth.setStuAccTot(pay);
			spotExpenseOth.setStuOwnTot(0);
			spotExpenseOth.setCreator(userName);
			spotExpenseOth.setOperator(userName);
			spotExpenseOth.setSemesterId(semesterId);
			spotExpenseOth.setSpotCode(spotCode);
			spotExpenseOth.setState(0);
			spotExpenseOth.setClearTime(operateTime);
			spotExpenseOthDAO.save(spotExpenseOth);
		} else {
			SpotExpenseOth spotExpenseOth = spotExpenseOthList.get(0);
			spotExpenseOth.setPay(new BigDecimal(spotExpenseOth.getPay()).add(new BigDecimal(pay)).setScale(2, BigDecimal.ROUND_HALF_UP).floatValue());
			spotExpenseOth.setStuAccTot(new BigDecimal(spotExpenseOth.getStuAccTot()).add(new BigDecimal(pay)).setScale(2, BigDecimal.ROUND_HALF_UP).floatValue());
			spotExpenseOth.setOperator(userName);
			spotExpenseOth.setOperateTime(operateTime);
			spotExpenseOthDAO.update(spotExpenseOth);
		}
	}

	protected void updateSpotExpenseOth2(String spotCode, long semesterId, double moreMoney, double tempMoney, float pay, float buy, String userName){
		Date operateTime = DateTools.getLongNowTime();
		double oldTemp = new BigDecimal(pay).subtract(new BigDecimal(buy)).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
		double temp = new BigDecimal(moreMoney).add(new BigDecimal(tempMoney)).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
		List<SpotExpenseOth> spotExpenseOthList = spotExpenseOthDAO.querySpotExpenseOthBySemeter(semesterId, spotCode);
		SpotExpenseOth spotExpenseOth = spotExpenseOthList.get(0);
		spotExpenseOth.setPay(new BigDecimal(spotExpenseOth.getPay()).add(new BigDecimal(moreMoney)).add(new BigDecimal(tempMoney)).setScale(2, BigDecimal.ROUND_HALF_UP).floatValue());
		if(oldTemp >= 0){
			//以前有余额
			spotExpenseOth.setStuAccTot(new BigDecimal(spotExpenseOth.getStuAccTot()).add(new BigDecimal(moreMoney)).add(new BigDecimal(tempMoney)).setScale(2, BigDecimal.ROUND_HALF_UP).floatValue());
		}else{
			//以前有欠款, 本次缴费后没有欠款，产生余额
			if(oldTemp + temp >= 0){
				//说明还有余额
				spotExpenseOth.setStuOwnTot(new BigDecimal(spotExpenseOth.getStuOwnTot()).add(new BigDecimal(oldTemp)).setScale(2, BigDecimal.ROUND_HALF_UP).floatValue());
				spotExpenseOth.setStuAccTot(new BigDecimal(spotExpenseOth.getStuAccTot()).add(new BigDecimal(temp).add(new BigDecimal(oldTemp))).setScale(2, BigDecimal.ROUND_HALF_UP).floatValue());
			}else{
				//说明，抵消了一部分欠款，还有欠款
				spotExpenseOth.setStuOwnTot(new BigDecimal(spotExpenseOth.getStuOwnTot()).subtract(new BigDecimal(temp)).setScale(2, BigDecimal.ROUND_HALF_UP).floatValue());
			}
		}
		if(spotExpenseOth.getStuOwnTot() <= 0){
			spotExpenseOth.setState(0);
			spotExpenseOth.setClearTime(operateTime);
		}else{
			spotExpenseOth.setState(1);
			spotExpenseOth.setClearTime(null);
		}
		spotExpenseOth.setOperator(userName);
		spotExpenseOth.setOperateTime(operateTime);
		spotExpenseOthDAO.update(spotExpenseOth);
	}

	protected void updateSpotExpenseOth3(String spotCode, long semesterId, double moreMoney, double tempMoney, float pay, float buy, String userName){
		Date operateTime = DateTools.getLongNowTime();
		double oldTemp = new BigDecimal(pay).subtract(new BigDecimal(buy)).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
		double temp = new BigDecimal(moreMoney).add(new BigDecimal(tempMoney)).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
		List<SpotExpenseOth> spotExpenseOthList = spotExpenseOthDAO.querySpotExpenseOthBySemeter(semesterId, spotCode);
		SpotExpenseOth spotExpenseOth = spotExpenseOthList.get(0);
		if(oldTemp >= 0){
			//以前有余额,减掉多的余额
			spotExpenseOth.setStuAccTot(new BigDecimal(spotExpenseOth.getStuAccTot()).subtract(new BigDecimal(oldTemp)).setScale(2, BigDecimal.ROUND_HALF_UP).floatValue());
		}else{
			//以前有欠款, 本次缴费后抵消了欠款
			if (oldTemp + temp >= 0){
				//说明还有余额
				spotExpenseOth.setPay(new BigDecimal(spotExpenseOth.getPay()).subtract(new BigDecimal(oldTemp)).setScale(2, BigDecimal.ROUND_HALF_UP).floatValue());
				spotExpenseOth.setStuOwnTot(new BigDecimal(spotExpenseOth.getStuOwnTot()).add(new BigDecimal(oldTemp)).setScale(2, BigDecimal.ROUND_HALF_UP).floatValue());
			}else{
				//说明，抵消了一部分欠款，还有欠款
				spotExpenseOth.setPay(new BigDecimal(spotExpenseOth.getPay()).add(new BigDecimal(temp)).setScale(2, BigDecimal.ROUND_HALF_UP).floatValue());
				spotExpenseOth.setStuOwnTot(new BigDecimal(spotExpenseOth.getStuOwnTot()).subtract(new BigDecimal(temp)).setScale(2, BigDecimal.ROUND_HALF_UP).floatValue());
			}
		}
		if(spotExpenseOth.getStuOwnTot() <= 0){
			spotExpenseOth.setState(0);
			spotExpenseOth.setClearTime(operateTime);
		}else{
			spotExpenseOth.setState(1);
			spotExpenseOth.setClearTime(null);
		}
		spotExpenseOth.setOperator(userName);
		spotExpenseOth.setOperateTime(operateTime);
		spotExpenseOthDAO.update(spotExpenseOth);
	}

	protected void updateSpotExpenseOth4(String spotCode, long semesterId, float pay, float buy, String userName){
		Date operateTime = DateTools.getLongNowTime();
		List<SpotExpenseOth> spotExpenseOthList = spotExpenseOthDAO.querySpotExpenseOthBySemeter(semesterId, spotCode);
		if(null == spotExpenseOthList || 0 == spotExpenseOthList.size()){
			SpotExpenseOth spotExpenseOth = new SpotExpenseOth();
			spotExpenseOth.setPay(pay);
			spotExpenseOth.setBuy(buy);
			if(pay >= buy){
				spotExpenseOth.setStuAccTot(new BigDecimal(pay).subtract(new BigDecimal(buy)).setScale(2, BigDecimal.ROUND_HALF_UP).floatValue());
				spotExpenseOth.setStuOwnTot(0);
				spotExpenseOth.setState(0);
				spotExpenseOth.setClearTime(operateTime);
			}else{
				spotExpenseOth.setStuAccTot(0);
				spotExpenseOth.setStuOwnTot(new BigDecimal(buy).subtract(new BigDecimal(pay)).setScale(2, BigDecimal.ROUND_HALF_UP).floatValue());
				spotExpenseOth.setState(1);
			}
			spotExpenseOth.setCreator(userName);
			spotExpenseOth.setOperator(userName);
			spotExpenseOth.setSemesterId(semesterId);
			spotExpenseOth.setSpotCode(spotCode);
			spotExpenseOthDAO.save(spotExpenseOth);
		}else{
			SpotExpenseOth spotExpenseOth = spotExpenseOthList.get(0);
			spotExpenseOth.setPay(new BigDecimal(spotExpenseOth.getPay()).add(new BigDecimal(pay)).setScale(2, BigDecimal.ROUND_HALF_UP).floatValue());
			spotExpenseOth.setBuy(new BigDecimal(spotExpenseOth.getBuy()).add(new BigDecimal(buy)).setScale(2, BigDecimal.ROUND_HALF_UP).floatValue());
			if(pay > buy){
				spotExpenseOth.setStuAccTot(new BigDecimal(spotExpenseOth.getStuAccTot()).add(new BigDecimal(pay)).subtract(new BigDecimal(buy)).setScale(2, BigDecimal.ROUND_HALF_UP).floatValue());
			}else{
				spotExpenseOth.setStuOwnTot(new BigDecimal(spotExpenseOth.getStuOwnTot()).add(new BigDecimal(buy).subtract(new BigDecimal(pay))).setScale(2, BigDecimal.ROUND_HALF_UP).floatValue());
				if(spotExpenseOth.getStuOwnTot() > 0){
					spotExpenseOth.setState(1);
					spotExpenseOth.setClearTime(null);
				}
			}
			spotExpenseOth.setOperator(userName);
			spotExpenseOth.setOperateTime(operateTime);
			spotExpenseOthDAO.update(spotExpenseOth);
		}
	}
}
