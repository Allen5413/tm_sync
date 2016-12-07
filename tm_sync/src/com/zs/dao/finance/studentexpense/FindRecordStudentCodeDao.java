package com.zs.dao.finance.studentexpense;

import com.feinno.framework.common.dao.jpa.EntityJpaDao;
import com.zs.domain.finance.StudentExpense;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * 通过学生的学号查找学生费用信息
 * Created by LihongZhang on 2015/5/14.
 */
public interface FindRecordStudentCodeDao extends EntityJpaDao<StudentExpense,Long> {

    @Query("from StudentExpense where studentCode = ?1 order by semesterId ")
    public List<StudentExpense> getRecordByStuCode(String studentCode);

    @Query("from StudentExpense where studentCode = ?1 and semester_id = ?2")
    public StudentExpense getRecordByStuCode(String studentCode, long semesterId);

    @Query("from StudentExpense where semesterId = ?1")
    public List<StudentExpense> getRecordBySemesterId(long semesterId);
}
