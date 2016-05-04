package com.zs.dao.finance.studentexpense;

import com.feinno.framework.common.dao.jpa.EntityJpaDao;
import com.zs.domain.finance.StudentExpense;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * 用于不使用特殊查询的service
 * Created by LihongZhang on 2015/5/14.
 */
public interface StudentExpenseDao extends EntityJpaDao<StudentExpense,Long> {
	
	//@Query("select a from StudentExpense a where a.studentCode = ?1 and a.pay < a.buy order by a.semesterId asc")
	@Query("select a from StudentExpense a where a.studentCode = ?1 order by a.semesterId asc")
	public List<StudentExpense> queryStudentExpenseOnSemeter(String stuCode);
	
	@Query("select a from StudentExpense a,Semester b where a.studentCode = ?1 and a.semesterId = b.id and b.isNowSemester = 0")
	public StudentExpense queryLocalSemeterStuExpense(String stuCode);
}
